package com.rs.java.game.player.content;

import com.rs.core.cache.defintions.ItemDefinitions;
import com.rs.java.game.item.Item;
import com.rs.java.game.player.Player;
import com.rs.java.utils.Utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Buy-only custom shop using interface 1155.
 * Supports multi-tab shops by overriding onTabClicked().
 *
 * SETUP:
 *  1. Call shop.open(player) from an NPC dialogue or command.
 *  2. ButtonHandler already routes interface 1155 here — no changes needed.
 *  3. For Buy X: in your InputHandler/RunScript handler, add:
 *
 *       Integer customShopSlot = (Integer) player.temporaryAttribute().remove("CustomShopSlot");
 *       if (customShopSlot != null) {
 *           CustomShop cs = (CustomShop) player.temporaryAttribute().get("CustomShop");
 *           if (cs != null) cs.buy(player, customShopSlot, Math.max(1, Math.min(amount, 10000)));
 *           return;
 *       }
 */
public class CustomShop {

    public static final int INTERFACE_ID     = 1155;
    private static final int ITEMS_COMPONENT = 19;
    private static final int TITLE_COMPONENT = 15;
    private static final int CONTAINER_KEY   = 4;

    private final String name;
    private final int    money;
    private final Item[] stock;
    private final int[]  defaultQuantity;
    private final List<Player> viewers = new CopyOnWriteArrayList<>();

    public CustomShop(String name, int money, Item[] stock) {
        this.name  = name;
        this.money = money;
        this.stock = stock;
        this.defaultQuantity = new int[stock.length];
        for (int i = 0; i < stock.length; i++)
            defaultQuantity[i] = stock[i].getAmount();
    }

    // ── Open ─────────────────────────────────────────────────────────────────

    /** Opens the shop with tab 1 highlighted (button 4). */
    public void open(Player player) {
        open(player, 1);
    }

    /**
     * Opens the shop and highlights the correct tab.
     * tabArg = componentId - 3  (button 4 → arg 1, button 5 → arg 2, etc.)
     */
    public void open(Player player, int tabArg) {
        // Replace any previous session cleanly
        CustomShop previous = (CustomShop) player.temporaryAttribute().get("CustomShop");
        if (previous != null && previous != this)
            previous.viewers.remove(player);

        viewers.add(player);
        player.temporaryAttribute().put("CustomShop", this);

        player.setCloseInterfacesEvent(() -> {
            viewers.remove(player);
            player.temporaryAttribute().remove("CustomShop");
            player.temporaryAttribute().remove("CustomShopSlot");
        });

        // Vars — matches Matrix 718 Shop.addPlayer() buy-only style
        player.getPackets().sendVar(118, 4);
        player.getPackets().sendVar(1496, -1);   // -1 = buy only, no sell tab
        player.getPackets().sendVar(532, money);
        player.getPackets().sendVar(2565, 0);
        player.getPackets().sendVar(2563, 0);

        player.getPackets().sendGlobalVar(199, -1);
        for (int i = 0; i <= 40; i++)
            player.getPackets().sendGlobalVar(946 + i, 0);
        player.getPackets().sendGlobalVar(1241, 16750848);
        player.getPackets().sendGlobalVar(1242, 15439903);
        player.getPackets().sendGlobalVar(741, -1);
        player.getPackets().sendGlobalVar(743, -1);
        player.getPackets().sendGlobalVar(744, 0); // 0 = buy-only

        player.getInterfaceManager().sendInterface(INTERFACE_ID);
        player.getPackets().sendHideIComponent(INTERFACE_ID, 17, true);   // hide placeholder
        player.getPackets().sendHideIComponent(INTERFACE_ID, 18, false);  // show item panel
        player.getPackets().sendTextOnComponent(INTERFACE_ID, TITLE_COMPONENT, name);

        // Register item options BEFORE sending items
        // Button1=Value, Button2=Buy 1, Button3=Buy 5, Button4=Buy 10, Button5=Buy 50, Button6=Buy X
        player.getPackets().sendInterSetItemsOptionsScript(
                INTERFACE_ID, ITEMS_COMPONENT, CONTAINER_KEY, 10, 5,
                "Value", "Buy 1", "Buy 5", "Buy 10", "Buy 50", "Buy X");
        player.getPackets().sendComponentSettings(INTERFACE_ID, ITEMS_COMPONENT, 0, stock.length * 6, 1150);
        player.getPackets().sendRunScript(5397, stock.length >= 50 ? stock.length : 0);

        // Highlight the tab — ButtonHandler passes (componentId - 3) so we mirror that
        player.getPackets().sendRunScript(5398, tabArg);

        sendStore(player);
        sendInventory(player);
    }

    // ── Tab switching ─────────────────────────────────────────────────────────

    /**
     * Called by ButtonHandler when a tab button (componentId 4-14) is clicked.
     * tabIndex = componentId - 3.
     *
     * ButtonHandler already calls sendRunScript(5398, tabIndex) before this.
     * Override in a subclass to swap stock when the player clicks a different tab.
     *
     * Example:
     *   @Override
     *   public void onTabClicked(Player player, int tabIndex) {
     *       switch (tabIndex) {
     *           case 1: MyShops.GENERAL.open(player, 1); break;
     *           case 2: MyShops.DONATOR.open(player, 2); break;
     *           case 3: MyShops.SLAYER.open(player,  3); break;
     *       }
     *   }
     */
    public void onTabClicked(Player player, int tabIndex) {
        // no-op — override for multi-tab support
    }

    // ── Buy ──────────────────────────────────────────────────────────────────

    /**
     * Mirrors Matrix 718 Shop.buy() exactly:
     * pays from money pouch first, then inventory, then combined.
     */
    public void buy(Player player, int slot, int quantity) {
        if (slot < 0 || slot >= stock.length) return;
        Item item = stock[slot];
        if (item == null) return;

        if (quantity == 0) {
            player.getPackets().sendGameMessage("You must choose a quantity first.");
            return;
        }
        if (item.getAmount() == 0) {
            player.getPackets().sendGameMessage(item.getName() + " is out of stock.");
            return;
        }

        int price = item.getDefinitions().getPrice();
        if (price <= 0) {
            player.getPackets().sendGameMessage("This item is not for sale.");
            return;
        }

        if (quantity > item.getAmount())
            quantity = item.getAmount();

        int inventoryMoney = player.getInventory().getNumberOf(money);
        int pouchMoney     = player.getMoneyPouch().getTotal();
        int totalMoney     = inventoryMoney + pouchMoney;

        int totalCost   = 0;
        int buyMethod   = -1;  // 0=pouch, 1=inventory, 2=combined
        int leftOver    = 0;
        int totalBought = 0;
        boolean cantBuyAll = false;
        boolean outOfStock = false;
        boolean noSpace    = false;

        for (int i = 0; i < quantity; i++) {
            if (!player.getInventory().hasFreeSlots()
                    && !item.getDefinitions().isStackable()
                    && player.getInventory().containsOneItem(item.getId())) {
                noSpace = true;
                continue;
            }
            if (item.getAmount() == 0) {
                outOfStock = true;
                continue;
            }
            if ((totalCost > 0 ? pouchMoney - totalCost : pouchMoney) >= price) {
                totalCost += price;
                buyMethod  = 0;
                totalBought++;
                item.setAmount(item.getAmount() - 1);
            } else if ((totalCost > 0 ? inventoryMoney - totalCost : inventoryMoney) >= price) {
                totalCost += price;
                buyMethod  = 1;
                totalBought++;
                item.setAmount(item.getAmount() - 1);
            } else if ((totalCost > 0 ? totalMoney - totalCost : totalMoney) >= price) {
                totalCost += price;
                leftOver   = totalCost - pouchMoney;
                buyMethod  = 2;
                totalBought++;
                item.setAmount(item.getAmount() - 1);
            } else {
                cantBuyAll = true;
            }
        }

        if (buyMethod == 0) {
            player.getPackets().sendRunScript(5561, 0, totalCost);
            player.getMoneyPouch().takeMoneyFromPouch(totalCost);
        } else if (buyMethod == 1) {
            player.getInventory().deleteItem(money, totalCost);
        } else if (buyMethod == 2) {
            player.getPackets().sendRunScript(5561, 0, pouchMoney);
            player.getMoneyPouch().takeMoneyFromPouch(pouchMoney);
            player.getInventory().deleteItem(money, leftOver);
        }

        if (outOfStock)
            player.getPackets().sendGameMessage(item.getName() + " is out of stock.");
        if (cantBuyAll)
            player.getPackets().sendGameMessage("You don't have enough "
                    + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase()
                    + " to buy " + item.getName() + ".");
        if (noSpace)
            player.getPackets().sendGameMessage("You don't have enough inventory space.");

        if (totalBought > 0)
            player.getInventory().addItem(item.getId(), totalBought);

        refreshShop();
        sendInventory(player);
    }

    // ── Info ─────────────────────────────────────────────────────────────────

    public void sendInfo(Player player, int slot) {
        if (slot < 0 || slot >= stock.length) return;
        Item item = stock[slot];
        if (item == null) return;
        int price = item.getDefinitions().getPrice();
        if (price <= 0) {
            player.getPackets().sendGameMessage(item.getName() + ": shop will sell this item for free.");
            return;
        }
        player.getPackets().sendGameMessage(item.getName() + ": shop will sell for "
                + Utils.getFormattedNumber(price, ',') + " "
                + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
    }

    // ── Refresh / send ───────────────────────────────────────────────────────

    public void sendStore(Player player) {
        player.getPackets().sendItems(CONTAINER_KEY, stock);
    }

    public void sendInventory(Player player) {
        player.getInterfaceManager().sendInventoryInterface(1266);
        player.getPackets().sendItems(93, player.getInventory().getItems());
        // Button9 = examine in Matrix 718's 1266 handler
        player.getPackets().sendUnlockOptions(1266, 0, 0, 27, 8);
        player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Examine");
    }

    public void refreshShop() {
        for (Player viewer : viewers)
            sendStore(viewer);
    }

    public void restoreItems() {
        boolean needRefresh = false;
        for (int i = 0; i < stock.length; i++) {
            if (stock[i].getAmount() < defaultQuantity[i]) {
                stock[i].setAmount(stock[i].getAmount() + 1);
                needRefresh = true;
            }
        }
        if (needRefresh) refreshShop();
    }

    public String       getName()    { return name; }
    public int          getMoney()   { return money; }
    public Item[]       getStock()   { return stock; }
    public List<Player> getViewers() { return viewers; }
}
package com.rs.java.game.player.content.shops;

import com.rs.java.game.item.Item;
import com.rs.java.game.player.Player;
import com.rs.java.game.player.content.CustomShop;

/**
 * Central registry for all CustomShop instances.
 * Supports up to 11 tabs (interface buttons 4-14, tabIndex 1-11).
 *
 * USAGE:
 *   CustomShopHandler.open(player, CustomShopHandler.DONATOR_SHOP);
 *
 * ADDING A SHOP:
 *   1. Add a constant below
 *   2. Bump TOTAL_SHOPS
 *   3. Define it in initShops()
 *   4. Add a case in openTab()
 */
public class CustomShopHandler {

    // ── Shop ID constants (0-based, maps to tabIndex = shopId + 1) ───────────
    public static final int DONATOR_SHOP  = 0;  // tab 1, button 4
    public static final int SLAYER_SHOP   = 1;  // tab 2, button 5
    public static final int SKILLING_SHOP = 2;  // tab 3, button 6
    public static final int MAGIC_SHOP    = 3;  // tab 4, button 7
    public static final int RANGE_SHOP    = 4;  // tab 5, button 8
    public static final int MELEE_SHOP    = 5;  // tab 6, button 9
    public static final int COSMETIC_SHOP = 6;  // tab 7, button 10
    public static final int PET_SHOP      = 7;  // tab 8, button 11
    public static final int MISC_SHOP     = 8;  // tab 9, button 12
    public static final int VOTE_SHOP     = 9;  // tab 10, button 13
    public static final int PREMIUM_SHOP  = 10; // tab 11, button 14

    private static final int TOTAL_SHOPS  = 11;

    private static CustomShop[] shops;

    // ── Init ─────────────────────────────────────────────────────────────────

    public static void init() {
        shops = new CustomShop[TOTAL_SHOPS];
        initShops();
    }

    private static void initShops() {

        shops[DONATOR_SHOP] = new CustomShop("Donator Shop", 995, new Item[]{
                new Item(13740, 5),
                new Item(13742, 5),
                new Item(13738, 5),
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[SLAYER_SHOP] = new CustomShop("Slayer Shop", 995, new Item[]{
                new Item(13263, 50),
                new Item(4151,  20),
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[SKILLING_SHOP] = new CustomShop("Skilling Shop", 995, new Item[]{
                new Item(5733,  500),
                new Item(11920, 100),
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[MAGIC_SHOP] = new CustomShop("Magic Shop", 995, new Item[]{
                new Item(3054, 100),  // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[RANGE_SHOP] = new CustomShop("Range Shop", 995, new Item[]{
                new Item(11235, 10),  // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[MELEE_SHOP] = new CustomShop("Melee Shop", 995, new Item[]{
                new Item(4151, 10),   // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[COSMETIC_SHOP] = new CustomShop("Cosmetic Shop", 995, new Item[]{
                new Item(1038, 10),   // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[PET_SHOP] = new CustomShop("Pet Shop", 995, new Item[]{
                new Item(12694, 5),   // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[MISC_SHOP] = new CustomShop("Misc Shop", 995, new Item[]{
                new Item(995, 1000),  // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[VOTE_SHOP] = new CustomShop("Vote Shop", 995, new Item[]{
                new Item(995, 1000),  // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };

        shops[PREMIUM_SHOP] = new CustomShop("Premium Shop", 995, new Item[]{
                new Item(995, 1000),  // add your items
        }) {
            @Override
            public void onTabClicked(Player player, int tabIndex) {
                openTab(player, tabIndex);
            }
        };
    }

    // ── Tab routing ───────────────────────────────────────────────────────────

    /**
     * tabIndex = componentId - 3
     * button 4 = index 1 ... button 14 = index 11
     */
    private static void openTab(Player player, int tabIndex) {
        switch (tabIndex) {
            case 1:  open(player, DONATOR_SHOP);  break;
            case 2:  open(player, SLAYER_SHOP);   break;
            case 3:  open(player, SKILLING_SHOP); break;
            case 4:  open(player, MAGIC_SHOP);    break;
            case 5:  open(player, RANGE_SHOP);    break;
            case 6:  open(player, MELEE_SHOP);    break;
            case 7:  open(player, COSMETIC_SHOP); break;
            case 8:  open(player, PET_SHOP);      break;
            case 9:  open(player, MISC_SHOP);     break;
            case 10: open(player, VOTE_SHOP);     break;
            case 11: open(player, PREMIUM_SHOP);  break;
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public static void open(Player player, int shopId) {
        if (shops == null) {
            System.err.println("[CustomShopHandler] Not initialized! Call CustomShopHandler.init() on startup.");
            return;
        }
        if (shopId < 0 || shopId >= shops.length || shops[shopId] == null) {
            System.err.println("[CustomShopHandler] No shop registered for id: " + shopId);
            return;
        }
        shops[shopId].open(player, shopId + 1);
    }

    public static CustomShop getShop(int shopId) {
        return (shops != null && shopId >= 0 && shopId < shops.length) ? shops[shopId] : null;
    }

    public static void restoreAll() {
        if (shops == null) return;
        for (CustomShop shop : shops)
            if (shop != null) shop.restoreItems();
    }
}
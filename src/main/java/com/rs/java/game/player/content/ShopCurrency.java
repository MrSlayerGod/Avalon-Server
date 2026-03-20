package com.rs.java.game.player.content;

import com.rs.java.game.player.Player;

public enum ShopCurrency implements CurrencyPalette {
    COINS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id()) + player.getMoneyPouch().getTotal();
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 995;
        }

        /** Drains the money pouch first, then falls back to inventory coins. */
        @Override
        public void remove(final Player player, final int amount) {
            final int pouch = player.getMoneyPouch().getTotal();
            if (amount <= pouch) {
                player.getPackets().sendRunScript(5561, 0, amount);
                player.getMoneyPouch().takeMoneyFromPouch(amount);
            } else {
                if (pouch > 0) {
                    player.getPackets().sendRunScript(5561, 0, pouch);
                    player.getMoneyPouch().takeMoneyFromPouch(pouch);
                }
                player.getInventory().deleteItem(id(), amount - pouch);
            }
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getMoneyPouch().addMoney(amount, false);
        }
    },
    TOKKUL {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 6529;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }
    },
    MARK_OF_GRACE {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 11849;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "marks of grace";
        }
    },
    RUSTY_COINS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 18201;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "rusty coins";
        }
    },
    ARCHERY_TICKETS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 1464;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "archery tickets";
        }
    },
    TRADING_STICKS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 6306;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "trading sticks";
        }
    },
    ECTO_TOKENS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 4278;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "ecto-tokens";
        }
    },
    PLATINUM_TOKENS {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 13204;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }

        @Override
        public String toString() {
            return "platinum tokens";
        }
    },
    STARDUST {
        @Override
        public int getAmount(final Player player) {
            return player.getInventory().getNumberOf(id());
        }

        @Override
        public boolean isStackable() {
            return true;
        }

        @Override
        public boolean isPhysical() {
            return true;
        }

        @Override
        public int id() {
            return 25527;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.getInventory().deleteItem(id(), amount);
        }

        @Override
        public void add(final Player player, final int amount) {
            player.getInventory().addItem(id(), amount);
        }
    },
    AVALON_POINTS {
        @Override
        public int getAmount(final Player player) {
            return player.getAvalonPoints();
        }

        @Override
        public boolean isStackable() {
            return false;
        }

        @Override
        public boolean isPhysical() {
            return false;
        }

        @Override
        public int id() {
            return -1;
        }

        @Override
        public void remove(final Player player, final int amount) {
            player.setAvalonPoints(Math.max(0, player.getAvalonPoints() - amount));
        }

        @Override
        public void add(final Player player, final int amount) {
            player.setAvalonPoints(Math.min(player.getAvalonPoints() + amount, Integer.MAX_VALUE));
        }

        @Override
        public String toString() {
            return "avalon points";
        }
    };

    private final String formattedString = name().toLowerCase();

    @Override
    public String toString() {
        return formattedString;
    }

    /**
     * Returns the {@link ShopCurrency} whose {@link #id()} matches {@code itemId},
     * falling back to {@link #COINS} when none match (used by the legacy packed loader).
     */
    public static ShopCurrency fromItemId(final int itemId) {
        for (final ShopCurrency c : values()) {
            if (c.id() == itemId) return c;
        }
        return COINS;
    }
}

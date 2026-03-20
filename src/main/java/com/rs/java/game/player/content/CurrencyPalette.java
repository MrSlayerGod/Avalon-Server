package com.rs.java.game.player.content;

import com.rs.java.game.player.Player;

interface CurrencyPalette {

    int getAmount(final Player player);

    boolean isStackable();

    boolean isPhysical();

    int id();

    default int getMaximumAmount() {
        return Integer.MAX_VALUE;
    }

    void remove(final Player player, final int amount);

    void add(final Player player, final int amount);

}

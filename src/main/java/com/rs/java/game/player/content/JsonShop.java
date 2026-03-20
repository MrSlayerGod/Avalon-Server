package com.rs.java.game.player.content;

import java.util.List;

/**
 * Raw structure that Gson maps each {@code data/shops/*.json} file to.
 * Fields use the enum types directly; Gson resolves them by enum name.
 */
public class JsonShop {

    private final int shopUID;
    private final String shopName;
    private final ShopCurrency currency;
    private final ShopPolicy sellPolicy;
    private final float sellMultiplier;
    private final List<Item> items;

    // -------------------------------------------------------------------------

    public static class Item {
        public int     id;
        public int     amount;
        public int     sellPrice;
        public int     buyPrice;
        public int     restockTimer;
        public boolean ironmanRestricted;
    }

    // -------------------------------------------------------------------------

    public JsonShop(final int shopUID, final String shopName, final ShopCurrency currency,
                    final ShopPolicy sellPolicy, final float sellMultiplier, final List<Item> items) {
        this.shopUID        = shopUID;
        this.shopName       = shopName;
        this.currency       = currency;
        this.sellPolicy     = sellPolicy;
        this.sellMultiplier = sellMultiplier;
        this.items          = items;
    }

    public int getShopUID() {
        return shopUID;
    }

    public String getShopName() {
        return shopName;
    }

    public ShopCurrency getCurrency() {
        return currency;
    }

    public ShopPolicy getSellPolicy() {
        return sellPolicy;
    }

    public float getSellMultiplier() {
        return sellMultiplier;
    }

    public List<Item> getItems() {
        return items;
    }

    // -------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JsonShop)) return false;
        final JsonShop other = (JsonShop) o;
        if (!other.canEqual(this)) return false;
        if (this.getShopUID() != other.getShopUID()) return false;
        if (Float.compare(this.getSellMultiplier(), other.getSellMultiplier()) != 0) return false;
        final Object thisName = this.getShopName(), otherName = other.getShopName();
        if (thisName == null ? otherName != null : !thisName.equals(otherName)) return false;
        if (this.getCurrency() != other.getCurrency()) return false;
        if (this.getSellPolicy() != other.getSellPolicy()) return false;
        final Object thisItems = this.getItems(), otherItems = other.getItems();
        return thisItems == null ? otherItems == null : thisItems.equals(otherItems);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JsonShop;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getShopUID();
        result = result * PRIME + Float.floatToIntBits(this.getSellMultiplier());
        final Object $name = this.getShopName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + (currency == null ? 43 : currency.hashCode());
        result = result * PRIME + (sellPolicy == null ? 43 : sellPolicy.hashCode());
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "JsonShop(shopUID=" + shopUID + ", shopName=" + shopName
                + ", currency=" + currency + ", sellPolicy=" + sellPolicy
                + ", sellMultiplier=" + sellMultiplier + ", items=" + items + ")";
    }
}

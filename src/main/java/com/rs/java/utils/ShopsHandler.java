package com.rs.java.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.rs.java.game.item.Item;
import com.rs.java.game.player.Player;
import com.rs.java.game.player.content.JsonShop;
import com.rs.java.game.player.content.Shop;

public class ShopsHandler {

	private static final HashMap<Integer, Shop> handledShops = new HashMap<>();

	private static final String JSON_SHOPS_PATH = "data/shops/";

	public static void init() {
		File dir = new File(JSON_SHOPS_PATH);
		if (!dir.exists() || !dir.isDirectory()) {
			Logger.log("ShopsHandler", "Shop directory not found: " + JSON_SHOPS_PATH);
			return;
		}
		File[] files = dir.listFiles(f -> f.getName().endsWith(".json"));
		if (files == null)
			return;
		Gson gson = new Gson();
		int count = 0;
		for (File file : files) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				JsonShop def = gson.fromJson(reader, JsonShop.class);
				if (def == null || def.getShopName() == null)
					continue;
				int size = def.getItems().size();
				Item[]    items              = new Item[size];
				int[]     buyPrices          = new int[size];
				int[]     sellPrices         = new int[size];
				int[]     restockTimers      = new int[size];
				boolean[] ironmanRestricted  = new boolean[size];
				for (int i = 0; i < size; i++) {
					JsonShop.Item ji     = def.getItems().get(i);
					items[i]             = new Item(ji.id, ji.amount);
					buyPrices[i]         = ji.buyPrice;
					sellPrices[i]        = ji.sellPrice;
					restockTimers[i]     = ji.restockTimer;
					ironmanRestricted[i] = ji.ironmanRestricted;
				}
				addShop(def.getShopUID(), new Shop(
						def.getShopName(), def.getCurrency(), items, def.getSellPolicy(),
						buyPrices, sellPrices, restockTimers, ironmanRestricted));
				count++;
			} catch (Exception e) {
				Logger.log("ShopsHandler", "Failed to load shop from " + file.getName() + ": " + e.getMessage());
			}
		}
		Logger.log("ShopsHandler", "Loaded " + count + " shops from " + JSON_SHOPS_PATH);
	}

	public static void restoreShops() {
		for (Shop shop : handledShops.values())
			shop.restoreItems();
	}

	public static void degradeShops() {
		for (Shop shop : handledShops.values())
			shop.degradeItems();
	}

	public static boolean openShop(Player player, int key) {
		Shop shop = getShop(key);
		if (shop == null)
			return false;
		shop.addPlayer(player, key);
		return true;
	}

	public static Shop getShop(int key) {
		return handledShops.get(key);
	}

	public static void addShop(int key, Shop shop) {
		handledShops.put(key, shop);
	}
}

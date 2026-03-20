"""
Converts data/items/unpackedShops.txt into individual JSON files
under data/shops/, one file per shop.

Format of each line in unpackedShops.txt:
  key money generalStore - Shop Name - itemId qty [itemId qty ...]

Run from the project root:
  python tools/convert_shops.py
"""

import os
import json

# --------------------------------------------------------------------------- #
# Map the money item-ID to the ShopCurrency enum name used in the JSON
# --------------------------------------------------------------------------- #
MONEY_TO_CURRENCY = {
    995:   "COINS",
    6529:  "TOKKUL",
    11849: "MARK_OF_GRACE",
    18201: "RUSTY_COINS",
    1464:  "ARCHERY_TICKETS",
    6306:  "TRADING_STICKS",
    4278:  "ECTO_TOKENS",
    13204: "PLATINUM_TOKENS",
    25527: "STARDUST",
}

INPUT_FILE  = "data/items/unpackedShops.txt"
OUTPUT_DIR  = "data/shops"

os.makedirs(OUTPUT_DIR, exist_ok=True)

converted = 0
skipped   = 0

with open(INPUT_FILE, "r", encoding="utf-8") as f:
    for raw_line in f:
        line = raw_line.strip()

        # Skip blank lines and comments
        if not line or line.startswith("//"):
            continue

        # Split into exactly 3 parts: header, name, items
        parts = line.split(" - ", 2)
        if len(parts) != 3:
            print(f"  SKIP (bad format): {line}")
            skipped += 1
            continue

        header_parts = parts[0].strip().split()
        shop_name    = parts[1].strip()
        item_tokens  = parts[2].strip().split()

        if len(header_parts) < 3:
            print(f"  SKIP (bad header): {line}")
            skipped += 1
            continue

        shop_uid      = int(header_parts[0])
        money_id      = int(header_parts[1])
        general_store = header_parts[2].lower() == "true"

        currency    = MONEY_TO_CURRENCY.get(money_id, "COINS")
        sell_policy = "CAN_SELL" if general_store else "STOCK_ONLY"

        # Parse item pairs
        items = []
        i = 0
        while i + 1 < len(item_tokens):
            items.append({
                "id":                int(item_tokens[i]),
                "amount":            int(item_tokens[i + 1]),
                "sellPrice":         0,
                "buyPrice":          0,
                "restockTimer":      0,
                "ironmanRestricted": False
            })
            i += 2

        shop = {
            "shopUID":       shop_uid,
            "shopName":      shop_name,
            "currency":      currency,
            "sellPolicy":    sell_policy,
            "sellMultiplier": 0.6,
            "items":         items
        }

        # Use the shop name as the filename (sanitise special chars)
        safe_name = shop_name.replace("/", "_").replace("\\", "_")
        out_path  = os.path.join(OUTPUT_DIR, f"{safe_name}.json")

        with open(out_path, "w", encoding="utf-8") as out:
            json.dump(shop, out, indent=2)

        print(f"  [{shop_uid:>3}] {shop_name}  ->  {safe_name}.json")
        converted += 1

print(f"\nDone. {converted} shops converted, {skipped} skipped.")
print(f"Output: {os.path.abspath(OUTPUT_DIR)}")

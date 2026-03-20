package com.rs.java.tools;

import com.displee.cache.CacheLibrary;
import com.displee.cache.index.Index;
import com.rs.java.utils.MapArchiveKeys;

import java.io.IOException;

/**
 * Bulk map packer — copies all map regions from a source cache (e.g. 718/727)
 * into the server's destination cache.
 *
 * Steps to use:
 *  1. Set SOURCE_CACHE to the path of your 718 or 727 cache folder.
 *  2. Set DEST_CACHE   to the path of your server cache folder (data/cache/).
 *  3. Run main().  All regions present in the source will be packed.
 *
 * XTEA keys are loaded from data/map/archiveKeys/ and used for both
 * decrypting land files from the source and re-encrypting them in the dest.
 * This works when source and dest share the same key set (same revision).
 */
public class MapPacker {

    private static final int MAPS_INDEX = 5;

    /** Path to your 718 / 727 source cache. Change this before running. */
    private static final String SOURCE_CACHE = "D:\\cache-runescape-live-en-b634-2011-01-22-00-00-00-openrs2#1204\\cache\\";

    /** Path to your server's cache that will be updated. */
    private static final String DEST_CACHE = "data/cache/";

    /** Max region coordinate (RS uses 0-255 for both X and Y). */
    private static final int MAX_REGION = 256;

    public static void main(String[] args) throws IOException {
        System.out.println("Loading XTEA keys...");
        MapArchiveKeys.init();

        System.out.println("Opening source cache: " + SOURCE_CACHE);
        CacheLibrary source = new CacheLibrary(SOURCE_CACHE, false, null);

        System.out.println("Opening dest cache:   " + DEST_CACHE);
        CacheLibrary dest = new CacheLibrary(DEST_CACHE, false, null);

        Index srcIndex = source.index(MAPS_INDEX);

        int packed  = 0;
        int skipped = 0;
        int errors  = 0;

        for (int regionX = 0; regionX < MAX_REGION; regionX++) {
            for (int regionY = 0; regionY < MAX_REGION; regionY++) {
                String mapName  = "m" + regionX + "_" + regionY;
                String landName = "l" + regionX + "_" + regionY;

                int mapArchiveId  = srcIndex.archiveId(mapName);
                int landArchiveId = srcIndex.archiveId(landName);

                if (mapArchiveId == -1) {
                    // Region does not exist in source cache — skip silently.
                    continue;
                }

                int   regionId = (regionX << 8) | regionY;
                int[] keys     = MapArchiveKeys.getMapKeys(regionId);
                if (keys == null) {
                    keys = new int[]{0, 0, 0, 0};
                }

                try {
                    byte[] mapData = source.data(MAPS_INDEX, mapArchiveId);
                    if (mapData == null) {
                        System.out.println("SKIP " + mapName + " — null terrain data");
                        skipped++;
                        continue;
                    }

                    dest.put(MAPS_INDEX, mapName, mapData);

                    if (landArchiveId != -1) {
                        byte[] landData = source.data(MAPS_INDEX, landArchiveId, 0, keys);
                        if (landData != null) {
                            dest.put(MAPS_INDEX, landName, landData, keys);
                        } else {
                            System.out.println("WARN " + landName + " — null land data (bad XTEA keys?)");
                        }
                    }

                    packed++;
                    if (packed % 100 == 0) {
                        System.out.println("Packed " + packed + " regions so far...");
                    }

                } catch (Exception e) {
                    System.err.println("ERROR packing region " + regionX + "," + regionY + ": " + e.getMessage());
                    errors++;
                }
            }
        }

        System.out.println("Saving destination cache...");
        dest.update();

        System.out.println("=== Done ===");
        System.out.println("Packed:  " + packed);
        System.out.println("Skipped: " + skipped);
        System.out.println("Errors:  " + errors);

        source.close();
        dest.close();
    }
}

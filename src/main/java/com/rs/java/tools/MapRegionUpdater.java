package com.rs.java.tools;

import com.alex.store.Index;
import com.alex.store.Store;
import com.rs.Settings;
import com.rs.java.utils.MapArchiveKeys;

import java.util.Arrays;

/**
 * @author _jordan <jordan.abraham1997@gmail.com>
 *
 * Created on May 22, 2018
 */
public class MapRegionUpdater {

	public static final int VERSION = 634;// we can pull maps from other revisions.

	public static final int[] REGIONS = new int[] { 12598 };

	public static void main(String[] args) {
		boolean result;
		try {
			MapArchiveKeys.init();

			//Store fromCache = new Store(System.getProperty("user.home") + "/Dropbox/Maps/");


			Store fromCache = new Store("D:\\cache-runescape-live-en-b634-2011-01-22-00-00-00-openrs2#1204\\cache\\");
			Store toCache = new Store(Settings.CACHE_PATH);
			Index toIndex = toCache.getIndexes()[5];
			Index fromIndex = fromCache.getIndexes()[5];

			for (int regionId : REGIONS) {
				int regionX = (regionId >> 8) * 64;
				int regionY = (regionId & 0xff) * 64;
				String name;

				name = "m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				byte[] data = fromIndex.getFile(fromIndex.getArchiveId(name));
				/*if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name));
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				name = "um" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name));
				if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name));
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}*/

				int[] xteas = MapArchiveKeys.getMapKeys(regionId);

				name = "l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0, xteas);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}

				/*name = "ul" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0, xteas);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}*/

				/*name = "m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8);
				data = fromIndex.getFile(fromIndex.getArchiveId(name), 0);
				if (data == null)
					data = toIndex.getFile(toIndex.getArchiveId(name), 0);
				if (data != null) {
					result = addMapFile(toIndex, name, data);
					System.out.println(name + ", " + result);
				}*/

				toIndex.rewriteTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Replaced region(s): " + Arrays.toString(REGIONS));
	}

	private static boolean addMapFile(Index index, String name, byte[] data) {
		int archiveId = index.getArchiveId(name);
		if (archiveId == -1)
			archiveId = index.getTable().getValidArchiveIds().length;
		return index.putFile(archiveId, 0, 2, data, null, false, false, name.toLowerCase().hashCode(), -1);
	}

}
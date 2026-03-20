package com.rs.java.tools;


import com.rs.core.cache.Cache;

import java.io.FileOutputStream;


public class DumpCS2 {

	public static void main(String[] args) throws Throwable {
		Cache.init();
		for (int i = 0; i < 20000; i++) {
			if (!Cache.STORE.getIndexes()[12].fileExists(i, 0))
				continue;
			byte[] data = Cache.STORE.getIndexes()[12].getFile(i, 0);
			FileOutputStream fos = new FileOutputStream(
					"dump/cs2/" + i);
			fos.write(data);
			fos.close();
		}
	}
}
/**
 * 
 */
package com.rs.java.tools.osrs;

import com.alex.utils.Constants;
import com.rs.core.cache.Cache;
import com.rs.java.utils.Utils;


import java.io.IOException;

/**
 * @author dragonkk(Alex)
 * Sep 5, 2017
 */
public class DeleteFakeItems {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Cache.init();
		for (int i = 25354; i < Utils.getItemDefinitionsSize(); i++) {
			Cache.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].removeFile(i >>> 8, 0xff & i);
			System.out.println("removed "+i);
		}
		Cache.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].rewriteTable();
	}
	
/*	public int getArchiveId() {
		return getId() >>> 8;
	}

	public int getFileId() {
		return 0xff & getId();
	}*/
}

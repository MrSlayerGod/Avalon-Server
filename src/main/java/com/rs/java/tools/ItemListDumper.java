package com.rs.java.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.defintions.ItemDefinitions;
import com.rs.java.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class ItemListDumper {

	public static void main(String[] args) {
		try {
			new ItemListDumper();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ItemListDumper() throws IOException {
		Cache.init();
		File file = new File("itemList.txt"); //= new File("information/itemlist.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		//writer.append("//Version = 709\n");
		writer.append("//Version = 718\n");
		writer.flush();
		for (int id = 0; id < Utils.getItemDefinitionsSize(); id++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
			if (def.getName().equals("null"))
				continue;
			writer.append(id+" - "+def.getName());
			writer.newLine();
			writer.flush();
		}
		writer.close();
	}

	  public static int convertInt(String str) {
	    try {
	      int i = Integer.parseInt(str);
	      return i; } catch (NumberFormatException e) {
	    }
	    return 0;
	  }

}

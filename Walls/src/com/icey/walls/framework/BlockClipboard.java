package com.icey.walls.framework;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BlockClipboard {
	
	private ArrayList<SavedBlockInfo> blockList;
	
	
	public BlockClipboard() {
		blockList = new ArrayList<SavedBlockInfo>();
	}
	
	public void addBlock(Block block) {
		if (block.getState() instanceof InventoryHolder) {
			InventoryHolder inventory = (InventoryHolder) block.getState();
			blockList.add(new SavedBlockInfo(block, block.getType(), block.getData(), inventory.getInventory(), inventory.getInventory().getContents()));
		}
		blockList.add(new SavedBlockInfo(block, block.getType(), block.getData()));
	}
	
	public void pasteBlocksInClipboard() {
		for (int i = 0; i < getBlockList().size(); i++) {
			blockList.get(i).getBlock().setType(blockList.get(i).getMaterial());
			blockList.get(i).getBlock().setData(blockList.get(i).getBlockData());
			if (blockList.get(i).isContainer()) {
				InventoryHolder inv = (InventoryHolder) blockList.get(i).getBlock().getState();
				inv.getInventory().setContents(blockList.get(i).getItemStack());
			}
		}
	}
	
	public int numBlocks() {
		return blockList.size();
	}
	
	public void clear() {
		blockList.clear();
	}
	
	public String listBlocksInClipboard() {
		String output = "";
		for (int i = 0; i < blockList.size(); i++) {
			output += (i+1) + ": " + blockList.get(i).getBlock() + "\n";
		}
		return output;
	}
	
	public ArrayList<SavedBlockInfo> getBlockList() {
		return this.blockList;
	}
}

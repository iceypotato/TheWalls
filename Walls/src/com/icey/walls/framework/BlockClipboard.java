package com.icey.walls.framework;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BlockClipboard {
	
	private ArrayList<SavedBlockInfo> blockList;
	
	
	public BlockClipboard() {
		blockList = new ArrayList<SavedBlockInfo>();
	}
	
	public void addBlock(Block block) {
		if (block.getState() instanceof InventoryHolder) {
			InventoryHolder inventory = (InventoryHolder) block.getState();
			blockList.add(new SavedBlockInfo(block, block.getType(), block.getData(), block.getState(), inventory.getInventory(), inventory.getInventory().getContents()));
		}
		else {
			blockList.add(new SavedBlockInfo(block, block.getType(), block.getData(), block.getState()));
		}
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
	public void listItemStacks() {
	    for (SavedBlockInfo block : blockList) {
	        if (block.getBlockState() instanceof InventoryHolder) {
	            InventoryHolder inventory = (InventoryHolder) block.getBlockState();
	            for (ItemStack stack : inventory.getInventory().getContents()) {
	                Bukkit.broadcastMessage("Type: " + stack.getType().toString() + " Amount: " + stack.getAmount());
	            }
	        }
	    }
	}
	
	public ArrayList<SavedBlockInfo> getBlockList() {
		return this.blockList;
	}
}

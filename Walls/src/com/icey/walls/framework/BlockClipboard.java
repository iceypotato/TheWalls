package com.icey.walls;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

public class BlockClipboard {

	private ArrayList<Block> blockList;
	private ArrayList<Material> materialList;
	private ArrayList<BlockState> blockStates;
	private ArrayList<Byte> blockData;
	private ArrayList<MaterialData> materialDatas;
	private ArrayList<Inventory> blockInventories;
	
	
	public BlockClipboard() {
		blockList = new ArrayList<Block>();
		materialList = new ArrayList<Material>();
		blockStates = new ArrayList<BlockState>();
		blockData = new ArrayList<Byte>();
		materialDatas = new ArrayList<MaterialData>();
		blockInventories = new ArrayList<Inventory>();
	}
	
	public void addBlock(Block block) {
		for (int i = 0; i < blockList.size(); i++) {
			if (getBlock(i).getLocation().getBlockX() == block.getLocation().getBlockX() && getBlock(i).getLocation().getBlockY() == block.getLocation().getBlockY() && getBlock(i).getLocation().getBlockZ() == block.getLocation().getBlockZ()) {
				blockList.set(i, block);
				materialList.set(i, block.getType());
				blockStates.set(i, block.getState());
				blockData.set(i, block.getData());
				if (block.getState() instanceof InventoryHolder) {
					InventoryHolder container = (InventoryHolder) block.getState();
					blockInventories.set(i, container.getInventory());
					
				}
				else {
					blockInventories.set(i, null);
				}
				return;
			}
		}
		blockList.add(block);
		materialList.add(block.getType());
		blockStates.add(block.getState());
		blockData.add(block.getData());
		if (block.getState() instanceof InventoryHolder) {
			InventoryHolder container = (InventoryHolder) block.getState();
			blockInventories.add(container.getInventory());
		}
		else {
			blockInventories.add(null);
		}
	}
	
	public void pasteBlocksInClipboard() {
		for (int i = 0; i < getBlockList().size(); i++) {
			World world = ((CraftWorld) blockList.get(i).getWorld()).getHandle();
			world.getType(new BlockPosition(blockList.get(i).getX(), blockList.get(i).getY(), blockList.get(i).getZ()));
			getBlock(i).setType(getMaterial(i));
			getBlock(i).setData(getBlockData(i));
			if (getBlock(i).getState() instanceof InventoryHolder) {
				InventoryHolder protectedContainer = (InventoryHolder) getBlock(i).getState();
				protectedContainer.getInventory().setContents(getInventory(i).getContents());
			}
		}
	}
	
	public int numBlocks() {
		return blockList.size();
	}
	
	public Block getBlock(int i) {
		return blockList.get(i);
	}
	
	public Material getMaterial(int i) {
		return materialList.get(i);
	}
	
	public BlockState getBlockState(int i) {
		return blockStates.get(i);
	}
	public Inventory getInventory(int i) {
		return blockInventories.get(i);
	}
	
	public byte getBlockData(int i) {
		return blockData.get(i);
	}
	
	public void clear() {
		blockList.clear();
		materialList.clear();
		blockStates.clear();
		blockData.clear();
	}
	
	public String listBlocksInClipboard() {
		String output = "";
		for (int i = 0; i < blockList.size(); i++) {
			output += (i+1) + ": " + blockList.get(i).toString() + "\n";
		}
		return output;
	}
	
	public ArrayList<Block> getBlockList() {
		return this.blockList;
	}
	
	public void setBlockList(ArrayList<Block> blockList) {
		this.blockList = blockList;
	}
	public ArrayList<Material> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(ArrayList<Material> materialList) {
		this.materialList = materialList;
	}

	public ArrayList<BlockState> getBlockStates() {
		return blockStates;
	}

	public void setBlockStates(ArrayList<BlockState> blockStates) {
		this.blockStates = blockStates;
	}

	public ArrayList<Byte> getBlockData() {
		return blockData;
	}

	public void setBlockData(ArrayList<Byte> blockData) {
		this.blockData = blockData;
	}
	
}

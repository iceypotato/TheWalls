package com.icey.walls.util;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SavedBlockInfo {
	
	private Block block;
	private BlockState blockState;
	private MaterialData materialData;
	private Inventory blockInventory;
	private ItemStack[] itemStack;
	private String[] lines;
	private boolean isContainer;
	private boolean isSign;
	
	//Regular Block
	public SavedBlockInfo(Block block, BlockState blockState) {
		this.block = block;
		this.blockState = blockState;
	}
	
	//container
	public SavedBlockInfo(Block block, BlockState blockState, Inventory blockInventory, ItemStack[] itemStack) {
		this.block = block;
		this.blockState = blockState;
		this.blockInventory = blockInventory;
		this.itemStack = cloneItemStack(itemStack);
		this.isContainer = true;
	}
	
	//sign
	public SavedBlockInfo(Block block, BlockState blockState, String[] lines) {
		this.block = block;
		this.blockState = blockState;
		this.lines = cloneText(lines);
		this.isSign = true;
	}
	

	private ItemStack[] cloneItemStack(ItemStack[] input) {
		ItemStack[] newItemStack = new ItemStack[input.length];
		int i = 0;
		for (ItemStack itemstack : input) {
			if (itemstack != null) {
				newItemStack[i] = itemstack.clone();
			}
			i++;
		}
		return newItemStack;
	}
	private String[] cloneText(String[] input) {
		String[] newLines = new String[input.length];
		for (int i = 0; i < input.length; i++) {
			if (input[i] != null) {
				newLines[i] = input[i];
			}
		}
		return newLines;
	}

	public Inventory getBlockInventory() {
		return blockInventory;
	}

	public void setBlockInventory(Inventory blockInventory) {
		this.blockInventory = blockInventory;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public BlockState getBlockState() {
		return blockState;
	}

	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}

	public MaterialData getMaterialData() {
		return materialData;
	}

	public void setMaterialData(MaterialData materialData) {
		this.materialData = materialData;
	}

	public ItemStack[] getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack[] itemStack) {
		this.itemStack = itemStack;
	}

	public boolean isContainer() {
		return isContainer;
	}

	public void setContainer(boolean isContainer) {
		this.isContainer = isContainer;
	}

	public boolean isSign() {
		return isSign;
	}

	public void setSign(boolean isSign) {
		this.isSign = isSign;
	}

	public String[] getLines() {
		return lines;
	}

	public void setLines(String[] lines) {
		this.lines = lines;
	}
}

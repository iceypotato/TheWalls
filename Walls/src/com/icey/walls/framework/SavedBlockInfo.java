package com.icey.walls.framework;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SavedBlockInfo {
	
	private Block block;
	private Material material;
	private byte blockData;
	private BlockState blockState;
	private MaterialData materialData;
	private Inventory blockInventory;
	private ItemStack[] itemStack;
	private boolean isContainer;
	
	//Regular Block
	public SavedBlockInfo(Block block, Material material, byte blockData, BlockState blockState) {
		this.block = block;
		this.material = material;
		this.blockData = blockData;
		this.blockState = blockState;
	}
	
	//container
	public SavedBlockInfo(Block block, Material material, byte blockData, BlockState blockState, Inventory blockInventory, ItemStack[] itemStack) {
		this.block = block;
		this.material = material;
		this.blockData = blockData;
		this.blockState = blockState;
		this.blockInventory = blockInventory;
		this.itemStack = cloneItemStack(itemStack);
		this.isContainer = true;
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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public byte getBlockData() {
		return blockData;
	}

	public void setBlockData(byte blockData) {
		this.blockData = blockData;
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
}

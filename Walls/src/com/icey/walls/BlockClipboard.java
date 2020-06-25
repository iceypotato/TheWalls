package com.icey.walls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class BlockClipboard {

	private ArrayList<Block> blockList;
	private ArrayList<Material> materialList;
	private ArrayList<BlockState> blockStates;
	private ArrayList<Byte> blockData;
	
	public BlockClipboard() {
		blockList = new ArrayList<Block>();
		setMaterialList(new ArrayList<Material>());
		setBlockStates(new ArrayList<BlockState>());
	}
	
	public void addBlock(Block block) {
		blockList.add(block);
		materialList.add(block.getType());
		blockStates.add(block.getState());
		blockData.add(block.getData());
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
	
	public byte getBlockData(int i) {
		return blockData.get(i);
	}
	
	public void clear() {
		blockList.clear();
		materialList.clear();
		blockStates.clear();
		blockData.clear();
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

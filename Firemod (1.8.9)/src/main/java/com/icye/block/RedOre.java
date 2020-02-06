package com.icye.block;

import com.iceypotato.firemod.FireMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class RedOre extends Block {
	
	public static final String name = "RedOre";

	public RedOre(Material materialIn) {
		super(materialIn);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		this.blockMaterial(Material.ground);
		// TODO Auto-generated constructor stub
	}

	private void blockMaterial(Material ground) {
		// TODO Auto-generated method stub
		
	}

}

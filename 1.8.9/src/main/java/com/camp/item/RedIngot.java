package com.camp.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class RedIngot extends Item {
	
	public static final String name = "RedIngot";
	
	public RedIngot() {
		super();
		this.setUnlocalizedName(FireMod.MODID + "_" + this.name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setMaxStackSize(64);
	}
}

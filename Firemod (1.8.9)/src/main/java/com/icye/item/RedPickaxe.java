package com.icye.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;

public class RedPickaxe extends ItemPickaxe {
	
	public static final String name = "RedPickaxe";

	protected RedPickaxe(ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		this.setCreativeTab(CreativeTabs.tabTools);
		// TODO Auto-generated constructor stub
	}

}

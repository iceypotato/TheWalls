package com.icye.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class RedAxe extends ItemAxe {
	
	public static final String name = "RedAxe";

	protected RedAxe(ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		this.setCreativeTab(CreativeTabs.tabTools);
		// TODO Auto-generated constructor stub
	}

}

package com.icye.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class RedHoe extends ItemHoe {
	
	public static final String name = "redIngot";
	
	public RedHoe(ToolMaterial material) {
		super(material);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		// TODO Auto-generated constructor stub
	}

	

}

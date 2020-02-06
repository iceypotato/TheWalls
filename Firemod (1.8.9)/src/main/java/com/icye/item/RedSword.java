package com.icye.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class RedSword extends ItemSword {
	
	public static final String name = "RedSword";

	public RedSword(ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		this.setCreativeTab(CreativeTabs.tabCombat);
		// TODO Auto-generated constructor stub
	}

}

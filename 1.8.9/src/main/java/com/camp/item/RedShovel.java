package com.camp.item;

import com.iceypotato.firemod.FireMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

public class RedShovel extends ItemSpade {
	
	public static final String name = "RedShovel";

	public RedShovel(ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
		this.setCreativeTab(CreativeTabs.tabTools);
		// TODO Auto-generated constructor stub
	}

}

package com.icye.item;

import com.iceypotato.firemod.FireMod;
import com.icye.entities.WtfEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WTFEXE extends Item {
	
	public static final String name = "fucc";
	
	public WTFEXE() {
		this.setUnlocalizedName(FireMod.MODID + "_" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			world.spawnEntityInWorld(new WtfEntity(world, player));
		}
		return itemStack;
	}

}

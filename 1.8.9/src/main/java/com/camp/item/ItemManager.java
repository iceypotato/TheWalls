package com.camp.item;

import com.iceypotato.firemod.FireMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemManager {
	
	public static RedIngot redIngot;
	public static RedPickaxe redPickaxe;
	public static RedAxe redAxe;
	public static RedShovel redShovel;
	public static RedSword redSword;
	
	public static void mainRegistry() {
		initializeItem();
		registerItem();
	}

	public static void initializeItem() {
		redIngot = new RedIngot();
		
		//Items with durability
		redPickaxe = new RedPickaxe(FireMod.redMatter);
		redAxe = new RedAxe(FireMod.redMatter);
		redShovel = new RedShovel(FireMod.redMatter);
		redSword = new RedSword(FireMod.redMatter);
		
	}
	
	public static void registerItem() {
		GameRegistry.registerItem(redIngot, redIngot.name);
		GameRegistry.registerItem(redPickaxe, redPickaxe.name);
		GameRegistry.registerItem(redAxe, redAxe.name);
		GameRegistry.registerItem(redShovel, redShovel.name);
		GameRegistry.registerItem(redSword, redSword.name);
	}

}

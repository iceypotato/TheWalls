package com.icye.item;

import com.iceypotato.firemod.FireMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemManager {
	
	public static RedIngot redIngot;
	public static RedPickaxe redPickaxe;
	public static RedAxe redAxe;
	public static RedShovel redShovel;
	public static RedSword redSword;
	public static WTFEXE wtfexe;
	
	public static void mainRegistry() {
		initializeItem();
		registerItem();
	}

	public static void initializeItem() {
		redIngot = new RedIngot();
		wtfexe = new WTFEXE();
		//Items with durability
		redPickaxe = new RedPickaxe(FireMod.redMatter);
		redAxe = new RedAxe(FireMod.redMatter);
		redShovel = new RedShovel(FireMod.redMatter);
		redSword = new RedSword(FireMod.redMatter);
		
	}
	
	public static void registerItem() {
		GameRegistry.registerItem(redIngot, RedIngot.name);
		GameRegistry.registerItem(redPickaxe, RedPickaxe.name);
		GameRegistry.registerItem(redAxe, RedAxe.name);
		GameRegistry.registerItem(redShovel, RedShovel.name);
		GameRegistry.registerItem(redSword, RedSword.name);
		GameRegistry.registerItem(wtfexe, WTFEXE.name);
	}

}

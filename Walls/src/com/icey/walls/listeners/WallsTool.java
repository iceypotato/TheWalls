package com.icey.walls.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WallsTool implements Listener {
	
	public void giveTool(Player p) {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Walls Tool");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Left-Click to set the first corner of a region,");
		lore.add(ChatColor.DARK_PURPLE + "Right-Click to set the second corner of a region.");
		lore.add(ChatColor.DARK_PURPLE + "Left-Click to set the spawnpoint for lobbies or team spawns.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		p.getInventory().addItem(item);
	}
}

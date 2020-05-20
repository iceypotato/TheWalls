package com.icey.walls.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WallsTool implements Listener {
	
	ItemStack item;
	ItemMeta meta;
	Player player;
	
	public WallsTool() {
		item = new ItemStack(Material.BLAZE_ROD);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Walls Tool");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Left-Click to set the first corner of a region,");
		lore.add(ChatColor.DARK_PURPLE + "Right-Click to set the second corner of a region.");
		lore.add(ChatColor.DARK_PURPLE + "Left-Click to set the spawnpoint for lobbies or team spawns.");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public void giveTool(Player p) {
		p.getInventory().addItem(item);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		player = event.getPlayer();
		ItemStack item = player.getItemInHand();

		if (item.equals(this.item)) {
			Block block = event.getClickedBlock();
			if (block != null) {
				Player player = event.getPlayer();
				Location loc = block.getLocation();
				Action action = event.getAction();
				if (action == Action.LEFT_CLICK_BLOCK) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Region #1:");
					player.sendMessage(ChatColor.WHITE + "X: " + ChatColor.GREEN + loc.getBlockX());
					player.sendMessage(ChatColor.WHITE + "Y: " + ChatColor.GREEN + loc.getBlockY());
					player.sendMessage(ChatColor.WHITE + "Z: " + ChatColor.GREEN + loc.getBlockZ());
					event.setCancelled(true);
				}
				else if (action == Action.RIGHT_CLICK_BLOCK) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Region #2:");
					player.sendMessage(ChatColor.WHITE + "X: " + ChatColor.GREEN + loc.getBlockX());
					player.sendMessage(ChatColor.WHITE + "Y: " + ChatColor.GREEN + loc.getBlockY());
					player.sendMessage(ChatColor.WHITE + "Z: " + ChatColor.GREEN + loc.getBlockZ());
					event.setCancelled(true);
				}
			}
		}
	}
}

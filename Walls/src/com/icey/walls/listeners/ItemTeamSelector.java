package com.icey.walls.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.icey.walls.MainClass;
import com.icey.walls.framework.WallsArena;


public class ItemTeamSelector implements Listener {
	
	ItemStack itemStack;
	ItemMeta meta;
	WallsArena arena;
	List<UUID> teamToJoin;
	String message;
	World world;
	
	public ItemTeamSelector(MainClass plugin, ItemStack itemStack, String displayName, String clickMsg, WallsArena arena, List<UUID> teamToJoin) {
		this.itemStack = itemStack;
		this.arena = arena;
		this.teamToJoin = teamToJoin;
		this.message = clickMsg;
		meta = this.itemStack.getItemMeta();
		meta.setDisplayName(displayName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "Click to join this team.");
		meta.setLore(lore);
		this.itemStack.setItemMeta(meta);
	}
	
	public void giveItemToPlayer(Player p) {
		p.getInventory().addItem(itemStack);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand();
		if (itemInHand.equals(this.itemStack)) {
			arena.joinTeam(player.getUniqueId(), teamToJoin);
			player.sendMessage(ChatColor.GOLD + "You have joined team " + message);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		ItemStack droppedItem = event.getItemDrop().getItemStack();
		if (droppedItem.equals(this.itemStack)) {
			event.setCancelled(true);
		}
	}
}

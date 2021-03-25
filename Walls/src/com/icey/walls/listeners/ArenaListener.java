package com.icey.walls.listeners;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.icey.walls.framework.WallsArena;

	
public class ArenaListener implements Listener {
	
	private WallsArena arena;
	private ArrayList<Location> wallBlocks;
	private ArrayList<Location> buildRegionBlocks;
	private Location oldLocation;
	
	public ArenaListener(WallsArena arena, ArrayList<Location> wallBlocks, ArrayList<Location> buildRegionBlocks) {
		this.arena = arena;
		this.wallBlocks = wallBlocks;
		this.buildRegionBlocks = buildRegionBlocks;
	}
	
	@EventHandler
	public void playerDisconnect(PlayerQuitEvent quitEvent) {
		if ((arena.isInProgress() || arena.isWaiting()) && quitEvent.getPlayer() != null) arena.playerLeave(quitEvent.getPlayer());
	}
	
	@EventHandler
	public void waitingForPlayers(PlayerInteractEvent event) {
		if(arena.isWaiting() && arena.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void waitingForplayers(EntityDamageEvent event) {
		if(arena.isWaiting() && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (arena.getPlayersInGame().contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void preventBlockBreaking(BlockBreakEvent interact) {
		Player player = interact.getPlayer();
		boolean isBlockNotInRegion = true;
		boolean isBlockAWallBlock = false;
		if (arena.isInProgress()) {
			for (int i = 0; i < buildRegionBlocks.size(); i++) {
				if ((buildRegionBlocks.get(i).equals(interact.getBlock().getLocation()))) {
					isBlockNotInRegion = false;
				}
			}
			if (!arena.isWallsFall()) {
				for (int i = 0; i < wallBlocks.size(); i++) {
					if (interact.getBlock().getLocation().equals(wallBlocks.get(i))) {
						isBlockAWallBlock = true;
					}
				}
			}
			if (isBlockNotInRegion || isBlockAWallBlock) {
				player.sendMessage(ChatColor.RED + "You cannot break those blocks!");
				interact.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void preventBlockPlacing(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		boolean isBlockNotInRegion = true;
		boolean blockPlacedonWall = false;
		if (arena.isInProgress()) {
			for (int i = 0; i < buildRegionBlocks.size(); i++) {
				if ((buildRegionBlocks.get(i).equals(event.getBlock().getLocation()))) {
					isBlockNotInRegion = false;
				}
			}
		}
		if (!arena.isWallsFall() && arena.isInProgress()) {
			for (int i = 0; i < wallBlocks.size(); i++) {
				if (event.getBlock().getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && event.getBlock().getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) {
					blockPlacedonWall = true;
				}
			}
		}
		if (blockPlacedonWall || isBlockNotInRegion) {
			player.sendMessage(ChatColor.RED + "You cannot place those blocks there!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerDies(PlayerDeathEvent deathEvent) {
		arena.getTeamRed().remove(deathEvent.getEntity().getPlayer().getUniqueId());
		arena.getTeamGreen().remove(deathEvent.getEntity().getPlayer().getUniqueId());
		arena.getTeamBlue().remove(deathEvent.getEntity().getPlayer().getUniqueId());
		arena.getTeamYellow().remove(deathEvent.getEntity().getPlayer().getUniqueId());
		arena.updateScoreboard();
		if (arena.getPlayersInGame().contains(deathEvent.getEntity().getPlayer().getUniqueId())) {
			deathEvent.setDeathMessage("");
			//Run this if a player was killed by a player.
			Random rand = new Random();
			int msgID = rand.nextInt(11);
			String deathMsg = "", killee = deathEvent.getEntity().getPlayer().getDisplayName();
			String[] deathMsgs = new String[11];
			deathMsgs[0] = " was slain by ";
			deathMsgs[1] = " was 69ed by ";
			deathMsgs[2] = " was memed by ";
			deathMsgs[3] = " got epic gamer moved by ";
			deathMsgs[4] = " was isekaied to another world by ";
			deathMsgs[5] = " got rekt by ";
			deathMsgs[6] = " got w-tapped by ";
			deathMsgs[7] = " got destroyed by ";
			deathMsgs[8] = " got creeper aw maned by ";
			deathMsgs[9] = " got squashed by anime thighs by ";
			deathMsgs[10] = " lost all hp and fainted to ";
			if (deathEvent.getEntity().getPlayer().getKiller() != null) {
				String killer = deathEvent.getEntity().getKiller().getDisplayName();
				deathMsg = killee + ChatColor.GRAY+ deathMsgs[msgID] + ChatColor.RESET + killer;
			}
			//run this if player died of natural causes.
			else {
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) deathMsg = killee + ChatColor.GRAY + " fell in the void.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.LAVA) deathMsg = killee + ChatColor.GRAY + " thought he had the high ground and melted in lava.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.DROWNING) deathMsg = killee + ChatColor.GRAY + " forgot how to breathe.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE_TICK) deathMsg = killee + ChatColor.GRAY + " burnt to a crisp.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE) deathMsg = killee + ChatColor.GRAY + " played with fire.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FALL) deathMsg = killee + ChatColor.GRAY + " fell to a clumsy death.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.SUICIDE) deathMsg = killee + ChatColor.GRAY + " said goodbye, cruel world!";
				else deathMsg = killee + ChatColor.GRAY + " has been eliminated!";
			}
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage(deathMsg);}
		}
		arena.checkForRemainingTeams();
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent pRespawnEvent) {
		if (arena.getPlayersInGame().contains(pRespawnEvent.getPlayer().getUniqueId())) {
			pRespawnEvent.getPlayer().setGameMode(GameMode.SPECTATOR);
			pRespawnEvent.setRespawnLocation(arena.getConfig().getLobbySpawn());
			pRespawnEvent.getPlayer().teleport(arena.getConfig().getLobbySpawn());
			pRespawnEvent.getPlayer().sendTitle(ChatColor.RED + "You Died!", ChatColor.YELLOW+"Get gud.");
		}
	}
	
	@EventHandler
	public void preventCrossingWalls(PlayerMoveEvent pMoveEvent) {
		if (!arena.isWallsFall() && arena.isInProgress()) {
			for (int i = 0; i < wallBlocks.size(); i++) {
				if ((pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ()) ||
				(pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ())) {
					oldLocation = pMoveEvent.getPlayer().getLocation();
				}
				else if (wallBlocks.get(i).getBlockZ() == pMoveEvent.getPlayer().getLocation().getBlockZ() && wallBlocks.get(i).getBlockX() == pMoveEvent.getPlayer().getLocation().getBlockX()) {
					pMoveEvent.getPlayer().teleport(oldLocation);
					pMoveEvent.getPlayer().sendMessage(ChatColor.YELLOW + "What do you think you're doin");
				}
			}
		}
	}
}

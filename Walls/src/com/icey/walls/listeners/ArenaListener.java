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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.icey.walls.framework.Arena;

	
public class ArenaListener implements Listener {
	
	private Arena arena;
	private ArrayList<Location> wallBlocks;
	private ArrayList<Location> buildRegionBlocks;
	
	private Location oldLocation;
	
	public ArenaListener(Arena arena, ArrayList<Location> wallBlocks, ArrayList<Location> buildRegionBlocks) {
		this.arena = arena;
		this.wallBlocks = wallBlocks;
		this.buildRegionBlocks = buildRegionBlocks;
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent quitEvent) {
		if (arena.isInProgress() || arena.isWaiting()) arena.playerLeave(quitEvent.getPlayer());
	}
	
	@EventHandler
	public void waitingForPlayers(PlayerInteractEvent event) {
		if(arena.isWaiting() && arena.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void waitingForplayers(EntityDamageEvent event) {
		if(arena.isWaiting()&& event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (arena.getPlayersInGame().contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void preventBlockBreaking(BlockBreakEvent interact) {
		Player player = interact.getPlayer();
		if (interact.getBlock() != null) {
			boolean isBlockNotInRegion = true;
			boolean isBlockAWallBlock = false;
			if (arena.isInProgress()) {
				for (int i = 0; i < buildRegionBlocks.size(); i++) {
					if ((buildRegionBlocks.get(i).equals(interact.getBlock().getLocation()))) {
						isBlockNotInRegion = false;
						break;
					}
				}
				if (!arena.isWallsFall()) {
					for (int i = 0; i < wallBlocks.size(); i++) {
						if (interact.getBlock().getLocation().equals(wallBlocks.get(i))) {
							isBlockAWallBlock = true;
							break;
						}
					}
				}
				if (isBlockNotInRegion || isBlockAWallBlock) {
					player.sendMessage(ChatColor.RED + "You cannot break those blocks!");
					interact.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void preventBlockPlacing(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!arena.isWallsFall() && arena.isInProgress()) {
			boolean blockPlacedonWall = false;
			for (int i = 0; i < wallBlocks.size(); i++) {
				if (event.getBlock().getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && event.getBlock().getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) {
					blockPlacedonWall = true;
					break;
				}
			}
			if (blockPlacedonWall) {
				player.sendMessage(ChatColor.RED + "You cannot place those blocks there!");
				event.setCancelled(true);
			}
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
			String deathMsg = "", killer = deathEvent.getEntity().getKiller().getDisplayName(), killee = deathEvent.getEntity().getPlayer().getDisplayName();
			if (deathEvent.getEntity().getPlayer().getKiller() != null) {
				if (msgID == 0) deathMsg = killee + ChatColor.GRAY+" was slain by " + ChatColor.RESET + killer;
				if (msgID == 1) deathMsg = killee + ChatColor.GRAY+" was 69ed by " + ChatColor.RESET + killer;
				if (msgID == 2) deathMsg = killee + ChatColor.GRAY+" was memed by " + ChatColor.RESET + killer;
				if (msgID == 3) deathMsg = killee + ChatColor.GRAY+" got epic gamer moved by " + ChatColor.RESET + killer;
				if (msgID == 4) deathMsg = killee + ChatColor.GRAY+" was isekaied to another world by " + ChatColor.RESET + killer;
				if (msgID == 5) deathMsg = killee + ChatColor.GRAY+" got rekt by " + ChatColor.RESET + killer;
				if (msgID == 6) deathMsg = killee + ChatColor.GRAY+" got w-tapped by " + ChatColor.RESET + killer;
				if (msgID == 7) deathMsg = killee + ChatColor.GRAY+" got destroyed by " + ChatColor.RESET + killer;
				if (msgID == 8) deathMsg = killee + ChatColor.GRAY+" got creeper aw maned by " + ChatColor.RESET + killer;
				if (msgID == 9) deathMsg = killee + ChatColor.GRAY+" got squashed by anime thighs by " + ChatColor.RESET + killer;
				if (msgID == 10) deathMsg = killee + ChatColor.GRAY+" lost all hp and fainted to " + ChatColor.RESET + killer;
			}
			//run this if player died of natural causes.
			else {
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) deathMsg = killee + ChatColor.GRAY + " fell in the void.";
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.LAVA) deathMsg = killee + ChatColor.GRAY + " thought he had the high ground and melted in lava.";
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.DROWNING) deathMsg = killee + ChatColor.GRAY + " forgot how to breathe.";
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE_TICK) deathMsg = killee + ChatColor.GRAY + " burnt to a crisp.";
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE) deathMsg = killee + ChatColor.GRAY + " played with fire.";
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FALL) deathMsg = killee + ChatColor.GRAY + " fell to a clumsy death.";
			}
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage(deathMsg);}
		}
		if (arena.getTeamRed().size() == 0) {
			arena.setRemainingTeams(arena.getRemainingTeams()-1);
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.RED+"Red Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (arena.getTeamGreen().size() == 0) {
			arena.setRemainingTeams(arena.getRemainingTeams()-1);
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.GREEN+"Green Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (arena.getTeamBlue().size() == 0) {;
		arena.setRemainingTeams(arena.getRemainingTeams()-1);
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.BLUE+"Blue Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (arena.getTeamYellow().size() == 0) {
			arena.setRemainingTeams(arena.getRemainingTeams()-1);	
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.YELLOW+"Yellow Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent pRespawnEvent) {
		if (arena.getPlayersInGame().contains(pRespawnEvent.getPlayer().getUniqueId())) {
			pRespawnEvent.getPlayer().setGameMode(GameMode.SPECTATOR);
			pRespawnEvent.setRespawnLocation(arena.getLobbySpawn());
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

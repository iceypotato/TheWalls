package com.icey.walls.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.icey.walls.MainClass;
import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsArenaConfig;

	
public class ArenaListener implements Listener {
	
	private MainClass plugin;
	private WallsArena arena;
	private List<Location> wallBlocks;
	private List<Location> buildRegionBlocks;
	private HashMap<UUID, Location> oldLocations;
	private Location oldLocation;
	
	public ArenaListener(WallsArena arena) {
		this.arena = arena;
		this.plugin = arena.getPlugin();
		this.wallBlocks = arena.getWallBlocks();
		this.buildRegionBlocks = arena.getBuildRegionBlocks();
		oldLocations = new HashMap<>();
	}
	
	@EventHandler
	public void playerDisconnect(PlayerQuitEvent quitEvent) {
		if ((arena.isInProgress() || arena.isWaiting()) && quitEvent.getPlayer() != null) arena.playerLeave(quitEvent.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void waitingForPlayers(PlayerInteractEvent event) {
		if(arena.isWaiting() && arena.getPlayersInGame().contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerTakeNaturalDamage(EntityDamageEvent event) {
		if (arena.isWaiting() && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (arena.getPlayersInGame().contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void playerFriendlyFire(EntityDamageByEntityEvent event) {
		if (arena.isInProgress() && event.getEntity() instanceof Player) {
			Player defender = (Player) event.getEntity();
			Player attacker = (Player) event.getDamager();
			if (arena.getTeamRed().contains(defender.getUniqueId()) && arena.getTeamRed().contains(attacker.getUniqueId())) event.setCancelled(true);
			if (arena.getTeamGreen().contains(defender.getUniqueId()) && arena.getTeamGreen().contains(attacker.getUniqueId())) event.setCancelled(true);
			if (arena.getTeamBlue().contains(defender.getUniqueId()) && arena.getTeamBlue().contains(attacker.getUniqueId())) event.setCancelled(true);
			if (arena.getTeamYellow().contains(defender.getUniqueId()) && arena.getTeamYellow().contains(attacker.getUniqueId())) event.setCancelled(true);
		}
		if (!arena.isWallsFall()) event.setCancelled(true);
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
		Player deadPlayer = deathEvent.getEntity().getPlayer();
		arena.getTeamRed().remove(deadPlayer.getUniqueId());
		arena.getTeamGreen().remove(deadPlayer.getUniqueId());
		arena.getTeamBlue().remove(deadPlayer.getUniqueId());
		arena.getTeamYellow().remove(deadPlayer.getUniqueId());

		if (arena.getPlayersInGame().contains(deadPlayer.getUniqueId())) {
			deathEvent.setDeathMessage("");
			Random rand = new Random();
			int msgID = rand.nextInt(plugin.arenaManager.getDeathMsgStrings().size());
			String deathMsg = "", killeeName = deadPlayer.getDisplayName();
//			String[] deathMsgs = new String[11];
//			deathMsgs[0] = " was slain by ";
//			deathMsgs[1] = " was 69ed by ";
//			deathMsgs[2] = " was memed by ";
//			deathMsgs[3] = " got epic gamer moved by ";
//			deathMsgs[4] = " was isekaied to another world by ";
//			deathMsgs[5] = " got rekt by ";
//			deathMsgs[6] = " got w-tapped by ";
//			deathMsgs[7] = " got destroyed by ";
//			deathMsgs[8] = " got creeper aw maned by ";
//			deathMsgs[9] = " got squashed by anime thighs by ";
//			deathMsgs[10] = " lost all hp and fainted to ";
			//Run this if a player was killed by a player.
			if (deathEvent.getEntity().getPlayer().getKiller() != null) {
				Player killer = deathEvent.getEntity().getPlayer().getKiller();
				String killerName = killer.getDisplayName();
				deathMsg = plugin.arenaManager.getDeathMsgStrings().get(msgID);
				deathMsg = deathMsg.replace("%killee%", killeeName);
				deathMsg = deathMsg.replace("%killer%", killerName);
				arena.getPlayerScoreboards().get(killer.getUniqueId()).setKills(arena.getPlayerScoreboards().get(killer.getUniqueId()).getKills()+1);
			}
			//run this if player died of natural causes.
			else {
				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) deathMsg = killeeName + ChatColor.GRAY + " fell in the void.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.LAVA) deathMsg = killeeName + ChatColor.GRAY + " thought he had the high ground and melted in lava.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.DROWNING) deathMsg = killeeName + ChatColor.GRAY + " forgot how to breathe.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE_TICK) deathMsg = killeeName + ChatColor.GRAY + " burnt to a crisp.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE) deathMsg = killeeName + ChatColor.GRAY + " played with fire.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FALL) deathMsg = killeeName + ChatColor.GRAY + " fell to a clumsy death.";
				else if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.SUICIDE) deathMsg = killeeName + ChatColor.GRAY + " said goodbye, cruel world!";
				else deathMsg = killeeName + ChatColor.GRAY + " has been eliminated!";
			}
			for (UUID id : arena.getPlayersInGame()) {Bukkit.getPlayer(id).sendMessage(ChatColor.translateAlternateColorCodes('&', deathMsg));}
		}
		arena.updateScoreboard();
		deathEvent.getEntity().teleport(arena.getConfig().getLobbySpawn());
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
		Player p = pMoveEvent.getPlayer();
		if (!arena.isWallsFall() && arena.isInProgress() && arena.getPlayersInGame().contains(p.getUniqueId())) {
			boolean isInWall = false;
			for (int i = 0; i < wallBlocks.size(); i++) {
				if (p.getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) {
					p.teleport(oldLocations.get(p.getUniqueId()));
					p.sendMessage(ChatColor.YELLOW + "What do you think you're doing?");
					isInWall = true;
				}
			}
			for (int i = 0; i < wallBlocks.size() && !isInWall; i++) {
				if (
				(p.getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()-1 == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ() == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX()-1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX() == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ()) ||
				(p.getLocation().getBlockX()+1 == wallBlocks.get(i).getBlockX() && p.getLocation().getBlockZ()+1 == wallBlocks.get(i).getBlockZ())) {
					oldLocations.put(p.getUniqueId(), pMoveEvent.getPlayer().getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void preventGoingOutofBounds(PlayerMoveEvent pMoveEvent) {
		Player p = pMoveEvent.getPlayer();
		if (arena.isInProgress() && arena.getPlayersInGame().contains(p.getUniqueId())) {
			boolean notInsideBoundary = true;
			for (Location[] region : arena.getConfig().getBoundaryRegions()) {
				if (
				Math.min(region[0].getBlockX(), region[1].getBlockX()) <= p.getLocation().getBlockX() && p.getLocation().getBlockX() <= Math.max(region[0].getBlockX(), region[1].getBlockX()) &&
				Math.min(region[0].getBlockZ(), region[1].getBlockZ()) <= p.getLocation().getBlockZ() && p.getLocation().getBlockZ() <= Math.max(region[0].getBlockZ(), region[1].getBlockZ())){
					oldLocations.put(p.getUniqueId(), pMoveEvent.getFrom());
					notInsideBoundary = false;
				}
			}
			if (notInsideBoundary) {
				p.teleport(oldLocations.get(p.getUniqueId()));
			}
		}
	}
	
	@EventHandler
	public void preventBlockExplosions(EntityExplodeEvent event) {
		List<Block> blocksNotToExplode = new ArrayList<>();
		for (Block block : event.blockList()) {
			if (!(buildRegionBlocks.contains(block.getLocation()))) {
				blocksNotToExplode.add(block);
			}
		}
		for (Block block : blocksNotToExplode) {
			event.blockList().remove(block);
		}
	}
}

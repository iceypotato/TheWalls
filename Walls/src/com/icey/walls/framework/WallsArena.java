package com.icey.walls.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import com.icey.walls.MainClass;
import com.icey.walls.listeners.ArenaListener;
import com.icey.walls.listeners.ItemTeamSelector;
import com.icey.walls.runnables.WallsCountdown;
import com.icey.walls.runnables.WallsGiveEffect;
import com.icey.walls.timers.WallsBattleCountdown;
import com.icey.walls.timers.WallsFallCountdown;
import com.icey.walls.timers.WallsGameEndCountdown;
import com.icey.walls.timers.WallsLobbyCountdown;
import com.icey.walls.util.BlockClipboard;
import com.icey.walls.util.PlayerOriginalState;
import com.icey.walls.scoreboard.WallsScoreboard;

public class WallsArena {
	
	private WallsArenaConfig config;
	private ScoreboardSharedTeams scoreboardSharedTeams;
	private MainClass plugin;
	private boolean running; //if someone is using the arena
	private boolean waiting; //if the arena is waiting for players
	private boolean inProgress; //if the arena has started.
	private boolean ending; //when there is a winner.
	private boolean wallsFall; //when the walls fall
	private boolean suddenDeath; ///when its time for sudden death.
	private ItemTeamSelector[] itemTeamSelectors;
	private HashMap<UUID, WallsScoreboard> playerScoreboards;
	private ArenaListener arenaListener;
	private BukkitRunnable wallsCountdown;
	private List<UUID> playersInGame;
	private int remainingTeams;
//	private List<WallsTeam> teams; // Will implement later
	private WallsTeam teamRed;
	private WallsTeam teamGreen;
	private WallsTeam teamBlue;
	private WallsTeam teamYellow;
	private HashMap<UUID, PlayerOriginalState> playerOriginalState;
	private BlockClipboard originalArena;
	private List<Location> wallBlocks;
	private List<Location> buildRegionBlocks;
	
	public WallsArena(WallsArenaConfig config, MainClass plugin) {
		this.config = config;
		this.inProgress = false;
		this.waiting = false;
		this.wallsFall = false;
		this.ending = false;
		this.plugin = plugin;
		this.remainingTeams = 0;
		this.playersInGame = new ArrayList<>();
//		this.teams = new ArrayList<>();
//		this.teams.add(new WallsTeam("Red", ChatColor.RED+"", ""));
//		this.teams.add(new WallsTeam("Green", ChatColor.GREEN+"", ""));
//		this.teams.add(new WallsTeam("Blue", ChatColor.BLUE+"", ""));
//		this.teams.add(new WallsTeam("Yellow", ChatColor.YELLOW+"", ""));
		
		this.teamRed = new WallsTeam("Red", ChatColor.RED+"", "");
		this.teamGreen = new WallsTeam("Green", ChatColor.GREEN+"", "");
		this.teamBlue = new WallsTeam("Blue", ChatColor.BLUE+"", "");
		this.teamYellow = new WallsTeam("Yellow", ChatColor.YELLOW+"", "");
		this.itemTeamSelectors = new ItemTeamSelector[4];
		this.originalArena = new BlockClipboard();
		this.buildRegionBlocks = new ArrayList<>();
		this.wallBlocks = new ArrayList<>();
		this.playerOriginalState = new HashMap<>();
	}
	
	//run this only when one person joins
	public void initializeArena() {
		for (Location[] region : config.getArenaRegions()) {
			originalArena.addRegion(region[0], region[1]);
		}
		for (Location[] region : config.getWallRegions()) {
			for (int x = Math.min(region[0].getBlockX(), region[1].getBlockX()); x <= Math.max(region[0].getBlockX(), region[1].getBlockX()); x++) {
				for (int y = Math.min(region[0].getBlockY(), region[1].getBlockY()); y <= Math.max(region[0].getBlockY(), region[1].getBlockY()); y++) {
					for (int z = Math.min(region[0].getBlockZ(), region[1].getBlockZ()); z <= Math.max(region[0].getBlockZ(), region[1].getBlockZ()); z++) {
						Location loc = new Location(region[0].getWorld(), x, y, z);
						wallBlocks.add(loc);
					}
				}
			}
		}
		for (Location[] region : config.getBuildRegions()) {
			for (int x = Math.min(region[0].getBlockX(), region[1].getBlockX()); x <= Math.max(region[0].getBlockX(), region[1].getBlockX()); x++) {
				for (int y = Math.min(region[0].getBlockY(), region[1].getBlockY()); y <= Math.max(region[0].getBlockY(), region[1].getBlockY()); y++) {
					for (int z = Math.min(region[0].getBlockZ(), region[1].getBlockZ()); z <= Math.max(region[0].getBlockZ(), region[1].getBlockZ()); z++) {
						Location loc = new Location(region[0].getWorld(), x, y, z);
						buildRegionBlocks.add(loc);
					}
				}
			}
		}
		itemTeamSelectors[0] = new ItemTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.RED+"Join Team Red", ChatColor.RED+"RED", this, teamRed);
		itemTeamSelectors[1] = new ItemTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 5), ChatColor.GREEN+"Join Team Green", ChatColor.GREEN+"GREEN", this, teamGreen);
		itemTeamSelectors[2] = new ItemTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 11), ChatColor.BLUE+"Join Team Blue", ChatColor.BLUE+"BLUE", this, teamBlue);
		itemTeamSelectors[3] = new ItemTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 4), ChatColor.YELLOW+"Join Team Yellow", ChatColor.YELLOW+"YELLOW", this, teamYellow);
		for (ItemTeamSelector item : itemTeamSelectors) {
			plugin.getServer().getPluginManager().registerEvents(item, plugin);
		}
		arenaListener = new ArenaListener(this);
		plugin.getServer().getPluginManager().registerEvents(arenaListener, plugin);
		this.scoreboardSharedTeams = plugin.getNewScoreboardSharedTeams();
		playerScoreboards = new HashMap<UUID, WallsScoreboard>();
		wallsCountdown = new WallsLobbyCountdown(config.getWaitingTime() / 60, config.getWaitingTime() % 60, playerScoreboards.values(), this);
	}
	
	/*
	 * 
	 * Arena Game Phases/Events
	 * 
	 */
	
	public void playerJoin(UUID playerUUID) {
		Player player = Bukkit.getPlayer(playerUUID);
		running = true;
		if (!inProgress) {
			waiting = true;
			if (playersInGame.size() == 0) {
				player.sendMessage(ChatColor.GREEN + "Loading... This will take some time depending on the size of the arena.");
				initializeArena();
			}
			Random random = new Random();
			int randNum = random.nextInt(4);
			if (randNum == 0) joinTeam(playerUUID, teamRed);
			if (randNum == 1) joinTeam(playerUUID, teamGreen);
			if (randNum == 2) joinTeam(playerUUID, teamBlue);
			if (randNum == 3) joinTeam(playerUUID, teamYellow);
			player.sendMessage("R: " + teamRed.getNumPlayersAliveOnTeam());
			player.sendMessage("G: " + teamGreen.getNumPlayersAliveOnTeam());
			player.sendMessage("B: " + teamBlue.getNumPlayersAliveOnTeam());
			player.sendMessage("Y: " + teamYellow.getNumPlayersAliveOnTeam());
//			joinTeam(playerUUID, teams.get(randNum));
			
			playersInGame.add(playerUUID);
			playerOriginalState.put(playerUUID, new PlayerOriginalState(player));
			playerScoreboards.put(playerUUID, new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", scoreboardSharedTeams, player));
			playerScoreboards.get(playerUUID).setPlayerScoreboard();
			updateScoreboard();
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.teleport(config.getLobbySpawn());
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setSaturation(100);
			player.setFoodLevel(20);
			for (PotionEffect potionEffect : player.getActivePotionEffects()) {
				player.removePotionEffect(potionEffect.getType());
			}
			for (int i = 0; i < itemTeamSelectors.length; i++) {
				itemTeamSelectors[i].giveItemToPlayer(player);
			}
			if (playersInGame.size() >= config.getMinPlayers() && !(((WallsCountdown) wallsCountdown).isRunning())) {
				wallsCountdown = new WallsLobbyCountdown(config.getWaitingTime() / 60, config.getWaitingTime() % 60, playerScoreboards.values(), this);
				wallsCountdown.runTaskTimer(plugin, 0, 20);
			}
		}
		else {
			player.sendMessage(ChatColor.YELLOW + "This arena has already started!");
		}
	}
	
	public void playerLeave(UUID playerUUID) {
		Player player = Bukkit.getPlayer(playerUUID);
		if (waiting) {
			if (playersInGame.size() < config.getMinPlayers() && ((WallsCountdown)wallsCountdown).isRunning()) {
				wallsCountdown.cancel();
			}
		}
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
		playerOriginalState.get(playerUUID).restoreState();
		playersInGame.remove(playerUUID);
		playerOriginalState.remove(playerUUID);
		playerScoreboards.remove(playerUUID);
		
//		for (WallsTeam wt : teams) {
//			wt.removePlayer(player);
//		}
		teamRed.removePlayer(player);
		teamGreen.removePlayer(player);
		teamBlue.removePlayer(player);
		teamYellow.removePlayer(player);
		updateScoreboard();
		if (inProgress) checkForRemainingTeams();
		if (playersInGame.size() == 0) stopGame();
	}
	
	public void joinTeam(UUID playerUUID, WallsTeam teamToJoin) {
		teamRed.removePlayer(Bukkit.getPlayer(playerUUID));
		teamGreen.removePlayer(Bukkit.getPlayer(playerUUID));
		teamBlue.removePlayer(Bukkit.getPlayer(playerUUID));
		teamYellow.removePlayer(Bukkit.getPlayer(playerUUID));
//		for (WallsTeam wt : teams) {
//			wt.removePlayer(Bukkit.getPlayer(playerUUID));
//		}
		teamToJoin.addPlayer(Bukkit.getPlayer(playerUUID));
	}
	
	public void updateScoreboard() {
		for (WallsScoreboard wallsScoreboard : playerScoreboards.values()) {
			wallsScoreboard.clearSB();
			if (waiting) {
				wallsScoreboard.setPlayers(playersInGame.size());
				wallsScoreboard.setMaxPlayers(config.getMaxPlayers());
				wallsScoreboard.setMinPlayers(config.getMinPlayers());
				wallsScoreboard.putWaiting();
			}
			if (inProgress) {
				wallsScoreboard.putKills();
				wallsScoreboard.showHealth();
				wallsScoreboard.setReds(teamRed.getNumPlayersAliveOnTeam());
				wallsScoreboard.setGreens(teamGreen.getNumPlayersAliveOnTeam());
				wallsScoreboard.setBlues(teamBlue.getNumPlayersAliveOnTeam());
				wallsScoreboard.setYellows(teamYellow.getNumPlayersAliveOnTeam());
				if (ending) {
					wallsScoreboard.putEndingTimer();
				}
				else if (wallsFall && !suddenDeath) {
					wallsScoreboard.putSuddenDeathTime();
				}
				else if (!wallsFall) {
					wallsScoreboard.putPrepTime();
				}
				wallsScoreboard.putPlayersAlive();
			}
		}
	}
	
	/*
	 * Stages of the game
	 */
	
	public void startPrep() {
		for (Player player : teamRed.getPlayers()) {
			player.teleport(config.getRedSpawn());
			player.setDisplayName(ChatColor.RED + player.getName());
			scoreboardSharedTeams.joinTeam(player, "red");
		}
		for (Player player : teamGreen.getPlayers()) {
			player.teleport(config.getGreenSpawn());
			player.setDisplayName(ChatColor.GREEN + player.getName());
			scoreboardSharedTeams.joinTeam(player, "green");
		}
		for (Player player : teamBlue.getPlayers()) {
			player.teleport(config.getBlueSpawn());
			player.setDisplayName(ChatColor.BLUE + player.getName());
			scoreboardSharedTeams.joinTeam(player, "blue");
		}
		for (Player player : teamYellow.getPlayers()) {
			player.teleport(config.getYellowSpawn());
			player.setDisplayName(ChatColor.YELLOW + player.getName());
			scoreboardSharedTeams.joinTeam(player, "yellow");
		}
		for (UUID id: playersInGame) {
			Bukkit.getPlayer(id).getInventory().clear();
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			playerScoreboards.put(id, new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", scoreboardSharedTeams, Bukkit.getPlayer(id)));
			playerScoreboards.get(id).setPlayerScoreboard();
			if (Bukkit.getPlayer(id).getHealth() > 0) Bukkit.getPlayer(id).setHealth(Bukkit.getPlayer(id).getHealth() - 0.0001);
		}
		if (teamBlue.getNumPlayersAliveOnTeam() > 0) remainingTeams++;
		if (teamRed.getNumPlayersAliveOnTeam() > 0) remainingTeams++;
		if (teamGreen.getNumPlayersAliveOnTeam() > 0) remainingTeams++;
		if (teamYellow.getNumPlayersAliveOnTeam() > 0) remainingTeams++;
		teamRed.checkEliminated();
		teamGreen.checkEliminated();
		teamBlue.checkEliminated();
		teamYellow.checkEliminated();
		waiting = false;
		inProgress = true;
		updateScoreboard();
		wallsCountdown = new WallsFallCountdown(config.getPrepTime() / 60, config.getPrepTime() % 60, playerScoreboards.values(), this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void startPvp() {
		wallsFall = true;
		for (UUID id : playersInGame) {
			Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
			Bukkit.getPlayer(id).sendTitle(ChatColor.AQUA + "The Walls Have Fallen!", ChatColor.GOLD+"Go go go!");
		}
		for (Location block : wallBlocks) {
			block.getBlock().setType(Material.AIR);
		}
		wallsCountdown = new WallsBattleCountdown(config.getBattleTime() / 60, config.getBattleTime() % 60, playerScoreboards.values(), this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void suddenDeath() {
		suddenDeath = true;
		for (UUID id : playersInGame) {
			Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.WITHER_SPAWN, 10, 1);
			Bukkit.getPlayer(id).sendTitle(ChatColor.AQUA + "Sudden Death!", ChatColor.GOLD+"Head to the middle to stop recieving wither!");
		}
		wallsCountdown = new WallsGiveEffect(this);
		wallsCountdown.runTaskTimer(plugin, 0, 1);
	}
	
	public void winner() {
		String winner = ChatColor.GOLD +""+ ChatColor.BOLD + "\nWINNER>> ";
		if (teamRed.getNumPlayersAliveOnTeam() != 0) winner += ChatColor.RED + "Red Wins!\n";
		if (teamGreen.getNumPlayersAliveOnTeam() != 0) winner += ChatColor.GREEN + "Green Wins!\n";
		if (teamBlue.getNumPlayersAliveOnTeam() != 0) winner += ChatColor.BLUE + "Blue Wins!\n";
		if (teamYellow.getNumPlayersAliveOnTeam() != 0) winner += ChatColor.YELLOW + "Yellow Wins!\n";
		for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage(winner);}
		ending = true;
		updateScoreboard();
		wallsCountdown.cancel();
		wallsCountdown = new WallsGameEndCountdown(0, 10, playerScoreboards.values(), this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void checkForRemainingTeams() {
		if (teamRed.getNumPlayersAliveOnTeam() == 0 && !teamRed.isEliminated()) {
			remainingTeams -= 1; teamRed.setEliminated(true);
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.RED+"Red Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamGreen.getNumPlayersAliveOnTeam() == 0 && !teamGreen.isEliminated()) {
			remainingTeams -= 1; teamGreen.setEliminated(true);
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.GREEN+"Green Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamBlue.getNumPlayersAliveOnTeam() == 0 && !teamBlue.isEliminated()) {
			remainingTeams -= 1; teamBlue.setEliminated(true);
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.BLUE+"Blue Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamYellow.getNumPlayersAliveOnTeam() == 0 && !teamYellow.isEliminated()) {
			remainingTeams -= 1; teamYellow.setEliminated(true);
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.YELLOW+"Yellow Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (remainingTeams == 1) {
			winner();
		}
	}
	
	public void stopGame() {
		if (running) {
			running = false;
			inProgress = false;
			waiting = false;
			wallsFall = false;
			ending = false;
			suddenDeath = false;
			remainingTeams = 0;
			try {
				wallsCountdown.cancel();
			} catch (IllegalStateException e) {
				plugin.getLogger().warning("A timer has been canceled with no scheduled task.");
			}
			for (UUID uuid : playersInGame) {
				for (PotionEffect potionEffect : Bukkit.getPlayer(uuid).getActivePotionEffects()) {
					Bukkit.getPlayer(uuid).removePotionEffect(potionEffect.getType());
				}
				Bukkit.getPlayer(uuid).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				playerOriginalState.get(uuid).restoreState();
			}
			playersInGame.clear();
			playerOriginalState.clear();
			HandlerList.unregisterAll(arenaListener);
			for (int i = 0; i < itemTeamSelectors.length; i++) HandlerList.unregisterAll(itemTeamSelectors[i]);
			plugin.getLogger().info(config.getName() + " Arena is restoring. This will cause lag.");
			originalArena.pasteBlocksInClipboard();
			for (int j = 0; j < config.getArenaRegions().size(); j++) {
				if (config.getArenaRegions().get(j)[0].getWorld() == null) {
					plugin.getLogger().warning("An arena could not be loaded due to it being in an unloaded world! If using multiverse, please load the world and make sure to join the world. Then run /walls reload.");
					break;
				}
				for (int i = 0; i < config.getArenaRegions().get(j)[0].getWorld().getEntities().size(); i++) {
					Location locofEnt = config.getArenaRegions().get(j)[0].getWorld().getEntities().get(i).getLocation();
					if (Math.min(config.getArenaRegions().get(j)[0].getBlockX(), config.getArenaRegions().get(j)[1].getBlockX()) <= locofEnt.getBlockX() &&
					locofEnt.getBlockX() <= Math.max(config.getArenaRegions().get(j)[0].getBlockX(), config.getArenaRegions().get(j)[1].getBlockX()) &&
					Math.min(config.getArenaRegions().get(j)[0].getBlockY(), config.getArenaRegions().get(j)[1].getBlockY()) <= locofEnt.getBlockY() &&
					locofEnt.getBlockY() <= Math.max(config.getArenaRegions().get(j)[0].getBlockY(), config.getArenaRegions().get(j)[1].getBlockY()) &&
					Math.min(config.getArenaRegions().get(j)[0].getBlockZ(), config.getArenaRegions().get(j)[1].getBlockZ()) <= locofEnt.getBlockZ() &&
					locofEnt.getBlockZ() <= Math.max(config.getArenaRegions().get(j)[0].getBlockZ(), config.getArenaRegions().get(j)[1].getBlockZ())) {
						if (config.getArenaRegions().get(j)[0].getWorld().getEntities().get(i) instanceof Item) config.getArenaRegions().get(j)[0].getWorld().getEntities().get(i).remove();
					}
				}
			}
		}
	}
	
	public void brodcastMsgToArena(String msg, String killer, String killee) {
		for (UUID id : playersInGame) {
			Bukkit.getPlayer(id).sendMessage(killee + ChatColor.GRAY + msg + ChatColor.RESET + killer);
		}
	}
	
	public WallsTeam getPlayerFromTeam(Player player) {
		if (teamRed.getPlayers().contains(player)) return teamRed;
		return null;
	}
	
	
	/* 
	 * 
	 * Normal Getters and setters 
	 * 
	 */

	public boolean isRunning() { return running; }
	public boolean isInProgress() { return inProgress; }
	public boolean isWaiting() {return waiting;}
	public void setWaiting(boolean waiting) {this.waiting = waiting;}
	public boolean isWallsFall() {return wallsFall;}
	public void setWallsFall(boolean wallsFall) {this.wallsFall = wallsFall;}
	public boolean isEnding() {return ending;}
	
	public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
	public List<UUID> getPlayersInGame() { return playersInGame; }
	public void setPlayersInGame(List<UUID> playersInGame) { this.playersInGame = playersInGame; }
	public WallsTeam getTeamRed() {return teamRed;}
	public void setTeamRed(WallsTeam teamRed) {this.teamRed = teamRed;}
	public WallsTeam getTeamGreen() {return teamGreen;}
	public void setTeamGreen(WallsTeam teamGreen) {this.teamGreen = teamGreen;}
	public WallsTeam getTeamBlue() {return teamBlue;}
	public void setTeamBlue(WallsTeam teamBlue) {this.teamBlue = teamBlue;}
	public WallsTeam getTeamYellow() {return teamYellow;}
	public void setTeamYellow(WallsTeam teamYellow) {this.teamYellow = teamYellow;}
	public MainClass getPlugin() {return plugin;}

	public List<Location> getWallBlocks() {
		return wallBlocks;
	}
	public List<Location> getBuildRegionBlocks() {
		return buildRegionBlocks;
	}
	
	public int getRemainingTeams() {return remainingTeams;}
	public void setRemainingTeams(int remainingTeams) {this.remainingTeams = remainingTeams;}
	public WallsArenaConfig getConfig() {return config;}
	public void setConfig(WallsArenaConfig config) {this.config = config;}

	public BukkitRunnable getWallsCountdown() {
		return wallsCountdown;
	}
	/**
	 * @return the suddenDeath
	 */
	public boolean isSuddenDeath() {
		return suddenDeath;
	}

	/**
	 * @param suddenDeath the suddenDeath to set
	 */
	public void setSuddenDeath(boolean suddenDeath) {
		this.suddenDeath = suddenDeath;
	}
	public HashMap<UUID, WallsScoreboard> getPlayerScoreboards() {
		return playerScoreboards;
	}
	public ScoreboardSharedTeams getScoreboardSharedTeams() {
		return scoreboardSharedTeams;
	}
}
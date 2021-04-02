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
import com.icey.walls.listeners.WoolTeamSelector;
import com.icey.walls.timers.WallsBattleCountdown;
import com.icey.walls.timers.WallsCountdown;
import com.icey.walls.timers.WallsFallCountdown;
import com.icey.walls.timers.WallsGameEndCountdown;
import com.icey.walls.timers.WallsLobbyCountdown;
import com.icey.walls.util.BlockClipboard;
import com.icey.walls.util.PlayerOriginalState;
import com.icey.walls.util.WallsGiveEffect;
import com.icey.walls.scoreboard.WallsScoreboard;

public class WallsArena {
	
	private WallsArenaConfig config;
	private MainClass plugin;
	private boolean running; //if someone is using the arena
	private boolean waiting; //if the arena is waiting for players
	private boolean inProgress; //if the arena has started.
	private boolean ending; //when there is a winner.
	private boolean wallsFall; //when the walls fall
	private boolean suddenDeath; ///when its time for sudden death.
	private WoolTeamSelector[] woolTeamSelectors;
	private HashMap<UUID, WallsScoreboard> playerScoreboards;
	//private WallsScoreboard wallsSB;
	private ArenaListener arenaListener;
	private BukkitRunnable wallsCountdown;
	private ArrayList<UUID> playersInGame;
	private int remainingTeams;
	private List<UUID> teamRed;
	private List<UUID> teamGreen;
	private List<UUID> teamBlue;
	private List<UUID> teamYellow;
	private boolean redEliminated;
	private boolean greenEliminated;
	private boolean blueEliminated;
	private boolean yellowEliminated;
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
		this.teamRed = new ArrayList<>();
		this.teamGreen = new ArrayList<>();
		this.teamBlue = new ArrayList<>();
		this.teamYellow = new ArrayList<>();
		this.woolTeamSelectors = new WoolTeamSelector[4];
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
		woolTeamSelectors[0] = new WoolTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.RED+"Join Team Red", ChatColor.RED+"RED", this, teamRed);
		woolTeamSelectors[1] = new WoolTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 5), ChatColor.GREEN+"Join Team Green", ChatColor.GREEN+"GREEN", this, teamGreen);
		woolTeamSelectors[2] = new WoolTeamSelector(plugin, new ItemStack(Material.WOOL, 1, (short) 11), ChatColor.BLUE+"Join Team Blue", ChatColor.BLUE+"BLUE", this, teamBlue);
		woolTeamSelectors[3] = new WoolTeamSelector(plugin ,new ItemStack(Material.WOOL, 1, (short) 4), ChatColor.YELLOW+"Join Team Yellow", ChatColor.YELLOW+"YELLOW", this, teamYellow);
		for (WoolTeamSelector wool : woolTeamSelectors) {
			plugin.getServer().getPluginManager().registerEvents(wool, plugin);
		}
		arenaListener = new ArenaListener(this, wallBlocks, buildRegionBlocks);
		plugin.getServer().getPluginManager().registerEvents(arenaListener, plugin);
		//wallsSB = new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", "dummy", DisplaySlot.SIDEBAR);
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
			playersInGame.add(playerUUID);
			playerOriginalState.put(playerUUID, new PlayerOriginalState(player));
			playerScoreboards.put(playerUUID, new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", player));
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
			for (int i = 0; i < woolTeamSelectors.length; i++) {
				woolTeamSelectors[i].giveItemToPlayer(player);
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
		teamRed.remove(playerUUID);
		teamGreen.remove(playerUUID);
		teamBlue.remove(playerUUID);
		teamYellow.remove(playerUUID);
		playerScoreboards.get(playerUUID).leaveTeams();
		updateScoreboard();
		if (inProgress) checkForRemainingTeams();
		if (playersInGame.size() == 0) stopGame();
	}
	
	public void joinTeam(UUID playerUUID, List<UUID> teamToJoin) {
		teamRed.remove(playerUUID);
		teamGreen.remove(playerUUID);
		teamBlue.remove(playerUUID);
		teamYellow.remove(playerUUID);
		teamToJoin.add(playerUUID);
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
				wallsScoreboard.setReds(teamRed.size());
				wallsScoreboard.setGreens(teamGreen.size());
				wallsScoreboard.setBlues(teamBlue.size());
				wallsScoreboard.setYellows(teamYellow.size());
				if (ending) {
					wallsScoreboard.putEndingTimer();
				}
				else if (wallsFall) {
					wallsScoreboard.putSuddenDeathTime();
				}
				else {
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
		for (UUID id : teamRed) {
			Player player = Bukkit.getPlayer(id);
			playerScoreboards.get(id).joinRedTeam();
			player.teleport(config.getRedSpawn());
			player.setGameMode(GameMode.SURVIVAL);
			player.setDisplayName(ChatColor.RED + player.getName());
			player.setPlayerListName(ChatColor.RED + player.getName());
		}
		for (UUID id : teamGreen) {
			playerScoreboards.get(id).joinGreenTeam();
			Player player = Bukkit.getPlayer(id);
			player.teleport(config.getGreenSpawn());
			player.setGameMode(GameMode.SURVIVAL);
			player.setDisplayName(ChatColor.GREEN + player.getName());
			player.setPlayerListName(ChatColor.GREEN + player.getName());
		}
		for (UUID id : teamBlue) {
			playerScoreboards.get(id).joinBlueTeam();
			Player player = Bukkit.getPlayer(id);
			player.teleport(config.getBlueSpawn());
			player.setGameMode(GameMode.SURVIVAL);
			player.setDisplayName(ChatColor.BLUE + player.getName());
			player.setPlayerListName(ChatColor.BLUE + player.getName());
		}
		for (UUID id : teamYellow) {
			playerScoreboards.get(id).joinYellowTeam();
			Player player = Bukkit.getPlayer(id);
			player.teleport(config.getYellowSpawn());
			player.setGameMode(GameMode.SURVIVAL);
			player.setDisplayName(ChatColor.YELLOW + player.getName());
			player.setPlayerListName(ChatColor.YELLOW + player.getName());
		}
		//wallsSB.setKills(new HashMap<Player,Integer>());
		for (UUID id: playersInGame) {
			Bukkit.getPlayer(id).getInventory().clear();
			//wallsSB.getKills().put(Bukkit.getPlayer(id), 0);
		}
		if (teamBlue.size() > 0) remainingTeams++;
		if (teamRed.size() > 0) remainingTeams++;
		if (teamGreen.size() > 0) remainingTeams++;
		if (teamYellow.size() > 0) remainingTeams++;
		this.redEliminated = (teamRed.size() == 0) ? true : false;
		this.greenEliminated = (teamGreen.size() == 0) ? true : false;
		this.blueEliminated = (teamBlue.size() == 0) ? true : false;
		this.yellowEliminated = (teamYellow.size() == 0) ? true : false;
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
		if (teamRed.size() != 0) winner += ChatColor.RED + "Red Wins!\n";
		if (teamGreen.size() != 0) winner += ChatColor.GREEN + "Green Wins!\n";
		if (teamBlue.size() != 0) winner += ChatColor.BLUE + "Blue Wins!\n";
		if (teamYellow.size() != 0) winner += ChatColor.YELLOW + "Yellow Wins!\n";
		for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage(winner);}
		ending = true;
		updateScoreboard();
		wallsCountdown.cancel();
		wallsCountdown = new WallsGameEndCountdown(0, 10, playerScoreboards.values(), this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void checkForRemainingTeams() {
		if (teamRed.size() == 0 && !redEliminated) {
			redEliminated = true;
			remainingTeams -= 1;
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.RED+"Red Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamGreen.size() == 0 && !greenEliminated) {
			greenEliminated = true;
			remainingTeams -= 1;
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.GREEN+"Green Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamBlue.size() == 0 && !blueEliminated) {
			blueEliminated = true;
			remainingTeams -= 1;
			for (UUID id : playersInGame) {Bukkit.getPlayer(id).sendMessage("\n"+ChatColor.BOLD+"ELIMINATION>> " + ChatColor.BLUE+"Blue Team" + ChatColor.RESET+" Has Been Eliminated!\n");}
		}
		if (teamYellow.size() == 0 && !yellowEliminated) {
			yellowEliminated = true;
			remainingTeams -= 1;
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
			for (int i = 0; i < woolTeamSelectors.length; i++) HandlerList.unregisterAll(woolTeamSelectors[i]);
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
	
	
	/* 
	 * 
	 * Normal Getters and setters 
	 * 
	 */

	public boolean isInProgress() { return inProgress; }
	public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
	public ArrayList<UUID> getPlayersInGame() { return playersInGame; }
	public void setPlayersInGame(ArrayList<UUID> playersInGame) { this.playersInGame = playersInGame; }
	public List<UUID> getTeamRed() {return teamRed;}
	public void setTeamRed(List<UUID> teamRed) {this.teamRed = teamRed;}
	public List<UUID> getTeamGreen() {return teamGreen;}
	public void setTeamGreen(List<UUID> teamGreen) {this.teamGreen = teamGreen;}
	public List<UUID> getTeamBlue() {return teamBlue;}
	public void setTeamBlue(List<UUID> teamBlue) {this.teamBlue = teamBlue;}
	public List<UUID> getTeamYellow() {return teamYellow;}
	public void setTeamYellow(List<UUID> teamYellow) {this.teamYellow = teamYellow;}
	public MainClass getPlugin() {return plugin;}
	public boolean isWaiting() {return waiting;}
	public void setWaiting(boolean waiting) {this.waiting = waiting;}
	public boolean isWallsFall() {return wallsFall;}
	public void setWallsFall(boolean wallsFall) {this.wallsFall = wallsFall;}
	public int getRemainingTeams() {return remainingTeams;}
	public void setRemainingTeams(int remainingTeams) {this.remainingTeams = remainingTeams;}
	public WallsArenaConfig getConfig() {return config;}
	public void setConfig(WallsArenaConfig config) {this.config = config;}

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
}
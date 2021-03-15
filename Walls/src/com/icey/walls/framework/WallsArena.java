package com.icey.walls.framework;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import com.icey.walls.MainClass;
import com.icey.walls.listeners.ArenaListener;
import com.icey.walls.listeners.WoolTeamSelector;
import com.icey.walls.timers.WallsFallCountdown;
import com.icey.walls.timers.WallsGameEndCountdown;
import com.icey.walls.timers.WallsLobbyCountdown;
import com.icey.walls.util.BlockClipboard;
import com.icey.walls.util.PlayerOriginalState;

public class WallsArena {
	
	private WallsArenaConfig config;
	private MainClass plugin;
	private boolean waiting;
	private boolean inProgress;
	private boolean ending;
	private boolean wallsFall;
	private WoolTeamSelector[] woolTeamSelectors;
	private WallsScoreboard wallsSB;
	private ArenaListener arenaListener;
	private BukkitRunnable wallsCountdown;
	private HashMap<UUID, PlayerOriginalState> playerOriginalState;
	private ArrayList<UUID> playersInGame;
	private int remainingTeams;
	private ArrayList<UUID> teamRed;
	private ArrayList<UUID> teamGreen;
	private ArrayList<UUID> teamBlue;
	private ArrayList<UUID> teamYellow;
	private boolean redEliminated;
	private boolean greenEliminated;
	private boolean blueEliminated;
	private boolean yellowEliminated;
	private BlockClipboard originalArena;
	private ArrayList<Location> wallBlocks;
	private ArrayList<Location> buildRegionBlocks;
	
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
	}
	
	/*
	 * 
	 * Arena Game Phases/Events
	 * 
	 */
	
	public void playerJoin(Player player) {
		if (!inProgress) {
			waiting = true;
			if (playersInGame.size() == 0) {
				initializeArena();
				wallsSB = new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", "dummy", DisplaySlot.SIDEBAR);
			}
			Random random = new Random();
			int randNum = random.nextInt(4);
			if (randNum == 0) joinTeam(player, teamRed);
			if (randNum == 1) joinTeam(player, teamGreen);
			if (randNum == 2) joinTeam(player, teamBlue);
			if (randNum == 3) joinTeam(player, teamYellow);
			playersInGame.add(player.getUniqueId());
			playerOriginalState.put(player.getUniqueId(), new PlayerOriginalState(player));
			updateScoreboard();
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.teleport(config.getLobbySpawn());
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setSaturation(100);
			player.setFoodLevel(20);
			player.getActivePotionEffects().clear();
			for (int i = 0; i < woolTeamSelectors.length; i++) {
				woolTeamSelectors[i].giveItemToPlayer(player);
			}
			if (playersInGame.size() >= config.getMinPlayers()) lobbyCountdown();
		}
		else {
			player.sendMessage(ChatColor.YELLOW + "This arena has already started!");
		}
	}
	
	public void playerLeave(Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setScoreboard(wallsSB.getManager().getMainScoreboard());
		playerOriginalState.get(player.getUniqueId()).restoreState();
		playersInGame.remove(player.getUniqueId());
		playerOriginalState.remove(player.getUniqueId());
		teamRed.remove(player.getUniqueId());
		teamGreen.remove(player.getUniqueId());
		teamBlue.remove(player.getUniqueId());
		teamYellow.remove(player.getUniqueId());
		updateScoreboard();

		if (playersInGame.size() < config.getMinPlayers() && wallsCountdown != null) wallsCountdown.cancel();
		if (playersInGame.size() == 0) stopGame();
		if (inProgress) checkForRemainingTeams();
	}
	
	public void joinTeam(Player player, ArrayList<UUID> teamToJoin) {
		teamRed.remove(player.getUniqueId());
		teamGreen.remove(player.getUniqueId());
		teamBlue.remove(player.getUniqueId());
		teamYellow.remove(player.getUniqueId());
		teamToJoin.add(player.getUniqueId());
	}
	
	public void updateScoreboard() {
		if (waiting) {
			wallsSB.clearSB();
			wallsSB.setPlayers(playersInGame.size());
			wallsSB.setMaxPlayers(config.getMaxPlayers());
			wallsSB.setMinPlayers(config.getMinPlayers());
			wallsSB.putWaiting();
		}
		
		if (inProgress) {
			wallsSB.clearSB();
			wallsSB.showHealth();
			wallsSB.setReds(teamRed.size());
			wallsSB.setGreens(teamGreen.size());
			wallsSB.setBlues(teamBlue.size());
			wallsSB.setYellows(teamYellow.size());
			wallsSB.putPrepTime();
			wallsSB.putPlayersAlive();
		}
		if (ending) {
			wallsSB.clearSB();
			wallsSB.putPlayersAlive();
			wallsSB.putEndingTimer();
		}
		for (UUID id : playersInGame) {
			wallsSB.updatePlayersSB(Bukkit.getPlayer(id));
		}
	}
	
	public void lobbyCountdown() {
		wallsCountdown = new WallsLobbyCountdown(config.getWaitingTime() / 60, config.getWaitingTime() % 60, wallsSB, this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void startPrep() {
		for (UUID id : teamRed) {
			wallsSB.joinRedTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(config.getRedSpawn());
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.RED + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamGreen) {
			wallsSB.joinGreenTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(config.getGreenSpawn());
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.GREEN + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamBlue) {
			wallsSB.joinBlueTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(config.getBlueSpawn());
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.BLUE + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamYellow) {
			wallsSB.joinYellowTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(config.getYellowSpawn());
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.YELLOW + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id: playersInGame) {
			Bukkit.getPlayer(id).getInventory().clear();
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
		wallsCountdown = new WallsFallCountdown(config.getPrepTime() / 60, config.getPrepTime() % 60, wallsSB, this);
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
		wallsCountdown = new WallsGameEndCountdown(0, 10, wallsSB, this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void checkForRemainingTeams() {
		plugin.getLogger().info(remainingTeams+"");
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
		inProgress = false;
		waiting = false;
		wallsFall = false;
		ending = false;
		remainingTeams = 0;
		for (UUID uuid : playersInGame) {
			Bukkit.getPlayer(uuid).setScoreboard(wallsSB.getManager().getMainScoreboard());
			playerOriginalState.get(uuid).restoreState();
		}
		playersInGame.clear();
		playerOriginalState.clear();
		HandlerList.unregisterAll(arenaListener);
		for (int i = 0; i < woolTeamSelectors.length; i++) HandlerList.unregisterAll(woolTeamSelectors[i]);
		plugin.getLogger().info(config.getName() + " Arena is restoring. This will cause lag.");
		originalArena.pasteBlocksInClipboard();
		plugin.getLogger().info(config.getArenaRegions().toString()+"");
		if (config.getArenaRegions().size() != 0) {
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
	public ArrayList<UUID> getTeamRed() {return teamRed;}
	public void setTeamRed(ArrayList<UUID> teamRed) {this.teamRed = teamRed;}
	public ArrayList<UUID> getTeamGreen() {return teamGreen;}
	public void setTeamGreen(ArrayList<UUID> teamGreen) {this.teamGreen = teamGreen;}
	public ArrayList<UUID> getTeamBlue() {return teamBlue;}
	public void setTeamBlue(ArrayList<UUID> teamBlue) {this.teamBlue = teamBlue;}
	public ArrayList<UUID> getTeamYellow() {return teamYellow;}
	public void setTeamYellow(ArrayList<UUID> teamYellow) {this.teamYellow = teamYellow;}
	public MainClass getPlugin() {return plugin;}
	public boolean isWaiting() {return waiting;}
	public void setWaiting(boolean waiting) {this.waiting = waiting;}
	public boolean isWallsFall() {return wallsFall;}
	public void setWallsFall(boolean wallsFall) {this.wallsFall = wallsFall;}
	public int getRemainingTeams() {return remainingTeams;}
	public void setRemainingTeams(int remainingTeams) {this.remainingTeams = remainingTeams;}
	public WallsArenaConfig getConfig() {return config;}
	public void setConfig(WallsArenaConfig config) {this.config = config;}
}
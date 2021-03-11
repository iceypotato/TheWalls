package com.icey.walls.listeners;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import com.icey.walls.MainClass;
import com.icey.walls.framework.BlockClipboard;
import com.icey.walls.framework.PlayerOriginalState;
import com.icey.walls.framework.WallsFallCountdown;
import com.icey.walls.framework.WallsLobbyCountdown;
import com.icey.walls.framework.WallsScoreboard;

public class Arena implements Listener {

	private MainClass plugin;
	private String name;
	private boolean running;
	private boolean inProgress;
	private boolean waiting;
	private boolean enabled;
	private int maxPlayers;
	private int minPlayers;
	private int waitingTime;
	private int prepTime;
	private ArrayList<UUID> playersInGame;
	private ArrayList<UUID> teamRed;
	private ArrayList<UUID> teamGreen;
	private ArrayList<UUID> teamBlue;
	private ArrayList<UUID> teamYellow;
	private HashMap<UUID, PlayerOriginalState> playerOriginalState;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private BlockClipboard protectedBlocks;
	private ArrayList<Location[]> arenaRegions;
	private ArrayList<Location[]> buildRegions;
	private ArrayList<Location[]> wallRegions;
	private Timer tm;
	private File arenaFile;
	private WallsScoreboard wallsSB;
	private	FileConfiguration arenaConfig;
	
	public Arena(String name, boolean enabled, boolean inProgress, boolean waiting, File arenaFile, MainClass plugin) {
		this.name = name;
		this.running = false;
		this.enabled = enabled;
		this.inProgress = inProgress;
		this.waiting = waiting;
		this.arenaFile = arenaFile;
		this.plugin = plugin;
		this.playersInGame = new ArrayList<UUID>();
		this.teamRed = new ArrayList<>();
		this.teamGreen = new ArrayList<>();
		this.teamBlue = new ArrayList<>();
		this.teamYellow = new ArrayList<>();
		this.playerOriginalState = new HashMap<UUID, PlayerOriginalState>();
		this.protectedBlocks = new BlockClipboard();
		this.arenaRegions = new ArrayList<>();
		this.buildRegions = new ArrayList<>();
		this.wallRegions = new ArrayList<>();
		this.running = false;
		this.tm = new Timer();
		this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void loadConfig() {
		readLobbySpawn();
		readBlueSpawn();
		readRedSpawn();
		readYellowSpawn();
		readGreenSpawn();
		addArenaRegion();
		addWallRegion();
		addBuildRegion();
		readSettings();
	}
	
	/*
	 * 
	 * Arena Events
	 * 
	 */
	
	public void playerJoin(Player player) {
		Random random = new Random();
		int randNum = random.nextInt(4);
		if (randNum == 0) teamRed.add(player.getUniqueId());
		if (randNum == 1) teamGreen.add(player.getUniqueId());
		if (randNum == 2) teamBlue.add(player.getUniqueId());
		if (randNum == 3) teamYellow.add(player.getUniqueId());
		if (playersInGame.size() == 0) {
			loadConfig();
			saveArenaState();
			wallsSB = new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", "dummy", DisplaySlot.SIDEBAR);
		}
		playersInGame.add(player.getUniqueId());
		playerOriginalState.put(player.getUniqueId(), new PlayerOriginalState(player));
		updateScoreboard();
		player.getInventory().clear();
		player.getEquipment().clear();
		player.teleport(lobbySpawn);
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(20);
		player.setSaturation(100);
		player.setFoodLevel(20);
		player.getActivePotionEffects().clear();
		waiting = true;
		if (playersInGame.size() >= minPlayers) lobbyCountdown();
	}
	
	public void playerLeave(Player player) {
		player.getInventory().clear();
		player.getEquipment().clear();
		player.setScoreboard(wallsSB.getManager().getMainScoreboard());
		playerOriginalState.get(player.getUniqueId()).restoreState();
		playersInGame.remove(player.getUniqueId());
		playerOriginalState.remove(player.getUniqueId());
		teamRed.remove(player.getUniqueId());
		teamGreen.remove(player.getUniqueId());
		teamBlue.remove(player.getUniqueId());
		teamYellow.remove(player.getUniqueId());
		updateScoreboard();

		if (playersInGame.size() < minPlayers) stopLobbyCountdown();
		if (playersInGame.size() == 0) {
			waiting = false;
			protectedBlocks.pasteBlocksInClipboard();
		}
	}
	
	public void updateScoreboard() {
		wallsSB.clearSB();
		wallsSB.setMinutes(waitingTime / 60);
		wallsSB.setSeconds(waitingTime % 60);
		wallsSB.setPlayers(playersInGame.size());
		wallsSB.setPlayers(playersInGame.size());
		wallsSB.setMaxPlayers(maxPlayers);
		wallsSB.setMinPlayers(minPlayers);
		wallsSB.putWaiting();
		if (playersInGame.size() < minPlayers) wallsSB.putRequiredToStart();
		for (UUID id : playersInGame) {
			wallsSB.updatePlayersSB(Bukkit.getPlayer(id));
		}
	}

	
	@EventHandler
	public void waitingForPlayers(PlayerInteractEvent event) {
		waiting = true;
		if(waiting && playersInGame.contains(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	public void lobbyCountdown() {
		tm = new Timer();
		WallsLobbyCountdown wallsCountdown = new WallsLobbyCountdown(waitingTime / 60, waitingTime % 60, wallsSB, playersInGame, this);
		tm.schedule(wallsCountdown, 0, 1000);
	}
	public void stopLobbyCountdown() {
		tm.cancel();
	}
	
	public void startPrep() {
		waiting = false;
		tm = new Timer();
		WallsFallCountdown wallsCountdown = new WallsFallCountdown(waitingTime / 60, waitingTime % 60, wallsSB, playersInGame, this);
		tm.schedule(wallsCountdown, 0, 1000);
	}
	
	public void startPvp() {
		
	}
	
	public void stopGame() {
		running = false;
		inProgress = false;
		playersInGame.clear();
		playerOriginalState.clear();
		for (UUID uuid : playersInGame) {
			playerLeave(Bukkit.getPlayer(uuid));
		}
		protectedBlocks.pasteBlocksInClipboard();
	}
	
	//run this only when one person joins
	public void saveArenaState() {
		protectedBlocks.clear();
		for (int i = 0; i < arenaRegions.size(); i++) {
			protectedBlocks.addRegion(arenaRegions.get(i)[0], arenaRegions.get(i)[1]);
		}
	}
	
	@EventHandler
	public void setup(PlayerInteractEvent event, BlockExplodeEvent block) {
		for (int i = 0; i < buildRegions.size(); i++) {
			if (!(event.getClickedBlock().getLocation().getBlockX() >= Math.min(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) && 
				event.getClickedBlock().getLocation().getBlockX() <= Math.max(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) &&
				event.getClickedBlock().getLocation().getBlockY() >= Math.min(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) && 
				event.getClickedBlock().getLocation().getBlockY() <= Math.max(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) &&
				event.getClickedBlock().getLocation().getBlockZ() >= Math.min(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ()) && 
				event.getClickedBlock().getLocation().getBlockZ() <= Math.max(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ())))
			{
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "You cannot break those blocks!");
				event.setCancelled(true);
			}
		}
		for (int i = 0; i < buildRegions.size(); i++) {
			if (!(block.getBlock().getLocation().getBlockX() >= Math.min(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) && 
				block.getBlock().getLocation().getBlockX() <= Math.max(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) &&
				block.getBlock().getLocation().getBlockY() >= Math.min(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) && 
				block.getBlock().getLocation().getBlockY() <= Math.max(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) &&
				block.getBlock().getLocation().getBlockZ() >= Math.min(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ()) && 
				block.getBlock().getLocation().getBlockZ() <= Math.max(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ())))
			{
				block.blockList().clear();
				block.setCancelled(true);
			}
		}
	}
	
	/*
	 * 
	 * Arena Configuration
	 * 
	 */
	
	public void readSettings() {
		if (arenaConfig.get("Settings.enabled") != null) enabled = arenaConfig.getBoolean("Settings.enabled");
		if (arenaConfig.get("Settings.waiting-time") != null) waitingTime = arenaConfig.getInt("Settings.waiting-time");
		if (arenaConfig.get("Settings.preparation-time") != null) prepTime = arenaConfig.getInt("Settings.preparation-time");
		if (arenaConfig.get("Settings.max-players") != null) maxPlayers = arenaConfig.getInt("Settings.max-players");
		if (arenaConfig.get("Settings.start-min-players") != null) minPlayers = arenaConfig.getInt("Settings.start-min-players");
	}
	
	public void addArenaRegion() { arenaRegions = readRegions("Arena"); }
	public void addWallRegion() { wallRegions = readRegions("Walls"); }
	public void addBuildRegion() { buildRegions = readRegions("Build"); }
	public ArrayList<Location[]> readRegions(String name) {
		int i = 1;
		ArrayList<Location[]> inRegion = new ArrayList<Location[]>();
		if (arenaConfig.get("Regions." + name + "." + i) != null) {
			while (i <= arenaConfig.getConfigurationSection("Regions." + name + "").getKeys(false).toArray().length) {
				if (arenaConfig.get("Regions." + name + "." + i).equals("") || arenaConfig.get("Regions." + name + "." + i) == null) {
					plugin.getLogger().info(this.name + " arena has an invalid config! Check the " + name + " regions");
					return null;
				}
				Location[] region = new Location[2];
				region[0] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions." + name + "." + i + ".world")),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos1.x"),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos1.y"),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos1.z"));
				region[1] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions." + name + "." + i + ".world")),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos2.x"),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos2.y"),
						arenaConfig.getDouble("Regions." + name + "." + i + ".pos2.z"));
				inRegion.add(region);
				i++;
			}
		}
		else {
			plugin.getLogger().info(this.name + " arena has an invalid config! Check the "+ name +" regions");
			return null;
		}
		return inRegion;
	}
	
	public void readLobbySpawn() {lobbySpawn = readSpawns("Lobby");}
	public void readBlueSpawn() {blueSpawn = readSpawns("Blue");}
	public void readRedSpawn() {redSpawn = readSpawns("Red");}
	public void readGreenSpawn() {greenSpawn = readSpawns("Green");}
	public void readYellowSpawn() {yellowSpawn = readSpawns("Yellow");}
	public Location readSpawns(String name) {
		if (arenaConfig.contains("Spawns." + name + ".world") && arenaConfig.contains("Spawns." + name + ".x") && arenaConfig.contains("Spawns." + name + ".y") && arenaConfig.contains("Spawns." + name + ".z")) {
			Location spawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns." + name + ".world")), arenaConfig.getDouble("Spawns." + name + ".x"), arenaConfig.getDouble("Spawns." + name + ".y"), arenaConfig.getDouble("Spawns." + name + ".z"));
			spawn.setPitch((float) arenaConfig.getDouble("Spawns." + name + ".pitch"));
			spawn.setYaw((float) arenaConfig.getDouble("Spawns." + name + ".yaw"));
			return spawn;
		}
		return null;
	}

	public String listRegions() {
		String regions = "";
		if (wallRegions == null) {
			regions += "No wall regions.\n";
		}
		else {
			regions += "Wall regions:\n";
			for (int i = 0; i < wallRegions.size(); i++) {
				for (int j = 0; j < wallRegions.get(i).length; j++) {
					Location[] loc = wallRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (buildRegions == null) {
			regions += "No build regions.\n";
		}
		else {
			regions += "Build Regions:\n";
			for (int i = 0; i < buildRegions.size(); i++) {
				for (int j = 0; j < buildRegions.get(i).length; j++) {
					Location[] loc = buildRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (arenaRegions == null) {
			regions += "No arena regions.\n";
		}
		else {
			regions += "Arena regions:\n";
			for (int i = 0; i < arenaRegions.size(); i++) {
				for (int j = 0; j < arenaRegions.get(i).length; j++) {
					Location[] loc = arenaRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		return regions;
	}
	
	public String listSpawns() {
		String info = "";
		if (lobbySpawn != null) {
			info += "Lobby:\n World: " + lobbySpawn.getWorld().getName() + "\n X: " + lobbySpawn.getBlockX() + "\n Y: " + lobbySpawn.getBlockY() + "\n Z: " + lobbySpawn.getBlockZ() + "\n";
		}
		if (blueSpawn != null) {
			info += "Blue Spawn:\n World: " + blueSpawn.getWorld().getName() + "\n X: " + blueSpawn.getBlockX() + "\n Y: " + blueSpawn.getBlockY() + "\n Z: " + blueSpawn.getBlockZ() + "\n";
		}
		if (redSpawn != null) {
			info += "Red Spawn:\n World: " + redSpawn.getWorld().getName() + "\n X: " + redSpawn.getBlockX() + "\n Y: " + redSpawn.getBlockY() + "\n Z: " + redSpawn.getBlockZ() + "\n";
		}
		if (yellowSpawn != null) {
			info += "Blue Spawn:\n World: " + yellowSpawn.getWorld().getName() + "\n X: " + yellowSpawn.getBlockX() + "\n Y: " + yellowSpawn.getBlockY() + "\n Z: " + yellowSpawn.getBlockZ() + "\n";
		}
		if (greenSpawn != null) {
			info += "Green Spawn:\n World: " + greenSpawn.getWorld().getName() + "\n X: " + greenSpawn.getBlockX() + "\n Y: " + greenSpawn.getBlockY() + "\n Z: " + greenSpawn.getBlockZ() + "\n";
		}
		if (info.equals("")) {
			return "No Spawns";
		}
		return info;
	}
	
	/* 
	 * 
	 * Normal Getters and setters 
	 * 
	 */

	public boolean isInProgress() { return inProgress; }
	public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
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
	public Location getLobbySpawn() { return lobbySpawn; }
	public Location getBlueSpawn() { return blueSpawn; }
	public Location getRedSpawn() { return redSpawn; }
	public Location getGreenSpawn() { return greenSpawn; }
	public Location getYellowSpawn() { return yellowSpawn; }
	public ArrayList<Location[]> getWallRegions() { return wallRegions; }
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public int getMaxPlayers() { return maxPlayers; }
	public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
	public int getMinPlayers() { return minPlayers; }
	public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
	public int getPrepTime() { return prepTime; }
	public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
	public ArrayList<Location[]> getArenaRegion() { return arenaRegions; }
	public void setArenaRegion(ArrayList<Location[]> arenaRegion) { this.arenaRegions = arenaRegion; }
	
}

package com.icey.walls.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import com.icey.walls.MainClass;
import com.icey.walls.listeners.ArenaListener;
import com.icey.walls.util.BlockClipboard;
import com.icey.walls.util.PlayerOriginalState;

public class Arena {

	//info about arena
	private MainClass plugin;
	private String name;
	private boolean enabled;
	private boolean waiting;
	private boolean inProgress;
	private boolean wallsFall;
	private int maxPlayers;
	private int minPlayers;
	private int waitingTime;
	private int prepTime;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private ArrayList<Location[]> arenaRegions;
	private ArrayList<Location[]> buildRegions;
	private ArrayList<Location[]> wallRegions;
	private File arenaFile;
	private WallsScoreboard wallsSB;
	private ArenaListener arenaListener;
	private BukkitRunnable wallsCountdown;
	private	FileConfiguration arenaConfig;
	
	//Used during the game
	private HashMap<UUID, PlayerOriginalState> playerOriginalState;
	private ArrayList<UUID> playersInGame;
	private int remainingTeams;
	private ArrayList<UUID> teamRed;
	private ArrayList<UUID> teamGreen;
	private ArrayList<UUID> teamBlue;
	private ArrayList<UUID> teamYellow;
	private BlockClipboard originalArena;
	private ArrayList<Location> wallBlocks;
	private ArrayList<Location> buildRegionBlocks;

	
	public Arena(String name, File arenaFile, MainClass plugin) {
		this.name = name;
		this.enabled = false;
		this.inProgress = false;
		this.waiting = false;
		this.wallsFall = false;
		this.arenaFile = arenaFile;
		this.plugin = plugin;
		this.remainingTeams = 0;
		this.playersInGame = new ArrayList<>();
		this.arenaRegions = new ArrayList<>();
		this.buildRegions = new ArrayList<>();
		this.wallRegions = new ArrayList<>();
		this.teamRed = new ArrayList<>();
		this.teamGreen = new ArrayList<>();
		this.teamBlue = new ArrayList<>();
		this.teamYellow = new ArrayList<>();
		
		this.originalArena = new BlockClipboard();
		this.buildRegionBlocks = new ArrayList<>();
		this.wallBlocks = new ArrayList<>();
		this.playerOriginalState = new HashMap<>();
		this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
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
		if (!inProgress) {
			waiting = true;
			if (playersInGame.size() == 0) {
				loadConfig();
				initializeArena();
				wallsSB = new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", "dummy", DisplaySlot.SIDEBAR);
			}
			Random random = new Random();
			int randNum = random.nextInt(4);
			if (randNum == 0) teamRed.add(player.getUniqueId());
			if (randNum == 1) teamGreen.add(player.getUniqueId());
			if (randNum == 2) teamBlue.add(player.getUniqueId());
			if (randNum == 3) teamYellow.add(player.getUniqueId());
			playersInGame.add(player.getUniqueId());
			playerOriginalState.put(player.getUniqueId(), new PlayerOriginalState(player));
			updateScoreboard();
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.teleport(lobbySpawn);
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setSaturation(100);
			player.setFoodLevel(20);
			player.getActivePotionEffects().clear();
//			player.setBedSpawnLocation(lobbySpawn, true);
			if (playersInGame.size() >= minPlayers) lobbyCountdown();
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

		if (playersInGame.size() < minPlayers && wallsCountdown != null) wallsCountdown.cancel();
		if (playersInGame.size() == 0) stopGame();
	}
	
	public void updateScoreboard() {
		if (waiting) {
			wallsSB.clearSB();
			wallsSB.setPlayers(playersInGame.size());
			wallsSB.setMaxPlayers(maxPlayers);
			wallsSB.setMinPlayers(minPlayers);
			wallsSB.putWaiting();
		}
		
		if (inProgress) {
			wallsSB.clearSB();
			wallsSB.setReds(teamRed.size());
			wallsSB.setGreens(teamGreen.size());
			wallsSB.setBlues(teamBlue.size());
			wallsSB.setYellows(teamYellow.size());
			wallsSB.putPrepTime();
			wallsSB.putPlayersAlive();
		}
		
		for (UUID id : playersInGame) {
			wallsSB.updatePlayersSB(Bukkit.getPlayer(id));
		}
	}
	
	public void lobbyCountdown() {
		wallsCountdown = new WallsLobbyCountdown(waitingTime / 60, waitingTime % 60, wallsSB, playersInGame, this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void startPrep() {
		for (UUID id : teamRed) {
			wallsSB.joinRedTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(redSpawn);
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.RED + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamGreen) {
			wallsSB.joinGreenTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(greenSpawn);
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.GREEN + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamBlue) {
			wallsSB.joinBlueTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(blueSpawn);
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.BLUE + Bukkit.getPlayer(id).getDisplayName());
		}
		for (UUID id : teamYellow) {
			wallsSB.joinYellowTeam(Bukkit.getPlayer(id));
			Bukkit.getPlayer(id).teleport(yellowSpawn);
			Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
			Bukkit.getPlayer(id).setDisplayName(ChatColor.YELLOW + Bukkit.getPlayer(id).getDisplayName());
		}
		if (teamBlue.size() > 0) remainingTeams++;
		if (teamRed.size() > 0) remainingTeams++;
		if (teamGreen.size() > 0) remainingTeams++;
		if (teamYellow.size() > 0) remainingTeams++;
		waiting = false;
		inProgress = true;
		updateScoreboard();
		wallsCountdown = new WallsFallCountdown(prepTime / 60, prepTime % 60, wallsSB, playersInGame, this);
		wallsCountdown.runTaskTimer(plugin, 0, 20);
	}
	
	public void startPvp() {
		wallsFall = true;
		for (Location block : wallBlocks) {
			block.getBlock().setType(Material.AIR);
		}
	}
	
	public void winner() {
		
	}
	
	public void stopGame() {
		inProgress = false;
		waiting = false;
		wallsFall = false;
		for (UUID uuid : playersInGame) {
			playerLeave(Bukkit.getPlayer(uuid));
		}
		playersInGame.clear();
		playerOriginalState.clear();
		originalArena.pasteBlocksInClipboard();
		HandlerList.unregisterAll(arenaListener);
		for (int j = 0; j < arenaRegions.size(); j++) {
			for (int i = 0; i < arenaRegions.get(j)[0].getWorld().getEntities().size(); i++) {
				Location locofEnt = arenaRegions.get(j)[0].getWorld().getEntities().get(i).getLocation();
				if (Math.min(arenaRegions.get(j)[0].getBlockX(), arenaRegions.get(j)[1].getBlockX()) <= locofEnt.getBlockX() &&
				locofEnt.getBlockX() <= Math.max(arenaRegions.get(j)[0].getBlockX(), arenaRegions.get(j)[1].getBlockX()) &&
				Math.min(arenaRegions.get(j)[0].getBlockY(), arenaRegions.get(j)[1].getBlockY()) <= locofEnt.getBlockY() &&
				locofEnt.getBlockY() <= Math.max(arenaRegions.get(j)[0].getBlockY(), arenaRegions.get(j)[1].getBlockY()) &&
				Math.min(arenaRegions.get(j)[0].getBlockZ(), arenaRegions.get(j)[1].getBlockZ()) <= locofEnt.getBlockZ() &&
				locofEnt.getBlockZ() <= Math.max(arenaRegions.get(j)[0].getBlockZ(), arenaRegions.get(j)[1].getBlockZ())) {
					if (arenaRegions.get(j)[0].getWorld().getEntities().get(i) instanceof Item) arenaRegions.get(j)[0].getWorld().getEntities().get(i).remove();
				}
			}
		}
	}
	
	//run this only when one person joins
	public void initializeArena() {
		for (Location[] region : arenaRegions) {
			originalArena.addRegion(region[0], region[1]);
		}
		for (Location[] region : wallRegions) {
			for (int x = Math.min(region[0].getBlockX(), region[1].getBlockX()); x <= Math.max(region[0].getBlockX(), region[1].getBlockX()); x++) {
				for (int y = Math.min(region[0].getBlockY(), region[1].getBlockY()); y <= Math.max(region[0].getBlockY(), region[1].getBlockY()); y++) {
					for (int z = Math.min(region[0].getBlockZ(), region[1].getBlockZ()); z <= Math.max(region[0].getBlockZ(), region[1].getBlockZ()); z++) {
						Location loc = new Location(region[0].getWorld(), x, y, z);
						wallBlocks.add(loc);
					}
				}
			}
		}
		for (Location[] region : buildRegions) {
			for (int x = Math.min(region[0].getBlockX(), region[1].getBlockX()); x <= Math.max(region[0].getBlockX(), region[1].getBlockX()); x++) {
				for (int y = Math.min(region[0].getBlockY(), region[1].getBlockY()); y <= Math.max(region[0].getBlockY(), region[1].getBlockY()); y++) {
					for (int z = Math.min(region[0].getBlockZ(), region[1].getBlockZ()); z <= Math.max(region[0].getBlockZ(), region[1].getBlockZ()); z++) {
						Location loc = new Location(region[0].getWorld(), x, y, z);
						buildRegionBlocks.add(loc);
					}
				}
			}
		}
		arenaListener = new ArenaListener(this, wallBlocks, buildRegionBlocks);
		plugin.getServer().getPluginManager().registerEvents(arenaListener, plugin);
	}
	public void brodcastMsgToArena(String msg, String killer, String killee) {
		for (UUID id : playersInGame) {
			Bukkit.getPlayer(id).sendMessage(killee + ChatColor.GRAY + msg + ChatColor.RESET + killer);
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
		ArrayList<Location[]> inRegion = new ArrayList<Location[]>();
		int i = 1;
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
		else plugin.getLogger().info(this.name + " arena has an invalid config! Check the "+ name +" regions");
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
		if (wallRegions == null) {regions += "No wall regions.\n";}
		else {
			regions += "Wall regions:\n";
			for (int i = 0; i < wallRegions.size(); i++) {
				regions += " Region" + (i+1) +":\n";
				for (int j = 0; j < wallRegions.get(i).length; j++) {
					Location[] loc = wallRegions.get(i);
					regions += "  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (buildRegions == null) {regions += "No build regions.\n";}
		else {
			regions += "Build Regions:\n";
			for (int i = 0; i < buildRegions.size(); i++) {
				regions += " Region" + (i+1) +":\n";
				for (int j = 0; j < buildRegions.get(i).length; j++) {
					Location[] loc = buildRegions.get(i);
					regions += "  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (arenaRegions == null) {regions += "No arena regions.\n";}
		else {
			regions += "Arena regions:\n";
			for (int i = 0; i < arenaRegions.size(); i++) {
				regions += " Region" + (i+1) +":\n";
				for (int j = 0; j < arenaRegions.get(i).length; j++) {
					Location[] loc = arenaRegions.get(i);
					regions += "  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		return regions;
	}
	
	public String listSpawns() {
		String info = "";
		if (lobbySpawn != null) info += "Lobby:\n World: " + lobbySpawn.getWorld().getName() + "\n X: " + lobbySpawn.getBlockX() + "\n Y: " + lobbySpawn.getBlockY() + "\n Z: " + lobbySpawn.getBlockZ() + "\n";
		if (blueSpawn != null) info += "Blue Spawn:\n World: " + blueSpawn.getWorld().getName() + "\n X: " + blueSpawn.getBlockX() + "\n Y: " + blueSpawn.getBlockY() + "\n Z: " + blueSpawn.getBlockZ() + "\n";
		if (redSpawn != null) info += "Red Spawn:\n World: " + redSpawn.getWorld().getName() + "\n X: " + redSpawn.getBlockX() + "\n Y: " + redSpawn.getBlockY() + "\n Z: " + redSpawn.getBlockZ() + "\n";
		if (yellowSpawn != null) info += "Blue Spawn:\n World: " + yellowSpawn.getWorld().getName() + "\n X: " + yellowSpawn.getBlockX() + "\n Y: " + yellowSpawn.getBlockY() + "\n Z: " + yellowSpawn.getBlockZ() + "\n";
		if (greenSpawn != null) info += "Green Spawn:\n World: " + greenSpawn.getWorld().getName() + "\n X: " + greenSpawn.getBlockX() + "\n Y: " + greenSpawn.getBlockY() + "\n Z: " + greenSpawn.getBlockZ() + "\n";
		if (info.equals("")) return "No Spawns";
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
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public int getMaxPlayers() { return maxPlayers; }
	public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
	public int getMinPlayers() { return minPlayers; }
	public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
	public int getPrepTime() { return prepTime; }
	public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
	public MainClass getPlugin() {return plugin;}
	public boolean isWaiting() {return waiting;}
	public void setWaiting(boolean waiting) {this.waiting = waiting;}
	public boolean isWallsFall() {return wallsFall;}
	public void setWallsFall(boolean wallsFall) {this.wallsFall = wallsFall;}

	/**
	 * @return the remainingTeams
	 */
	public int getRemainingTeams() {
		return remainingTeams;
	}

	/**
	 * @param remainingTeams the remainingTeams to set
	 */
	public void setRemainingTeams(int remainingTeams) {
		this.remainingTeams = remainingTeams;
	}
}
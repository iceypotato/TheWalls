package com.icey.walls;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class Arena implements EventListener {

	private MainPluginClass plugin;
	private String name;
	private boolean inProgress;
	private boolean enabled;
	private int maxPlayers;
	private int minPlayers;
	private int prepTime;
	private ArrayList<UUID> playersInGame;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private ArrayList<Location[]> buildRegions;
	private ArrayList<Location[]> wallRegions;
	private File arenaFile;
	private	FileConfiguration arenaConfig;
	
	public Arena(String name, boolean enabled, boolean inProgress, File arenaFile, MainPluginClass plugin) {
		this.name = name;
		this.setEnabled(enabled);
		this.inProgress = inProgress;
		this.playersInGame = new ArrayList<UUID>();
		this.arenaFile = arenaFile;
		this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
		this.plugin = plugin;
	}
	public void loadConfig() {
		setLobbySpawn();
		setBlueSpawn();
		setRedSpawn();
		setYellowSpawn();
		addWallRegion();
	}
	
	public void runGame() {
		
	}
	public void stopGame() {
		
	}
	public void waiting() {
		
	}
	@EventHandler
	public void setup(BlockBreakEvent block) {
		if (buildRegions.get(i)) {
			
		}
	}
	public void pvp() {
		
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayersInGame() {
		return playersInGame;
	}

	public void setPlayersInGame(ArrayList<UUID> playersInGame) {
		this.playersInGame = playersInGame;
	}
	public void setLobbySpawn() {
		if (arenaConfig.contains("Spawns.Lobby.world") && arenaConfig.contains("Spawns.Lobby.x") && arenaConfig.contains("Spawns.Lobby.y") && arenaConfig.contains("Spawns.Lobby.z")) {
			lobbySpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Lobby.world")), arenaConfig.getDouble("Spawns.Lobby.x"), arenaConfig.getDouble("Spawns.Lobby.y"), arenaConfig.getDouble("Spawns.Lobby.x"));
			lobbySpawn.setPitch((float) arenaConfig.getDouble("Spawns.Lobby.pitch"));
			lobbySpawn.setYaw((float) arenaConfig.getDouble("Spawns.Lobby.yaw"));
		}
	}
	public void setBlueSpawn() {
		if (arenaConfig.contains("Spawns.Blue.world") && arenaConfig.contains("Spawns.Blue.x") && arenaConfig.contains("Spawns.Blue.y") && arenaConfig.contains("Spawns.Blue.z")) {
			blueSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Blue.world")), arenaConfig.getDouble("Spawns.Blue.x"), arenaConfig.getDouble("Spawns.Blue.y"), arenaConfig.getDouble("Spawns.Blue.z"));
			blueSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Blue.pitch"));
			blueSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Blue.yaw"));
		}
	}
	public void setRedSpawn() {
		if (arenaConfig.contains("Spawns.Red.world") && arenaConfig.contains("Spawns.Red.x") && arenaConfig.contains("Spawns.Red.y") && arenaConfig.contains("Spawns.Red.z")) {
			redSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Red.world")), arenaConfig.getDouble("Spawns.Red.x"), arenaConfig.getDouble("Spawns.Red.y"), arenaConfig.getDouble("Spawns.Red.z"));
			redSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Red.pitch"));
			redSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Red.yaw"));
		}
	}
	public void setGreenSpawn() {
		if (arenaConfig.contains("Spawns.Green.world") && arenaConfig.contains("Spawns.Green.x") && arenaConfig.contains("Spawns.Green.y") && arenaConfig.contains("Spawns.Green.z")) {
			greenSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Green.world")), arenaConfig.getDouble("Spawns.Green.x"), arenaConfig.getDouble("Spawns.Green.y"), arenaConfig.getDouble("Spawns.Green.z"));
			greenSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Green.pitch"));
			greenSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Green.yaw"));
		}
	}
	public void setYellowSpawn() {
		if (arenaConfig.contains("Spawns.Yellow.world") && arenaConfig.contains("Spawns.Yellow.x") && arenaConfig.contains("Spawns.Yellow.y") && arenaConfig.contains("Spawns.Yellow.z")) {
			yellowSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Yellow.world")), arenaConfig.getDouble("Spawns.Yellow.x"), arenaConfig.getDouble("Spawns.Yellow.y"), arenaConfig.getDouble("Spawns.Yellow.z"));
			yellowSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Yellow.pitch"));
			yellowSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Yellow.yaw"));
		}
	}
	public void addWallRegion() {
		int i = 1;
		while (arenaConfig.contains("Regions.Walls") && !arenaConfig.get("Regions.Walls." + i).equals("")) {
			if (arenaConfig.get("Regions.Walls." + i).equals("") || arenaConfig.get("Regions.Walls." + i) == null) {
				plugin.getLogger().info(name + " arena has an invalid config! Check the Walls region");
				break;
			}
			wallRegions = new ArrayList<Location[]>();
			Location[] region = new Location[2];
			region[0].setWorld(Bukkit.getWorld(arenaConfig.getString("Regions.Walls." + i + "world")));
			region[1].setWorld(Bukkit.getWorld(arenaConfig.getString("Regions.Walls." + i + "world")));
			region[0].setX(arenaConfig.getDouble("Regions.Walls." + i + "R1.x"));
			region[0].setY(arenaConfig.getDouble("Regions.Walls." + i + "R1.y"));
			region[0].setZ(arenaConfig.getDouble("Regions.Walls." + i + "R1.z"));
			region[1].setX(arenaConfig.getDouble("Regions.Walls." + i + "R2.x"));
			region[1].setY(arenaConfig.getDouble("Regions.Walls." + i + "R2.y"));
			region[1].setZ(arenaConfig.getDouble("Regions.Walls." + i + "R2.z"));
			wallRegions.add(region);
			i++;
		}
	}
	public void addBuildRegion() {
		
	}
	public String listWallRegions() {
		String regions = "";
		if (wallRegions == null) {
			return "No wall regions.";
		}
		for (int i = 0; i < wallRegions.size(); i++) {
			for (int j = 0; j < wallRegions.get(i).length; j++) {
				Location[] loc = wallRegions.get(i);
				regions += "R" + j+1 +": X:" + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
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

	public Location getLobbySpawn() {
		return lobbySpawn;
	}

	public Location getBlueSpawn() {
		return blueSpawn;
	}

	public Location getRedSpawn() {
		return redSpawn;
	}

	public Location getGreenSpawn() {
		return greenSpawn;
	}

	public Location getYellowSpawn() {
		return yellowSpawn;
	}
	public ArrayList<Location[]> getWallRegions() {
		return wallRegions;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public int getMinPlayers() {
		return minPlayers;
	}
	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}
	public int getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(int prepTime) {
		this.prepTime = prepTime;
	}

}

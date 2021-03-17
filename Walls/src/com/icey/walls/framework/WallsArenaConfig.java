package com.icey.walls.framework;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.icey.walls.MainClass;

public class WallsArenaConfig {
	
	private String name;
	private WallsArena arena;
	private MainClass plugin;
	private boolean enabled;
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
	private	FileConfiguration arenaFileConfiguration;
	
	public WallsArenaConfig(String name, File arenaFile, MainClass plugin) {
		this.name = name;
		this.enabled = false;
		this.arenaFile = arenaFile;
		this.plugin = plugin;
		arena = new WallsArena(this, plugin);
	}
	
	/**
	 * Loads all settings in the file.
	 */
	public void loadConfig() {
		this.arenaFileConfiguration = YamlConfiguration.loadConfiguration(this.arenaFile);
		readLobbySpawn();
		readBlueSpawn();
		readRedSpawn();
		readYellowSpawn();
		readGreenSpawn();
		addArenaRegion();
		addWallRegion();
		addBuildRegion();
		readSettings();
		if (lobbySpawn == null || blueSpawn == null || redSpawn==null||greenSpawn==null||yellowSpawn==null||
		arenaRegions==null||buildRegions==null||wallRegions==null) {
			enabled = false;
		}
	}
	
	public void readSettings() {
		if (arenaFileConfiguration.get("Settings.enabled") != null) enabled = arenaFileConfiguration.getBoolean("Settings.enabled");
		if (arenaFileConfiguration.get("Settings.waiting-time") != null) waitingTime = arenaFileConfiguration.getInt("Settings.waiting-time");
		if (arenaFileConfiguration.get("Settings.preparation-time") != null) prepTime = arenaFileConfiguration.getInt("Settings.preparation-time");
		if (arenaFileConfiguration.get("Settings.max-players") != null) maxPlayers = arenaFileConfiguration.getInt("Settings.max-players");
		if (arenaFileConfiguration.get("Settings.start-min-players") != null) minPlayers = arenaFileConfiguration.getInt("Settings.start-min-players");
	}
	
	public void addArenaRegion() { arenaRegions = readRegions("Arena"); }
	public void addWallRegion() { wallRegions = readRegions("Walls"); }
	public void addBuildRegion() { buildRegions = readRegions("Build"); }
	
	public ArrayList<Location[]> readRegions(String name) {
		ArrayList<Location[]> inRegion = new ArrayList<Location[]>();
		int i = 1;
		if (arenaFileConfiguration.get("Regions." + name + "." + i) != null) {
			while (i <= arenaFileConfiguration.getConfigurationSection("Regions." + name + "").getKeys(false).toArray().length) {
				if (!(arenaFileConfiguration.get("Regions." + name + "." + i).equals("")) && arenaFileConfiguration.get("Regions." + name + "." + i) != null) {
					Location[] region = new Location[2];
					region[0] = new Location(Bukkit.getWorld(arenaFileConfiguration.getString("Regions." + name + "." + i + ".world")),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos1.x"),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos1.y"),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos1.z"));
					region[1] = new Location(Bukkit.getWorld(arenaFileConfiguration.getString("Regions." + name + "." + i + ".world")),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos2.x"),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos2.y"),
						arenaFileConfiguration.getDouble("Regions." + name + "." + i + ".pos2.z"));
					inRegion.add(region);
					i++;
				}
				else {
					//plugin.getLogger().info(this.name + " arena has an invalid config! Check the "+ name +" regions");
					return null;
				}
			}
			return inRegion;
		}
		//plugin.getLogger().info(this.name + " arena has an invalid config! Check the "+ name +" regions");
		return null;
	}
	
	public void readLobbySpawn() {lobbySpawn = readSpawns("Lobby");}
	public void readBlueSpawn() {blueSpawn = readSpawns("Blue");}
	public void readRedSpawn() {redSpawn = readSpawns("Red");}
	public void readGreenSpawn() {greenSpawn = readSpawns("Green");}
	public void readYellowSpawn() {yellowSpawn = readSpawns("Yellow");}
	public Location readSpawns(String name) {
		if (arenaFileConfiguration.contains("Spawns." + name + ".world") && arenaFileConfiguration.contains("Spawns." + name + ".x") && arenaFileConfiguration.contains("Spawns." + name + ".y") && arenaFileConfiguration.contains("Spawns." + name + ".z")) {
			Location spawn = new Location(Bukkit.getWorld(arenaFileConfiguration.getString("Spawns." + name + ".world")), arenaFileConfiguration.getDouble("Spawns." + name + ".x"), arenaFileConfiguration.getDouble("Spawns." + name + ".y"), arenaFileConfiguration.getDouble("Spawns." + name + ".z"));
			spawn.setPitch((float) arenaFileConfiguration.getDouble("Spawns." + name + ".pitch"));
			spawn.setYaw((float) arenaFileConfiguration.getDouble("Spawns." + name + ".yaw"));
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
	
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public int getMaxPlayers() { return maxPlayers; }
	public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
	public int getMinPlayers() { return minPlayers; }
	public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
	public int getPrepTime() { return prepTime; }
	public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
	public int getWaitingTime() {return waitingTime;}
	public void setWaitingTime(int waitingTime) {this.waitingTime = waitingTime;}
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Location getLobbySpawn() { return lobbySpawn; }
	public Location getBlueSpawn() { return blueSpawn; }
	public Location getRedSpawn() { return redSpawn; }
	public Location getGreenSpawn() { return greenSpawn; }
	public Location getYellowSpawn() { return yellowSpawn; }
	public ArrayList<Location[]> getArenaRegions() {return arenaRegions;}
	public void setArenaRegions(ArrayList<Location[]> arenaRegions) {this.arenaRegions = arenaRegions;}
	public ArrayList<Location[]> getBuildRegions() {return buildRegions;}
	public void setBuildRegions(ArrayList<Location[]> buildRegions) {this.buildRegions = buildRegions;}
	public ArrayList<Location[]> getWallRegions() {return wallRegions;}
	public void setWallRegions(ArrayList<Location[]> wallRegions) {this.wallRegions = wallRegions;}
	public File getArenaFile() {return arenaFile;}
	public void setArenaFile(File arenaFile) {this.arenaFile = arenaFile;}
	public WallsArena getArena() {return arena;}
	public void setArena(WallsArena arena) {this.arena = arena;}
	
}

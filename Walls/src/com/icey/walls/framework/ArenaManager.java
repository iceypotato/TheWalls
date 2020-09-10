package com.icey.walls.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.icey.walls.MainPluginClass;
import com.icey.walls.listeners.Arena;
import com.icey.walls.listeners.WallsTool;

public class ArenaManager {

	private MainPluginClass plugin;
	private FileConfiguration dataConfig;
	private File arenaFolder;
	private File configFile;
	private int numArenas;
	private ArrayList<Arena> arenas;

	public ArenaManager(MainPluginClass plugin) {
		arenas = new ArrayList<Arena>();
		this.plugin = plugin;
		this.numArenas = 0;
	}

	public FileConfiguration getConfigFile(String name) {
		arenaFolder = new File(plugin.getDataFolder(), "arenas");
		configFile = new File(arenaFolder, name + ".yml");
		if (configFile.exists()) {
			dataConfig = YamlConfiguration.loadConfiguration(configFile);
			return dataConfig;
		} else {
			return null;
		}
	}

	public void saveFile(String name, FileConfiguration fileConfiguration) {
		arenaFolder = new File(plugin.getDataFolder(), "arenas");
		configFile = new File(arenaFolder, name + ".yml");
		try {
			fileConfiguration.save(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int createArenaConfig(String name) {
		try {
			arenaFolder = new File(plugin.getDataFolder(), "arenas");
			configFile = new File(arenaFolder, name + ".yml");
			if (configFile.exists() == false) {
				arenaFolder.mkdirs();
				configFile.createNewFile();
				addArena(new Arena(name, false, false, configFile, plugin));
				dataConfig = YamlConfiguration.loadConfiguration(configFile);
				dataConfig.set("Settings.enabled", false);
				dataConfig.set("Settings.waiting-time", 120);
				dataConfig.set("Settings.preparation-time", 10);
				dataConfig.set("Settings.start-min-players", 2);
				dataConfig.set("Settings.max-players", 24);
				dataConfig.set("Spawns.Lobby", "");
				dataConfig.set("Spawns.Blue", "");
				dataConfig.set("Spawns.Red", "");
				dataConfig.set("Spawns.Green", "");
				dataConfig.set("Spawns.Yellow", "");
				dataConfig.set("Regions.Arena.1", "");
				dataConfig.set("Regions.Walls.1", "");
				dataConfig.set("Regions.Build.1", "");
				dataConfig.save(configFile);
				return 0;
			} else {
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean checkConfig(String name) {
		FileConfiguration arenaConfig = getConfigFile(name);
		if (!arenaConfig.contains("Settings.preparation-time") || arenaConfig.get("Settings.preparation-time").equals("") ) {
			return false;
		}
		if (!arenaConfig.contains("Settings.enabled") || arenaConfig.getBoolean("Settings.enabled") == false) return false;
		if (!arenaConfig.contains("Spawns.Lobby") || arenaConfig.get("Spawns.Lobby").equals("")) return false;
		if (!arenaConfig.contains("Spawns.Blue") || arenaConfig.get("Spawns.Blue").equals("")) return false;
		if (!arenaConfig.contains("Spawns.Red") || arenaConfig.get("Spawns.Red").equals("")) return false;
		if (!arenaConfig.contains("Spawns.Green") || arenaConfig.get("Spawns.Green").equals("")) return false;
		if (!arenaConfig.contains("Spawns.Yellow") || arenaConfig.get("Spawns.Yellow").equals("")) return false;
		if (!arenaConfig.contains("Regions.Walls")) return false;
		if (!arenaConfig.contains("Regions.Build")) return false;
		if (!arenaConfig.contains("Regions.Arena")) return false;
		for (int i = 1; i <= arenaConfig.getConfigurationSection("Regions.Walls").getKeys(false).toArray().length; i++) {
			if (arenaConfig.get("Regions.Walls." + i) == null || arenaConfig.get("Regions.Walls." + i).equals("")) {
				return false;
			}
		}
		for (int i = 1; i <= arenaConfig.getConfigurationSection("Regions.Build").getKeys(false).toArray().length; i++) {
			if (arenaConfig.get("Regions.Build." + i) == null || arenaConfig.get("Regions.Build." + i).equals("")) {
				return false;
			}
		}
		for (int i = 1; i <= arenaConfig.getConfigurationSection("Regions.Arena").getKeys(false).toArray().length; i++) {
			if (arenaConfig.get("Regions.Arena." + i) == null || arenaConfig.get("Regions.Arena." + i).equals("")) {
				return false;
			}
		}
		return true;
	}
	
	public void writeSpawns(String name, String spawnName, Player player) {
		FileConfiguration arenaConfig = getConfigFile(name);
		arenaConfig.set("Spawns." + spawnName + ".world", player.getWorld().getName());
		arenaConfig.set("Spawns." + spawnName + ".x", player.getLocation().getBlockX());
		arenaConfig.set("Spawns." + spawnName + ".y", player.getLocation().getBlockY());
		arenaConfig.set("Spawns." + spawnName + ".z", player.getLocation().getBlockZ());
		arenaConfig.set("Spawns." + spawnName + ".yaw", player.getLocation().getYaw());
		arenaConfig.set("Spawns." + spawnName + ".pitch", player.getLocation().getPitch());
		saveFile(name, arenaConfig);
	}
	public void writeRegions(String name, String regionName, Player player,  WallsTool wallsTool) {
		FileConfiguration arenaConfig = getConfigFile(name);
		if (!arenaConfig.contains("Regions." + regionName + ".1")) {
			dataConfig.set("Regions." + regionName + ".1", "");
		}
		int i = 1;
		while (!arenaConfig.getString("Regions." + regionName + "." + i).equals("")) {
			if (arenaConfig.getString("Regions." + regionName + "." + (i + 1)) == null) {
				i++;
				break;
			}
			i++;
		}
		player.sendMessage(i + "");
		arenaConfig.set("Regions." + regionName + "." + i + ".world", wallsTool.getWorld().getName());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos1.x", wallsTool.getPos1().getBlockX());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos1.y", wallsTool.getPos1().getBlockY());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos1.z", wallsTool.getPos1().getBlockZ());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos2.x", wallsTool.getPos2().getBlockX());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos2.y", wallsTool.getPos2().getBlockY());
		arenaConfig.set("Regions." + regionName + "." + i + ".pos2.z", wallsTool.getPos2().getBlockZ());
		saveFile(name, arenaConfig);
	}
	public void writeSettings(String name, String value) {
		FileConfiguration arenaConfig = getConfigFile(name);
		arenaConfig.set("Settings." + name, value);
	}
	
	public void stopAllArenas() {
		for (int i = 0; i < arenas.size(); i++) {
			arenas.get(i).stopGame();
		}
	}
	
	
	public int numArenas() {
		int i = 0;
		while (i < arenas.size()) {
			i++;
		}
		return i;
	}

	public String[] getArenaNames() {
		String[] names = new String[arenas.size()];
		for (int i = 0; i < arenas.size(); i++) {
			names[i] = arenas.get(i).getName();
		}
		return names;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public Arena getArena(String name) {
		for (int i = 0; i < arenas.size(); i++) {
			if (arenas.get(i).getName().equals(name))
				return arenas.get(i);
		}
		return null;
	}

	public void addArena(Arena arena) {
		arenas.add(arena);
	}

	public String listArenas() {
		String arenaList = ChatColor.GOLD + "Arenas: \n";
		for (int i = 0; i < arenas.size(); i++) {
			arenaList += i+1 + ": " + getArenaNames()[i] + "\n";
		}
		return arenaList;
	}

	public void reloadArenas() {
		arenas.clear();
		if (configFile == null) {
			arenaFolder = new File(plugin.getDataFolder(), "arenas");
			if (arenaFolder.exists()) {
				for (int i = 0; i < arenaFolder.list().length; i++) {
					configFile = new File(arenaFolder, arenaFolder.list()[i]);
					String name = configFile.getName().substring(0, configFile.getName().indexOf(".yml"));
					dataConfig = YamlConfiguration.loadConfiguration(configFile);
					addArena(new Arena(name, false, false, configFile, plugin));
					if (checkConfig(name) == false) {
						dataConfig.set("Settings.enabled", false);
					}
					arenas.get(i).loadConfig();
				}
			} else {
				plugin.getLogger().info("No arenas exist.");
			}
		}
	}
}

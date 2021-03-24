package com.icey.walls.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.icey.walls.MainClass;
import com.icey.walls.commands.walls.Arena;
import com.icey.walls.listeners.WallsTool;

public class WallsArenaManagerOld {

	private MainClass plugin;
	private File arenaFolder;
	private File configFile;
	private ArrayList<WallsArenaConfig> arenaConfigs;

	public WallsArenaManagerOld(MainClass plugin) {
		arenaConfigs = new ArrayList<>();
		this.plugin = plugin;
	}

	public FileConfiguration getFileConfigFromFile(String fileName) {
		arenaFolder = new File(plugin.getDataFolder(), "arenas");
		configFile = new File(arenaFolder, fileName + ".yml");
		if (configFile.exists()) {
			FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
			return dataConfig;
		} else {
			return null;
		}
	}
	
	/**
	 * saves the FileConfiguraion to a file, specified by fileName.
	 * @param fileName
	 * @param fileConfiguration
	 */
	public void saveFile(String fileName, FileConfiguration fileConfiguration) {
		arenaFolder = new File(plugin.getDataFolder(), "arenas");
		configFile = new File(arenaFolder, fileName + ".yml");
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
				addArenaConfig(new WallsArenaConfig(name, configFile, plugin));
				FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
				dataConfig.set("Settings.enabled", false);
				dataConfig.set("Settings.waiting-time", 120);
				dataConfig.set("Settings.preparation-time", 900);
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
	
	public boolean checkConfig(String name, CommandSender sender) {
		WallsArenaConfig wallsArenaConfig = getArenaConfig(name);
		boolean configGood = true;
		if (wallsArenaConfig.getLobbySpawn() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No spawnpoint set for Lobby for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getRedSpawn() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No spawnpoint set for Red Team for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getGreenSpawn() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No spawnpoint set for Green Team for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getBlueSpawn() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No spawnpoint set for Blue Team for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getYellowSpawn() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No spawnpoint set for Yellow Team for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getArenaRegions() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No Arena Regions for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getBuildRegions() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No Build Regions for arena: " + name);
			configGood = false;
		}
		if (wallsArenaConfig.getWallRegions() == null) {
			sender.sendMessage(ChatColor.YELLOW + "No Wall Regions for arena: " + name);
			configGood = false;
		}
		return configGood;
	}
	
	public void writeSpawns(String name, String spawnName, Player player) {
		FileConfiguration arenaConfig = getFileConfigFromFile(name);
		arenaConfig.set("Spawns." + spawnName + ".world", player.getWorld().getName());
		arenaConfig.set("Spawns." + spawnName + ".x", player.getLocation().getBlockX());
		arenaConfig.set("Spawns." + spawnName + ".y", player.getLocation().getBlockY());
		arenaConfig.set("Spawns." + spawnName + ".z", player.getLocation().getBlockZ());
		arenaConfig.set("Spawns." + spawnName + ".yaw", player.getLocation().getYaw());
		arenaConfig.set("Spawns." + spawnName + ".pitch", player.getLocation().getPitch());
		saveFile(name, arenaConfig);
		getArenaConfig(name).setArenaFile(configFile);
		getArenaConfig(name).loadConfig();
	}
	
	public void writeRegions(String name, String regionName, Player player,  WallsTool wallsTool) {
		FileConfiguration arenaConfig = getFileConfigFromFile(name);
		if (!arenaConfig.contains("Regions." + regionName + ".1")) {
			arenaConfig.set("Regions." + regionName + ".1", "");
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
		getArenaConfig(name).setArenaFile(configFile);
		getArenaConfig(name).loadConfig();
	}
	
	public void writeSettings(String arena, String name, Object value) {
		FileConfiguration arenaConfig = getFileConfigFromFile(arena);
		arenaConfig.set("Settings." + name, value);
		saveFile(arena, arenaConfig);
	}
	
	public void reloadArenas() {
		stopAllArenas();
		arenaConfigs.clear();
		arenaFolder = new File(plugin.getDataFolder(), "arenas");
		if (arenaFolder.exists() && arenaFolder.list().length > 0) {
			for (int i = 0; i < arenaFolder.list().length; i++) {
				configFile = new File(arenaFolder, arenaFolder.list()[i]);
				String name = configFile.getName().substring(0, configFile.getName().indexOf(".yml"));
				FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(configFile);
				addArenaConfig(new WallsArenaConfig(name, configFile, plugin));
				arenaConfigs.get(i).loadConfig();
				if (checkConfig(name, plugin.getServer().getConsoleSender()) == false) {
					arenaConfig.set("Settings.enabled", false);
					saveFile(name, arenaConfig);
				}
			}
		}
		else plugin.getLogger().info("No arenas exist.");
	}
	
	public void stopAllArenas() {
		for (int i = 0; i < arenaConfigs.size(); i++) {
			arenaConfigs.get(i).getArena().stopGame();
		}
	}
	
	public int numArenas() {
		return arenaConfigs.size();
	}

	public String[] getArenaNames() {
		String[] names = new String[arenaConfigs.size()];
		for (int i = 0; i < arenaConfigs.size(); i++) {
			names[i] = arenaConfigs.get(i).getName();
		}
		return names;
	}

	public WallsArenaConfig getArenaConfig(String name) {
		for (int i = 0; i < arenaConfigs.size(); i++) {
			if (arenaConfigs.get(i).getName().equals(name)) return arenaConfigs.get(i);
		}
		return null;
	}

	public void addArenaConfig(WallsArenaConfig arena) {
		arenaConfigs.add(arena);
	}

	public String listArenas() {
		String arenaList = ChatColor.GOLD + "Arenas: \n";
		for (int i = 0; i < arenaConfigs.size(); i++) {
			arenaList += i+1 + ": " + getArenaNames()[i] + "\n";
		}
		return arenaList;
	}


	public WallsArena getArenaFromPlayer(Player player) {
		for (WallsArenaConfig arenaConfig : arenaConfigs) {
			for (UUID uuid : arenaConfig.getArena().getPlayersInGame()) {
				if (player.getUniqueId().equals(uuid)) return arenaConfig.getArena();
			}
		}
		return null;
	}
	
	/* Getters and Setters */
	
	public ArrayList<WallsArenaConfig> getArenaConfigs() {
		return arenaConfigs;
	}
}

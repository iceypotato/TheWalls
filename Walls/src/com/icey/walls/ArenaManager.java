package com.icey.walls;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
				addArena(new Arena(name, false, false, configFile));
				dataConfig = YamlConfiguration.loadConfiguration(configFile);
				dataConfig.set("Spawns.Lobby", "");
				dataConfig.set("Spawns.Blue", "");
				dataConfig.set("Spawns.Red", "");
				dataConfig.set("Spawns.Green", "");
				dataConfig.set("Spawns.Yellow", "");
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
			if (arenas.get(i).getName() == name)
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
			arenaList += i + ": " + getArenaNames()[i] + "\n";
		}
		return arenaList;
	}

	public void reloadArenas() {
		getArenas().removeAll(getArenas());
		if (configFile == null) {
			arenaFolder = new File(plugin.getDataFolder(), "arenas");
			if (arenaFolder.exists()) {
				for (int i = 0; i < arenaFolder.list().length; i++) {
					configFile = new File(arenaFolder, arenaFolder.list()[i]);
					String name = configFile.getName().substring(0, configFile.getName().indexOf(".yml"));
					dataConfig = YamlConfiguration.loadConfiguration(configFile);
					addArena(new Arena(name, false, false, configFile));
					arenas.get(i).loadConfig(configFile);
				}
			} else {
				plugin.getLogger().info("No arenas exist.");
			}
		}
	}
}

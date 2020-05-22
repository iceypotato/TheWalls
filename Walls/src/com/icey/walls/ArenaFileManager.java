package com.icey.walls;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ArenaFileManager {
	private MainPluginClass plugin;
	private FileConfiguration dataConfig;
	private File configFile;
	private ArenaList arenaList;
	
	public ArenaFileManager(MainPluginClass plugin, ArenaList arenaList) {
		this.plugin = plugin;
		this.arenaList = arenaList;
	}
	public void reloadConfig() {
		if (configFile == null) {
			File arenaFolde = new File(plugin.getDataFolder(), "arenas");
			for (int i = 0; i < arenaFolde.list().length; i++) {
				
				configFile = new File(arenaFolde, arenaFolde.list()[i]);
			}
			
		}
	}

}

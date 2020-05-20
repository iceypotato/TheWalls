package com.icey.walls;

import java.io.File;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Walls;

public class MainPluginClass extends JavaPlugin {

	public HashMap<String, Arena> arenaManagerHashMap = new HashMap<String, Arena>();
	public File arenas;
	public File arenaFolder;
	
	@Override
	public void onEnable() {
		getLogger().info("Walls 1.0 has been enabled.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Walls(this));
		loadConfig();
	}
	@Override
	public void onDisable() {
		getLogger().info("Walls 1.0 has been disabled.");
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}

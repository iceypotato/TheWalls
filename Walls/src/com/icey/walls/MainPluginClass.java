package com.icey.walls;

import java.io.File;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Walls;
import com.icey.walls.framework.ArenaManager;
import com.icey.walls.listeners.WallsTool;

public class MainPluginClass extends JavaPlugin {

	public ArenaManager arenaManager = new ArenaManager(this);
	public WallsTool wallsTool = new WallsTool();
	public String pluginDataFolder = "WallsMinigame";
	
	@Override
	public void onEnable() {
		arenaManager.reloadArenas();
		getLogger().info(arenaManager.numArenas() + " arenas loaded.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Walls(this, arenaManager, wallsTool));
		getServer().getPluginManager().registerEvents(wallsTool, this);
		loadConfig();
		getLogger().info("Walls 1.0 has been enabled.");
	}
	
	@Override
	public void onDisable() {
		arenaManager.stopAllArenas();
		getLogger().info("Walls 1.0 has been disabled.");
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}

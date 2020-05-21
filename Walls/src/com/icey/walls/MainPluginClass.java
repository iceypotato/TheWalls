package com.icey.walls;

import java.io.File;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Walls;
import com.icey.walls.listeners.WallsTool;

public class MainPluginClass extends JavaPlugin {

	public ArenaList arenaMang;
	public File arenas;
	public File arenaFolder;
	
	@Override
	public void onEnable() {
		getLogger().info("Walls 1.0 has been enabled.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Walls(this, arenaMang));
		getServer().getPluginManager().registerEvents(new WallsTool(), this);
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

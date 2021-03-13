package com.icey.walls;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Test;
import com.icey.walls.commands.Walls;
import com.icey.walls.framework.Arena;
import com.icey.walls.framework.ArenaManager;
import com.icey.walls.listeners.TestListener;
import com.icey.walls.listeners.WallsTool;

public class MainClass extends JavaPlugin {

	public ArenaManager arenaManager = new ArenaManager(this);
	public WallsTool wallsTool = new WallsTool();
	public TestListener testL = new TestListener(wallsTool, this);
	public String pluginDataFolder = "WallsMinigame";
	
	@Override
	public void onEnable() {
		arenaManager.reloadArenas();
		getLogger().info(arenaManager.numArenas() + " arenas loaded.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Walls(this, arenaManager, wallsTool));
		this.getCommand("test").setExecutor((CommandExecutor) new Test(this, wallsTool));
		
		getServer().getPluginManager().registerEvents(wallsTool, this);
		getServer().getPluginManager().registerEvents(testL, this);
		
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

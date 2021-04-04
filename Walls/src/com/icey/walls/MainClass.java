package com.icey.walls;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Test;
import com.icey.walls.commands.Walls;
import com.icey.walls.framework.ActionBarSender;
import com.icey.walls.framework.ScoreboardSharedTeams;
import com.icey.walls.framework.WallsArenaManager;
import com.icey.walls.listeners.TestListener;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.versions.v1_8_R3.WallsScoreboardSharedTeams;

public class MainClass extends JavaPlugin {
	
	public String serverVersion = Bukkit.getBukkitVersion();
	public WallsArenaManager arenaManager = new WallsArenaManager(this);
	public WallsTool wallsTool = new WallsTool();
	public TestListener testL = new TestListener(wallsTool, this);
	public String pluginDataFolder = "WallsMinigame";
	
	ActionBarSender actionBar;
	
	@Override
	public void onEnable() {
		if (!isCompatible()) Bukkit.getPluginManager().disablePlugin(this);
		arenaManager.reloadArenas();
		getLogger().info(arenaManager.numArenas() + " arenas found.");
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
	
	public boolean isCompatible() {
		if (serverVersion.contains("1.8.8")) {
			actionBar = new com.icey.walls.versions.v1_8_R3.WallsActionBarSender();
			return true;
		}
		return false;
	}
	
	public ScoreboardSharedTeams getNewScoreboardSharedTeams() {
		if (serverVersion.contains("1.8.8")) {
			return new WallsScoreboardSharedTeams();
		}
		return null;
	}

}

package com.icey.walls;

import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Walls;

public class MainPluginClass extends JavaPlugin {
	
	public HashMap<String, ArenaManager> arenaManagerHashMap = new HashMap<String, ArenaManager>();
	
	@Override
	public void onEnable() {
		getLogger().info("Walls 1.0 has been enabled.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Walls(this));
		
	}
	@Override
	public void onDisable() {
		getLogger().info("Walls 1.0 has been disabled.");
	}

}

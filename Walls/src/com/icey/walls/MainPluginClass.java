package com.icey.walls;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.icey.walls.commands.Info;

public class MainPluginClass extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info("Walls 1.0 has been enabled.");
		this.getCommand("walls").setExecutor((CommandExecutor) new Info(this));
	}
	@Override
	public void onDisable() {
		getLogger().info("Walls 1.0 has been disabled.");
	}

}

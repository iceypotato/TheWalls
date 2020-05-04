package com.icey.walls;

import org.bukkit.plugin.java.JavaPlugin;

public class MainPluginClass extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info("Walls 1.0 has been enabled.");
		getServer().getConsoleSender().sendMessage("Walls 1.0 has been enabled.");
	}
	@Override
	public void onDisable() {
		getLogger().info("Walls 1.0 has been disabled.");
		getServer().getConsoleSender().sendMessage("Walls 1.0 has been disabled.");
	}

}

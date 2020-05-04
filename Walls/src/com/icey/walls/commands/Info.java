package com.icey.walls.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.icey.walls.MainPluginClass;

public class Info implements CommandExecutor {
	private MainPluginClass myplugin;
	
	public Info(MainPluginClass mainClass) {
		myplugin = mainClass;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		sender.sendMessage(ChatColor.GOLD+"Running Walls Minigame Plugin 1.0");
		return true;
	}

}

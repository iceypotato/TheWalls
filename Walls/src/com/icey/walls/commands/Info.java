package com.icey.walls.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import com.icey.walls.MainPluginClass;

public class Info implements CommandExecutor {
	private MainPluginClass myplugin;

	public Info(MainPluginClass mainClass) {
		myplugin = mainClass;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equals("help")) {
				sender.sendMessage(ChatColor.GOLD + "Walls Help Placeholder");
			}
			else {
				sender.sendMessage(ChatColor.RED + "Incorrect Argument! " + ChatColor.GREEN + "do /walls help for the list of commands.");
			}
		}
		else {
			sender.sendMessage(ChatColor.GOLD + "Running Walls Minigame Plugin 1.0. Type /walls help for more.");
		}
		return true;
	}

}

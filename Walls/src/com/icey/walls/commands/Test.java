package com.icey.walls.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.icey.walls.MainClass;
import com.icey.walls.framework.ScoreboardSharedTeams;
import com.icey.walls.listeners.WallsTool;

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	ScoreboardSharedTeams sharedteams;
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
		sharedteams = myplugin.getNewScoreboardSharedTeams();
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("setspawn")) {
				myplugin.location = player.getLocation();
			}
			else if (args[0].equalsIgnoreCase("seename")) {
				player.sendMessage(player.getDisplayName());
			}
			else {
				sender.sendMessage("Invalid subcommand.");
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid subcommand.");
		}
		return false;
	}

}

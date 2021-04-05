package com.icey.walls.commands;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.icey.walls.MainClass;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.versions.v1_8_R3.WallsScoreboardSharedTeams;

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	WallsScoreboardSharedTeams sharedteams = new WallsScoreboardSharedTeams();
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("rand")) {
				for(String msg : myplugin.arenaManager.getDeathMsgStrings()) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
				}
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

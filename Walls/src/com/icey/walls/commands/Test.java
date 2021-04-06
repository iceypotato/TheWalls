package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.icey.walls.MainClass;
import com.icey.walls.framework.ScoreboardSharedTeams;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.versions.v1_8_R3.WallsScoreboardSharedTeams;

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
			if (args[0].equalsIgnoreCase("clearwt")) {
				wallsTool.setPos1(null);
				wallsTool.setPos2(null);
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

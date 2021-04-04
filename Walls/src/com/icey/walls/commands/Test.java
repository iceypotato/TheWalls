package com.icey.walls.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.icey.walls.MainClass;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.scoreboard.WallsScoreboard;
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
			if (args[0].equalsIgnoreCase("getsb")) {
				Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
				sharedteams.addTeamsToScoreboard(scoreboard);
				player.setScoreboard(scoreboard);
			}
			else if (args[0].equalsIgnoreCase("leaveteams")) {
				sharedteams.leaveTeam(player, "red");
				sharedteams.leaveTeam(player, "green");
				sharedteams.leaveTeam(player, "blue");
				sharedteams.leaveTeam(player, "yellow");
			}
			else if (args[0].equalsIgnoreCase("jointeam")) {
				Random random = new Random();
				int randInt = random.nextInt(4);
				if (randInt==0) sharedteams.joinTeam(player, "red");
				if (randInt==1) sharedteams.joinTeam(player, "green");
				if (randInt==2) sharedteams.joinTeam(player, "blue");
				if (randInt==3) sharedteams.joinTeam(player, "yellow");
			}
			else if (args[0].equalsIgnoreCase("leavesb")) {
				player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			}
			else if (args[0].equalsIgnoreCase("list")) {
				int i = 1;
				for (Player p : Bukkit.getOnlinePlayers()) {
					Bukkit.getConsoleSender().sendMessage(p.getName() + "'s Scoreboard: " + p.getScoreboard());
					Bukkit.getConsoleSender().sendMessage("Teams: ");
					for(Team t : p.getScoreboard().getTeams()) {
						Bukkit.getConsoleSender().sendMessage(t.getName()+" "+t);
					}
					i++;
				}
			}
			else if (args[0].equalsIgnoreCase("listppl")) {
				for (Team t : sharedteams.getTeams().values()) {
					Bukkit.getConsoleSender().sendMessage(t.getEntries()+"");
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

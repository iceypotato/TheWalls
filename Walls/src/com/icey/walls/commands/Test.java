package com.icey.walls.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.icey.walls.MainClass;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.scoreboard.WallsScoreboard;

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	private HashMap<Player, WallsScoreboard> playerScoreboards;
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
		playerScoreboards = new HashMap<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("changename")) {
				player.setDisplayName(ChatColor.BLUE + player.getDisplayName() + ChatColor.RESET);
				player.setPlayerListName(ChatColor.BLUE + player.getDisplayName());
			}
			else if (args[0].equalsIgnoreCase("resetname")) {
				player.setDisplayName(player.getName());
				player.setPlayerListName(player.getName());
			}
			else if (args[0].equalsIgnoreCase("rmsb")) {
				player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			}
			else if (args[0].equalsIgnoreCase("addkill")) {
				playerScoreboards.get(player).setKills(playerScoreboards.get(player).getKills()+1);
			}
			else if (args[0].equalsIgnoreCase("updatesb")) {
				playerScoreboards.get(player).clearSB();
				playerScoreboards.get(player).putKills();
			}
			else if (args[0].equalsIgnoreCase("joinred")) {
				//playerScoreboards.get(player).joinRedTeam(player);
			}
			else if (args[0].equalsIgnoreCase("switchsb")) {
			}
			else if (args[0].equalsIgnoreCase("whatteam")) {
			}
			else {
				sender.sendMessage("Invalid subcommand");
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid subcommand.");
		}
		return false;
	}

}

package com.icey.walls.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import com.icey.walls.MainPluginClass;

public class WallsCmd implements CommandExecutor, TabCompleter {
	private MainPluginClass myplugin;

	public WallsCmd(MainPluginClass mainClass) {
		myplugin = mainClass;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equals("help")) {
				sender.sendMessage(ChatColor.GOLD + "Walls Help Placeholder");
			}
			else if (args[0].equals("setup")) {
				
			}
			else if (args[0].equals("admin")) {
				sender.sendMessage("Poopluser");
			}
			else if (args[0].equals("pooploser")) {
				sender.sendMessage("lmao trolled");
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> tabList = new ArrayList<String>();
		if (args.length == 1) {
			tabList.add("help");
			tabList.add("setup");
			tabList.add("admin");
			tabList.add("pooploser");
		}
		return tabList;
	}
}

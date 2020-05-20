package com.icey.walls.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.icey.walls.Arena;

import com.icey.walls.MainPluginClass;

public class Walls implements CommandExecutor, TabCompleter {
	private MainPluginClass myplugin;

	public Walls(MainPluginClass mainClass) {
		myplugin = mainClass;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {

			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.GOLD + "Walls Help Placeholder");
			}

			else if (args[0].equalsIgnoreCase("arena")) {
				
				if (args.length >= 2) {
					//Create arena
					if (args[1].equalsIgnoreCase("create")) {
						if (args.length != 3) {
							sender.sendMessage(ChatColor.RED + "No arena name specified! /walls create arena <name>");
						}
						else {
							String name = args[2];
							try {
								myplugin.arenaFolder = new File(myplugin.getDataFolder() , "arenas");
								myplugin.arenas = new File(myplugin.arenaFolder, name + ".yml");
								if (myplugin.arenas.exists() == false) {
									myplugin.arenaFolder.mkdirs();
									myplugin.arenas.createNewFile();
								} 
								else {
									sender.sendMessage("Arena " + name + " Already Exists!");
								}
								myplugin.arenaManagerHashMap.put(name, new Arena(name, false, false));
								sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.WHITE + name + ChatColor.GREEN + " Created!");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				//Display cmds
				else {
					sender.sendMessage("Walls Setup Commands");
				}
			}

			else if (args[0].equalsIgnoreCase("admin")) {
				sender.sendMessage("Poopluser");
			}

			else if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(ChatColor.GREEN + "Reloading Config...");
				myplugin.loadConfig();
				sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Walls Config Reloaded!");
			}

			else {
				sender.sendMessage(ChatColor.RED + "Incorrect Argument! " + ChatColor.GREEN
						+ "do /walls help for the list of commands.");
			}

		} else {
			sender.sendMessage(ChatColor.GOLD + "Running Walls Minigame Plugin 1.0. Type /walls help for more.");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		ArrayList<String> tabList = new ArrayList<String>();
		if (args.length == 1) {
			tabList.add("help");
			tabList.add("arena");
			tabList.add("admin");
			tabList.add("pooploser");
		}
		return tabList;
	}
}

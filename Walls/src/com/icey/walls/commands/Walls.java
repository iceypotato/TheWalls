package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.icey.walls.ArenaManager;
import com.icey.walls.MainPluginClass;
import com.icey.walls.listeners.WallsTool;

public class Walls implements CommandExecutor, TabCompleter {
	private MainPluginClass myplugin;
	private ArenaManager arenaManager;
	private FileConfiguration arenaConfig;
	private WallsTool wallsTool;

	public Walls(MainPluginClass mainClass, ArenaManager arenaManager) {
		myplugin = mainClass;
		this.arenaManager = arenaManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {

			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.GOLD + "Walls Help Page: 1/1");
				sender.sendMessage(ChatColor.AQUA + "/walls arena" + ChatColor.RESET + " Walls Arena Commands");
				sender.sendMessage(ChatColor.AQUA + "/walls admin" + ChatColor.RESET + " Walls Admin Commands");
				sender.sendMessage(ChatColor.AQUA + "/walls reload" + ChatColor.RESET + " Reload The Walls plugin");
			}
			else if (args[0].equalsIgnoreCase("listarenas")) {
				sender.sendMessage(arenaManager.listArenas());
			}
			//Arena
			else if (args[0].equalsIgnoreCase("arena")) {
				if (args.length >= 2) {
					//Create arena
					if (args[1].equalsIgnoreCase("create")) {
						if (args.length != 3) {
							sender.sendMessage(ChatColor.RED + "No arena name specified! /walls create arena <name>");
						}
						else {
							String name = args[2];
							int result = arenaManager.createArenaConfig(name);
							if (result == 1) sender.sendMessage(ChatColor.RED + "Arena " + name + " already exists!");
							if (result == 0) sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.WHITE + name + ChatColor.GREEN + " Created!");
							if (result == -1) sender.sendMessage(ChatColor.RED + "An error has occured. Check the console.");
						}
					}
					//walls tool
					else if (args[1].equalsIgnoreCase("tool")) {
						if (sender instanceof Player) {
							wallsTool = new WallsTool();
							Player p = (Player) sender;
							wallsTool.giveTool(p);
						}
						else {
							sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
						}
					}
					//Specifying an arena
					else {
						String name = args[1];
						arenaConfig = arenaManager.getConfigFile(name);
						if (arenaConfig != null) {
							if (args.length >= 3) {
								if (sender instanceof Player) {
									if (args[2].equalsIgnoreCase("setlobby")) {
										if (myplugin.wallsTool.getRegion1() != null) {
											arenaConfig.set("Spawns.Lobby.World", myplugin.wallsTool.getWorld().getName());
											arenaConfig.set("Spawns.Lobby.X", myplugin.wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Spawns.Lobby.Y", myplugin.wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Spawns.Lobby.Z", myplugin.wallsTool.getRegion1().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage(ChatColor.GOLD + "Lobby spawnpoint set for arena " + ChatColor.AQUA+ name);
										}
										else {
											sender.sendMessage(ChatColor.RED + "Select a region first!");
										}
									}
									else if (args[2].equalsIgnoreCase("setblue")) {
										if (myplugin.wallsTool.getRegion1() != null) {
											arenaConfig.set("Spawns.Blue.World", myplugin.wallsTool.getWorld().getName());
											arenaConfig.set("Spawns.Blue.X", myplugin.wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Spawns.Blue.Y", myplugin.wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Spawns.Blue.Z", myplugin.wallsTool.getRegion1().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage(ChatColor.BLUE + "Blue Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
										}
										else {
											sender.sendMessage(ChatColor.RED + "Select a region first!");
										}

									}
									else if (args[2].equalsIgnoreCase("setgreen")) {
										if (myplugin.wallsTool.getRegion1() != null) {
											arenaConfig.set("Spawns.Green.world", myplugin.wallsTool.getWorld().getName());
											arenaConfig.set("Spawns.Green.X", myplugin.wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Spawns.Green.Y", myplugin.wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Spawns.Green.Z", myplugin.wallsTool.getRegion1().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage(ChatColor.GREEN + "Green Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
										}
										else {
											sender.sendMessage(ChatColor.RED + "Select a region first!");
										}

									}
									else if (args[2].equalsIgnoreCase("setred")) {
										if (myplugin.wallsTool.getRegion1() != null) {
											arenaConfig.set("Spawns.Red.world", myplugin.wallsTool.getWorld().getName());
											arenaConfig.set("Spawns.Red.X", myplugin.wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Spawns.Red.Y", myplugin.wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Spawns.Red.Z", myplugin.wallsTool.getRegion1().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage(ChatColor.RED + "Red Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
										}
										else {
											sender.sendMessage(ChatColor.RED + "Select a region first!");
										}

									}
									else if (args[2].equalsIgnoreCase("setyellow")) {
										if (myplugin.wallsTool.getRegion1() != null) {
											arenaConfig.set("Spawns.Yellow.world", myplugin.wallsTool.getWorld().getName());
											arenaConfig.set("Spawns.Yellow.X", myplugin.wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Spawns.Yellow.Y", myplugin.wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Spawns.Yellow.Z", myplugin.wallsTool.getRegion1().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage(ChatColor.YELLOW + "Yellow Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
										}
										else {
											sender.sendMessage(ChatColor.RED + "Select a region first!");
										}

									}
									else if (args[2].equalsIgnoreCase("addwall")) {
										if (myplugin.wallsTool.getRegion1() != null && myplugin.wallsTool.getRegion2() != null) {
											int i = 1;
											if (arenaConfig.getInt("Regions.Walls." + i) != 0) {
												i = arenaConfig.getInt("Regions.Walls." + i);
												arenaConfig.set("Regions.Walls." + i +".R1.X", myplugin.wallsTool.getRegion1().getBlockX());
												arenaConfig.set("Regions.Walls." + i +".R1.Y", myplugin.wallsTool.getRegion1().getBlockY());
												arenaConfig.set("Regions.Walls." + i +".R1.Z", myplugin.wallsTool.getRegion1().getBlockZ());
												arenaConfig.set("Regions.Walls." + i +".R2.X", myplugin.wallsTool.getRegion2().getBlockX());
												arenaConfig.set("Regions.Walls." + i +".R2.Y", myplugin.wallsTool.getRegion2().getBlockY());
												arenaConfig.set("Regions.Walls." + i +".R2.Z", myplugin.wallsTool.getRegion2().getBlockZ());
												arenaManager.saveFile(name, arenaConfig);
												
											}

										}
									}
									else if (args[2].equalsIgnoreCase("addbuild")) {
										
									}
									
								}
								else {
									sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
								}
							}
							else {
								sender.sendMessage("no arena command specified!");
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + "Arena " + name + " does not exist! Do /walls arena create <arenaname>");
						}
					}
				}
				
				//Display cmds
				else {
					sender.sendMessage("Walls Setup Commands");
				}
			}
			
			
			
			
			//Admin
			else if (args[0].equalsIgnoreCase("admin")) {
				sender.sendMessage("Walls Admin Commands");
			}
			
			
			//Reload
			else if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(ChatColor.GREEN + "Reloading Config...");
				myplugin.loadConfig();
				myplugin.arenaManager.reloadArenas();
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
		return tabList;
	}
}

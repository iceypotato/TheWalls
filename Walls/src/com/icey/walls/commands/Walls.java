package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

	public Walls(MainPluginClass mainClass, ArenaManager arenaManager, WallsTool wallsTool) {
		myplugin = mainClass;
		this.arenaManager = arenaManager;
		this.wallsTool = wallsTool;
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
								if (args[2].equalsIgnoreCase("info")) {
									sender.sendMessage(arenaManager.getArena(name).listSpawns());
									sender.sendMessage(arenaManager.getArena(name).listWallRegions());
								}
								else if (sender instanceof Player) {
									Player player = (Player) sender;
									if (args[2].equalsIgnoreCase("setlobby")) {
										arenaConfig.set("Spawns.Lobby.world", player.getWorld().getName());
										arenaConfig.set("Spawns.Lobby.x", player.getLocation().getBlockX());
										arenaConfig.set("Spawns.Lobby.y", player.getLocation().getBlockY());
										arenaConfig.set("Spawns.Lobby.z", player.getLocation().getBlockZ());
										arenaConfig.set("Spawns.Lobby.yaw", player.getLocation().getYaw());
										arenaConfig.set("Spawns.Lobby.pitch", player.getLocation().getPitch());
										arenaManager.saveFile(name, arenaConfig);
										sender.sendMessage(ChatColor.GOLD + "Lobby spawnpoint set for arena " + ChatColor.AQUA+ name);
									}
									else if (args[2].equalsIgnoreCase("setblue")) {
										arenaConfig.set("Spawns.Blue.world", player.getWorld().getName());
										arenaConfig.set("Spawns.Blue.x", player.getLocation().getBlockX());
										arenaConfig.set("Spawns.Blue.y", player.getLocation().getBlockY());
										arenaConfig.set("Spawns.Blue.z", player.getLocation().getBlockZ());
										arenaConfig.set("Spawns.Blue.yaw", player.getLocation().getYaw());
										arenaConfig.set("Spawns.Blue.pitch", player.getLocation().getPitch());
										arenaManager.saveFile(name, arenaConfig);
										sender.sendMessage(ChatColor.BLUE + "Blue Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setgreen")) {
										arenaConfig.set("Spawns.Green.world", player.getWorld().getName());
										arenaConfig.set("Spawns.Green.x", player.getLocation().getBlockX());
										arenaConfig.set("Spawns.Green.y", player.getLocation().getBlockY());
										arenaConfig.set("Spawns.Green.z", player.getLocation().getBlockZ());
										arenaConfig.set("Spawns.Green.yaw", player.getLocation().getYaw());
										arenaConfig.set("Spawns.Green.pitch", player.getLocation().getPitch());
										arenaManager.saveFile(name, arenaConfig);
										sender.sendMessage(ChatColor.GREEN + "Green Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setred")) {
										arenaConfig.set("Spawns.Red.world", player.getWorld().getName());
										arenaConfig.set("Spawns.Red.x", player.getLocation().getBlockX());
										arenaConfig.set("Spawns.Red.y", player.getLocation().getBlockY());
										arenaConfig.set("Spawns.Red.z", player.getLocation().getBlockZ());
										arenaConfig.set("Spawns.Red.yaw", player.getLocation().getYaw());
										arenaConfig.set("Spawns.Red.pitch", player.getLocation().getPitch());
										arenaManager.saveFile(name, arenaConfig);
										sender.sendMessage(ChatColor.RED + "Red Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setyellow")) {
										arenaConfig.set("Spawns.Yellow.world", player.getWorld().getName());
										arenaConfig.set("Spawns.Yellow.x", player.getLocation().getBlockX());
										arenaConfig.set("Spawns.Yellow.y", player.getLocation().getBlockY());
										arenaConfig.set("Spawns.Yellow.z", player.getLocation().getBlockZ());
										arenaConfig.set("Spawns.Yellow.yaw", player.getLocation().getYaw());
										arenaConfig.set("Spawns.Yellow.pitch", player.getLocation().getPitch());
										arenaManager.saveFile(name, arenaConfig);
										sender.sendMessage(ChatColor.YELLOW + "Yellow Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("addwall")) {
										if (wallsTool.getRegion1() != null && wallsTool.getRegion2() != null) {
											int i = 1;
											sender.sendMessage(arenaConfig.getConfigurationSection("Regions.Walls").getKeys(false).toArray().length + "");
											while (!arenaConfig.getString("Regions.Walls." + i).equals("")) {
												if (arenaConfig.getString("Regions.Walls." + (i+1)) == null) {
													i++;
													break;
												}
												i++;
											}
											sender.sendMessage(i + "");
											arenaConfig.set("Regions.Walls." + i + ".world", wallsTool.getWorld().getName());
											arenaConfig.set("Regions.Walls." + i + ".R1.x", wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Regions.Walls." + i + ".R1.y", wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Regions.Walls." + i + ".R1.z", wallsTool.getRegion1().getBlockZ());
											arenaConfig.set("Regions.Walls." + i + ".R2.x", wallsTool.getRegion2().getBlockX());
											arenaConfig.set("Regions.Walls." + i + ".R2.y", wallsTool.getRegion2().getBlockY());
											arenaConfig.set("Regions.Walls." + i + ".R2.z", wallsTool.getRegion2().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage("wall added for " + name);

										}
										else {
											sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
										}
									}
									else if (args[2].equalsIgnoreCase("addbuild")) {
										if (wallsTool.getRegion1() != null && wallsTool.getRegion2() != null) {
											int i = 1;
											sender.sendMessage(arenaConfig.getConfigurationSection("Regions.Walls").getKeys(false).toArray().length + "");
											while (!arenaConfig.getString("Regions.Walls." + i).equals("")) {
												if (arenaConfig.getString("Regions.Walls." + (i+1)) == null) {
													i++;
													break;
												}
												i++;
											}
											sender.sendMessage(i + "");
											arenaConfig.set("Regions.Build." + i + ".R1.x", wallsTool.getRegion1().getBlockX());
											arenaConfig.set("Regions.Build." + i + ".R1.y", wallsTool.getRegion1().getBlockY());
											arenaConfig.set("Regions.Build." + i + ".R1.z", wallsTool.getRegion1().getBlockZ());
											arenaConfig.set("Regions.Build." + i + ".R2.x", wallsTool.getRegion2().getBlockX());
											arenaConfig.set("Regions.Build." + i + ".R2.y", wallsTool.getRegion2().getBlockY());
											arenaConfig.set("Regions.Build." + i + ".R2.z", wallsTool.getRegion2().getBlockZ());
											arenaManager.saveFile(name, arenaConfig);
											sender.sendMessage("wall added for " + name);

										}
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

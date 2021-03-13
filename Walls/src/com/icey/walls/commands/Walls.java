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
import com.icey.walls.MainClass;
import com.icey.walls.framework.ArenaManager;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.util.BlockClipboard;

public class Walls implements CommandExecutor, TabCompleter {
	
	private MainClass myplugin;
	private ArenaManager arenaManager;
	private FileConfiguration arenaConfig;
	private WallsTool wallsTool;
	private BlockClipboard protectedBlocks;

	public Walls(MainClass mainClass, ArenaManager arenaManager, WallsTool wallsTool) {
		myplugin = mainClass;
		this.arenaManager = arenaManager;
		this.wallsTool = wallsTool;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			
			// help \\
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.GOLD + "Walls Help Page: 1/1");
				sender.sendMessage(ChatColor.AQUA + "/walls arena" + ChatColor.RESET + " Walls Arena Commands");
				sender.sendMessage(ChatColor.AQUA + "/walls listarenas" + ChatColor.RESET + " List Arenas");
				sender.sendMessage(ChatColor.AQUA + "/walls admin" + ChatColor.RESET + " Walls Admin Commands");
				sender.sendMessage(ChatColor.AQUA + "/walls reload" + ChatColor.RESET + " Reload The Walls plugin");
			}
			// listarenas \\
			else if (args[0].equalsIgnoreCase("listarenas")) {
				sender.sendMessage(arenaManager.listArenas());
			}
			// join \\
			else if (args[0].equalsIgnoreCase("join")) {
				if (args.length == 1) sender.sendMessage("You must specify an arena");
				else if (args.length >= 2 && sender instanceof Player) {
					Player player = (Player) sender;
					if (arenaManager.getArena(args[1]) == null) sender.sendMessage(ChatColor.RED + "That arena does not exist!");
					else if (arenaManager.getArenaFromPlayer(player) != null) sender.sendMessage(ChatColor.RED+"You must leave your current arena before joining another.");
					else if (arenaManager.getArena(args[1]).isEnabled()) {
						sender.sendMessage(ChatColor.GREEN + "You have joined " + args[1] + ".");
						arenaManager.getArena(args[1]).playerJoin(player);
					}
					else sender.sendMessage(ChatColor.RED + "Arena: " + args[1] + " is not enabled!");
				}
				else sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
			}
			
			// leave \\
			else if (args[0].equalsIgnoreCase("leave")) {
				if (args.length >= 1 && sender instanceof Player) {
					Player player = (Player) sender;
					if (arenaManager.getArenaFromPlayer(player) != null) {
						arenaManager.getArenaFromPlayer(player).playerLeave(player);
						sender.sendMessage(ChatColor.GOLD + "You have left the arena.");
					}
					else sender.sendMessage(ChatColor.YELLOW + "You have to join an arena to leave one.");
				}
				else sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
			}
			
			// arena \\
			else if (args[0].equalsIgnoreCase("arena")) {
				if (args.length >= 2) {
					//Create arena
					if (args[1].equalsIgnoreCase("create")) {
						if (args.length != 3) {
							sender.sendMessage(ChatColor.RED + "No arena name specified! /walls arena create <name>");
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
							sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
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
									sender.sendMessage(arenaManager.getArena(name).listRegions());
								}
								else if (args[2].equalsIgnoreCase("stop")){
									arenaManager.getArena(name).stopGame();
								}
								else if (args[2].equalsIgnoreCase("enable")) {
									sender.sendMessage(args[1]);
									if (arenaManager.checkConfig(args[1])) {
										arenaManager.getArena(args[1]).setEnabled(true);
										arenaManager.writeSettings(args[1], "enabled", true);
										sender.sendMessage("Arena " + args[1] + " enabled.");
									}
									else sender.sendMessage("Error: cannot enable arena. Check the config.");
								}
								else if (sender instanceof Player) {
									Player player = (Player) sender;
									if (args[2].equalsIgnoreCase("setlobby")) {
										arenaManager.writeSpawns(name, "Lobby", player);
										sender.sendMessage(ChatColor.GOLD + "Lobby spawnpoint set for arena " + ChatColor.AQUA+ name);
									}
									else if (args[2].equalsIgnoreCase("setblue")) {
										arenaManager.writeSpawns(name, "Blue", player);
										sender.sendMessage(ChatColor.BLUE + "Blue Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setgreen")) {
										arenaManager.writeSpawns(name, "Green", player);
										sender.sendMessage(ChatColor.GREEN + "Green Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setred")) {
										arenaManager.writeSpawns(name, "Red", player);
										sender.sendMessage(ChatColor.RED + "Red Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("setyellow")) {
										arenaManager.writeSpawns(name, "Yellow", player);
										sender.sendMessage(ChatColor.YELLOW + "Yellow Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
									}
									else if (args[2].equalsIgnoreCase("addarenaregion")) {
										if (wallsTool.getPos1() == null || wallsTool.getPos1() == null) { sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!"); }
										else {
											arenaManager.writeRegions(name, "Arena", player, wallsTool);
											sender.sendMessage("Arena region added for " + name);
										}
									}
									else if (args[2].equalsIgnoreCase("addwall")) {
										if (wallsTool.getPos1() == null || wallsTool.getPos1() == null) {
											sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
										}
										else {
											arenaManager.writeRegions(name, "Walls", player, wallsTool);
											sender.sendMessage("Wall region added for " + name);
										}
									}
									else if (args[2].equalsIgnoreCase("addbuild")) {
										if (wallsTool.getPos2() == null || wallsTool.getPos2() == null) {
											sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
										}
										else {
											arenaManager.writeRegions(name, "Build", player, wallsTool);
											sender.sendMessage("Build region added for " + name);
										}
									}
								}
								else {
									sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
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

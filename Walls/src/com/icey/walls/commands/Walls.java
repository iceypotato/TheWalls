package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import com.icey.walls.MainPluginClass;
import com.icey.walls.framework.ArenaManager;
import com.icey.walls.framework.BlockClipboard;
import com.icey.walls.listeners.WallsTool;

public class Walls implements CommandExecutor, TabCompleter {
	private MainPluginClass myplugin;
	private ArenaManager arenaManager;
	private FileConfiguration arenaConfig;
	private WallsTool wallsTool;
	private BlockClipboard protectedBlocks;

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
				sender.sendMessage(ChatColor.AQUA + "/walls listarenas" + ChatColor.RESET + " List Arenas");
				sender.sendMessage(ChatColor.AQUA + "/walls admin" + ChatColor.RESET + " Walls Admin Commands");
				sender.sendMessage(ChatColor.AQUA + "/walls reload" + ChatColor.RESET + " Reload The Walls plugin");
			}
			else if (args[0].equalsIgnoreCase("listarenas")) {
				sender.sendMessage(arenaManager.listArenas());
			}
			else if (args[0].equalsIgnoreCase("join")) {
				if (args.length >= 2 && sender instanceof Player) {
					Player player = (Player) sender;
					if (arenaManager.getArena(args[1]).isEnabled()) {
						player.teleport(arenaManager.getArena(args[1]).getLobbySpawn());
					}
					else sender.sendMessage(ChatColor.RED + "Arena: " + args[1] + " is not enabled!");
				}
				else {
					sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
				}
			}
			else if (args[0].equalsIgnoreCase("addcopy")) {
				if (protectedBlocks == null) {
					protectedBlocks = new BlockClipboard();
				}
				if (!protectedBlocks.getBlockList().isEmpty()) {
					sender.sendMessage("Clipboard was not empty");
				}
				for (int x = Math.min(wallsTool.getPos1().getBlockX(), wallsTool.getPos2().getBlockX()); x <= Math.max(wallsTool.getPos1().getBlockX(), wallsTool.getPos2().getBlockX()); x++) {
					for (int y = Math.min(wallsTool.getPos1().getBlockY(), wallsTool.getPos2().getBlockY()); y <= Math.max(wallsTool.getPos1().getBlockY(), wallsTool.getPos2().getBlockY()); y++) {
						for (int z = Math.min(wallsTool.getPos1().getBlockZ(), wallsTool.getPos2().getBlockZ()); z <= Math.max(wallsTool.getPos1().getBlockZ(), wallsTool.getPos2().getBlockZ()); z++) {
							Location loc = new Location(wallsTool.getWorld(), x, y, z);
							sender.sendMessage(loc.toString());
							protectedBlocks.addBlock(loc.getBlock());
						}
					}
				}
				sender.sendMessage("copied");
			}
			else if (args[0].equalsIgnoreCase("paste")) {
				protectedBlocks.pasteBlocksInClipboard();
				sender.sendMessage("pasted");
			}
			else if (args[0].equalsIgnoreCase("listcopy")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("nothing");
				}
				else {
					for (int i = 0; i < protectedBlocks.getBlockList().size(); i++) {
						sender.sendMessage(protectedBlocks.listBlocksInClipboard());
					}
				}
			}
			else if (args[0].equalsIgnoreCase("listitems")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("nothing");
				}
				else {
					for (int i = 0; i < protectedBlocks.getBlockList().size(); i++) {
						protectedBlocks.listItemStacks();
					}
				}
			}
			else if (args[0].equalsIgnoreCase("clear")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("Empty");
				}
				else {
					protectedBlocks.clear();
					sender.sendMessage("cleared.");
				}
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
									arenaManager.getArena(name).setEnabled(true);
									arenaManager.writeSettings("enabled", "true");
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
									else if (args[2].equalsIgnoreCase("addwall")) {
										if (wallsTool.getPos1() == null || wallsTool.getPos1() == null) {
											sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
										}
										else {
											arenaManager.writeRegions(name, "Walls", player, wallsTool);
											sender.sendMessage("wall added for " + name);
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

package com.icey.walls.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.icey.walls.Arena;
import com.icey.walls.ArenaList;
import com.icey.walls.MainPluginClass;
import com.icey.walls.listeners.WallsTool;

public class Walls implements CommandExecutor, TabCompleter {
	private MainPluginClass myplugin;
	private ArenaList arenaList;
	private FileConfiguration arenaConfig;
	private WallsTool wallsTool;
	private File arenaFile;
	private File arenaFolder;

	public Walls(MainPluginClass mainClass, ArenaList arenaList) {
		myplugin = mainClass;
		this.arenaList = arenaList;
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
				sender.sendMessage(arenaList.toString());
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
							try {
								arenaFolder = new File(myplugin.getDataFolder() , "arenas");
								arenaFile = new File(arenaFolder, name + ".yml");
								if (arenaFile.exists() == false) {
									arenaFolder.mkdirs();
									arenaFile.createNewFile();
									arenaList.addArena(new Arena(name, false, false, arenaFile));
									sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.WHITE + name + ChatColor.GREEN + " Created!");
								} 
								else {
									sender.sendMessage("Arena " + name + " Already Exists!");
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					//walls tool
					else if (args[1].equalsIgnoreCase("tool")) {
						if (sender instanceof Player) {
							WallsTool tool = new WallsTool();
							Player p = (Player) sender;
							tool.giveTool(p);
						}
						else {
							sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
						}
					}
					//Specifying an arena
					else {
						String name = args[1];
						arenaFolder = new File(myplugin.getDataFolder(), "arenas");
						arenaFile = new File(arenaFolder, name + ".yml");
						if (arenaFile.exists() == true) {
							if (args.length >= 3) {
								if (args[2].equalsIgnoreCase("lobbyspawn")) {
									if (myplugin.wallsTool.getRegion1() != null) {
										arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
										sender.sendMessage(arenaConfig.getCurrentPath());
										arenaConfig.set("lobbyspawn.world", myplugin.wallsTool.getWorld().toString());
										arenaConfig.set("lobbyspawn.coordX", myplugin.wallsTool.getRegion1().getBlockX());
										arenaConfig.set("lobbyspawn.coordY", myplugin.wallsTool.getRegion1().getBlockY());
										arenaConfig.set("lobbyspawn.coordZ", myplugin.wallsTool.getRegion1().getBlockZ());
										try {
											arenaConfig.save(arenaFile);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									else {
										sender.sendMessage(ChatColor.RED + "Select a region first!");
									}

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

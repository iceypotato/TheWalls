package com.icey.walls.commands.walls;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.icey.walls.commands.SubCommand;
import com.icey.walls.commands.Walls;
import com.icey.walls.listeners.WallsTool;

public class Arena extends SubCommand {

	public String name;
	
	public Arena(CommandExecutor superCommand) {
		super(superCommand);
		this.name = "arena";
		setAliases(new ArrayList<>());
		getAliases().add(name);
	}

	// arena arg[0] arg[1]...
	@Override
	public boolean onSubCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Walls wallsSupercommand = (Walls) getSuperCommand();
			//Create arena
			if (args[0].equalsIgnoreCase("create")) {
				if (args.length != 2) {
					sender.sendMessage(ChatColor.RED + "No arena name specified! /walls arena create <name>");
				}
				else {
					String name = args[1];
					int result = wallsSupercommand.getArenaManager().createArenaConfig(name);
					if (result == 1) sender.sendMessage(ChatColor.RED + "Arena " + name + " already exists!");
					if (result == 0) sender.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.WHITE + name + ChatColor.GREEN + " Created!");
					if (result == -1) sender.sendMessage(ChatColor.RED + "An error has occured. Check the console.");
				}
			}
			//walls tool
			else if (args[0].equalsIgnoreCase("tool")) {
				if (sender instanceof Player) {
					wallsSupercommand.setWallsTool(new WallsTool());
					Player p = (Player) sender;
					wallsSupercommand.getWallsTool().giveTool(p);
				}
				else {
					sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
				}
			}
			//Specifying an arena
			else {
				String name = args[0];
				wallsSupercommand.setArenaConfig(wallsSupercommand.getArenaManager().getFileConfigFromFile(name));
				if (wallsSupercommand.getArenaConfig() != null) {
					if (args.length > 1) {
						if (args[1].equalsIgnoreCase("info")) {
							sender.sendMessage(wallsSupercommand.getArenaManager().getArenaConfig(name).listSpawns());
							sender.sendMessage(wallsSupercommand.getArenaManager().getArenaConfig(name).listRegions());
						}
						else if (args[1].equalsIgnoreCase("stop")){
							wallsSupercommand.getArenaManager().getArenaConfig(name).getArena().stopGame();
						}
						else if (args[1].equalsIgnoreCase("enable")) {
							if (wallsSupercommand.getArenaManager().checkConfig(args[0], sender)) {
								wallsSupercommand.getArenaManager().getArenaConfig(args[0]).setEnabled(true);
								wallsSupercommand.getArenaManager().writeSettings(args[0], "enabled", true);
								sender.sendMessage("Arena " + args[0] + " enabled.");
							}
							else sender.sendMessage("Error: cannot enable arena. Errors must be fixed in the config in order to enable arena. Do /walls reload when finished.");
						}
						else if (sender instanceof Player) {
							Player player = (Player) sender;
							if (args[1].equalsIgnoreCase("setlobby")) {
								wallsSupercommand.getArenaManager().writeSpawns(name, "Lobby", player);
								sender.sendMessage(ChatColor.GOLD + "Lobby spawnpoint set for arena " + ChatColor.AQUA+ name);
							}
							else if (args[1].equalsIgnoreCase("setblue")) {
								wallsSupercommand.getArenaManager().writeSpawns(name, "Blue", player);
								sender.sendMessage(ChatColor.BLUE + "Blue Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
							}
							else if (args[1].equalsIgnoreCase("setgreen")) {
								wallsSupercommand.getArenaManager().writeSpawns(name, "Green", player);
								sender.sendMessage(ChatColor.GREEN + "Green Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
							}
							else if (args[1].equalsIgnoreCase("setred")) {
								wallsSupercommand.getArenaManager().writeSpawns(name, "Red", player);
								sender.sendMessage(ChatColor.RED + "Red Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
							}
							else if (args[1].equalsIgnoreCase("setyellow")) {
								wallsSupercommand.getArenaManager().writeSpawns(name, "Yellow", player);
								sender.sendMessage(ChatColor.YELLOW + "Yellow Team " + ChatColor.GOLD + "spawnpoint set for arena " + ChatColor.AQUA + name);
							}
							else if (args[1].equalsIgnoreCase("addarenaregion")) {
								if (wallsSupercommand.getWallsTool().getPos1() == null || wallsSupercommand.getWallsTool().getPos1() == null) { sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!"); }
								else {
									wallsSupercommand.getArenaManager().writeRegions(name, "Arena", player, wallsSupercommand.getWallsTool());
									sender.sendMessage("Arena region added for " + name);
								}
							}
							else if (args[1].equalsIgnoreCase("addwall")) {
								if (wallsSupercommand.getWallsTool().getPos1() == null || wallsSupercommand.getWallsTool().getPos1() == null) {
									sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
								}
								else {
									wallsSupercommand.getArenaManager().writeRegions(name, "Walls", player, wallsSupercommand.getWallsTool());
									sender.sendMessage("Wall region added for " + name);
								}
							}
							else if (args[1].equalsIgnoreCase("addbuild")) {
								if (wallsSupercommand.getWallsTool().getPos2() == null || wallsSupercommand.getWallsTool().getPos2() == null) {
									sender.sendMessage(ChatColor.RED + "Region 1 and Region 2 needs to be selected!");
								}
								else {
									wallsSupercommand.getArenaManager().writeRegions(name, "Build", player, wallsSupercommand.getWallsTool());
									sender.sendMessage("Build region added for " + name);
								}
							}
							else sender.sendMessage("Invalid action specified!");
						}
						else if (!(sender instanceof Player)) sender.sendMessage(ChatColor.RED + "You must be an online player to do this!");
						else sender.sendMessage("Invalid action specified!");
					}
					else {
						sender.sendMessage("no actions specified!");
						sender.sendMessage("Possible actions:");
						sender.sendMessage("info, stop, enable");
					}
				}
				else sender.sendMessage(ChatColor.RED + "Arena " + name + " does not exist! Do /walls arena create <arenaname>");
			}
			return true;
		}
		else {
			sender.sendMessage(ChatColor.GOLD + "Walls Arena Commands:");
			sender.sendMessage(ChatColor.AQUA + "/walls arena tool: " + ChatColor.RESET + "used to setup an arena. Region 1 is used to set spawnpoints.");
			sender.sendMessage(ChatColor.AQUA + "/walls arena create <arenaname>: " + ChatColor.RESET + "used to create an arena.");
			sender.sendMessage(ChatColor.AQUA + "/walls arena <arenaname> stop: " + ChatColor.RESET + "used to stop an arena.");
			sender.sendMessage(ChatColor.AQUA + "/walls arena <arenaname> info: " + ChatColor.RESET + "gives information about an arena.");
			sender.sendMessage(ChatColor.AQUA + "/walls arena <arenaname> enable: " + ChatColor.RESET + "will attempt to enable an arena.");
		}
		return true;
	}
}

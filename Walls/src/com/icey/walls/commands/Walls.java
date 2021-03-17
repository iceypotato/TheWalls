package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.icey.walls.MainClass;
import com.icey.walls.commands.walls.Admin;
import com.icey.walls.commands.walls.Arena;
import com.icey.walls.framework.WallsArenaManager;
import com.icey.walls.listeners.WallsTool;

public class Walls implements CommandExecutor, TabCompleter {
	
	private MainClass myplugin;
	private WallsArenaManager arenaManager;
	private FileConfiguration arenaConfig;
	private List<SubCommand> subcmds;
	private WallsTool wallsTool;
	
	public Walls(MainClass mainClass, WallsArenaManager arenaManager, WallsTool wallsTool) {
		myplugin = mainClass;
		this.arenaManager = arenaManager;
		this.wallsTool = wallsTool;
		subcmds = new ArrayList<>();
		subcmds.add(new Arena(this));
		subcmds.add(new Admin(this));
	}

	
	
	//Walls args[0] args[1] args[2]...
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0 ) {
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
					if (arenaManager.getArenaConfig(args[1]) == null) sender.sendMessage(ChatColor.RED + "That arena does not exist!");
					else if (arenaManager.getArenaFromPlayer(player) != null) sender.sendMessage(ChatColor.RED+"You must leave your current arena before joining another.");
					else if (arenaManager.getArenaConfig(args[1]).isEnabled()) {
						sender.sendMessage(ChatColor.GREEN + "Joining arena " + ChatColor.AQUA + args[1]);
						arenaManager.getArenaConfig(args[1]).getArena().playerJoin(player);
						sender.sendMessage(ChatColor.GREEN + "You have joined " + args[1] + ".");
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
			// reload \\
			else if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(ChatColor.GREEN + "Reloading Config...");
				myplugin.loadConfig();
				myplugin.arenaManager.reloadArenas();
				sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Walls Config Reloaded!");
			}
			//Other subcommands
			else {
				String[] subargs = (args.length > 1) ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
				boolean foundCommand = false;
				for (int i=0; i < subcmds.size() && !foundCommand; i++) {
					for (int j=0; j < subcmds.get(i).getAliases().size() && !foundCommand; j++) {
						if (subcmds.get(i).getAliases().get(j).contains(args[0])) {
							subcmds.get(i).onSubCommand(sender, command, label, subargs);
							foundCommand = true;
						}
					}
				}
				if (!foundCommand) {
					sender.sendMessage(ChatColor.RED + "Incorrect Argument! " + ChatColor.GREEN + "do /walls help for the list of commands.");
				}
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
		return tabList;
	}

	public WallsArenaManager getArenaManager() {
		return arenaManager;
	}

	public void setArenaManager(WallsArenaManager arenaManager) {
		this.arenaManager = arenaManager;
	}

	public FileConfiguration getArenaConfig() {
		return arenaConfig;
	}

	public void setArenaConfig(FileConfiguration arenaConfig) {
		this.arenaConfig = arenaConfig;
	}

	public WallsTool getWallsTool() {
		return wallsTool;
	}

	public void setWallsTool(WallsTool wallsTool) {
		this.wallsTool = wallsTool;
	}
	
	
}

package com.icey.walls.commands.walls;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.icey.walls.commands.SubCommand;
import com.icey.walls.commands.Walls;
import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsArenaConfig;

public class Admin extends SubCommand {
	
	public Admin(CommandExecutor superCommand) {
		super(superCommand);
		setAliases( new ArrayList<>());
		getAliases().add("admin");
		getAliases().add("ad");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, Command command, String label, String[] args) {
		Walls wallsCmd = (Walls) getSuperCommand();
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("forcestop")) {
				if (args.length > 1) {
					WallsArenaConfig arenaConfig = wallsCmd.getArenaManager().getArenaConfig(args[1]);
					if (arenaConfig.getArena().isRunning()) {
						sender.sendMessage(args[1] + " stopped.");
						arenaConfig.getArena().stopGame();
					}
					if (arenaConfig != null) arenaConfig.getArena().stopGame();
					else sender.sendMessage(args[1] + " arena does not exist.");
				}
				else {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						WallsArena wallsArena = wallsCmd.getArenaManager().getArenaFromPlayer(player);
						if (wallsArena != null) {
							if (wallsArena.isRunning()) {
								sender.sendMessage(wallsCmd.getArenaManager().getArenaFromPlayer(player).getConfig().getName() + " stopped.");
								wallsArena.stopGame();
							}
							else sender.sendMessage("Arena not running.");
						}
						else sender.sendMessage("You are not in an arena. You must specify the arena name.");
					}
					else sender.sendMessage("You are not a player. You must specify the arena name.");
				}
			}
			else if (args[0].equalsIgnoreCase("nextphase")) {
				if (args.length > 1) {
					WallsArenaConfig arenaConfig = wallsCmd.getArenaManager().getArenaConfig(args[1]);
					if (arenaConfig != null) {
						WallsArena wallsArena = arenaConfig.getArena();
						if (wallsArena.isRunning()) {
							if (wallsArena.isWaiting()) wallsArena.startPrep();
							else if (!wallsArena.isWallsFall()) wallsArena.startPvp();
							else if (!wallsArena.isSuddenDeath()) wallsArena.suddenDeath();
							else if (!wallsArena.isEnding()) wallsArena.winner();
						}
						else sender.sendMessage("Arena not running.");
					}
					else sender.sendMessage(args[1] + " arena does not exist.");
				}
				else {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						WallsArena wallsArena = wallsCmd.getArenaManager().getArenaFromPlayer(player);
						if (wallsArena != null) {
							if (wallsArena.isRunning()) {
								if (wallsArena.isWaiting()) wallsArena.startPrep();
								else if (!wallsArena.isWallsFall()) wallsArena.startPvp();
								else if (!wallsArena.isSuddenDeath()) wallsArena.suddenDeath();
								else if (!wallsArena.isEnding()) wallsArena.winner();
							}
							else sender.sendMessage("Arena not running.");
						}
						else sender.sendMessage("You are not in an arena. You must specify the arena name.");
					}
					else sender.sendMessage("You are not a player. You must specify the arena name.");
				}
			}
			else {
				sender.sendMessage("Incorrect argument.");
			}
		}
		else {
			sender.sendMessage("Walls Admin Help:");
			sender.sendMessage("/walls admin forcestop [arena]: Force stops the current arena you are in. If you are not in an arena, you must specify the arena.");
			sender.sendMessage("/walls admin nextphase [arena]: Forces the next game phase of an arena. Only works if there are people in an arena.");
		}
		return true;
	}
}

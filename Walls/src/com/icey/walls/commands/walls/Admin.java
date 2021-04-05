package com.icey.walls.commands.walls;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.icey.walls.commands.SubCommand;

public class Admin extends SubCommand {
	
	public Admin(CommandExecutor superCommand) {
		super(superCommand);
		setAliases( new ArrayList<>());
		getAliases().add("admin");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			String arenaName = args[0];
		}
		else {
			sender.sendMessage("Walls Admin Help:");
			sender.sendMessage("/walls forcestop <arena>");
		}
		return true;
	}
}

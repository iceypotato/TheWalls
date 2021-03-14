package com.icey.walls.commands.walls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.icey.walls.commands.SubCommand;

public class Admin implements SubCommand {
	
	public CommandExecutor superCommand;
	public List<String> aliases;
	
	public Admin(CommandExecutor superCommand) {
		this.superCommand = superCommand;
		aliases = new ArrayList<>();
		aliases.add("admin");
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


	@Override
	public void setSuperCommand(CommandExecutor executor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return this.aliases;
	}

	@Override
	public void setAliases(List<String> aliases) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommandExecutor getSuperCommand() {
		// TODO Auto-generated method stub
		return this.superCommand;
	}
}

package com.icey.walls.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
	
	CommandExecutor commandExecutor;
	List<String> aliases;
	
	public SubCommand(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}
	
	public abstract boolean onSubCommand(CommandSender sender, Command command, String label, String[] args);
	
	public CommandExecutor getSuperCommand() {
		return commandExecutor;
	}
	
	public void setSuperCommand(CommandExecutor executor) {
		
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public void setAliases(List<String> aliases) {
		
	}
}

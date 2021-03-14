package com.icey.walls.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface SubCommand {
	
	public boolean onSubCommand(CommandSender sender, Command command, String label, String[] args);
	
	public CommandExecutor getSuperCommand();
	
	public void setSuperCommand(CommandExecutor executor);
	
	public List<String> getAliases();
	
	public void setAliases(List<String> aliases);
}

package com.icey.walls.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.icey.walls.MainClass;
import com.icey.walls.framework.WallsArenaManager;

public class WallsTabCompleter implements TabCompleter {
	
	List<String> arguments = new ArrayList<>();
	MainClass plugin;
	WallsArenaManager wallsArenaManager;
	
	public WallsTabCompleter(MainClass plugin, WallsArenaManager wallsArenaManager) {
		this.plugin = plugin;
		this.wallsArenaManager = wallsArenaManager;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		arguments.clear();
		List<String> result = new ArrayList<String>();
		if (args.length == 1) {
			arguments.add("arena"); arguments.add("admin"); arguments.add("reload"); arguments.add("list");
			arguments.add("join"); arguments.add("leave"); arguments.add("help");
			for (String a : arguments) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) result.add(a);
			}
			return result;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("arena")) {
				arguments.add("tool"); arguments.add("create");
				for (String s : wallsArenaManager.getArenaNames()) if (!arguments.contains(s)) arguments.add(s);
				for (String a : arguments) if (a.toLowerCase().startsWith(args[1].toLowerCase())) result.add(a);
				return result;
			}
			if (args[0].equalsIgnoreCase("admin")) {
				arguments.add("forcestop"); arguments.add("nextphase");
				for (String a : arguments) if (a.toLowerCase().startsWith(args[1].toLowerCase())) result.add(a);
				return result;
			}

		}
		return null;
	}
}

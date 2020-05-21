package com.icey.walls;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaList {
	
	private ArrayList<Arena> arenas; 
	
	public ArenaList() {
		arenas = new ArrayList<Arena>();
	}
	
	public ArrayList<Arena> getArenas() {
		return arenas;
	}
	
	public void addArena(Arena arena) {
		arenas.add(arena);
	}
	
	public String toString() {
		String arenaList = ChatColor.GOLD + "Arenas: \n";
		for (int i = 0; i < arenas.size(); i++) {
			arenaList += i + ": " + arenas.get(i).getName() + "\n";
		}
		return arenaList;
	}
	
}

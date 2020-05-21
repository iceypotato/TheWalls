package com.icey.walls;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public class ArenaList {
	
	ArrayList<Arena> arenas;
	
	public ArenaList() {
		arenas = new ArrayList<Arena>();
	}
	
	public ArrayList<Arena> getArenas() {
		return arenas;
	}
	
	public String toString() {
		String arenaList = ChatColor.GOLD + "Arenas: \n";
		for (int i = 0; i < arenas.size(); i++) {
			arenaList += i + ": " + arenas.get(i).getName() + "\n";
		}
		return arenaList;
	}
	
}

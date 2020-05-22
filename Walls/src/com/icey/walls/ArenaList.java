package com.icey.walls;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaList {
	
	private int numArenas;
	private ArrayList<Arena> arenas; 
	
	public ArenaList() {
		arenas = new ArrayList<Arena>();
	}
	
	public int numArenas() {
		int i = 0;
		while(0 < arenas.size()) {
			i++;
		}
		return i;
	}
	
	public ArrayList<Arena> getArenas() {
		return arenas;
	}
	public Arena getArena(String name) {
		for (int i = 0; i < arenas.size(); i++) {
			if (arenas.get(i).getName() == name) return arenas.get(i);
		}
		return null;
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

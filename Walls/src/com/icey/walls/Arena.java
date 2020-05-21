package com.icey.walls;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Arena {
	
	private String name;
	private boolean inProgress;
	private boolean enabled;
	private ArrayList<UUID> playersInGame;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private ArrayList<Location> buildRegions;
	private ArrayList<Location> wallRegions;
	private File arenaConfiguration;
	
	public Arena(String name, boolean enabled, boolean inProgress, File config) {
		this.name = name;
		this.enabled = enabled;
		this.inProgress = inProgress;
		this.playersInGame = new ArrayList<UUID>();

	}
	
	public void runGame() {
		FileConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(this.arenaConfiguration);
		arenaConfiguration.addDefault("spawn", lobbySpawn);
	}
	public void waiting() {
		
	}
	public void setup() {
		
	}
	public void pvp() {
		
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayersInGame() {
		return playersInGame;
	}

	public void setPlayersInGame(ArrayList<UUID> playersInGame) {
		this.playersInGame = playersInGame;
	}

	public Location getLobbySpawn() {
		return lobbySpawn;
	}

	public void setLobbySpawn(Location lobbySpawn) {
		this.lobbySpawn = lobbySpawn;
	}

}

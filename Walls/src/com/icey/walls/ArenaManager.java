package com.icey.walls;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;

public class ArenaManager {
	
	private String name;
	private int id;
	private boolean inProgress;
	private boolean enabled;
	private ArrayList<UUID> playersInGame;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	
	public ArenaManager(String name, int id, boolean enabled, boolean inProgress, ArrayList<UUID> playersInGame, Location lobbySpawn) {
		this.setName(name);
		this.setId(id);
		this.enabled = enabled;
		this.inProgress = inProgress;
		this.playersInGame = playersInGame;
		this.lobbySpawn = lobbySpawn;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<UUID> getPlayersInGame() {
		return playersInGame;
	}

	public void setPlayersInGame(ArrayList<UUID> playersInGame) {
		this.playersInGame = playersInGame;
	}

}

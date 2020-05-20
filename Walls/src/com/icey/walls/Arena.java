package com.icey.walls;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;

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
	
	public Arena(String name, boolean enabled, boolean inProgress) {
		this.name = name;
		this.enabled = enabled;
		this.inProgress = inProgress;
		this.playersInGame = new ArrayList<UUID>();
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

}

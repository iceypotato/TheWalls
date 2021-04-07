package com.icey.walls.framework;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WallsPlayer {
	
	private Player player;
	private String playerDisplayName;
	private WallsTeam team;
	private boolean eliminated;
	
	public WallsPlayer(Player player) {
		this.player = player;
		eliminated = false;
	}
	
	public String getPlayerDisplayName() {
		return team.getPrefix() + playerDisplayName;
	}
	
	public void setPlayerDisplayName(String playerDisplayName) {
		this.playerDisplayName = playerDisplayName;
	}
	
	/**
	 * Do not use this to set teams. Instead, add a player using a WallsTeam object.
	 * @param team
	 */
	void setTeam(WallsTeam team) {
		this.team = team;
	}
	
	public WallsTeam getTeam() {
		return team;
	}

	public boolean isEliminated() {
		return eliminated;
	}

	public void setEliminated(boolean eliminated) {
		this.eliminated = eliminated;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}

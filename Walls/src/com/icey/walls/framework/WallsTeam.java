package com.icey.walls.framework;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class WallsTeam {

	private List<Player> players;
	private List<Player> alivePlayers;
	private String name;
	private String prefix;
	private String suffix;
	private boolean eliminated;
	
	public WallsTeam(String name, String prefix, String suffix) {
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
		players = new ArrayList<>();
		alivePlayers = new ArrayList<>();
	}
	
	public WallsTeam(String name) {
		this.name = name;
		prefix = "";
		suffix = "";
		players = new ArrayList<>();
		alivePlayers = new ArrayList<>();
	}
	
	public void addPlayer(Player p) {
		players.add(p);
		alivePlayers.add(p);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
		alivePlayers.remove(p);
	}
	
	public void removeAlivePlayer(Player p) {
		alivePlayers.remove(p);
	}
	
	public List<Player> getAlivePlayers() {
		return this.alivePlayers;
	}
	
	/**
	 * Gets the nunmber of all players that are not dead on this team.
	 * @return players that are not eliminated
	 */
	public int getNumPlayersAliveOnTeam() {
		return alivePlayers.size();
	}
	/**
	 * Gets the number of all players on this team.
	 * @return players.size
	 */
	public int getNumPlayersOnTeam() {
		return players.size();
	}
	
	/**
	 * Checks if alivePlayers isEmpty, then updates eliminated.
	 */
	public void checkEliminated() {
		eliminated = alivePlayers.isEmpty() ? true : false;
	}
	
	/*
	 * Getters and Setters
	 */
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public boolean isEliminated() {
		return eliminated;
	}
	
	public void setEliminated(boolean eliminated) {
		this.eliminated = eliminated;
	}
	
}

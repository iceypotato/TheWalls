package com.icey.walls.framework;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface ScoreboardSharedTeams {

	public void addTeamsToScoreboard(Scoreboard scoreboard);
	
	public void joinTeam(Player player, String name);
	
	public void leaveTeam(Player player, String name);
	
}

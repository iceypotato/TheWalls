package com.icey.walls.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class WallsSubScoreboard {
	
	WallsScoreboard superScoreboard;
	ScoreboardManager scoreboardManager;
	Scoreboard scoreboard;
	Objective health;

	public WallsSubScoreboard(WallsScoreboard superScoreboard) {
		this.superScoreboard = superScoreboard;
		scoreboardManager = Bukkit.getScoreboardManager();
		scoreboard = scoreboardManager.getNewScoreboard();
		health = superScoreboard.getHealthObjective();
	}
	
}

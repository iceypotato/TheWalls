package com.icey.walls.framework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class WallsScoreboard {
	
	public static void setScoreboard(Player player) {
		int minutes = 0;
		int seconds = 0;
		int reds = 10;
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Objective objective = board.registerNewObjective(ChatColor.GOLD+""+ChatColor.BOLD + "Walls", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score time = objective.getScore("Walls Fall in " + ChatColor.GREEN + minutes + ":" + seconds);
		Score redRemaining = objective.getScore(ChatColor.RED + "Red: " + ChatColor.WHITE + reds);
		time.setScore(1);
		player.setScoreboard(board);
	}
	
}

package com.icey.walls.framework;

import java.util.Timer;
import java.util.TimerTask;

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
	
	private int minutes;
	private int seconds;
	private int reds;
	private int greens;
	private int blues;
	private int yellows;
	private ScoreboardManager manager;
	private Scoreboard board;
	private Objective objective;
	private Timer wallsFall;
	String name;
	String displayName;
	String criteria;
	DisplaySlot slot;
	
	public WallsScoreboard(int minutes, int seconds, int reds, int greens, int blues, int yellows, String name, String displayName, String criteria, DisplaySlot slot) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.reds = reds;
		this.greens = greens;
		this.blues = blues;
		this.yellows = yellows;
		this.name = name;
		this.displayName = displayName;
		this.criteria = criteria;
		this.slot = slot;
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
	}
	public void addScore(Score score, String text) {
		
	}
	
	// Put the players alive on the scoreboard
	public void putPlayersAlive() {
		String reds = this.reds+"";
		String greens = this.greens+"";
		String blues = this.blues+"";
		String yellows = this.yellows+"";
		if (this.reds == 0) reds = ChatColor.RED + "✘";
		if (this.greens == 0) greens = ChatColor.RED + "✘";
		if (this.blues == 0) blues = ChatColor.RED + "✘";
		if (this.yellows == 0) yellows = ChatColor.RED + "✘";
		Score redRemaining = objective.getScore(ChatColor.RED + "Red" + ChatColor.WHITE+": " + reds);
		Score greenRemaining = objective.getScore(ChatColor.GREEN + "Green" + ChatColor.WHITE+": " + greens);
		Score blueRemaining = objective.getScore(ChatColor.BLUE + "Blue" + ChatColor.WHITE+": " + blues);
		Score yellowRemaining = objective.getScore(ChatColor.YELLOW + "Yellow" + ChatColor.WHITE+": " + yellows);

		redRemaining.setScore(4);
		greenRemaining.setScore(3);
		blueRemaining.setScore(2);
		yellowRemaining.setScore(1);
	}
	
	//put the prep time on the scoreboard
	public void putPrepTime() {
		Score time;
		if (seconds != -1) {
			String seconds = this.seconds+"";
			if (seconds.length() == 1) seconds = "0" + seconds;
			time = objective.getScore("Walls Fall in " + ChatColor.GREEN + minutes + ":" + seconds);
			time.setScore(6);
		}
		Score space1 = objective.getScore("");
	}
	public void putWaitingTime() {
		Score space1 = objective.getScore("");
		space1.setScore(2);
		Score time;
		if (seconds != -1) {
			String seconds = this.seconds+"";
			if (seconds.length() == 1) seconds = "0" + seconds;
			time = objective.getScore("Waiting For Players: " + ChatColor.GREEN + minutes + ":" + seconds);
			time.setScore(1);
		}
		Score space2 = objective.getScore("");
		space2.setScore(0);
	}
	//Displays the scoreboard to the player.
	public void updatePlayersSB(Player player) {
		player.setScoreboard(board);
	}
	
	//clears the scoreboard, but keep the name and objective
	public void clearSB() {
		if (objective != null) objective.unregister();
		objective = board.registerNewObjective(name, criteria);
		objective.setDisplayName(displayName);
		objective.setDisplaySlot(slot);
	}

	public void startTimer(Player player) {
		WallsCountdown wallsCD = new WallsCountdown(minutes, seconds, this, player);
		wallsFall = new Timer();
		wallsFall.schedule(wallsCD, 0, 1000);
	}
	
	public void cancelTimer() {
		wallsFall.cancel();
	}
	
	public int getReds() {
		return reds;
	}

	public void setReds(int reds) {
		this.reds = reds;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getGreens() {
		return greens;
	}

	public void setGreens(int greens) {
		this.greens = greens;
	}

	public int getBlues() {
		return blues;
	}

	public void setBlues(int blues) {
		this.blues = blues;
	}

	public int getYellows() {
		return yellows;
	}

	public void setYellows(int yellows) {
		this.yellows = yellows;
	}
	public Objective getObjective() {
		return this.objective;
	}
}

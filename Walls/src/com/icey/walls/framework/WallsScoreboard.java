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
	
	private int minutes;
	private int seconds;
	private int players;
	private int minPlayers;
	private int maxPlayers;
	private int reds;
	private int greens;
	private int blues;
	private int yellows;
	private ScoreboardManager manager;
	private Team redTeam;
	private Team greenTeam;
	private Team blueTeam;
	private Team yellowTeam;
	private Scoreboard board;
	private Objective objective;
	private Objective health;
	private String name;
	private String displayName;
	private String criteria;
	private DisplaySlot slot;
	
	public WallsScoreboard(String name, String displayName, String criteria, DisplaySlot slot) {
		this.name = name;
		this.displayName = displayName;
		this.criteria = criteria;
		this.slot = slot;
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		health = board.registerNewObjective("health", "health");
		redTeam = board.registerNewTeam("red");
		greenTeam = board.registerNewTeam("green");
		blueTeam = board.registerNewTeam("blue");
		yellowTeam = board.registerNewTeam("yellow");
		redTeam.setAllowFriendlyFire(false);
		greenTeam.setAllowFriendlyFire(false);
		blueTeam.setAllowFriendlyFire(false);
		yellowTeam.setAllowFriendlyFire(false);
		redTeam.setPrefix(ChatColor.RED+"");
		greenTeam.setPrefix(ChatColor.GREEN+"");
		blueTeam.setPrefix(ChatColor.BLUE+"");
		yellowTeam.setPrefix(ChatColor.YELLOW+"");
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
			Score space1 = objective.getScore("");
			space1.setScore(5);
		}
	}

	//put the sudden death time on the scoreboard
	public void putSuddenDeathTime() {
		Score time;
		if (seconds != -1) {
			String seconds = this.seconds+"";
			if (seconds.length() == 1) seconds = "0" + seconds;
			time = objective.getScore("Sudden Death in " + ChatColor.GREEN + minutes + ":" + seconds);
			time.setScore(6);
			Score space1 = objective.getScore("");
			space1.setScore(5);
		}
	}
	
	//Put the waiting scorboard
	public void putWaiting() {
		Score time;
		if (seconds != -1 && players >= minPlayers) {
			String seconds = this.seconds+"";
			if (seconds.length() == 1) seconds = "0" + seconds;
			time = objective.getScore("Waiting For Players: " + ChatColor.GREEN + minutes + ":" + seconds);
			time.setScore(2);
		}
		else {
			Score required = objective.getScore(ChatColor.GREEN+""+ (minPlayers - players) +""+ChatColor.WHITE +" required to start.");
			required.setScore(0);
		}
		Score players = objective.getScore("Players: " + ChatColor.GREEN + this.players + "/" + maxPlayers);
		players.setScore(1);
	}
	
	//Game End
	public void putEndingTimer() {
		String seconds = this.seconds+"";
		if (seconds.length() == 1) seconds = "0" + seconds;
		Score time = objective.getScore("Game Over " + ChatColor.GREEN + minutes + ":" + seconds);
		time.setScore(6);
		Score space1 = objective.getScore("");
		space1.setScore(5);
	}
	
	//Displays the scoreboard to the player.
	public void updatePlayersSB(Player player) {
		player.setScoreboard(board);
	}
	public void showHealth() {
		health.setDisplayName(ChatColor.RED+"❤");
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}
	//clears the scoreboard, but keep the name and objective
	public void clearSB() {
		if (objective != null) objective.unregister();
		objective = board.registerNewObjective(name, criteria);
		objective.setDisplayName(displayName);
		objective.setDisplaySlot(slot);
	}
	
	public void joinRedTeam(Player player) {
		redTeam.addEntry(player.getName());
	}
	public void joinGreenTeam(Player player) {
		greenTeam.addEntry(player.getName());
	}
	public void joinBlueTeam(Player player) {
		blueTeam.addEntry(player.getName());
	}
	public void joinYellowTeam(Player player) {
		yellowTeam.addEntry(player.getName());
	}
	
	
	// Getters and Setters \\
	
	public int getReds() {return reds;}
	
	public void setReds(int reds) {this.reds = reds;}
	
	public int getMinutes() {return minutes;}
	
	public void setMinutes(int minutes) {this.minutes = minutes;}
	
	public int getSeconds() {return seconds;}
	
	public void setSeconds(int seconds) {this.seconds = seconds;}
	
	public int getGreens() {return greens;}
	
	public void setGreens(int greens) {this.greens = greens;}
	
	public int getBlues() {return blues;}
	
	public void setBlues(int blues) {this.blues = blues;}
	
	public int getYellows() {return yellows;}
	
	public void setYellows(int yellows) {this.yellows = yellows;}
	
	public Objective getObjective() {return this.objective;}
	
	public int getPlayers() {return players;}
	
	public void setPlayers(int players) {this.players = players;}
	
	public int getMaxPlayers() {return maxPlayers;}
	
	public void setMaxPlayers(int maxPlayers) {this.maxPlayers = maxPlayers;}
	
	public ScoreboardManager getManager() {return manager;}
	public void setManager(ScoreboardManager manager) {this.manager = manager;}
	public int getMinPlayers() {return minPlayers;}
	public void setMinPlayers(int minPlayers) {this.minPlayers = minPlayers;}
}

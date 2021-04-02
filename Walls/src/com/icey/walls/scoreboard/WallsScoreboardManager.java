package com.icey.walls.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class WallsScoreboardManager {

	HashMap<Player, WallsScoreboard> scoreboards;
	private Team redTeam;
	private Team greenTeam;
	private Team blueTeam;
	private Team yellowTeam;
	ScoreboardManager manager;
	Scoreboard mainScoreboard;
	
	public WallsScoreboardManager() {
		scoreboards = new HashMap<Player, WallsScoreboard>();
		manager = Bukkit.getScoreboardManager();
		mainScoreboard = manager.getNewScoreboard();
		redTeam = mainScoreboard.registerNewTeam("red");
		greenTeam = mainScoreboard.registerNewTeam("green");
		blueTeam = mainScoreboard.registerNewTeam("blue");
		yellowTeam = mainScoreboard.registerNewTeam("yellow");
		redTeam.setAllowFriendlyFire(false);
		greenTeam.setAllowFriendlyFire(false);
		blueTeam.setAllowFriendlyFire(false);
		yellowTeam.setAllowFriendlyFire(false);
		redTeam.setPrefix(ChatColor.RED+"");
		greenTeam.setPrefix(ChatColor.GREEN+"");
		blueTeam.setPrefix(ChatColor.BLUE+"");
		yellowTeam.setPrefix(ChatColor.YELLOW+"");

	}
	public void addPlayerScoreboard(Player player) {
		scoreboards.put(player, new WallsScoreboard("walls", ChatColor.GOLD+""+ChatColor.BOLD+"The Walls", player));
	}
	public HashMap<Player, WallsScoreboard> getScoreboards() {
		return scoreboards;
	}
	public void setPlayersSB(Player player) {
		player.setScoreboard(mainScoreboard);
	}
	public Team getRedTeam() {
		return redTeam;
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
	public void leaveTeams(Player player) {
		redTeam.removeEntry(player.getName());
		greenTeam.removeEntry(player.getName());
		blueTeam.removeEntry(player.getName());
		yellowTeam.removeEntry(player.getName());
	}
	
}

package com.icey.walls.versions.v1_8_R3;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.icey.walls.framework.ScoreboardSharedTeams;

import net.minecraft.server.v1_8_R3.ScoreboardTeam;

public class WallsScoreboardSharedTeams implements ScoreboardSharedTeams {
	
	Scoreboard scoreboard;
	HashMap<String, Team> teams;
	
	public WallsScoreboardSharedTeams() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		teams = new HashMap<String, Team>();
		teams.put("red", scoreboard.registerNewTeam("red"));
		teams.get("red").setPrefix(ChatColor.RED+"");
		teams.put("green", scoreboard.registerNewTeam("green")); 
		teams.get("green").setPrefix(ChatColor.GREEN+"");
		teams.put("blue", scoreboard.registerNewTeam("blue"));
		teams.get("blue").setPrefix(ChatColor.BLUE+"");
		teams.put("yellow", scoreboard.registerNewTeam("yellow")); 
		teams.get("yellow").setPrefix(ChatColor.YELLOW+"");
		for (Team team : teams.values()) {
			team.setAllowFriendlyFire(false);
		}
	}
	
	public void joinTeam(Player player, String name) {
		scoreboard.getTeam("red").removeEntry(player.getName());
		scoreboard.getTeam("green").removeEntry(player.getName());
		scoreboard.getTeam("blue").removeEntry(player.getName());
		scoreboard.getTeam("yellow").removeEntry(player.getName());
		scoreboard.getTeam(name).addEntry(player.getName());
	}
	
	public void leaveTeam(Player player, String name) {
		scoreboard.getTeam(name).removeEntry(player.getName());
	}
	
	/**
	 * Uses Reflection to add a shared bundle of teams to each player's scoreboard.
	 */
	public void addTeamsToScoreboard(Scoreboard scoreboard) {
		for (Team team : teams.values()) {
			Class teamClass = team.getClass();
			Class scoreboardClass = scoreboard.getClass();
			try {
				//Get the nms ScoreboardTeam object
				Field teamField = teamClass.getDeclaredField("team");
				teamField.setAccessible(true);
				ScoreboardTeam nmsScoreboardTeam = (ScoreboardTeam) teamField.get(team);
				
				//Get the nms Scoreboard object
				Field boardField = scoreboardClass.getDeclaredField("board");
				boardField.setAccessible(true);
				net.minecraft.server.v1_8_R3.Scoreboard nmsScoreboard = (net.minecraft.server.v1_8_R3.Scoreboard) boardField.get(scoreboard);
				Class nmsScoreBoardClass = nmsScoreboard.getClass().getSuperclass();
				Field nmsScoreboardTeamsbyName = nmsScoreBoardClass.getDeclaredField("teamsByName");
				nmsScoreboardTeamsbyName.setAccessible(true);
				HashMap<String, ScoreboardTeam> teams = (HashMap<String, ScoreboardTeam>) nmsScoreboardTeamsbyName.get(nmsScoreboard);
				teams.put(team.getName(), nmsScoreboardTeam); // Put the nmsScoreboardTeam into the nms Scoreboard
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public HashMap<String, Team> getTeams() {
		return teams;
	}
}

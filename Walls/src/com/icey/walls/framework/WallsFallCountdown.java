package com.icey.walls.framework;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import com.icey.walls.listeners.Arena;

public class WallsFallCountdown extends TimerTask {
	
	private int minutes;
	private int seconds;
	private WallsScoreboard wallsSB;
	private ArrayList<UUID> player;
	private Arena arena;
	
	public WallsFallCountdown(int minutes, int seconds, WallsScoreboard wallsSB, ArrayList<UUID> player, Arena arena) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.wallsSB = wallsSB;
		this.player = player;
		this.arena = arena;
	}

	@Override
	public void run() {
		seconds--;
		if (minutes <= 0 && seconds < 0) {
			arena.startPvp();
			this.cancel();
		}
		else {
			if (seconds == -1) {
				seconds = 59; 
				if(minutes != 0) minutes--; 
			}
		}
		wallsSB.setMinutes(minutes);
		wallsSB.setSeconds(seconds);
		wallsSB.clearSB();
		wallsSB.putPrepTime();
		wallsSB.putPlayersAlive();
		for (UUID id : player) {
			wallsSB.updatePlayersSB(Bukkit.getPlayer(id));
		}
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
	
}

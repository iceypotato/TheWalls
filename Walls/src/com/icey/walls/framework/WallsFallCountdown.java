package com.icey.walls.framework;

import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WallsFallCountdown extends TimerTask {
	
	private int minutes;
	private int seconds;
	
	public WallsFallCountdown(int minutes, int seconds) {
		this.minutes = minutes;
		this.seconds = seconds;
	}

	@Override
	public void run() {
		seconds--;
		if (minutes <= 0 && seconds < 0) {
			this.cancel();
		}
		else {
			if (seconds == -1) {
				seconds = 59; 
				if(minutes != 0) minutes--; 
			}
		}
		//move this code to Arena.class
		sc.setMinutes(minutes);
		sc.setSeconds(seconds);
		sc.clearSB();
		//todo merge this with the scoreboard and account for sudden death and endgame.
		sc.putPrepTime();
		sc.putPlayersAlive();
		sc.updatePlayersSB(player);
	}
	public int getMinutes() {
		return minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	
}

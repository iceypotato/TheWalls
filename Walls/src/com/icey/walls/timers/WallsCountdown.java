package com.icey.walls.timers;

import org.bukkit.scheduler.BukkitRunnable;

import com.icey.walls.framework.Arena;
import com.icey.walls.framework.WallsScoreboard;

public abstract class WallsCountdown extends BukkitRunnable {
	
	private int minutes;
	private int seconds;
	private WallsScoreboard wallsSB;
	private Arena arena;
	
	public WallsCountdown(int minutes, int seconds, WallsScoreboard wallsSB, Arena arena) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.wallsSB = wallsSB;
		this.arena = arena;
	}

	@Override
	public void run() {
		seconds--;
		if (minutes <= 0 && seconds < 0) {
			runNextStage();
			this.cancel();
		}
		else {
			if (seconds == -1) {
				seconds = 59; 
				if(minutes != 0) minutes--; 
			}
		}
		runEverySecond();
		wallsSB.setMinutes(minutes);
		wallsSB.setSeconds(seconds);
		arena.updateScoreboard();
	}
	
	/**
	 * Override this when timer ends
	 */
	public abstract void runNextStage();
	public abstract void runEverySecond();
	
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

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}
}

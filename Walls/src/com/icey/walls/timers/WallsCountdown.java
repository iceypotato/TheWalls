package com.icey.walls.timers;

import org.bukkit.scheduler.BukkitRunnable;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsScoreboard;

public abstract class WallsCountdown extends BukkitRunnable {
	
	private int minutes;
	private int seconds;
	private boolean running;
	private WallsScoreboard wallsSB;
	private WallsArena arena;
	
	public WallsCountdown(int minutes, int seconds, WallsScoreboard wallsSB, WallsArena arena) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.wallsSB = wallsSB;
		this.arena = arena;
		this.running = false;
	}

	@Override
	public void run() {
		running = true;
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
	
	@Override
	public void cancel() {
		super.cancel();
		running = false;
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

	public WallsArena getArena() {
		return arena;
	}

	public void setArena(WallsArena arena) {
		this.arena = arena;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}

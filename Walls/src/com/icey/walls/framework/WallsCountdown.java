package com.icey.walls.framework;

import java.util.TimerTask;

public class WallsCountdown extends TimerTask {
	
	private int starting;
	
	public WallsCountdown(int starting) {
		this.starting = starting;
	}

	@Override
	public void run() {
		starting--;
	}

	public int getStarting() {
		return starting;
	}

	public void setStarting(int starting) {
		this.starting = starting;
	}

}

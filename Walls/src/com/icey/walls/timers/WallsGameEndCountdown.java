package com.icey.walls.timers;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsScoreboard;

public class WallsGameEndCountdown extends WallsCountdown {

	public WallsGameEndCountdown(int minutes, int seconds, WallsScoreboard wallsSB, WallsArena arena) {
		super(minutes, seconds, wallsSB, arena);
	}
	
	@Override
	public void runNextStage() {
		getArena().stopGame();
	}

	@Override
	public void runEverySecond() {
	}
}

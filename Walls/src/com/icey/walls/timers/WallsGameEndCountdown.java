package com.icey.walls.timers;

import java.util.Collection;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.runnables.WallsCountdown;
import com.icey.walls.scoreboard.WallsScoreboard;

public class WallsGameEndCountdown extends WallsCountdown {

	public WallsGameEndCountdown(int minutes, int seconds, Collection<WallsScoreboard> wallsSB, WallsArena arena) {
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

package com.icey.walls.timers;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsScoreboard;

public class WallsBattleCountdown extends WallsCountdown {

	public WallsBattleCountdown(int minutes, int seconds, WallsScoreboard wallsSB, WallsArena arena) {
		super(minutes, seconds, wallsSB, arena);
	}

	@Override
	public void runNextStage() {
		getArena().suddenDeath();
	}

	@Override
	public void runEverySecond() {	
	}

}

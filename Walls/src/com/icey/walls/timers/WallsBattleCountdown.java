package com.icey.walls.timers;

import java.util.Collection;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.scoreboard.WallsScoreboard;

public class WallsBattleCountdown extends WallsCountdown {

	public WallsBattleCountdown(int minutes, int seconds, Collection<WallsScoreboard> wallsSB, WallsArena arena) {
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

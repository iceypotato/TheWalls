package com.icey.walls.framework;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class WallsGameEndCountdown extends WallsCountdown {

	public WallsGameEndCountdown(int minutes, int seconds, WallsScoreboard wallsSB, Arena arena) {
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

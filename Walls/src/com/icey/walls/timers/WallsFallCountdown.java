package com.icey.walls.timers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import com.icey.walls.framework.WallsArena;
import com.icey.walls.framework.WallsScoreboard;

public class WallsFallCountdown extends WallsCountdown {
	
	public WallsFallCountdown(int minutes, int seconds, WallsScoreboard wallsSB, WallsArena arena) {
		super(minutes, seconds, wallsSB, arena);
	}
	
	@Override
	public void runNextStage() {
		getArena().startPvp();
	}

	@Override
	public void runEverySecond() {
		if (getSeconds() >= 0 && getSeconds() <= 10 && getMinutes()==0) {
			for (UUID id : getArena().getPlayersInGame()) {
				Bukkit.getPlayer(id).playSound(Bukkit.getPlayer(id).getLocation(), Sound.WOOD_CLICK, 10, 1);
				Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD +"Walls Fall in " + ChatColor.GREEN +""+getSeconds()+""+ChatColor.GOLD+" seconds!");
			}
		}
	}
}

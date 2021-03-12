package com.icey.walls.listeners;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.icey.walls.MainClass;

public class TestListener implements Listener  {
	
	private WallsTool wt;
	private MainClass plugin;
	private Location oldloc;
	
	public TestListener(WallsTool wt, MainClass plugin) {
		this.wt = wt;
		this.plugin = plugin;
	}
	
	@EventHandler
	public void walkingEvent(PlayerMoveEvent pMoveEvent) {
		if(wt.getPos1() != null) {
			if (pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX() == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()-1 == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ() == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ() == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX()-1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX() == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wt.getPos1().getBlockZ() ||
				pMoveEvent.getPlayer().getLocation().getBlockX()+1 == wt.getPos1().getBlockX() && pMoveEvent.getPlayer().getLocation().getBlockZ()+1 == wt.getPos1().getBlockZ()) {
				oldloc = pMoveEvent.getPlayer().getLocation();
				plugin.getLogger().info("Recording");
			}
			else if (wt.getPos1().getBlockZ() == pMoveEvent.getPlayer().getLocation().getBlockZ() && wt.getPos1().getBlockX() == pMoveEvent.getPlayer().getLocation().getBlockX()) {
				pMoveEvent.getPlayer().teleport(oldloc);
			}
		}
	}
}

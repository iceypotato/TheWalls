package com.icey.walls.listeners;

import org.bukkit.event.Listener;

public class TestListener implements Listener {
	
	private WallsTool wt;
	
	public TestListener(WallsTool wt) {
		this.wt = wt;
	}
	
//	@EventHandler
//	public void walkingEvent(PlayerMoveEvent pMoveEvent) {
//		if (wt.getPos1().getBlockZ() == pMoveEvent.getPlayer().getLocation().getBlockZ()) {
//			pMoveEvent.setCancelled(true);
//		}
//	}
	
}

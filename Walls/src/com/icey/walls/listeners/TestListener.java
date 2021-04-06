package com.icey.walls.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.icey.walls.MainClass;

public class TestListener implements Listener  {
	
	private WallsTool wt;
	private MainClass plugin;
	private Location oldLoc;
	
	public TestListener(WallsTool wt, MainClass plugin) {
		this.wt = wt;
		this.plugin = plugin;
	}
	
	@EventHandler
	public void preventBlockExplosions(EntityExplodeEvent event) {
		List<Block> blocksNotToExplode = new ArrayList<>();
		for (Block block : event.blockList()) {
			if (!(Math.min(wt.getPos1().getBlockX(), wt.getPos2().getBlockX()) <= block.getX() && block.getX() <= Math.max(wt.getPos1().getBlockX(), wt.getPos2().getBlockX()) && 
				Math.min(wt.getPos1().getBlockY(), wt.getPos2().getBlockY()) <= block.getY() && block.getY() <= Math.max(wt.getPos1().getBlockY(), wt.getPos2().getBlockY()) &&
				Math.min(wt.getPos1().getBlockZ(), wt.getPos2().getBlockZ()) <= block.getZ() && block.getZ() <= Math.max(wt.getPos1().getBlockZ(), wt.getPos2().getBlockZ()))) {
				blocksNotToExplode.add(block);
			}
		}
		for (Block block : blocksNotToExplode) {
			event.blockList().remove(block);
		}
	}
}

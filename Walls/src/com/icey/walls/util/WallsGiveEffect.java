package com.icey.walls.util;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.icey.walls.framework.WallsArena;

/**
 * Used for giving players not in the region potion effects.
 * @author nickyjedi
 *
 */

public class WallsGiveEffect extends BukkitRunnable {
	
	WallsArena arena;
	
	public WallsGiveEffect(WallsArena arena) {
		this.arena = arena;
	}

	@Override
	public void run() {
		for (UUID id : arena.getPlayersInGame()) {
			Location playerLoc = Bukkit.getPlayer(id).getLocation();
			boolean playerInSafeZone = false;
			for (int i = 0; i < arena.getConfig().getSafeZoneRegions().size(); i++) {
				Location[] region = arena.getConfig().getSafeZoneRegions().get(i);
				if (Math.min(region[0].getBlockX(), region[1].getBlockX()) <= playerLoc.getBlockX() && playerLoc.getBlockX() <= Math.max(region[0].getBlockX(), region[1].getBlockX()) &&
					Math.min(region[0].getBlockY(), region[1].getBlockY()) <= playerLoc.getBlockY() && playerLoc.getBlockY() <= Math.max(region[0].getBlockY(), region[1].getBlockY()) &&
					Math.min(region[0].getBlockZ(), region[1].getBlockZ()) <= playerLoc.getBlockZ() && playerLoc.getBlockZ() <= Math.max(region[0].getBlockZ(), region[1].getBlockZ())) {
					playerInSafeZone = true;
				}
			}
			if (playerInSafeZone) Bukkit.getPlayer(id).removePotionEffect(PotionEffectType.WITHER);
			else Bukkit.getPlayer(id).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
		}
	}
}

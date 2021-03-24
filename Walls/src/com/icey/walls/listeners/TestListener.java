package com.icey.walls.listeners;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import com.icey.walls.MainClass;

public class TestListener implements Listener  {
	
	private WallsTool wt;
	private MainClass plugin;
	private Location oldloc;
	
	public TestListener(WallsTool wt, MainClass plugin) {
		this.wt = wt;
		this.plugin = plugin;
	}
//	@EventHandler
//	public void playerDies(PlayerDeathEvent deathEvent) {
//			deathEvent.setDeathMessage("");
//			//Run this if a player was killed by a player.
//			Random rand = new Random();
//			int msgID = rand.nextInt(11);
//			String deathMsg = "";
//			if (deathEvent.getEntity().getPlayer().getKiller() != null) {
//				if (msgID == 0) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " was slain by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 1) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " was 69ed by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 2) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " was memed by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 3) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got epic gamer moved by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 4) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " was isekaied to another world by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 5) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got rekt by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 6) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got w-tapped by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 7) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got destroyed by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 8) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got creeper aw maned by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 9) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " got squashed by anime thighs by " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//				if (msgID == 10) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " lost all hp and fainted to " + ChatColor.RESET + deathEvent.getEntity().getKiller().getDisplayName();
//
//
//			}
//			//run this if player died of natural causes.
//			else {
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " fell in the void.";
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.LAVA) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " thought he had the high ground and melted in lava.";
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.DROWNING) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " forgot how to breathe.";
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE_TICK) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " burnt to a crisp.";
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FIRE) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " played with fire.";
//				if (deathEvent.getEntity().getLastDamageCause().getCause() == DamageCause.FALL) deathMsg = deathEvent.getEntity().getPlayer().getDisplayName() + ChatColor.GRAY + " fell to a clumsy death.";
//			}
//			for (Player id : Bukkit.getOnlinePlayers()) {
//				id.sendMessage(deathMsg);
//			}
//		}
	
}

package com.icey.walls.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.icey.walls.MainPluginClass;

public class Arena implements EventListener {

	private MainPluginClass plugin;
	private String name;
	private boolean inProgress;
	private boolean enabled;
	private int waitingTime;
	private int maxPlayers;
	private int minPlayers;
	private int prepTime;
	private ArrayList<UUID> playersInGame;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private ArrayList<Location> protectedBlocks;
	private ArrayList<Location[]> arenaRegions;
	private ArrayList<Location[]> buildRegions;
	private ArrayList<Location[]> wallRegions;
	private File arenaFile;
	private	FileConfiguration arenaConfig;
	
	public Arena(String name, boolean enabled, boolean inProgress, File arenaFile, MainPluginClass plugin) {
		this.name = name;
		this.setEnabled(enabled);
		this.inProgress = inProgress;
		this.playersInGame = new ArrayList<UUID>();
		this.arenaFile = arenaFile;
		this.arenaConfig = YamlConfiguration.loadConfiguration(this.arenaFile);
		this.plugin = plugin;
	}
	public void loadConfig() {
		readLobbySpawn();
		readBlueSpawn();
		readRedSpawn();
		readYellowSpawn();
		addArenaRegion();
		addWallRegion();
		addBuildRegion();
	}
	
	public void runGame() {
		
	}
	public void stopGame() {
		inProgress = false;
		playersInGame.clear();
		if (protectedBlocks != null) {
			for (int i = 0; i < protectedBlocks.size(); i++) {
				Location loc = protectedBlocks.get(i);
				loc.getBlock().setType(protectedBlocks.get(i).getBlock().getType());
			}
		}
	}
	@EventHandler
	public void waiting(BlockBreakEvent block) {
		block.setCancelled(true);
	}
	@EventHandler
	public void setup(PlayerInteractEvent event, BlockExplodeEvent block) {
		for (int i = 0; i < buildRegions.size(); i++) {
			if (!(event.getClickedBlock().getLocation().getBlockX() >= Math.min(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) && 
				event.getClickedBlock().getLocation().getBlockX() <= Math.max(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) &&
				event.getClickedBlock().getLocation().getBlockY() >= Math.min(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) && 
				event.getClickedBlock().getLocation().getBlockY() <= Math.max(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) &&
				event.getClickedBlock().getLocation().getBlockZ() >= Math.min(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ()) && 
				event.getClickedBlock().getLocation().getBlockZ() <= Math.max(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ())))
			{
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "You cannot break those blocks!");
				event.setCancelled(true);
			}
		}
		for (int i = 0; i < buildRegions.size(); i++) {
			if (!(block.getBlock().getLocation().getBlockX() >= Math.min(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) && 
				block.getBlock().getLocation().getBlockX() <= Math.max(buildRegions.get(i)[0].getBlockX(), buildRegions.get(i)[1].getBlockX()) &&
				block.getBlock().getLocation().getBlockY() >= Math.min(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) && 
				block.getBlock().getLocation().getBlockY() <= Math.max(buildRegions.get(i)[0].getBlockY(), buildRegions.get(i)[1].getBlockY()) &&
				block.getBlock().getLocation().getBlockZ() >= Math.min(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ()) && 
				block.getBlock().getLocation().getBlockZ() <= Math.max(buildRegions.get(i)[0].getBlockZ(), buildRegions.get(i)[1].getBlockZ())))
			{
				block.blockList().clear();
				block.setCancelled(true);
			}
		}
	}
	public void pvp() {
		
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayersInGame() {
		return playersInGame;
	}

	public void setPlayersInGame(ArrayList<UUID> playersInGame) {
		this.playersInGame = playersInGame;
	}
	public void addArenaRegion() {
		int i = 1;
		if (arenaConfig.get("Regions.Arena." + i) != null) {
			while (i <= arenaConfig.getConfigurationSection("Regions.Arena").getKeys(false).toArray().length) {
				if (arenaConfig.get("Regions.Arena." + i).equals("") || arenaConfig.get("Regions.Arena." + i) == null) {
					plugin.getLogger().info(name + " arena has an invalid config! Check the arena regions");
					return;
				}
				buildRegions = new ArrayList<Location[]>();
				Location[] region = new Location[2];
				region[0] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Arena." + i + ".world")),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos1.x"),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos1.y"),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos1.z"));
				region[1] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Arena." + i + ".world")),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos2.x"),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos2.y"),
						arenaConfig.getDouble("Regions.Arena." + i + ".pos2.z"));
				arenaRegions.add(region);
				i++;
			}
			protectedBlocks = new ArrayList<Location>();
			for (int j = 0; j < arenaRegions.size(); j++) {
				for (int x = Math.min(arenaRegions.get(j)[0].getBlockX(), arenaRegions.get(j)[1].getBlockX()); x < Math.max(arenaRegions.get(j)[0].getBlockX(), arenaRegions.get(j)[1].getBlockX()); x++) {
					for (int y = Math.min(arenaRegions.get(j)[0].getBlockY(), arenaRegions.get(j)[1].getBlockY()); y < Math.max(arenaRegions.get(j)[0].getBlockY(), arenaRegions.get(j)[1].getBlockY()); y++) {
						for (int z = Math.min(arenaRegions.get(j)[0].getBlockZ(), arenaRegions.get(j)[1].getBlockZ()); z < Math.max(arenaRegions.get(j)[0].getBlockZ(), arenaRegions.get(j)[1].getBlockZ()); z++) {
							protectedBlocks.add(new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Arena.world")), x, y, z));
						}
					}
				}
			}
		}
		else {
			plugin.getLogger().info(name + " arena has an invalid config! Check the arena regions");
		}
	}
	
	public void addWallRegion() {
		int i = 1;
		if (arenaConfig.get("Regions.Walls." + i) != null) {
			while (i <= arenaConfig.getConfigurationSection("Regions.Walls").getKeys(false).toArray().length) {
				if (arenaConfig.get("Regions.Walls." + i).equals("") || arenaConfig.get("Regions.Walls." + i) == null) {
					plugin.getLogger().info(name + " arena has an invalid config! Check the wall regions");
					break;
				}
				wallRegions = new ArrayList<Location[]>();
				Location[] region = new Location[2];
				region[0] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Walls." + i + ".world")),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos1.x"),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos1.y"),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos1.z"));
				region[1] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Walls." + i + ".world")),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos2.x"),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos2.y"),
						arenaConfig.getDouble("Regions.Walls." + i + ".pos2.z"));
				wallRegions.add(region);
				i++;
			}
		}
		else plugin.getLogger().info(name + " arena has an invalid config! Check the wall regions");
	}
	public void addBuildRegion() {
		int i = 1;
		if (arenaConfig.get("Regions.Build." + i) != null) {
			while (i <= arenaConfig.getConfigurationSection("Regions.Build").getKeys(false).toArray().length) {
				if (arenaConfig.get("Regions.Build." + i).equals("") || arenaConfig.get("Regions.Build." + i) == null) {
					plugin.getLogger().info(name + " arena has an invalid config! Check the build regions");
					break;
				}
				buildRegions = new ArrayList<Location[]>();
				Location[] region = new Location[2];
				region[0] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Build." + i + ".world")),
						arenaConfig.getDouble("Regions.Build." + i + ".pos1.x"),
						arenaConfig.getDouble("Regions.Build." + i + ".pos1.y"),
						arenaConfig.getDouble("Regions.Build." + i + ".pos1.z"));
				region[1] = new Location(Bukkit.getWorld(arenaConfig.getString("Regions.Build." + i + ".world")),
						arenaConfig.getDouble("Regions.Build." + i + ".pos2.x"),
						arenaConfig.getDouble("Regions.Build." + i + ".pos2.y"),
						arenaConfig.getDouble("Regions.Build." + i + ".pos2.z"));
				buildRegions.add(region);
				i++;
			}
		}
		else plugin.getLogger().info(name + " arena has an invalid config! Check the build regions");
	}
	
	public void readLobbySpawn() {
		if (arenaConfig.contains("Spawns.Lobby.world") && arenaConfig.contains("Spawns.Lobby.x") && arenaConfig.contains("Spawns.Lobby.y") && arenaConfig.contains("Spawns.Lobby.z")) {
			lobbySpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Lobby.world")), arenaConfig.getDouble("Spawns.Lobby.x"), arenaConfig.getDouble("Spawns.Lobby.y"), arenaConfig.getDouble("Spawns.Lobby.x"));
			lobbySpawn.setPitch((float) arenaConfig.getDouble("Spawns.Lobby.pitch"));
			lobbySpawn.setYaw((float) arenaConfig.getDouble("Spawns.Lobby.yaw"));
		}
	}
	public void readBlueSpawn() {
		if (arenaConfig.contains("Spawns.Blue.world") && arenaConfig.contains("Spawns.Blue.x") && arenaConfig.contains("Spawns.Blue.y") && arenaConfig.contains("Spawns.Blue.z")) {
			blueSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Blue.world")), arenaConfig.getDouble("Spawns.Blue.x"), arenaConfig.getDouble("Spawns.Blue.y"), arenaConfig.getDouble("Spawns.Blue.z"));
			blueSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Blue.pitch"));
			blueSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Blue.yaw"));
		}
	}
	public void readRedSpawn() {
		if (arenaConfig.contains("Spawns.Red.world") && arenaConfig.contains("Spawns.Red.x") && arenaConfig.contains("Spawns.Red.y") && arenaConfig.contains("Spawns.Red.z")) {
			redSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Red.world")), arenaConfig.getDouble("Spawns.Red.x"), arenaConfig.getDouble("Spawns.Red.y"), arenaConfig.getDouble("Spawns.Red.z"));
			redSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Red.pitch"));
			redSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Red.yaw"));
		}
	}
	public void readGreenSpawn() {
		if (arenaConfig.contains("Spawns.Green.world") && arenaConfig.contains("Spawns.Green.x") && arenaConfig.contains("Spawns.Green.y") && arenaConfig.contains("Spawns.Green.z")) {
			greenSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Green.world")), arenaConfig.getDouble("Spawns.Green.x"), arenaConfig.getDouble("Spawns.Green.y"), arenaConfig.getDouble("Spawns.Green.z"));
			greenSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Green.pitch"));
			greenSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Green.yaw"));
		}
	}
	public void readYellowSpawn() {
		if (arenaConfig.contains("Spawns.Yellow.world") && arenaConfig.contains("Spawns.Yellow.x") && arenaConfig.contains("Spawns.Yellow.y") && arenaConfig.contains("Spawns.Yellow.z")) {
			yellowSpawn = new Location(Bukkit.getWorld(arenaConfig.getString("Spawns.Yellow.world")), arenaConfig.getDouble("Spawns.Yellow.x"), arenaConfig.getDouble("Spawns.Yellow.y"), arenaConfig.getDouble("Spawns.Yellow.z"));
			yellowSpawn.setPitch((float) arenaConfig.getDouble("Spawns.Yellow.pitch"));
			yellowSpawn.setYaw((float) arenaConfig.getDouble("Spawns.Yellow.yaw"));
		}
	}

	public String listRegions() {
		String regions = "";
		if (wallRegions == null) {
			regions += "No wall regions.\n";
		}
		else {
			regions += "Wall regions:\n";
			for (int i = 0; i < wallRegions.size(); i++) {
				for (int j = 0; j < wallRegions.get(i).length; j++) {
					Location[] loc = wallRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (buildRegions == null) {
			regions += "No build regions.\n";
		}
		else {
			regions += "Build Regions:\n";
			for (int i = 0; i < buildRegions.size(); i++) {
				for (int j = 0; j < buildRegions.get(i).length; j++) {
					Location[] loc = buildRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		if (arenaRegions == null) {
			regions += "No arena regions.\n";
		}
		else {
			regions += "Arena regions:\n";
			for (int i = 0; i < arenaRegions.size(); i++) {
				for (int j = 0; j < arenaRegions.get(i).length; j++) {
					Location[] loc = arenaRegions.get(i);
					regions += " Region" + (i+1) + ":\n  pos" + (j+1) +":\n   X: " + loc[j].getBlockX() + " Y: " + loc[j].getBlockY() + " Z: " + loc[j].getBlockZ() + "\n";
				}
			}
		}
		return regions;
	}
	
	public String listSpawns() {
		String info = "";
		if (lobbySpawn != null) {
			info += "Lobby:\n World: " + lobbySpawn.getWorld().getName() + "\n X: " + lobbySpawn.getBlockX() + "\n Y: " + lobbySpawn.getBlockY() + "\n Z: " + lobbySpawn.getBlockZ() + "\n";
		}
		if (blueSpawn != null) {
			info += "Blue Spawn:\n World: " + blueSpawn.getWorld().getName() + "\n X: " + blueSpawn.getBlockX() + "\n Y: " + blueSpawn.getBlockY() + "\n Z: " + blueSpawn.getBlockZ() + "\n";
		}
		if (redSpawn != null) {
			info += "Red Spawn:\n World: " + redSpawn.getWorld().getName() + "\n X: " + redSpawn.getBlockX() + "\n Y: " + redSpawn.getBlockY() + "\n Z: " + redSpawn.getBlockZ() + "\n";
		}
		if (yellowSpawn != null) {
			info += "Blue Spawn:\n World: " + yellowSpawn.getWorld().getName() + "\n X: " + yellowSpawn.getBlockX() + "\n Y: " + yellowSpawn.getBlockY() + "\n Z: " + yellowSpawn.getBlockZ() + "\n";
		}
		if (greenSpawn != null) {
			info += "Green Spawn:\n World: " + greenSpawn.getWorld().getName() + "\n X: " + greenSpawn.getBlockX() + "\n Y: " + greenSpawn.getBlockY() + "\n Z: " + greenSpawn.getBlockZ() + "\n";
		}
		if (info.equals("")) {
			return "No Spawns";
		}
		return info;
	}
	
	/* Normal Getters and setters */

	public Location getLobbySpawn() {
		return lobbySpawn;
	}

	public Location getBlueSpawn() {
		return blueSpawn;
	}

	public Location getRedSpawn() {
		return redSpawn;
	}

	public Location getGreenSpawn() {
		return greenSpawn;
	}

	public Location getYellowSpawn() {
		return yellowSpawn;
	}
	public ArrayList<Location[]> getWallRegions() {
		return wallRegions;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public int getMinPlayers() {
		return minPlayers;
	}
	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}
	public int getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(int prepTime) {
		this.prepTime = prepTime;
	}
	public ArrayList<Location[]> getArenaRegion() {
		return arenaRegions;
	}
	public void setArenaRegion(ArrayList<Location[]> arenaRegion) {
		this.arenaRegions = arenaRegion;
	}

}

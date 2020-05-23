package com.icey.walls;

import java.awt.geom.Area;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Arena {
	
	private String name;
	private boolean inProgress;
	private boolean enabled;
	private ArrayList<UUID> playersInGame;
	private Location lobbySpawn;
	private Location blueSpawn;
	private Location redSpawn;
	private Location greenSpawn;
	private Location yellowSpawn;
	private ArrayList<Location[]> buildRegions;
	private ArrayList<Location[]> wallRegions;
	private File arenaConfiguration;
	
	public Arena(String name, boolean enabled, boolean inProgress, File config) {
		this.name = name;
		this.setEnabled(enabled);
		this.inProgress = inProgress;
		this.playersInGame = new ArrayList<UUID>();
	}
	public void loadConfig(File arenaFile) {
		FileConfiguration arenaConfiguration = YamlConfiguration.loadConfiguration(this.arenaConfiguration);
		if (arenaConfiguration.contains("Spawns.Lobby.World") && arenaConfiguration.contains("Spawns.Lobby.X") && arenaConfiguration.contains("Spawns.Lobby.Y") && arenaConfiguration.contains("Spawns.Lobby.Z")) {
			lobbySpawn = new Location(Bukkit.getWorld(arenaConfiguration.getString("Spawns.Lobby.World")), arenaConfiguration.getDouble("Spawns.Lobby.X"), arenaConfiguration.getDouble("Spawns.Lobby.Y"), arenaConfiguration.getDouble("Spawns.Lobby.Z"));
		}
		if (arenaConfiguration.contains("Spawns.Blue.World") && arenaConfiguration.contains("Spawns.Blue.X") && arenaConfiguration.contains("Spawns.Blue.Y") && arenaConfiguration.contains("Spawns.Blue.Z")) {
			blueSpawn = new Location(Bukkit.getWorld(arenaConfiguration.getString("Spawns.Blue.World")), arenaConfiguration.getDouble("Spawns.Blue.X"), arenaConfiguration.getDouble("Spawns.Blue.Y"), arenaConfiguration.getDouble("Spawns.Blue.Z"));
		}
		if (arenaConfiguration.contains("Spawns.Red.World") && arenaConfiguration.contains("Spawns.Red.X") && arenaConfiguration.contains("Spawns.Red.Y") && arenaConfiguration.contains("Spawns.Red.Z")) {
			redSpawn = new Location(Bukkit.getWorld(arenaConfiguration.getString("Spawns.Red.World")), arenaConfiguration.getDouble("Spawns.Red.X"), arenaConfiguration.getDouble("Spawns.Red.Y"), arenaConfiguration.getDouble("Spawns.Red.Z"));
		}
		if (arenaConfiguration.contains("Spawns.Green.World") && arenaConfiguration.contains("Spawns.Green.X") && arenaConfiguration.contains("Spawns.Green.Y") && arenaConfiguration.contains("Spawns.Green.Z")) {
			blueSpawn = new Location(Bukkit.getWorld(arenaConfiguration.getString("Spawns.Green.World")), arenaConfiguration.getDouble("Spawns.Green.X"), arenaConfiguration.getDouble("Spawns.Green.Y"), arenaConfiguration.getDouble("Spawns.Green.Z"));
		}
		if (arenaConfiguration.contains("Spawns.Yellow.World") && arenaConfiguration.contains("Spawns.Yellow.X") && arenaConfiguration.contains("Spawns.Yellow.Y") && arenaConfiguration.contains("Spawns.Yellow.Z")) {
			blueSpawn = new Location(Bukkit.getWorld(arenaConfiguration.getString("Spawns.Yellow.World")), arenaConfiguration.getDouble("Spawns.Yellow.X"), arenaConfiguration.getDouble("Spawns.Yellow.Y"), arenaConfiguration.getDouble("Spawns.Yellow.Z"));
		}
		if (arenaConfiguration.contains("Regions.Walls")) {
			int i = 0;
			wallRegions = new ArrayList<Location[]>();
			Location[] region = new Location[2];
			region[0].setWorld(Bukkit.getWorld(arenaConfiguration.getString("Regions.Walls." + i + "World")));
			region[1].setWorld(Bukkit.getWorld(arenaConfiguration.getString("Regions.Walls." + i + "World")));
			region[0].setX(arenaConfiguration.getDouble("Regions.Walls." + i + ".X"));
			region[0].setY(arenaConfiguration.getDouble("Regions.Walls." + i + ".Y"));
			region[0].setZ(arenaConfiguration.getDouble("Regions.Walls." + i + ".Z"));
			region[1].setX(arenaConfiguration.getDouble("Regions.Walls." + i + ".X"));
			region[1].setY(arenaConfiguration.getDouble("Regions.Walls." + i + ".Y"));
			region[1].setZ(arenaConfiguration.getDouble("Regions.Walls." + i + ".Z"));
			wallRegions.set(arenaConfiguration.getInt("Regions.Walls." + i), region);
		}
	}
	
	public void runGame() {
		
	}
	public void waiting() {
		
	}
	public void setup() {
		
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

}

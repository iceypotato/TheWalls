package com.icey.walls.util;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerOriginalState {
	
	private Player player;
	private String displayName;
	private String tabName;
	private Location location;
	private ItemStack[] inv;
	private ItemStack[]	armor;
	private GameMode gamemode;
	private Collection<PotionEffect> potionEffects;
	private double hp;
	private float saturation;
	private int foodlvl;
	private Location spawnpoint;
	
	public PlayerOriginalState(Player player) {
		this.player = player;
		displayName = player.getDisplayName();
		tabName = player.getPlayerListName();
		location = player.getLocation();
		inv = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		gamemode = player.getGameMode();
		potionEffects = player.getActivePotionEffects();
		hp = player.getHealth();
		saturation = player.getSaturation();
		foodlvl = player.getFoodLevel();
		spawnpoint = player.getBedSpawnLocation();
	}

	public void restoreState() {
		player.teleport(location);
		player.getInventory().setContents(inv);
		player.getInventory().setArmorContents(armor);
		player.setGameMode(gamemode);
		player.addPotionEffects(potionEffects);
		player.setHealth(hp);
		player.setSaturation(saturation);
		player.setFoodLevel(foodlvl);
		player.setDisplayName(displayName);
		player.setPlayerListName(tabName);
		player.setBedSpawnLocation(spawnpoint, true);
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ItemStack[] getInv() {
		return inv;
	}

	public void setInv(ItemStack[] inv) {
		this.inv = inv;
	}

	public GameMode getGamemode() {
		return gamemode;
	}

	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	public Collection<PotionEffect> getPotionEffects() {
		return potionEffects;
	}

	public void setPotionEffects(Collection<PotionEffect> effects) {
		this.potionEffects = effects;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public int getFoodlvl() {
		return foodlvl;
	}

	public void setFoodlvl(int foodlvl) {
		this.foodlvl = foodlvl;
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public String getName() {
		return displayName;
	}

	public void setName(String name) {
		this.displayName = name;
	}

	public Location getSpawnpoint() {
		return spawnpoint;
	}

	public void setSpawnpoint(Location spawnpoint) {
		this.spawnpoint = spawnpoint;
	}
}

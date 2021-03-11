package com.icey.walls.framework;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerOriginalState {
	
	private Player player;
	private Location location;
	private ItemStack[] inv;
	private ItemStack[]	armor;
	private GameMode gamemode;
	private Collection<PotionEffect> effects;
	private double hp;
	private float saturation;
	private int foodlvl;
	
	public PlayerOriginalState(Player player) {
		this.player = player;
		location = player.getLocation();
		inv = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		gamemode = player.getGameMode();
		effects = player.getActivePotionEffects();
		hp = player.getHealth();
		saturation = player.getSaturation();
		foodlvl = player.getFoodLevel();
	}

	public void restoreState() {
		player.teleport(location);
		player.getInventory().setContents(inv);
		player.getEquipment().setArmorContents(armor);
		player.setGameMode(gamemode);
		player.getActivePotionEffects().addAll(effects);
		player.setHealth(hp);
		player.setSaturation(saturation);
		player.setFoodLevel(foodlvl);
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

	public Collection<PotionEffect> getEffects() {
		return effects;
	}

	public void setEffects(Collection<PotionEffect> effects) {
		this.effects = effects;
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
}

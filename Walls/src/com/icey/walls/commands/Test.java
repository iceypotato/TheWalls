package com.icey.walls.commands;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.icey.walls.MainClass;
import com.icey.walls.framework.WallsScoreboard;
import com.icey.walls.listeners.WallsTool;
import com.icey.walls.util.BlockClipboard;

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	private BlockClipboard protectedBlocks;
	private WallsScoreboard wallsSc;
	private File file;
	private FileConfiguration fileConfiguration;
	private String[] positions;
	private Collection<PotionEffect> potioneffects;
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
		positions = new String[2];
		file = new File(myplugin.getDataFolder(), "test.yml");
		fileConfiguration = YamlConfiguration.loadConfiguration(file);
		//fileConfiguration.createSection("Regions.1");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("saveeffects")) {
				potioneffects = player.getActivePotionEffects();
			}
			if (args[0].equalsIgnoreCase("cleareffects")) {
				for (PotionEffect potionEffect : player.getActivePotionEffects()) {
					player.removePotionEffect(potionEffect.getType());
				}
			}
			else if (args[0].equalsIgnoreCase("loadeffects")) {
				player.addPotionEffects(potioneffects);
			}
			else {
				sender.sendMessage("Invalid subcommand");
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid subcommand.");
		}
		return false;
	}

}

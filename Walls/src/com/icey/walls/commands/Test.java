package com.icey.walls.commands;

import java.io.File;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
			if (args[0].equals("makelist")) {
				String location = wallsTool.getWorld().getName()+","+wallsTool.getPos1().getBlockX()+","+wallsTool.getPos1().getBlockY()+","+wallsTool.getPos1().getBlockZ();
				positions[0] = location;
				location = wallsTool.getWorld().getName()+","+wallsTool.getPos2().getBlockX()+","+wallsTool.getPos2().getBlockY()+","+wallsTool.getPos2().getBlockZ();
				positions[1] = location;
				fileConfiguration.set("Regions.1", positions);
				try {
					fileConfiguration.save(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (args[0].equals("readlist")) {
				if (fileConfiguration.getList("Regions.1") == null) {
					sender.sendMessage("Null");
				}
				else {
					String[] r = fileConfiguration.getStringList("Regions.1").get(0).split(",");
					sender.sendMessage("R1:\n World: " + r[0] + "\n X: " + r[1] + "\n Y: " + r[2] + "\n Z: " + r[3]);
					r = fileConfiguration.getStringList("Regions.1").get(1).split(",");
					sender.sendMessage("R2:\n World: " + r[0] + "\n X: " + r[1] + "\n Y: " + r[2] + "\n Z: " + r[3]);
				}
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid Command.");
		}
		return false;
	}

}

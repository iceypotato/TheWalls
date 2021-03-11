package com.icey.walls.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.icey.walls.MainClass;
import com.icey.walls.framework.BlockClipboard;
import com.icey.walls.framework.WallsScoreboard;
import com.icey.walls.listeners.WallsTool;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import net.minecraft.server.v1_8_R3.Scoreboard;

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	private BlockClipboard protectedBlocks;
	private WallsScoreboard wallsSc;
	
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("setsc")) {
				wallsSc = new WallsScoreboard(2, 0, 0, 0, 0, 0, "walls1", ChatColor.GOLD+""+ChatColor.BOLD +"The Walls", "dummy", DisplaySlot.SIDEBAR);
				wallsSc.updatePlayersSB((Player) sender);
			}
			else if (args[0].equalsIgnoreCase("starttm")) {
				wallsSc.startTimer((Player) sender);
			}
			else if (args[0].equalsIgnoreCase("addred")) {
				wallsSc.setReds(wallsSc.getReds()+1);
				wallsSc.clearSB();
				wallsSc.putPrepTime();
				wallsSc.putPlayersAlive();
				wallsSc.updatePlayersSB((Player) sender);
			}
			else if (args[0].equalsIgnoreCase("rmred")) {
				wallsSc.setReds(wallsSc.getReds()-1);
				wallsSc.clearSB();
				wallsSc.putPrepTime();
				wallsSc.putPlayersAlive();
				wallsSc.updatePlayersSB((Player) sender);
			}
			else if (args[0].equalsIgnoreCase("stop")) {
				wallsSc.cancelTimer();
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid Command.");
		}
		return false;
	}

}

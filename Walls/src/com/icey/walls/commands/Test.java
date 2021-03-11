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
			if (args[0].equalsIgnoreCase("copy")) {
				protectedBlocks.addRegion(wallsTool.getPos1(), wallsTool.getPos2());
			}
			else if (args[0].equalsIgnoreCase("paste")) {
				protectedBlocks.pasteBlocksInClipboard();
			}
			else if (args[0].equalsIgnoreCase("addred")) {
			}
			else if (args[0].equalsIgnoreCase("rmred")) {
			}
			else if (args[0].equalsIgnoreCase("stop")) {
			}
			return true;
		}
		else {
			sender.sendMessage("Invalid Command.");
		}
		return false;
	}

}

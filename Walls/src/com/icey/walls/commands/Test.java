package com.icey.walls.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.icey.walls.MainClass;
import com.icey.walls.framework.BlockClipboard;
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

public class Test implements CommandExecutor {
	
	private MainClass myplugin;
	private WallsTool wallsTool;
	private BlockClipboard protectedBlocks;
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length > 0) {
			
			/* 
			 * addcopy
			 */
			if (args[0].equalsIgnoreCase("addcopy")) {
				if (protectedBlocks == null) {
					protectedBlocks = new BlockClipboard();
				}
				if (!protectedBlocks.getBlockList().isEmpty()) {
					sender.sendMessage("Clipboard was not empty");
				}
				for (int x = Math.min(wallsTool.getPos1().getBlockX(), wallsTool.getPos2().getBlockX()); x <= Math.max(wallsTool.getPos1().getBlockX(), wallsTool.getPos2().getBlockX()); x++) {
					for (int y = Math.min(wallsTool.getPos1().getBlockY(), wallsTool.getPos2().getBlockY()); y <= Math.max(wallsTool.getPos1().getBlockY(), wallsTool.getPos2().getBlockY()); y++) {
						for (int z = Math.min(wallsTool.getPos1().getBlockZ(), wallsTool.getPos2().getBlockZ()); z <= Math.max(wallsTool.getPos1().getBlockZ(), wallsTool.getPos2().getBlockZ()); z++) {
							Location loc = new Location(wallsTool.getWorld(), x, y, z);
							sender.sendMessage(loc.toString());
							protectedBlocks.addBlock(loc.getBlock());
						}
					}
				}
				sender.sendMessage("copied");
			}
			/*
			 * paste
			 */
			else if (args[0].equalsIgnoreCase("paste")) {
				protectedBlocks.pasteBlocksInClipboard();
				sender.sendMessage("pasted");
			}
			else if (args[0].equalsIgnoreCase("listcopy")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("nothing");
				}
				else {
					for (int i = 0; i < protectedBlocks.getBlockList().size(); i++) {
						sender.sendMessage(protectedBlocks.listBlocksInClipboard());
					}
				}
			}
			else if (args[0].equalsIgnoreCase("listitems")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("nothing");
				}
				else {
					for (int i = 0; i < protectedBlocks.getBlockList().size(); i++) {
						protectedBlocks.listItemStacks();
					}
				}
			}
			else if (args[0].equalsIgnoreCase("clear")) {
				if (protectedBlocks == null || protectedBlocks.getBlockList().size() == 0) {
					sender.sendMessage("Empty");
				}
				else {
					protectedBlocks.clear();
					sender.sendMessage("cleared.");
				}
			}
			return true;
		}
		return false;
	}

}

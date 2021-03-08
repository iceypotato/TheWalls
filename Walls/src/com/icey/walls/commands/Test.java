package com.icey.walls.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.icey.walls.MainClass;
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
	
	public Test(MainClass main, WallsTool wallsTool) {
		this.myplugin = main;
		this.wallsTool = wallsTool;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length > 0) {
			
			Vector vec1 = new Vector(wallsTool.getPos1().getBlockX(), wallsTool.getPos1().getBlockY(), wallsTool.getPos1().getBlockZ());
			Vector vec2 = new Vector(wallsTool.getPos2().getBlockX(), wallsTool.getPos2().getBlockY(), wallsTool.getPos2().getBlockZ());
			World world = BukkitUtil.getLocalWorld(wallsTool.getWorld());
			CuboidRegion region = new CuboidRegion(world, vec1, vec2);
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);

			
			// addcopy \\
			if (args[0].equalsIgnoreCase("addcopy")) {
				BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
				ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
				forwardExtentCopy.setRemovingEntities(false);
			    try {
					Operations.complete(forwardExtentCopy);
				} catch (WorldEditException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream(myplugin.getDataFolder() + "/" + "region.schem"))) {
				    writer.write(clipboard, world.getWorldData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage("copied");
			}
			
			// paste \\
			else if (args[0].equalsIgnoreCase("paste")) {
				File file = new File(myplugin.getDataFolder() + "/" + "region.schem");
				Clipboard clipboard;
				ClipboardFormat format = ClipboardFormat.findByFile(file);
				ClipboardReader reader;
				try {
					reader = format.getReader(new FileInputStream(file));
					clipboard = reader.read(world.getWorldData());
					Operation operation = new ClipboardHolder(clipboard, world.getWorldData()).createPaste(clipboard, world.getWorldData()).to(vec1).ignoreAirBlocks(false).build();
					Operations.complete(operation);
				} catch (WorldEditException | IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage("pasted");
			}
			return true;
		}
		return false;
	}

}

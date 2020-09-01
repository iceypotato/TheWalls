package com.icey.walls;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Location;
import org.bukkit.block.Block;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
public class PasteSchematic {
	 @SuppressWarnings("deprecation")
	    public PasteSchematic(File f,Location loc){
	        try {
	            FileInputStream fis;
	            fis = new FileInputStream(f);
	            NBTTagCompound nbt = NBTCompressedStreamTools.a(fis);
	            short width = nbt.getShort("Width");
	            short height = nbt.getShort("Height");
	            short length = nbt.getShort("Length");
	        
	            byte[] blocks = nbt.getByteArray("Blocks");
	            byte[] data = nbt.getByteArray("Data");
	        
	            for (int x = 0; x < width; ++x) {
	                for (int y = 0; y < height; ++y) {
	                    for (int z = 0; z < length; ++z) {
	                        int index = y * width * length + z * width + x;
	                        Block block = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
	                        block.setTypeIdAndData(blocks[index], data[index], true);
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}

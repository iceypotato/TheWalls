package com.icey.walls.versions.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.icey.walls.framework.ActionBarSender;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class WallsActionBarSender implements ActionBarSender {
	
    public void setActionBar(Player p, String msg) {
    	PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + msg + "\"}"), (byte) 2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

}

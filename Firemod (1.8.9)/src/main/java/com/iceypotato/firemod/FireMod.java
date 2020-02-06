package com.iceypotato.firemod;

import com.icye.item.ItemManager;
import com.icye.item.RedAxe;
import com.icye.item.RedIngot;
import com.icye.item.RedPickaxe;
import com.icye.item.RedShovel;
import com.icye.item.RedSword;
import com.icye.item.WTFEXE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = FireMod.MODID, version = FireMod.VERSION)
public class FireMod
{
    public static final String MODID = "FireMod";
    public static final String VERSION = "1.0";
    
    public static ToolMaterial redMatter;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	redMatter = EnumHelper.addToolMaterial("RedMatter", 3, 38520, 500, 40, Integer.MAX_VALUE);
    	
    	ItemManager.mainRegistry();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        
        //Shaped Crafting Recipes
        GameRegistry.addShapedRecipe(new ItemStack(ItemManager.redPickaxe, 1), "xxx", " y ", " y ", 'x', ItemManager.redIngot, 'y', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemManager.redAxe, 1), "xx", "xy", " y", 'x', ItemManager.redIngot, 'y', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemManager.redShovel, 1), "x", "y", "y", 'x', ItemManager.redIngot, 'y', Items.stick);
        GameRegistry.addShapedRecipe(new ItemStack(ItemManager.redSword, 1), "x", "x", "y", 'x', ItemManager.redIngot, 'y', Items.stick);
        
        //Shapeless Crafting Recipes
        GameRegistry.addShapelessRecipe(new ItemStack(Items.diamond, 4), Items.poisonous_potato, Items.apple);
        
        //Smelting Recipes
        
        //Render Item
        if(event.getSide() == Side.CLIENT);
        {
        	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        	
        	renderItem.getItemModelMesher().register(ItemManager.redIngot, 0, new ModelResourceLocation(FireMod.MODID + ":" + RedIngot.name, "inventory"));
        	renderItem.getItemModelMesher().register(ItemManager.redPickaxe, 0, new ModelResourceLocation(FireMod.MODID + ":" + RedPickaxe.name, "inventory"));
        	renderItem.getItemModelMesher().register(ItemManager.redAxe, 0, new ModelResourceLocation(FireMod.MODID + ":" + RedAxe.name, "inventory"));
        	renderItem.getItemModelMesher().register(ItemManager.redShovel, 0, new ModelResourceLocation(FireMod.MODID + ":" + RedShovel.name, "inventory"));
        	renderItem.getItemModelMesher().register(ItemManager.redSword, 0, new ModelResourceLocation(FireMod.MODID + ":" + RedSword.name, "inventory"));
        	renderItem.getItemModelMesher().register(ItemManager.wtfexe, 0, new ModelResourceLocation(FireMod.MODID + ":" + WTFEXE.name, "inventory"));
        }
    }
}

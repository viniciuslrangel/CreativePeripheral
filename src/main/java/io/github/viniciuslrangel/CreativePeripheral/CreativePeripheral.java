package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = CreativePeripheral.MODID, version = CreativePeripheral.VERSION, dependencies = "required-after:ComputerCraft")
public class CreativePeripheral {
    public static final String MODID = "CreativePeripheral";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static CreativePeripheral creativePeripheral;

    public BlockCreativePeripheral blockCreativePeripheral;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        blockCreativePeripheral = new BlockCreativePeripheral();
        GameRegistry.registerBlock(blockCreativePeripheral, BlockCreativePeripheral.NAME);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(blockCreativePeripheral), new ItemMeshDefinition() {
            ModelResourceLocation rl = new ModelResourceLocation(MODID + ":" + BlockCreativePeripheral.NAME, "inventory");

            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return rl;
            }
        });

        GameRegistry.registerTileEntity(TileEntityCreativePeripheral.class, TileEntityCreativePeripheral.NAME);
        ComputerCraftAPI.registerPeripheralProvider(new TileEntityCreativePeripheral.Provider());
        ComputerCraftAPI.registerTurtleUpgrade(new TurtleCreativePeripheral());
    }

}

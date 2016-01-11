package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.ComputerCraftAPI;
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
        GameRegistry.registerTileEntity(TileEntityCreativePeripheral.class, TileEntityCreativePeripheral.NAME);
        ComputerCraftAPI.registerPeripheralProvider(new TileEntityCreativePeripheral.Provider());
        ComputerCraftAPI.registerTurtleUpgrade(new TurtleCreativePeripheral());
    }

}

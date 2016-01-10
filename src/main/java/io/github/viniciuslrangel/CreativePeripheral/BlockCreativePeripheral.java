package io.github.viniciuslrangel.CreativePeripheral;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;

/**
 * By viniciuslrangel
 */
public class BlockCreativePeripheral extends Block implements ITileEntityProvider {

    public static final String NAME = "BlockCreativePeripheral";

    public BlockCreativePeripheral() {
        super(Material.iron);
        setBlockUnbreakable();
        setUnlocalizedName(NAME);
        try {
            Field creativeTab = Class.forName("dan200.computercraft.ComputerCraft").getField("mainCreativeTab");
            CreativeTabs c = (CreativeTabs) creativeTab.get(null);
            setCreativeTab(c);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCreativePeripheral();
    }
}

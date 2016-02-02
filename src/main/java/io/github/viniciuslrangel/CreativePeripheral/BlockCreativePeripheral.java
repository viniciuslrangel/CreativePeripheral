package io.github.viniciuslrangel.CreativePeripheral;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.lang.reflect.Field;

/**
 * By viniciuslrangel
 */
public class BlockCreativePeripheral extends Block implements ITileEntityProvider {

    public static final String NAME = "BlockCreativePeripheral";

    public static final PropertyDirection DIRECTION = PropertyDirection.create("direction");

    public BlockCreativePeripheral() {
        super(Material.iron);
        setBlockUnbreakable();
        setUnlocalizedName(NAME);
        translucent = true;
        fullBlock = false;
        setDefaultState(blockState.getBaseState().withProperty(DIRECTION, EnumFacing.DOWN));
        try {
            Field creativeTab = Class.forName("dan200.computercraft.ComputerCraft").getField("mainCreativeTab");
            CreativeTabs c = (CreativeTabs) creativeTab.get(null);
            setCreativeTab(c);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        switch ((EnumFacing)worldIn.getBlockState(pos).getValue(DIRECTION)){
            case UP:
                setBlockBounds(0.125f, 0f, 0.125f, 0.875f, 0.1875f, 0.875f);
                break;
            case DOWN:
                setBlockBounds(0.125f, 1f, 0.125f, 0.875f, 0.8125f, 0.875f);
                break;
            case EAST:
                setBlockBounds(0f, 0.125f, 0.125f, 0.1875f, 0.875f, 0.875f);
                break;
            case NORTH:
                setBlockBounds(0.125f, 0.125f, 1f, 0.875f, 0.875f, 0.8125f);
                break;
            case SOUTH:
                setBlockBounds(0.125f, 0.125f, 0f, 0.875f, 0.875f, 0.1875f);
                break;
            case WEST:
                setBlockBounds(0.8125f, 0.125f, 0.125f, 1f, 0.875f, 0.875f);
                break;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, DIRECTION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(DIRECTION, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(DIRECTION)).getIndex();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCreativePeripheral();
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return blockState.getBaseState().withProperty(DIRECTION, facing);
    }
}

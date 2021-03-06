package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

/**
 * By viniciuslrangel
 */
public class TurtleCreativePeripheral implements ITurtleUpgrade {

    @Override
    public ResourceLocation getUpgradeID() {
        return new ResourceLocation(CreativePeripheral.MODID, TileEntityCreativePeripheral.PERIPHERAL);
    }

    @Override
    public int getLegacyUpgradeID() {
        return -1;
    }

    @Override
    public String getUnlocalisedAdjective() {
        return TileEntityCreativePeripheral.PERIPHERAL;
    }

    @Override
    public TurtleUpgradeType getType() {
        return TurtleUpgradeType.Peripheral;
    }

    @Override
    public ItemStack getCraftingItem() {
        return new ItemStack(CreativePeripheral.creativePeripheral.blockCreativePeripheral);
    }

    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new TileEntityCreativePeripheral();
    }

    @Override
    public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) { //TODO Model
        BlockCreativePeripheral block = CreativePeripheral.creativePeripheral.blockCreativePeripheral;
        IBlockState s = block.getDefaultState();
        s = s.withProperty(BlockCreativePeripheral.DIRECTION, side == TurtleSide.Right ? EnumFacing.WEST : EnumFacing.EAST);
        return Pair.of(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(s, turtle.getWorld(), BlockPos.ORIGIN),
                new Matrix4f(-1f, 0f ,0f ,0f,
                            0f, 0f, 0f, 0f,
                            0f, 0f, 0f, 0f,
                            0f, 0f, 0f, 0f
                )
        );
    }

    @Override
    public void update(ITurtleAccess turtle, TurtleSide side) {
        if (turtle.getPeripheral(side) != null && turtle.getPeripheral(side) instanceof TileEntityCreativePeripheral) {
            TileEntityCreativePeripheral t = (TileEntityCreativePeripheral) turtle.getPeripheral(side);
            t.setPos(turtle.getPosition());
            t.setWorldObj(turtle.getWorld());
        }
    }
}

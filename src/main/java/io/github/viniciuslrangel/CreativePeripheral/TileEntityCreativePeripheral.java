package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.HashMap;

/**
 * By viniciuslrangel
 */
public class TileEntityCreativePeripheral extends TileEntity implements IPeripheral {

    public static final String NAME = "tileEntityCreativePeripheral";
    public static final String PERIPHERAL = "creativePeripheral";

    private static final String[] methods = {};

    public TileEntityCreativePeripheral(){}

    @Override
    public String getType() {
        return PERIPHERAL;
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"getWorlds", "getPlayers", ""};
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        WorldServer[] worlds = MinecraftServer.getServer().worldServers;
        switch (method){
            case 0:
                worlds = MinecraftServer.getServer().worldServers;
                HashMap<Integer, Integer> toReturn = new HashMap<>();
                for(WorldServer world:worlds)
                    toReturn.put(toReturn.size()+1, world.provider.getDimensionId());
                return new Object[]{toReturn};
            case 1:
                if(arguments.length > 0 && !(arguments[0] instanceof Boolean))
                    throw new LuaException("getPlayers([boolean uuid])");
                HashMap<Integer, Object> players = new HashMap<>();
                for(WorldServer world:worlds)
                    for(Object player:world.playerEntities)
                        if(arguments.length > 0  && ((Boolean) arguments[0]) == true)
                            players.put(players.size()+1, ((EntityPlayer) player).getUniqueID().toString());
                        else
                            players.put(players.size()+1, ((EntityPlayer) player).getName());
                return new Object[]{players};
        }
        return null;
    }

    @Override
    public void attach(IComputerAccess computer) {}

    @Override
    public void detach(IComputerAccess computer) {}

    @Override
    public boolean equals(IPeripheral other) {
        return other == null ? this == null : other.equals(this);
    }


    public static class Provider implements IPeripheralProvider{

        @Override
        public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileEntityCreativePeripheral){
                return (IPeripheral) te;
            }
            return null;
        }
    }

}

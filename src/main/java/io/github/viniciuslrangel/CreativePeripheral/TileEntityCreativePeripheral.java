package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.filesystem.IMount;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * By viniciuslrangel
 */
public class TileEntityCreativePeripheral extends TileEntity implements IPeripheral {

    public static final String NAME = "tileEntityCreativePeripheral";
    public static final String PERIPHERAL = "Creative";

    private static final String[] methods = {"getPlayers", "getWorlds", "getWorldSeed", "getWorldTime", "isWorldBlockLoaded", "getBlock", "setBlock"};
    private static Mount mount = new Mount();

    @Override
    public String getType() {
        return PERIPHERAL;
    }

    @Override
    public String[] getMethodNames() {
        return methods;
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        WorldServer[] worlds = MinecraftServer.getServer().worldServers;
        switch (method) {
            case 0:
                if (arguments.length > 1 && !(arguments[0] instanceof Boolean))
                    throw new LuaException("Usage: getPlayers([boolean uuid])");
                HashMap<Integer, String> players = new HashMap<>();
                for (WorldServer world : worlds)
                    for (Object player : world.playerEntities)
                        if (arguments.length > 0 && ((Boolean) arguments[0]))
                            players.put(players.size() + 1, ((EntityPlayer) player).getUniqueID().toString());
                        else
                            players.put(players.size() + 1, ((EntityPlayer) player).getName());
                return new Object[]{players};
            case 1:
                HashMap<String, Integer> toReturn = new HashMap<>();
                for (WorldServer world : worlds)
                    toReturn.put(world.provider.getDimensionName(), world.provider.getDimensionId());
                return new Object[]{toReturn};
            case 2:
                if (arguments.length > 0 && !(arguments[0] instanceof Double)) {
                    throw new LuaException("Usage: getWorldSeed(number dimensionId)");
                }
                int id;
                if (arguments.length > 0)
                    id = ((Double) arguments[0]).intValue();
                else
                    id = 0;
                for (WorldServer world : worlds)
                    if (world.provider.getDimensionId() == id)
                        return new Object[]{world.provider.getSeed()};
            case 3:
                if (arguments.length < 1 || !(arguments[0] instanceof Double)) {
                    throw new LuaException("Usage: getWorldTime(number dimensionId)");
                }
                id = ((Double) arguments[0]).intValue();
                for (WorldServer world : worlds)
                    if (world.provider.getDimensionId() == id)
                        return new Object[]{world.provider.getWorldTime()};
            case 4:
                if (arguments.length < 4)
                    throw new LuaException("Usage: isWorldBlockLoaded(number dimensionId, number x, number y, number z)");
                for (int i = 0; i < 4; i++)
                    if (!(arguments[i] instanceof Double))
                        throw new LuaException("Usage: isWorldBlockLoaded(number dimensionId, number x, number y, number z)");
                id = ((Double) arguments[0]).intValue();
                int x = ((Double) arguments[1]).intValue();
                int y = ((Double) arguments[2]).intValue();
                int z = ((Double) arguments[3]).intValue();
                for (WorldServer world : worlds)
                    if (world.provider.getDimensionId() == id)
                        return new Object[]{world.isBlockLoaded(new BlockPos(x, y, z))};
            case 5:
                if (arguments.length < 4)
                    throw new LuaException("Usage: getBlock(number dimensionId, number x, number y, number z)");
                for (int i = 0; i < 4; i++)
                    if (!(arguments[i] instanceof Double))
                        throw new LuaException("Usage: getBlock(number dimensionId, number x, number y, number z)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                for (WorldServer world : worlds)
                    if (world.provider.getDimensionId() == id) {
                        TileEntity te = world.getTileEntity(pos);
                        return new Object[]{
                                te.getBlockType().getUnlocalizedName(),
                                te.getBlockMetadata(),
                                te.getTileData().toString()
                        };
                    }
        }

        return null;
    }

    private EntityPlayer getPlayer(String nick) {
        for (WorldServer world : MinecraftServer.getServer().worldServers)
            for (Object player : world.playerEntities)
                if (((EntityPlayer) player).getName().equalsIgnoreCase(nick))
                    return (EntityPlayer) player;
        return null;
    }


    @Override
    public void attach(IComputerAccess computer) {
        computer.mount(PERIPHERAL, mount);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computer.unmount(PERIPHERAL);
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other != null && other == this;
    }


    public static class Provider implements IPeripheralProvider {

        @Override
        public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityCreativePeripheral) {
                return (IPeripheral) te;
            }
            return null;
        }
    }

    public static class Mount implements IMount {

        private static final String PATH = "/assets/creativeperipheral/lua/";
        private List<String> files;

        public Mount() {
            files = new ArrayList<>();
            try {
                Path main = Paths.get(getClass().getResource(PATH).toURI());

                try (DirectoryStream<Path> directory = Files.newDirectoryStream(main)) {
                    for (Path file : directory) {
                        files.add(file.getFileName().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean exists(String path) throws IOException {
            return path.isEmpty() || files.contains(path);
        }

        @Override
        public boolean isDirectory(String path) throws IOException {
            return path.isEmpty();
        }

        @Override
        public void list(String path, List<String> contents) throws IOException {
            contents.addAll(files);
        }

        @Override
        public long getSize(String path) throws IOException {
            return 0;
        }

        @Override
        public InputStream openForRead(String path) throws IOException {
            if (!files.contains(path)) throw new IOException();
            return getClass().getResourceAsStream(PATH + path);
        }
    }

}

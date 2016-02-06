package io.github.viniciuslrangel.CreativePeripheral;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * By viniciuslrangel
 */
public class TileEntityCreativePeripheral extends TileEntity implements IPeripheral {

    public static final String NAME = "tileEntityCreativePeripheral";
    public static final String PERIPHERAL = "Creative";

    private static final String[] methods = {"getPlayers", "getWorlds", "getWorldSeed", "getWorldTime", "isWorldBlockLoaded", "getBlock", "getAllBlocksName", "setBlock", "setBlockState", "getBlockState", "getBlockNbt", "setBlockNbt", "addBlockNbt", "getEntityList", "getPlayer", "getEntityNbt", "setEntityNbt", "addEntityNbt", "setEntityPosition", "setEntityRotation", "setEntityVelocity", "getPeripheralPosition"};
    private static Mount mount = new Mount();

    @Override
    public String getType() {
        return PERIPHERAL;
    }

    @Override
    public String[] getMethodNames() {
        return methods;
    }

    @SuppressWarnings("null")
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
//        try { //Used for tests
        switch (method) {
            case 0://getPlayers
                if (arguments.length > 1 && !(arguments[0] instanceof Boolean))
                    throw new LuaException("Usage: getPlayers([boolean uuid])");
                HashMap<Object, String> players = new HashMap<>();
                WorldServer[] worlds = MinecraftServer.getServer().worldServers;
                for (WorldServer world : worlds)
                    for (Object player : world.playerEntities)
                        if (arguments.length > 0 && ((Boolean) arguments[0]))
                            players.put(((EntityPlayer) player).getName(), ((EntityPlayer) player).getUniqueID().toString());
                        else
                            players.put(players.size() + 1, ((EntityPlayer) player).getName());
                return new Object[]{players};
            case 1://getWorlds
                worlds = MinecraftServer.getServer().worldServers;
                HashMap<String, Integer> toReturn = new HashMap<>();
                for (WorldServer world : worlds)
                    toReturn.put(world.provider.getDimensionName(), world.provider.getDimensionId());
                return new Object[]{toReturn};
            case 2://getWorldSeed
                if (arguments.length > 0 && !(arguments[0] instanceof Double)) {
                    throw new LuaException("Usage: getWorldSeed(number dimensionId)");
                }
                int id;
                if (arguments.length > 0)
                    id = ((Double) arguments[0]).intValue();
                else
                    id = 0;
                return new Object[]{getWorld(id).provider.getSeed()};
            case 3://getWorldTime
                if (arguments.length < 1 || !(arguments[0] instanceof Double)) {
                    throw new LuaException("Usage: getWorldTime(number dimensionId)");
                }
                id = ((Double) arguments[0]).intValue();
                return new Object[]{getWorld(id).provider.getWorldTime()};
            case 4://isWorldBlockLoaded
                if (arguments.length < 4)
                    throw new LuaException("Usage: isWorldBlockLoaded(number dimensionId, number x, number y, number z)");
                for (int i = 0; i < 4; i++)
                    if (!(arguments[i] instanceof Double))
                        throw new LuaException("Usage: isWorldBlockLoaded(number dimensionId, number x, number y, number z)");
                id = ((Double) arguments[0]).intValue();
                int x = ((Double) arguments[1]).intValue();
                int y = ((Double) arguments[2]).intValue();
                int z = ((Double) arguments[3]).intValue();
                return new Object[]{getWorld(id).isBlockLoaded(new BlockPos(x, y, z))};
            case 5://getBlock
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
                WorldServer world = getWorld(id);
                IBlockState blockState = world.getBlockState(pos);
                return new Object[]{
                        Block.blockRegistry.getNameForObject(blockState.getBlock()).toString(),
                };

            case 6://getAllBlocksName
                HashMap<Integer, String> toReturn2 = new HashMap<>();
                for (Object s : Item.itemRegistry.getKeys())
                    toReturn2.put(toReturn2.size() + 1, ((ResourceLocation) s).getResourcePath());
                return new Object[]{toReturn2};

            case 7://setBlock
                if (arguments.length < 5)
                    throw new LuaException("Usage: setBlock(number dimensionId, number x, number y, number z, name)");

                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                world = getWorld(((Double) arguments[0]).intValue());
                Block b = Block.getBlockFromName((String) arguments[4]);
                world.setBlockState(pos, b.getDefaultState());
                break;
            case 8://setBlockState //TODO Not working
                throw new LuaException("WIP");
                /*if (arguments.length < 5)
                    throw new LuaException("Usage: setBlockState(number dimensionId, number x, number y, number z, state)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                world = getWorld(id);
                IBlockState state = null;
                try {
                    state = PropertyParser.fromString((HashMap<Integer, HashMap<String, Object>>) arguments[4]);
                    world.setBlockState(pos, Blocks.air.getDefaultState());
                    return new Object[]{world.setBlockState(pos, state)};
                } catch (ClassNotFoundException e) {
                    throw new LuaException(e.getMessage());
                }*/
            case 9://getBlockState
                if (arguments.length < 4)
                    throw new LuaException("Usage: getBlockState(number dimensionId, number x, number y, number z)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                return new Object[]{PropertyParser.toString(getWorld(id).getBlockState(pos))};
            case 10://getBlockNbt
                if (arguments.length < 4)
                    throw new LuaException("Usage: getBlockNbt(number dimensionId, number x, number y, number z)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                world = getWorld(id);
                TileEntity te = world.getTileEntity(pos);
                if (te == null)
                    return new Object[]{false};
                NBTTagCompound nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 11://setBlockNbt
                if (arguments.length < 4)
                    throw new LuaException("Usage: setBlockNbt(number dimensionId, number x, number y, number z, json nbt)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                world = getWorld(id);
                te = world.getTileEntity(pos);
                if (te == null)
                    return new Object[]{false};
                try {
                    te.readFromNBT(NbtParser.toNbt((HashMap<Object, Object>) arguments[4]));
                } catch (NBTException e) {
                    new LuaException("Invalid NBT:" + e.getMessage());
                }
                nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 12://addBlockNbt
                if (arguments.length < 4)
                    throw new LuaException("Usage: setBlockNbt(number dimensionId, number x, number y, number z, json nbt)");
                id = ((Double) arguments[0]).intValue();
                x = ((Double) arguments[1]).intValue();
                y = ((Double) arguments[2]).intValue();
                z = ((Double) arguments[3]).intValue();
                pos = new BlockPos(x, y, z);
                world = getWorld(id);
                te = world.getTileEntity(pos);
                if (te == null)
                    return new Object[]{false};
                nbt = new NBTTagCompound();
                te.writeToNBT(nbt);
                try {
                    nbt.merge(NbtParser.toNbt((HashMap<Object, Object>) arguments[4]));
                } catch (NBTException e) {
                    new LuaException("Invalid NBT:" + e.getMessage());
                }
                te.readFromNBT(nbt);
                te.writeToNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 13://getEntityList
                if (arguments.length != 4 && arguments.length != 1)
                    throw new LuaException("Usage: getEntityList([number dimensionId, number x, number y, number z,] string match)");
                String match;
                if (arguments.length == 4) {
                    id = ((Double) arguments[0]).intValue();
                    x = ((Double) arguments[1]).intValue();
                    y = ((Double) arguments[2]).intValue();
                    z = ((Double) arguments[3]).intValue();
                    match = (String) arguments[4];
                } else {
                    id = 0;
                    x = 0;
                    y = 0;
                    z = 0;
                    match = (String) arguments[0];
                }
                world = getWorld(id);
                List<Entity> entities = PlayerSelector.matchEntities(getCommandSender(world, new Vec3(x, y, z)), match, Entity.class);
                HashMap<Integer, String> t2 = new HashMap<>();
                for (Entity entity : entities)
                    t2.put(t2.size() + 1, entity.getUniqueID().toString());
                return new Object[]{t2};
            case 14://getPlayer
                if (arguments.length < 1)
                    throw new LuaException("Usage: getPlayer(nick/uuid)");
                EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername((String) arguments[0]);
                if (player == null)
                    try {
                        player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(UUID.fromString((String) arguments[0]));
                    } catch (IllegalArgumentException e) {
                    }
                if (player == null)
                    return new Object[]{false};
                HashMap<String, Object> t4 = new HashMap<>();
                t4.put("name", player.getName());
                t4.put("uuid", player.getUniqueID().toString());
                t4.put("displayName", player.getDisplayNameString());
                t4.put("health", String.valueOf(player.getHealth()));
                t4.put("maxHealth", String.valueOf(player.getMaxHealth()));
                HashMap<String, Double> t3 = new HashMap<>();
                t3.put("x", player.posX);
                t3.put("y", player.posY);
                t3.put("z", player.posZ);
                t4.put("position", t3);

                return new Object[]{t4};
            case 15://getEntityNbt
                if (arguments.length < 1)
                    throw new LuaException("Usage: getEntityNbt(UUID)");
                UUID uuid;
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                nbt = new NBTTagCompound();
                Entity entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                if (entity == null)
                    return new Object[]{false};
                entity.writeToNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 16://setEntityNbt
                if (arguments.length < 2)
                    throw new LuaException("Usage: setEntityNbt(UUID, nbt)");
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                if (entity == null)
                    return new Object[]{false};
                try {
                    nbt = NbtParser.toNbt((HashMap<Object, Object>) arguments[1]); //TODO Java Json(Minecraft style :/ ) parser
                } catch (NBTException e) {
                    throw new LuaException("Invalid NBT format");
                }
                entity.readFromNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 17://addEntityNbt
                if (arguments.length < 2)
                    throw new LuaException("Usage: addEntityNbt(UUID, nbt)");
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                nbt = new NBTTagCompound();
                entity.writeToNBT(nbt);
                if (entity == null)
                    return new Object[]{false};
                try {
                    nbt.merge(NbtParser.toNbt((HashMap<Object, Object>) arguments[1]));
                } catch (NBTException e) {
                    throw new LuaException("Invalid NBT format");
                }
                entity.readFromNBT(nbt);
                return new Object[]{NbtParser.fromNbt(nbt)};
            case 18://setEntityPosition
                if (arguments.length < 5)
                    throw new LuaException("Usage: setEntityPosition(UUID, boolean relative, number x, number y, number z");
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                boolean relative = (boolean) arguments[1];
                double xd = (Double) arguments[2];
                double yd = (Double) arguments[3];
                double zd = (Double) arguments[4];
                entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                if (relative) {
                    xd += entity.posX;
                    yd += entity.posY;
                    zd += entity.posZ;
                }
                entity.setPositionAndUpdate(xd, yd, zd);
                break;
            case 19://setEntityRotation
                if (arguments.length < 5)
                    throw new LuaException("Usage: setEntityRotation(UUID, boolean relative, number yaw, number pitch");
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                relative = (boolean) arguments[1];
                float yaw = ((Double) arguments[2]).floatValue();
                float pitch = ((Double) arguments[3]).floatValue();
                entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                if (relative) {
                    yaw += entity.rotationYaw;
                    pitch += entity.rotationPitch;
                }
                entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, yaw, pitch);
                break;
            case 20://setEntityVelocity
                if (arguments.length < 5)
                    throw new LuaException("Usage: setEntityVelocity(UUID, boolean relative, number x, number y, number z");
                try {
                    uuid = UUID.fromString((String) arguments[0]);
                } catch (IllegalArgumentException e) {
                    throw new LuaException("Invalid UUID format");
                }
                relative = (boolean) arguments[1];
                xd = ((Double) arguments[2]);
                yd = ((Double) arguments[3]);
                zd = ((Double) arguments[4]);
                entity = MinecraftServer.getServer().getEntityFromUuid(uuid);
                if (relative) {
                    xd += entity.motionX;
                    yd += entity.motionY;
                    zd += entity.motionZ;
                }
                entity.motionX = xd;
                entity.motionY = yd;
                entity.motionZ = zd;
                break;
            case 21://getPeripheralPosition
                HashMap<Integer, Integer> toReturn3 = new HashMap<>();
                BlockPos pos = getPos();
                toReturn3.put(1, pos.getX());
                toReturn3.put(2, pos.getY());
                toReturn3.put(3, pos.getZ());
                return new Object[]{toReturn3};
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    private WorldServer getWorld(int id) {
        for (WorldServer world : MinecraftServer.getServer().worldServers)
            if (world.provider.getDimensionId() == id)
                return world;
        return null;
    }

    private ICommandSender getCommandSender(final WorldServer world, final Vec3 position) {
        return new ICommandSender() {

            BlockPos pos = new BlockPos(position.xCoord, position.yCoord, position.zCoord);

            @Override
            public String getName() {
                return "CreativePeripheral";
            }

            @Override
            public IChatComponent getDisplayName() {
                return new ChatComponentText(getName());
            }

            @Override
            public void addChatMessage(IChatComponent message) {
            }

            @Override
            public boolean canUseCommand(int permLevel, String commandName) {
                return true;
            }

            @Override
            public BlockPos getPosition() {
                return pos;
            }

            @Override
            public Vec3 getPositionVector() {
                return position;
            }

            @Override
            public World getEntityWorld() {
                return world;
            }

            @Override
            public Entity getCommandSenderEntity() {
                return FakePlayerFactory.getMinecraft(world);
            }

            @Override
            public boolean sendCommandFeedback() {
                return false;
            }

            @Override
            public void setCommandStat(CommandResultStats.Type type, int amount) {
            }
        };
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
            if (te instanceof TileEntityCreativePeripheral && world.getBlockState(pos).getBlock() == CreativePeripheral.creativePeripheral.blockCreativePeripheral)
                if(((EnumFacing)world.getBlockState(pos).getValue(BlockCreativePeripheral.DIRECTION)).getOpposite() == side)
                    return (IPeripheral) te;
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

                try {
                    DirectoryStream<Path> directory = Files.newDirectoryStream(main);
                    for (Path file : directory) {
                        files.add(file.getFileName().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Error mounting Creative Peripheral Lua APIs");
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

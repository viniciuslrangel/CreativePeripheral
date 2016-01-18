package io.github.viniciuslrangel.CreativePeripheral;

import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.Set;

/**
 * By viniciuslrangel
 */
public class NbtParser {


    public static HashMap<Object, Object> fromNbt(NBTTagCompound nbt) {
        HashMap<Object, Object> main = new HashMap<>();
        for (String key : (Set<String>) nbt.getKeySet()) {
            HashMap<Object, Object> c = new HashMap<>();
            NBTBase n = nbt.getTag(key);
            switch (nbt.getTagType(key)) {
                case 0:
                    main.put(key, nbt.getTag(key).toString());
                case 1:
                    c.put("value", ((NBTTagByte)n).getByte());
                    c.put("type", "byte");
                    main.put(key, c);
                case 2:
                    c.put("value", ((NBTTagShort)n).getShort());
                    c.put("type", "short");
                    main.put(key, c);
                case 3:
                    c.put("value", ((NBTTagInt)n).getInt());
                    c.put("type", "int");
                    main.put(key, c);
                case 4:
                    c.put("value", ((NBTTagLong)n).getLong());
                    c.put("type", "long");
                    main.put(key, c);
                case 5:
                    c.put("value", ((NBTTagFloat)n).getFloat());
                    c.put("type", "float");
                    main.put(key, c);
                case 6:
                    c.put("value", ((NBTTagDouble)n).getDouble());
                    c.put("type", "double");
                    main.put(key, c);
                case 7:
                    NBTTagByteArray b = (NBTTagByteArray) n;
                    HashMap<Integer, Byte> values = new HashMap<>();
                    for(byte b2:b.getByteArray())
                        values.put(values.size()+1, b2);
                    c.put("value", values);
                    c.put("type", "byteArray");
                    main.put(key, c);
                case 8:
                    c.put("value", nbt.getTag(key).toString());
                    c.put("type", "string");
                    main.put(key, c);
                case 9:
                    NBTTagList tag = (NBTTagList) nbt.getTag(key);
                    c.put("value", fromNbtTagList(tag));
                    c.put("type", "tagList");
                    c.put("valueType", tag.getTagType());
                case 10:
                    c.put("value", fromNbt((NBTTagCompound) nbt.getTag(key)));
                    c.put("type", "tagCompound");
                    main.put(key, c);
                case 11:
                    NBTTagIntArray i = (NBTTagIntArray) n;
                    HashMap<Integer, Integer> values2 = new HashMap<>();
                    for(int i2:i.getIntArray())
                        values2.put(values2.size()+1, i2);
                    c.put("value", values2);
                    c.put("type", "intArray");
                    main.put(key, c);
            }
        }
        return main;
    }

    public static HashMap<Integer, Object> fromNbtTagList(NBTTagList nbt) {
        HashMap<Integer, Object> main = new HashMap<>();
        for (int i = 1; i < nbt.tagCount()+1; i++) {
            HashMap<Object, Object> c = new HashMap<>();
            switch (nbt.getTagType()) {
                case 0:
                    main.put(i, nbt.get(i).toString());
                case 1:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "byte");
                    main.put(i, c);
                case 2:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "short");
                    main.put(i, c);
                case 3:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "int");
                    main.put(i, c);
                case 4:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "long");
                    main.put(i, c);
                case 5:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "float");
                    main.put(i, c);
                case 6:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "double");
                    main.put(i, c);
                case 7:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "byteArray");
                    main.put(i, c);
                case 8:
                    c.put("value", nbt.get(i).toString());
                    c.put("type", "string");
                    main.put(i, c);
                case 9:
                    NBTTagList tag = (NBTTagList) nbt.get(i);
                    c.put("value", tag.toString());
                    c.put("type", "tagList");
                    c.put("valueType", tag.getTagType());
                    main.put(i, c);
                case 10:
                    c.put("value", fromNbt((NBTTagCompound) nbt.get(i)));
                    c.put("type", "tagCompound");
                    main.put(i, c);
                case 11:
                    NBTTagIntArray i = (NBTTagIntArray) n;
                    HashMap<Integer, Integer> values2 = new HashMap<>();
                    for(int i2:i.getIntArray())
                        values2.put(values2.size()+1, i2);
                    c.put("value", values2);
                    c.put("type", "intArray");
                    main.put(key, c);
            }
        }
        return main;
    }

    public static NBTTagCompound toNbt(HashMap<Object, Object> table) {
        return null;
    }


}

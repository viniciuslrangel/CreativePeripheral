package io.github.viniciuslrangel.CreativePeripheral;

import net.minecraft.nbt.*;

import java.util.*;

/**
 * By viniciuslrangel
 */
public class NbtParser {


    public static HashMap<Object, Object> fromNbt(NBTTagCompound nbt) {
        HashMap<Object, Object> main = new HashMap<>();
        for (String key : (Set<String>) nbt.getKeySet()) {
            HashMap<Object, Object> c = new HashMap<>();
            NBTBase n = nbt.getTag(key);
            c.put("type", nbt.getTagType(key));
            switch (nbt.getTagType(key)) {
                case 0:
                    main.put(key, nbt.getTag(key).toString());
                    break;
                case 1:
                    c.put("value", ((NBTTagByte) n).getByte());
                    main.put(key, c);
                    break;
                case 2:
                    c.put("value", ((NBTTagShort) n).getShort());
                    main.put(key, c);
                    break;
                case 3:
                    c.put("value", ((NBTTagInt) n).getInt());
                    main.put(key, c);
                    break;
                case 4:
                    c.put("value", ((NBTTagLong) n).getLong());
                    main.put(key, c);
                    break;
                case 5:
                    c.put("value", ((NBTTagFloat) n).getFloat());
                    main.put(key, c);
                    break;
                case 6:
                    c.put("value", ((NBTTagDouble) n).getDouble());
                    main.put(key, c);
                    break;
                case 7:
                    NBTTagByteArray b = (NBTTagByteArray) n;
                    HashMap<Double, Byte> values = new HashMap<>();
                    for (byte b2 : b.getByteArray())
                        values.put((double) (values.size() + 1), b2);
                    c.put("value", values);
                    main.put(key, c);
                    break;
                case 8:
                    c.put("value", nbt.getTag(key).toString());
                    main.put(key, c);
                    break;
                case 9:
                    NBTTagList tag = (NBTTagList) nbt.getTag(key);
                    c.put("value", fromNbtTagList(tag));
                    c.put("valueType", tag.getTagType());
                    main.put(key, c);
                    break;
                case 10:
                    c.put("value", fromNbt((NBTTagCompound) nbt.getTag(key)));
                    main.put(key, c);
                    break;
                case 11:
                    NBTTagIntArray i = (NBTTagIntArray) n;
                    HashMap<Double, Double> values2 = new HashMap<>();
                    for (int i2 : i.getIntArray())
                        values2.put((double) (values2.size() + 1), (double) i2);
                    c.put("value", values2);
                    main.put(key, c);
                    break;
            }
        }
        return main;
    }

    public static HashMap<Integer, Object> fromNbtTagList(NBTTagList nbt) {
        HashMap<Integer, Object> main = new HashMap<>();
        for (int i = 1; i < nbt.tagCount() + 1; i++) {
            NBTBase n = nbt.get(i - 1);
            switch (nbt.getTagType()) {
                case 0:
                    main.put(main.size() + 1, n.toString());
                    break;
                case 1:
                    main.put(main.size() + 1, ((NBTTagByte) n).getByte());
                    break;
                case 2:
                    main.put(main.size() + 1, ((NBTTagShort) n).getShort());
                    break;
                case 3:
                    main.put(main.size() + 1, ((NBTTagInt) n).getInt());
                    break;
                case 4:
                    main.put(main.size() + 1, ((NBTTagLong) n).getLong());
                    break;
                case 5:
                    main.put(main.size() + 1, ((NBTTagFloat) n).getFloat());
                    break;
                case 6:
                    main.put(main.size() + 1, ((NBTTagDouble) n).getDouble());
                    break;
                case 7:
                    NBTTagByteArray b = (NBTTagByteArray) n;
                    HashMap<Integer, Byte> values = new HashMap<>();
                    for (byte b2 : b.getByteArray())
                        values.put(values.size() + 1, b2);
                    main.put(main.size() + 1, values);
                    break;
                case 8:
                    main.put(main.size() + 1, n.toString());
                    break;
                case 9:
                    main.put(main.size() + 1, fromNbtTagList((NBTTagList) n));
                    break;
                case 10:
                    main.put(main.size() + 1, fromNbt((NBTTagCompound) n));
                    break;
                case 11:
                    NBTTagIntArray i3 = (NBTTagIntArray) n;
                    HashMap<Integer, Integer> values2 = new HashMap<>();
                    for (int i2 : i3.getIntArray())
                        values2.put(values2.size() + 1, i2);
                    main.put(main.size() + 1, values2);
                    break;
            }
        }
        return main;
    }

    public static NBTTagCompound toNbt(HashMap<Object, Object> table) throws NBTException {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Map.Entry<Object, Object> entry : table.entrySet()) {
            String key = String.valueOf(entry.getKey());
            HashMap<Object, Object> values = (HashMap<Object, Object>) entry.getValue();
            switch (((Double) values.get("type")).intValue()) {
                case 0:
                    nbt.setTag(key, new NBTTagEnd());
                    break;
                case 1:
                    nbt.setTag(key, new NBTTagByte(((Double) values.get("value")).byteValue()));
                    break;
                case 2:
                    nbt.setTag(key, new NBTTagShort(((Double) values.get("value")).shortValue()));
                    break;
                case 3:
                    nbt.setTag(key, new NBTTagInt(((Double) values.get("value")).intValue()));
                    break;
                case 4:
                    nbt.setTag(key, new NBTTagLong(((Double) values.get("value")).longValue()));
                    break;
                case 5:
                    nbt.setTag(key, new NBTTagFloat(((Double) values.get("value")).floatValue()));
                    break;
                case 6:
                    nbt.setTag(key, new NBTTagDouble((Double) values.get("value")));
                    break;
                case 7:
                    byte[] list2 = new byte[values.size()];
                    for (Object key3 : values.keySet()) {
                        Double key2 = (Double) key3;
                        list2[key2.intValue()] = ((Double)values.get(key2.intValue())).byteValue();
                    }
                    nbt.setTag(key, new NBTTagByteArray(list2));
                    break;
                case 8:
                    nbt.setTag(key, new NBTTagString((String) entry.getValue()));
                    break;
                case 9:
                    HashMap<Object, Object> subList = (HashMap<Object, Object>) entry.getValue();
                    nbt.setTag(key, toNbtList((HashMap<Double, Object>) subList.get("value"), ((Double) subList.get("valueType")).byteValue()));
                    break;
                case 10:
                    nbt.setTag(key, toNbt((HashMap<Object, Object>) entry.getValue()));
                    break;
                case 11:
                    HashMap<Double, Double> values3 = (HashMap<Double, Double>) entry.getValue();
                    int[] list = new int[values3.size()];
                    for (Double key2 : values3.keySet())
                        list[key2.intValue()] = values3.get(key2.intValue()).intValue();
                    nbt.setTag(key, new NBTTagIntArray(list));
                    break;
            }
        }
        return nbt;
    }

    public static NBTTagList toNbtList(HashMap<Double, Object> table, byte type) throws NBTException {
        NBTTagList nbt = new NBTTagList();
        for (Map.Entry<Double, Object> entry : table.entrySet()) {
            switch (type) {
                case 0:
                    nbt.appendTag(new NBTTagEnd());
                    break;
                case 1:
                    nbt.appendTag(new NBTTagByte(((Double) entry.getValue()).byteValue()));
                    break;
                case 2:
                    nbt.appendTag(new NBTTagShort(((Double) entry.getValue()).shortValue()));
                    break;
                case 3:
                    nbt.appendTag(new NBTTagInt(((Double) entry.getValue()).intValue()));
                    break;
                case 4:
                    nbt.appendTag(new NBTTagLong(((Double) entry.getValue()).longValue()));
                    break;
                case 5:
                    nbt.appendTag(new NBTTagFloat(((Double) entry.getValue()).floatValue()));
                    break;
                case 6:
                    nbt.appendTag(new NBTTagDouble((Double) entry.getValue()));
                    break;
                case 7:
                    HashMap<Double, Double> values2 = (HashMap<Double, Double>) entry.getValue();
                    byte[] list2 = new byte[values2.size()];
                    for (Double key : values2.keySet())
                        list2[key.intValue()] = values2.get(key.intValue()).byteValue();
                    nbt.appendTag(new NBTTagByteArray(list2));
                    break;
                case 8:
                    nbt.appendTag(new NBTTagString((String) entry.getValue()));
                    break;
                case 9:
                    HashMap<Object, Object> subList = (HashMap<Object, Object>) entry.getValue();
                    nbt.appendTag(toNbtList((HashMap<Double, Object>) subList.get("value"), ((Double) subList.get("valueType")).byteValue()));
                    break;
                case 10:
                    nbt.appendTag(toNbt((HashMap<Object, Object>) entry.getValue()));
                    break;
                case 11:
                    HashMap<Double, Double> values = (HashMap<Double, Double>) entry.getValue();
                    int[] list = new int[values.size()];
                    for (Double key : values.keySet())
                        list[key.intValue()] = values.get(key.intValue()).intValue();
                    nbt.appendTag(new NBTTagIntArray(list));
                    break;
            }
        }

        return nbt;
    }


}

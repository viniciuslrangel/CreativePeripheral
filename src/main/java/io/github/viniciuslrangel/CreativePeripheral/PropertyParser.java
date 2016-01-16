package io.github.viniciuslrangel.CreativePeripheral;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * By viniciuslrangel
 */
public class PropertyParser {

    public static HashMap<Object, HashMap<String, Object>> toString(IBlockState state) {
        HashMap<Object, HashMap<String, Object>> table = new HashMap<Object, HashMap<String, Object>>();
        HashMap<String, Object> b = new HashMap<String, Object>();
        b.put("block", Block.blockRegistry.getNameForObject(state.getBlock()).toString());
        table.put(1, b);
        for (Object _key : state.getProperties().keySet()) {
            IProperty prop = (IProperty) _key;
            HashMap<String, Object> t2 = new HashMap<>();
            t2.put("name", prop.getName());
            t2.put("className", prop.getClass().getSimpleName().substring(8));
            if (t2.get("className").equals("Enum"))
                t2.put("classValue", prop.getValueClass().getName());
            t2.put("value", prop.getValueClass() == Boolean.class || prop.getValueClass() == Integer.class ? state.getValue(prop) : state.getValue(prop).toString());
            if (t2.containsKey("classValue")) {
                HashMap<Integer, Object> t3 = new HashMap<>();
                for (Object can : prop.getAllowedValues())
                    t3.put(t3.size() + 1, can.toString());
                t2.put("canValues", t3);
            } else if (t2.get("className").equals("Integer")) {
                int l = (int) prop.getAllowedValues().toArray()[0];
                int g = (int) prop.getAllowedValues().toArray()[0];
                for (Object _can : prop.getAllowedValues()) {
                    int can = (int) _can;
                    if (can > g)
                        g = can;
                    if (can < l)
                        l = can;
                }
                t2.put("lesser", l);
                t2.put("greater", g);
            }
            table.put(table.size() + 1, t2);
        }
        return table;
    }

    public static IBlockState fromString(HashMap<Integer, HashMap<String, Object>> propS) throws ClassNotFoundException {
        List<IProperty> props = new ArrayList<>();
        List value = new ArrayList();
        Block block = null;
        for (HashMap<String, Object> t2 : propS.values()) {
            if(t2.containsKey("block")) {
                block = Block.getBlockFromName((String) t2.get("block"));
                continue;
            }
            String c = (String) t2.get("className");
            if (c.equalsIgnoreCase("Enum")) {
                Class eClass = Class.forName((String) t2.get("classValue"));
                List<Object> values = new ArrayList<>();
                values.addAll(Lists.newArrayList(eClass.getEnumConstants()));
                Class c2 = Class.forName((String) t2.get("classValue"));
                String name = (String) t2.get("name");
//                IProperty prop = PropertyEnum.<IStringSerializable>create(name, c2);
//                props.add(prop);
//                for (Object o : values) {
//                    if (o.toString().equalsIgnoreCase((String) t2.get("value")))
//                        value.add(o);
//                }
            } else if (c.equalsIgnoreCase("Bool")) {
                props.add(PropertyBool.create((String) t2.get("name")));
                value.add((Boolean) t2.get("value"));
            } else if (c.equalsIgnoreCase("Direction")) {
                props.add(PropertyDirection.create((String) t2.get("name")));
                value.add(EnumFacing.byName((String) t2.get("value")));
            } else if (c.equalsIgnoreCase("Integer")) {
                int l = ((Double) t2.get("lesser")).intValue();
                int g = ((Double) t2.get("greater")).intValue();
                props.add(PropertyInteger.create((String) t2.get("name"), l, g));
                value.add(((Double) t2.get("value")).intValue());
            }

        }
        IBlockState state = new BlockState(block, props.toArray(new IProperty[0])).getBaseState();
        for (int i = 0; i < props.size(); i++) {
            state = state.withProperty(props.get(i), (Comparable) value.get(i));
        }
        return state;
    }

}

package io.github.viniciuslrangel.CreativePeripheral;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * By viniciuslrangel
 */
public class PropertyParser {

    public static HashMap<Integer, HashMap<String, Object>> toString(IBlockState state) {
        HashMap<Integer, HashMap<String, Object>> table = new HashMap<>();
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

    public static IBlockState fromString(Block block, HashMap<Integer, HashMap<String, Object>> propS) throws ClassNotFoundException {
        List<IProperty> props = new ArrayList();
        List value = new ArrayList();
        for (HashMap<String, Object> t2 : propS.values()) {
            String c = (String) t2.get("className");
            if (c.equalsIgnoreCase("Enum")) {
                Class eClass = Class.forName((String) t2.get("classValue"));
                List<Object> values = new ArrayList<>();
                values.addAll(Lists.newArrayList(eClass.getEnumConstants()));
                props.add(PropertyEnum.create((String) t2.get("name"), Class.forName((String) t2.get("classValue")), values));
                for (Object o : values) {
                    if (o.toString().equalsIgnoreCase((String) t2.get("value")))
                        value.add(o);
                }
            } else if (c.equalsIgnoreCase("Bool")) {
                props.add(PropertyBool.create((String) t2.get("name")));
                value.add((Boolean) t2.get("value"));
            } else if (c.equalsIgnoreCase("Direction")) {
                props.add(PropertyDirection.create((String) t2.get("name")));
                value.add(EnumFacing.byName((String) t2.get("value")));
            } else if (c.equalsIgnoreCase("Integer")) {
                int l = (int) t2.get("lesser");
                int g = (int) t2.get("greater");
                props.add(PropertyInteger.create((String) t2.get("name"), l, g));
                value.add(Integer.valueOf((String) t2.get("value")));
            }

        }
        IBlockState state = new BlockState(block, props.toArray(new IProperty[0])).getBaseState();
        for (int i = 0; i < props.size(); i++) {
            state = state.withProperty(props.get(i), (Comparable) value.get(i));
        }

        return state;
    }

}

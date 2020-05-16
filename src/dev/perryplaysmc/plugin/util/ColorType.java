package dev.perryplaysmc.plugin.util;

import java.util.Random;

/**
 * Copy Right ©
 * This code is private
 * Owner: PerryPlaysMC
 * From: 11/3/19-2200
 * Package: me.perryplaysmc.titancells.utils.inventory
 * Class: ColorType
 * <p>
 * Path: me.perryplaysmc.titancells.utils.inventory.ColorType
 * <p>
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/
public enum ColorType {
    WHITE(0),
    ORANGE(1),
    MAGENTA(2),
    LIGHT_BLUE(3),
    YELLOW(4),
    LIME(5),
    PINK(6),
    GRAY(7),
    LIGHT_GRAY(8),
    CYAN(9),
    PURPLE(10),
    BLUE(11),
    BROWN(12),
    GREEN(13),
    RED(14),
    BLACK(15);

    short data;

    ColorType(int data) {
        this.data = (short) data;
    }

    public short getData() {
        return data;
    }

    public static ColorType byData(int data) {
        for(ColorType t : values()) {
            if(t.getData()==(short)data)return t;
        }
        return WHITE;
    }

    public static ColorType random() {
        return random(values());
    }

    public static ColorType random(ColorType... types) {
        return types[new Random().nextInt(types.length)];
    }

}

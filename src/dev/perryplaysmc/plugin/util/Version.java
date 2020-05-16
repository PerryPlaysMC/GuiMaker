package dev.perryplaysmc.plugin.util;

import org.bukkit.Bukkit;

public enum Version {
    v1_8(180), v1_8_R1(181), v1_8_R2(182), v1_8_R3(183),
    v1_9(190), v1_9_R1(191), v1_9_R2(192),
    v1_10(1100), v1_10_R1(1101),
    v1_11(1110), v1_11_R1(1111),
    v1_12(1120), v1_12_R1(1121),
    v1_13(1130), v1_13_R1(1131), v1_13_R2(1132),
    v1_14(1140), v1_14_R1(1141),
    v1_15(1150), v1_15_R1(1151), UNKNOWN(-1);
    static Version current = UNKNOWN, exact = UNKNOWN;
    private int ver;
    Version(int ver) {
        this.ver = ver;
    }

    public int getVersionInt() {
        return ver;
    }

    public boolean isHigher(Version v) {
        return getVersionInt()>v.getVersionInt();
    }

    public boolean isLower(Version v) {
        return getVersionInt()<v.getVersionInt();
    }

    public static boolean isCurrentLower(Version v) {
        return getCurrentVersion().isLower(v);
    }

    public static boolean isCurrentHigher(Version v) {
        if(v.name().contains("R"))
            return getCurrentVersionExact().isHigher(v);
        return getCurrentVersion().isHigher(v);
    }

    public static Version getCurrentVersionExact() {
        if(exact!=UNKNOWN) {
            return exact;
        }
        String pack =  Bukkit.getServer().getClass().getPackage().getName();
        String version = pack.substring(pack.lastIndexOf('.')+1).replaceFirst("v", "");
        Version ret = UNKNOWN;
        switch(version) {
            case "1_8_R1": {
                ret = v1_8_R1;
                break;
            } case "1_8_R2": {
                ret = v1_8_R2;
                break;
            } case "1_8_R3": {
                ret = v1_8_R3;
                break;
            } case "1_9_R1": {
                ret = v1_9_R1;
                break;
            } case "1_9_R2": {
                ret = v1_9_R2;
                break;
            } case "1_10_R1": {
                ret = v1_10_R1;
                break;
            } case "1_11_R1": {
                ret = v1_11_R1;
                break;
            } case "1_12_R1": {
                ret = v1_12_R1;
                break;
            } case "1_13_R1": {
                ret = v1_13_R1;
                break;
            } case "1_13_R2": {
                ret = v1_13_R2;
                break;
            } case "1_14_R1": {
                ret = v1_14_R1;
                break;
            } case "1_15_R1": {
                ret = v1_15_R1;
                break;
            }
        }
        if(exact==UNKNOWN || exact!=ret) exact = ret;
        return ret;
    }

    public static Version getCurrentVersion() {
        if(current!=UNKNOWN) {
            return current;
        }
        String pack =  Bukkit.getServer().getClass().getPackage().getName();
        String version = pack.substring(pack.lastIndexOf('.')+1).replaceFirst("v", "");
        Version ret = UNKNOWN;
        switch(version) {
            case "1_8_R1":
            case "1_8_R2":
            case "1_8_R3": {
                ret = v1_8;
                break;
            }
            case "1_9_R1":
            case "1_9_R2": {
                ret = v1_9;
                break;
            }
            case "1_10_R1": {
                ret = v1_10;
                break;
            } case "1_11_R1": {
                ret = v1_11;
                break;
            } case "1_12_R1": {
                ret = v1_12;
                break;
            } case "1_13_R1":
            case "1_13_R2": {
                ret = v1_13;
                break;
            }
            case "1_14_R1": {
                ret = v1_14;
                break;
            } case "1_15_R1": {
                ret = v1_15;
                break;
            }
        }
        if(current == UNKNOWN||current!=ret) current = ret;
        return ret;
    }

}
package dev.perryplaysmc.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copy Right ©
 * This code is private
 * Owner: PerryPlaysMC
 * From: 11/3/19-2200
 * Package: dev.perryplaysmc.mines.configuration
 * Class: ConfigManager
 * <p>
 * Path: dev.perryplaysmc.mines.configuration.ConfigManager
 * <p>
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

@SuppressWarnings("all")
public class ConfigManager {


    public static void printToFile(List<String> is, File toPrint) {
        try {
            String fullmessage = "";
            if(is==null)return;
            for(int i = 0 ; i < is.size(); i++) {
                if((i+1) < (is.size()-1)) {
                    fullmessage += is.get(i+1).equalsIgnoreCase(";") ? is.get(i) : is.get(i) + "\n";
                }else {
                    fullmessage+=is.get(i)+"\n";
                }
            }
            fullmessage = fullmessage.trim();
            FileOutputStream outputStream = new FileOutputStream(toPrint);
            byte[] strToBytes = fullmessage.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

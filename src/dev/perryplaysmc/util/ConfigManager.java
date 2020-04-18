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

    public static void printToFile(InputStream is, File toPrint) {
        try {
            int i;
            String fullmessage = "";
            if(is==null)return;
            while((i = is.read())!=-1) {
                fullmessage+=(char)i;
            }
            FileOutputStream outputStream = new FileOutputStream(toPrint);
            byte[] strToBytes = fullmessage.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void printToFile(File is1, File toPrint) {
        try {
            int i;
            String fullmessage = "";
            InputStream is = new FileInputStream(is1);
            if(is==null)return;
            while((i = is.read())!=-1) {
                fullmessage+=(char)i;
            }
            FileOutputStream outputStream = new FileOutputStream(toPrint);
            byte[] strToBytes = fullmessage.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> readFile(File f) {
        try {
            if(!f.exists())
                return new ArrayList<>();
            FileInputStream i = new FileInputStream(f);
            int index;
            String line = "";
            List<String> lines = new ArrayList<>();
            while((index = i.read()) !=-1) {
                char c = (char) index;
                if(c=='\n') {
                    lines.add(line);
                    line="";
                    continue;
                }
                line+=c;
            }
            return lines;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<File> getFiles(File dir) {
        List<File> files = new ArrayList<>();
        File[] fls = dir.listFiles();
        if(fls == null) return files;
        for(File f : fls) {
            if(f.isDirectory()) {
                files.addAll(getFiles(f));
                continue;
            }
            if(!files.contains(f))
                files.add(f);
        }
        return files;
    }

    public static void deleteFiles(File dir) {
        if(dir.exists()) {
            List<File> files = getFiles(dir);
            for (File f : files) {
                if(f.exists()) {
                    if(f.isDirectory()) {
                        deleteFiles(f);
                        continue;
                    }
                    f.delete();
                }
            }
            dir.delete();
        }
    }


}

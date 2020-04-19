package dev.perryplaysmc;

import dev.perryplaysmc.commands.CommandEditItem;
import dev.perryplaysmc.commands.CommandGuiMaker;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copy Right ©
 * This code is private
 * Owner: PerryPlaysMC
 * From: 11/3/19-2200
 * Package: dev.perryplaysmc
 * Class: Main
 * <p>
 * Path: dev.perryplaysmc.Main
 * <p>
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

@SuppressWarnings("all")
public class Main extends JavaPlugin {

    public static Main getInstance() {
        return instance;
    }

    @Getter(AccessLevel.PUBLIC)
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("makegui").setExecutor(new CommandGuiMaker());
        getCommand("edititem").setExecutor(new CommandEditItem());
        Bukkit.getConsoleSender().sendMessage("§cGuiMaker enabled");
    }


}

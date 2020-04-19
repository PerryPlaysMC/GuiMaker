package dev.perryplaysmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Copy Right ©
 * This code is private
 * Owner: PerryPlaysMC
 * From: 11/3/19-2200
 * Package: dev.perryplaysmc.commands
 * Class: CommandEditItem
 * <p>
 * Path: dev.perryplaysmc.commands.CommandEditItem
 * <p>
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

@SuppressWarnings("all")
public class CommandEditItem implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender s, Command command, String cl, String[] args) {
        if(!(s instanceof CommandSender)) {
            s.sendMessage("§cPlayers only, sorry. (not sorry)");
            return true;
        }
        Player p = (Player) s;
        if(args.length < 2 || (!args[0].equalsIgnoreCase("name")&&!args[0].equalsIgnoreCase("lore"))) {
            s.sendMessage("§a§l§m-------§r§2[§e§lGuiMaker§2]§a§l§m-------");
            s.sendMessage("§a/§eitemedit §2name <Name>");
            s.sendMessage("§a/§eitemedit §2lore <Lore...>");
            return true;
        }
        if(args[0].equalsIgnoreCase("name")) {
            if(p.getItemInHand()==null||!(p.getItemInHand().getType().name().endsWith("AIR") && !p.getItemInHand().getType().name().endsWith("AIRS"))) {
                s.sendMessage("§cItem cannot be air. Please hold the item you would like to rename");
                return true;
            }
            String name = "";
            for(int i = 1; i < args.length; i++) {
                name+=args[i]+" ";
            }
            name = name.trim();
            ItemStack item = p.getItemInHand();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(im);
            p.setItemInHand(item);
            s.sendMessage("§aName set: §2" + name);
            return true;
        }
        if(args[0].equalsIgnoreCase("lore")) {
            if(p.getItemInHand()==null||!(p.getItemInHand().getType().name().endsWith("AIR") && !p.getItemInHand().getType().name().endsWith("AIRS"))) {
                s.sendMessage("§cItem cannot be air. Please hold the item you would like to relore");
                return true;
            }
            String lore = "";
            for(int i = 1; i < args.length; i++) {
                lore+=args[i]+" ";
            }
            lore = lore.trim().replace("\\n", "\n").replace("\n", "\n");
            ItemStack item = p.getItemInHand();
            ItemMeta im = item.getItemMeta();
            im.setLore(Collections.singletonList(lore));
            item.setItemMeta(im);
            p.setItemInHand(item);
            s.sendMessage("§aLore set: \n§a-§2" + lore.replace("\n", "\n§a-§2").replace("\\n", "\n§a-§2") );
            return true;
        }
        return true;
    }
}

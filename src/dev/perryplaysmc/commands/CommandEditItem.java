package dev.perryplaysmc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class CommandEditItem implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender s, Command command, String cl, String[] args) {
        if(!(s instanceof CommandSender)) {
            s.sendMessage("§cPlayers only, sorry. (not sorry)");
            return true;
        }
        Player p = (Player) s;
        if(!s.hasPermission("edititem.use")) {
            s.sendMessage("§cYou do not have permission for this command.");
            return true;
        }
        if(args.length < 2 || (!args[0].equalsIgnoreCase("name")&&!args[0].equalsIgnoreCase("lore"))) {
            s.sendMessage("§a§l§m-------§r§2[§e§lGuiMaker§2]§a§l§m-------");
            s.sendMessage("§a/§eitemedit §2name <Name>");
            s.sendMessage("§a/§eitemedit §2lore <Lore...>");
            return true;
        }
        if(args[0].equalsIgnoreCase("name")) {
            if(p.getItemInHand()==null||(p.getItemInHand().getType().name().endsWith("AIR") && !p.getItemInHand().getType().name().endsWith("AIRS"))) {
                s.sendMessage("§cItem cannot be air. Please hold the item you would like to rename");
                return true;
            }
            String name = "";
            for(int i = 1; i < args.length; i++) {
                name+=args[i]+" ";
            }
            name = ChatColor.translateAlternateColorCodes('&', name.trim());
            ItemStack item = p.getItemInHand();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            item.setItemMeta(im);
            p.setItemInHand(item);
            s.sendMessage("§aName set: §2" + name);
            return true;
        }
        if(args[0].equalsIgnoreCase("lore")) {
            if(p.getItemInHand()==null||(p.getItemInHand().getType().name().endsWith("AIR") && !p.getItemInHand().getType().name().endsWith("AIRS"))) {
                s.sendMessage("§cItem cannot be air. Please hold the item you would like to relore");
                return true;
            }
            String lore = "";
            for(int i = 1; i < args.length; i++) {
                lore+=args[i]+" ";
            }
            lore = ChatColor.translateAlternateColorCodes('&', lore).trim().replace("\\n", "\n").replace("\n", "\n");
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

    @Override
    public List<String> onTabComplete( CommandSender s,  Command command,  String cl, String[] args) {
        List<String> f = new ArrayList<>();
        List<String> t = Arrays.asList("name", "lore");
        if(args.length == 1 && s.hasPermission("edititem.use"))
            for(String s1 : t)
                if(s1.toLowerCase().startsWith(args[0].toLowerCase())) f.add(s1);

        return f;
    }
}

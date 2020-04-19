package dev.perryplaysmc.commands;

import dev.perryplaysmc.Main;
import dev.perryplaysmc.util.ColorType;
import dev.perryplaysmc.util.ConfigManager;
import dev.perryplaysmc.util.Version;
import dev.perryplaysmc.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copy Right ©
 * This code is private
 * Owner: PerryPlaysMC
 * From: 11/3/19-2200
 * Package: dev.perryplaysmc.commands
 * Class: CommandGuiMaker
 * <p>
 * Path: dev.perryplaysmc.commands.CommandGuiMaker
 * <p>
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

@SuppressWarnings("all")
public class CommandGuiMaker implements CommandExecutor {

    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender s, Command command, String cl, String[] args) {
        File f = plugin.getDataFolder();
        if(!f.isDirectory()) f.mkdirs();
        int index = f.listFiles() != null && f.listFiles().length > 0 ? f.listFiles().length + 1 : 1;
        if(!(s instanceof Player)) {
            s.sendMessage("You must be a player to use this command");
            return true;
        }
        if(!s.hasPermission("guimaker.create") && !s.isOp()) {
            s.sendMessage("§cYou do not have permission for this");
            return true;
        }
        Player p = (Player) s;
        Block b = p.getTargetBlock(null, 5);
        if(b == null) {
            p.sendMessage("§cPlease look at a block (With a storage inventory)");
            return true;
        }
        if(!(b.getState() instanceof InventoryHolder)) {
            p.sendMessage("§cThats not an inventoryholder thats a " + b.getState().getClass().getSimpleName());
            return true;
        }
        String type = args.length == 1 ? args[0] : "default";
        InventoryHolder c = (InventoryHolder) b.getState();
        List<String> contents = new ArrayList<>();
        contents.add("public " + (!type.equalsIgnoreCase("-p") ? "Inventory" : "GuiUtil") + " createInventory_" + index + "() {");
        HashMap<ItemStack, Integer> slots = new HashMap<>();


        if(type == "default") {
            contents.add("  Inventory inv = Bukkit.createInventory(null, " + c.getInventory().getSize() + ", \"Chest\");");
            int itemIndex = 0;
            for(int i = 0; i < c.getInventory().getContents().length; i++) {
                ItemStack item = c.getInventory().getItem(i);
                if(item != null) {
                    if(!slots.containsKey(item)) {
                        String iname = item.getType().name();
                        if(Version.isCurrentHigher(Version.v1_12)&&iname.contains("STAINED") || iname.contains("WOOL") && Version.isCurrentHigher(Version.v1_12)) {
                            ColorType ct = ColorType.byData(item.getData().getData());
                            if(!iname.startsWith(ct.name() + "_")) {
                                contents.add("  ItemStack item" + itemIndex + " = new ItemStack(Material." + ct.name() + "_" + iname + ", "
                                        + item.getAmount() + ", (short)" + ct.getData() + ");");
                                iname = ct.name() + "_" + iname;
                            } else
                                contents.add("  ItemStack item" + itemIndex + " = new ItemStack(Material." +
                                        (isLegacy(iname) ? "LEGACY_" + iname : iname) + "," + item.getAmount() + ");");
                        }else
                            contents.add("  ItemStack item" + itemIndex + " = new ItemStack(Material." + item.getType().name() + ", "
                                    + item.getAmount() + ", (short)" + item.getData().getData() + ");");
                        if(item.getDurability() > 0)
                            contents.add("    item" + itemIndex + ".setDurability((short)" + item.getDurability() + ");");
                        if(item.hasItemMeta()) {
                            ItemMeta im = item.getItemMeta();
                            contents.add("    ItemMeta im" + itemIndex + " = item" + itemIndex + ".getItemMeta();");
                            if(im.hasDisplayName())
                                contents.add("      im" + itemIndex + ".setDisplayName(\"" + im.getDisplayName() + "\");");
                            if(im.hasLore()) {
                                String lore = "";
                                for(String l : im.getLore()) {
                                    lore += "\"" + l + "\",";
                                }
                                contents.add("      im" + itemIndex + ".setLore(Arrays.asList(" + lore.substring(0, lore.length() - 1) + "));");
                            }
                            if(im.hasEnchants()) {
                                for(Map.Entry<Enchantment, Integer> e : im.getEnchants().entrySet()) {
                                    contents.add("      im" + itemIndex + ".addEnchant(Enchantment." + e.getKey().getName().toUpperCase() + ", " + e.getValue().intValue() + ", true);");
                                }
                            }
                            if(im.hasCustomModelData()) {
                                contents.add("      im" + itemIndex + ".setCustomModelData(" + im.getCustomModelData() + ");");
                            }
                            if(im.getItemFlags().size() > 0) {
                                for(ItemFlag flag : im.getItemFlags()) {
                                    contents.add("      im" + itemIndex + ".addItemFlags(ItemFlag." + flag.name() + ");");
                                }
                            }
                            contents.add("    item" + itemIndex + ".setItemMeta(im" + itemIndex + ");");
                        }
                        contents.add(";");
                        slots.put(item, itemIndex);
                        itemIndex++;
                    }
                    // contents.add("   inv.setItem(" + itemIndex + ", item" + slots.get(item) + ");");
                }
            }
            HashMap<ItemStack, List<Integer>> ints = new HashMap<>();
            for(int i = 0; i < c.getInventory().getContents().length; i++) {
                ItemStack item = c.getInventory().getItem(i);
                if(item != null) {
                    List<Integer> integers = ints.containsKey(item) ? ints.get(item) : new ArrayList<>();

                    boolean otherHas = false;
                    for(List<Integer> ig : ints.values())
                        if(ig.contains(i)) otherHas = true;
                    if(!integers.contains(i) && !otherHas)
                        integers.add(i);
                    ints.put(item, integers);
                    //contents.add("   inv.setItem(" + itemIndex + ", item" + slots.get(item) + ".buildItem());");
                }
            }
            List<String> toAdd = new ArrayList<>();
            for(Map.Entry<ItemStack, Integer> e : slots.entrySet()) {
                String integers = "";
                List<Integer> check = new ArrayList<>();
                for(Integer i : ints.get(e.getKey())) {
                    if(!integers.contains(i + ",") && !integers.contains("," + i) && !check.contains(i)) {
                        integers += i + ",";
                        check.add(i);
                    }
                }
                integers = integers.endsWith(",") ? integers.substring(0, integers.length() - 1) : integers;
                if(!integers.contains(",")) {
                    toAdd.add("  inv.setItem(" + integers + ", item" + e.getValue() + ");");
                } else{
                    contents.add("  for(int i : Arrays.asList(" + integers + ")) {");
                    contents.add("    inv.setItem(i, item" + e.getValue() + ");");
                    contents.add("  }");
                }
            }
            if(toAdd.size() > 0)
                contents.addAll(toAdd);
        }
        else if(plugin.getConfig().getConfigurationSection("formats." + type) != null) {
            String title = b instanceof Nameable ? ((Nameable) b).getCustomName() : "Chest";
            contents.add(" Inventory inv = Bukkit.createInventory(null, " + c.getInventory().getSize() + ", \"" + title + "\");");
            ConfigurationSection sec = plugin.getConfig().getConfigurationSection("formats." + type);
            int itemIndex = 0;
            for(int i = 0; i < c.getInventory().getContents().length; i++) {
                ItemStack item = c.getInventory().getItem(i);
                if(item != null) {
                    if(!slots.containsKey(item)) {
                        String iname = item.getType().name();
                        String params = sec.getString("creation.parameters");
                        contents.add("  " + sec.getString("itemclassName") + " item" + itemIndex + " = "
                                + (sec.getString("creation.name").contains("constructor") ?
                                sec.getString("creation.name").replace("constructor", "new " + sec.getString("itemclassName")) :
                                sec.getString("itemclassName") + sec.getString("creation.name")) +
                                "(" + toString(getFromParametersString(item, params)) + ");");
                        if(!params.contains("Amount") && !sec.getString("setAmount.parameters").isEmpty())
                            contents.add("    item" + itemIndex + "." + sec.getString("setAmount.name") +
                                    "(" + toString(getFromParametersString(item, sec.getString("setAmount.parameters"))) + ");");
                        if(item.hasItemMeta()) {
                            ItemMeta im = item.getItemMeta();
                            if(im.hasDisplayName() && !sec.getString("setDisplayName.name").isEmpty())
                                contents.add("    item" + itemIndex + "." + sec.getString("setDisplayName.name") +
                                        "(" + toString(getFromParametersString(item, sec.getString("setDisplayName.parameters"))) + ");");
                            if(im.hasLore() && !sec.getString("setLore.name").isEmpty())
                                contents.add("    item" + itemIndex + "." + sec.getString("setLore.name") +
                                        "(" + toString(getFromParametersString(item, sec.getString("setLore.parameters"))) + ");");
                            if(im.hasCustomModelData() && !sec.getString("setCustomModelData.name").isEmpty())
                                contents.add("    item" + itemIndex + "." + sec.getString("setCustomModelData.name") +
                                        "(" + toString(getFromParametersString(item, sec.getString("setLore.parameters"))) + ");");
                            if(im.hasEnchants() && !sec.getString("addEnchants.name").isEmpty())
                                for(Map.Entry<Enchantment, Integer> e : im.getEnchants().entrySet()) {
                                    contents.add("    item" + itemIndex + "." + sec.getString("addEnchants.name") +
                                            "(" + sec.getString("addEnchants.parameters").replace("enchant.getEnchant()",
                                            "Enchantment." + e.getKey().getName())
                                            .replace("enchant.getValue()", e.getValue() + "") + ");");
                                }
                            if(im.getItemFlags().size() > 0 && !sec.getString("addItemFlag.name").isEmpty()) {
                                for(ItemFlag flag : im.getItemFlags()) {
                                    contents.add("    item" + itemIndex + "." + sec.getString("addEnchants.name") +
                                            "(" + sec.getString("addEnchants.parameters").replace("flag",
                                            "ItemFlag." + flag.name() + "") + ");");
                                }
                            }
                        }
                        if(i < (c.getInventory().getSize() - 1))
                            contents.add(" ");
                        slots.put(item, itemIndex);
                        itemIndex++;
                    }
                }
            }

            HashMap<ItemStack, List<Integer>> ints = new HashMap<>();
            for(int i = 0; i < c.getInventory().getContents().length; i++) {
                ItemStack item = c.getInventory().getItem(i);
                if(item != null) {
                    List<Integer> integers = ints.containsKey(item) ? ints.get(item) : new ArrayList<>();

                    boolean otherHas = false;
                    for(List<Integer> ig : ints.values())
                        if(ig.contains(i)) otherHas = true;
                    if(!integers.contains(i) && !otherHas)
                        integers.add(i);
                    ints.put(item, integers);
                    //contents.add("   inv.setItem(" + itemIndex + ", item" + slots.get(item) + ".buildItem());");
                }
            }
            List<String> toAdd = new ArrayList<>();
            for(Map.Entry<ItemStack, Integer> e : slots.entrySet()) {
                String integers = "";
                List<Integer> check = new ArrayList<>();
                for(Integer i : ints.get(e.getKey())) {
                    if(!integers.contains(i + ",") && !integers.contains("," + i) && !check.contains(i)) {
                        integers += i + ",";
                        check.add(i);
                    }
                }
                integers = integers.endsWith(",") ? integers.substring(0, integers.length() - 1) : integers;
                if(!integers.contains(",")) {
                    if(!sec.getString("build.name").isEmpty())
                        toAdd.add("  inv.setItem(" + integers + ", item" + e.getValue() + "." + sec.getString("build.name") +
                                "(" + toString(getFromParametersString(e.getKey(), sec.getString("build.parameters"))) + "));");
                    else toAdd.add("  inv.setItem(" + integers + ", item" + e.getValue() + ");");
                } else{
                    contents.add("  for(int i : Arrays.asList(" + integers + ")) {");

                    if(!sec.getString("build.name").isEmpty())
                        contents.add("    inv.setItem(i, item" + e.getValue() + "." + sec.getString("build.name") +
                                "(" + toString(getFromParametersString(e.getKey(), sec.getString("build.parameters"))) + "));");
                    else contents.add("    inv.setItem(" + integers + ", item" + e.getValue() + ");");
                    contents.add("  }");
                }
            }
            contents.addAll(toAdd);
        }
        else{
            s.sendMessage("§cInvalid format");
            return true;
        }
        contents.add("  return inv;");
        contents.add("}");
        ConfigManager.printToFile(contents, new File(plugin.getDataFolder(), "inventory-" + type + "-" + index + ".java"));
        s.sendMessage("§cCreated Inventory file! (filename: inventory-" + type + "-" + index + ".java)");
        return true;
    }

    String toString(String[] strs) {
        String ret = "";
        for(String s : strs) {
            ret += s + ",";
        }
        return !ret.isEmpty() ? ret.substring(0, ret.length() - 1) : ret;
    }

    String[] getFromParametersString(ItemStack item, String str) {
        String[] objs;
        ItemMeta im = item.getItemMeta();
        str = str.replace(" ", "");
        int i = 0;
        if(str.contains(",")) {
            objs = new String[str.split("\\,").length];
            for(String s : str.split(",")) {
                if(s.contains(".")) {
                    if(s.toLowerCase().startsWith("item.set") || s.toLowerCase().startsWith("itemmeta.set")) {
                        System.out.println("Method cannot be set method!");
                        continue;
                    }
                    if(s.split("\\.")[0].equalsIgnoreCase("item")) {
                        try {
                            if(s.split("\\.")[1].split("\\(\\)")[0].equalsIgnoreCase("getData")) {

                                objs[i] = "(short)" + item.getData().getData();
                                i++;
                                continue;
                            }
                            if(s.split("\\.")[1].split("\\(\\)")[0].equalsIgnoreCase("getMaterialData")) {

                                objs[i] = "new MaterialData(Material." + item.getData().getItemType().name() + ", (short)" + item.getData().getData() + ")";
                                i++;
                                continue;
                            }
                            Method m = ItemStack.class.getMethod(s.split("\\.")[1].split("\\(\\)")[0]);
                            Object o = m.invoke(item);
                            if(o instanceof Material) {
                                objs[i] = "Material." + o.toString();
                            } else if(o instanceof Short) {
                                objs[i] = "(short)" + o.toString();
                            } else if(o instanceof Byte) {
                                objs[i] = "(byte)" + o.toString();
                            } else if(o instanceof Integer) {
                                objs[i] = o.toString();
                            } else if(o instanceof String) {
                                objs[i] = "\"" + o.toString() + "\"";
                            } else if(o instanceof List) {
                                String x = "";
                                for(Object f : (List) o) {
                                    if(f instanceof String) {
                                        x += "\"" + f + "\",";
                                    }
                                }
                                if(x.length() > 0)
                                    objs[i] = x.substring(0, x.length() - 1);
                                else objs[i] = o.toString();
                            } else{
                                objs[i] = o.toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if(s.split("\\.")[0].equalsIgnoreCase("itemmeta")) {
                        try {
                            Method m = ItemMeta.class.getMethod(s.split("\\.")[1].split("\\(\\)")[0]);
                            Object o = m.invoke(item.getItemMeta());
                            if(o instanceof Material) {
                                objs[i] = "Material." + o.toString();
                            } else if(o instanceof Short) {
                                objs[i] = "(short)" + o.toString();
                            } else if(o instanceof Byte) {
                                objs[i] = "(byte)" + o.toString();
                            } else if(o instanceof Integer) {
                                objs[i] = o.toString();
                            } else if(o instanceof String) {
                                objs[i] = "\"" + o.toString() + "\"";
                            } else if(o instanceof List) {
                                String x = "";
                                for(Object f : (List) o) {
                                    if(f instanceof String) {
                                        x += "\"" + f + "\",";
                                    }
                                }
                                if(x.length() > 0)
                                    objs[i] = x.substring(0, x.length() - 1);
                                else objs[i] = o.toString();
                            } else{
                                objs[i] = o.toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else{
                    System.out.println("Must contain a period!!!!!!!!!!! E.G. item.getAmount() ");
                }
                i++;
            }
        } else{
            objs = new String[str.length() > 0 ? 1 : 0];
            i = 0;
            if(objs.length > 0)
                if(str.contains(".")) {
                    if(str.split("\\.")[0].equalsIgnoreCase("item")) {
                        if(str.split("\\.")[1].split("\\(\\)")[0].equalsIgnoreCase("getData")) {

                            objs[i] = "(short)" + item.getData().getData();
                            i++;
                        }
                        else if(str.split("\\.")[1].split("\\(\\)")[0].equalsIgnoreCase("getMaterialData")) {

                            objs[i] = "new MaterialData(Material." + item.getData().getItemType().name() + ", (short)" + item.getData().getData() + ")";
                            i++;
                        }else
                            try {

                                Method m = ItemStack.class.getMethod(str.split("\\.")[1].split("\\(\\)")[0]);
                                Object o = m.invoke(item);
                                if(o instanceof Material) {
                                    objs[i] = "Material." + o.toString();
                                } else if(o instanceof Short) {
                                    objs[i] = "(short)" + o.toString();
                                } else if(o instanceof Byte) {
                                    objs[i] = "(byte)" + o.toString();
                                } else if(o instanceof Integer) {
                                    objs[i] = o.toString();
                                } else  if(o instanceof String) {
                                    objs[i] = "\"" + o.toString() + "\"";
                                } else if(o instanceof List) {
                                    String x = "";
                                    for(Object f : (List) o) {
                                        if(f instanceof String) {
                                            x += "\"" + f + "\",";
                                        }
                                    }
                                    if(x.length() > 0)
                                        objs[i] = x.substring(0, x.length() - 1);
                                    else objs[i] = o.toString();
                                } else {
                                    objs[i] = o.toString();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else if(str.split("\\.")[0].equalsIgnoreCase("itemmeta")) {
                        try {
                            Method m = ItemMeta.class.getMethod(str.split("\\.")[1].split("\\(\\)")[0]);
                            Object o = m.invoke(item.getItemMeta());
                            if(o instanceof Material) {
                                objs[i] = "Material." + o.toString();
                            } else if(o instanceof Short) {
                                objs[i] = "(short)" + o.toString();
                            } else if(o instanceof Byte) {
                                objs[i] = "(byte)" + o.toString();
                            } else if(o instanceof Integer) {
                                objs[i] = o.toString();
                            } else  if(o instanceof String) {
                                objs[i] = "\"" + o.toString() + "\"";
                            } else if(o instanceof List) {
                                String x = "";
                                for(Object f : (List) o) {
                                    if(f instanceof String) {
                                        x += "\"" + f + "\",";
                                    }
                                }
                                if(x.length() > 0)
                                    objs[i] = x.substring(0, x.length() - 1);
                                else objs[i] = o.toString();
                            } else {
                                objs[i] = o.toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
        return objs;
    }

    boolean isLegacy(String name) {
        name = name.toUpperCase();
        if(Version.isCurrentLower(Version.v1_13)) return false;
        return XMaterial.isLegacy(name);
    }

    public static Material convertMaterial(String m) {
        if(Version.isCurrentHigher(Version.v1_12)) {
            if(m == null || m.isEmpty()) return null;
            if(getMaterial("LEGACY_" + m.toUpperCase()) != null)
                return getMaterial("LEGACY_" + m.toUpperCase());
            if(getMaterial(m.toUpperCase()) != null)
                return getMaterial(m.toUpperCase());
            return getMaterial(m.toUpperCase());
        }
        if(m.toUpperCase().startsWith("LEGACY_"))
            return Material.getMaterial(m.toUpperCase().replace("LEGACY_", "").replace("SHOVEL", "SPADE"));
        return Material.getMaterial(m.toUpperCase());
    }

    public static Material getMaterialFromLine(String line) {
        line = line.toUpperCase();
        Material m = Material.getMaterial("LEGACY_" + line);
        if(line.contains("SIGN") && (m = Material.getMaterial("OAK_" + line)) != null) return m;
        if((m = Material.getMaterial("LEGACY_" + line)) != null) return m;
        if((m = Material.getMaterial(line)) != null) return m;
        if((m = Material.getMaterial(line.replace("LEGACY_", ""))) != null) return m;
        if((m = Material.getMaterial(line)) != null) return m;
        for(Material m1 : Material.values()) {
            if(line.equalsIgnoreCase(m1.name()) ||
                    ("LEGACY_" + line).equalsIgnoreCase(m1.name()) ||
                    line.replace("LEGACY_", "").equalsIgnoreCase(m1.name()) ||
                    line.equalsIgnoreCase(m1.name().replace("_", "")))
                return m1;
        }
        return null;
    }


    public static Material convertMaterial(Material m) {
        if(Version.isCurrentHigher(Version.v1_12)) {
            if(Material.valueOf("LEGACY_" + m.name()) != null)
                return Material.valueOf("LEGACY_" + m.name().replace("SPADE", "SHOVEL"));
            return m;
        }
        if(m.name().startsWith("LEGACY_"))
            return Material.valueOf(m.name().replace("LEGACY_", "").replace("SHOVEL", "SPADE"));
        return m;
    }

    static Material getMaterial(String name) {
        if(name.startsWith("LEGACY_"))
            return Material.getMaterial(name);
        else
            return Material.getMaterial(name);
    }
}

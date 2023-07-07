package org.thanhmagics.gachapon.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.MoneyType;

import java.lang.reflect.Field;
import java.util.*;

import static org.thanhmagics.gachapon.utils.Utils.applyColor;

public class ItemBuilder {

    private ItemStack stack;

    private ItemMeta meta;

    private List<String> lore = new ArrayList<>();

    public ItemBuilder(Material m) {
        this.stack = new ItemStack(m);
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(String skullValue) {
        this.stack = skullWithValue(skullValue);
        this.meta = stack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.stack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    public ItemBuilder clone() {
        return new ItemBuilder(build().clone());
    }

    public ItemBuilder displayName(String str) {
        if (str != null)
            meta.setDisplayName((applyColor(str)));
        return this;
    }

    public ItemBuilder addDisplayName(String str) {
        if (str != null) {
            if (meta.getDisplayName().length() == 0) {
                meta.setDisplayName(stack.getI18NDisplayName() + Utils.applyColor(str));
                return this;
            }
            meta.setDisplayName(meta.getDisplayName() + Utils.applyColor(str));
        }
        return this;
    }

    public ItemBuilder lore(String str) {
        if (str != null)
            lore.add((applyColor(str)));
        return this;
    }

    public ItemBuilder lore(List<String> strs) {
        for (String s : strs)
            lore(s);
        return this;
    }

    public ItemBuilder enchant(boolean b) {
        if (b) {
            meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                meta.removeEnchant(enchantment);
            }
        }
        return this;
    }

    public ItemStack build() {
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack skullWithValue(String value) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", value));
        meta.setPlayerProfile(profile);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getItemStackByConfig(FileConfiguration config,String path,LoreReplacer loreReplacer) {
        ItemBuilder itemBuilder;
        if (config.getString(path + ".material").equalsIgnoreCase("AIR"))
            return null;
        if (config.getString(path + ".material").equalsIgnoreCase("PLAYER_HEAD") &&
        config.contains(path + ".skull_value")) {
            itemBuilder = new ItemBuilder(config.getString(path + ".skull_value"));
        } else {
            itemBuilder = new ItemBuilder(Material.valueOf(config.getString(path + ".material")));
        }
        if (config.contains(path + ".display_name"))
            if (loreReplacer != null)
                itemBuilder.displayName(config.getString(path + ".display_name").replace("{pack_id}",String.valueOf(loreReplacer.id)));
            else
                itemBuilder.displayName(config.getString(path + ".display_name"));
        if (config.contains(path + ".lore")) {
            //itemBuilder.lore(loreReplacer.convert(config.getStringList(path + ".lore")));
            if (loreReplacer != null) {
                List<String> price = new ArrayList<>();
                if (config.getStringList(path + ".lore").contains("{pack_price}")) {
                    for (String s : loreReplacer.price.keySet()) {
                        if (loreReplacer.price.get(s).equals(MoneyType.ITEM)) {
                            ItemStack stack1 = Utils.getItemStackFromString(s);
                            price.add((Utils.inventoryContainItem(loreReplacer.player.getInventory(),stack1) ? "&a✔ " : "&c✘ ") +
                                    Utils.unColor(config.getString("lore_replacer.price.item").replace("{item}", (stack1.getItemMeta().getDisplayName().length() == 0 ?
                                                    "§f" + stack1.getI18NDisplayName() : stack1.getItemMeta().getDisplayName()))
                                            .replace("{amount}", String.valueOf(Utils.getItemStackFromString(s).getAmount()))));
                        } else if (loreReplacer.price.get(s).equals(MoneyType.PLAYERPOINT)) {
                            price.add((Gachapon.getInstance().getPlayerPoints().getAPI().look(loreReplacer.player.getUniqueId()) >= Integer.valueOf(s) ? "&a✔ " : "&c✘ ") +
                                    Utils.unColor(config.getString("lore_replacer.price.player_point").replace("{player_point}", s)));
                        } else {
                            price.add((Gachapon.getInstance().getEssentials().getUser(loreReplacer.player.getUniqueId()).getMoney().intValue() >= Integer.valueOf(s) ? "&a✔ " : "&c✘ ") +
                                    Utils.unColor(config.getString("lore_replacer.price.money").replace("{money}", s)));
                        }
                    }
                }
                for (String s : config.getStringList(path + ".lore")) {
                    if (s.contains("{buyrequires_player}"))
                        s = s.replace("{buyrequires_player}",loreReplacer.min);
                    if (s.contains("{buyrequires}"))
                        s = s.replace("{buyrequires}", loreReplacer.max);
                    if (s.contains("{pack_dailybuy}"))
                        s = s.replace("{pack_dailybuy}",String.valueOf(loreReplacer.dailyBuy));
                    if (s.contains("{pack_price}")) {
                        itemBuilder.lore(price);
                    } else {
                        itemBuilder.lore(s);
                    }
                }
            } else {
                for (String s : config.getStringList(path + ".lore"))
                    itemBuilder.lore(s);
            }
        }
        if (config.contains(path + ".enchant"))
            itemBuilder.enchant(config.getBoolean(path + ".enchant"));
        return itemBuilder.build();
    }

    public static class LoreReplacer {
        public int id = 0;

        public String min,max;

        public Player player;

        public Integer dailyBuy = 0;

        public Map<String, MoneyType> price = new HashMap<>();

        public LoreReplacer setPlayer(Player player1 ) {
            this.player = player1;
            return this;
        }

        public LoreReplacer setDailYBuy(int i) {
            this.dailyBuy = i;return this;
        }

        public LoreReplacer setId(int i) {
            this.id = i; return this;
        }

        public LoreReplacer setPrice(Map<String,MoneyType> mao) {
            this.price = mao; return this;
        }
    }

//    public static class LoreReplacer {
//
//        private List<Item> list = new LinkedList<>();
//
//        public LoreReplacer(Item... items) {
//            list.addAll(List.of(items));
//        }
//
//        public String convert(String str) {
//            String rs = str;
//            for (Item item : list) {
//                if (str.contains(item.to)) {
//                    rs = str.replace(item.to, item.from);
//                }
//            }
//            return rs;
//        }
//
//        public List<String> convert(List<String> str) {
//            List<String> rs = new LinkedList<>();
//            for (String string : str)
//                rs.add(convert(string));
//            return rs;
//        }
//
//        public static class Item {
//            String to,from;
//
//            List<String> froms = null;
//
//            public Item(String to, String from) {
//                this.to = to;
//                this.from = from;
//            }
//
//            public Item(String to,List<String> forms) {
//                this.to = to;
//                this.froms = forms;
//            }
//
//        }
//
//    }

}
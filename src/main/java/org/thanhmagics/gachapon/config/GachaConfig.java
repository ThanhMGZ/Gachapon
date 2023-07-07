package org.thanhmagics.gachapon.config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.gui.ConfirmGUI;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.*;

public class GachaConfig {

    private String name;

    private String permission = null;

    private boolean enable = false;

    private int dailyLimited = 69;

    private Map<Integer,GachaPack> gachaPackMap = new HashMap<>();

    private Map<Integer,Integer> buyRequires = new HashMap<>();


    public static List<Player> inAnimation = new ArrayList<>();

    public static Map<Player, ItemStack> inAnimationReward = new HashMap<>();
    private int i = 0;

    public GachaConfig(String name) {
        buyRequires.put(0,0);
        this.name = name;
        Gachapon.getInstance().getGachaConfigMap().put(name,this);
    }

    public void addNewGachaPack() {
        GachaPack gachaPack = new GachaPack(this);
        gachaPackMap.put(i,gachaPack);
        if (i != 0)
            buyRequires.put(i,10);
        this.i++;
        reloadGachaPack();
        for (Player player : Bukkit.getOnlinePlayers())
            Gachapon.getInstance().getPlayerData().get(player.getUniqueId()).reloadDailyBuy();
    }

    public GachaConfig(String name,String data) {
        String[] configString = data.split("}");
        buyRequires.put(0,0);
        try {
            this.name = name;
            this.permission = configString[0].split("\\{")[1];
            this.enable = Boolean.parseBoolean(configString[1].split("\\{")[1]);
            this.dailyLimited = Integer.parseInt(configString[2].split("\\{")[1]);
            Gachapon.getInstance().getGachaConfigMap().put(name,this);
            if (configString[3].length() > 2) {
                for (int i = 0; i < configString[3].split("\\{")[1].split(";").length; i++) {
                    String s = configString[3].split("\\{")[1].split(";")[i];
                    if (s.split(",")[0].equalsIgnoreCase("0"))
                        continue;
                    buyRequires.put(i, Integer.valueOf(s.split(",")[0]));
                    buyRequires.put(i, Integer.valueOf(s.split(",")[1]));
                }
            }
            if (configString[4].length() > 2) {
                for (int i = 0; i < configString[4].split("\\{")[1].split(";").length; i++) {
                    GachaPack gachaPack = GachaPack.fromString(this, configString[4].split("\\{")[1].split(";")[i]);
                    this.gachaPackMap.put(this.i, gachaPack);
                    this.i++;
                }
            }
        } catch (Exception e) {e.printStackTrace();}
        reloadGachaPack();
    }


    public String toStringg() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(permission).append("}");
        stringBuilder.append("{").append(enable).append("}");
        stringBuilder.append("{").append(dailyLimited).append("}");
        stringBuilder.append("{");
        for (Integer integer : buyRequires.keySet()) {
            stringBuilder.append(integer).append(",").append(buyRequires.get(integer)).append(";");
        }
        if (buyRequires.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}{");
        for (GachaPack gachaPack : gachaPackMap.values()) {
            stringBuilder.append(gachaPack.toStringg()).append(";");
        }
        if (gachaPackMap.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public int getDailyLimited() {
        return dailyLimited;
    }

    public void setDailyLimited(int dailyLimited) {
        this.dailyLimited = dailyLimited;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Map<Integer, Integer> getBuyRequires() {
        return buyRequires;
    }

    public void reloadGachaPack() {
        Map<Integer,GachaPack> gp = new HashMap<>();
        Map<Integer,Integer> br = new HashMap<>();
        int i = 0;
        for (Integer integer : gachaPackMap.keySet()) {
            if (gachaPackMap.get(integer)!=null) {
                br.put(i,buyRequires.get(integer));
                gp.put(i, gachaPackMap.get(integer));
                i++;
            }
        }
        this.buyRequires = br;
        this.gachaPackMap = gp;
        this.i = gp.size();
    }

    public String getPermission() {
        return permission;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<Integer, GachaPack> getGachaPackMap() {
        return gachaPackMap;
    }

    public void delete() {
        Gachapon.getInstance().getGachaConfigMap().remove(name);
        Gachapon.getInstance().getGcConfigFile().getConfig().set("Items." + name,null);
        Gachapon.getInstance().getGcConfigFile().save();
    }

    public void save() {
        Gachapon.getInstance().getGcConfigFile().getConfig().set("Items." + name, toStringg());
        Gachapon.getInstance().getGcConfigFile().save();
    }

    public static void open(PlayerData playerData,GachaPack pack) {
        if (Utils.isInvFull(playerData.getPlayer().getInventory())) {
            playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.InventoryFull"));
            return;
        }
        ConfirmGUI.open(playerData,true,pack);
    }
}

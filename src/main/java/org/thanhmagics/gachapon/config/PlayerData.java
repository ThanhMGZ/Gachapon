package org.thanhmagics.gachapon.config;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.gui.GachaGUI;
import org.thanhmagics.gachapon.utils.Storage;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.*;

public class PlayerData {

    private UUID uuid;

    private Map<GachaPack,Integer> dailyBuy = new HashMap<>();

    private Inventory inventory;

    private GachaGUI gachaGUI;

    private GachaPack pack;

    private Player player;

    private Storage<GachaConfig,GachaPack,Integer> pack_da_mua = new Storage<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        Gachapon.getInstance().getPlayerData().put(uuid,this);
    }

    public void reloadDailyBuy() {
        for (GachaConfig gachaConfig : Gachapon.getInstance().getGachaConfigMap().values()) {
            for (GachaPack gachaPack : gachaConfig.getGachaPackMap().values()) {
                if (!dailyBuy.containsKey(gachaPack)) {
                    dailyBuy.put(gachaPack,0);
                }
            }
        }
    }

    public Map<GachaPack, Integer> getDailyBuy() {
        return dailyBuy;
    }

    public PlayerData sendMessage(String... s) {
        //Bukkit.getPlayer(uuid).sendMessage(Utils.applyColor(s));
        for (String str : s) {
            Bukkit.getPlayer(uuid
            ).sendMessage(Utils.applyColor(str));
        }
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GachaPack getPack() {
        return pack;
    }

    public void setPack(GachaPack pack) {
        this.pack = pack;
    }

    public Storage<GachaConfig, GachaPack, Integer> getPack_da_mua() {
        return pack_da_mua;
    }

    public void setPack_da_mua(Storage<GachaConfig, GachaPack, Integer> pack_da_mua) {
        this.pack_da_mua = pack_da_mua;
    }

    public String pack_da_mua_toString() {
        return PlayerData.pack_da_mua_toString(pack_da_mua);
    }

    public static String pack_da_mua_toString(Storage<GachaConfig,GachaPack,Integer> storage) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < storage.size(); i++) {
            GachaConfig gachaConfig = storage.key1List().get(i);
            GachaPack gachaPack = storage.key2List().get(i);
            int bought = storage.get(gachaConfig,gachaPack);
            stringBuilder.append(gachaConfig.getName()).append(":").append(Utils.getKeyByValue(gachaConfig.getGachaPackMap(), gachaPack)).append(";").append(bought).append(",");
        }
        if (storage.size() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static Storage<GachaConfig,GachaPack,Integer> pack_da_mua_fromString(String s) {
        Storage<GachaConfig,GachaPack,Integer> rs = new Storage<>() ;
        try {
            if (s.contains(",")) {
                for (String v1 : s.split(",")) {
                    a(v1, rs);
                }
            } else {
                a(s, rs);
            }
        }catch (Exception e) {e.printStackTrace();}
        return rs;
    }

    private static void a(String v, Storage<GachaConfig, GachaPack, Integer> rs) {
        GachaConfig config = Gachapon.getInstance().getGachaConfigMap().get(v.split(":")[0]);
        GachaPack pack = config.getGachaPackMap().get(Integer.valueOf(v.split(":")[1].split(";")[0]));
        rs.put(config, pack, Integer.valueOf(v.split(":")[1].split(";")[1]));
    }

    public void clearDailyBuy() {
        dailyBuy.clear();
    }

    public GachaGUI getGachaGUI() {
        return gachaGUI;
    }

    public void setGachaGUI(GachaGUI gachaGUI) {
        this.gachaGUI = gachaGUI;
    }

    public void openInventory(Inventory inventory) {
        player.sendMessage(inventory.getType().getDefaultTitle());
    }

    public PlayerData sendMessage(TextComponent... textComponents) {
        Bukkit.getPlayer(uuid).sendMessage(textComponents);
        return this;
    }

    public PlayerData sendMessage(LinkedList<TextComponent> textComponents) {
        for (TextComponent textComponent : textComponents)
            sendMessage(textComponent);
        return this;
    }

    public PlayerData sendMessage(List<String> strings) {
        for (String s : strings)
            sendMessage(s);
        return this;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDailyBuyString() {
        Gachapon.getInstance().checkDate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utils.getDate() + ";");
        for (GachaPack gachaPack : dailyBuy.keySet()) {
            stringBuilder.append("{").append(gachaPack.gachaConfig.getName() + "=" + Utils.getKeyByValue( gachaPack.gachaConfig.getGachaPackMap(),gachaPack) + "=" + dailyBuy.get(gachaPack)).append("}");
        }
        return stringBuilder.toString();
    }
}

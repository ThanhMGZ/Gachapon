package org.thanhmagics.gachapon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.MoneyType;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.gui.setup.AddItemGUI;
import org.thanhmagics.gachapon.gui.setup.BoxEditGUI;
import org.thanhmagics.gachapon.gui.setup.SetupGUI;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChatEvent implements Listener {

    private static Map<Player,String> editingCode = new HashMap<>();

    public static void addPlayer(Player player,String code) {
        if (!editingCode.containsKey(player)) {
            editingCode.put(player,code);
        }
    }
    public static void remove(Player player) {
        editingCode.remove(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        PlayerData playerData = Gachapon.getInstance().getPlayerData().get(player.getUniqueId());
        String msg = event.getMessage();
        if (GachaConfig.inAnimation.contains(player))
            event.setCancelled(true);
        if (Utils.isKTĐB(msg)) {
            playerData.sendMessage("&cHủy Hoạt Động Vì Chuổi Có Chứa KTĐB!");
            return;
        }
        if (editingCode.containsKey(player)) {
            event.setCancelled(true);
            int code = Integer.parseInt(editingCode.get(player).split(";")[0]);
            String string = editingCode.get(player).split(";")[1];
            if (msg.equalsIgnoreCase("cancel")) {
                if (code == 1) {
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string);
                    new SetupGUI(gachaConfig).build(playerData, true);
                    remove(player);
                    playerData.sendMessage("&cCancel Success!");
                } else if (code == 2) {
                    remove(player);
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[3]))).build(playerData, true, true,
                            new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.valueOf(string.split(":")[1]))
                            , null, Boolean.parseBoolean(string.split(":")[2]));
                    playerData.sendMessage("&cCancel Success!");
                } else if (code == 3 || code == 4) {
                    remove(player, playerData, string);
                } else if (code == 5) {
                    remove(player, playerData, string);
                } else if (code == 6) {
                    remove(player);
                    new SetupGUI(Gachapon.getInstance().getGachaConfigMap().get(string)).build(playerData, true);
                } else if (code == 7) {
                    remove(player);
                    new BoxEditGUI(Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]).getGachaPackMap().get(
                            Integer.valueOf(string.split(":")[1])
                    )).build(playerData, true);
                }
            } else {
                if (code == 1) {
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string);
                    gachaConfig.setPermission(msg);
                    new SetupGUI(gachaConfig).build(playerData, true);
                    remove(player);
                    playerData.sendMessage("&aChange Success!");
                } else if (code == 2) {
                    remove(player);
                    Integer i = convertToInt(msg, player);
                    if (i == null) return;
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[3]))).build(playerData, true, true,
                            new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.valueOf(string.split(":")[1]))
                                    .setValue(msg)
                            , null, Boolean.parseBoolean(string.split(":")[2]));
                    playerData.sendMessage("&aChange Success!");
                } else if (code == 3) {
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[2])
                    )).build(playerData, false, true, null,
                            new AddItemGUI.RewardConfig().setCurrent(Utils.getItemStackFromString(
                                            string.split(":")[3])).setGT(string.split(":")[5])
                                    .setTier(Utils.applyColor(msg)).setChance(Double.parseDouble(string.split(":")[6]))
                            , Boolean.valueOf(string.split(":")[1]));
                    remove(player);
                    playerData.sendMessage("&aChange Success!");
                } else if (code == 4) {
                    remove(player);
                    Integer gt = convertToInt(msg, player);
                    if (gt == null) return;
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[2])
                    )).build(playerData, false, true, null,
                            new AddItemGUI.RewardConfig().setCurrent(Utils.getItemStackFromString(
                                            string.split(":")[3])).setGT(String.valueOf(gt))
                                    .setTier(string.split(":")[4]).setChance(Double.parseDouble(string.split(":")[6]))
                            , Boolean.valueOf(string.split(":")[1]));
                    playerData.sendMessage("&aChange Success!");
                } else if (code == 5) {
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[2])
                    )).build(playerData, false, true, null,
                            new AddItemGUI.RewardConfig().setCurrent(Utils.getItemStackFromString(
                                            string.split(":")[3])).setGT(string.split(":")[5])
                                    .setTier(string.split(":")[4]).setChance(Double.parseDouble(msg))
                            , Boolean.valueOf(string.split(":")[1]));
                    remove(player);
                    playerData.sendMessage("&aChange Success!");
                } else if (code == 6) {
                    remove(player);
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string);
                    try {
                        gachaConfig.setDailyLimited(Integer.parseInt(msg));
                        playerData.sendMessage("&aChange Success!");
                    } catch (Exception e) {
                        playerData.sendMessage("&cVui Lòng Nhập Số!");
                    }
                    new SetupGUI(gachaConfig).build(playerData, true);
                } else if (code == 7) {
                    remove(player);
                    GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
                    int i = Integer.parseInt(string.split(":")[1]);
                    GachaPack gachaPack = gachaConfig.getGachaPackMap().get(Integer.valueOf(i));
                    try {
                        int n = Integer.parseInt(msg);
                        if (gachaConfig.getBuyRequires().containsKey(i)) {
                            int old = gachaConfig.getBuyRequires().get(i);
                            gachaConfig.getBuyRequires().replace(i, old, n);
                        }
                        playerData.sendMessage("&aChange Success!");
                    } catch (Exception e) {
                        playerData.sendMessage("&c Vui Lòng Nhập Số!");
                    }
                    new BoxEditGUI(gachaPack).build(playerData, true);
                }
            }
        }
    }

    private Integer convertToInt(String msg,Player player) {
        try {
            return Integer.parseInt(msg);
        } catch (Exception e) {
            player.sendMessage("&c Vui Lòng Nhập Số!");
            return null;
        }
    }

    private void remove(Player player, PlayerData playerData, String string) {
        remove(player);
        GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(string.split(":")[0]);
        new AddItemGUI(gachaConfig.getGachaPackMap().get(Integer.parseInt(string.split(":")[2])
        )).build(playerData,false,true,null,
                new AddItemGUI.RewardConfig().setCurrent(Utils.getItemStackFromString(
                        string.split(":")[3])).setGT(string.split(":")[5])
                        .setTier(string.split(":")[4]).setChance(Double.parseDouble(string.split(":")[6]))
                ,Boolean.valueOf(string.split(":")[1]));
        playerData.sendMessage("&cCancel Success!");
    }
}

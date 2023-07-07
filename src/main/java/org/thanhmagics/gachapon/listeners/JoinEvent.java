package org.thanhmagics.gachapon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.PlayerData;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = (Player) event.getPlayer();
        Gachapon.getInstance().getDatabase().addPlayer(player.getUniqueId(),player);
        InventoryClick.sendHidePacket(player);
        Gachapon.getInstance().getPlayerData().get(player.getUniqueId()).reloadDailyBuy();
        Gachapon.getInstance().getPlayerData().get(player.getUniqueId()).setPlayer(player);
    }
}

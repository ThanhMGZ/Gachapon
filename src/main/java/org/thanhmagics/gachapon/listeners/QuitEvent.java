package org.thanhmagics.gachapon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;

import java.util.logging.Level;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Gachapon.getInstance().getDatabase().save(Gachapon.getInstance().getPlayerData().get(player.getUniqueId()));
        Gachapon.getInstance().getPlayerData().get(player.getUniqueId()).setPlayer(null);
        if (GachaConfig.inAnimationReward.containsKey(player)) {
            player.getInventory().addItem(GachaConfig.inAnimationReward.get(player));
            GachaConfig.inAnimationReward.remove(player);
        }
        if (GachaConfig.inAnimation.contains(player)) {
            GachaConfig.inAnimation.remove(player);
        }
    }
}

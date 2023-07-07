package org.thanhmagics.gachapon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.thanhmagics.gachapon.config.GachaConfig;

public class CommandEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (GachaConfig.inAnimation.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

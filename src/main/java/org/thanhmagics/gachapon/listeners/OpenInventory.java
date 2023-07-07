package org.thanhmagics.gachapon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.thanhmagics.gachapon.config.GachaConfig;

public class OpenInventory implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (GachaConfig.inAnimation.contains(player))
            event.setCancelled(true);
    }
}

package org.thanhmagics.gachapon.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.Utils;

public class InventoryClose implements Listener {


    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        PlayerData playerData = Gachapon.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData.getInventory() != null) {
            playerData.setInventory(null);
        }
        if (playerData.getGachaGUI() != null) {
            playerData.setGachaGUI(null);
        }
        if (playerData.getPack() != null) {
            playerData.setPack(null);
        }
    }
}

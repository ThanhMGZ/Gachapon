package org.thanhmagics.gachapon.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;

public class ConfirmGUI {

    public static void open(PlayerData playerData, boolean open, GachaPack gachaPack) {
        Inventory inventory = Bukkit.createInventory(null,3*9,"Tiếp Tục ?");

        inventory.setItem(11, new ItemBuilder(Material.GREEN_WOOL)
                .displayName("&aĐồng Ý Mua!")
                .build());

        inventory.setItem(15, new ItemBuilder(Material.RED_WOOL)
                .displayName("&cHủy Bỏ!")
                .build());

        if (open) {
            playerData.getPlayer().openInventory(inventory);
            playerData.setPack(gachaPack);
        }
    }

}

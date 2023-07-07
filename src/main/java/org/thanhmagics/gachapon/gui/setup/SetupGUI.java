package org.thanhmagics.gachapon.gui.setup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.concurrent.CompletableFuture;

public class SetupGUI {

    private GachaConfig gachaConfig;


    public SetupGUI(GachaConfig gachaConfig) {
        this.gachaConfig = gachaConfig;
    }

    public void build(PlayerData playerData,boolean open) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Edit: " + gachaConfig.getName());
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&a").build());
        }

        inventory.setItem(10, new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWI0MjdlM2E0NzI1NTI2MjQ2NzhiMzM5ZjQwODk2YjQ4ZWUzMGNjMWM5OGIyZjAyZTZiYTg2NTMzOWVjM2U5OSJ9fX0=")
                .displayName("&6Edit Packs")
                .lore("")
                .lore("&7- Số Lượng Đã Tạo: &6" + gachaConfig.getGachaPackMap().size())
                .lore("")
                .lore("&eClick Để Xem Thêm!")
                .build());

        if (gachaConfig.isEnable()) {
            inventory.setItem(12, new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIwYTA2MjlhNzRhMjBjZjQxOWMxYTEyZmNhNDlmODcyYjc4NTI0YmIyNWRkOGI5ZDc0YzFkMjZlN2IwOTI2OCJ9fX0=")
                    .displayName("&6Status")
                    .lore("")
                    .lore("&7- Trạng Thái: &aBẬT")
                    .lore("")
                    .lore("&e Ấn Để Tạm Thời Vô Hiệu Hóa Store Này!")
                    .build());
        } else {
            inventory.setItem(12, new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiNjYyZDNiNTI5YTE4NzI2MWNhYjg2YzZlNTY0MjNiZjg3NmFhMjQ5ZDAzMGZhZWFmMzQzNjJmMzQ0NzI3NyJ9fX0==")
                    .displayName("&6Status")
                    .lore("")
                    .lore("&7- Trạng Thái: &cTẮT")
                    .lore("")
                    .lore("&e Ấn Để Kích Hoạt Store Này!")
                    .build());
        }

        inventory.setItem(14, new ItemBuilder(Material.OAK_SIGN)
                .displayName("&aSet Permission")
                .lore("")
                .lore("&7- Đã Chọn: &6" + (gachaConfig.getPermission() != null ? gachaConfig.getPermission() : "CHƯA_CÓ"))
                .lore("")
                .lore("&e Ấn Để Thay Đổi!")
                .enchant(gachaConfig.getPermission() != null)
                .build());

        inventory.setItem(16, new ItemBuilder(Material.BARRIER)
                .displayName("&aSet Daily Limited")
                .lore("")
                .lore("&7- Đã Chọn: &6" + gachaConfig.getDailyLimited())
                .lore("")
                .lore("&eClick Để Thay Đổi!").build());

        if (open) {
            playerData.getPlayer().openInventory(inventory);
            playerData.setInventory(inventory);
        }
    }
}

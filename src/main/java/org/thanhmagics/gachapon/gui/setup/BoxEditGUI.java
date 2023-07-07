package org.thanhmagics.gachapon.gui.setup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.Objects;

import static org.thanhmagics.gachapon.gui.setup.PackageEditGUI.getL1;
import static org.thanhmagics.gachapon.gui.setup.PackageEditGUI.getL2;

public class BoxEditGUI {

    private GachaPack gachaPack;

    public BoxEditGUI(GachaPack gachaPack) {
        this.gachaPack = gachaPack;
    }

    public Inventory build(PlayerData playerData,boolean open) {

        Inventory inventory = Bukkit.createInventory(null,3*9,"Box Edit: " + gachaPack.gachaConfig.getName() + " | PackID: " + Utils.getKeyByValue(gachaPack.gachaConfig.getGachaPackMap(),gachaPack));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i,new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&a").build());
        }

        inventory.setItem(11, new ItemBuilder(Material.GOLD_BLOCK)
                .displayName("&6Giá Bán")
                .lore("")
                .lore("&7Current: ")
                .lore(getL2(gachaPack))
                .lore("")
                .lore("&eClick Để Edit!")
                .enchant(true)
                .build());

        inventory.setItem(15, new ItemBuilder(Material.DIAMOND_BLOCK)
                .displayName("&6Nhận Đc (Reward)")
                .lore("")
                .lore("&7Current: ")
                .lore(getL1(gachaPack))
                .lore("")
                .lore("&eClick Để Edit!")
                .enchant(true)
                .build());

        int pid = (int) Objects.requireNonNull(Utils.getKeyByValue(gachaPack.gachaConfig.getGachaPackMap(), gachaPack));
        if (pid != 0) {
            inventory.setItem(13, new ItemBuilder(Material.OAK_SIGN)
                    .displayName("&aSet BuyRequires")
                    .lore("&7- Note: phải mua bao nhiêu &6pack trước &7để có thể mua pack này!")
                    .lore("&7- Đã Chọn: &6" + gachaPack.gachaConfig.getBuyRequires().get(pid))
                    .lore("")
                    .lore("&eClick Để Thay Đổi!").build());
        } else {
            inventory.setItem(13, new ItemBuilder(Material.OAK_SIGN)
                    .displayName("&cKhi Pack Bằng 0 Thì Ko Set Đc BuyRequires!")
                    .build());
        }

        inventory.setItem(18, new ItemBuilder(Material.ARROW).displayName("&aBack").build());

        if (open) {
            playerData.getPlayer().openInventory(inventory);
            playerData.setInventory(inventory);
        }
        return inventory;

    }

}

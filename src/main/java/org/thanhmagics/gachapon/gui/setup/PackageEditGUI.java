package org.thanhmagics.gachapon.gui.setup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.MoneyType;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackageEditGUI {

    private GachaConfig gachaConfig;

    public PackageEditGUI(GachaConfig gachaConfig) {
        this.gachaConfig = gachaConfig;
    }

    public Integer[] freeSlots = new Integer[] {
            10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34
    };

    public Inventory build(PlayerData playerData,boolean open,int page) {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9,"Package Editing: " + gachaConfig.getName() + " | Page: " + page);
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!Utils.contain(freeSlots,i))
                inventory.setItem(i,new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&a").build());
        }

        for (int i = (21 * (page - 1)); i < gachaConfig.getGachaPackMap().size(); i++) {
            GachaPack pack = gachaConfig.getGachaPackMap().get(i);
            if (!Utils.isInvFull(inventory)) {
                inventory.setItem(inventory.firstEmpty(),new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRjMzZjOWNiNTBhNTI3YWE1NTYwN2EwZGY3MTg1YWQyMGFhYmFhOTAzZThkOWFiZmM3ODI2MDcwNTU0MGRlZiJ9fX0=")
                        .displayName("&6Pack #" + i)
                        .lore("")
                        .lore("&7- Các Vật Phẩm Có Trong Box:")
                        .lore(getL1(pack))
                        .lore("")
                        .lore("&7- Giá Bán:")
                        .lore(getL2(pack))
                        .lore("")
                        .lore("&eLeft-Click Để Edit!")
                        .lore("&eRight-Click Để Xóa")
                        .build());
            }
        }

        inventory.setItem(18,new ItemBuilder(Material.ARROW)
                .displayName("&aPrevious Page")
                .lore("&7- Page Hiện Tại: " + page)
                .build());

        inventory.setItem(26,new ItemBuilder(Material.ARROW)
                .displayName("&aNext Page")
                .lore("&7- Page Hiện Tại: " + page)
                .build());

        if (!Utils.isInvFull(inventory)) {
            inventory.setItem(inventory.firstEmpty(),new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=")
                    .displayName("&6Add More Item")
                    .lore("")
                    .lore("&eClick Để Add Box Mới!")
                    .build());
        }

        inventory.setItem(45-9,new ItemBuilder(Material.ARROW)
                .displayName("&6Go Back")
                .lore("&eClick Để Trở Lại Trang Trước!"
                ).build());

        if (open) {
            playerData.getPlayer().openInventory(inventory);
            playerData.setInventory(inventory);
        }
        return inventory;
    }


    public static List<String> getL1(GachaPack pack) {
        List<String> l1 = new ArrayList<>();
        for (ItemStack is : pack.chance.keySet())
            l1.add("  &7 " + (is.getItemMeta().getDisplayName().length() == 0 ? is.getI18NDisplayName() : is.getItemMeta().getDisplayName()) + " §f[x" + is.getAmount() + "] §d(Change: " + pack.chance.get(is) + "%&d) " + "&6(Giá Trị VP: "+pack.gia_tri_VP.get(is)+"&6) &aPhẩm Chất:&f " + pack.tier.get(is));
        return l1;
    }

    public static List<String> getL2(GachaPack pack) {
        List<String> l2 = new ArrayList<>();
        for (String s : pack.price.keySet()) {
            String ns = null;
            Integer a = null;
            if (pack.price.get(s).equals(MoneyType.ITEM)) {
                ns = Utils.getItemStackFromString(s).getItemMeta().getDisplayName();
                if (ns.length() == 0)
                    ns =  Utils.getItemStackFromString(s).getI18NDisplayName();
                a = Utils.getItemStackFromString(s).getAmount();
            }
            l2.add("   &7-&6 " + (ns != null ? ("&7" + ns) : s) + "&b:&d" + pack.price.get(s).name() + (a != null ? " &f[x" + a + "]" : ""));
        }
        return l2;
    }

}

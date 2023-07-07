package org.thanhmagics.gachapon.gui.setup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.ItemStackStorage;
import org.thanhmagics.gachapon.config.MoneyType;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AddItemGUI {

    private GachaPack gachaPack;

    public boolean b = false;

    public AddItemGUI(GachaPack gachaPack) {
        this.gachaPack = gachaPack;
    }

    public void build(PlayerData playerData, boolean priceOrReward, boolean open,PriceConfig priceConfig,RewardConfig rewardConfig,boolean created) {
        Inventory inventory = Bukkit.createInventory(null,27,"Add Item: " + gachaPack.gachaConfig.getName());

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i,new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("&a").build());
        }

        inventory.setItem(0, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("&0|" + Utils.getKeyByValue(gachaPack.gachaConfig.getGachaPackMap(),gachaPack) + ";" +
                        priceOrReward + ";" + created).build());

        if (priceOrReward) {
            if (created) {
                inventory.setItem(1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .displayName("&0|" + (priceConfig.itemStack != null ? Utils.itemStackToString(priceConfig.itemStack) :
                                (priceConfig.value != null ? priceConfig.value : null))).build());
            }
            MoneyType moneyType = priceConfig.moneyType != null ? priceConfig.moneyType : MoneyType.ITEM;
            List<String> lore = new LinkedList<>();
            int i=0,i2 = 0;
            for (MoneyType mt : MoneyType.values()) {
                if (mt.equals(moneyType)) {
                    lore.add(Utils.applyColor("&a+ ") + mt.name());
                    i2 = i;
                } else {
                    lore.add(Utils.applyColor("&7- ") + mt.name());
                }
                i++;
            }
            if (moneyType.equals(MoneyType.MONEY) || moneyType.equals(MoneyType.PLAYERPOINT)) {
                inventory.setItem(12, new ItemBuilder(Material.OAK_SIGN)
                        .displayName("&aSet Giá Trị &0" + (i2 ))
                        .lore("")
                        .lore("&7- Đã Ghi: &6" + (priceConfig.value != null ? priceConfig.value : "0"))
                        .lore("")
                        .lore("&eClick Để Chỉnh Sửa!")
                        .build());
            } else {
                inventory.setItem(12, priceConfig.itemStack != null ? priceConfig.itemStack : new ItemStack(Material.AIR));
                inventory.setItem(11, new ItemBuilder(Material.OAK_SIGN).displayName("&a Đặt Item Vào Ô Trống! &b&m===>").build());
            }
            inventory.setItem(14, new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=")
                    .displayName("&aSet Money Type &0" + (i2 ))
                    .lore("")
                    .lore(lore)
                    .lore("")
                    .lore("&eClick Để Edit!")
                    .build());
            if (created) {
                inventory.setItem(22, new ItemBuilder(Material.RED_WOOL).displayName("&cDelete").build());
            }
            inventory.setItem(26, new ItemBuilder(Material.GREEN_WOOL).displayName("&aSave &0" + i2)
                    .lore(b ? "&cVui Lòng Chọn Item Đặt Vào Slot Trống!" : "").build());
        } else {
            ItemStack itemStack = rewardConfig.current != null ? rewardConfig.current : new ItemStack(Material.AIR);
            inventory.setItem(10, itemStack);
            String tier = rewardConfig.tier != null ? rewardConfig.tier : "&dMYTHIC";
            inventory.setItem(13, new ItemBuilder(Material.PAPER).displayName("&a Set Phẩm Chất Của Item!")
                    .lore("")
                    .lore("&7- Đã Ghi:&f " + tier)
                    .lore("")
                    .lore("&eClick Để Edit!")
                    .build());
            String gt = rewardConfig.gia_tri != null ? rewardConfig.gia_tri : "&66969";
            inventory.setItem(14, new ItemBuilder(Material.OAK_SIGN)
                    .displayName("&a Set Giá Trị VP")
                    .lore("")
                    .lore("&7- Đã Ghi:&f " + gt)
                    .lore("")
                    .lore("&eClick Để Edit!")
                    .build());
            inventory.setItem(15, new ItemBuilder(Material.FLINT)
                    .displayName("&aSet Chance")
                    .lore("")
                    .lore("&7- Đã Ghi: &6" + rewardConfig.chance).build());
            UUID uuid = UUID.randomUUID();
            inventory.setItem(1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                    .displayName("&0:" + tier + ";" + gt + ";" + rewardConfig.chance)
                    .lore("&0|" + uuid.toString()).build());
            ItemStackStorage.map.put(uuid,itemStack);
            inventory.setItem(26, new ItemBuilder(Material.GREEN_WOOL).displayName("&aSave")
                    .lore(b ? "&cVui Lòng Chọn Item Đặt Vào Slot Trống!" : "").build());
        }

        if (open) {
            if (playerData.getPlayer() == null)
                playerData.sendMessage("SIUUU");
            playerData.getPlayer().openInventory(inventory);
            playerData.setInventory(inventory);
        }
    }

    public static class PriceConfig {
        public MoneyType moneyType;

        public String value;

        public ItemStack itemStack;

        public PriceConfig setIS(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public PriceConfig setMT(MoneyType mt) {
            this.moneyType = mt;
            return this;
        }

        public PriceConfig setValue(String value) {
            this.value = value;
            return this;
        }

    }

    public static class RewardConfig {
        public ItemStack current = null;

        public String tier = "&dMYTHIC";

        public String gia_tri = "6969";

        public double chance = 0;

        public RewardConfig setChance(double chance) {
            this.chance = chance;
            return this;
        }

        public RewardConfig setCurrent(ItemStack itemStack) {
            this.current = itemStack;
            return this;
        }

        public RewardConfig setTier(String tier) {
            this.tier = tier;
            return this;
        }
        public RewardConfig setGT(String gt) {
            this.gia_tri = gt;
            return this;
        }
    }
}

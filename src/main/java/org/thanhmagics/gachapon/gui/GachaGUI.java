package org.thanhmagics.gachapon.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.*;

public class GachaGUI {

    private List<Integer> packs = new ArrayList<>();

    private Map<Integer,Integer> packsId = new HashMap<>();

    private List<Integer> preview = new ArrayList<>();

    private List<Integer> activePack = new ArrayList<>();

    private PlayerData playerData;

    private GachaConfig gachaConfig;

    private Integer packid;

    public void open(PlayerData playerData, boolean open, GachaConfig gachaConfig,Integer packid) {
        this.playerData = playerData;
        this.gachaConfig = gachaConfig;
        this.packid = packid;
        FileConfiguration config = Gachapon.getInstance().getGgcFile().getConfig();
        Inventory inventory = Bukkit.createInventory(null,Integer.parseInt(config.getString("InvCol")) * 9,Utils.applyColor(config.getString("Title").replace("{name}",gachaConfig.getName())));
        for (String k : config.getConfigurationSection("GUI").getKeys(false)) {
            int[] slot = config.getString("GUI." + k + ".slot").contains("{full_slot}") ? getSize(inventory) : stringToArray(Objects.requireNonNull(config.getString("GUI." + k + ".slot")));
            if (contain(packs,slot) || contain(preview,slot))
                Gachapon.getInstance().getServer().shutdown();
            if (k.equals("pack")) {
                int l = 0;
                for (int j : slot) {
                    packs.add(j);
                    packsId.put(j,l);
                    l++;
                }
            }
            if (k.equals("preview"))
                for (int j : slot)
                    preview.add(j);
            for (int i = 0; i < slot.length; i++) {
                if (k.equalsIgnoreCase("pack")) {
                    if (gachaConfig.getGachaPackMap().containsKey(i)) {
                        int requires = gachaConfig.getBuyRequires().get(i);
                        int player;
                        GachaPack pack = gachaConfig.getGachaPackMap().get(i);
                        if (!(playerData.getPack_da_mua().contains(gachaConfig,gachaConfig.getGachaPackMap().get(i - 1)))) {
                            player = 0;
                        }  else {
                            player = playerData.getPack_da_mua().get(gachaConfig,gachaConfig.getGachaPackMap().get(i - 1));
                        }
                        Integer j = playerData.getDailyBuy().get(pack);
                        if (j == null)
                            j = 0;
                        if (player >= requires) {
                            ItemStack itemStack = ItemBuilder.getItemStackByConfig(config, "item_stack." + k,
                                    new ItemBuilder.LoreReplacer().setPlayer(playerData.getPlayer())
                                            .setId(i)
                                            .setPrice(pack.price)
                                            .setDailYBuy((gachaConfig.getDailyLimited() - j)));
                            inventory.setItem(slot[i], itemStack);
                            activePack.add(slot[i]);
                        } else {
                            ItemBuilder.LoreReplacer loreReplacer =new ItemBuilder.LoreReplacer().setPlayer(playerData.getPlayer())
                                    .setId(i).setPrice(pack.price).setDailYBuy((gachaConfig.getDailyLimited() - j));
                            loreReplacer.min = String.valueOf(player);
                            loreReplacer.max = String.valueOf(requires);
                            ItemStack itemStack = ItemBuilder.getItemStackByConfig(config, "item_stack.packnq",loreReplacer);
                            inventory.setItem(slot[i], itemStack);
                        }
                    } else {
                        inventory.setItem(slot[i],ItemBuilder.getItemStackByConfig(config,"item_stack.null_pack",null));
                    }
                } else {
                    inventory.setItem(slot[i],ItemBuilder.getItemStackByConfig(config,"item_stack." + k,null));
                }
            }
        }

        if (packid != null) {
//            for (int i = 0; i < gachaConfig.getGachaPackMap().size(); i++) {
//                if ((preview.size() - 1) <= i)
//                    inventory.setItem(preview.get(i),getKeyByValue(i,gachaConfig));
//            }
            GachaPack gachaPack = gachaConfig.getGachaPackMap().get(packid);
            for (int i = 0; i < preview.size(); i++) {
                if ((gachaPack.chance.size()) > i) {
                    ItemStack is = getKeyByIndex(i, gachaPack);
                    ItemStack nis = is.clone();
                    double chance = gachaPack.chance.get(is);
                    inventory.setItem(preview.get(i),new ItemBuilder(nis).addDisplayName(" &7(" + chance + "%&f)").build());
                  //  inventory.setItem(preview.get(i), new ItemBuilder(is).clone().addDisplayName(" &7(" + gachaPack.chance.get(is) + "%&f)").build());
                } else {
                    break;
                }
            }
        }

        if (open) {
            playerData.getPlayer().openInventory(inventory);
            playerData.setGachaGUI(this);
        }
    }

    private ItemStack getKeyByIndex(int i, GachaPack gachaConfig) {
       // return new ArrayList<>(gachaConfig.chance.keySet()).get(i);
        ArrayList<ItemStack> arrayList = new ArrayList<>(gachaConfig.chance.keySet());
        return arrayList.get(i);
    }

    private boolean contain(List<Integer> list, int[] ints) {
        for (int i : ints) {
            if (list.contains(i))
                return true;
        }
        return false;
    }

    public List<Integer> getActivePack() {
        return activePack;
    }

    private int[] getSize(Inventory inventory) {
        int[] i = new int[inventory.getSize()];
        for (int j = 0; j < inventory.getSize(); j++) {
            i[j] = j;
        }
        return i;
    }

    private int[] stringToArray(String s) {
        int[] i = new int[s.split(",").length];
        for (int j = 0; j < s.split(",").length; j++) {
            i[j] = Integer.parseInt(s.split(",")[j]);
        }
        return i;
    }

    public List<Integer> getPacks() {
        return packs;
    }

    public Map<Integer, Integer> getPacksId() {
        return packsId;
    }

    public List<Integer> getPreview() {
        return preview;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public GachaConfig getGachaConfig() {
        return gachaConfig;
    }

    public Integer getPackid() {
        return packid;
    }
}

package org.thanhmagics.gachapon.config;

import org.bukkit.inventory.ItemStack;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class GachaPack {
    public Map<ItemStack,Double> chance = new HashMap<>();

  //  public String tier = Utils.applyColor("&6LEGENDARY");
    public Map<ItemStack,String> tier = new HashMap<>();

    public Map<String,MoneyType> price = new HashMap<>();

    public Map<ItemStack,String> gia_tri_VP = new HashMap<>();


    public GachaConfig gachaConfig;

    public GachaPack(GachaConfig gachaConfig) {
        this.gachaConfig = gachaConfig;
    }

    private Map<Integer,ItemStack> map = new HashMap<>();

    public ItemStack getItem(int i) {
        if (map.size() == 0 || chance.size() != map.size()) {
            Map<Integer, ItemStack> map = new HashMap<>();
            int j = 0;
            for (ItemStack k : chance.keySet()) {
                for (int l = 0; l < chance.get(k); l++) {
                    map.put(j, k);
                    j++;
                }
            }
            this.map = map;
            return map.get(i);
        } else {
            return map.get(i);
        }
    }

    public String toStringg() {
        StringBuilder stringBuilder = new StringBuilder();
       // stringBuilder.append(tier.replace("§","@color") + ":(");

//        stringBuilder.append("<");
//        for (ItemStack k : tier.keySet()) {
//            stringBuilder.append(Utils.itemStackToString(k) + "|" + tier.get(k) + ",");
//        }
//        if (tier.size() > 0)
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        stringBuilder.append(">%(");
//        for (ItemStack itemStack : chance.keySet()) {
//            stringBuilder.append(Utils.itemStackToString(itemStack) + "|" + chance.get(itemStack) + ",");
//        }
//        if (chance.keySet().size() > 0)
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        stringBuilder.append(")%");
//        stringBuilder.append("[");
//        for (String string : price.keySet()) {
//            stringBuilder.append(string + "|" + price.get(string) + ",");
//        }
//        if (price.keySet().size() > 0)
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        stringBuilder.append("]%");
//        stringBuilder.append("<");
//        for (ItemStack k : gia_tri_VP.keySet()) {
//            stringBuilder.append(Utils.itemStackToString(k) + "|" + gia_tri_VP.get(k) + ",");
//        }
//        if (gia_tri_VP.size() > 0)
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        stringBuilder.append(">");
        tier.keySet().forEach(k -> stringBuilder.append(Utils.itemStackToString(k)).append(",").append(tier.get(k)).append(":"));
        if (tier.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        stringBuilder.append("~");
        chance.keySet().forEach(k -> stringBuilder.append(Utils.itemStackToString(k)).append(",").append(chance.get(k)).append(":"));
        if (chance.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        stringBuilder.append("~");
        price.keySet().forEach(k -> stringBuilder.append(k).append(",").append(price.get(k)).append(":"));
        if (price.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        stringBuilder.append("~");
        gia_tri_VP.keySet().forEach(k -> stringBuilder.append(Utils.itemStackToString(k)).append(",").append(gia_tri_VP.get(k)).append(":"));
        if (gia_tri_VP.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return removeColor(stringBuilder.toString());
    }

    public static GachaPack fromString(GachaConfig gachaConfig,String string) {
        try {
            String[] args = string.split("~");
            for (int i = 0; i < args.length; i++) {
                args[i] = applyColor(args[i]);
            }
            GachaPack gachaPack = new GachaPack(gachaConfig);
            for (int i = 0; i < args[0].split(":").length; i++) {
                String s = args[0].split(":")[i];
                gachaPack.tier.put(Utils.getItemStackFromString(s.split(",")[0]),s.split(",")[1]);
            }
            for (int i = 0; i < args[1].split(":").length; i++) {
                String s = args[1].split(":")[i];
                gachaPack.chance.put(Utils.getItemStackFromString(s.split(",")[0]), Double.valueOf(s.split(",")[1]));
            }
            for (int i = 0; i < args[2].split(":").length; i++) {
                String s = args[2].split(":")[i];
                gachaPack.price.put((s.split(",")[0]), MoneyType.valueOf(s.split(",")[1]));
            }
            for (int i = 0; i < args[3].split(":").length; i++) {
                String s = args[3].split(":")[i];
                gachaPack.gia_tri_VP.put(Utils.getItemStackFromString(s.split(",")[0]),s.split(",")[1]);
            }
            return gachaPack;
        } catch (Exception e) {
            return new GachaPack(gachaConfig);
        }
//        try {
//            String[] args = string.split("%");
//            GachaPack gachaPack = new GachaPack(gachaConfig);
//            if (args[0].length() > 2) {
//                String arg = args[0].split(">")[0].split("<")[1];
//                if (arg.startsWith("{"))
//                    Utils.deleteChar(arg,0);
//                if (arg.contains(",")) {
//                    for (String k : arg.split(",")) {
//                        gachaPack.tier.put(Utils.getItemStackFromString(k.split("\\|")[0]),k.split("\\|")[1]);
//                    }
//                } else {
//                    gachaPack.tier.put(Utils.getItemStackFromString(arg.split("\\|")[0]),arg.split("\\|")[1]);
//                }
//            }
//            if (args[1].length() > 2) {
//                String s1 = args[1].split("\\)")[0].split("\\(")[1];
//                String[] s2 = s1.split(",");
//                for (String s3 : s2) {
//                    String s4 = s3.split("\\|")[0];
//                    String s5 = s3.split("\\|")[1];
//                    gachaPack.chance.put(Utils.getItemStackFromString(s4), Double.valueOf(s5));
//                }
//            }
//            if (args[3].length() > 3) {
//                String arg = args[3].split(">")[0].split("<")[1];
//                if (arg.contains(",")) {
//                    for (String k : arg.split(",")) {
//                        gachaPack.gia_tri_VP.put(Utils.getItemStackFromString(k.split("\\|")[0]),k.split("\\|")[1]);
//                        System.out.println(k.split("\\|")[0]);
//                    }
//                } else {
//                    gachaPack.gia_tri_VP.put(Utils.getItemStackFromString(arg.split("\\|")[0]),arg.split("\\|")[1]);
//                    System.out.println(arg.split("\\|")[0]);
//                }
//            }
//            if (args[2].length() > 2) {
//                String s6 = args[2].split("]")[0].split("\\[")[1];
//                String[] s7 = s6.split(",");
//                for (String s8 : s7) {
//                    String s9 = s8.split("\\|")[0];
//                    String s10 = s8.split("\\|")[1];
//                    gachaPack.price.put(s9, MoneyType.valueOf(s10));
//                }
//            }
//            return gachaPack;
//        } catch (Exception e) {
//            return new GachaPack(gachaConfig);
//        }
    }

    private static String applyColor(String s) {
        if (!s.contains("@color")) return s;
        return s.replaceAll("@color","§");
    }

    private String removeColor(String s) {
        if (!s.contains("§")) return s;
        return s.replaceAll("§","@color");
    }

    public void delete(int i ) {
        //Integer id = (Integer) Utils.getKeyByValue(gachaConfig.getGachaPackMap(),this);
        Gachapon.getInstance().getDatabase().updatePack_da_mua(this);
        gachaConfig.getGachaPackMap().remove(i,this);
        gachaConfig.reloadGachaPack();
    }
}

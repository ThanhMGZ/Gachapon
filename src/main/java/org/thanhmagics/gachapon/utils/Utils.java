package org.thanhmagics.gachapon.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isKTĐB(String s) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9][&]]");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    public static String applyColor(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static String unColor(String s) {
        return ChatColor.stripColor(s);
    }

    public List<String> unColor(List<String> strings) {
        List<String> ss = new ArrayList<>();
        for (String s : strings)
            ss.add(unColor(s));
        return ss;
    }

    public static List<String> applyColor(List<String> s) {
        List<String> n = new ArrayList<>();
        for (String ss : s) {
            n.add(applyColor(ss));
        }
        return n;
    }

    public static String[] applyColor(String[] strings) {
        List<String> ss = new ArrayList<>();
        for (String s:
             strings) {
            ss.add(applyColor(s));
        }
        return (String[]) ss.toArray();
    }

    public static int randomNumber(int min,int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static String getDate() {
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime currentDate = ZonedDateTime.now(vietnamZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

    public static Object getKeyByValue(Map<?,?> map,Object v) {
        Map<Object, Object> map1 = new HashMap<>();
        for (Object o : map.keySet()) {
            map1.put(map.get(o),o);
        }
        if (map1.containsKey(v))
            return map1.get(v);
        return null;
    }

    public static Class<?> getKeyByValue(Map<Object,Class<?>> map,Class<?> clazz) {
        Map<Class<?>, Object> map1 = new HashMap<>();
        for (Object o : map.keySet()) {
            map1.put(map.get(o),o);
        }
        if (map1.containsKey(clazz))
            return (Class<?>) map1.get(clazz);
        return null;
    }

    @Deprecated
    public static ItemStack getItemStackFromString(String data) {
        return ItemData.getItemStack(data);
//        ItemStack item = null;
//        try {
//            ByteArrayInputStream is = new ByteArrayInputStream(Base64Coder.decodeLines(data));
//            try {
//                BukkitObjectInputStream dip = new BukkitObjectInputStream(is);
//                item = (ItemStack) dip.readObject();
//                dip.close();
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return item;
    }

    @Deprecated
    public static String itemStackToString(ItemStack is) {
        return ItemData.getData(is);
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(outputStream);
//            bukkitObjectOutputStream.writeInt(1);
//            bukkitObjectOutputStream.writeObject(is);
//            bukkitObjectOutputStream.close();
//            return Base64Coder.encodeLines(outputStream.toByteArray());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static boolean isInvFull(Inventory inventory) {
        ItemStack[] c = inventory.getContents();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (c[i] == null)
                return false;
        }
        return true;
    }

    public static boolean contain(Object[] o,Object o1) {
        for (Object o2 : o)
            if (o2.equals(o1))
                return true;
        return false;
    }

    public static Object[] addElementToArray(Object[] o,Object e) {
        if (o.length > 0 && !o[0].equals(e)) return o;
        Object[] n = new Object[o.length + 1];
        System.arraycopy(o, 0, n, 0, o.length);
        n[o.length] = e;
        return n;
    }

    public static Class<?>[] addElementToArray(Class<?>[] clzz,Class<?> e) {
        Class<?>[] n = new Class<?>[clzz.length + 1];
        for (int i = 0; i < clzz.length; i++) {
            n[i] = clzz[i];
        }
        n[clzz.length] = e;
        return n;
    }

    public static String deleteChar(String s,int l) {
        if (s.length() >= l) {
            StringBuilder stringBuilder = new StringBuilder(s);
            stringBuilder.deleteCharAt(l);
            return stringBuilder.toString();
        }
        return s;
    }

    public static boolean inventoryContainItem(PlayerInventory inventory,ItemStack itemStack) {
        if (inventory.contains(itemStack))
            return true;
        Integer[] w = a(inventory,itemStack);
        ItemStack[] content = inventory.getContents();
        if (w.length == 0)
            return false;
        for (int i : w) {
            if (content[i].getAmount() >= itemStack.getAmount()) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeItem(PlayerInventory inventory,ItemStack itemStack) {
//        if (!inventoryContainItem(inventory,itemStack))
//            return false;
        Integer[] w = a(inventory,itemStack);
        ItemStack[] content = inventory.getContents();
        for (int i : w) {
            ItemStack is = content[i];
            if (is != null) {
                if (is.getAmount() == itemStack.getAmount()) {
                    inventory.remove(itemStack);
                } else {
                    ItemStack isc = itemStack.clone();
                    isc.setAmount(is.getAmount() - itemStack.getAmount());
                    inventory.setItem(getInvSlotByItemStack(inventory, is), isc);
                }
                return true;
            }
        }
        return false;
    }

    public static double distance(Location l1,Location l2) {
        return Math.sqrt(Math.pow(l2.getX() - l1.getX(),2) + Math.pow(l2.getY() - l1.getY(),2) + Math.pow(l2.getZ() - l1.getZ(),2));
    }

    private static int getInvSlotByItemStack(PlayerInventory inventory,ItemStack is) {
        ItemStack[] c = inventory.getContents();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (c[i] != null) {
                if (c[i].equals(is))
                    return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    private static Integer[] a(PlayerInventory inventory,ItemStack itemStack) {
        Integer[] w = new Integer[0];
        ItemStack[] content = inventory.getContents();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (content[i] != null) {
                if (content[i].getType().equals(itemStack.getType())) {
                    ItemStack ic1 = content[i].clone();
                    ItemStack ic2 = itemStack.clone();
                    ic1.setAmount(1);
                    ic2.setAmount(1);
                    if (ic1.equals(ic2)) {
                        //   w = (Integer[]) addElementToArray(w,i);
                        Integer[] nw = new Integer[w.length + 1];
                        System.arraycopy(w, 0, nw, 0, w.length);
                        nw[w.length] = i;
                        w = nw;
                    }
                }
            }
        }
        return w;
    }

    public static int lastChar(String s) {
        return Integer.parseInt(String.valueOf(s.toCharArray()[s.length() - 1]));
    }

}

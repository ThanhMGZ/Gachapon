package org.thanhmagics.gachapon.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ItemData {

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Inventory getInventory(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ItemStack getItemStack(String data) {
        ItemStack item = null;
        try {
            ItemStack[] stack1 = ItemData.itemStackArrayFromBase64(data);
            ItemStack[] arrayOfItemStack1;
            int in = (Objects.requireNonNull(arrayOfItemStack1 = stack1)).length;
            for (byte b = 0; b < in; b++) {
                ItemStack itemStack = arrayOfItemStack1[b];
                if (itemStack != null)
                    item = new ItemStack(itemStack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static String getData(Inventory inventory) {
        return ItemData.itemStackArrayToBase64(inventory.getContents());
    }

    public static String getData(ItemStack stack) {
        Inventory inventory = Bukkit.createInventory(null, 9, String.valueOf(System.currentTimeMillis()));
        inventory.setItem(0, stack);
        return ItemData.itemStackArrayToBase64(inventory.getContents());
    }
}

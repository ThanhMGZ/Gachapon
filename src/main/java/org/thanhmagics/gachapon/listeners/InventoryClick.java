package org.thanhmagics.gachapon.listeners;

import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.*;
import org.thanhmagics.gachapon.gui.GachaGUI;
import org.thanhmagics.gachapon.gui.setup.AddItemGUI;
import org.thanhmagics.gachapon.gui.setup.BoxEditGUI;
import org.thanhmagics.gachapon.gui.setup.PackageEditGUI;
import org.thanhmagics.gachapon.gui.setup.SetupGUI;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.TextComponentBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.math.BigDecimal;
import java.util.*;

public class InventoryClick implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerData playerData = Gachapon.getInstance().getPlayerData().get(player.getUniqueId());
        if (playerData.getInventory() != null) {
            if (event.getView().getTitle().startsWith("Edit: ")) {
                String gc = event.getView().getTitle().split("Edit: ")[1];
                GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(gc);
                SetupGUI setupGUI = (SetupGUI) new SetupGUI(gachaConfig);
                event.setCancelled(true);
                if (event.getSlot() == 10) {
                    if (gachaConfig.isEnable()) {
                        playerData.sendMessage("&c Tắt Status đi rồi Edit!");
                        player.closeInventory();
                        return;
                    }
                    new PackageEditGUI(gachaConfig).build(playerData, true,1);
                } else if (event.getSlot() == 12) {
                    if (Gachapon.getInstance().getConfig().getString("Animation.ViewerLocation").length() < 5 ||
                    Gachapon.getInstance().getConfig().getString("Animation.Location").length() < 5) {
                        playerData.sendMessage("&cVui Lòng Set Reward Location (sử dụng /gachaadmin để để xem thêm)");
                        return;
                    }
                    for (GachaPack pack : gachaConfig.getGachaPackMap().values()) {
                        int c = 0;
                        for (double chance : pack.chance.values()) {
                            c += chance;
                        }
                        if (c != 100) {
                            playerData.sendMessage("&a Hảy Đảm Bảo Rằng Tất Cả tỉ lệ 'chance' trong các GachaPack đều là 100%");
                            player.closeInventory();
                            return;
                        }
                    }
                    gachaConfig.setEnable(!gachaConfig.isEnable());
                    setupGUI.build(playerData, true);
                } else if (event.getSlot() == 16) {
//                    gachaConfig.delete();
//                    player.closeInventory();
//                    playerData.sendMessage("&aDelete &d"+gachaConfig.getName()+"&a Success!");
                    playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                            "&7- &aEnter Xuống Chat Một Số Để Ghi Vào File Config!",
                            "&7- &aEX: 69","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                    player.closeInventory();
                    ChatEvent.addPlayer(player,"6;" + gachaConfig.getName());
                } else if (event.getSlot() == 14) {
                    player.closeInventory();
                    ChatEvent.addPlayer(player,"1;" + gachaConfig.getName());
                    playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                            "&7- &aEnter Xuống Chat Permission Để Ghi Vào File Config!",
                            "&7- &aEX: gacha.admin","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                }
            }
            if (event.getView().getTitle().startsWith("Package Editing: ")) {
                String gc = event.getView().getTitle().split("Package Editing: ")[1];
                GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(gc.split(" \\| ")[0]);
                int page = Integer.parseInt(gc.split(" \\| ")[1].split("Page: ")[1]);
                int max = (gachaConfig.getGachaPackMap().size() / 21) + 1;
                event.setCancelled(true);
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Pack #")) {
                    if (event.getClick().equals(ClickType.LEFT)) {
                        int i = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split("§6Pack #")[1]);
                        new BoxEditGUI(gachaConfig.getGachaPackMap().get(i)).build(playerData, true);
                    } else if (event.getClick().equals(ClickType.RIGHT)) {
                        int i = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split("§6Pack #")[1]);
                        gachaConfig.getGachaPackMap().get(i).delete(i);
                        new PackageEditGUI(gachaConfig).build(playerData,true,page);
                    }
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Add More Item")) {
                    gachaConfig.addNewGachaPack();
                    new PackageEditGUI(gachaConfig).build(playerData,true,page);
                } else if (event.getSlot() == (45-9)) {
                    new SetupGUI(gachaConfig).build(playerData,true);
                } else if (event.getSlot() == 18) {
                    new PackageEditGUI(gachaConfig).build(playerData,true,page < max ? page + 1 : page);
                } else if (event.getSlot() == 26) {
                    new PackageEditGUI(gachaConfig).build(playerData,true,page < max ? page + 1 : page);
                }
            } else if (event.getView().getTitle().startsWith("Box Edit: ")) {
                event.setCancelled(true);
                String packid = event.getView().getTitle().split("\\|")[1].split(" PackID: ")[1];
                GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(event.getView().getTitle().split(" \\| ")[0].split("Box Edit: ")[1]);
                GachaPack gachaPack = gachaConfig.getGachaPackMap().get(Integer.valueOf(packid));
                if (event.getSlot() == 11) {
                    player.closeInventory();
                    playerData.sendMessage("&6&m<=====> &d&lGachaPack Edit &6&m<=====>");
                    playerData.sendMessage("&a- Note: &7Click Vào Tên Item Trên Chat Dể Delete!","&a- Danh Sách Đã Tạo: &7(Các Item Dưới Đây Dùng Để Mua Các Pack Trong Store)");
                    LinkedList<TextComponent> listOfItem = new LinkedList<>();
                    int i = 0;
                    for (String k : gachaPack.price.keySet()) {
                        if (gachaPack.price.get(k).equals(MoneyType.ITEM)) {
                            ItemStack item = Utils.getItemStackFromString(k);
                            UUID uuid = UUID.randomUUID();
                            ItemStackStorage.map.put(uuid,item);
                            listOfItem.add(new TextComponentBuilder("&f" + i + ".&7 " + (item.getItemMeta().getDisplayName().length() == 0 ? item.getI18NDisplayName() : item.getItemMeta().getDisplayName()) + " &f[x" + item.getAmount() + "]")
                                    .addDescription("&aClick To Delete!")
                                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "5;" + gachaConfig.getName() + ":" + packid + ":" + uuid))
                                    .build());
                        } else {
                            listOfItem.add(new TextComponentBuilder("&f" + i + ".&6 "  + gachaPack.price.get(k).name() + " &b: &d" + k)
                                    .addDescription("&aClick To Delete!")
                                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "6;" + gachaConfig.getName() + ":" + packid + ":" + k))
                                    .build());
                        }
                        i++;
                    }
                    playerData.sendMessage(listOfItem);
                    playerData.sendMessage("&7---------------------");
                    playerData.sendMessage(
                            new TextComponentBuilder("&7+[AddItem]")
                            .addDescription("&aClick Để Thêm Item Dùng Để Mua Pack!")
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "1;" + gachaConfig.getName() + ":" + packid))
                            .build(),
                            new TextComponentBuilder("  &d+[AddMoney]")
                            .setUnderLine(false)
                            .addDescription("&aClick Để Thêm Số Tiền Cần Dùng Để Mua Pack!")
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "2;" + gachaConfig.getName() + ":" + packid))
                            .build(),
                            new TextComponentBuilder("  &c+[AddPoint]")
                            .setUnderLine(false)
                            .addDescription("&aClick Để Thêm Số PlayerPoint Cần Dùng Đề Mua Pack!")
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "3;" + gachaConfig.getName() + ":" + packid))
                            .build());
                    playerData.sendMessage("&6&m<==================>");
                } else if (event.getSlot() == 15) {
                    player.closeInventory();
                    playerData.sendMessage("&d&m<=====>&6&l GachaPack Edit&d&m <=====>");
                    playerData.sendMessage("&a- Note: &7Click Vào Click Tên Item Trên Chat Dể Delete!","&a- Danh Sách Đã Tạo: ");
                    LinkedList<TextComponent> listOfItem = new LinkedList<>();
                    int i = 0;
                    for (ItemStack stack : gachaPack.chance.keySet()) {
                        UUID uuid = UUID.randomUUID();
                        ItemStackStorage.map.put(uuid,stack);
                        listOfItem.add(new TextComponentBuilder(
                                "&f" + i + ". " + (stack.getItemMeta().getDisplayName().length() == 0 ? stack.getI18NDisplayName() : stack.getItemMeta().getDisplayName()) + " &f[x" + stack.getAmount() + "]" + " &d(" + gachaPack.chance.get(stack) + "%&d)" + " &a(TIER: " + gachaPack.tier.get(stack) + "&a) &6(GT: " + gachaPack.gia_tri_VP.get(stack) + "&6)"
                        ).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "7;" + gachaConfig.getName() + ":" + packid + ":" + uuid)).addDescription("&aClick To Delete!")
                                .build());
                        i++;
                    }
                    playerData.sendMessage(listOfItem);
                    playerData.sendMessage("&7---------------------");
                    playerData.sendMessage(new TextComponentBuilder("&6+[AddItem]")
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin --p " + "4;" + gachaConfig.getName() + ":" + packid))
                            .addDescription("&aClick Để Thêm Item Vào Store!").build());
                    playerData.sendMessage("&6&m<==================>");
                } else if (event.getSlot() == 18) {
                    new PackageEditGUI(gachaConfig).build(playerData,true,1);
                } else if (event.getSlot() == 13) {
                    if (event.getInventory().getItem(13).getLore().size() < 2) {
                        event.setCancelled(true);
                        return;
                    }
                    ChatEvent.addPlayer(player,"7;" + gachaConfig.getName() + ":" + packid);
                    player.closeInventory();
                    playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                            "&7- &aEnter Xuống Chat Số Muốn Ghi Vào File Config!",
                            "&7- &aEX: 123","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                }
            } else if (event.getView().getTitle().startsWith("Add Item: ")) {
                GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(event.getView().getTitle().split("Add Item: ")[1]);
                String is = event.getInventory().getItem(0).getItemMeta().getDisplayName().split("\\|")[1];
                GachaPack gachaPack = gachaConfig.getGachaPackMap().get(Integer.parseInt(is.split(";")[0]));
                boolean priceOrReward = Boolean.valueOf(is.split(";")[1]);
                boolean created = Boolean.valueOf(is.split(";")[2]);
                event.setCancelled(true);
                if (priceOrReward) {
                    if (event.getSlot() == 12) {
                        MoneyType current = MoneyType.values()[Utils.lastChar(event.getCurrentItem().getItemMeta().getDisplayName())];
                        if (!current.equals(MoneyType.ITEM)) {
                            ChatEvent.addPlayer(player,"2;" + gachaConfig.getName() + ":" + current.name() + ":" + created + ":" + Utils.getKeyByValue(gachaPack.gachaConfig.getGachaPackMap(),gachaPack));
                            player.closeInventory();
                            playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                                    "&7- &aEnter Xuống Chat Giá Trị Sẽ Add Vào GachaPack!",
                                    "&7- &aEX: 1234","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                        } else {
                            new AddItemGUI(gachaPack).build(playerData,true,true,new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.ITEM).setValue(null).setIS(null),null,created);
                        }
                    } else if (event.getSlot() == 14) {
                        int i = Utils.lastChar(event.getCurrentItem().getItemMeta().getDisplayName());
                        if (i == 2) {
                            new AddItemGUI(gachaPack).build(playerData,true,true,new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.ITEM).setValue(null),null,created);
                        } else if (i == 1) {
                            new AddItemGUI(gachaPack).build(playerData,true,true,new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.MONEY).setValue(null),null,created);
                        } else if (i == 0) {
                            new AddItemGUI(gachaPack).build(playerData,true,true,new AddItemGUI.PriceConfig()
                                    .setMT(MoneyType.PLAYERPOINT).setValue(null),null,created);
                        }
                    } else if (event.getSlot() == 22) {
                        event.setCancelled(true);
//                        if (created) {
//                            gachaPack.price.remove(event.getInventory().getItem(1).getItemMeta().getDisplayName().split("\\|")[1]);
//                        }
//                        new BoxEditGUI(gachaPack).build(playerData,true);
                        playerData.sendMessage("&aDelete GachaPack Success!");
                    } else if (event.getSlot() == 26) {
                        MoneyType current = MoneyType.values()[Utils.lastChar(event.getCurrentItem().getItemMeta().getDisplayName())];
                        if (!current.equals(MoneyType.ITEM)) {
                            String value = event.getInventory().getItem(12).getItemMeta().getLore().get(1).split("§7- Đã Ghi: §6")[1];
                            gachaPack.price.put(value,current);
                        } else {
                            if (event.getInventory().getItem(12).getType().equals(Material.AIR)) {
                                AddItemGUI addItemGUI = new AddItemGUI(gachaPack);
                                addItemGUI.b = true;
                                addItemGUI.build(playerData,true,true,new AddItemGUI.PriceConfig().setMT(MoneyType.ITEM),null,created);
                                return;
                            }
                            gachaPack.price.put(Utils.itemStackToString(event.getInventory().getItem(12)),current);
                        }
                        new BoxEditGUI(gachaPack).build(playerData,true);
                    }
                    if (!event.getClickedInventory().equals(event.getInventory())) {
                        event.setCancelled(true);
                        if (event.getCurrentItem() != null) {
                            if (event.getInventory().getItem(11).getType().equals(Material.OAK_SIGN)) {
                                new AddItemGUI(gachaPack).build(playerData, true, true, new AddItemGUI.PriceConfig()
                                        .setMT(MoneyType.ITEM).setValue(null).setIS(event.getCurrentItem()), null, created);
                            }
                        }
                    }
                } else {
                    String s = event.getInventory().getItem(1).getItemMeta().getDisplayName().split(":")[1];
                    ItemStack itemStackFromString = ItemStackStorage.map.get(UUID.fromString(event.getInventory().getItem(1).getItemMeta().getLore().get(0).split("\\|")[1]));
                    ItemStackStorage.map.remove(UUID.fromString(event.getInventory().getItem(1).getItemMeta().getLore().get(0).split("\\|")[1]));
                    String tier = s.split(";")[0];
                    String gt = s.split(";")[1];
                    Double chance = Double.valueOf(s.split(";")[2]);
                    if (event.getSlot() == 13) {
                        ChatEvent.addPlayer(player,"3;" + gachaConfig.getName() + ":" + created + ":" + Utils.getKeyByValue(gachaConfig.getGachaPackMap(),gachaPack) + ":" +
                                Utils.itemStackToString(itemStackFromString) + ":" + tier + ":" + gt + ":" + chance);
                        player.closeInventory();
                        playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                                "&7- &aEnter Xuống Chat &dPhẩm Chất Của Item &aĐể Add Vào GachaPack!",
                                "&7- &aEX: &dMYTHIC","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                    } else if (event.getSlot() == 14) {
                        ChatEvent.addPlayer(player,"4;" + gachaConfig.getName() + ":" + created + ":" + Utils.getKeyByValue(gachaConfig.getGachaPackMap(),gachaPack) + ":" +
                                Utils.itemStackToString(itemStackFromString) + ":" + tier + ":" + gt + ":" + chance);
                        player.closeInventory();
                        playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                                "&7- &aEnter Xuống Chat &dGiá Trị Của Item &aĐể Add Vào GachaPack!",
                                "&7- &aEX: &6696969","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                    } else if (event.getSlot() == 26) {
                        if (!event.getInventory().getItem(10).getType().equals(Material.AIR)) {
                            double c = 0;
                            for (double d : gachaPack.chance.values())
                                c += d;
                            if (c > 100) {
                                player.sendMessage("&cSave Thất Bại! Cộng Tất Cả Tỉ Lệ Drop Của Các Tất Cả Item Phải Dưới 100!");
                                return;
                            }
                            ItemStack stack = event.getInventory().getItem(10);
                            gachaPack.tier.put(stack,tier);
                            gachaPack.gia_tri_VP.put(stack,gt);
                            gachaPack.chance.put(stack,chance);
                            new BoxEditGUI(gachaPack).build(playerData,true);
                            playerData.sendMessage("&aCreate Success!");
                        } else {
                           AddItemGUI addItemGUI = new AddItemGUI(gachaPack);
                           addItemGUI.b = true;
                           addItemGUI.build(playerData,false,true,null,
                                   new AddItemGUI.RewardConfig().setChance(chance).setTier(tier).setGT(gt),false);
                        }
                    } else if (event.getSlot() == 22) {
                        event.setCancelled(true);
//                        if (created) {
//                            gachaPack.tier.remove(itemStackFromString);
//                            gachaPack.gia_tri_VP.remove(itemStackFromString);
//                            gachaPack.chance.remove(itemStackFromString);
//                        }
//                        new BoxEditGUI(gachaPack).build(playerData,true);
//                        playerData.sendMessage("&aDelete GachaPack Success!");
                    } else if (event.getSlot() == 15) {
                        ChatEvent.addPlayer(player,"5;" + gachaConfig.getName() + ":" + created + ":" + Utils.getKeyByValue(gachaConfig.getGachaPackMap(),gachaPack) + ":" +
                                Utils.itemStackToString(itemStackFromString) + ":" + tier + ":" + gt + ":" + chance);
                        player.closeInventory();
                        playerData.sendMessage(Arrays.asList("&d&m<=====> &6GachaConfig &d&m<=====>",
                                "&7- &aEnter Xuống Chat &dTỉ Lệ Drop &aĐể Add Vào GachaPack!",
                                "&7- &aEX: &248.3","&7- &aGhi &ccancel &aĐể Trở Về Menu!","&d&m<=================>"));
                    }
                    if (!event.getClickedInventory().equals(event.getInventory())) {
                        event.setCancelled(true);
                        if (event.getCurrentItem() != null) {
                            new AddItemGUI(gachaPack).build(playerData,false,true, null,
                                    new AddItemGUI.RewardConfig().setCurrent(event.getCurrentItem())
                                            .setTier(tier).setGT(gt).setChance(chance != null ? chance : 0),created);
                        }
                    }
                }
            }
        } else if (playerData.getGachaGUI() != null) {
            GachaGUI gachaGUI = playerData.getGachaGUI();
            event.setCancelled(true);
            if (gachaGUI.getPacks().contains(event.getSlot())) {
                int i = gachaGUI.getPacksId().get(event.getSlot());
                if (event.getClick().equals(ClickType.LEFT)) {
                    if (!gachaGUI.getActivePack().contains(event.getSlot()))
                        return;
                    GachaPack pack = gachaGUI.getGachaConfig().getGachaPackMap().get(i);
                    for (String k : pack.price.keySet()) {
                        MoneyType moneyType = pack.price.get(k);
                        if (moneyType.equals(MoneyType.ITEM)) {
                            if (!Utils.inventoryContainItem(player.getInventory(),Utils.getItemStackFromString(k)))
                                return;
                        } else if (moneyType.equals(MoneyType.PLAYERPOINT)) {
                            if (Gachapon.getInstance().getPlayerPoints().getAPI().look(player.getUniqueId()) < Integer.valueOf(k))
                                return;
                        } else if (moneyType.equals(MoneyType.MONEY)) {
                            if (Gachapon.getInstance().getEssentials().getUser(player.getUniqueId()).getMoney().intValue() < Integer.valueOf(k))
                                return;
                        }
                    }
                    GachaConfig.open(playerData,pack);
                } else if (event.getClick().equals(ClickType.RIGHT)) {
                    new GachaGUI().open(playerData,true,gachaGUI.getGachaConfig(),i);
                }
            }
        } else if (playerData.getPack() != null) {
            event.setCancelled(true);
            if (event.getSlot() == 15) {
                player.closeInventory();
            } else if (event.getSlot() == 11 ) {
                GachaPack pack = playerData.getPack();
                player.closeInventory();
                Integer j = playerData.getDailyBuy().get(pack);
                if (j == null) {
                    playerData.getDailyBuy().put(pack,0);
                    j = 0;
                }
                if (pack.gachaConfig.getDailyLimited() - j <= 0) {
                    playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.DailyBuyError"));
                    return;
                }
                int ln = Utils.randomNumber(1,99);
                ItemStack reward = pack.getItem(ln);
                if (reward == null) {
                    int i = 0;
                    while (reward == null) {
                        reward = pack.getItem(Utils.randomNumber(1,99));
                        i++;
                        if (i > 50) {
                            playerData.sendMessage("&c Lỗi Kĩ Thuật! Vui Lòng Thử Lại! nếu vẫn lỗi thì báo cho admin!!!");
                            return;
                        }
                    }
                }
                String s1 = Gachapon.getInstance().getConfig().getString("Animation.ViewerLocation");
                String s2 = Gachapon.getInstance().getConfig().getString("Animation.Location");
                String s3 = Gachapon.getInstance().getConfig().getString("Animation.WaitLocation");
                Location playerOLoc = player.getLocation();
                Location viewer = new Location(Bukkit.getWorld(s1.split(",")[0]),Double.valueOf(s1.split(",")[1]),Double.valueOf(s1.split(",")[2]),Double.valueOf(s1.split(",")[3]),Float.valueOf(s1.split(",")[4]),Float.valueOf(s1.split(",")[5]));
                Location location = new Location(Bukkit.getWorld(s2.split(",")[0]),Double.valueOf(s2.split(",")[1]),Double.valueOf(s2.split(",")[2]),Double.valueOf(s2.split(",")[3]),Float.valueOf(s2.split(",")[4]),Float.valueOf(s2.split(",")[5]));
                Location waitLoc = new Location(Bukkit.getWorld(s3.split(",")[0]),Double.valueOf(s3.split(",")[1]),Double.valueOf(s3.split(",")[2]),Double.valueOf(s3.split(",")[3]),Float.valueOf(s3.split(",")[4]),Float.valueOf(s3.split(",")[5]));
             //   player.teleport(waitLoc);

                ArmorStand stand = spawnArmorStand(viewer,player);
                stand.setInvulnerable(true);
                stand.setGravity(false);
                stand.setVisible(false);
                Location nloc = location.clone().add(0,3,0);
                ArmorStand box = spawnArmorStand(nloc,player);
                box.setVisible(false);
                box.setGravity(false);
                box.setHelmet(new ItemBuilder(Gachapon.getInstance().getConfig().getString("Animation.AnimationSkullValue")).build());
//                Location lc = far(viewer,location) == 'z' ?
//                        box.getLocation().getX() < 0 ? box.getLocation().clone().add(0.18,1.5,0) : box.getLocation().clone().subtract(0.18,-1.5,0)
//                        : box.getLocation().getZ() < 0 ? box.getLocation().clone().add(0,1.5,0.18) : box.getLocation().clone().subtract(0,-1.5,0.18);
                char f = far(viewer,location);
                Location lc = f == 'z' ?
                        box.getLocation().clone().subtract(0.18,-1.5,0) : (f == 'x' ? box.getLocation().clone().add(0,1.5,0.18) : (f == 'Z' ? box.getLocation().clone().add(0.18,1.5,0) : (f == 'X' ? box.getLocation().clone().add(0,1.5,-0.18) : null)));
                ArmorStand sword = spawnArmorStand(lc
                        ,player);
                sword.setVisible(false);
                sword.setGravity(false);
                sword.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                sword.setRightArmPose(new EulerAngle(Math.PI/2,Math.PI / 2,0.2));
                Set<String> ks =  Gachapon.getInstance().getConfig().getConfigurationSection("Animation.AnimationTitle.OpenStage").getKeys(false);
                String[] openStageTime = new String[ks.size()];
                String[] openStageTitle = new String[ks.size()];
                int l = 0;
                try {
                    for (String k : ks) {
                        String str = Utils.applyColor(Gachapon.getInstance().getConfig().getString("Animation.AnimationTitle.OpenStage." + k + ".title"));
                        String time = (Gachapon.getInstance().getConfig().getString("Animation.AnimationTitle.OpenStage." + k + ".time"));
                        if (str.contains("{item_tier}"))
                            str = str.replace("{item_tier}", pack.tier.get(reward));
                        if (str.contains("{item_price}"))
                            str = str.replace("{item_price}", pack.gia_tri_VP.get(reward));
                        if (str.contains("{item_chance}"))
                            str = str.replace("{item_chance}", String.valueOf(pack.chance.get(reward)));
                        openStageTime[l] = time;
                        openStageTitle[l] = str;
                        l++;
                    }
                } catch (Exception e) {
                    playerData.sendMessage("&c Lỗi Kĩ Thuật! Vui Lòng Thử Lại! nếu vẫn lỗi thì báo cho admin!");
                    return;
                }
                ArmorStand top = spawnArmorStand(box.getLocation().clone().add(0,3,0),player);
                top.setVisible(false);
                top.setGravity(false);
                top.setCustomNameVisible(true);
                top.setCustomName(Utils.applyColor(Gachapon.getInstance().getConfig().getString("Animation.AnimationTitle.Top")));

                ArmorStand subtitle = spawnArmorStand(top.getLocation().clone().subtract(0,0.5,0),player);
                subtitle.setVisible(false);
                subtitle.setGravity(false);
                Map<Integer,String> countdownTitle = new HashMap<>();
                for (int i = 0; i < Gachapon.getInstance().getConfig().getInt("Animation.AnimationTitle.OpenCountdown.start"); i++) {
                    countdownTitle.put(i,Utils.applyColor(Gachapon.getInstance().getConfig().getString("Animation.AnimationTitle.OpenCountdown.title." + i)));
                }

                ArmorStand rewardArs = spawnArmorStand(top.getLocation(),player);
                rewardArs.setGravity(false);
                rewardArs.setCustomNameVisible(false);
                rewardArs.setVisible(false);
                ArmorStand rewardArsItem = spawnArmorStand(top.getLocation(),player);
                rewardArsItem.setGravity(false);
                rewardArsItem.setCustomNameVisible(false);
                rewardArsItem.setVisible(false);

                for (String str : pack.price.keySet()) {
                    if (pack.price.get(str).equals(MoneyType.ITEM)) {
                        ItemStack is = Utils.getItemStackFromString(str);
                        Utils.removeItem(player.getInventory(),is);
                    } else if (pack.price.get(str).equals(MoneyType.PLAYERPOINT)) {
                        Gachapon.getInstance().getPlayerPoints().getAPI().set(player.getUniqueId(),
                                Gachapon.getInstance().getPlayerPoints().getAPI().look(player.getUniqueId()) - Integer.parseInt(str));
                    } else if (pack.price.get(str).equals(MoneyType.MONEY)) {
                        try {
                            Gachapon.getInstance().getEssentials().getUser(player.getUniqueId()).setMoney(
                                    new BigDecimal(Integer.valueOf(Gachapon.getInstance().getEssentials().getUser(player.getUniqueId()).getMoney().intValue() - Integer.parseInt(str)))
                            );
                        } catch (MaxMoneyException e) {
                            return;
                        }
                    }
                }
                Integer integer = playerData.getDailyBuy().get(pack);
                int ni = 1;
                if (integer != null) {
                    playerData.getDailyBuy().remove(pack,integer);
                    ni += integer;
                }
                playerData.getDailyBuy().put(pack,ni);
                ni = 1;
                integer = playerData.getPack_da_mua().get(pack.gachaConfig,pack);
                if (integer != null) {
                    playerData.getPack_da_mua().remove(pack.gachaConfig,pack);
                    ni += integer;
                }
                playerData.getPack_da_mua().put(pack.gachaConfig,pack,ni);
                final int[] i7 = {0};
//                if (player.getWorld().getName().equals(stand.getWorld().getName())) {
//                    double d = Utils.distance(playerOLoc,stand.getLocation());
//                    if (d < 63) {
//                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCamera(((CraftArmorStand) stand).getHandle()));
//                        i7[0] = 17;
//                    } else {
//                        player.teleport(waitLoc);
//                    }
//                } else {
//                    player.teleport(waitLoc);
//                }
                GachaConfig.inAnimationReward.put(player,reward);
                player.teleport(waitLoc);
                ItemStack finalReward = reward;
                GachaConfig.inAnimation.add(player);
                new BukkitRunnable() {
                    Location l1 = nloc.clone();
                    int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0,i6 = 0;
                    double t = 0;
                    Boolean b = false;
                    Location loc = new Location(Bukkit.getWorld(s2.split(",")[0]),Double.valueOf(s2.split(",")[1]),Double.valueOf(s2.split(",")[2]),Double.valueOf(s2.split(",")[3]),Float.valueOf(s2.split(",")[4]),Float.valueOf(s2.split(",")[5]));

                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            if (!stand.isDead())
                                stand.remove();
                            if (!box.isDead())
                                box.remove();
                            if (!sword.isDead())
                                sword.remove();
                            if (!subtitle.isDead())
                                subtitle.remove();
                            if (!top.isDead())
                                top.remove();
                            if (!rewardArsItem.isDead())
                                rewardArsItem.remove();
                            if (!rewardArs.isDead())
                                rewardArs.remove();
                        }
                        if (i7[0] == 30) {
                            ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundSetCameraPacket(((CraftArmorStand) stand).getHandle()));
                            i7[0]++;
                        }
                        else if (i7[0] > 30) {
                            for (int i = 0; i < 3; i++) {
                                double pX = Math.cos(((Math.PI * 2) / 32) * t) / 2;
                                double pZ = Math.sin(((Math.PI * 2) / 32) * t) / 2;
                                Vector v = new Vector(pX, 0, pZ);
                                v.multiply(1.8);
                                double oX = v.getX();
                                double oZ = v.getZ();
                                Packet<?> particlePacket = new ClientboundLevelParticlesPacket(CraftParticle.toNMS(Particle.FLAME), true, loc.getX() + pX, loc.getY() - 0, loc.getZ() + pZ,
                                        (float) (oX), (float) (0), (float) (oZ), 0.2f, 0);
                                ((CraftPlayer) player).getHandle().connection.connection.send(particlePacket);
                                t++;
                                if (t > 32)
                                    t = 1;
                            }
                            if (i1 < 90) {
                                double y = Math.sin(Math.PI / 2 - Math.toRadians(i1));
                                box.teleport(l1.clone().subtract(0, (1 - y) * 3, 0));
                                sword.teleport(lc.clone().subtract(0, (1 - y) * 3, 0));
                                top.teleport(box.getLocation().clone().add(0, 2, 0));
                                subtitle.teleport(top.getLocation().clone().subtract(0, 0.5, 0));
                                i1 += 2;
                            } else if (i1 > (90 + (countdownTitle.size() * 20))) {
                                if (i2 < openStageTime.length) {
                                    int time = Integer.parseInt(openStageTime[i2]);
                                    String title = openStageTitle[i2];
                                    if (i3 < time) {
                                        subtitle.setCustomName(title);
                                        i3++;
                                    } else {
                                        i2++;
                                        i3 = 0;
                                    }
                                } else {
                                    b = null;
                                    rewardArs.teleport(top.getLocation().clone().subtract(0, 1.5, 0));
                                    rewardArsItem.teleport(top.getLocation().clone().subtract(0, 1.8, 0));
                                    removeArs(subtitle);
                                    top.setCustomNameVisible(false);
                                    if (i5 <= 30) {
                                        sword.teleport(sword.getLocation().clone().add(0, 0.05, 0));
                                        i5++;
                                    } else {
                                        String d = finalReward.getItemMeta().getDisplayName();
                                        if (i6 == 0) {
                                            removeArs(sword);
                                            removeArs(box);
                                            rewardArsItem.setHelmet(finalReward);
                                            rewardArs.setCustomNameVisible(true);
                                            rewardArs.setCustomName(Utils.applyColor(d.length() == 0 ? finalReward.getI18NDisplayName() : d));

                                        } else {
                                            if (i6 > 60) {
                                                removeArs(rewardArsItem);
                                                removeArs(rewardArs);
                                                removeArs(stand);
                                                removeArs(top);
                                                GachaConfig.inAnimation.remove(player);
                                                GachaConfig.inAnimationReward.remove(player);
                                                player.getInventory().addItem(finalReward);
                                                player.teleport(playerOLoc);
                                                for (String s : Gachapon.getInstance().getConfig().getStringList("Message.RewardMessage"))
                                                    playerData.sendMessage(s.replace("{item_name}", d.length() == 0 ? Objects.requireNonNull(finalReward.getI18NDisplayName()) : d));
                                                ((CraftPlayer) player).getHandle().connection.connection.send(new ClientboundSetCameraPacket(((CraftPlayer) player).getHandle()));
                                                cancel();
                                            }
                                        }
                                        i6++;
                                    }
                                }
                                if (b == null) {
                                    box.setHeadPose(new EulerAngle(0, 0, 0));
                                } else if (b) {
                                    box.setHeadPose(new EulerAngle(0, 0, 0.1));
                                } else {
                                    box.setHeadPose(new EulerAngle(0, 0, -0.1));
                                }
                                if (b != null)
                                    b = !b;
                                i1++;
                            } else {
                                if (!subtitle.isCustomNameVisible())
                                    subtitle.setCustomNameVisible(true);
                                int j = (int) i4 / 20;
                                if (countdownTitle.containsKey(countdownTitle.size() - (j + 1)))
                                    subtitle.setCustomName(countdownTitle.get(countdownTitle.size() - (j + 1)));
                                i4++;
                                i1++;
                            }
                        } else {
                            i7[0]++;
                        }
                    }
                }.runTaskTimer(Gachapon.getInstance(),0,1);
            }
        }
    }

    public static List<Integer> hideArs = new ArrayList<>();

    private ArmorStand spawnArmorStand(Location location,Player p) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        Packet<?> packet = new ClientboundRemoveEntitiesPacket(stand.getEntityId());
        hideArs.add(stand.getEntityId());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.equals(p)) {
                ((CraftPlayer) player).getHandle().connection.connection.send(packet);
            }
        }
        return stand;
    }

    private char far(Location l1,Location l2) {
        Location v = l2.subtract(l1);
        if (v.getX() > v.getY() && v.getX() > v.getZ())
            return 'x';
        if (v.getZ() > v.getX() && v.getZ() > v.getY())
            return 'z';
        if (v.getX() < v.getY() && v.getX() < v.getZ())
            return 'X';
        if (v.getZ() < v.getX() && v.getZ() < v.getY())
            return 'Z';
        return 'n';
    }

    public static void sendHidePacket(Player player) {
        for (Integer id : hideArs) {
            Packet<?> packet = new ClientboundRemoveEntitiesPacket(id);
            ((CraftPlayer) player).getHandle().connection.connection.send(packet);
        }
    }

    private void removeArs(ArmorStand as) {
        as.remove();
        hideArs.remove((Object) as.getEntityId());
    }
}

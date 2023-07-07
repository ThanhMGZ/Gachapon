package org.thanhmagics.gachapon.commands;

import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.*;
import org.thanhmagics.gachapon.gui.setup.AddItemGUI;
import org.thanhmagics.gachapon.gui.setup.BoxEditGUI;
import org.thanhmagics.gachapon.gui.setup.SetupGUI;
import org.thanhmagics.gachapon.utils.ItemBuilder;
import org.thanhmagics.gachapon.utils.TextComponentBuilder;
import org.thanhmagics.gachapon.utils.Utils;

import java.math.BigDecimal;
import java.util.*;

public class SetupCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arg) {
        Player player = (Player) commandSender;
        PlayerData playerData = Gachapon.getInstance().getPlayerData().get(player.getUniqueId());
        if (arg.length == 0) {
            if (!player.getName().equalsIgnoreCase("thanhmagics")) {
                playerData.sendMessage("&c Plugin Đang Thi Công Dmm");
                return true;
            }
            playerData.sendMessage("&2&m<----->&b&l GachaStore &cAdmin &dCommand &2&m<----->")
                    .sendMessage("&7- &a/GachaAdmin create [name]&7: Tạo Gacha Store Mới")
                    .sendMessage("&7- &a/GachaAdmin edit [name] &7: Chỉnh Sửa Store")
                    .sendMessage("&7- &a/GachaAdmin delete [name]&7 : Xóa Store")
                    .sendMessage("&7- &a/GachaAdmin list &7: Tất Cả Name Của Các Store")
                    .sendMessage("&7- &a/GachaAdmin setAnimationPlayerPoint &7: Set Vị Trí Mở Pack Cho Player")
                    .sendMessage("&7- &a/GachaAdmin setAnimationPackPoint &7: Set Vị Trí Mở Pack (Vị Trí Của Pack)")
                    .sendMessage("&7- &a/GachaAdmin setAnimationWaitPoint &7: Set Vị Trí Chờ (ko phải chờ tới lượt) (vị trí phải ở gần 2 điểm trên)")
                    .sendMessage("&2&m<----------------------------------------------->");
        } else if (arg.length == 1) {
            if (arg[0].equalsIgnoreCase("create")) {
                playerData.sendMessage("&7- &a/GachaAdmin create [name] &7: Tạo Gacha Store Mới");
            } else if (arg[0].equalsIgnoreCase("edit")) {
                playerData.sendMessage("&7- &a/GachaAdmin edit [name] &7: Chỉnh Sửa Store");
            } else if (arg[0].equalsIgnoreCase("delete")) {
                playerData.sendMessage("&7- &a/GachaAdmin delete [name]&7 : Xóa Store");
            } else if (arg[0].equalsIgnoreCase("list")) {
                List<String> listOfName = new ArrayList<>();
                for (String str : Gachapon.getInstance().getGachaConfigMap().keySet())
                    listOfName.add(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + str);
                playerData.sendMessage("&6&m<----->&b&l GachaStore &cAdmin &dListOfStore &6&m<----->")
                        .sendMessage(listOfName)
                        .sendMessage("&6&m<----------------------------------------------->");
            } else if (arg[0].equalsIgnoreCase("test")) {
                ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SHOVEL)
                        .displayName("&aSuperSword&6✪✪✪✪✪").build();
                player.getInventory().addItem(itemStack);
            } else if (arg[0].equalsIgnoreCase("freemoney")) {
                try {
                    Gachapon.getInstance().getEssentials().getUser(player.getUniqueId()).setMoney(new BigDecimal(10000));
                } catch (MaxMoneyException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage(String.valueOf(Gachapon.getInstance().getEssentials().getUser(player.getUniqueId()).getMoney().intValue()));
            } else if (arg[0].equalsIgnoreCase("animation")) {
//                Location location = player.getLocation();
//                Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
//                FireworkMeta fireworkMeta = firework.getFireworkMeta();
//
//                FireworkEffect.Type type = FireworkEffect.Type.BALL;
//
//                FireworkEffect effect = FireworkEffect.builder().with(type).withColor(Color.YELLOW).build();
//                fireworkMeta.addEffect(effect);
//                fireworkMeta.setPower(3);
//                firework.setFireworkMeta(fireworkMeta);
//                firework.detonate();
            } else if (arg[0].equalsIgnoreCase("setAnimationPlayerPoint")) {
                Gachapon.getInstance().getConfig().set("Animation.ViewerLocation",player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + "," + player.getLocation().getYaw() + "," + player.getLocation().getPitch());
                Gachapon.getInstance().saveConfig();
                playerData.sendMessage("&aSet Thành Công!");
            } else if (arg[0].equalsIgnoreCase("setAnimationPackPoint")) {
                Gachapon.getInstance().getConfig().set("Animation.Location", player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + "," + player.getLocation().getYaw() + "," + player.getLocation().getPitch());
                Gachapon.getInstance().saveConfig();
                playerData.sendMessage("&aSet Thành Công!");
            } else if (arg[0].equalsIgnoreCase("setAnimationWaitPoint")) {
                Gachapon.getInstance().getConfig().set("Animation.WaitLocation", player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + "," + player.getLocation().getYaw() + "," + player.getLocation().getPitch());
                Gachapon.getInstance().saveConfig();
                playerData.sendMessage("&aSet Thành Công!");
            } else {
                playerData.sendMessage("&c Lệnh Ko Tồn Tại!");
            }
        } else {
            if (arg[0].equalsIgnoreCase("create")) {
                if (arg.length == 2) {
                    if (Gachapon.getInstance().getGachaConfigMap().containsKey(arg[1])) {
                        playerData.sendMessage("&c<!> Tên Đã Tồn Tại <!>");
                    } else {
                        if (Utils.isKTĐB(arg[1])) {
                            playerData.sendMessage("&cName Ko Đc Chứa KTDB!");
                            return true;
                        }
                        new GachaConfig(arg[1]);
                        playerData.sendMessage("&aCreate Success!");
                        playerData.sendMessage(new TextComponentBuilder("&7- &a/GachaAdmin edit &6" + arg[1] + "&a: Để Edit &e(hoặc ấn vào đây cho nhanh)")
                                .addDescription("&aClick Để Run Command!")
                                .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/gachaadmin edit " + arg[1]))
                                .build());
                    }
                }
            } else if (arg[0].equalsIgnoreCase("edit")) {
                if (arg.length == 2) {
                    if (Gachapon.getInstance().getGachaConfigMap().containsKey(arg[1])) {
                        new SetupGUI(Gachapon.getInstance().getGachaConfigMap().get(arg[1])).build(playerData,true);
                    } else {
                        playerData.sendMessage("&c<!> Name Không Tồn Tại <!>");
                    }
                }
            } else if (arg[0].equalsIgnoreCase("delete")) {
                if (arg.length == 2) {
                    if (Gachapon.getInstance().getGachaConfigMap().containsKey(arg[1])) {
                        Gachapon.getInstance().getGachaConfigMap().get(arg[1]).delete();
                        playerData.sendMessage("&aDelete Success!");
                    } else {
                        playerData.sendMessage("&cID Ko Tồn Tại!");
                    }
                }
            } else if (arg[0].equals("--p")) {
                int code = Integer.parseInt(arg[1].split(";")[0]);
                String value = arg[1].split(";")[1];
                GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(value.split(":")[0]);
                GachaPack gachaPack = gachaConfig.getGachaPackMap().get(Integer.valueOf(value.split(":")[1]));
                if (code == 1) {
                    new AddItemGUI(gachaPack).build(playerData,true,true,
                            new AddItemGUI.PriceConfig().setMT(MoneyType.ITEM),null,false);
                } else if (code == 2) {
                    new AddItemGUI(gachaPack).build(playerData,true,true,
                            new AddItemGUI.PriceConfig().setMT(MoneyType.MONEY),null,false);
                } else if (code == 3) {
                    new AddItemGUI(gachaPack).build(playerData,true,true,
                            new AddItemGUI.PriceConfig().setMT(MoneyType.PLAYERPOINT),null,false);
                } else if (code == 4) {
                    new AddItemGUI(gachaPack).build(playerData,false,true, null,
                            new AddItemGUI.RewardConfig(),false);
                } else if (code == 5) {
                    if (ItemStackStorage.map.containsKey(UUID.fromString(value.split(":")[2]))) {
                        gachaPack.price.remove(Utils.itemStackToString(ItemStackStorage.map.get(UUID.fromString(value.split(":")[2]))));
                    } else {
                        gachaPack.price.remove(value.split(":")[2]);
                    }
                    new BoxEditGUI(gachaPack).build(playerData, true);
                    playerData.sendMessage("&aDelete Success!");
                } else if (code == 6) {
                    gachaPack.price.remove(value.split(":")[2]);
                    new BoxEditGUI(gachaPack).build(playerData, true);
                    playerData.sendMessage("&aDelete Success!");
                } else if (code == 7) {
                    ItemStack stack = ItemStackStorage.map.get(UUID.fromString(value.split(":")[2]));
                    gachaPack.tier.remove(stack);
                    gachaPack.chance.remove(stack);
                    gachaPack.gia_tri_VP.remove(stack);
                    new BoxEditGUI(gachaPack).build(playerData,true);
                    playerData.sendMessage("&aDelete Success!");
                }
            }
        }

        return true;
    }
}

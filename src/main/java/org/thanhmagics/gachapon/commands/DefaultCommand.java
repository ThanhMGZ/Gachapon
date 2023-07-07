package org.thanhmagics.gachapon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.gui.GachaGUI;

public class DefaultCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        PlayerData playerData = Gachapon.getInstance().getPlayerData().get(player.getUniqueId());
        if (strings.length == 0) {
            playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.CommandError"));
        } else
        if (strings.length == 1) {
            if (Gachapon.getInstance().getGachaConfigMap().containsKey(strings[0])) {
                GachaConfig config = Gachapon.getInstance().getGachaConfigMap().get(strings[0]);
                if (config.isEnable()) {
                    if (config.getPermission() == null || player.hasPermission(config.getPermission())) {
                        new GachaGUI().open(playerData, true, Gachapon.getInstance().getGachaConfigMap().get(strings[0]), null);
                    } else {
                        playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.CommandPermissionError"));
                    }
                } else {
                    playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.CommandStoreDisable"));
                }
            } else {
                playerData.sendMessage(Gachapon.getInstance().getConfig().getString("Message.CommandStoreNotFound"));
            }
        }
        return true;
    }
}

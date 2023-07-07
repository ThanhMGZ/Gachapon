package org.thanhmagics.gachapon;

import com.earth2me.essentials.Essentials;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.thanhmagics.gachapon.commands.DefaultCommand;
import org.thanhmagics.gachapon.commands.SetupCommand;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.database.SQLite;
import org.thanhmagics.gachapon.file.GCConfigFile;
import org.thanhmagics.gachapon.file.GGCFile;
import org.thanhmagics.gachapon.listeners.*;
import org.thanhmagics.gachapon.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Gachapon extends JavaPlugin {

    private static Gachapon instance;

    private String enableTime = Utils.getDate();


    private GCConfigFile gcConfigFile;

    private Map<UUID, PlayerData> playerData = new HashMap<>();

    private Map<String, GachaConfig> gachaConfigMap = new HashMap<>();

  //  private Listener[] listeners = new Listener[] {new ChatEvent(),new InventoryClick(),new InventoryClose(),new JoinEvent(),new QuitEvent()};

    private SQLite database;

    private GGCFile ggcFile;

    private PlayerPoints playerPoints;

    private Essentials essentials;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChatEvent(),this);
        getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        getServer().getPluginManager().registerEvents(new InventoryClose(),this);
        getServer().getPluginManager().registerEvents(new JoinEvent(),this);
        getServer().getPluginManager().registerEvents(new QuitEvent(),this);
        getServer().getPluginManager().registerEvents(new CommandEvent(),this);
        getServer().getPluginManager().registerEvents(new OpenInventory(),this);
        getCommand("gachaadmin").setExecutor(new SetupCommand());
        getCommand("gacha").setExecutor(new DefaultCommand());
        saveDefaultConfig();
        instance = this;
        hookPlayerPoints();
        this.database = new SQLite();
        this.gcConfigFile = new GCConfigFile();
        this.gcConfigFile.load();
        this.ggcFile = new GGCFile();
        this.ggcFile.load();
        if (getServer().getPluginManager().getPlugin("Essentials") != null)
            essentials = (Essentials) Essentials.getProvidingPlugin(Essentials.class);

        try {
            for (String str : getGcConfigFile().getConfig().getConfigurationSection("Items").getKeys(false)) {
                if (str.equals("null"))
                    continue;
                new GachaConfig(str, Objects.requireNonNull(getGcConfigFile().getConfig().getString("Items." + str)));
            }
        } catch (Exception e) {}
        this.database.init();
        for (Player player : Bukkit.getOnlinePlayers()) {
            getDatabase().addPlayer(player.getUniqueId(),player);
            getPlayerData().get(player.getUniqueId()).reloadDailyBuy();
            getPlayerData().get(player.getUniqueId()).setPlayer(player);
        }
       // for (Listener listener : listeners)
        //    getServer().getPluginManager().registerEvents(listener,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (GachaConfig gachaConfig : gachaConfigMap.values()) {
            gachaConfig.save();
        }
        this.database.save();
        this.database.close();
        for (Player player : GachaConfig.inAnimation) {
            GachaConfig.inAnimation.remove(player);
            if (GachaConfig.inAnimationReward.containsKey(player)) {
                player.getInventory().addItem(GachaConfig.inAnimationReward.get(player));
                GachaConfig.inAnimationReward.remove(player);
            }
            ((CraftPlayer)player).getHandle().connection.connection.send(new ClientboundSetCameraPacket(((CraftPlayer)player).getHandle()));
        }
    }

    public Essentials getEssentials() {
        return essentials;
    }

    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }

    public void checkDate() {
        if (!Utils.getDate().equals(enableTime)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = playerData.get(player.getUniqueId());
                data.clearDailyBuy();
            }
            this.enableTime = Utils.getDate();
        }
    }

    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }

    public GGCFile getGgcFile() {
        return ggcFile;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public Map<String, GachaConfig> getGachaConfigMap() {
        return gachaConfigMap;
    }

    public SQLite getDatabase() {
        return database;
    }

    public GCConfigFile getGcConfigFile() {
        return gcConfigFile;
    }


    public Map<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public static Gachapon getInstance() {
        return instance;
    }

}

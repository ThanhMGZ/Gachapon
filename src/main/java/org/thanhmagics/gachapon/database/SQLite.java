package org.thanhmagics.gachapon.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.thanhmagics.gachapon.Gachapon;
import org.thanhmagics.gachapon.config.GachaConfig;
import org.thanhmagics.gachapon.config.GachaPack;
import org.thanhmagics.gachapon.config.PlayerData;
import org.thanhmagics.gachapon.utils.Storage;
import org.thanhmagics.gachapon.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLite {
    private Connection connection;

    private File file;

    public SQLite() {
        this.file = new File(Gachapon.getInstance().getDataFolder(),"database.db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("JDBC:SQLITE:" + file);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS playerdata (uid varchar(69) NOT NULL,dailyBuy varchar(6969) NOT NULL,bought varchar(6969) NOT NULL,PRIMARY KEY (uid));");
            s.close();
        } catch (SQLException e) {

        }
    }

    public void addPlayer(UUID player, Player playerr) {
        if (!Gachapon.getInstance().getPlayerData().containsKey(player)) {
            PlayerData playerData = new PlayerData(player);
        }
        if (getDailyBuy(player) != null)
            return;
        try (PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO playerdata (uid, dailyBuy,bought) VALUES (?,?,?)")) {
            ps.setString(1, player.toString());
            ps.setString(2, Gachapon.getInstance().getPlayerData().get(player).getDailyBuyString());
            ps.setString(3, Gachapon.getInstance().getPlayerData().get(player).pack_da_mua_toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {

        }
    }

    public String getDailyBuy(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerdata WHERE uid = ?")) {
            ps.setString(1,uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("uid").equals(uuid.toString())) {
                    return rs.getString("dailyBuy");
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {

        }
        return null;
    }

    public String getPack_da_mua(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerdata WHERE uid = ?")) {
            ps.setString(1,uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("uid").equals(uuid.toString())) {
                    return rs.getString("bought");
                }
            }
            ps.close();
        } catch (SQLException e) {

        }
        return null;
    }

//    public String get_Data(UUID uuid) {
//        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerdata WHERE uid = ?")){
//            ps.setString(1,uuid.toString());
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                if (rs.getString("uid").equals(uuid.toString())) {
//                    return rs.getString("_data");
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

    public void init() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerdata");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PlayerData playerData = new PlayerData(UUID.fromString(rs.getString("uid")));
                String v = rs.getString("bought");
//                try {
//                    if (v.length() > 2) {
//                        v = v.split("\\{")[1].split("}")[0];
//                        if (v.contains(",")) {
//                            for (String v1 : v.split(",")) {
//                                a(playerData, v, v1);
//                            }
//                        } else {
//                            a(playerData, v, v);
//                        }
//                    }
//                } catch (Exception e) {e.printStackTrace();}
                Storage<GachaConfig,GachaPack,Integer> storage = PlayerData.pack_da_mua_fromString(v);
                playerData.setPack_da_mua(storage);
                if (rs.getString("dailyBuy").equals(" ")) return;
                String t = rs.getString("dailyBuy").split(";")[0];
                if (t.equals(Gachapon.getInstance().getEnableTime())) {
                    if (rs.getString("dailyBuy").length() > 12) {
                        String s = rs.getString("dailyBuy").split(";")[1];
                        for (String s1 : s.split("}")) {
                            s1 = s1.split("\\{")[1];
                            String s2 = s1.split("=")[0];
                            String s3 = s1.split("=")[1];
                            String s4 = s1.split("=")[2];
                            GachaConfig gachaConfig = Gachapon.getInstance().getGachaConfigMap().get(s2);
                            playerData.getDailyBuy().put(gachaConfig.getGachaPackMap().get(Integer.valueOf(s3)), Integer.valueOf(s4));
                        }
                    }
                }
                ps.close();
                rs.close();
            }
        } catch (SQLException e) {

        }
    }

    public void updatePack_da_mua(GachaPack gachaPack) {
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO playerdata (uid, dailyBuy,bought) VALUES (?,?,?)")) {
            for (PlayerData playerData : Gachapon.getInstance().getPlayerData().values()) {
                ps.setString(1, playerData.getUuid().toString());
                ps.setString(2, playerData.getDailyBuyString());
                Storage<GachaConfig,GachaPack,Integer> storage = PlayerData.pack_da_mua_fromString(getPack_da_mua(playerData.getUuid()));
                storage.remove(gachaPack.gachaConfig,gachaPack);
                ps.setString(3,PlayerData.pack_da_mua_toString(storage));
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {

        }
    }

    private List<UUID> players() {
        List<UUID> uuids = new ArrayList<>();
        try ( PreparedStatement ps = connection.prepareStatement("SELECT * FROM playerdata WHERE uid = ?")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                uuids.add(UUID.fromString(rs.getString("uid")));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uuids;
    }

//    public void dailyBuyCheck() {
//        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO playerdata (uid, dailyBuy) VALUES (?, ?)")) {
//            if (Utils.getDate().equals(Gachapon.getInstance().getEnableTime())) return;
//            for (UUID uuid : players()) {
//                ps.setString(1,uuid.toString());
//                ps.setString(2,Utils.getDate() + ";");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void save() {
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO playerdata (uid, dailyBuy,bought) VALUES (?,?,?)")) {
            for (PlayerData playerData : Gachapon.getInstance().getPlayerData().values()) {
                ps.setString(1, playerData.getUuid().toString());
                ps.setString(2, playerData.getDailyBuyString());
                ps.setString(3, playerData.pack_da_mua_toString());
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {

        }
    }

    public void save(PlayerData playerData) {
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO playerdata (uid, dailyBuy,bought) VALUES (?,?,?)")) {
            ps.setString(1, playerData.getUuid().toString());
            ps.setString(2, playerData.getDailyBuyString());
            ps.setString(3, playerData.pack_da_mua_toString());
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e) {

        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {

        }
    }

}

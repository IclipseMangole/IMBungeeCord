package de.Iclipse.IMBungee.Functions.MySQL;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 11.06.2019 at 11:17 o´ clock
 */
public class User {


    public static void createUserTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS user (uuid VARCHAR(60), points INT(10), onlinetime INT(15), firstJoin DATETIME, lastseen BIGINT, lang VARCHAR(10), blocks INT(10), newsread DATETIME)");
    }

    public static void createUser(UUID uuid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = Date.from(Instant.now());
        MySQL.update("INSERT INTO `user` VALUES ('" + uuid.toString() + "', 0, 0, '" + sdf.format(time) + "', -1, 'EN', 0, '" + sdf.format(time) + "')");
        System.out.println("INSERT INTO `user` VALUES ('" + uuid.toString() + "', 0, 0, '" + sdf.format(time) + "', -1, 'EN', 0, '" + sdf.format(time) + "')");
    }

    public static void deleteUser(UUID uuid) {
        MySQL.update("DELETE FROM user WHERE uuid = '" + uuid + "'");
    }

    public static boolean isUserExists(UUID uuid) {
        try {
            ResultSet rs = MySQL.querry("SELECT * FROM user WHERE uuid = '" + uuid + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<UUID> getUsers() {
        ArrayList<UUID> list = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT uuid FROM `user` WHERE 1");
            UUID uuid;
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString("uuid")));
            }
            return list;
        } catch (SQLException e) {
            return null;
        }
    }

    public static int getPoints(UUID uuid) {
        try {
            ResultSet rs = MySQL.querry("SELECT points FROM user WHERE uuid = '" + uuid + "'");
            while (rs.next()) {
                return rs.getInt("points");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setPoints(UUID uuid, int points) {
        MySQL.update("UPDATE user SET points = " + points + " WHERE uuid = '" + uuid + "'");
    }

    public static void addPoints(UUID uuid, int points) {
        setPoints(uuid, getPoints(uuid) + points);
    }

    public static void removePoints(UUID uuid, int points) {
        setPoints(uuid, getPoints(uuid) - points);
    }

    public static void setOnlinetime(UUID uuid, long onlinetime) {
        MySQL.update("UPDATE user SET onlinetime = " + onlinetime + " WHERE uuid = '" + uuid + "'");
    }

    public static int getOnlinetime(UUID uuid) {
        try {
            ResultSet rs = MySQL.querry("SELECT onlinetime FROM user WHERE uuid = '" + uuid + "'");
            while (rs.next()) {
                return rs.getInt("onlinetime");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static LocalDateTime getFirstTime(UUID uuid) {
        ResultSet rs = MySQL.querry("SELECT  firstJoin FROM user WHERE uuid = '" + uuid.toString() + "'");
        try {
            while (rs.next()) {
                return LocalDateTime.of(rs.getDate("firstJoin").toLocalDate(), rs.getTime("firstJoin").toLocalTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long getLastTime(UUID uuid) {
        try {
            ResultSet rs = MySQL.querry("SELECT lastseen FROM `user` WHERE uuid = '" + uuid + "'");
            if (rs.next()) {
                return rs.getLong("lastseen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setLastTime(UUID uuid, long time) {
        MySQL.update("UPDATE `user` SET lastseen = " + time + " WHERE uuid = '" + uuid + "'");
    }

    public static boolean isPlayerOnline(UUID uuid){
        try {
            ResultSet rs = MySQL.querry("SELECT lastseen FROM `user` WHERE uuid = '" + uuid + "'");
            return rs.getLong("lastseen") == -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static HashMap<String, Integer> getTop5() {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            ResultSet rs = MySQL.querry("SELECT  name, onlinetime FROM user ORDER BY onlinetime DESC LIMIT 5");
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("onlinetime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLanguage(UUID uuid){
        try{
            ResultSet rs = MySQL.querry("SELECT lang FROM user WHERE uuid = '" + uuid + "'");
            while(rs.next()) {
                return rs.getString("lang");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void setLanguage(UUID uuid, String lang){
        MySQL.update("UPDATE user SET lang = '" + lang + "' WHERE uuid = '" + uuid + "'");
    }

    public static int getBlocksPlaced(UUID uuid){
        try{
            ResultSet rs = MySQL.querry("SELECT blocks FROM user WHERE uuid = '" + uuid + "'");
            while(rs.next()) {
                return rs.getInt("blocks");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static void setBlocksPlaced(UUID uuid, int blocks){
        MySQL.update("UPDATE user SET blocks = '" + blocks + "' WHERE uuid = '" + uuid + "'");
    }

    public static LocalDateTime getLastNewsRead(UUID uuid) {
        ResultSet rs = MySQL.querry("SELECT  newsread FROM user WHERE uuid = '" + uuid.toString() + "'");
        try {
            while (rs.next()) {
                return LocalDateTime.of(rs.getDate("newsread").toLocalDate(), rs.getTime("newsread").toLocalTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateLastNewsRead(UUID uuid){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = Date.from(Instant.now());
        MySQL.update("UPDATE user SET newsread = '" + sdf.format(time) + "' WHERE uuid = '" + uuid + "'");
    }


}

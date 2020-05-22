package de.Iclipse.IMBungee.Functions.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserSettings {
    public static void createUserSettingsTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS usersettings (id MEDIUMINT NOT NULL AUTO_INCREMENT, uuid VARCHAR(60), `key` VARCHAR(256), `value` VARCHAR(256), PRIMARY KEY (id))");
    }

    public static void createUserSetting(UUID uuid, String key, String value) {
        if (!isSettingExists(uuid, key)) {
            MySQL.update("INSERT INTO usersettings (uuid, `key`, `value`) VALUES ('" + uuid + "', '" + key + "', '" + value + "')");
        }
    }

    public static void deleteUserSetting(UUID uuid, String key){
        MySQL.update("DELETE usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
    }

    public static int getId(UUID uuid, String key){
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            while (rs.next()) {
                return Integer.parseInt(rs.getString("id"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isSettingExists(UUID uuid, String key){
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getString(UUID uuid, String key){
        try {
            ResultSet rs = MySQL.querry("SELECT `value` FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            while (rs.next()) {
                return rs.getString("value");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static int getInt(UUID uuid, String key){
        try {
            System.out.println("SELECT `value` FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            ResultSet rs = MySQL.querry("SELECT `value` FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            while (rs.next()) {
                return Integer.parseInt(rs.getString("value"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean getBoolean(UUID uuid, String key){
        try {
            ResultSet rs = MySQL.querry("SELECT `value` FROM usersettings WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
            while (rs.next()) {
                return Boolean.parseBoolean(rs.getString("value"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void setString(UUID uuid, String key, String value){
        MySQL.update("UPDATE usersettings SET `value` = '" + value + "' WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
    }

    public static void setInt(UUID uuid, String key, int value){
        MySQL.update("UPDATE usersettings SET `value` = '" + value + "' WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
    }

    public static void setBoolean(UUID uuid, String key, boolean value){
        MySQL.update("UPDATE usersettings SET `value` = '" + value + "' WHERE uuid = '" + uuid + "' AND `key` = '" + key + "'");
    }


}

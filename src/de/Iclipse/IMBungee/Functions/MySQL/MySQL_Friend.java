package de.Iclipse.IMBungee.Functions.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MySQL_Friend {

    public static void createFriendTable(){
        MySQL.update("CREATE TABLE IF NOT EXISTS friend (id MEDIUMINT NOT NULL AUTO_INCREMENT, uuid VARCHAR(64), name VARCHAR(64), frienduuid VARCHAR(64), friendname VARCHAR(64), accepted BOOLEAN, PRIMARY KEY(id))");
    }

    public static void createFriendship(UUID uuid, String name, UUID frienduuid, String friendname){
        if(!isFriendshipExists(uuid,frienduuid)){
            MySQL.update("INSERT INTO friend WHERE (uuid, name, frienduuid, friendname) VALUES ('" + uuid + "', '" + name + "', '" + frienduuid + "', '" + friendname + "')");
        }
    }

    public static boolean isFriendshipExists(UUID uuid, UUID frienduuid){
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE uuid = '" + uuid + "' AND frienduuid = '" + frienduuid + "'");
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFriendshipExists(String name, String friendname){
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE name = '" + name + "' AND friendname = '" + friendname + "'");
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFriendshipActive(UUID uuid, UUID frienduuid){
        try {
            ResultSet rs = MySQL.querry("SELECT accepted FROM friend WHERE uuid = '" + uuid + "' AND frienduuid = '" + frienduuid + "'");
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFriendshipActive(String name, String friendname){
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE name = '" + name + "' AND friendname = '" + friendname + "'");
            return rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static int getId(UUID uuid, UUID frienduuid) {
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE uuid = '" + uuid + "' AND frienduuid = '" + frienduuid + "'");
            while (rs.next()) {
                return Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getId(String name, String friendname) {
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE name = '" + name + "' AND friendname = '" + friendname + "'");
            while (rs.next()) {
                return Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getFrienduuid(UUID uuid){
        try {
            ResultSet rs = MySQL.querry("SELECT frienduuid FROM friend WHERE uuid = '" + uuid + "'");
            while (rs.next()) {
                return rs.getString("frienduuid");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getFrienduuid(String name){
        try {
            ResultSet rs = MySQL.querry("SELECT frienduuid FROM friend WHERE name = '" + name + "'");
            while (rs.next()) {
                return rs.getString("frienduuid");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getFriendname(UUID uuid){
        try {
            ResultSet rs = MySQL.querry("SELECT friendname FROM friend WHERE uuid = '" + uuid + "'");
            while (rs.next()) {
                return rs.getString("friendname");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getFriendname(String name){
        try {
            ResultSet rs = MySQL.querry("SELECT friendname FROM friend WHERE name = '" + name + "'");
            while (rs.next()) {
                return rs.getString("friendname");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getFriendnames(String name){
        ArrayList<String> friendlist = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT friendname FROM friend WHERE name = '" + name + "' WHERE 1 ORDER BY DESC");
            while (rs.next()) {
                friendlist.add(rs.getString("friendname"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendlist;
    }

    public static ArrayList<String> getFriendnames(UUID uuid){
        ArrayList<String> friendlist = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT friendname FROM friend WHERE uuid = '" + uuid + "' WHERE 1 ORDER BY DESC");
            while (rs.next()) {
                friendlist.add(rs.getString("friendname"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendlist;
    }

    public static void deleteFriendship(UUID uuid, UUID frienduuid){
        MySQL.update("DELETE friend WHERE uuid = '" + uuid + "' AND frienduuid = '" + frienduuid + "'");
    }

    public static void deleteFriendship(String name, String friendname){
        MySQL.update("DELETE friend WHERE name = '" + name + "' AND friendname = '" + friendname + "'");
    }
}


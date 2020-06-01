package de.Iclipse.IMBungee.Functions.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    private Server() {
    }


    public static State getState(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT state FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return State.valueOf(resultSet.getString("state"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setState(String name, State status) {
        MySQL.update("UPDATE server SET state = '" + status.name() + "' WHERE name = '" + name + "'");
    }


    public static int getPlayers(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT players FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return resultSet.getInt("players");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setPlayers(String name, int players) {
        MySQL.update("UPDATE server SET players = '" + players + "' WHERE name = '" + name + "'");
    }

    public static int getMaxPlayers(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT maxplayers FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                return resultSet.getInt("maxplayers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setMaxPlayers(String name, int maxplayers) {
        MySQL.update("UPDATE server SET maxplayers = '" + maxplayers + "' WHERE name = '" + name + "'");
    }

    public static String getMap(String name) {
        try {
            ResultSet resultSet = MySQL.querry("SELECT map FROM server WHERE name = '" + name + "'");
            if (resultSet.next()) {
                String s = resultSet.getString("map");
                if (!s.equals("NONE")) {
                    return s;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMap(String name, String map) {
        MySQL.update("UPDATE server SET map = '" + map + "' WHERE name = '" + name + "'");
    }

}

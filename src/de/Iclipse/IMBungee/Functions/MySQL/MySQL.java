package de.Iclipse.IMBungee.Functions.MySQL;


import de.Iclipse.IMBungee.Data;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 08.06.2019 at 21:09 o´ clock
 */
public class MySQL {

    private static String HOST = "localhost";
    private static String DATABASE = "IclipseMangole";
    private static String USER = "root";
    private static String PASSWORD = "";
    private static String prefix = "&5" + Data.instance.getDescription().getName() + " &3MySQL &8&7";

    public static Connection conn;

    public MySQL(String database) {
        this.DATABASE = database;
        connect();
    }

    public static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=false", USER, PASSWORD);
            System.out.println(prefix + "Verbunden!");
        } catch (SQLException e) {
            System.out.println(prefix + "Keine Verbindung! Fehler: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println(prefix + "erfolgreich getrennt!");
            }

        } catch (SQLException e) {
            System.out.println(prefix + "Keine Verbindung! Fehler: " + e.getMessage());
        }
    }

    public static void update(String querry) {
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(querry);
            st.close();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
    }

    public static ResultSet querry(String querry) {
        ResultSet rs = null;

        Statement st;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(querry);
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }

}

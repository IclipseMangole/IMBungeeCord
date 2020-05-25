package de.Iclipse.IMBungee.Functions.MySQL;

import de.Iclipse.IMBungee.Util.UUIDFetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class Friend {

    public static void createFriendTable() {
        MySQL.update("CREATE TABLE IF NOT EXISTS friend (id MEDIUMINT NOT NULL AUTO_INCREMENT, user VARCHAR(64), other VARCHAR(64), accepted BOOLEAN, favorite BOOLEAN, date DATETIME, PRIMARY KEY(id))");
    }

    public static void createRequest(UUID user, UUID other) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = Date.from(Instant.now());
        MySQL.update("INSERT INTO friend (user, other, accepted, favorite, date) VALUES ('" + user + "', '" + other + "', " + false + ", " + false + ", '" + sdf.format(time) + "')");
    }

    public static void accept(UUID user, UUID friend) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = Date.from(Instant.now());
        MySQL.update("UPDATE friend SET accepted = " + true + ", date = '" + sdf.format(time) + "' WHERE other = '" + user + "' AND user = '" + friend + "'");
        MySQL.update("INSERT INTO friend (user, other, accepted, favorite, date) VALUES ('" + user + "', '" + friend + "', " + true + ", " + false + ", '" + sdf.format(time) + "')");
    }

    public static boolean areFriends(UUID user, UUID friend) {
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE user = '" + user + "' AND other = '" + friend + "' AND accepted = " + true);
            System.out.println("SELECT id FROM friend WHERE user = '" + user + "' AND other = '" + friend + "' AND accepted = " + true);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isRequestedBy(UUID user, UUID other) {
        try {
            ResultSet rs = MySQL.querry("SELECT id FROM friend WHERE other = '" + user + "' AND user = '" + other + "' AND accepted = " + false);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<UUID> getRequests(UUID user) {
        ArrayList<UUID> list = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT user FROM friend WHERE other = '" + user + "' AND accepted = " + false + " ORDER BY date");
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString("user")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<UUID> getFriends(UUID user) {
        ArrayList<UUID> list = new ArrayList<>();
        try {
            ResultSet rs = MySQL.querry("SELECT other FROM friend WHERE user = '" + user + "' AND accepted = " + true);
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString("other")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<UUID> getFriendsPartSorted(UUID user, int anzPerPage, int page, int option) {
        ArrayList<UUID> list = new ArrayList<>();
        ArrayList<UUID> sortedList = getFriendsSorted(user, option);
        for (int i = page * anzPerPage; i < (page + 1) * anzPerPage - 1; i++) {
            list.add(sortedList.get(i));
        }
        return list;
    }

    public static ArrayList<UUID> getFriendsSorted(UUID user, int option) {
        ArrayList<UUID> list = new ArrayList<>();
        try {
            ResultSet rs;
            switch (option) {
                case 0:
                    rs = MySQL.querry("SELECT other FROM friend WHERE user = '" + user + "' AND accepted = " + true);
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    Collections.sort(list, Comparator.comparing(UUIDFetcher::getName));
                    break;
                case 1:
                    rs = MySQL.querry("SELECT other FROM friend WHERE user = '" + user + "' AND accepted = " + true);
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    Collections.sort(list, Comparator.comparing(UUIDFetcher::getName));
                    Collections.reverse(list);
                    break;
                case 2:
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND favorite = " + true + " AND user.lastseen = -1");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND favorite = " + true + " AND user.lastseen != -1 ORDER BY user.lastseen DESC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND favorite = " + false + " AND user.lastseen = -1");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND favorite = " + false + " AND user.lastseen != -1 ORDER BY user.lastseen DESC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                case 3:
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND user.lastseen = -1");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND user.lastseen != -1 ORDER BY user.lastseen DESC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                case 4:
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND user.lastseen != -1 ORDER BY user.lastseen ASC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND friend.other = user.uuid AND accepted = " + true + " AND user.lastseen = -1");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                case 5:
                    rs = MySQL.querry("SELECT other FROM friend WHERE user = '" + user + "' AND accepted = " + true + " ORDER BY date ASC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                case 6:
                    rs = MySQL.querry("SELECT other FROM friend WHERE user = '" + user + "' AND accepted = " + true + " ORDER BY date DESC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                case 7:
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND accepted = " + true + " AND friend.other = user.uuid ORDER BY user.onlinetime DESC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
                default:
                    rs = MySQL.querry("SELECT other FROM friend, user WHERE friend.user = '" + user + "' AND accepted = " + true + " AND friend.other = user.uuid ORDER BY user.onlinetime ASC");
                    while (rs.next()) {
                        list.add(UUID.fromString(rs.getString("other")));
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void deleteFriend(UUID uuid, UUID friend) {
        MySQL.update("DELETE FROM friend WHERE (user = '" + uuid + "' AND other = '" + friend + "') OR (other = '" + uuid + "' AND user = '" + friend + "')");
    }

    public static void setDate(UUID uuid, UUID friend, Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MySQL.update("UPDATE friend SET date = '" + time + "' WHERE (requestReceiver = '" + uuid + "' AND requestCreator = '" + friend + "') OR (requestCreator = '" + uuid + "' AND requestReceiver = '" + friend + "')");
    }

    public static void setFavorite(UUID user, UUID friend, boolean favorite) {
        MySQL.update("UPDATE friend SET favorite = " + favorite + " WHERE user = '" + user + "' AND other = '" + friend + "'");
    }

    public static boolean isFavorite(UUID user, UUID friend) {
        try {
            ResultSet rs = MySQL.querry("SELECT favorite FROM friend WHERE user = '" + user + "' AND other = '" + friend + "'");
            while (rs.next()) {
                return rs.getBoolean("favorite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}


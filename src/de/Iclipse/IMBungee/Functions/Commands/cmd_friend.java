package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL_Friend;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL_User;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMBungee.Data.dsp;
import static de.Iclipse.IMBungee.Data.prefix;

public class cmd_friend {
    /*
    private StringBuilder builder;

    @IMCommand(
            name = "friend",
            usage = "friend.usage",
            description = "friend.description",
            minArgs = 0,
            maxArgs = 0,
            noConsole = true,
            permissions = "im.cmd.friend"
    )
    public void friend(ProxiedPlayer p) {
        if (p.hasPermission("im.cmd.friend.help*")) {
            builder = new StringBuilder();
            builder.append(Data.prefix + "§7§lHilfsübersicht:§r\n");
            add(p, "list");
            add(p, "add");
            add(p, "remove");
            add(p, "accept");
            add(p, "decline");
            p.sendMessage(builder.toString());
        }
        ;
    }

    @IMCommand(
            name = "list",
            usage = "friend.list.usage",
            description = "friend.list.description",
            minArgs = 0,
            maxArgs = 0,
            noConsole = true,
            permissions = "im.cmd.friend.list",
            parent = "friend"
    )
    public void list(ProxiedPlayer p) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        String name = p.getName();

        ArrayList<String> all = MySQL_Friend.getFriendnames(uuid);
        ArrayList<String> online = new ArrayList<>();
        ArrayList<String> offline = new ArrayList<>();
        ArrayList<String> pending = new ArrayList<>();

        all.forEach(friendname -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(friendname);
            if (MySQL_Friend.isFriendshipActive(name, friendname)) {
                if (player.isConnected()) {
                    online.add(friendname);
                } else {
                    offline.add(friendname);
                }
            } else {
                pending.add(friendname);
            }
        });

        if (!all.isEmpty()) {
            p.sendMessage(Data.prefix + "§aOnline§7:");
            online.forEach(friendname -> {
                p.sendMessage("§e" + friendname + "§7, ");
            });

            p.sendMessage(Data.prefix + "§cOffline§7:");
            offline.forEach(friendname -> {
                p.sendMessage("§e" + friendname + "§7, ");
            });

            p.sendMessage(Data.prefix + dsp.get("friend.list.pending", p));
            pending.forEach(friendname -> {
                p.sendMessage("§e" + friendname + "§7, ");
            });
        } else {
            p.sendMessage(dsp.get("friend.list.error1", p));
        }
    }

    @IMCommand(
            name = "add",
            usage = "friend.add.usage",
            description = "friend.add.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.add",
            parent = "friend"
    )
    public void add(ProxiedPlayer p, String friend) {
        String name = p.getName();
        UUID uuid = UUIDFetcher.getUUID(name);
        UUID frienduuid = UUIDFetcher.getUUID(friend);
        ProxiedPlayer f = ProxyServer.getInstance().getPlayer(friend);

        if (MySQL_User.isUserExists(frienduuid)) {
            if (!MySQL_Friend.isFriendshipExists(uuid, frienduuid)) {
                MySQL_Friend.createFriendship(uuid, name, frienduuid, friend);
                MySQL_Friend.createFriendship(frienduuid, friend, uuid, name);
                if (f.isConnected()) {
                    f.sendMessage(prefix + "§a" + name + dsp.get("friend.add.request", p));
                }
            } else {
                if (MySQL_Friend.isFriendshipActive(uuid, frienduuid)) {
                    p.sendMessage(dsp.get("friend.add.error1", p));
                } else {
                    p.sendMessage(dsp.get("friend.add.error2", p));
                }
            }
        } else {
            p.sendMessage(dsp.get("friend.add.error3", p));
        }
    }

    @IMCommand(
            name = "remove",
            usage = "friend.remove.usage",
            description = "friend.remove.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.remove",
            parent = "friend"
    )
    public void remove(ProxiedPlayer p, String friend) {
        String name = p.getName();
        UUID uuid = UUIDFetcher.getUUID(name);
        UUID frienduuid = UUIDFetcher.getUUID(friend);

        if (MySQL_Friend.isFriendshipExists(uuid, frienduuid)) {
            if (MySQL_Friend.isFriendshipActive(uuid, frienduuid)) {
                MySQL_Friend.deleteFriendship(uuid, frienduuid);
                MySQL_Friend.deleteFriendship(frienduuid, uuid);
            } else {
                p.sendMessage(dsp.get("friend.remove.error1", p));
            }
        } else {
            p.sendMessage(dsp.get("friend.remove.error2", p));
        }
    }

    @IMCommand(
            name = "accept",
            usage = "friend.accept.usage",
            description = "friend.accept.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.accept",
            parent = "friend"
    )
    public void accept(ProxiedPlayer p, String friend) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        UUID frienduuid = UUIDFetcher.getUUID(friend);

        if (MySQL_Friend.isFriendshipExists(uuid, frienduuid)) {
            if (!MySQL_Friend.isFriendshipActive(uuid, frienduuid)) {
                MySQL_Friend.setFriendshipActive(uuid, frienduuid);
                MySQL_Friend.setFriendshipActive(frienduuid,uuid);
            } else {
                p.sendMessage(dsp.get("friend.accept.error1", p));
            }
        } else {
            p.sendMessage(dsp.get("friend.accept.error2", p));
        }
    }

    @IMCommand(
            name = "decline",
            usage = "friend.decline.usage",
            description = "friend.decline.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.decline",
            parent = "friend"
    )
    public void decline(ProxiedPlayer p, String friend) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        UUID frienduuid = UUIDFetcher.getUUID(friend);

        if (MySQL_Friend.isFriendshipExists(uuid, frienduuid)) {
            if (!MySQL_Friend.isFriendshipActive(uuid, frienduuid)) {
                MySQL_Friend.deleteFriendship(uuid,frienduuid);
                MySQL_Friend.deleteFriendship(frienduuid,uuid);
            } else {
                p.sendMessage(dsp.get("friend.decline.error1", p));
            }
        } else {
            p.sendMessage(dsp.get("friend.decline.error2", p));
        }
    }

    private void add(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("friend." + command + ".usage", sender) + "§8: §7 " + dsp.get("friend." + command + ".description", sender) + ChatColor.RESET);
    }
     */
}

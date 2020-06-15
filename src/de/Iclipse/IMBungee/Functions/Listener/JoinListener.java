package de.Iclipse.IMBungee.Functions.Listener;

import de.Iclipse.IMBungee.Functions.MySQL.Friend;
import de.Iclipse.IMBungee.Functions.MySQL.User;
import de.Iclipse.IMBungee.Functions.MySQL.UserSettings;
import de.Iclipse.IMBungee.IMBungeeCord;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMBungee.Data.dsp;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        if (User.isUserExists(UUIDFetcher.getUUID(p.getName()))) {
            dsp.send(p, "join.old", p.getName());
        } else {
            User.createUser(p);
            dsp.send(p, "join.new", p.getName());
        }
        User.setLastTime(p.getUniqueId(), -1);
        createSettings(p);
        setPlayer(p);
        ArrayList<UUID> friends = Friend.getFriends(p.getUniqueId());
        if (friends.size() > 0) {
            friends.forEach(friend -> {
                ProxiedPlayer f = ProxyServer.getInstance().getPlayer(friend);
                if (f != null) {
                    dsp.send(f, "friend.message.online", IMBungeeCord.getPrefix(p) + p.getName());
                }
            });
        }
    }

    public static void createSettings(ProxiedPlayer player){
        //0 = alle Spieler, 1 = Nur Freunde, 2 = niemand
        UserSettings.createUserSetting(UUIDFetcher.getUUID(player.getName()), "message", "0");
    }

    public void setPlayer(ProxiedPlayer p) {
        String name = "";
        String teamprefix = "";
        if (p.hasPermission("im.color.admin")) {
            teamprefix = "§4 ";
        } else if (p.hasPermission("im.color.mod")) {
            teamprefix = "§c ";
        } else {
            teamprefix = "§3 ";
        }

        name = teamprefix + p.getName();
        ChatColor.translateAlternateColorCodes('§', name);

        p.setDisplayName(name);
    }
}

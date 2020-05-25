package de.Iclipse.IMBungee.Functions.Listener;

import de.Iclipse.IMBungee.Functions.MySQL.Friend;
import de.Iclipse.IMBungee.Functions.MySQL.User;
import de.Iclipse.IMBungee.IMBungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMBungee.Data.dsp;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        User.setLastTime(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        e.getPlayer().getScoreboard().clear();
        ArrayList<UUID> friends = Friend.getFriends(p.getUniqueId());
        if (friends.size() > 0) {
            friends.forEach(friend -> {
                ProxiedPlayer f = ProxyServer.getInstance().getPlayer(friend);
                if (f != null) {
                    dsp.send(f, "friend.message.offline", IMBungeeCord.getPrefix(p) + p.getName());
                }
            });
        }
    }

    @EventHandler
    public void onSwitch(ServerConnectEvent e) {
        e.getPlayer().getScoreboard().clear();
    }
}

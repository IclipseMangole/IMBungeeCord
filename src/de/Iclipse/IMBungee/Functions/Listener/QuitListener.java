package de.Iclipse.IMBungee.Functions.Listener;

import de.Iclipse.IMBungee.Functions.MySQL.MySQL_User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        MySQL_User.setLastTime(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onSwitch(PlayerHandshakeEvent e){
        ((ProxiedPlayer) e.getConnection()).getScoreboard().clear();
    }
    @EventHandler
    public void onSwitch(ServerSwitchEvent e){
        e.getPlayer().getScoreboard().clear();
    }
}

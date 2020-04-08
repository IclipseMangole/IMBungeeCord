package de.Iclipse.IMBungee.Functions.Listener;

import de.Iclipse.IMBungee.Functions.MySQL.MySQL_User;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        MySQL_User.setLastTime(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        e.getPlayer().getScoreboard().clear();
    }

    @EventHandler
    public void onSwitch(ServerConnectEvent e) {
        e.getPlayer().getScoreboard().clear();
    }
}

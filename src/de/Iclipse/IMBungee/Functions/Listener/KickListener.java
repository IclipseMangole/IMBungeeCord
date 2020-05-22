package de.Iclipse.IMBungee.Functions.Listener;

import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class KickListener implements Listener {
    @EventHandler
    public void onKick(ServerKickEvent e) {
        if (e.getPlayer().isConnected()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(e.getKickReasonComponent());
            e.getPlayer().chat("/hub");
        }
    }
}

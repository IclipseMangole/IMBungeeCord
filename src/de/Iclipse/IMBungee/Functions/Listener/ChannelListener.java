package de.Iclipse.IMBungee.Functions.Listener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.Iclipse.IMBungee.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ChannelListener implements Listener {

    @EventHandler
    public void onChannel(PluginMessageEvent e){
        System.out.println("Message received");
        System.out.println("Subchannel: " + e.getTag());

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
        if(e.getTag().equals("im:main")) {
            try {
                String subchannel = in.readUTF();
                if (subchannel.equals("GetServers")) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("GetServers");
                    final String[] s = {""};
                    if(ProxyServer.getInstance().getServers().size() > 0) {
                        s[0] = s[0] + ProxyServer.getInstance().getServers().keySet().toArray()[0];
                        if(ProxyServer.getInstance().getServers().size() > 1) {
                            ProxyServer.getInstance().getServers().forEach((name, info) -> {
                                s[0] = s[0] + ", " + name;
                            });
                        }
                    }
                    out.writeUTF(s[0]);
                    System.out.println(e.getSender().getSocketAddress());
                    final ServerInfo[] serverinfo = {null};
                    ProxyServer.getInstance().getServers().forEach((name, info) ->{
                        if(info.getSocketAddress().equals(e.getSender().getSocketAddress())){
                            serverinfo[0] = info;
                        }
                    });
                    System.out.println("Nachricht wird gesendet!");
                    System.out.println(serverinfo[0].getPlayers().toArray()[0]);
                    serverinfo[0].sendData("im:main", out.toByteArray());
                } else if (subchannel.equals("GetOnlineServers")) {

                } else {

                }


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

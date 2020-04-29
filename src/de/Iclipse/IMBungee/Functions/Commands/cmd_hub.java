package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

import static de.Iclipse.IMBungee.Data.dsp;

public class cmd_hub {
    @IMCommand(
            name = "hub",
            permissions = "im.cmd.hub",
            maxArgs = 0,
            description = "hub.description",
            usage = "hub.usage",
            noConsole = true
    )
    public void hub(ProxiedPlayer p){
        if(p.getServer().getInfo().getName().contains("Lobby")){
            dsp.send(p, "hub.already");
        }else {
            System.out.println(Data.serverstatus.get(ProxyServer.getInstance().getServerInfo("Lobby01")));
            if (Data.serverstatus.get(ProxyServer.getInstance().getServerInfo("Lobby01"))) {
                dsp.send(p, "hub.successfull");
                p.connect(ProxyServer.getInstance().getServerInfo("Lobby01"));
                return;
            } else {
                for (Map.Entry<ServerInfo, Boolean> entry : Data.serverstatus.entrySet()) {
                    ServerInfo info = entry.getKey();
                    boolean online = entry.getValue();
                    if (online) {
                        if (info.getName().startsWith("Lobby")) {
                            //System.out.println(info.getMotd());
                            dsp.send(p, "hub.successfull");
                            p.connect(info);
                            return;
                        }
                    }
                }
                dsp.send(p, "hub.offline");
            }
        }
    }
}

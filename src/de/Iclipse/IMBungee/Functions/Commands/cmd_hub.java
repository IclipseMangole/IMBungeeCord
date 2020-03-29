package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Util.Command.IMCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
        }else{
            ProxyServer.getInstance().getServers().forEach((name, info)->{
               if(name.startsWith("Lobby")){
                   dsp.send(p, "hub.successfull");
                   p.connect(info);
                   return;
               }
            });
            dsp.send(p, "hub.offline");
        }
    }
}

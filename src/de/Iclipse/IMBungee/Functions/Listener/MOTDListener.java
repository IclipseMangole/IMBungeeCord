package de.Iclipse.IMBungee.Functions.Listener;

import de.Iclipse.IMBungee.Data;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static de.Iclipse.IMBungee.Data.wartung;

public class MOTDListener implements Listener {
    private String basemotd = "§5§lIclipse§7&§f§lMangole";

    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        String motd;
        if (wartung) {
            motd = "§4Pause wegen Coronavirus";
            ping.setVersion(new ServerPing.Protocol("Pause wegen Coronavirus", -1));
        } else {
            motd = "§eDer Server ist für sie offen";
        }
        ping.setDescriptionComponent(new TextComponent(basemotd + "\n§e" + motd));
        //ServerPing.PlayerInfo[] info = {new ServerPing.PlayerInfo("Coronapatient1", "1"), new ServerPing.PlayerInfo("Coronapatient2", "2"), new ServerPing.PlayerInfo("DeineMutter", "3")};
        //ping.setPlayers(new ServerPing.Players(-888888888, 3, info));
        File file = new File(Data.instance.getDataFolder() + "/Icons");
        int rand = new Random().nextInt(file.listFiles().length);
        try {
            ping.setFavicon(Favicon.create(ImageIO.read(file.listFiles()[rand])));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}

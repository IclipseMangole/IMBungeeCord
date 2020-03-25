package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL_Friend;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL_User;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.Dispatching.Language;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMBungee.Data.dsp;

public class cmd_friend {
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
    public void friend(ProxiedPlayer p){
        if(p.hasPermission("im.cmd.friend.help*")) {
            builder = new StringBuilder();
            builder.append(Data.prefix + "§7§lHilfsübersicht:§r\n");
            add(p, "list");
            add(p, "add");
            add(p, "remove");
            add(p, "accept");
            add(p, "decline");
            p.sendMessage(builder.toString());
        };
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
    public void list(ProxiedPlayer p){
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        String name = p.getName();

        ArrayList<String> all = MySQL_Friend.getFriendnames(uuid);
        ArrayList<String> offline = new ArrayList<>();
        ArrayList<String> pending = new ArrayList<>();

        p.sendMessage(Data.prefix +"§aOnline§7:");
        all.forEach(name-> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
            if(player.isConnected()){
                p.sendMessage("§e"+ name + "§7, ");
            }else{
                offline.add(name);
            }
        });

        p.sendMessage(Data.prefix + "§cOffline§7:");
        offline.forEach(name -> {
            p.sendMessage("§e" + name + "§7, ");
        });

        p.sendMessage(Data.prefix + dsp.get("friend.list.pending",Language.getLanguage(name)));

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
    public void add(ProxiedPlayer p, ProxiedPlayer friend){
        UUID uuid = UUIDFetcher.getUUID(p.getName());

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
    public void remove(ProxiedPlayer p, ProxiedPlayer friend){

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
    public void accept(ProxiedPlayer p, ProxiedPlayer friend){

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
    public void decline(ProxiedPlayer p, ProxiedPlayer friend){

    }

    private void add(CommandSender sender, String command) {
        Language lang;
        if(sender instanceof ProxiedPlayer){
            lang = MySQL_User.getLanguage(UUIDFetcher.getUUID(sender.getName()));
        }else{
            lang = Data.defaultLang;
        }
        builder.append("\n" + Data.symbol + "§e" + dsp.get("friend." + command + ".usage", lang) + "§8: §7 " + dsp.get("friend." + command + ".description", lang) + ChatColor.RESET);
    }
}

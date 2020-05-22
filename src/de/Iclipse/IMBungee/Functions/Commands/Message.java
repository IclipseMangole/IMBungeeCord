package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Functions.MySQL.UserSettings;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static de.Iclipse.IMBungee.Data.*;

public class Message {
    @IMCommand(
            name = "message",
            aliases = {"tell", "msg"},
            description = "message.description",
            usage = "message.usage",
            permissions = "im.cmd.message"
    )
    public void execute(CommandSender sender, String name, String string) {
        final String[] message = {string};
        if (name != null) {
            if (message[0] != null) {
                final boolean[] contains = {false};
                ProxyServer.getInstance().getPlayers().forEach(entry -> {
                    if (entry.getName().equals(name)) {
                        message[0] = ChatColor.translateAlternateColorCodes('$', message[0]);
                        if (sender instanceof ProxiedPlayer) {
                            if (isAllowed((ProxiedPlayer) sender, entry)) {
                                sender.sendMessage(prefix + ((ProxiedPlayer) sender).getDisplayName() + " " + symbol + " " + entry.getDisplayName() + "§7: " + message[0]);
                                entry.sendMessage(prefix + ((ProxiedPlayer) sender).getDisplayName() + " " + symbol + " " + entry.getDisplayName() + "§7: " + message[0]);
                                if (!reactions.containsKey(name)) {
                                    reactions.put(name, sender.getName());
                                } else {
                                    reactions.replace(name, sender.getName());
                                }
                            } else {
                                dsp.send(sender, "message.blocked");
                            }
                            contains[0] = true;
                        } else {
                            sender.sendMessage(prefix + "§5Server " + symbol + " " + entry.getDisplayName() + "§7: " + message[0]);
                            entry.sendMessage(prefix + "§5Server " + symbol + " " + entry.getDisplayName() + "§7: " + message[0]);
                            if (!reactions.containsKey(name)) {
                                reactions.put(name, "Server");
                            } else {
                                reactions.replace(name, "Server");
                            }
                            contains[0] = true;
                        }
                    }
                });
                if (!contains[0]) {
                    dsp.send(sender, "message.notfound");
                }
            } else {
                dsp.send(sender, "message.notext");
            }
        } else {
            if (sender instanceof ProxiedPlayer) {
                switch (UserSettings.getInt(UUIDFetcher.getUUID(sender.getName()), "message")) {
                    case 0:
                        UserSettings.setInt(UUIDFetcher.getUUID(sender.getName()), "message", 1);
                        dsp.send(sender, "message.friends");
                        break;
                    case 1:
                        UserSettings.setInt(UUIDFetcher.getUUID(sender.getName()), "message", 2);
                        dsp.send(sender, "message.nobody");
                        break;
                    case 2:
                        UserSettings.setInt(UUIDFetcher.getUUID(sender.getName()), "message", 0);
                        dsp.send(sender, "message.all");
                        break;
                }
            } else {
                dsp.send(sender, "message.noconsole");
            }
        }
    }


    @IMCommand(
            name = "react",
            aliases = {"r"},
            permissions = "im.cmd.message",
            minArgs = 2,
            usage = "react.usage",
            description = "react.description"
    )
    public void react(CommandSender sender, String string) {
        final String[] message = {string};
        String name;
        if (sender instanceof ProxiedPlayer) {
            name = sender.getName();
        } else {
            name = "Server";
        }

        if (reactions.containsKey(name)) {
            if (!reactions.get(name).equals("Server")) {
                ((ProxiedPlayer) sender).chat("/msg " + reactions.get(name) + " " + string);
            } else {
                sender.sendMessage(prefix + name + " " + symbol + " §5Serve§7: " + message[0]);
                ProxyServer.getInstance().getPlayer(name).sendMessage(prefix + name + " " + symbol + " §5Serve§7: " + message[0]);
            }
        } else {
            dsp.send(sender, "react.noMessageReceived");
        }
    }


    public static boolean isAllowed(ProxiedPlayer sender, ProxiedPlayer player) {
        switch (UserSettings.getInt(player.getUniqueId(), "message")) {
            case 0:
                return true;
            case 1:
                return true;
            default:
                return false;
        }

    }

}

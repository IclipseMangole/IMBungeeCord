package de.Iclipse.IMBungee.Functions.Commands;

import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Functions.MySQL.Friend;
import de.Iclipse.IMBungee.Functions.MySQL.User;
import de.Iclipse.IMBungee.Functions.MySQL.UserSettings;
import de.Iclipse.IMBungee.IMBungeeCord;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;

import static de.Iclipse.IMBungee.Data.dsp;

public class FriendCMD {
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
    public void execute(ProxiedPlayer p) {
        builder = new StringBuilder();
        builder.append(Data.prefix + "§7§lHilfsübersicht:§r\n");
        addToOverview(p, "list");
        addToOverview(p, "add");
        addToOverview(p, "remove");
        addToOverview(p, "jump");
        p.sendMessage(new TextComponent(builder.toString()));
    }

    @IMCommand(
            name = "list",
            parent = "friend",
            usage = "friend.list.usage",
            description = "friend.list.description",
            minArgs = 0,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.list"
    )
    public void flist(ProxiedPlayer p, Integer page) {
        if (page == null) {
            page = 0;
        }
        ArrayList<UUID> friends = Friend.getFriendsSorted(p.getUniqueId(), UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "friend_sort"));
        if (!IMBungeeCord.hasPage(friends, 8, page)) {
            page = 0;
        }
        ArrayList<UUID> shown = IMBungeeCord.getPage(friends, 8, page);
        if (shown.size() > 0) {
            dsp.send(p, "friend.list.header", page + 1 + "", friends.size() / 8 + "");
            shown.forEach(user -> {
                ProxiedPlayer f = ProxyServer.getInstance().getPlayer(user);
                if (f != null) {
                    dsp.send(p, "friend.list.format.online", IMBungeeCord.getPrefix(f) + f.getName(), f.getServer().getInfo().getName());
                } else {
                    int days = (int) (System.currentTimeMillis() - User.getLastTime(user)) / (1000 * 60 * 60 * 24);
                    dsp.send(p, "friend.list.format.offline", UUIDFetcher.getName(user) + ": §cOffline", days + "");
                }
            });
        } else {
            dsp.send(p, "friend.list.empty");
        }
    }

    @IMCommand(
            name = "add",
            parent = "friend",
            usage = "friend.add.usage",
            description = "friend.add.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.add"
    )
    public void fadd(ProxiedPlayer p, String friend) {
        UUID uuid = p.getUniqueId();
        UUID frienduuid;
        try {
            frienduuid = UUIDFetcher.getUUID(friend);
        } catch (Exception e) {
            dsp.send(p, "friend.add.notExisting");
            return;
        }
        if (User.isUserExists(frienduuid)) {
            if (!Friend.isRequestedBy(frienduuid, uuid)) {
                if (!Friend.areFriends(uuid, frienduuid)) {
                    if (!Friend.isRequestedBy(uuid, frienduuid)) {
                        Friend.createRequest(uuid, frienduuid);
                        ProxiedPlayer f = ProxyServer.getInstance().getPlayer(frienduuid);
                        if (f != null) {
                            dsp.send(f, "friend.add.request", p.getDisplayName());
                            TextComponent base = new TextComponent(Data.prefix);
                            TextComponent accept = new TextComponent(dsp.get("friend.add.request.click.accept", f));
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + p.getName()));
                            base.addExtra(accept);
                            TextComponent deny = new TextComponent(dsp.get("friend.add.request.click.deny", f));
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend remove " + p.getName()));
                            base.addExtra(deny);
                            f.sendMessage(base);
                        }
                        dsp.send(p, "friend.add.sent");
                    } else {
                        Friend.accept(uuid, frienduuid);
                        ProxiedPlayer f = ProxyServer.getInstance().getPlayer(frienduuid);
                        if (f != null) {
                            dsp.send(p, "friend.add.accepted", IMBungeeCord.getPrefix(f) + f.getName());
                            dsp.send(f, "friend.add.accepted", p.getDisplayName());
                        } else {
                            dsp.send(p, "friend.add.accepted", "§e" + UUIDFetcher.getName(frienduuid));
                        }
                    }
                } else {
                    p.sendMessage(dsp.get("friend.add.alreadyFriends", p));
                }
            } else {
                p.sendMessage(dsp.get("friend.add.alreadyRequested", p));
            }
        } else {
            p.sendMessage(dsp.get("friend.add.notExisting", p));
        }
    }

    @IMCommand(
            name = "remove",
            parent = "friend",
            usage = "friend.remove.usage",
            description = "friend.remove.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.remove"
    )
    public void fremove(ProxiedPlayer p, String friend) {
        UUID uuid = UUIDFetcher.getUUID(p.getName());
        UUID frienduuid;
        try {
            frienduuid = UUIDFetcher.getUUID(friend);
        } catch (Exception e) {
            dsp.send(p, "friend.remove.notExisting");
            return;
        }

        if (Friend.areFriends(uuid, frienduuid)) {
            Friend.deleteFriend(uuid, frienduuid);
            dsp.send(p, "friend.remove.successfull");
            ProxiedPlayer f = ProxyServer.getInstance().getPlayer(frienduuid);
            if (f != null) {
                dsp.send(f, "friend.remove.removed", IMBungeeCord.getDisplayname(p));
            }
        } else if (Friend.isRequestedBy(p.getUniqueId(), frienduuid)) {
            ProxiedPlayer f = ProxyServer.getInstance().getPlayer(frienduuid);
            if (f != null) {
                dsp.send(f, "friend.remove.denied", IMBungeeCord.getDisplayname(p));
            }
        } else {
            dsp.send(p, "friend.remove.noFriend");
        }
    }

    @IMCommand(
            name = "jump",
            usage = "friend.jump.usage",
            description = "friend.jump.description",
            minArgs = 1,
            maxArgs = 1,
            noConsole = true,
            permissions = "im.cmd.friend.jump",
            parent = "friend"
    )
    public void fjump(ProxiedPlayer p, String name) {
        ProxiedPlayer f = ProxyServer.getInstance().getPlayer(name);
        if (f != null) {
            if (Friend.areFriends(p.getUniqueId(), f.getUniqueId())) {
                if (!f.getServer().equals(p.getServer())) {
                    dsp.send(p, "friend.jump.successfull");
                } else {
                    dsp.send(p, "friend.jump.already");
                }
            } else {
                dsp.send(p, "friend.jump.noFriend");
            }
        } else {
            try {
                UUID uuid = UUIDFetcher.getUUID(name);
                if (Friend.areFriends(p.getUniqueId(), uuid)) {
                    dsp.send(p, "friend.jump.notOnline");
                } else {
                    dsp.send(p, "friend.jump.noFriend");
                }
            } catch (Exception e) {
                dsp.send(p, "friend.jump.notExisting");
            }
        }
    }


    private void addToOverview(CommandSender sender, String command) {
        builder.append("\n" + Data.symbol + "§e" + dsp.get("friend." + command + ".usage", sender) + "§8: §7 " + dsp.get("friend." + command + ".description", sender) + ChatColor.RESET);
    }

}

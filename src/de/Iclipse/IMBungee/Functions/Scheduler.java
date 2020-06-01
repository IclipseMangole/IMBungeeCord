package de.Iclipse.IMBungee.Functions;

import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Functions.MySQL.Server;
import de.Iclipse.IMBungee.Functions.MySQL.State;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static ScheduledTask task;

    public static void startScheduler() {
        task = ProxyServer.getInstance().getScheduler().schedule(Data.instance, new Runnable() {
            @Override
            public void run() {
                updateStatus();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public static void stopScheduler() {
        task.cancel();
    }

    public static void updateStatus() {
        ProxyServer.getInstance().getServers().forEach((name, info) -> {
            boolean statusOnline = Data.serverstatus.get(info);
            info.ping(new Callback<ServerPing>() {

                @Override
                public void done(ServerPing result, Throwable error) {
                    boolean serverOnline = (error == null);
                    //System.out.println(info.getName() + ": Status: " + statusOnline + ", Online: " + serverOnline);
                    if (statusOnline != serverOnline) {
                        if (!serverOnline) {
                            Server.setState(name, State.Offline);
                            Server.setPlayers(name, 0);
                        }
                        Data.serverstatus.replace(info, statusOnline, serverOnline);
                    }
                    return;
                }
            });
        });
    }
}

package de.Iclipse.IMBungee;

import de.Iclipse.IMBungee.Functions.Commands.FriendCMD;
import de.Iclipse.IMBungee.Functions.Commands.Hub;
import de.Iclipse.IMBungee.Functions.Commands.Message;
import de.Iclipse.IMBungee.Functions.Listener.*;
import de.Iclipse.IMBungee.Functions.MySQL.Friend;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL;
import de.Iclipse.IMBungee.Functions.Scheduler;
import de.Iclipse.IMBungee.Util.Command.BungeeCommand;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.Dispatching.Dispatcher;
import de.Iclipse.IMBungee.Util.Executor.BungeeExecutor;
import de.Iclipse.IMBungee.Util.Executor.ThreadExecutor;
import de.Iclipse.IMBungee.Util.IScheduler;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.lang.reflect.Method;
import java.util.*;

import static de.Iclipse.IMBungee.Data.langDE;
import static de.Iclipse.IMBungee.Data.langEN;

public final class IMBungeeCord extends Plugin {

    @Override
    public void onEnable() {
        super.onEnable();
        ThreadExecutor.setExecutor(new BungeeExecutor());
        Data.instance = this;
        MySQL.connect();
        loadResourceBundles();
        registerCommands();
        registerListener();
        createTables();
        ProxyServer.getInstance().getServers().forEach((name, info) -> Data.serverstatus.put(info, false));
        Scheduler.startScheduler();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MySQL.close();
        Scheduler.stopScheduler();
    }

    public void registerCommands() {
        register(Message.class);
        register(Hub.class);
        register(FriendCMD.class);
    }

    public void registerListener() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MOTDListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new QuitListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChannelListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new KickListener());
    }

    public void createTables() {
        Friend.createFriendTable();
    }

    public void loadResourceBundles() {
        try {
            HashMap<String, ResourceBundle> langs = new HashMap<>();
            langDE = ResourceBundle.getBundle("i18n.langDE");
            langEN = ResourceBundle.getBundle("i18n.langEN");
            langs.put("DE", langDE);
            langs.put("EN", langEN);
            Data.dsp = new Dispatcher(this, langs);
            System.out.println(Data.prefix + "Loaded languages!");
        } catch (MissingResourceException | NullPointerException e) {
            System.out.println("Reload oder Bundle not found!");
        }
    }


    private Map<String, Command> commandMap = new HashMap<>();
    private List<Object[]> unavailableSubcommands = new ArrayList<>();


    public void register(Class functionClass) {
        register(functionClass, false);
    }

    public void register(Class functionClass, boolean required) {
        try {
            Object function = functionClass.newInstance();

            Method[] methods = functionClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(IMCommand.class))
                    this.registerCommand(function, method);
            }

            if (Listener.class.isInstance(function)) {
                ProxyServer.getInstance().getPluginManager().registerListener(Data.instance, (Listener) function);
            }

            if (IScheduler.class.isInstance(function)) {
                TaskScheduler scheduler = ProxyServer.getInstance().getScheduler();
                ((IScheduler) function).registerTasks(scheduler);
            }
        } catch (Exception e) {
            System.out.println("[AtomicsWorld] Error while registering function " + functionClass.getSimpleName() + ":");
            e.printStackTrace();
            if (required) {
                System.out.println("[AtomicsWorld] Missing required function, stopping server. " + functionClass.getSimpleName() + ":");
                ProxyServer.getInstance().stop();
            }
        }
    }

    private void registerCommand(Object function, Method method) {
        IMCommand cmd = method.getAnnotation(IMCommand.class);

        if (cmd.parent().length == 0) {
            BungeeCommand BungeeCommand = new BungeeCommand(function, method, cmd);
            getProxy().getPluginManager().registerCommand(Data.instance, BungeeCommand);

            commandMap.put(BungeeCommand.getName(), BungeeCommand);

            for (Object[] unavailableSubcommand : unavailableSubcommands) {
                Method oldMethod = (Method) unavailableSubcommand[1];
                IMCommand old = oldMethod.getAnnotation(IMCommand.class);
                if (old.parent()[0].equalsIgnoreCase(old.name())) {
                    registerCommand(unavailableSubcommand[0], oldMethod);
                }
            }
        } else {
            Command pluginCommand = commandMap.get(cmd.parent()[0]);
            if (pluginCommand == null) {
                System.out.println("Plugincommand = null");
                unavailableSubcommands.add(new Object[]{function, method});
            } else {
                if (pluginCommand instanceof BungeeCommand) {
                    ((BungeeCommand) pluginCommand).getProcessor().addSubCommand(cmd, function, method);
                }
            }
        }
    }

    public static ArrayList getPage(ArrayList list, int anzPerPage, int page) {
        ArrayList pageList = new ArrayList<>();
        for (int i = page * anzPerPage; i < (page + 1) * anzPerPage && i < list.size(); i++) {
            pageList.add(list.get(i));
        }
        return pageList;
    }

    public static boolean hasPage(ArrayList list, int anzPerPage, int page) {
        if ((double) list.size() / (double) anzPerPage > page && page >= 0) {
            return true;
        }
        return false;
    }

    public static int maxPage(ArrayList list, int anzPerPage) {
        return (int) Math.ceil((double) list.size() / (double) anzPerPage);
    }

    public static String getPrefix(ProxiedPlayer p) {
        return BungeePermsAPI.groupDisplay(BungeePermsAPI.userMainGroup(p.getName()), ProxyServer.getInstance().getName(), null).replace(ChatColor.RESET.toString(), "");
    }

    public static String getDisplayname(ProxiedPlayer p) {
        return getPrefix(p);
    }
}

package de.Iclipse.IMBungee;

import de.Iclipse.IMBungee.Functions.Commands.cmd_message;
import de.Iclipse.IMBungee.Functions.Listener.ChannelListener;
import de.Iclipse.IMBungee.Functions.Listener.JoinListener;
import de.Iclipse.IMBungee.Functions.Listener.MOTDListener;
import de.Iclipse.IMBungee.Functions.Listener.QuitListener;
import de.Iclipse.IMBungee.Functions.MySQL.MySQL;
import de.Iclipse.IMBungee.Util.Command.BungeeCommand;
import de.Iclipse.IMBungee.Util.Command.IMCommand;
import de.Iclipse.IMBungee.Util.Dispatching.Dispatcher;
import de.Iclipse.IMBungee.Util.Dispatching.Language;
import de.Iclipse.IMBungee.Util.Executor.BungeeExecutor;
import de.Iclipse.IMBungee.Util.Executor.ThreadExecutor;
import de.Iclipse.IMBungee.Util.IScheduler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.Iclipse.IMBungee.Util.Dispatching.ResourceBundle.*;

public final class IMBungeeCord extends Plugin {

    @Override
    public void onEnable() {
        super.onEnable();
        ThreadExecutor.setExecutor(new BungeeExecutor());
        Data.instance = this;
        MySQL.connect();
        loadResourceBundles();
        Data.dsp = new Dispatcher(this);
        registerCommands();
        registerListener();
        ProxyServer.getInstance().registerChannel("im:main");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MySQL.close();
    }

    public void registerCommands(){
        register(cmd_message.class, true);
    }

    public void registerListener(){
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MOTDListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new QuitListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChannelListener());
    }

    public static void loadResourceBundles(){
        loadResourceBundleDE("langDE");
        loadResourceBundleEN("langEN");
        Language.DE.setBundle(msgDE);
        Language.EN.setBundle(msgEN);
        System.out.println(Data.prefix + "Loaded languages!");
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
                unavailableSubcommands.add(new Object[]{function, method});
            } else {
                if (pluginCommand instanceof BungeeCommand) {
                    ((BungeeCommand) pluginCommand).getProcessor().addSubCommand(cmd, function, method);
                }
            }
        }
    }
}

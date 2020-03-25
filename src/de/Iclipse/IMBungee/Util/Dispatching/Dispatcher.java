package de.Iclipse.IMBungee.Util.Dispatching;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Dispatcher extends Dispatch<CommandSender> {

    private String title = "";

    public Dispatcher(Plugin plugin, HashMap<String, ResourceBundle> langs) {
        super(plugin.getProxy().getName(), plugin.getLogger(), langs);
        title = plugin.getProxy().getName();
    }

    public Dispatcher(String title, Logger logger, HashMap<String, ResourceBundle> langs) {
        super(title, logger, langs);
        this.title = title;
    }

    @Override
    public void online(String key, String... args) {
        super.online(key, args);
    }

    @Override
    public void certain(String key, String permission, String... args) {
        super.certain(key, permission, args);
    }

    @Override
    public void send(CommandSender p, String key, String... args) {
        super.send(p, key, args);
    }

    @Override
    public void sendTextMessage(CommandSender receiver, String message) {
        receiver.sendMessage(message);
    }

    @Override
    public String get(String key,ResourceBundle lang,Boolean prefix, String... args) {
        return super.get(key, lang, prefix, args);
    }

    @Override
    public String get(String key, ResourceBundle lang, String... args) {
        return get(key, lang, false, args);
    }

    @Override
    public void logInfo(String message, String... args) {
        super.logInfo(message, args);
    }

    @Override
    public boolean isPlayerNull(CommandSender reciever, String player) {
        return super.isPlayerNull(reciever, player);
    }

    @Override
    public void playerNull(CommandSender reciever, String player) {
        super.playerNull(reciever, player);
    }

    @Override
    public void logSevere(String message, String... args) {
        super.logSevere(message, args);
    }

    @Override
    public void logWarning(String message, String... args) {
        super.logWarning(message, args);
    }



    public String getTitle() {
        return title;
    }

}

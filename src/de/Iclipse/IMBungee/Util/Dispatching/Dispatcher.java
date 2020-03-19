package de.Iclipse.IMBungee.Util.Dispatching;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class Dispatcher extends Dispatch<CommandSender> {

    private String title = "";

    public Dispatcher(Plugin plugin) {
        super(plugin.getDescription().getName(), plugin.getLogger());
        title = plugin.getDescription().getName();
    }

    public Dispatcher(String title, Logger logger) {
        super(title, logger);
        this.title = title;
    }

    @Override
    public void online(String key, Object... args) {
        super.online(key, args);
    }

    @Override
    public void certain(String key, String permission, Object... args) {
        super.certain(key, permission, args);
    }

    @Override
    public void send(CommandSender p, String key, Object... args) {
        super.send(p, key, args);
    }

    @Override
    public void sendTextMessage(CommandSender receiver, String message) {
        receiver.sendMessage(message);
    }

    @Override
    public String get(String key, Language lang,Boolean prefix, Object... params) {
        return super.get(key, lang, prefix, params);
    }

    @Override
    public String get(String key, Language lang, Object... params) {
        return get(key, lang, false, params);
    }

    @Override
    public void logInfo(String message, Object... args) {
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
    public void logSevere(String message, Object... args) {
        super.logSevere(message, args);
    }

    @Override
    public void logWarning(String message, Object... args) {
        super.logWarning(message, args);
    }

    public String getTitle() {
        return title;
    }

}

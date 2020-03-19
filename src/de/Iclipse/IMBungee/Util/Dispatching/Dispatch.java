package de.Iclipse.IMBungee.Util.Dispatching;


import de.Iclipse.IMBungee.Data;
import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import static de.Iclipse.IMBungee.Functions.MySQL.MySQL_User.getLanguage;
import static de.Iclipse.IMBungee.Functions.MySQL.MySQL_User.isUserExists;

public abstract class Dispatch<R> {
    private String title;
    private Logger logger;
    private String textcolor;
    private String highlight;
    private String warning;
    private java.util.ResourceBundle bundleDE = ResourceBundle.msgDE;

    public Dispatch(String title, Logger logger) {
        this.title = title;
        this.logger = logger;
        this.textcolor = bundleDE.getString("color.text");
        this.highlight = bundleDE.getString("color.highlight");
        this.warning = bundleDE.getString("color.warning");
        Data.textcolor = this.textcolor;
        Data.highlight = this.highlight;
        Data.warning = this.warning;
    }

    public String get(String key, Language lang, Object... args) {
        return get(key, lang, false, args);
    }

    public String get(String key, Language lang, Boolean prefix, Object... args) {
        try {
            StringBuilder builder = new StringBuilder();
            if (prefix) builder.append(Data.prefix.replace("IM", title));


            builder.append(MessageFormat.format(lang.getBundle().getString(key), args).replaceAll("%r", textcolor)
                    .replaceAll("%w", warning)
                    .replaceAll("%h", highlight)
                    .replaceAll("%z", "\n" + Data.symbol + " "));
            return builder.toString();
        } catch (MissingResourceException e) {
            return "Missing resource-key!";
        }
    }


    public void logInfo(String message, Object... args) {
        this.logger.info(this.get(message, Data.defaultLang, args));
    }

    public void logWarning(String message, Object... args) {
        this.logger.warning(this.get(message, Data.defaultLang, args));
    }

    public void logSevere(String message, Object... args) {
        this.logger.severe(this.get(message, Data.defaultLang, args));
    }

    public boolean isPlayerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof ProxiedPlayer) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", getLanguage(UUIDFetcher.getUUID(((ProxiedPlayer) receiver).getName())), player));
                return true;
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", Data.defaultLang, player));
                return true;
            }
        } else {
            if(receiver instanceof ProxiedPlayer) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", getLanguage(UUIDFetcher.getUUID(((ProxiedPlayer) receiver).getName())), player));
                return false;
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", Data.defaultLang, player));
                return false;
            }
        }
    }

    public void playerNull(R receiver, String player) {
        if (!isUserExists(UUIDFetcher.getUUID(player))) {
            if (receiver instanceof ProxiedPlayer) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", getLanguage(UUIDFetcher.getUUID(((ProxiedPlayer) receiver).getName())),player));
            } else {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.invalidplayer", Data.defaultLang, player));
            }
        } else {
            if (receiver instanceof ProxiedPlayer) {
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", getLanguage(UUIDFetcher.getUUID(((ProxiedPlayer) receiver).getName())),player));
            }else{
                sendTextMessage(receiver, Data.prefix.replace("IM", title) + get("warning.playeroffline", Data.defaultLang,player));
            }
        }
    }

    public void certain(String key, String permission, Object... args) {
        ProxyServer.getInstance().getPlayers().stream().filter(R -> R.hasPermission(permission)).forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, getLanguage(UUIDFetcher.getUUID(R.getName())), args)));
    }

    public void online(String key, Object... args) {
        ProxyServer.getInstance().getPlayers().forEach(R -> R.sendMessage(Data.prefix.replace("IM", title) + get(key, getLanguage(UUIDFetcher.getUUID(R.getName())), args)));
    }

    public void send(R receiver, String key, Object... args) {
        if (receiver instanceof ProxiedPlayer) {
            sendTextMessage(receiver, Data.prefix + get(key, getLanguage(UUIDFetcher.getUUID(((ProxiedPlayer) receiver).getName())), args));
        } else {
            sendTextMessage(receiver, Data.prefix + get(key, Data.defaultLang, args));
        }
    }

    public abstract void sendTextMessage(R receiver, String submit);

}

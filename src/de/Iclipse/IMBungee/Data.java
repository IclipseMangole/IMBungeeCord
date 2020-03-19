package de.Iclipse.IMBungee;

import de.Iclipse.IMBungee.Util.Dispatching.Dispatcher;
import de.Iclipse.IMBungee.Util.Dispatching.Language;
import net.md_5.bungee.api.plugin.Plugin;

public class Data {
    public static boolean wartung = true;
    public static Plugin instance;
    public static String symbol = "§8 » §7";
    public static String prefix = "§5IM" + symbol;
    public static Language defaultLang = Language.DE;
    public static Dispatcher dsp;
    public static String textcolor;
    public static String highlight;
    public static String warning;
}

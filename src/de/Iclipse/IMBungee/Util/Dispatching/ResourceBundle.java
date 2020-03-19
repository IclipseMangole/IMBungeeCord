package de.Iclipse.IMBungee.Util.Dispatching;


public class ResourceBundle {
    public static java.util.ResourceBundle msgDE;
    public static java.util.ResourceBundle msgEN;

    public static void loadResourceBundleDE(String file) {
        msgDE = java.util.ResourceBundle.getBundle("i18n."+ file);
    }

    public static void loadResourceBundleEN(String file) {
        msgEN = java.util.ResourceBundle.getBundle("i18n." + file);
    }

}

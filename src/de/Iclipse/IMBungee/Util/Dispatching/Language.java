package de.Iclipse.IMBungee.Util.Dispatching;


public enum Language {
    DE("DE", ResourceBundle.msgDE, "Deutsch"), EN("EN", ResourceBundle.msgEN, "English");


    Language(String shortcut, java.util.ResourceBundle bundle, String translation) {
        this.shortcut = shortcut;
        this.bundle = bundle;
        this.translation = translation;
    }

    private String shortcut;
    private java.util.ResourceBundle bundle;
    private String translation;

    public String getShortcut() {
        return shortcut;
    }

    public java.util.ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(java.util.ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getTranslation(){
        return translation;
    }

    public static Language getLanguage(String shortcut){
        for (Language lang : Language.values()) {
            if(lang.getShortcut().equals(shortcut)){
                return lang;
            }
        }
        return null;
    }

}

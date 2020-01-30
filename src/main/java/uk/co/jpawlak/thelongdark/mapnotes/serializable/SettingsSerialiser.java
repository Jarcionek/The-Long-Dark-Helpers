package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import uk.co.jpawlak.thelongdark.mapnotes.Main;

import java.io.File;

public class SettingsSerialiser {

    public static void save(Settings settings) {
        Serialiser.save(settings, Main.MAIN_FOLDER, "settings");
    }

    public static Settings load() {
        return Serialiser.load(new File(Main.MAIN_FOLDER, "settings.json"), Settings.class);
    }

}

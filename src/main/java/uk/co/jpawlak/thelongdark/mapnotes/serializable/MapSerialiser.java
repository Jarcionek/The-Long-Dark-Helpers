package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import uk.co.jpawlak.thelongdark.mapnotes.Main;

import java.io.File;

public class MapSerialiser {

    public static boolean exists(String name) {
        return new File(Main.SAVED_MAPS_FOLDER, name + ".json").exists();
    }

    public static void save(Map map) {
        Serialiser.save(map, Main.SAVED_MAPS_FOLDER, map.getName());

    }

    public static Map loadAndMigrate(File file) {
        Map map = load(file);
        map.migrate();
        return map;
    }

    public static Map load(File file) {
        return Serialiser.load(file, Map.class);
    }

}

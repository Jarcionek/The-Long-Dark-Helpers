package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import uk.co.jpawlak.thelongdark.mapnotes.Main;

import java.io.File;

public class MapSerialiser {

    public boolean exists(String name) {
        return new File(Main.SAVED_MAPS_FOLDER, name + ".json").exists();
    }

    public void save(Map map) {
        Serialiser.save(map, Main.SAVED_MAPS_FOLDER, map.getName());
    }

    public Map loadAndMigrate(File file) {
        Map map = load(file);
        map.migrate();
        return map;
    }

    public Map load(File file) {
        return Serialiser.load(file, Map.class);
    }

}

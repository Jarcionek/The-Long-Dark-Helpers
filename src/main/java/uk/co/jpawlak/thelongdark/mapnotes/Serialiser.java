package uk.co.jpawlak.thelongdark.mapnotes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Serialiser {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(Map map) {
        File file = new File(Main.SAVED_MAPS_FOLDER, map.getName() + ".json");
        File backupFile = new File(Main.SAVED_MAPS_FOLDER, map.getName() + ".json.backup");

        if (file.exists()) {
            backupFile.delete();
            file.renameTo(backupFile);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(GSON.toJson(map));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        backupFile.delete();
    }

}

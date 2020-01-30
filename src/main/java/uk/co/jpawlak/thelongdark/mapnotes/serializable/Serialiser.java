package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Serialiser {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void save(Object object, File directory, String filenameWithoutExtension) {
        File file = new File(directory, filenameWithoutExtension + ".json");
        File backupFile = new File(directory, filenameWithoutExtension + ".json.backup");

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
            out.println(GSON.toJson(object));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        backupFile.delete();
    }

    static <T> T load(File file, Class<T> type) {
        try {
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            return Serialiser.GSON.fromJson(fileContent, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

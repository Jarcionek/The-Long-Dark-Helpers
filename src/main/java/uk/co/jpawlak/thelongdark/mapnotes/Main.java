package uk.co.jpawlak.thelongdark.mapnotes;

import org.apache.commons.io.FileUtils;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Settings;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;

import javax.swing.UIManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static final String APPLICATION_NAME = "The Long Dark Helpers";

    public static final File MAIN_FOLDER = new File(System.getProperty("user.dir"), APPLICATION_NAME);
    public static final File SAVED_MAPS_FOLDER = new File(MAIN_FOLDER, "Maps");
    public static final File MAPS_IMAGES_FOLDER = new File(MAIN_FOLDER, "Maps Images");
    public static final File MARKERS_IMAGES_FOLDER = new File(MAIN_FOLDER, "Markers Images");

    public static final String VERSION = "1.3"; //TODO ugly code: this should be in properties file - https://stackoverflow.com/questions/3697449
    //TODO ugly code: add missing fix version (update release script to increment minor version, rather than major)

    public static void main(String[] args) {
        boolean isFreshInstallation = MAIN_FOLDER.mkdirs();

        if (!SAVED_MAPS_FOLDER.mkdirs()) {
            backupMaps();
        }

        if (MAPS_IMAGES_FOLDER.mkdirs()) {
            copyMapsResourcesToFolder();
        }

        if (MARKERS_IMAGES_FOLDER.mkdirs()) {
            copyIconsResourcesToFolder();
        }

        SettingsSerialiser settingsSerialiser = new SettingsSerialiser();
        MapSerialiser mapSerialiser = new MapSerialiser();

        if (!isFreshInstallation) {
            migrateSavedMaps(settingsSerialiser, mapSerialiser);
        }
        if (!settingsSerialiser.settingsExist()) {
            //TODO ugly code: all this migration code is a mess...
            settingsSerialiser.save(new Settings(VERSION));
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set look and feel " + e.toString()); //TODO ugly code: logger!
        }

        FileChooser fileChooser = new FileChooser();
        NoteWindowManager noteWindowManager = new NoteWindowManager(settingsSerialiser, mapSerialiser);
        MapWindowManager mapWindowManager = new MapWindowManager(fileChooser, settingsSerialiser, mapSerialiser, noteWindowManager);

        mapWindowManager.create();
    }

    private static void migrateSavedMaps(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        String lastUsedVersion;
        if (settingsSerialiser.settingsExist()) {
            lastUsedVersion = settingsSerialiser.load().getVersion();
        } else {
            lastUsedVersion = "1.1";
            settingsSerialiser.save(new Settings(VERSION));
        }

        if (lastUsedVersion.equals("1.1")) {
            Stream.of(SAVED_MAPS_FOLDER.listFiles())
                    .map(mapSerialiser::loadAndMigrate)
                    .forEach(mapSerialiser::save);
        }
    }

    private static void backupMaps() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTime = localDateTime.format(formatter);

        File allBackupsFolder = new File(MAIN_FOLDER, "Maps backups");
        File backupFolder = new File(allBackupsFolder, dateTime);
        backupFolder.mkdirs();
        copyDirectory(SAVED_MAPS_FOLDER, backupFolder);
    }

    private static void copyMapsResourcesToFolder() {
        //TODO ugly code: find a nice solution to iterate over resources inside a jar file
        List<String> files = Arrays.asList(
                "caves/Cinder Hills Coal Mine.png",
                "caves/Cinder Hills Coal Mine (more detailed).jpg",
                "caves/Cinder Hills Coal Mine (Wintermute).png",
                "caves/No 3 Coal Mine.png",

                "connectors/Crumbling Highway.png",
                "connectors/Ravine.png",
                "connectors/Winding River.png",

                "Bleak Inlet.png",
                "Broken Railroad.png",
                "Coastal Highway.png",
                "Desolation Point.png",
                "Forlorn Muskeg.png",
                "Hushed River Valley.png",
                "Mountain Town.png",
                "Mystery Lake.jpg",
                "Pleasant Valley.png",
                "Timberwolf Mountain.png"
        );
        copyResources("/maps/", files, MAPS_IMAGES_FOLDER);
    }

    private static void copyIconsResourcesToFolder() {
        List<String> files = Arrays.asList(
                "Collectibles/Cairn.png",

                "Danger/Bear.png",
                "Danger/Moose.png",
                "Danger/Wolf.png",

                "Hunting/Deer.png",
                "Hunting/Rabbit.png",

                "Resources/Cloth.png",
                "Resources/Reclaimed wood.png",

                "Base.png",
                "Blue tick.png",
                "Cross.png",
                "Exclamation mark.png",
                "Medicines.png",
                "Note.png",
                "Question mark.png",
                "Tick.png"
        );
        copyResources("/icons/", files, MARKERS_IMAGES_FOLDER);
    }

    private static void copyResources(String resourceFolder, List<String> files, File destinationFolder) {
        try {
            for (String file : files) {
                InputStream source = Main.class.getResourceAsStream(resourceFolder + file);
                Path destination = Paths.get(destinationFolder + "/" + file);
                new File(destination.toUri()).getParentFile().mkdirs();
                Files.copy(source, destination);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyDirectory(File srcDir, File destDir) {
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

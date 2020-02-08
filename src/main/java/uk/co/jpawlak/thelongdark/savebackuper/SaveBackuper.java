package uk.co.jpawlak.thelongdark.savebackuper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SaveBackuper {

    public static final String USER_NAME = System.getProperty("user.name");

    public static final File BACKUP_DIRECTORY = new File("c:\\Users\\" + USER_NAME + "\\Desktop\\TLD backups");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public static void main(String[] args) throws InterruptedException, IOException {
        String saveName = args[0];
        File saveFile = new File("c:\\Users\\" + USER_NAME + "\\AppData\\Local\\Hinterland\\TheLongDark\\" + saveName);
        if (!saveFile.exists()) {
            throw new IllegalArgumentException("provide the existing filename to watch!");
        }

        BACKUP_DIRECTORY.mkdirs();

        long lastModified = saveFile.lastModified();
        while (true) {
            Thread.sleep(5000);

            if (saveFile.lastModified() == lastModified) {
                log("-");
                continue;
            }

            lastModified = saveFile.lastModified();
            File backupFile = new File(BACKUP_DIRECTORY, saveFile.getName() + "-" + DATE_FORMAT.format(lastModified));
            Files.copy(saveFile.toPath(), backupFile.toPath());
            log("backup created");
        }
    }

    private static int logCount = 0;

    private static void log(String s) {
        System.out.println(logCount++ + " " + s);
    }

}

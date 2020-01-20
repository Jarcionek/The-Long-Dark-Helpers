package uk.co.jpawlak.thelongdark.savebackuper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SaveBackuper {

    public static final String USER_NAME = System.getProperty("user.name");

    public static final String SAVE_NAME = "challenge5";
    public static final File SAVE_FILE = new File("c:\\Users\\" + USER_NAME + "\\AppData\\Local\\Hinterland\\TheLongDark\\" + SAVE_NAME);
    public static final File BACKUP_DIRECTORY = new File("c:\\Users\\" + USER_NAME + "\\Desktop\\TLD backups");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public static void main(String[] args) throws InterruptedException, IOException {
        if (!SAVE_FILE.exists()) {
            throw new IllegalArgumentException("provide the existing filename to watch!");
        }

        BACKUP_DIRECTORY.mkdirs();

        long lastModified = SAVE_FILE.lastModified();
        while (true) {
            Thread.sleep(5000);

            if (SAVE_FILE.lastModified() == lastModified) {
                log("-");
                continue;
            }

            lastModified = SAVE_FILE.lastModified();
            File backupFile = new File(BACKUP_DIRECTORY, SAVE_FILE.getName() + "-" + DATE_FORMAT.format(lastModified));
            Files.copy(SAVE_FILE.toPath(), backupFile.toPath());
            log("backup created");
        }
    }

    private static int logCount = 0;

    private static void log(String s) {
        System.out.println(logCount++ + " " + s);
    }

}

package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.io.File;

public class FileChooser {

    public static File chooseImage(Component parentComponent, File currentDirectoryPath) {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().matches("(?i).+\\.(jpg|jpeg|png|gif)");
            }

            @Override
            public String getDescription() {
                return "Image";
            }
        };

        return chooseFile(parentComponent, currentDirectoryPath, filter);
    }

    public static File chooseJson(Component parentComponent, File currentDirectoryPath) {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().matches("(?i).+\\.(json)");
            }

            @Override
            public String getDescription() {
                return "Json";
            }
        };

        return chooseFile(parentComponent, currentDirectoryPath, filter);

    }


    private static File chooseFile(Component parentComponent, File currentDirectoryPath, FileFilter filter) {
        JFileChooser fileChooser = new JFileChooser(currentDirectoryPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(parentComponent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

}

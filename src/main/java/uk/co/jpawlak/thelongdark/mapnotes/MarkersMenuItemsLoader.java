package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.awt.Image.SCALE_SMOOTH;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class MarkersMenuItemsLoader {

    public static List<JMenuItem> createFromFiles(Consumer<String> clickCallback) {
        return menuItemsFromDirectory(Main.MARKERS_IMAGES_FOLDER, clickCallback);
    }

    private static List<JMenuItem> menuItemsFromDirectory(File directory, Consumer<String> clickCallback) {
        return Stream.of(directory.listFiles())
                .filter(FileChooser::isFolderOrImage)
                .sorted(comparing(File::isDirectory).thenComparing(File::getName))
                .map(file -> file.isDirectory()
                        ? menuFromDirectory(file, clickCallback)
                        : menuItemFromFile(clickCallback, file)
                )
                .collect(toList());
    }

    private static JMenu menuFromDirectory(File directory, Consumer<String> clickCallback) {
        JMenu menu = new JMenu(directory.getName());
        menuItemsFromDirectory(directory, clickCallback).forEach(menu::add);
        return menu;
    }

    private static JMenuItem menuItemFromFile(Consumer<String> clickCallback, File file) {
        String fileNameWithoutExtension = file.getName().replaceFirst("\\..*?$", "");

        ImageIcon icon = icon(file);
        ImageIcon scaledIcon = scaledIcon(icon);

        JMenuItem menuItem = new JMenuItem(fileNameWithoutExtension, scaledIcon);

        String imageLocation = Main.MARKERS_IMAGES_FOLDER.toURI().relativize(file.toURI()).getPath();
        menuItem.addActionListener(action -> clickCallback.accept(imageLocation));

        return menuItem;
    }

    private static ImageIcon icon(File file) {
        try {
            return new ImageIcon(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ImageIcon scaledIcon(ImageIcon icon) {
        Image image = icon.getImage();
        //TODO nice to have: keep ratio while scaling
        Image scaledImage = image.getScaledInstance(20, 20, SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

}

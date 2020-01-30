package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class MarkersMenuItemsLoader {

    public static List<JMenuItem> createFromFiles(Consumer<String> clickCallback) {
        return menuItemsFromDirectory(Main.MARKERS_IMAGES_FOLDER, clickCallback);
    }

    private static List<JMenuItem> menuItemsFromDirectory(File directory, Consumer<String> clickCallback) {
        return Stream.of(directory.listFiles())
                .sorted(comparing(File::getName))
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
        JMenuItem menuItem = new JMenuItem(file.getName().replaceFirst("\\..*?$", ""));

        String imageLocation = Main.MARKERS_IMAGES_FOLDER.toURI().relativize(file.toURI()).getPath();
        menuItem.addActionListener(action -> clickCallback.accept(imageLocation));

        return menuItem;
    }

}

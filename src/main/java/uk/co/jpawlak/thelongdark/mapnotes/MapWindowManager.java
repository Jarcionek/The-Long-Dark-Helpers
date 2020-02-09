package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Settings;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.WindowSettings;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

public class MapWindowManager {

    private static final int SCROLL_SENSITIVITY = 15;

    private final FileChooser fileChooser;
    private final SettingsSerialiser settingsSerialiser;
    private final MapSerialiser mapSerialiser;
    private final NoteWindowManager noteWindowManager;

    public MapWindowManager(FileChooser fileChooser, SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser, NoteWindowManager noteWindowManager) {
        this.fileChooser = fileChooser;
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
        this.noteWindowManager = noteWindowManager;
    }

    public void create() {
        JFrame frame = new JFrame(Main.APPLICATION_NAME + " " + Main.VERSION);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mapMenu = new JMenu("Map");
        menuBar.add(mapMenu);

        JMenuItem newMapMenuItem = new JMenuItem("New");
        mapMenu.add(newMapMenuItem);
        newMapMenuItem.addActionListener(action -> {
            File selectedFile = fileChooser.chooseImage(frame, Main.MAPS_IMAGES_FOLDER);
            if (selectedFile == null) {
                return;
            }

            //TODO nice to have: if file is not in "Maps Images" folder, copy it there

            String name = (String) JOptionPane.showInputDialog(
                    frame,
                    "Name the map",
                    "New map",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    selectedFile.getName().replaceFirst("\\..*", "")
            );

            if (mapSerialiser.exists(name)) {
                JOptionPane.showMessageDialog(frame, "Name already exists!", "New map", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map map = new Map(name, selectedFile.getPath()); //TODO nice to have: path relative to Maps Images folder
            mapSerialiser.save(map);
            createMapPanelAndAddToFrame(frame, map);
        });

        JMenuItem loadMapMenuItem = new JMenuItem("Load");
        //TODO persist the map and reopen it on startup
        mapMenu.add(loadMapMenuItem);
        loadMapMenuItem.addActionListener(action -> {
            /* //TODO ugly code: inconsistency when loading/saving map
             * this allows to select a file in any folder when loading, but saving won't respect the location,
             * it will use the name saved in the file and save it to SAVED_MAPS_FOLDER, possibly overriding existing map
             * should it use a dropdown instead of file chooser?
             */
            File selectedFile = fileChooser.chooseJson(frame, Main.SAVED_MAPS_FOLDER);
            if (selectedFile == null) {
                return;
            }

            Map map = mapSerialiser.load(selectedFile);
            createMapPanelAndAddToFrame(frame, map);
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //TODO ugly code: copy-paste of NoteWindowManager code
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                saveMapWindowSettings(frame.getLocation(), frame.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                saveMapWindowSettings(frame.getLocation(), frame.getSize());
            }
        });

        //TODO ugly code: copy-paste of NoteWindowManager code
        WindowSettings mapWindowSettings = settingsSerialiser.load().getMapWindowSettings();
        if (mapWindowSettings == null) {
            frame.setSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
        } else {
            frame.setSize(mapWindowSettings.getWidth(), mapWindowSettings.getHeight());
            frame.setLocation(mapWindowSettings.getX(), mapWindowSettings.getY());
        }

        frame.setVisible(true);
    }

    private void createMapPanelAndAddToFrame(JFrame frame, Map map) {
        noteWindowManager.close();

        MapPanel mapPanel = new MapPanel(map, mapSerialiser, noteWindowManager);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        frame.setContentPane(scrollPane);
        frame.revalidate();
    }

    //TODO ugly code: copy-paste of NoteWindowManager code
    private void saveMapWindowSettings(Point location, Dimension size) {
        Settings settings = settingsSerialiser.load();
        settings.setMapWindowSettings(new WindowSettings(location.x, location.y, size.width, size.height));
        settingsSerialiser.save(settings);
    }

}

package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.io.File;

public class MapWindowManager {

    private static final int SCROLL_SENSITIVITY = 15;

    private final FileChooser fileChooser;
    private final MapSerialiser mapSerialiser;
    private final NoteWindowManager noteWindowManager;

    public MapWindowManager(FileChooser fileChooser, MapSerialiser mapSerialiser, NoteWindowManager noteWindowManager) {
        this.fileChooser = fileChooser;
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
        mapMenu.add(loadMapMenuItem);
        loadMapMenuItem.addActionListener(action -> {
            File selectedFile = fileChooser.chooseJson(frame, Main.SAVED_MAPS_FOLDER);
            if (selectedFile == null) {
                return;
            }

            Map map = mapSerialiser.load(selectedFile);
            createMapPanelAndAddToFrame(frame, map);
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createMapPanelAndAddToFrame(JFrame frame, Map map) {
        MapPanel mapPanel = new MapPanel(map, mapSerialiser, noteWindowManager);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        frame.setContentPane(scrollPane);
        frame.revalidate();
    }

}

package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.io.File;

public class Main {

    public static final int SCROLL_SENSITIVITY = 15;

    public static final File MAIN_FOLDER = new File(System.getProperty("user.dir"), "The Long Dark Helpers");
    public static final File SAVED_MAPS_FOLDER = new File(MAIN_FOLDER, "Maps");
    public static final File MAPS_IMAGES_FOLDER = new File(MAIN_FOLDER, "Maps Images");

    public static void main(String[] args) {
        MAIN_FOLDER.mkdirs();
        SAVED_MAPS_FOLDER.mkdirs();
        MAPS_IMAGES_FOLDER.mkdirs();
        //TODO nice to have: if folder was created, copy maps from resource into there

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set look and feel " + e.toString());
        }

        JFrame frame = new JFrame();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mapMenu = new JMenu("Map");
        menuBar.add(mapMenu);

        JMenuItem newMapMenuItem = new JMenuItem("New");
        mapMenu.add(newMapMenuItem);
        newMapMenuItem.addActionListener(action -> {
            File selectedFile = FileChooser.chooseImage(frame, MAPS_IMAGES_FOLDER);
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

            if (MapSerialiser.exists(name)) {
                JOptionPane.showMessageDialog(frame, "Name already exists!", "New map", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map map = new Map(name, selectedFile.getPath()); //TODO nice to have: path relative to Maps Images folder
            MapSerialiser.save(map);
            createMapPanelAndAddToFrame(frame, map);
        });

        JMenuItem loadMapMenuItem = new JMenuItem("Load");
        mapMenu.add(loadMapMenuItem);
        loadMapMenuItem.addActionListener(action -> {
            File selectedFile = FileChooser.chooseJson(frame, SAVED_MAPS_FOLDER);
            if (selectedFile == null) {
                return;
            }

            Map map = MapSerialiser.load(selectedFile);
            createMapPanelAndAddToFrame(frame, map);
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createMapPanelAndAddToFrame(JFrame frame, Map map) {
        MapPanel mapPanel = new MapPanel(map);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        frame.setContentPane(scrollPane);
        frame.revalidate();
    }

}

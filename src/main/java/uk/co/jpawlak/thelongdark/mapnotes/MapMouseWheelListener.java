package uk.co.jpawlak.thelongdark.mapnotes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class MapMouseWheelListener implements MouseWheelListener {

    private static final double SENSITIVITY = 1.25;
    private static final int MAX_ZOOM_IN_STEP = 5;
    private static final int MAX_ZOOM_OUT_STEP = -5;

    private final BufferedImage image;
    private final Consumer<Image> resizedImageConsumer;

    private int currentZoomStep = 0;

    public MapMouseWheelListener(BufferedImage image, Consumer<Image> resizedImageConsumer) {
        this.image = image;
        this.resizedImageConsumer = resizedImageConsumer;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
//        System.out.println("e.getX() = " + e.getX());
//        System.out.println("e.getY() = " + e.getY());
//        System.out.println("e.getScrollAmount() = " + e.getScrollAmount());
//        System.out.println("e.getScrollType() = " + e.getScrollType());
//        System.out.println("e.getUnitsToScroll() = " + e.getUnitsToScroll());
//        System.out.println("e.getWheelRotation() = " + e.getWheelRotation());

        currentZoomStep -= e.getWheelRotation();
        currentZoomStep = Math.min(currentZoomStep, MAX_ZOOM_IN_STEP);
        currentZoomStep = Math.max(currentZoomStep, MAX_ZOOM_OUT_STEP);

        System.out.println(currentZoomStep);

        int newImageWidth = (int) (image.getWidth() * Math.pow(SENSITIVITY, currentZoomStep));
        int newImageHeight = (int) (image.getHeight() * Math.pow(SENSITIVITY, currentZoomStep));
        BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, image.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newImageWidth, newImageHeight, null);
        g.dispose();

        resizedImageConsumer.accept(resizedImage);
//        imageIcon.setImage(resizedImage);
//        mapLabel.revalidate();
//        mapLabel.repaint();
    }

}

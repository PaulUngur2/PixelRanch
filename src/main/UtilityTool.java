package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    //This method is used to scale the image to the size of the tile to cut on processing time
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }
}

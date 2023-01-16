package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectsShop1 extends Objects {
    GamePanel panel;
    //Constructor for the shop objects
    public ObjectsShop1(GamePanel panel) {

        this.panel = panel;
        type = "Shop1";
        try {
            image =  ImageIO.read(java.util.Objects.requireNonNull(getClass().getResourceAsStream("/tile/shop_entrance1.png")));
            tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}


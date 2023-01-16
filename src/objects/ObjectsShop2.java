package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectsShop2 extends Objects {
    GamePanel panel;
    //Constructor for the shop objects
    public ObjectsShop2(GamePanel panel) {

        type = "Shop2";
        try {
            image =  ImageIO.read(java.util.Objects.requireNonNull(getClass().getResourceAsStream("/tile/shop_entrance2.png")));
            tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}

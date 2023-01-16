package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectsFarm2 extends Objects {
    GamePanel panel;
    //Constructor for the farm objects
    public ObjectsFarm2(GamePanel panel) {

        this.panel = panel;
        type = "Farm2";
        try {
            image =  ImageIO.read(java.util.Objects.requireNonNull(getClass().getResourceAsStream("/tile/farm_entrance2.png")));
            tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}

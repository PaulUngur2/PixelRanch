package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectsField2 extends Objects {
    GamePanel panel;
    //Constructor for the field objects
    public ObjectsField2(GamePanel panel) {

        this.panel = panel;
        type = "Field2";
        try {
            image =  ImageIO.read(java.util.Objects.requireNonNull(getClass().getResourceAsStream("/tile/field_entrance2.png")));
            tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}


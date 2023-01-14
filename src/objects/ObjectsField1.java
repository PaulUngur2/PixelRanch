package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectsField1 extends Objects{
    GamePanel panel;
    public ObjectsField1(GamePanel panel) {
        type = "Field1";
        try {
            image =  ImageIO.read(getClass().getResourceAsStream("/tile/field_entrance1.png"));
            tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}

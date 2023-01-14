package objects;
import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
public class Objects {
    public BufferedImage image;
    public boolean collision = false;
    public String type;
    public int x, y;
    public Rectangle hitBox = new Rectangle(0,0, 48, 48);
    public int hitBoxDefaultX = 0;
    public int hitBoxDefaultY = 0;
    UtilityTool tool = new UtilityTool();

    public void draw(Graphics2D g2d, GamePanel panel) {
        g2d.drawImage(image, x, y, panel.tilesSize, panel.tilesSize, null);
    }
}

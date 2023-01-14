package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public BufferedImage up1;
    public BufferedImage up2;
    public BufferedImage down1;
    public BufferedImage down2;
    public BufferedImage left1;
    public BufferedImage left2;
    public BufferedImage right1;
    public BufferedImage right2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNumber = 1;

    public Rectangle hitBox;
    public int hitBoxDefaultX, hitBoxDefaultY;
    
    public boolean collision = false;
}

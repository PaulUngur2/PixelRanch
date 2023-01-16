package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    GamePanel panel;
    KeyHandler keyH;
    //Constructor
    public Player(GamePanel panel, KeyHandler keyH) {

        this.panel = panel;
        this.keyH = keyH;

        hitBox = new Rectangle(0, 16, 32, 32);

        hitBoxDefaultX = hitBox.x;
        hitBoxDefaultY = hitBox.y;

        setDefaultValues();
        getPlayerImage();
    }
    //Sets the default values for the player
    public void setDefaultValues() {

        x = 365;
        y = 400;
        speed = 4;
        direction = "up";
    }
    //Gets the sprites for the player
    public void getPlayerImage() {

        up1 = setUp("up1");
        up2 = setUp("up2");
        down1 = setUp("down1");
        down2 = setUp("down2");
        left1 = setUp("left1");
        left2 = setUp("left2");
        right1 = setUp("right1");
        right2 = setUp("right2");
    }
    //Scales up the sprites for the player beforehand for better performance
    public BufferedImage setUp(String name){

        UtilityTool tool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/" + name + ".png")));
            image = tool.scaleImage(image, panel.tilesSize, panel.tilesSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    //Updates the player based on the user's input
    public void update() {
        //Checks if the player is moving
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                direction = "up";

            } else if (keyH.downPressed) {
                direction = "down";

            } else if (keyH.leftPressed) {
                direction = "left";

            } else if(keyH.rightPressed) {
                direction = "right";
            }

            //Checking the tile collision
            collision = false;
            panel.collisionChecker.checkTile(this);

            interact(panel.collisionChecker.checkObject(this, true));
            //If there is a collision, the player will not move
            if (!collision && !keyH.enterPressed) {
                switch (direction) {
                    case "up" -> y -= speed;
                    case "down" -> y += speed;
                    case "left" -> x -= speed;
                    case "right" -> x += speed;
                }
            }
            panel.keyH.enterPressed = false;
            spriteCounter++;
            //Changes the sprite of the player at each interval to create animation
            if (spriteCounter > 15) {
                if (spriteNumber == 1) {
                    spriteNumber = 0;
                } else {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }
    }
    //Checks if the player is interacting with an object
    public void interact(int i) {

        if (i != 999) {
            if (panel.keyH.enterPressed) {
                String objectName = panel.objects[i].type;
                //If the player interacts with an objects it enters menu state
                panel.gameState = panel.menuState;
                switch (objectName) {
                    case "Shop1", "Shop2" -> panel.ui.interaction = 1;
                    case "Field1", "Field2" -> panel.ui.interaction = 2;
                    case "Farm1", "Farm2" -> panel.ui.interaction = 3;
                }
            }
        }
    }
    //Renders the player
    public void draw(Graphics g2d) {
        //Draws the player based on the direction and checks to see which sprite to draw
        BufferedImage playerImage = switch (direction) {
            case "up" -> {
                if (spriteNumber == 0) {
                    yield up1;
                } else {
                    yield up2;
                }
            }
            case "down" -> {
                if (spriteNumber == 0) {
                    yield down1;
                } else {
                    yield down2;
                }
            }
            case "left" -> {
                if (spriteNumber == 0) {
                    yield left1;
                } else {
                    yield left2;
                }
            }
            case "right" -> {
                if (spriteNumber == 0) {
                    yield right1;
                } else {
                    yield right2;
                }
            }
            default -> null;
        };

        g2d.drawImage(playerImage, x, y, null);
    }
}

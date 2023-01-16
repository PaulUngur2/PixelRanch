package main;

import entity.Player;
import game.Game;
import objects.Objects;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //Screen settings
    final int originalTilesSize = 16; //16x16 pixels
    final int scale = 3; //3x scale
    public final int tilesSize = originalTilesSize * scale; //48x48 pixels
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tilesSize * maxScreenCol; //768 pixels
    public final int screenHeight = tilesSize * maxScreenRow; //576 pixels
    final int FPS = 60; //60 frames per second
    //Game Logic
    Game game = new Game();
    //UI
    public UI ui = new UI(this, game.playerLogic, game.crops, game.animals, game);
    //System
    public KeyHandler keyH = new KeyHandler(this, game.playerLogic, game.crops, game.animals, game, game.database);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    TileManager tileManager = new TileManager(this);
    Sound sound = new Sound();
    //Objects and Entities
    public ObjectSetter objectSetter = new ObjectSetter(this);
    Player player = new Player(this, keyH);
    public Objects[] objects = new Objects[10];
    //Thread
    Thread gameThread;
    //Game States
    public int gameState;
    public final int playState = 0;
    public final int pauseState = 1;
    public final int menuState = 2;
    public final int titleScreen = 3;

    //Game settings
    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    //Set up for the start of the game
    public void setUpGame() {
        objectSetter.setObject();
        gameState = titleScreen;
    }
    //Starts the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        //The variable drawInterval is calculated by dividing 1 second (1000000000 nanoseconds) by the desired frames per second (60).
        //This value represents the amount of time that should pass between each frame.
        double drawInterval = 1000000000 / FPS; //1 second divided by FPS
        double delta = 0; //The variable delta is used to keep track of the amount of time that has passed since the last frame.
        //lastTime variable is used to store the timestamp of the last frame, and currentTime variable is used to store the current timestamp.
        long lastTime = System.nanoTime();
        long currentTime;
        //The loop runs while the gameThread variable is not null.
        while (gameThread != null) {
            //In each iteration of the loop, the current time is recorded and the difference between the current time and the last time is added to the delta variable.
            //This value is then divided by the drawInterval to get the number of times the update and render methods should be called.
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                //The if statement checks if the delta variable is greater than or equal to 1, if so, it means that enough time has passed to update and render the game.
                // The update() and repaint() methods are called, and the delta variable is decremented by 1.
                update();
                repaint();
                delta--;
            }
        }
    }
    //Checks the state of the game, in playState it updates the player, in pauseState it stops updating it
    public void update() {
        if (gameState == playState) {
            player.update();
        } else if (gameState == pauseState) {
            //Do nothing
        }
    }

    public void paintComponent(Graphics g) {
        //Ensures that any background or other elements that the parent class is responsible for drawing are properly
        //rendered before the game's graphics are drawn.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Checks the state of the game
        if (gameState == titleScreen){//If it's title screen, do not draw the game
            //Do nothing
        }else{//If it's not title screen, draw the game, including the tiles, objects, ui, and player
            tileManager.draw(g2d);
            for (Objects object : objects) {
                if (object != null) {
                    object.draw(g2d, this);
                }
            }
            player.draw(g2d);
        }
        ui.draw(g2d);
        g2d.dispose();
    }
    //Plays the music and sets it on loop
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    //Stops the music
    public void stopMusic() {
        sound.stop();
    }

}

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

    public void setUpGame() {
        objectSetter.setObject();
        gameState = titleScreen;
    }

    public void startGameThread() {
        //Start the game thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; //1 second divided by FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }
    }
    public void update() {
        if (gameState == playState) {
            player.update();
        } else if (gameState == pauseState) {
            //Do nothing
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (gameState == titleScreen){

        }else {
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

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

}

package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        //Create the frame
        JFrame window = new JFrame();
        //It also ensures that the game will close in an orderly manner when the player quits the game.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Makes the window not resizable
        window.setResizable(false);
        //Sets the title of the window
        window.setTitle("Pixel Ranch");
        //Set app's icon
        Image icon;
        try {
            icon = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/player/down2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setIconImage(icon);
        //Create the game panel
        GamePanel panel = new GamePanel();
        //Add the panel to the main window of the game
        window.add(panel);

        //Automatically set the size of the window to fit the contents
        window.pack();

        //Center the window on the screen
        window.setLocationRelativeTo(null);
        //Make the window visible to the user
        window.setVisible(true);
        //Call the setUpGame method on the panel to initialize the game
        panel.setUpGame();
        //Start the game loop by calling the startGameThread method on the panel
        panel.startGameThread();
    }
}
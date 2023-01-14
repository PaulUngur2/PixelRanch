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
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pixel Ranch");
        Image icon;
        try {
            icon = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/player/down2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setIconImage(icon);
        GamePanel panel = new GamePanel();
        window.add(panel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        panel.setUpGame();
        panel.startGameThread();

    }
}
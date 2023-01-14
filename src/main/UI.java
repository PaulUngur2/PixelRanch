package main;

import game.interfaces.GameLogic;
import game.interfaces.GameLogicAnimals;
import game.interfaces.GameLogicCrops;
import game.interfaces.GameLogicPlayer;
import types.CropType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class UI {
    GamePanel panel;
    GameLogicPlayer player;
    GameLogicCrops crops;
    GameLogicAnimals animals;
    GameLogic game;
    Font pressStartK;
    Graphics2D g2d;
    JTextField textField;

    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> messagesCounter = new ArrayList<>();

    public String name = "";
    public int commandNum, interaction, pauseCommandNum, difficulty = 0;
    public int numberOf = 1;
    public int titleScreenState, buyScreenState, sellScreenState, fieldFarmScreenState, fieldScreenState = 0;

    public UI(GamePanel panel, GameLogicPlayer player, GameLogicCrops crops, GameLogicAnimals animals, GameLogic game) {
        this.panel = panel;
        this.player = player;
        this.crops = crops;
        this.animals = animals;
        this.game = game;
        textField = new JTextField(5);
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/prstartk.ttf");
            pressStartK = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(pressStartK);
        g2d.setColor(Color.WHITE);

        if (panel.gameState == panel.playState) {
            drawPlayerStats();
            drawMessage();
        } else if (panel.gameState == panel.pauseState) {
            drawPauseScreen();
        } else if (panel.gameState == panel.menuState) {

            switch (interaction) {
                case 1 -> drawShopWindow();
                case 2 -> drawFieldWindow();
                case 3 -> drawFarmWindow();
            }
            drawPlayerStats();
            drawMessage();
        } else if (panel.gameState == panel.titleScreen) {
            drawTitleScreen();
        }
    }
    public void addMessage(String message) {
        messages.add(message);
        messagesCounter.add(0);
    }
    public void drawPlayerStats() {
        g2d.setFont(pressStartK.deriveFont(Font.BOLD,10f));
        g2d.drawString("MONEY: " + player.getBalance(), 600, 570);
        g2d.drawString("DAY: " + player.getDays(), 470, 570);
        g2d.drawString("NEXT BILLS: " + game.getBills(), 180, 570);
        g2d.drawString("NAME: " + player.getName(), 50, 570);
    }

    public void drawMessage() {

        int messageY = panel.tilesSize * 8;
        int messageLimit = 4;
        g2d.setFont(pressStartK.deriveFont(Font.BOLD,15F));
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                int messageX = centerText(messages.get(i));
                if (panel.gameState == panel.playState) {
                    // Draw a rectangle under the text
                    int rectX = messageX - 5;
                    int rectY = messageY - 15;
                    Color color = new Color(0, 0, 0, 220);
                    g2d.setColor(color);
                    int rectWidth = g2d.getFontMetrics().stringWidth(messages.get(i)) + 20;
                    int rectHeight = 20;
                    g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 15, 15);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawString(messages.get(i), messageX + 2, messageY + 2);
                g2d.setColor(Color.WHITE);
                g2d.drawString(messages.get(i), messageX, messageY);

                int counter = messagesCounter.get(i) + 1;
                messagesCounter.set(i, counter);
                messageY += 20;
                if (messages.size() > messageLimit) {
                    messages.remove(i);
                    messagesCounter.remove(i);
                }
                if (messagesCounter.get(i) > 180) {
                    messages.remove(i);
                    messagesCounter.remove(i);
                }

            }
        }
    }
    public void drawTitleScreen() {
        switch (titleScreenState) {
            case 0 -> titleScreen();
            case 1 -> startScreen();
        }
    }

    public void drawPauseScreen(){
        int x = panel.tilesSize * 2;
        int y = (int) (panel.tilesSize * 3.5);
        int width = panel.screenWidth - (panel.tilesSize * 4);
        int height = panel.tilesSize * 5;
        Color color = new Color(0, 0, 0, 220);
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y, width, height, 35, 35);

        g2d.setFont(pressStartK.deriveFont(30f));
         x = centerText("PAUSED");
         y = (int) (panel.screenHeight / 2.50);
        g2d.drawString("PAUSED", x, y);
        g2d.setFont(pressStartK.deriveFont(15f));
        g2d.drawString("CONTINUE", x, y + panel.tilesSize);
        if (pauseCommandNum == 0) {
            g2d.drawString(">", x - panel.tilesSize/2, y + panel.tilesSize);
        }
        g2d.drawString("RESTART", x, (int) (y + panel.tilesSize * 1.5));
        if (pauseCommandNum == 1) {
            g2d.drawString(">", x - panel.tilesSize/2, (int) (y + panel.tilesSize * 1.5));
        }
        g2d.drawString("QUIT", x, y + panel.tilesSize * 2);
        if (pauseCommandNum == 2) {
            g2d.drawString(">", x - panel.tilesSize/2, y + panel.tilesSize * 2);
        }
        drawPlayerStats();
    }

    public void drawShopWindow(){
        g2d.setFont(pressStartK.deriveFont(20F));
        drawMenuScreen();
        drawTextMenu("SHOP", panel.tilesSize * 3, (int) (panel.tilesSize * 1.5));
        if(buyScreenState == 0 && sellScreenState == 0){
            g2d.setFont(pressStartK.deriveFont(15F));
            drawTextMenu("BUY", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
            if (commandNum == 0) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
            }

            drawTextMenu("SELL", panel.tilesSize * 4, panel.tilesSize * 3);
            if (commandNum == 1) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
            }
        }else if (buyScreenState == 1) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("WHAT DO YOU WANT TO BUY?", panel.tilesSize * 3, panel.tilesSize * 2);

            drawCrops();
            drawCropsNumber();
            drawMultiplier();
            drawTips();
        } else if (sellScreenState == 1) {

            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("WHAT DO YOU WANT TO SELL?", panel.tilesSize * 3, panel.tilesSize * 2);

            drawSellAnimalsProduce();
            drawProduceNumber();
            drawMultiplier();
            drawTips();
        }
        drawTips();
    }

    public void drawFieldWindow(){
        g2d.setFont(pressStartK.deriveFont(20F));
        drawMenuScreen();
        drawTextMenu("FIELD", panel.tilesSize * 3, (int) (panel.tilesSize * 1.5));
        switch (fieldScreenState) {
            case 0 -> {
                g2d.setFont(pressStartK.deriveFont(15F));
                drawTextMenu("PLANT", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
                if (commandNum == 0) {
                    g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
                }
                drawTextMenu("COLLECT", panel.tilesSize * 4, panel.tilesSize * 3);
                if (commandNum == 1) {
                    g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
                }
                drawTextMenu("WATER", panel.tilesSize * 4, (int) (panel.tilesSize * 3.5));
                if (commandNum == 2) {
                    g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 3.5));
                }
            }
            case 1 -> {
                g2d.setFont(pressStartK.deriveFont(10F));
                drawTextMenu("WHAT DO YOU WANT TO PLANT? YOU'LL NEED 5 SEEDS", panel.tilesSize * 3, panel.tilesSize * 2);
                drawCrops();
                drawCropsNumber();
                drawBuyFieldPrice();
                drawTips();
            }
            case 2 -> {
                g2d.setFont(pressStartK.deriveFont(10F));
                drawTextMenu("WHICH CROP DO YOU WANT TO COLLECT?", panel.tilesSize * 3, panel.tilesSize * 2);
                drawFieldText();
                drawTips();
            }
            case 3 -> {
                g2d.setFont(pressStartK.deriveFont(10F));
                drawTextMenu("WHICH CROP DO YOU WANT TO WATER?", panel.tilesSize * 3, panel.tilesSize * 2);
                drawFieldText();
                drawTips();
            }
            case 4 -> {
                g2d.setFont(pressStartK.deriveFont(10F));
                drawTextMenu("WHICH FIELD DO YOU WANT TO PLANT?", panel.tilesSize * 3, panel.tilesSize * 2);
                drawFieldText();
                drawTips();
            }
        }
        drawTips();
    }

    public void drawFarmWindow(){
        g2d.setFont(pressStartK.deriveFont(20F));
        drawMenuScreen();
        drawTextMenu("FARM", panel.tilesSize * 3, (int) (panel.tilesSize * 1.5));
        if(fieldFarmScreenState == 0){
            g2d.setFont(pressStartK.deriveFont(15F));
            drawTextMenu("BUY ANIMALS", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
            if (commandNum == 0) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
            }
            drawTextMenu("KILL ANIMALS", panel.tilesSize * 4, panel.tilesSize * 3);
            if (commandNum == 1) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
            }
            drawTextMenu("COLLECT PRODUCE", panel.tilesSize * 4, (int) (panel.tilesSize * 3.5));
            if (commandNum == 2) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 3.5));
            }
            drawTextMenu("FEED ANIMALS", panel.tilesSize * 4, panel.tilesSize * 4);
            if (commandNum == 3) {
                g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 4);
            }
        } else if (fieldFarmScreenState == 1) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("WHICH ANIMAL DO YOU WANT TO BUY?", panel.tilesSize * 3, panel.tilesSize * 2);
            drawAnimals();
            drawTextMenu("BUY FIELD", panel.tilesSize * 4, panel.tilesSize * 5);
            drawBuyFieldFarmPrice();
            drawTips();
        } else if (fieldFarmScreenState == 2) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("WHICH ANIMAL DO YOU WANT TO KILL?", panel.tilesSize * 3, panel.tilesSize * 2);
            drawFieldFarmText();
            drawTips();
        } else if (fieldFarmScreenState == 3) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("FROM WHICH DO YOU WANT TO COLLECT PRODUCE?", panel.tilesSize * 3, panel.tilesSize * 2);
            drawFieldFarmText();
            drawTips();
        } else if (fieldFarmScreenState == 4) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("WHICH ANIMAL DO YOU WANT TO FEED? YOU'LL NEED 5 FOOD", panel.tilesSize * 3, panel.tilesSize * 2);
            drawFieldFarmText();
            drawTips();
        } else if (fieldFarmScreenState == 5) {
            g2d.setFont(pressStartK.deriveFont(10F));
            drawTextMenu("IN WHICH FIELD DO YOU WANT TO PLACE THE ANIMAL", panel.tilesSize * 3, panel.tilesSize * 2);
            drawFieldFarmText();
            drawTips();
        }
        drawTips();
    }

    private void drawAnimals(){
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu("PIG", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
        if (commandNum == 0) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
        }

        drawTextMenu("CHICKEN", panel.tilesSize * 4, panel.tilesSize * 3);
        if (commandNum == 1) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
        }

        drawTextMenu("RABBIT", panel.tilesSize * 4, (int) (panel.tilesSize * 3.5));
        if (commandNum == 2) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 3.5));
        }

        drawTextMenu("COW", panel.tilesSize * 4, panel.tilesSize * 4);
        if (commandNum == 3) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 4);
        }

        drawTextMenu("SHEEP", panel.tilesSize * 4, (int) (panel.tilesSize * 4.5));
        if (commandNum == 4) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 4.5));
        }
    }
    private void startScreen() {
        g2d.setColor(Color.WHITE);
        g2d.setFont(pressStartK.deriveFont(15F));
        String intro = "After losing almost all of your money in the \ncrypto crash, you return to your grandparents' \nold farm in the countryside to make a living by \nselling animal produce."
                + "Despite your limited funds \nand growing bills, you work hard to make the farm \na success, gradually expanding your operation and overcoming \nthe challenges of the current times.";
        int y = panel.tilesSize * 2;
        int i = 1;
        for(String line : intro.split("\n")) {
            g2d.drawString(line.toUpperCase(), panel.tilesSize/2 , y);
            y += g2d.getFontMetrics().getHeight() + 10;
            i++;
        }

        g2d.setFont(pressStartK.deriveFont(20F));
        String text = "WHAT IS YOUR NAME?";
        int x = centerText(text);
        y = panel.tilesSize * 7;
        g2d.drawString(text, x, y);

        text = "_____";
        x = centerText(text);
        y = (int) (panel.tilesSize * 8.5);
        g2d.drawString(text, x, y);

        y = (int) (panel.tilesSize * 8.25);
        g2d.drawString(name, x, y);

        g2d.setFont(pressStartK.deriveFont(10F));
        g2d.drawString("CONTROLS:", panel.tilesSize, panel.tilesSize * 11);
        g2d.drawString("WASD - MOVE   ESC - PAUSE/BACK   1 - END DAY   ENTER - INTERACT", panel.tilesSize, (int) (panel.tilesSize * 11.5));

        try {
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/down1.png"))), panel.tilesSize * 10, panel.tilesSize * 8, panel.tilesSize, panel.tilesSize, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void titleScreen() {

        try {
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/down1.png"))), (int) (panel.tilesSize * 7.5), (int) (panel.tilesSize * 4.5), panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/grass.png"))), panel.tilesSize * 13, panel.tilesSize * 9, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/grass.png"))), panel.tilesSize * 12, panel.tilesSize * 9, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/grass.png"))), panel.tilesSize * 13, panel.tilesSize * 8, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/grass.png"))), panel.tilesSize * 14, panel.tilesSize * 9, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/grass.png"))), panel.tilesSize * 13, panel.tilesSize * 10, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/gravel.png"))), panel.tilesSize * 4, panel.tilesSize * 7, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/gravel.png"))), panel.tilesSize * 4, panel.tilesSize * 6, panel.tilesSize, panel.tilesSize, null);
            g2d.drawImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tile/gravel.png"))), panel.tilesSize * 2, panel.tilesSize * 5, panel.tilesSize, panel.tilesSize, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g2d.setFont(pressStartK.deriveFont(10F));
        g2d.setColor(Color.WHITE);
        String text = "PRESS ENTER TO START";
        int x = centerText(text);
        int y = (int) (panel.tilesSize * 7.5);
        g2d.drawString(text,x , y);

        g2d.setFont(pressStartK.deriveFont(60f));
        text = "PIXEL RANCH";
        x = centerText(text);
        y = panel.tilesSize * 4;
        g2d.drawString(text, x, y);

        g2d.setFont(pressStartK.deriveFont(43f));
        text = "START GAME";
        x = centerText(text);
        y = panel.tilesSize * 7;
        g2d.drawString(text, x, y);
        if (commandNum == 0) {
            g2d.drawString(">", x - panel.tilesSize, y);
        }

        text = "QUIT";
        x = centerText(text);
        y = panel.tilesSize * 9;
        g2d.drawString(text, x, y);
        if (commandNum == 1) {
            g2d.drawString(">", x - panel.tilesSize, y);
        }

        g2d.setFont(pressStartK.deriveFont(20F));
        g2d.drawString("DIFFICULTY", panel.tilesSize,panel.tilesSize * 10);

        g2d.setFont(pressStartK.deriveFont(15F));
        x = (int) (panel.tilesSize * 1.5);
        y = panel.tilesSize * 11;
        if (difficulty == 0) {
            g2d.drawString("< NORMAL >", x, y);
        } else if (difficulty == 1) {
            g2d.drawString("< HARD >", x, y);
        }
    }

    private void drawSellAnimalsProduce() {

        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu("PORK", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
        if (commandNum == 0) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
        }

        drawTextMenu("CHICKEN MEAT", panel.tilesSize * 4, panel.tilesSize * 3);
        if (commandNum == 1) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
        }

        drawTextMenu("RABBIT MEAT", panel.tilesSize * 4, (int) (panel.tilesSize * 3.5));
        if (commandNum == 2) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 3.5));
        }

        drawTextMenu("BEEF", panel.tilesSize * 4, panel.tilesSize * 4);
        if (commandNum == 3) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 4);
        }

        drawTextMenu("MUTTON", panel.tilesSize * 4, (int) (panel.tilesSize * 4.5));
        if (commandNum == 4) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 4.5));
        }

        drawTextMenu("EGGS", panel.tilesSize * 4, panel.tilesSize * 5);
        if (commandNum == 5) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 5);
        }

        drawTextMenu("MILK", panel.tilesSize * 4, (int) (panel.tilesSize * 5.5));
        if (commandNum == 6) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 5.5));
        }

        drawTextMenu("WOOL", panel.tilesSize * 4, panel.tilesSize * 6);
        if (commandNum == 7) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 6);
        }

        drawTextMenu("HIDE", panel.tilesSize * 4, (int) (panel.tilesSize * 6.5));
        if (commandNum == 8) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 6.5));
        }
    }

    private void drawCrops() {
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu("WHEAT", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
        if (commandNum == 0) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 2.5));
        }

        drawTextMenu("CORN", panel.tilesSize * 4, panel.tilesSize * 3);
        if (commandNum == 1) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 3);
        }

        drawTextMenu("POTATOES", panel.tilesSize * 4, (int) (panel.tilesSize * 3.5));
        if (commandNum == 2) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 3.5));
        }

        drawTextMenu("CARROTS", panel.tilesSize * 4, panel.tilesSize * 4);
        if (commandNum == 3) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 4);
        }

        drawTextMenu("HAY", panel.tilesSize * 4, (int) (panel.tilesSize * 4.5));
        if (commandNum == 4) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * 4.5));
        }
    }

    public void drawFieldText(){
        g2d.setFont(pressStartK.deriveFont(15F));
        int numFields = crops.getField().size();
        int i = 0;
        if (numFields == 0){
            drawTextMenu("YOU DO NOT OWN ANY FIELDS", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
        } else {
            for (String key : crops.getField().keySet()) {
                if (crops.getField().get(key).equals("empty")) {
                    drawTextMenu("FIELD", panel.tilesSize * 4, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    drawTextMenu(crops.getField().get(key).toUpperCase(), panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    if (commandNum == i) {
                        g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    }
                } else {
                    drawTextMenu(crops.getField().get(key).toUpperCase(), panel.tilesSize * 4, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    if (commandNum == i) {
                        g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    }
                    switch (fieldScreenState) {
                        case 2 -> {
                            if (CropType.valueOf(crops.getField().get(key)).getGrowTime() - crops.getSincePlanted().get(key) != 0) {
                                drawTextMenu((CropType.valueOf(crops.getField().get(key)).getGrowTime() - crops.getSincePlanted().get(key)) + " DAY/S LEFT", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            } else {
                                drawTextMenu("READY TO HARVEST", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            }
                        }
                        case 3 -> {
                            try {
                                if (crops.getWatered().get(key)) {
                                    drawTextMenu("WATERED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                                } else {
                                    drawTextMenu("NOT WATERED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                                }
                            } catch (NullPointerException e) {
                                drawTextMenu("NOT WATERED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    public void drawFieldFarmText(){
        g2d.setFont(pressStartK.deriveFont(15F));
        int numFields = animals.getField().size();
        int i = 0;
        if (numFields == 0){
            drawTextMenu("YOU DO NOT OWN ANY FIELDS", panel.tilesSize * 4, (int) (panel.tilesSize * 2.5));
        } else {
            for (String key : animals.getField().keySet()) {
                if (animals.getField().get(key).equals("empty")) {
                    drawTextMenu("FIELD", panel.tilesSize * 4, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    drawTextMenu(animals.getField().get(key).toUpperCase(), panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    if (commandNum == i) {
                        g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    }
                } else {
                    drawTextMenu(animals.getField().get(key).toUpperCase(), panel.tilesSize * 4, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    if (commandNum == i) {
                        g2d.drawString(">", (int) (panel.tilesSize * 3.5), (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                    }
                    switch (fieldFarmScreenState) {
                        case 3 -> {
                            if (animals.getCollected().get(key)) {
                                drawTextMenu("NOT COLLECTED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            } else {
                                drawTextMenu("COLLECTED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            }
                        }
                        case 4 -> {
                            if (animals.getFed().get(key)) {
                                drawTextMenu("FED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            } else {
                                drawTextMenu("NOT FED", panel.tilesSize * 8, (int) (panel.tilesSize * (2.5 + (0.5) * i)));
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    public void drawCropsNumber(){
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu(cropsNumber("Wheat"), panel.tilesSize * 8, (int) (panel.tilesSize * 2.5));
        drawTextMenu(cropsNumber("Corn"), panel.tilesSize * 8, panel.tilesSize * 3);
        drawTextMenu(cropsNumber("Potatoes"), panel.tilesSize * 8, (int) (panel.tilesSize * 3.5));
        drawTextMenu(cropsNumber("Carrots"), panel.tilesSize * 8, panel.tilesSize * 4);
        drawTextMenu(cropsNumber("Hay"), panel.tilesSize * 8, (int) (panel.tilesSize * 4.5));
    }

    public String cropsNumber(String type){
        String numberOf = "0";
        try {
            numberOf = String.valueOf(crops.getAmount().get(type));
            if (numberOf.equals("null")) {
                return  "0";
            }
        }catch (NullPointerException e){
            return numberOf;
        }
        return numberOf;
    }
    public void drawProduceNumber(){
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu(produceNumber("Pork"), panel.tilesSize * 8, (int) (panel.tilesSize * 2.5));
        drawTextMenu(produceNumber("Chicken_Meat"), panel.tilesSize * 8, panel.tilesSize * 3);
        drawTextMenu(produceNumber("Rabbit_Meat"), panel.tilesSize * 8, (int) (panel.tilesSize * 3.5));
        drawTextMenu(produceNumber("Beef"), panel.tilesSize * 8, panel.tilesSize * 4);
        drawTextMenu(produceNumber("Mutton"), panel.tilesSize * 8, (int) (panel.tilesSize * 4.5));
        drawTextMenu(produceNumber("Eggs"), panel.tilesSize * 8, panel.tilesSize * 5);
        drawTextMenu(produceNumber("Milk"), panel.tilesSize * 8, (int) (panel.tilesSize * 5.5));
        drawTextMenu(produceNumber("Wool"), panel.tilesSize * 8, panel.tilesSize * 6);
        drawTextMenu(produceNumber("Hide"), panel.tilesSize * 8, (int) (panel.tilesSize * 6.5));
    }
    public String produceNumber(String type){
        String numberOf = "0";
        try {
            numberOf = String.valueOf(animals.getProduceType().get(type));
            if (numberOf.equals("null")) {
                return  "0";
            }
        }catch (NullPointerException e){
            return numberOf;
        }
        return numberOf;
    }

    public void drawBuyFieldPrice(){
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu("BUY FIELD", panel.tilesSize * 4, panel.tilesSize * 5);
        if (commandNum == 5) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 5);
        }
        int numFields = crops.getField().size();
        if(numFields > 0){
            drawTextMenu("PRICE: " + (int) (100 * Math.pow(1.3, numFields)), panel.tilesSize * 4, panel.tilesSize * 6);
        }else {
            drawTextMenu("FIRST FIELD IS FREE", panel.tilesSize * 4, panel.tilesSize * 6);
        }
    }

    public void drawBuyFieldFarmPrice(){
        g2d.setFont(pressStartK.deriveFont(15F));
        drawTextMenu("BUY FIELD", panel.tilesSize * 4, panel.tilesSize * 5);
        if (commandNum == 5) {
            g2d.drawString(">", (int) (panel.tilesSize * 3.5), panel.tilesSize * 5);
        }
        int numFields = animals.getField().size();
        if(numFields > 0){
            drawTextMenu("PRICE: " + (int) (100 * Math.pow(1.3, numFields)), panel.tilesSize * 4, panel.tilesSize * 6);
        }else {
            drawTextMenu("FIRST FIELD IS FREE", panel.tilesSize * 4, panel.tilesSize * 6);
        }
    }

    public int centerText(String text){
        int length = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return (panel.screenWidth - length) / 2;
    }

    public void drawMenuScreen(){
        int x = panel.tilesSize * 2;
        int y = panel.tilesSize / 2;
        int width = panel.screenWidth - (panel.tilesSize * 4);
        int height = panel.tilesSize * 10;
        drawMenuWindow(x, y, width, height);
    }

    public void drawMultiplier (){
        g2d.setFont(pressStartK.deriveFont(20F));
        drawTextMenu("x" + numberOf, panel.tilesSize * 12, (int) (panel.tilesSize * 1.5));
        g2d.setFont(pressStartK.deriveFont(10F));

        drawTextMenu("PRESS SPACE TO MULTIPLY", panel.tilesSize * 8, panel.tilesSize * 10);
    }

    public void drawTips(){
        g2d.setFont(pressStartK.deriveFont(10F));
        drawTextMenu("PRESS ESC TO GO BACK", (int) (panel.tilesSize * 3.5), panel.tilesSize * 10);
    }

    public void drawMenuWindow(int x, int y, int width, int height){
        Color color = new Color(0, 0, 0, 220);
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x, y, width, height, 35, 35);

    }

    public void drawTextMenu(String text, int x, int y){
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }

}




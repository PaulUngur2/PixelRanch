package game;

import game.interfaces.GameLogicPlayer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PlayerLogic implements GameLogicPlayer {
    private String name;
    private int balance;
    private int days;
    Properties prop = new Properties();
    InputStream input = getClass().getClassLoader().getResourceAsStream("difficulty/difficulty.conf");

    public PlayerLogic(String name) {
        configDifficulty();
        this.name = name;
        this.days = 1;
    }

    public void configDifficulty() {

        try {
            // load a properties file
            prop.load(input);

            // get the value of the difficulty property
            String difficulty = prop.getProperty("difficulty");

            // if the difficulty is "hard", the starting balance will be 500 else 1000
            if (difficulty.equals("hard")) {
                this.balance = 500;
            } else {
                this.balance = 1000;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConfigDifficulty(String difficulty) throws IOException {
        prop.load(input);
        prop.setProperty("difficulty", difficulty);
        try (FileOutputStream out = new FileOutputStream("resources/difficulty/difficulty.conf")) {
            prop.store(out, "Updating difficulty property");
        }
    }

    public int getDays() {
        return days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void dayPassed () {
        this.days++;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
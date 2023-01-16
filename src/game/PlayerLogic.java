package game;

import game.interfaces.GameLogicPlayer;
import types.AnimalType;
import types.CropType;
import types.ShopPrices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PlayerLogic implements GameLogicPlayer {
    //Player variables
    private String name;
    private int balance;
    private int days;
    Properties prop = new Properties();
    InputStream input = this.getClass().getClassLoader().getResourceAsStream("difficulty/difficulty.conf");
    //Constructor
    public PlayerLogic(String name) {

        configDifficulty();
        this.name = name;
        this.days = 1;
        //Checks the difficulty when the player constructor is called to adjust the prices
        try {
            input = this.getClass().getClassLoader().getResourceAsStream("difficulty/difficulty.conf");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //Difficulty loader
    public void configDifficulty() {

        try {
            //Loads the properties file
            prop.load(input);

            //Gets the value of the difficulty property
            String difficulty = prop.getProperty("difficulty");

            //If the difficulty is "hard", the starting balance will be 500 else 1000
            if (difficulty.equals("hard")) {
                this.balance = 500;
            } else {
                this.balance = 1000;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Sets the difficulty based on the choice in the title screen
    public void setConfigDifficulty(String difficulty) throws IOException {

        prop.load(input);
        prop.setProperty("difficulty", difficulty);
        try (FileOutputStream out = new FileOutputStream("resources/difficulty/difficulty.conf")) {
            //Save the updated properties to the specified file, with a comment "Updating difficulty property"
            prop.store(out, "Updating difficulty property");
        }
    }
    //Gets the cost of the seed crops based on the difficulty
    public int getCostCrops(String type) {

        int cost = CropType.valueOf(type).getCost();
        String difficulty = prop.getProperty("difficulty");

        //If the difficulty is "hard", return the cost multiplied by 1.5
        if (difficulty.equals("hard")) {
            return (int) (cost * 1.5);
        } else {
            return cost;
        }
    }
    //Gets the cost of the animals based on the difficulty
    public int getCostAnimals(String type) {

        int cost = AnimalType.valueOf(type).getCost();
        String difficulty = prop.getProperty("difficulty");

        //If the difficulty is "hard", return the cost multiplied by 1.5
        if (difficulty.equals("hard")) {
            return (int) (cost * 1.5);
        } else {
            return cost;
        }
    }
    //Gets the value of the produce based on the difficulty
    public int getValue(String type) {

        int value = ShopPrices.valueOf(type).getValue();
        String difficulty = prop.getProperty("difficulty");

        //If the difficulty is "hard", return the cost divided by 1.5
        if (difficulty.equals("hard")) {
            return (int) (value / 1.5);
        } else {
            return value;
        }
    }
    //Getters and Setters
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
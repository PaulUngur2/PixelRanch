package types;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum AnimalType {
    Pig("Potatoes", 50, "Pork"),
    Chicken("Corn", 30,  "Eggs", "Chicken_Meat"),
    Rabbit("Carrots", 40, "Hide", "Rabbit_Meat"),
    Cow("Wheat", 100, "Milk", "Hide", "Beef"),
    Sheep("Hay", 80, "Wool", "Mutton");

    private final String food;
    private final String[] produce;
    private final int cost;

    AnimalType(String food, int cost, String... produce) {
        this.food = food;
        this.produce = produce;
        this.cost = cost;
    }

    public String getFood() {
        return food;
    }

    public String[] getProduce() {
        return produce;
    }

    public int getCost() {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = this.getClass().getClassLoader().getResourceAsStream("difficulty/difficulty.conf");

            // load a properties file
            prop.load(input);

            // get the value of the difficulty property
            String difficulty = prop.getProperty("difficulty");

            // if the difficulty is "hard", return the cost multiplied by 1.5
            if (difficulty.equals("hard")) {
                return (int) (cost * 1.5);
            } else {
                return cost;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cost;
    }
    }


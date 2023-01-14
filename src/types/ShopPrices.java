package types;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ShopPrices {
    Wheat(5),
    Corn(4),
    Potatoes(3),
    Carrots(2),
    Hay(6),
    Pork(10),
    Eggs(5),
    Chicken_Meat(8),
    Hide(20),
    Rabbit_Meat(12),
    Beef(15),
    Wool(10),
    Mutton(12),
    Milk(7);

    private final int value;

    ShopPrices(int value) {
        this.value = value;
    }

    public int getValue() {
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
                return (int) (value * 1.5);
            } else {
                return value;
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
        return value;
    }
}

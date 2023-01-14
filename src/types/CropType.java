package types;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum CropType {
    Wheat(2,5),
    Corn(3,4),
    Potatoes(3,3),
    Carrots(4,2),
    Hay(1,6);

    private final int growTime;
    private final int cost;

    CropType(int growTime, int cost) {
        this.growTime = growTime;
        this.cost = cost;
    }

    public int getGrowTime() {
        return growTime;
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

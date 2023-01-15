package game.interfaces;

import java.io.IOException;
import java.util.ArrayList;

public interface GameLogicPlayer {
    int getBalance();
    void setBalance(int balance);
    int getDays();
    String getName();
    void setName(String name);
    void dayPassed();
    void configDifficulty();
    void setConfigDifficulty(String difficulty) throws IOException;
    void setDays(int days);
    int getCostCrops(String type);
    int getCostAnimals(String type);
    int getValue(String type);
}

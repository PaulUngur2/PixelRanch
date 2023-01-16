package game.interfaces;

import game.Animals;

import java.util.Map;

public interface GameLogicAnimals {
    //Interface for the Animals class
    void sell(int numberOf, String type) throws Animals.NotEnoughProduceException;
    void buy(String type, String field) throws Animals.FieldsException, Animals.NotEnoughMoneyException;
    void buyField() throws game.Animals.FieldsException, game.Animals.NotEnoughMoneyException;
    void collectProduce(String field) throws Animals.FieldsException;
    void kill(String field) throws Animals.FieldsException;
    void setOption(int option);
    void setType(String type);
    int getOption();
    String getType();
    Map<String, String> getField();
    Map<String, Boolean> getCollected();
    Map<String, Integer> getProduceType();
    Map<String, Boolean> getFed();
    Map<String, String> getFoodType();
    void setField(String field, String value);
    void setFed(String field, boolean value);
    void setCollected(String field, boolean value);

}

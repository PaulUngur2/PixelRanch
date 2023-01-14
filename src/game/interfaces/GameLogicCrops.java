package game.interfaces;

import game.Crops;

import java.util.Map;

public interface GameLogicCrops {
    void buy(int numberOf, String type) throws Crops.NotEnoughMoneyException;
    void water(String field) throws Crops.CropsException, Crops.FieldsException;
    void buyField() throws Crops.FieldsException, Crops.NotEnoughMoneyException;
    void collect(String field) throws Crops.CropsException, Crops.FieldsException;
    void plant(String type, String field) throws Crops.CropsException;
    boolean checkGrowth(String field) throws Crops.FieldsException;
    String getType();
    void setType(String type);
    int getOption();
    void setOption(int option);
    Map<String, Integer> getAmount();
    Map<String, String> getField();
    Map<String, Integer> getSincePlanted();
    Map<String, Boolean> getWatered();
    void setAmount(String foodType, Integer value);
    void setField(String field, String value);

}

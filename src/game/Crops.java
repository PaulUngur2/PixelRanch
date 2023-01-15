package game;

import game.interfaces.GameLogicCrops;
import game.interfaces.GameLogicPlayer;
import types.CropType;

import java.util.*;

public class Crops implements GameLogicCrops {
    private Map<String, Integer> costCrops = new HashMap<>();
    private Map<String, Integer> amount = new HashMap<>();
    private Map<String, Integer> growTime = new HashMap<>();
    private Map<String, Boolean> watered = new HashMap<>();
    private Map<String, Integer> sincePlanted = new HashMap<>();
    private Map<String, String> field = new LinkedHashMap<>();
    private Map<String, Boolean> isLoaded = new HashMap<>();

    private String type;
    private int option;

    Random random = new Random();
    GameLogicPlayer playerLogic;

    public Crops(GameLogicPlayer player) {
        this.playerLogic = player;
    }

    public void loadInfo(String type){
        if (!isLoaded.getOrDefault(type, false)) {
            switch (type) {
                case "Wheat" -> {
                    this.costCrops.put("Wheat", playerLogic.getCostCrops("Wheat"));
                    this.growTime.put("Wheat",CropType.Wheat.getGrowTime());
                    this.amount.put("Wheat",0);
                    isLoaded.put(type, true);
                }
                case "Corn" -> {
                    this.costCrops.put("Corn", playerLogic.getCostCrops("Corn"));
                    this.growTime.put("Corn",CropType.Corn.getGrowTime());
                    this.amount.put("Corn",0);
                    isLoaded.put(type, true);
                }
                case "Carrots" -> {
                    this.costCrops.put("Carrots", playerLogic.getCostCrops("Carrots"));
                    this.growTime.put("Carrots",CropType.Carrots.getGrowTime());
                    this.amount.put("Carrots",0);
                    isLoaded.put(type, true);
                }
                case "Hay" -> {
                    this.costCrops.put("Hay", playerLogic.getCostCrops("Hay"));
                    this.growTime.put("Hay",CropType.Hay.getGrowTime());
                    this.amount.put("Hay",0);
                    isLoaded.put(type, true);
                }
                case "Potatoes" -> {
                    this.costCrops.put("Potatoes", playerLogic.getCostCrops("Potatoes"));
                    this.growTime.put("Potatoes",CropType.Potatoes.getGrowTime());
                    this.amount.put("Potatoes",0);
                    isLoaded.put(type, true);
                }
            }
        }
    }

    public static class FieldsException extends Exception {
        public FieldsException(String message) {
            super(message.toUpperCase());
        }
    }

    public static class NotEnoughMoneyException extends Exception {
        public NotEnoughMoneyException(String message) {
            super(message.toUpperCase());
        }
    }

    public static class CropsException extends Exception {
        public CropsException(String message) {
            super(message.toUpperCase());
        }
    }

    public void buyField() throws FieldsException, NotEnoughMoneyException {
        int numFields = field.size(); // number of fields currently owned
        int price;
        if (numFields == 0) {
            price = 0; // first field is free
        } else {
            price = (int) (100 * Math.pow(1.3, numFields)); // prices increase by 30% with each purchase
        }
        if (numFields >= 6) {
            throw new FieldsException("Cannot buy any more fields.");
        }
        if (playerLogic.getBalance() < price) {
            throw new NotEnoughMoneyException("Not enough money to buy field.");
        }
        field.put("Field " + (numFields + 1), "empty"); // add a new field to the map
        playerLogic.setBalance(playerLogic.getBalance() - price); // subtract the price from the player's money
    }

    public boolean checkGrowth(String field) throws FieldsException {
        try {
            if (!this.field.get(field).equals("empty")) {
                return Objects.equals(this.sincePlanted.get(field), this.growTime.get(this.field.get(field)));
            }
            return false;
        } catch (NullPointerException e){
            throw new FieldsException("You need to buy that field");
        }
    }

    public void collect(String field) throws CropsException, FieldsException {

        int numberOf = ( random.nextInt(15) + 1);
        String name = this.field.get(field);
        try {
            if (checkGrowth(field)) {
                this.amount.put(name,this.amount.get(name) + numberOf);
                this.field.put(field, "empty");
                this.sincePlanted.put(field, 0);
            } else {
                throw new CropsException("There is nothing to collect.");
            }
        } catch (NullPointerException e){
            throw new FieldsException("You need to buy that field");
        }
        throw new CropsException("You got " + numberOf + " " + name);
    }

    public void plant(String type, String field) throws CropsException {
        try {
            if (this.field.get(field) == null) {
                throw new CropsException("You need to buy that field");
            } else if (this.field.get(field).equals("empty") && this.amount.get(type) >= 5) {
                this.field.put(field, type);
                this.sincePlanted.put(field, 1);
                this.amount.put(type, this.amount.get(type) - 5);
                this.watered.put(field, false);
            } else {
                throw new CropsException("There is something growing.");
            }
        } catch (NullPointerException e){
            throw new CropsException("Not enough seeds");
        }
    }

    public void buy(int numberOf, String type) throws NotEnoughMoneyException {
        loadInfo(type);
            if (playerLogic.getBalance() >= costCrops.get(type) * numberOf) {
                this.amount.put(type, this.amount.get(type) + numberOf);
                playerLogic.setBalance(playerLogic.getBalance() - numberOf * costCrops.get(type));
            } else {
                throw new NotEnoughMoneyException("You don't have enough money");
            }
    }

    public void water(String field) throws CropsException, FieldsException {
        if (this.watered.containsKey(field)) {
            if (!this.watered.get(field) || this.watered.get(field) == null) {
                this.watered.put(field, true);
            } else {
                throw new CropsException("It's already watered");
            }
        } else {
            throw new FieldsException("There is no field with this name");
        }
    }
    public void restart() {
        this.amount.clear();
        this.field.clear();
        this.sincePlanted.clear();
        this.watered.clear();
    }

    public Map<String, Integer> getAmount() {
        return amount;
    }

    public Map<String, String> getField() {
        return field;
    }
    public void setAmount(String foodType, Integer value) {
        this.amount.put(foodType, value);
    }

    public Map<String, Boolean> getWatered() {
        return watered;
    }

    public void setField(String field, String value) {
        this.field.put(field, value);
    }

    public Map<String, Integer> getSincePlanted() {
        return sincePlanted;
    }

    public void setSincePlanted(String field, Integer value) {
        this.sincePlanted.put(field, value);
    }

    public void setWatered(String field, Boolean value) {
        this.watered.put(field, value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}

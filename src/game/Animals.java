package game;

import game.interfaces.GameLogicAnimals;
import game.interfaces.GameLogicPlayer;
import types.AnimalType;
import types.ShopPrices;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Animals implements GameLogicAnimals {

    private Map<String, String> foodType = new HashMap<>();
    private Map<String,Integer> produceType = new HashMap<>();
    private Map<String, Boolean> fed = new HashMap<>();
    private Map<String, Boolean> collected = new HashMap<>();
    private Map<String, Integer> costAnimals = new HashMap<>();
    private Map<String, String> field = new LinkedHashMap<>();
    private Map<String, Boolean> isLoaded = new HashMap<>();

    private String type;
    private int option;

    Random random = new Random();
    GameLogicPlayer playerLogic;

    public Animals(GameLogicPlayer player) {
        this.playerLogic = player;
    }


    public void loadInfo(String type) {
        if (!isLoaded.getOrDefault(type, false)) {
            switch (type) {
                case "Pig" -> {
                    this.costAnimals.put("Pig", playerLogic.getCostAnimals("Pig"));
                    this.foodType.put("Pig",AnimalType.Pig.getFood());
                    String[] produce = AnimalType.Pig.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Chicken" -> {
                    this.costAnimals.put("Chicken", playerLogic.getCostAnimals("Chicken"));
                    this.foodType.put("Chicken",AnimalType.Chicken.getFood());
                    String[] produce = AnimalType.Chicken.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Rabbit" -> {
                    this.costAnimals.put("Rabbit", playerLogic.getCostAnimals("Rabbit"));
                    this.foodType.put("Rabbit",AnimalType.Rabbit.getFood());
                    String[] produce = AnimalType.Rabbit.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Cow" -> {
                    this.costAnimals.put("Cow", playerLogic.getCostAnimals("Cow"));
                    this.foodType.put("Cow",AnimalType.Cow.getFood());
                    String[] produce = AnimalType.Cow.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Sheep" -> {
                    this.costAnimals.put("Sheep", playerLogic.getCostAnimals("Sheep"));
                    this.foodType.put("Sheep",AnimalType.Sheep.getFood());
                    String[] produce = AnimalType.Sheep.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
            }
        }
    }

    public static class NotEnoughProduceException extends RuntimeException {
        public NotEnoughProduceException(String message) {
            super(message.toUpperCase());
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

    public void sell(int numberOf, String type) throws NotEnoughProduceException {
        try {
            int available = this.produceType.get(type);
            int amountToSell = Math.min(numberOf, available);
            this.produceType.put(type, available - amountToSell);
            playerLogic.setBalance(playerLogic.getBalance() + amountToSell * playerLogic.getValue(type));
        } catch (NullPointerException e) {
            throw new NotEnoughProduceException("You can't sell more of that.");
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
            playerLogic.setBalance(playerLogic.getBalance() - price); // subtract the price from the player's money}
    }

    public void buy(String type, String field) throws NotEnoughMoneyException, FieldsException {
        try {
            loadInfo(type);
            if (this.field.get(field).equals("empty")) {
                if (playerLogic.getBalance() >= this.costAnimals.get(type)) {
                    playerLogic.setBalance(playerLogic.getBalance() - this.costAnimals.get(type));
                    this.fed.put(field, false);
                    this.field.put(field, type);
                    this.collected.put(field, false);
                } else {
                    throw new NotEnoughMoneyException("Not enough money to buy it.");
                }
            } else {
                throw new FieldsException("The field is occupied");
            }
        }catch(NullPointerException e){
            throw new FieldsException("You need to buy that field");
        }
    }

    public void kill(String field) throws FieldsException {
        try {
            if (!this.field.get(field).equals("empty")) {
                int numberOf;

                switch (this.field.get(field)) {
                    case "Pig" -> {
                        numberOf = random.nextInt(8) + 1;
                        this.produceType.put("Pork", this.produceType.get("Pork") + numberOf);
                        this.field.put(field, "empty");
                        throw new FieldsException("You collected " + numberOf + " of pork.");
                    }
                    case "Chicken" -> {
                        numberOf = random.nextInt(5) + 1;
                        this.produceType.put("Chicken_Meat", this.produceType.get("Chicken_Meat") + numberOf);
                        this.field.put(field, "empty");
                        throw new FieldsException("You collected " + numberOf + " of chicken meat.");
                    }
                    case "Rabbit" -> {
                        numberOf = random.nextInt(4) + 1;
                        this.produceType.put("Rabbit_Meat", this.produceType.get("Rabbit_Meat") + numberOf);
                        this.produceType.put("Hide", this.produceType.get("Hide") + 1);
                        this.field.put(field, "empty");
                        throw new FieldsException("You collected " + numberOf + " of rabbit meat and 1 hide.");
                    }
                    case "Cow" -> {
                        numberOf = random.nextInt(10) + 1;
                        this.produceType.put("Beef", this.produceType.get("Beef") + numberOf);
                        this.produceType.put("Hide", this.produceType.get("Hide") + 2);
                        this.field.put(field, "empty");
                        throw new FieldsException("You collected " + numberOf + " of beef and 2 hides.");
                    }
                    case "Sheep" -> {
                        numberOf = random.nextInt(7) + 1;
                        this.produceType.put("Mutton", this.produceType.get("Mutton") + numberOf);
                        this.produceType.put("Wool", this.produceType.get("Wool") + 1);
                        this.field.put(field, "empty");
                        throw new FieldsException("You collected " + numberOf + " of mutton and 1 wool.");
                    }
                }
            } else {
                throw new FieldsException("There are no animals to kill.");
            }
        } catch (NullPointerException e ){
            throw new FieldsException("You need to buy that field");
        }
    }

    public void collectProduce(String field) throws FieldsException {
        try {
            if (!this.field.get(field).equals("empty") && this.collected.get(field).equals(false)) {
                int numberOf;
                this.collected.put(field, true);
                switch (this.field.get(field)) {
                    case "Pig" -> throw new FieldsException("Pigs have a sad fate.");
                    case "Chicken" -> {
                        numberOf = random.nextInt(6) + 1;
                        this.produceType.put("Eggs", this.produceType.get("Eggs") + numberOf);
                        throw new FieldsException("You collected " + numberOf + " of eggs.");
                    }
                    case "Rabbit" -> throw new FieldsException("Rabbits have a sad fate.");
                    case "Cow" -> {
                        numberOf = random.nextInt(7) + 1;
                        this.produceType.put("Milk", this.produceType.get("Milk") + numberOf);
                        throw new FieldsException("You collected " + numberOf + " of milk.");
                    }
                    case "Sheep" -> {
                        numberOf = random.nextInt(7) + 1;
                        this.produceType.put("Wool", this.produceType.get("Wool") + numberOf);
                        throw new FieldsException("You collected " + numberOf + " of wool.");
                    }
                }
            } else {
                throw new FieldsException("There is nothing to collect.");
            }
        } catch (NullPointerException e) {
            throw new FieldsException("You need to buy that field");
        }
    }

    public void restart() {
        this.field.clear();
        this.fed.clear();
        this.produceType.clear();
    }

    public Map<String, String> getFoodType() {
        return foodType;
    }

    public Map<String, Integer> getProduceType() {
        return produceType;
    }

    public Map<String, Boolean> getFed() {
        return fed;
    }

    public Map<String, String> getField() {
        return field;
    }

    public void setFed(String field, boolean value) {
        this.fed.put(field, value);
    }

    public void setField(String field, String value) {
        this.field.put(field, value);
    }

    public Map<String, Boolean> getCollected() {
        return collected;
    }

    public void setCollected(String field, boolean value) {
        this.collected.put(field, value);
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

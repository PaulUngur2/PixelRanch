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
                    this.costAnimals.put("Pig", AnimalType.Pig.getCost());
                    this.foodType.put("Pig",AnimalType.Pig.getFood());
                    String[] produce = AnimalType.Pig.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Chicken" -> {
                    this.costAnimals.put("Chicken",AnimalType.Chicken.getCost());
                    this.foodType.put("Chicken",AnimalType.Chicken.getFood());
                    String[] produce = AnimalType.Chicken.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Rabbit" -> {
                    this.costAnimals.put("Rabbit",AnimalType.Rabbit.getCost());
                    this.foodType.put("Rabbit",AnimalType.Rabbit.getFood());
                    String[] produce = AnimalType.Rabbit.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Cow" -> {
                    this.costAnimals.put("Cow",AnimalType.Pig.getCost());
                    this.foodType.put("Cow",AnimalType.Cow.getFood());
                    String[] produce = AnimalType.Cow.getProduce();
                    for (String p : produce) {
                        this.produceType.put(p, 0);
                    }
                    isLoaded.put(type, true);
                }
                case "Sheep" -> {
                    this.costAnimals.put("Sheep",AnimalType.Pig.getCost());
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
            playerLogic.setBalance(playerLogic.getBalance() + amountToSell * ShopPrices.valueOf(type).getValue());
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
                switch (this.field.get(field)) {
                    case "Pig" -> this.produceType.put("Pork", random.nextInt(5) + 1);
                    case "Chicken" -> this.produceType.put("Chicken_Meat", random.nextInt(3) + 1);
                    case "Rabbit" -> {
                        this.produceType.put("Rabbit_Meat", random.nextInt(2) + 1);
                        this.produceType.put("Hide", 1);
                    }
                    case "Cow" -> {
                        this.produceType.put("Beef", random.nextInt(3) + 1);
                        this.produceType.put("Hide", random.nextInt(2) + 1);
                    }
                    case "Sheep" -> {
                        this.produceType.put("Mutton", random.nextInt(3) + 1);
                    }
                }
                this.field.put(field, "empty");

            } else {
                throw new FieldsException("There are no " + this.field.get(field) + " to kill.");
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
                        this.produceType.put("Eggs", this.produceType.get(this.field.get(field)) + numberOf);
                        throw new FieldsException("You " + numberOf + " collected eggs.");
                    }
                    case "Rabbit" -> throw new FieldsException("Rabbits have a sad fate.");
                    case "Cow" -> {
                        numberOf = random.nextInt(7) + 1;
                        this.produceType.put("Milk", this.produceType.get(this.field.get(field)) + numberOf);
                        throw new FieldsException("You " + numberOf + "collected milk.");
                    }
                    case "Sheep" -> {
                        numberOf = random.nextInt(7) + 1;
                        this.produceType.put("Wool", this.produceType.get(this.field.get(field)) + numberOf);
                        throw new FieldsException("You " + numberOf + "collected wool.");
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

    public void setFoodType(String animal, String type) {
        this.foodType.put(animal, type);
    }
}

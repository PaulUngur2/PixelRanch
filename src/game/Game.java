package game;

import game.interfaces.GameLogic;
import types.CropType;

import java.sql.SQLException;


public class Game implements GameLogic {
    //Game variables
    public PlayerLogic playerLogic;
    public Animals animals;
    public Crops crops;
    public Database database;
    public int bills;

    //Constructor
    public Game() {

        this.playerLogic = new PlayerLogic("PLAYER");
        this.animals = new Animals (this.playerLogic);
        this.crops = new Crops(this.playerLogic);
        this.database = new Database(this.playerLogic);
        this.bills = 100;
    }
    //Custom exception
    public static class GameSystemException extends Exception {
        public GameSystemException(String message) {
            super(message.toUpperCase());
        }
    }
    //Feed method which makes use of the animals and crops classes, checking if the user has the crops needed to feed each certain animal
    public void feed(String field) throws GameSystemException {

        try {
            String type = animals.getField().get(field);
            if (!animals.getFed().get(field)) {
                if (crops.getAmount().get(animals.getFoodType().get(type)) >= 5) {
                    animals.setFed(field, true);
                    crops.setAmount(animals.getFoodType().get(type), crops.getAmount().get(animals.getFoodType().get(type)) - 5);
                } else {
                    throw new GameSystemException("Not enough food.");
                }
            } else {
                throw new GameSystemException("It's already fed.");
            }
        } catch (NullPointerException e) {
            throw new GameSystemException("You can't feed it with that.");
        }
    }
    //Method for passing the day, checks if the bills need to be paid, if they do, and you don't have enough money, the game stops until
    //you have enough money to pay the bills, or you restart the game
    public void day() throws GameSystemException, Crops.FieldsException {

        if (playerLogic.getDays() % 3 != 0) {
            dayChecks();
        } else if (playerLogic.getDays() % 3 == 0) {//Bills are paid every 3 days
            if(playerLogic.getBalance() >= bills ){
                playerLogic.setBalance(playerLogic.getBalance() - bills);
                //Bills increase each 3 days with the number of fields you have
                if (animals.getField().size() == 0 && crops.getField().size() == 0) {
                    bills += 100;
                } else if (crops.getField().size() == 0) {
                    bills = (bills * animals.getField().size()) + 50;
                } else if (animals.getField().size() == 0) {
                    bills = (bills * crops.getField().size()) + 50;
                } else {
                    bills = bills * (animals.getField().size() + crops.getField().size());//
                }
                dayChecks();
            }else{
                throw new GameSystemException("You don't have enough money to pay your bills.");
            }
        }
    }
    //Method for checking the growth of the crops and animals, and for checking if the animals are fed, if they aren't, they die/wither
    private void dayChecks() throws GameSystemException, Crops.FieldsException {

            String nameA = null, nameC = null;
            for (String key : animals.getFed().keySet()) {
                if (!animals.getFed().get(key) && animals.getFed().get(key) != null && animals.getFed() != null && !animals.getField().get(key).equals("empty")) {
                    nameA = animals.getField().get(key);
                    animals.setField(key, "empty");
                } else {
                    animals.setFed(key, false);
                }
                if (animals.getCollected().get(key)) {
                    animals.setCollected(key, false);
                }
            }
            for (String key : crops.getField().keySet()) {
                if (crops.getWatered() != null && crops.getWatered().get(key) != null && !crops.getWatered().get(key) && !crops.getField().get(key).equals("empty")) {
                    nameC = crops.getField().get(key);
                    crops.setField(key, "empty");
                } else {
                    crops.setWatered(key,false);
                }
                if (crops.getSincePlanted().get(key) != null && crops.getSincePlanted().get(key) < CropType.valueOf(crops.getField().get(key)).getGrowTime()) {
                    crops.setSincePlanted(key, crops.getSincePlanted().get(key) + 1);
                }
            }
            playerLogic.dayPassed();
            if (nameA != null) {
                throw new GameSystemException(nameA + " has died.");
            } else if (nameC != null) {
                throw new GameSystemException(nameC + " has withered away.");
            } else if (nameC != null && nameA != null) {
                throw new GameSystemException("Crops and animals died trough the night.");
            }
    }
    //Method that calls the other restart methods when the player wants to restart the game
    public void restart() {
        playerLogic.setBalance(1000);
        playerLogic.setDays(0);
        crops.restart();
        animals.restart();
    }
    //Method for saving the game, it calls the database class to save the game
    public void shutdownHook(Database database, String playerName) {
        try {
            database.updateUser(playerName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Getter for the bills variable
    public int getBills() {
        return bills;
    }
}

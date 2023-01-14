package game;

import game.interfaces.GameLogic;

import java.sql.SQLException;
import java.util.Scanner;

public class Game implements GameLogic {
    public PlayerLogic playerLogic;
    public Animals animals;
    public Crops crops;
    public Database database;
    public int bills;

    public Game() {
        this.playerLogic = new PlayerLogic("PLAYER");
        this.animals = new Animals (this.playerLogic);
        this.crops = new Crops(this.playerLogic);
        this.database = new Database(this.playerLogic);
        this.bills = 100;
    }

    public static class GameSystemException extends Exception {
        public GameSystemException(String message) {
            super(message.toUpperCase());
        }
    }

    public void feed(String field) throws GameSystemException {
        try {
            String type = animals.getField().get(field);
            if (!animals.getFed().get(field)) {
                if (crops.getAmount().get(animals.getFoodType().get(type)) > 5) {
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

    public void day() throws GameSystemException, Crops.FieldsException {
        if (playerLogic.getDays() % 3 != 0) {
            dayChecks();
        } else if (playerLogic.getDays() % 3 == 0) {
            if(playerLogic.getBalance() >= bills ){
                playerLogic.setBalance(playerLogic.getBalance() - bills);
                if (animals.getField().size() == 0 && crops.getField().size() == 0) {
                    bills += 100;
                } else if (crops.getField().size() == 0) {
                    bills = (bills * animals.getField().size()) + 50;
                } else if (animals.getField().size() == 0) {
                    bills = (bills * crops.getField().size()) + 50;
                } else {
                    bills = bills * (animals.getField().size() + crops.getField().size());
                }
                dayChecks();
            }else{
                throw new GameSystemException("You don't have enough money to pay your bills.");
            }
        }
    }

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
                if (crops.getSincePlanted().get(key) != null) {
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

    public void restart() {
        playerLogic.setBalance(1000);
        playerLogic.setDays(0);
        crops.restart();
        animals.restart();
    }

    public void shutdownHook(Database database, String playerName) {
        try {
            database.updateUser(playerName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBills() {
        return bills;
    }
}

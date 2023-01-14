package tests;

import game.Crops;
import game.Game;
import game.interfaces.GameLogic;
import game.interfaces.GameLogicAnimals;
import game.interfaces.GameLogicCrops;
import game.interfaces.GameLogicPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    Game game = new Game();

    @Test
    public void testDay() throws Game.GameSystemException, Crops.FieldsException, Crops.CropsException {
        GameLogic gameLogic = game;
        GameLogicPlayer player = game.playerLogic;
        GameLogicCrops crops = game.crops;
        GameLogicAnimals animals = game.animals;

        player.setDays(1);
        player.setBalance(1000);
        animals.getField().put("Field 1", "Chicken");
        crops.getField().put("Field 1", "Wheat");
        gameLogic.day();
        assertEquals(1000, player.getBalance());
        animals.setFed("Field 1", true);
        crops.water("Field 1");
        assertTrue(animals.getFed().get("Field 1"));
        assertTrue(crops.getWatered().get("Field 1"));
        animals.setCollected("Field 1", true);
        player.setDays(3);
        gameLogic.day();
        assertEquals(900, player.getBalance());
        assertEquals(200, gameLogic.getBills());
        player.setBalance(199);
        try {
            animals.setFed("Field 1", true);
            crops.water("Field 1");
            player.setDays(6);
            game.day();
            fail("Expected GameSystemException to be thrown");
        } catch (Game.GameSystemException ex) {
            assertEquals("YOU DON'T HAVE ENOUGH MONEY TO PAY YOUR BILLS.", ex.getMessage());
        }
    }
}

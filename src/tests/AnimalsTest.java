package tests;

import game.Animals;
import game.PlayerLogic;
import game.interfaces.GameLogicAnimals;
import game.interfaces.GameLogicPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AnimalsTest {

    @Test
    public void testBuyField() throws Animals.NotEnoughMoneyException, Animals.FieldsException {

        GameLogicPlayer player = new PlayerLogic("Test");
        GameLogicAnimals animals = new Animals(player);
        player.setBalance(1000);
        int initialBalance = player.getBalance();
        //Test balance after buying field
        animals.buyField();
        assertEquals("empty", animals.getField().get("Field 1"));
        assertEquals(initialBalance , player.getBalance());
        animals.buyField();
        assertEquals("empty", animals.getField().get("Field 2"));
        assertEquals(initialBalance - 130, player.getBalance());
        initialBalance = player.getBalance();
        animals.buyField();
        assertEquals("empty", animals.getField().get("Field 3"));
        assertEquals(initialBalance - 169, player.getBalance());
        //Test when player doesn't have enough money
        player.setBalance(100);
        try {
            animals.buyField();
            fail("Expected NotEnoughMoneyException to be thrown");
        } catch (Animals.NotEnoughMoneyException ex) {
            assertEquals("NOT ENOUGH MONEY TO BUY FIELD.", ex.getMessage());
        }
        //Test when player already owns maximum number of fields
        player.setBalance(10000);
        animals.getField().clear();
        for(int i=0;i<=5;i++) animals.buyField();
        try {
            animals.buyField();
            fail("Expected FieldsException to be thrown");
        } catch (Animals.FieldsException ex) {
            assertEquals("CANNOT BUY ANY MORE FIELDS.", ex.getMessage());
        }
    }

}

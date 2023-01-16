package tests;

import game.PlayerLogic;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerLogicTests {

    @Test
    public void testPlayerConstructor() {

        PlayerLogic playerLogic = new PlayerLogic("TEST_PLAYER");

        //Test that the player's name and balance are set correctly
        assertEquals("TEST_PLAYER", playerLogic.getName());
        assertEquals(1000, playerLogic.getBalance());
        assertEquals(1, playerLogic.getDays());
    }

}

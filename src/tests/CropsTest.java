package tests;

import game.Crops;
import game.PlayerLogic;
import org.junit.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.*;

public class CropsTest {

    @Test
    public void testCheckGrowth() throws Crops.FieldsException {
        PlayerLogic player = new PlayerLogic("Test");
        Crops crops = new Crops(player);
        crops.getField().put("Field 1", "empty");
        assertFalse(crops.checkGrowth("Field 1"));
        crops.getField().put("Field 1", "Wheat");
        crops.setSincePlanted("Field 1", 5);
        crops.setSincePlanted("Field 1", 6);
        assertFalse(crops.checkGrowth("Field 1"));
        try {
            crops.checkGrowth("Field 2");
            fail("Expected FieldsException to be thrown");
        } catch (Crops.FieldsException ex) {
            assertEquals("YOU NEED TO BUY THAT FIELD", ex.getMessage());
        }
    }
}


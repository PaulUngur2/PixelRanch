package game.interfaces;

import game.Crops;
import game.Database;
import game.Game;

public interface GameLogic {
    void shutdownHook(Database database, String name);
    void restart();
    void day() throws Game.GameSystemException, Crops.FieldsException;
    void feed(String field) throws Game.GameSystemException;
    int getBills();
}

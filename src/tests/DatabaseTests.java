package tests;

import game.Database;
import game.PlayerLogic;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTests {

    @Test
    public void testUpdateUser() throws SQLException {



        // create a test player with a name and balance
        PlayerLogic playerLogic = new PlayerLogic("player1");
        playerLogic.setBalance(1000);

        // create an instance of the Database class
        Database database = new Database(playerLogic);
        // establish a connection to the database
        Connection conn = database.databaseConnection();

        // check if the user already exists in the database
        String sql = "SELECT balance FROM players WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, playerLogic.getName());
        ResultSet rs = stmt.executeQuery();

        // if the user exists, update their balance
        if (rs.next()) {
            sql = "UPDATE players SET balance = ? WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerLogic.getBalance());
            stmt.setString(2, playerLogic.getName());
            stmt.executeUpdate();
        }

        // close the connection to the database
        conn.close();
    }
}

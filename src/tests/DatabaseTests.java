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

        PlayerLogic playerLogic = new PlayerLogic("player1");
        playerLogic.setBalance(1000);

        Database database = new Database(playerLogic);
        Connection conn = database.databaseConnection();

        //Check if the user already exists in the database
        String sql = "SELECT balance FROM players WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, playerLogic.getName());
        ResultSet rs = stmt.executeQuery();

        //If the user exists, update their balance
        if (rs.next()) {
            sql = "UPDATE players SET balance = ? WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerLogic.getBalance());
            stmt.setString(2, playerLogic.getName());
            stmt.executeUpdate();
        }

        conn.close();
    }
}

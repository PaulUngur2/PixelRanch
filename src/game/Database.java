package game;

import game.interfaces.GameLogicPlayer;

import org.mariadb.jdbc.Driver;
import java.sql.*;
public class Database{

    GameLogicPlayer playerLogic;

    public Database(GameLogicPlayer playerLogic){
        this.playerLogic = playerLogic;
    }

    public Connection databaseConnection() throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/java";
        String username = "admin_user";
        String password = "secret_password";
        return DriverManager.getConnection(url, username, password);
    }


    // Method to save and retrieve the balance for a user
    public void updateBalance(String name) throws SQLException {

        // Connect to the database
        Connection conn = databaseConnection();

        // Check if the user already exists in the database
        String sql = "SELECT balance FROM players WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            playerLogic.setBalance(rs.getInt("balance"));
        } else {
            // Insert a new row for the user
            sql = "INSERT INTO players (name, balance) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, playerLogic.getBalance());
            stmt.executeUpdate();
        }
    }

    public void updateUser(String name) throws SQLException {

        // Connect to the database
        Connection conn = databaseConnection();

        // Check if the user already exists in the database
        String sql = "SELECT balance FROM players WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Update the balance for the user
            sql = "UPDATE players SET balance = ? WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerLogic.getBalance());
            stmt.setString(2, name);
            stmt.executeUpdate();
        }

    }
}


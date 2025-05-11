package dev.kittycore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

/// Handles balances of players and their tags
public class Economy {
    Connection conn;
    static HashMap<Integer, String> tags;

    static {
        tags = new HashMap<>();
        tags.put(0, "none");
        tags.put(1, "MEOW");
        tags.put(2, "KITTY");
        tags.put(3, "GURT");
        tags.put(4, "SECRET");
    }

    public Economy() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:economy.db");
        this.initTables();
    }

    // try initialize the tables
    private void initTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS balance (id STRING, balance INT)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tags (id STRING, selected INT, unlocked BLOB)");
        stmt.close();
    }

    // create a new user
    // safe to call if the user already exists
    public void createUser(UUID userId) throws SQLException {
        if (this.userExists(userId)) {
            return;
        }

        String asStr = userId.toString();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO balance (?, 0)");
        stmt.setString(1, asStr);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("INSERT INTO tags (?, 0, json_array(0))");
        stmt.setString(1, asStr);
        stmt.executeUpdate();
    }

    public boolean userExists(UUID userId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT 1 FROM balance WHERE id=? LIMIT 1");
        stmt.setString(1, userId.toString());
        ResultSet set = stmt.executeQuery();
        return set.next();
    }

    public long getBalance(UUID id) throws SQLException {
        if (!this.userExists(id)) {
            return 0;
        }

        PreparedStatement stmt = this.conn.prepareStatement("SELECT balance FROM balance WHERE id=? LIMIT 1");
        stmt.setString(1, id.toString());
        ResultSet set = stmt.executeQuery();

        return set.getLong(1);
    }

    public void setBalance(UUID id, long money) throws SQLException {
        if (!this.userExists(id)) {
            return;
        }

        PreparedStatement stmt = this.conn.prepareStatement("UPDATE balance SET balance=? WHERE id=? LIMIT 1");
        stmt.setLong(1, money);
        stmt.setString(2, id.toString());

    }

    public void give(UUID id, long money) throws SQLException {
        if (!this.userExists(id)) {
            return;
        }

        long balance = this.getBalance(id);
        this.setBalance(id, balance + money);
    }
}

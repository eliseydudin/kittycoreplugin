package dev.kittycore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/// Handles balances of players and their tags
public class Economy {
    private Connection conn;

    public Economy() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:economy.db");
        this.initTables();
    }

    // try initialize the tables
    private void initTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS balance (id STRING, balance INT)");
        stmt.close();
    }

    // create a new user
    // safe to call if the user already exists
    public void createUser(UUID userId) throws SQLException {
        if (this.userExists(userId)) {
            return;
        }

        String asStr = userId.toString();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO balance VALUES(?, 50)");
        stmt.setString(1, asStr);
        stmt.executeUpdate();
        stmt.close();
    }

    public boolean userExists(UUID userId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT 1 FROM balance WHERE id=? LIMIT 1");
        stmt.setString(1, userId.toString());
        ResultSet set = stmt.executeQuery();
        boolean exists = set.next();
        stmt.close();
        return exists;
    }

    public long getBalance(UUID id) throws SQLException {
        if (!this.userExists(id)) {
            return 0;
        }

        PreparedStatement stmt = this.conn.prepareStatement("SELECT balance FROM balance WHERE id=? LIMIT 1");
        stmt.setString(1, id.toString());
        ResultSet set = stmt.executeQuery();
        long result = set.getLong(1);
        stmt.close();

        return result;
    }

    public void setBalance(UUID id, long money) throws SQLException {
        if (!this.userExists(id)) {
            return;
        }

        PreparedStatement stmt = this.conn.prepareStatement("UPDATE balance SET balance=? WHERE id=?");
        stmt.setLong(1, money);
        stmt.setString(2, id.toString());
        stmt.execute();
        stmt.close();
    }

    public void give(UUID id, long money) throws SQLException {
        if (!this.userExists(id)) {
            return;
        }

        PreparedStatement stmt = this.conn.prepareStatement("UPDATE balance SET balance = (balance + ?)  WHERE id=?");
        stmt.setLong(1, money);
        stmt.setString(2, id.toString());
        stmt.execute();
        stmt.close();
    }

    /// determine how much a player is worth
    /// thats based on the amount of bosses (wither, ender dragon)
    /// they've defeated, the amount of player they've killed,
    /// and the amount of money they have in general
    ///
    /// caps out at 9999eu
    ///
    /// currently its only based on the balance of a player
    public long getWorth(UUID player) throws SQLException {
        long worth = this.getBalance(player) / 100;
        if (worth > 9999) {
            return 9999;
        } else if (worth == 0) {
            return 1;
        } else {
            return worth;
        }
    }

    /// calculate the money that should be given to a player after gambling
    public long gamble(long money) {
        // the bot should be compatible with telegram & in telegram the dice
        // messages return values in the range from 1 to 64
        int telegramRes = ThreadLocalRandom.current().nextInt(1, 64 + 1);
        return gamble(money, telegramRes);
    }

    /// TODO make this thing more complex
    public long gamble(long money, int tgValue) {
        // https://github.com/python-telegram-bot/python-telegram-bot/wiki/Code-snippets#map-a-slot-machine-dice-value-to-the-corresponding-symbols
        if (tgValue == 64) {
            return money * 7;
        } else if (tgValue % 21 == 1) {
            return money * 3;
        } else {
            return -money;
        }
    }
}

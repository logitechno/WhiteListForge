package ru.logitechno.whitelistforge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:whitelist.db";
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            WhiteListForge.LOGGER.info("Connected to SQLite");
            createTable();
        } catch (ClassNotFoundException e) {
            WhiteListForge.LOGGER.error("SQLite driver not found");
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Connection error: {}", e.getMessage());
        }
    }

    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS whitelist (username TEXT PRIMARY KEY);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            WhiteListForge.LOGGER.info("Whitelist table ready");
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Error creating table: {}", e.getMessage());
        }
    }

    public static void addPlayer(String username) {
        String sql = "INSERT INTO whitelist(username) VALUES(?) ON CONFLICT(username) DO NOTHING;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            WhiteListForge.LOGGER.info("Player added: {}", username);
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Error adding player: {}", e.getMessage());
        }
    }

    public static void removePlayer(String username) {
        String sql = "DELETE FROM whitelist WHERE username = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                WhiteListForge.LOGGER.info("Player {} removed from whitelist", username);
            } else {
                WhiteListForge.LOGGER.warn("Player {} not found in whitelist", username);
            }
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Error removing player: {}", e.getMessage());
        }
    }

    public static boolean isPlayerWhitelisted(String username) {
        String sql = "SELECT 1 FROM whitelist WHERE username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Error checking whitelist: {}", e.getMessage());
            return false;
        }
    }

    public static List<String> getAllWhitelistedPlayers() {
        List<String> players = new ArrayList<>();
        String sql = "SELECT username FROM whitelist;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String playerName = rs.getString("username");
                players.add(playerName);
            }
        } catch (SQLException e) {
            WhiteListForge.LOGGER.error("Error retrieving player list: {}", e.getMessage());
        }

        return players;
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                WhiteListForge.LOGGER.info("Connection to SQLite closed");
            } catch (SQLException e) {
                WhiteListForge.LOGGER.error("Error closing connection: {}", e.getMessage());
            }
        }
    }
}
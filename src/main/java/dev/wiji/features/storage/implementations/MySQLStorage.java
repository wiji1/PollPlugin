package dev.wiji.features.storage.implementations;

import dev.wiji.PollPlugin;
import dev.wiji.features.storage.models.SQLStorage;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLStorage extends SQLStorage {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MySQLStorage() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        this.host = PollPlugin.getInstance().getConfig().getString("mysql.host", "localhost");
        this.port = PollPlugin.getInstance().getConfig().getInt("mysql.port", 3306);
        this.database = PollPlugin.getInstance().getConfig().getString("mysql.database", "polls");
        this.username = PollPlugin.getInstance().getConfig().getString("mysql.username", "root");
        this.password = PollPlugin.getInstance().getConfig().getString("mysql.password", "");
    }

    @Override
    protected void establishConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=UTC", 
                                 host, port, database, false);
        
        Logger logger = PollPlugin.getInstance().getLogger();
        logger.info("Connecting to MySQL at: " + host + ":" + port + "/" + database);
        
        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    protected String getCreateTableSQL() {
        return """
            CREATE TABLE IF NOT EXISTS %s (
                uuid VARCHAR(36) PRIMARY KEY,
                poll_data TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """.formatted(TABLE_NAME);
    }

    @Override
    protected String getUpsertSQL() {
        return """
            INSERT INTO %s (uuid, poll_data, updated_at) 
            VALUES (?, ?, NOW()) 
            ON DUPLICATE KEY UPDATE 
                poll_data = VALUES(poll_data), 
                updated_at = NOW()
            """.formatted(TABLE_NAME);
    }

    @Override
    protected String getStorageType() {
        return "MySQL";
    }
}
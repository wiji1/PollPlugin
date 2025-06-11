package dev.wiji.features.storage.implementations;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.wiji.PollPlugin;
import dev.wiji.features.storage.models.SQLStorage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLStorage extends SQLStorage {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    private HikariDataSource dataSource;

    public MySQLStorage() {
        loadConfiguration();
        setupDataSource();
    }

    private void loadConfiguration() {
        this.host = PollPlugin.getInstance().getConfig().getString("mysql.host", "localhost");
        this.port = PollPlugin.getInstance().getConfig().getInt("mysql.port", 3306);
        this.database = PollPlugin.getInstance().getConfig().getString("mysql.database", "polls");
        this.username = PollPlugin.getInstance().getConfig().getString("mysql.username", "root");
        this.password = PollPlugin.getInstance().getConfig().getString("mysql.password", "");
    }

    private void setupDataSource() {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC",
                host, port, database);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setPoolName("PollPluginPool");

        Logger logger = PollPlugin.getInstance().getLogger();
        logger.info("Connecting to MySQL via HikariCP at: " + host + ":" + port + "/" + database);

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    protected void establishConnection() throws SQLException {
        this.connection = dataSource.getConnection();
    }

    @Override
    protected String getCreateTableSQL() {
        return """
            CREATE TABLE IF NOT EXISTS %s (
                uuid VARCHAR(36) PRIMARY KEY,
                poll_data TEXT NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """.formatted(TABLE_NAME);
    }

    @Override
    protected String getUpsertSQL() {
        return """
            INSERT INTO %s (uuid, poll_data) 
            VALUES (?, ?) 
            ON DUPLICATE KEY UPDATE 
                poll_data = VALUES(poll_data)
            """.formatted(TABLE_NAME);
    }

    @Override
    protected String getRemoveSQL() {
        return """
            DELETE FROM %s 
            WHERE uuid = ?
            """.formatted(TABLE_NAME);
    }

    @Override
    protected String getStorageType() {
        return "MySQL";
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

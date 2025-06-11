package dev.wiji.features.storage.models;

import com.google.gson.Gson;
import dev.wiji.PollPlugin;
import dev.wiji.features.poll.models.Poll;
import dev.wiji.features.storage.controllers.StorageManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class SQLStorage extends PluginStorage {
    protected static final String TABLE_NAME = "polls";
    protected Connection connection;

    protected abstract void establishConnection() throws SQLException;
    protected abstract String getCreateTableSQL();
    protected abstract String getUpsertSQL();

    @Override
    public void init() {
        Logger logger = PollPlugin.getInstance().getLogger();
        
        try {
            establishConnection();
            createTable();
            logger.info("Successfully connected to " + getStorageType() + " Storage");
        } catch (SQLException e) {
            logger.severe("Failed to connect to " + getStorageType() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract String getStorageType();

    private void createTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(getCreateTableSQL());
        }
    }

    @Override
    public void loadPolls(Consumer<List<Poll>> pollConsumer) {
        List<Poll> polls = new ArrayList<>();
        Gson gson = StorageManager.getInstance().getGson();
        Logger logger = PollPlugin.getInstance().getLogger();

        String selectSQL = "SELECT poll_data FROM " + TABLE_NAME;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                try {
                    String json = rs.getString("poll_data");
                    Poll poll = gson.fromJson(json, Poll.class);
                    polls.add(poll);
                } catch (Exception e) {
                    logger.warning("Failed to deserialize poll from database: " + e.getMessage());
                }
            }

            logger.info("Loaded " + polls.size() + " polls from " + getStorageType() + " database");
        } catch (SQLException e) {
            logger.severe("Failed to load polls from " + getStorageType() + ": " + e.getMessage());
            e.printStackTrace();
        }

        pollConsumer.accept(polls);
    }

    @Override
    public void savePolls(Supplier<List<Poll>> pollSupplier) {
        List<Poll> polls = pollSupplier.get();
        Gson gson = StorageManager.getInstance().getGson();
        Logger logger = PollPlugin.getInstance().getLogger();

        try (PreparedStatement pstmt = connection.prepareStatement(getUpsertSQL())) {
            connection.setAutoCommit(false);

            for (Poll poll : polls) {
                try {
                    String json = gson.toJson(poll);
                    pstmt.setString(1, poll.getUuid().toString());
                    pstmt.setString(2, json);
                    pstmt.addBatch();
                } catch (Exception e) {
                    logger.warning("Failed to serialize poll " + poll.getUuid() + ": " + e.getMessage());
                }
            }

            pstmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

            logger.info("Successfully saved " + polls.size() + " polls to " + getStorageType() + " database");
        } catch (SQLException e) {
            logger.severe("Failed to save polls to " + getStorageType() + ": " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                logger.severe("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    PollPlugin.getInstance().getLogger().info(getStorageType() + " Storage connection closed.");
                } else {
                    PollPlugin.getInstance().getLogger().warning(getStorageType() + " Storage connection was already closed.");
                }
            } catch (SQLException e) {
                PollPlugin.getInstance().getLogger().severe("Error closing " + getStorageType() + " connection: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            PollPlugin.getInstance().getLogger().warning(getStorageType() + " Storage connection was not initialized.");
        }
    }
}
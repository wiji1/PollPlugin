package dev.wiji.features.storage.implementations;

import dev.wiji.PollPlugin;
import dev.wiji.features.storage.models.SQLStorage;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteStorage extends SQLStorage {

	@Override
	protected void establishConnection() throws SQLException {
		String finalDbPath = PollPlugin.getInstance().getDataFolder().getPath() + "/database.sqlite";

		File dbFile = new File(finalDbPath);
		File parentDir = dbFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		String url = "jdbc:sqlite:" + finalDbPath;
		connection = DriverManager.getConnection(url);
	}

	@Override
	protected String getCreateTableSQL() {
		return """
            CREATE TABLE IF NOT EXISTS %s (
                uuid TEXT PRIMARY KEY,
                poll_data TEXT NOT NULL
            )
            """.formatted(TABLE_NAME);
	}

	@Override
	protected String getUpsertSQL() {
		return """
            INSERT OR REPLACE INTO %s (uuid, poll_data) 
            VALUES (?, ?)
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
		return "SQLite";
	}
}
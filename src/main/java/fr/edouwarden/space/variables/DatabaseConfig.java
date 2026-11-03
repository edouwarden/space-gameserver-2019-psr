package fr.edouwarden.space.variables;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Database settings, loaded from {@code config.yml} and overridable through
 * environment variables so that deployments never commit credentials.
 */
public final class DatabaseConfig {

    public static String host = "127.0.0.1";
    public static int port = 3306;
    public static String database = "space";
    public static String username = "space";
    public static String password = "";
    public static String tableShip = "sp_ship";
    public static String tableBounty = "sp_bounty";

    private DatabaseConfig() {
    }

    public static void load(FileConfiguration config) {
        host = env("SPACE_DB_HOST", config.getString("database.host", host));
        port = Integer.parseInt(env("SPACE_DB_PORT",
                String.valueOf(config.getInt("database.port", port))));
        database = env("SPACE_DB_NAME", config.getString("database.name", database));
        username = env("SPACE_DB_USER", config.getString("database.user", username));
        password = env("SPACE_DB_PASSWORD", config.getString("database.password", password));
        tableShip = config.getString("database.tables.ship", tableShip);
        tableBounty = config.getString("database.tables.bounty", tableBounty);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isEmpty() ? fallback : value;
    }
}

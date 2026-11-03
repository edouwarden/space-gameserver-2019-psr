package fr.edouwarden.space.mysql;

import fr.edouwarden.space.variables.General;

import fr.edouwarden.space.variables.DatabaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.List;


public class Mysql {

    private static Connection connection;


    public static void setup() {

        try {
            synchronized (General.plugin) {
                if (getConnection() != null && !getConnection().isClosed())
                    return;
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + DatabaseConfig.host + ":" + DatabaseConfig.port + "/" + DatabaseConfig.database, DatabaseConfig.username, DatabaseConfig.password));
            }
        } catch (SQLException e) {
            General.console.log(java.util.logging.Level.SEVERE, "Cannot open database connection", e);
            Bukkit.getServer().shutdown();
        } catch (ClassNotFoundException e) {
            General.console.log(java.util.logging.Level.SEVERE, "JDBC driver not found", e);
        }
    }
    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection conection) {
        connection = (Connection)conection;
    }



}

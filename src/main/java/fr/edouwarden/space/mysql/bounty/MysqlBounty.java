package fr.edouwarden.space.mysql.bounty;

import fr.edouwarden.space.mysql.Mysql;
import fr.edouwarden.space.variables.General;
import fr.edouwarden.space.variables.DatabaseConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MysqlBounty {

    public static boolean playerExists(UUID uuid) {
        try {

            PreparedStatement statement = (PreparedStatement) Mysql.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseConfig.tableBounty + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e){
            General.console.log(java.util.logging.Level.WARNING, "Bounty query failed", e);
        }
        return false;
    }
    public static boolean createPlayer(UUID uuid) {
        try {
            if (!playerExists(uuid)) {
                PreparedStatement insert = (PreparedStatement) Mysql.getConnection()
                        .prepareStatement("INSERT INTO " + DatabaseConfig.tableBounty + " (UUID,BOUNTY) VALUES (?,?)");
                insert.setString(1, uuid.toString());
                insert.setLong(2, 0);
                insert.executeUpdate();
            }
        } catch (SQLException e){
            General.console.log(java.util.logging.Level.WARNING, "Bounty query failed", e);
        }
        return false;
    }

    public static void setbounty(UUID uuid, long bounty) {
        try {
            PreparedStatement statement = (PreparedStatement) Mysql.getConnection()
                    .prepareStatement("UPDATE " + DatabaseConfig.tableBounty + " SET BOUNTY=? WHERE UUID=?");
            statement.setLong(1, bounty);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            General.console.log(java.util.logging.Level.WARNING, "Bounty query failed", e);
        }
    }

    public static long getbounty(UUID uuid) {
        try {
            PreparedStatement statement = (PreparedStatement) Mysql.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseConfig.tableBounty + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            long bounty = results.getLong("BOUNTY");
            return bounty;

        } catch (SQLException e) {
            General.console.log(java.util.logging.Level.WARNING, "Bounty query failed", e);
        }
        return 0;
    }

}

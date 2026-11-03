package fr.edouwarden.space.mysql;

import fr.edouwarden.space.variables.DatabaseConfig;
import fr.edouwarden.space.variables.General;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MysqlCheck {
    public static boolean haveAccount(UUID uuid){
        try {
            PreparedStatement statement = Mysql.getConnection()
                    .prepareStatement("SELECT * FROM " + DatabaseConfig.tableBounty + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                statement = Mysql.getConnection()
                        .prepareStatement("SELECT * FROM " + DatabaseConfig.tableShip + " WHERE UUID=?");
                statement.setString(1, uuid.toString());
                results = statement.executeQuery();
                if (results.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            General.console.log(java.util.logging.Level.WARNING, "Cannot check player account", e);
        }
        return false;

    }
}

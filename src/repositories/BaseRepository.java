package repositories;

import database.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseRepository {

    /*
     * START TRANSACTION or BEGIN start a new transaction.
     * COMMIT commits the current transaction, making its changes permanent.
     * ROLLBACK rolls back the current transaction, canceling its changes.
     * SET autocommit disables or enables the default autocommit mode for the current session.
    */

    public Connection getConnection() {
        return Connector.getInstance();
    }

    public void beginTransaction() {
        transaction("START TRANSACTION");
    }

    public void commitTransaction() {
        transaction("COMMIT");
    }

    public void rollbackTransaction() {
        transaction("ROLLBACK");
    }

    private void transaction(String command) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(command);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

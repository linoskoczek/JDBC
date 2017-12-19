package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static Connection connection;
    private static String user = "root";
    private static String password = "root";
    private static String database = "assignment10";
    private static int port = 3306;

    public Connector() {
    }

    public static Connection getInstance() {
        if (connection == null)
            return createConnection();
        else
            return connection;
    }

    private static Connection createConnection() {
        //checkDriver();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + database, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }

    private void checkDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver has not been found.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

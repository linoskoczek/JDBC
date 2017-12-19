package test;

import database.Connector;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    @Test
    public void connection() {
        Connector connector = new Connector();
        Connection con = connector.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM users");
            boolean executed = statement.execute();
            Assert.assertTrue(executed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

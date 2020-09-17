package seng202.team4.model;

import seng202.team4.Path;

import java.sql.*;

/**
 * Performs basic interaction with the database that bypasses
 * the errors thrown by the database.
 */
public abstract class DatabaseManager {

    /**
     * Gets a new {@link java.sql.Connection Connection}
     * @return a new {@link java.sql.Connection Connection} if connection does not throw an error, otherwise 'null'
     */
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(Path.DATABASE_CONNECTION);
            c.setAutoCommit(false);
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disconnects from a given connection.
     * @param c the connection to disconnect from.
     * @return 'true' if disconnected successfully, 'false' otherwise.
     */
    public static boolean disconnect(Connection c) {
        try {
            c.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a statement for a given connection.
     * @param c the connection the statement is made for.
     * @return the new statement, 'null' if failed to make a statement.
     */
    public static Statement getStatement(Connection c) {
        try {
            Statement stmt = c.createStatement();
            return stmt;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /*
    public void updateDatabase() {
        try {
            stmt.executeBatch();

            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }*/
}

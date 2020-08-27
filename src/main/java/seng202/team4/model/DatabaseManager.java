package seng202.team4.model;

import seng202.team4.Path;

import java.sql.*;


public abstract class DatabaseManager {
    private String between = "', '";


    private int dataCount = 0;
    private final int MAX_ENTRIES = 1000;

    Connection c;
    //Statement stmt = null;
    String sql = null;


    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(Path.database);
            c.setAutoCommit(false);
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
    }

    public static boolean disconnect(Connection c) {
        try {
            c.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Statement getStatement(Connection c) {
        try {
            Statement stmt = c.createStatement();
            return stmt;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

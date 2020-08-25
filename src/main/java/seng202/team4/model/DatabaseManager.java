package seng202.team4.model;

import seng202.team4.Path;

import java.sql.*;


public static class DatabaseManager {
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

            stmt = c.createStatement();
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }

    }

    public void addToDatabase(DataType dataType) {
        Connection c = connect();
        if (c != null) {
            try {
                Statement stmt = c.createStatement();
                stmt.addBatch(dataType.getInsertStatement());
                dataCount += 1;
                if (dataCount >= MAX_ENTRIES) {  // To be removed once we decide the max number of entries.
                    stmt.executeBatch();
                }

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }

    public void updateDatabase() {
        try {
            stmt.executeBatch();

            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }
}

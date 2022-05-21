package main.java.com.javastart.server;

import com.javastart.Account;
import com.javastart.Adjustment;
import com.javastart.Payment;

import java.sql.*;

public class DataBase {

    private static Connection getDBConnection() {
        Connection dbConnection = null;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/clientserver_base",
                    "postgres", "root");
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    public void createTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String AccountTableSQL = "CREATE TABLE IF NOT EXISTS ACCOUNT ("
                + "ID INTEGER UNIQUE NOT NULL, "
                + "USERNAME VARCHAR(255) NOT NULL, "
                + "BILL_ID INTEGER UNIQUE NOT NULL, "
                + "BILL INTEGER NOT NULL, "
                + "PRIMARY KEY (ID) "
                + ")";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.execute(AccountTableSQL);
            System.out.println("Table \"Account\" is created");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public void addToTable(Account account) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String insertAccountTableSQL = "INSERT INTO ACCOUNT (ID, USERNAME, BILL_ID, BILL) "
                + "VALUES (" + account.getId() + ", '" + account.getName() + "' ," + account.getBill().getId() + " , "
                + account.getBill().getAmount() + ")"
                + "ON CONFLICT (ID) DO NOTHING";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(insertAccountTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public static int getBillFromTable(Payment payment) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String requestToBill = "SELECT * FROM ACCOUNT WHERE ID = " + payment.getAccountId();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(requestToBill);

            while (rs.next()) {
                int prevBill = rs.getInt("BILL");
                return prevBill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if ((dbConnection != null)) {
                dbConnection.close();
            }
        }
        return 0;
    }

    public static int updateTableByPayment(Payment payment) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String updateBill = "UPDATE ACCOUNT "
                + "SET BILL = BILL - " + payment.getPayment()
                + "WHERE ID = " + payment.getAccountId()
                + "AND BILL >= " + payment.getPayment();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(updateBill);
            ResultSet rs = statement.executeQuery("SELECT * FROM ACCOUNT WHERE ID = " + payment.getAccountId());

            while (rs.next()) {
                int currentBill = rs.getInt("BILL");
                return currentBill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if ((dbConnection != null)) {
                dbConnection.close();
            }
        }
        return 0;
    }

    public static int updateTableByAdjustment(Adjustment adjustment) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String updateBill = "UPDATE ACCOUNT "
                + "SET BILL = BILL + " + adjustment.getAdjustment()
                + "WHERE ID = " + adjustment.getAccountId();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(updateBill);
            ResultSet rs = statement.executeQuery("SELECT * FROM ACCOUNT WHERE ID = " + adjustment.getAccountId());

            while (rs.next()) {
                int currentBill = rs.getInt("BILL");
                return currentBill;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if ((dbConnection != null)) {
                dbConnection.close();
            }
        }
        return 0;
    }
}
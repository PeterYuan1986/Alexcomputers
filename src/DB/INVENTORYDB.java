package DB;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class INVENTORYDB {
    // Constant for database URL.

    public final String DB_URL
            = "jdbc:derby://localhost:1527/AlexComputers";

    // Field for the database connection
    private Connection conn;

    /**
     * Constructor
     */
    public INVENTORYDB() throws SQLException {
        // Create a connection to the database.
        conn = DriverManager.getConnection(DB_URL);
    }

    
    public int getAmount_new(String name){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT qty_new FROM inventory WHERE NAME= '" + name + "'";
            ResultSet result = stmt.executeQuery(sql);
            return result.getInt("qty_new");
        } catch (SQLException ex) {
            Logger.getLogger(INVENTORYDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int getAmount_old(String name){
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT qty_new FROM inventory WHERE NAME= '" + name + "'";
            ResultSet result = stmt.executeQuery(sql);
            return result.getInt("qty_new");
        } catch (SQLException ex) {
            Logger.getLogger(INVENTORYDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    /**
     * The getCoffeeNames method returns an array of Strings containing all the
     * coffee names.
     */
    public String[] getGoodsNames()
            throws SQLException {
        // Create a Statement object for the query.
        Statement stmt
                = conn.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

        // Execute the query.
        ResultSet resultSet = stmt.executeQuery(
                "SELECT NAME FROM INVENTORY");

        // Get the number of rows
        resultSet.last(); // Move to last row
        int numRows = resultSet.getRow(); // Get row number
        resultSet.first(); // Move to first row

        // Create an array for the coffee names.
        String[] listData = new String[numRows];

        // Populate the array with coffee names.
        for (int index = 0; index < numRows; index++) {
            // Store the coffee name in the array.
            listData[index] = resultSet.getString(1);

            // Go to the next row in the result set.
            resultSet.next();
        }

        // Close the connection and statement objects.
        conn.close();
        stmt.close();

        // Return the listData array.
        return listData;
    }   
}

package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public final class InventoryWindow extends JFrame {

    private static final InventoryWindow INSTANCE = new InventoryWindow();
    final String DB_URL = "jdbc:derby://localhost:1527/AlexComputers";
    String[] colNames;
    String[][] tableData; // Table data
    JTable table;
    JScrollPane scrollPane;

    private InventoryWindow() {
        this.setTitle("Inventory");
        setSize(400, 400);
        setLocation(300, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            // Get a connection to the database.
            Connection conn = DriverManager.getConnection(DB_URL);
            try {
                // Create a Statement object for the query.
                Statement stmt = conn.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                // Execute the query.
                String sql = "SELECT * FROM INVENTORY WHERE QTY_NEW >0 OR QTY_OLD>0";
                ResultSet resultSet = stmt.executeQuery(sql);

                // Get the number of rows.
                resultSet.last(); // Move to last row
                int numRows = resultSet.getRow(); // Get row number
                resultSet.first(); // Move to first row

                // Get a metadata object for the result set.
                ResultSetMetaData meta = resultSet.getMetaData();

                // Create an array of Strings for the column names.
                colNames = new String[meta.getColumnCount()];

                // Store the column names in the colNames array.
                for (int i = 0; i < meta.getColumnCount(); i++) {
                    // Get a column name.
                    colNames[i] = meta.getColumnName(i + 1);
                }

                // Create a 2D String array for the table data.
                tableData
                        = new String[numRows + 1][meta.getColumnCount()];

                int total_new = 0;
                int total_old = 0;

                // Store the columns in the tableData array.
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < meta.getColumnCount(); col++) {
                        tableData[row][col] = resultSet.getString(col + 1);
                    }

                    // Go to the next row in the ResultSet.
                    resultSet.next();
                }

                for (int row = 0; row < numRows; row++) {
                    total_new += Integer.parseInt(tableData[row][1]);
                    total_old += Integer.parseInt(tableData[row][2]);
                }
                tableData[numRows][0] = "总计";
                tableData[numRows][1] = String.valueOf(total_new);
                tableData[numRows][2] = String.valueOf(total_old);

                // Close the statement and connection objects.
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            table = new JTable(tableData, colNames);
            scrollPane = new JScrollPane(table);
            this.add(scrollPane);
            this.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(InventoryWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static InventoryWindow getInstance() {
        INSTANCE.validate();
        INSTANCE.setVisible(true);
        return INSTANCE;
    }

}

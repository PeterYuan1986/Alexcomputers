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

public class OrderWindow extends JFrame {

    public static OrderWindow INSTANCE = null;
    final String DB_URL = "jdbc:derby://localhost:1527/AlexComputers";
    String[] colNames;
    String[][] rowData;
    JTable mytable;
    JScrollPane scrollPane;
    String danhao;

    private OrderWindow(String danhao) {
        this.danhao =danhao;
        setTitle("Order detail");
        setSize(400, 400);
        this.setLocation(200, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sql1 = "SELECT * FROM Z" + danhao;
            ResultSet resultSet3 = stmt.executeQuery(sql1);
            ResultSetMetaData meta = resultSet3.getMetaData();
            resultSet3.last(); // Move to last row
            int numRows3 = resultSet3.getRow(); // Get row number
            resultSet3.first(); // Move to first row
            colNames = new String[meta.getColumnCount()];

            // Store the column names in the colNames array.
            for (int i = 0; i < meta.getColumnCount(); i++) {
                // Get a column name.
                colNames[i] = meta.getColumnName(i + 1);
            }

            rowData = new String[numRows3][3];
            for (int i = 0; i < numRows3; i++) {
                for (int j = 0; j < 3; j++) {
                    rowData[i][j] = resultSet3.getString(j + 1);
                }
                resultSet3.next();
            }
            mytable = new JTable(rowData, colNames);
            scrollPane = new JScrollPane(mytable);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Order: " + danhao));
            add(scrollPane);
        } catch (SQLException ex) {
            Logger.getLogger(StatisticWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(true);
    }
    
    private String getTrackNo()
    {
        return danhao;
    }

    public static void getInstance(String danhao) {
        if (INSTANCE == null||!INSTANCE.getTrackNo().equals(danhao)) {
            INSTANCE = new OrderWindow(danhao);
        }
        INSTANCE.setVisible(true);
    }
}

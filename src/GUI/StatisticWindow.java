package GUI;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StatisticWindow extends JFrame {

    private static final StatisticWindow INSTANCE = new StatisticWindow();
    final String DB_URL = "jdbc:derby://localhost:1527/AlexComputers";

    JPanel panel1, panel2, panel3;
    JScrollPane scrollPane1, scrollPane2, scrollPane3;
    JTable myTable1, myTable2, myTable3;

    String[] colNames1;
    String[] colNames2;
    String[] colNames3 = {"Name", "Qty_new", "Qty_old"};
    String[][] rowData1;
    String[][] rowData2;
    String[][] rowData3;

    public static StatisticWindow getInstance() {
        INSTANCE.validate();
        INSTANCE.setVisible(true);
        return INSTANCE;
    }

    private StatisticWindow() {
        this.setTitle("Statistic");
        setSize(900, 400);
        setLocation(700, 400);
        setLayout(new GridLayout(3, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel1 = new JPanel(new GridLayout(1, 1));
        panel2 = new JPanel(new GridLayout(1, 1));
        panel3 = new JPanel(new GridLayout(1, 1));

        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM INCOMING ORDER BY date";
            ResultSet resultSet1 = stmt.executeQuery(sql);
            resultSet1.last(); // Move to last row
            int numRows1 = resultSet1.getRow(); // Get row number
            resultSet1.first(); // Move to first row
            ResultSetMetaData meta = resultSet1.getMetaData();
            colNames1 = new String[meta.getColumnCount()];
            for (int i = 0; i < meta.getColumnCount(); i++) {
                colNames1[i] = meta.getColumnName(i + 1);
            }

            rowData1 = new String[numRows1][meta.getColumnCount()];
            for (int i = 0; i < numRows1; i++) {
                for (int j = 0; j < meta.getColumnCount(); j++) {
                    rowData1[i][j] = resultSet1.getString(j + 1);
                }
                resultSet1.next();
            }

            myTable1 = new JTable(rowData1, colNames1);
            scrollPane1 = new JScrollPane(myTable1);
            panel1.add(scrollPane1);

            sql = "SELECT * FROM Outcoming ORDER BY date";
            ResultSet resultSet2 = stmt.executeQuery(sql);
            ResultSetMetaData meta2 = resultSet2.getMetaData();
            colNames2 = new String[meta2.getColumnCount()];
            for (int i = 0; i < meta2.getColumnCount(); i++) {
                colNames2[i] = meta2.getColumnName(i + 1);
            }
            resultSet2.last(); // Move to last row
            int numRows2 = resultSet2.getRow(); // Get row number
            resultSet2.first(); // Move to first row
            rowData2 = new String[numRows2][meta2.getColumnCount()];
            for (int i = 0; i < numRows2; i++) {
                for (int j = 0; j < meta2.getColumnCount(); j++) {
                    rowData2[i][j] = resultSet2.getString(j + 1);
                }
                resultSet2.next();
            }
            myTable2 = new JTable(rowData2, colNames2);
            scrollPane2 = new JScrollPane(myTable2);
            panel2.add(scrollPane2);

            panel3 = new JPanel(new GridLayout(1, 1));
            rowData3 = new String[0][3];
            myTable3 = new JTable(rowData3, colNames3);
            scrollPane3 = new JScrollPane(myTable3);
            panel3.add(scrollPane3);

            panel1.setBorder(BorderFactory.createTitledBorder("Incoming Orders"));
            panel2.setBorder(BorderFactory.createTitledBorder("Outcoming Orders"));
            panel3.setBorder(BorderFactory.createTitledBorder("Order detail"));

            add(panel1);
            add(panel2);
            add(panel3);
            setVisible(true);

            myTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {
                    if (myTable1.getSelectedRow() > -1) {
                        String danhao = myTable1.getValueAt(myTable1.getSelectedRow(), 0).toString();
                        try {
                            String sql1 = "SELECT * FROM Z" + danhao;
                            ResultSet resultSet3 = stmt.executeQuery(sql1);
                            ResultSetMetaData meta3 = resultSet3.getMetaData();
                            resultSet3.last(); // Move to last row
                            int numRows3 = resultSet3.getRow(); // Get row number
                            resultSet3.first(); // Move to first row
                            rowData3 = new String[numRows3][3];
                            for (int i = 0; i < numRows3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    rowData3[i][j] = resultSet3.getString(j + 1);
                                }
                                resultSet3.next();
                            }
                            panel3.remove(scrollPane3);
                            myTable3 = new JTable(rowData3, colNames3);
                            scrollPane3 = new JScrollPane(myTable3);
                            panel3.setBorder(BorderFactory.createTitledBorder("Order: " + danhao));
                            panel3.add(scrollPane3);
                            setVisible(true);
                        } catch (SQLException ex) {
                            Logger.getLogger(StatisticWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

            myTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {
                    if (myTable2.getSelectedRow() > -1) {
                        String danhao = myTable2.getValueAt(myTable2.getSelectedRow(), 0).toString();
                        try {
                            String sql1 = "SELECT * FROM Z" + danhao;
                            ResultSet resultSet3 = stmt.executeQuery(sql1);
                            ResultSetMetaData meta3 = resultSet3.getMetaData();
                            resultSet3.last(); // Move to last row
                            int numRows3 = resultSet3.getRow(); // Get row number
                            resultSet3.first(); // Move to first row
                            rowData3 = new String[numRows3][3];
                            for (int i = 0; i < numRows3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    rowData3[i][j] = resultSet3.getString(j + 1);
                                }
                                resultSet3.next();
                            }
                            panel3.remove(scrollPane3);
                            myTable3 = new JTable(rowData3, colNames3);
                            scrollPane3 = new JScrollPane(myTable3);
                            panel3.add(scrollPane3);
                            panel3.setBorder(BorderFactory.createTitledBorder("Order: " + danhao));
                            setVisible(true);
                        } catch (SQLException ex) {
                            Logger.getLogger(StatisticWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(StatisticWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

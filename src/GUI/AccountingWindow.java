package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AccountingWindow extends JFrame {

    public static AccountingWindow INSTANCE = new AccountingWindow();
    final String DB_URL = "jdbc:derby://localhost:1527/AlexComputers";
    Connection conn;
    Statement stmt;
    JPanel panel1, panel2, panel3, panel4, panel41;
    JScrollPane scrollPane1, scrollPane2, scrollPane3;
    JTable myTable1, myTable2, myTable3;
    JButton pay;

    JLabel total;

    String[] colNames1;
    String[] colNames2;
    String[] colNames3 = {"DATE", "PAYMENT HISTORY"};
    String[][] rowData1;
    String[][] rowData2;
    String[][] rowData3;
    double total1, total2, total3;

    public static AccountingWindow getInstance() {
        INSTANCE.validate();
        INSTANCE.setVisible(true);
        return INSTANCE;
    }

    private AccountingWindow() {
        this.setTitle("Accounting");
        setSize(900, 400);
        setLocation(600, 300);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel1 = new JPanel(new GridLayout(1, 1));
        panel2 = new JPanel(new GridLayout(1, 1));
        panel3 = new JPanel(new GridLayout(1, 1));
        panel4 = new JPanel(new GridLayout(2, 1));
        total1 = 0.0;
        total2 = 0.0;
        total3 = 0.0;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT TRACKNO,DATE,QTY,COST FROM INCOMING WHERE PAID = FALSE ORDER BY date ";
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
                total1 += resultSet1.getDouble(4);
                resultSet1.next();
            }

            myTable1 = new JTable(rowData1, colNames1);
            scrollPane1 = new JScrollPane(myTable1);
            panel1.add(scrollPane1);

            sql = "SELECT TRACKNO,DATE,QTY,INTERNATIONAL,COST FROM OUTCOMING WHERE PAID = FALSE ORDER BY date ";
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
                total2 += resultSet1.getDouble(5);
                resultSet2.next();
            }
            myTable2 = new JTable(rowData2, colNames2);
            scrollPane2 = new JScrollPane(myTable2);
            panel2.add(scrollPane2);

            pay = new JButton("Make a payment");
            pay.addActionListener(new payActionListener());
            panel41 = new JPanel(new GridLayout(2, 1));
            total = new JLabel("Total Balance: $" + (total1 + total2));
            panel41.add(total);
            panel41.add(pay);
            panel4.add(panel41, BorderLayout.NORTH);

            sql = "SELECT * FROM pay";
            ResultSet resultSet3 = stmt.executeQuery(sql);
            ResultSetMetaData meta3 = resultSet3.getMetaData();
            colNames3 = new String[meta3.getColumnCount()];
            for (int i = 0; i < meta3.getColumnCount(); i++) {
                colNames3[i] = meta3.getColumnName(i + 1);
            }
            resultSet3.last(); // Move to last row
            int numRows3 = resultSet3.getRow(); // Get row number
            resultSet3.first(); // Move to first row
            rowData3 = new String[numRows3 + 1][meta3.getColumnCount()];
            for (int i = 0; i < numRows3; i++) {
                for (int j = 0; j < meta3.getColumnCount(); j++) {
                    rowData3[i][j] = resultSet3.getString(j + 1);
                }
                total3 += resultSet3.getDouble(2);
                resultSet3.next();
            }
            rowData3[numRows3][0] = "总计：";
            rowData3[numRows3][1] = String.valueOf(total3);
            myTable3 = new JTable(rowData3, colNames3);
            scrollPane3 = new JScrollPane(myTable3);
            panel3.add(scrollPane3);

            panel1.setBorder(BorderFactory.createTitledBorder("Incoming Orders to be paid"));
            panel2.setBorder(BorderFactory.createTitledBorder("Outcoming Orders to be paid"));
            panel3.setBorder(BorderFactory.createTitledBorder("Payment historyt"));

            add(panel1);
            add(panel2);
            add(panel4);
            add(panel3);
            setVisible(true);

            myTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {
                    String danhao = null;
                    if (myTable1.getSelectedRow() > -1) {
                        danhao = myTable1.getValueAt(myTable1.getSelectedRow(), 0).toString();
                        OrderWindow.getInstance(danhao);
                    }
                }
            });

            myTable2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {

                    if (myTable2.getSelectedRow() > -1) {
                        String danhao = myTable2.getValueAt(myTable2.getSelectedRow(), 0).toString();
                        OrderWindow.getInstance(danhao);
                    }
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(StatisticWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class payActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ZoneId zonedId = ZoneId.of("America/Montreal");
                LocalDate date = LocalDate.now(zonedId);

                if (total1 + total2 > 0) {
                    String sql = "UPDATE INCOMING SET PAID =TRUE";
                    stmt.executeUpdate(sql);
                    sql = "UPDATE outcoming SET PAID =TRUE";
                    stmt.executeUpdate(sql);
                    double sum = total1 + total2;
                    sql = "INSERT into PAY VALUES('" + date + "'," + sum + ")";
                    stmt.execute(sql);

                    panel1.remove(scrollPane1);
                    panel2.remove(scrollPane2);
                    panel41.remove(total);
                    panel41.remove(pay);
                    panel4.remove(panel41);
                    panel3.remove(scrollPane3);

                    total1 = 0.0;
                    total2 = 0.0;
                    total3 = 0.0;
                    sql = "SELECT TRACKNO,DATE,QTY,COST FROM INCOMING WHERE PAID = FALSE ORDER BY date ";
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
                        total1 += resultSet1.getDouble(4);
                        resultSet1.next();
                    }

                    myTable1 = new JTable(rowData1, colNames1);
                    scrollPane1 = new JScrollPane(myTable1);
                    panel1.add(scrollPane1);

                    sql = "SELECT TRACKNO,DATE,QTY,INTERNATIONAL,COST FROM OUTCOMING WHERE PAID = FALSE ORDER BY date ";
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
                        total2 += resultSet1.getDouble(5);
                        resultSet2.next();
                    }
                    myTable2 = new JTable(rowData2, colNames2);
                    scrollPane2 = new JScrollPane(myTable2);
                    panel2.add(scrollPane2);

                    pay = new JButton("Make a payment");
                    pay.addActionListener(new payActionListener());
                    panel41 = new JPanel(new GridLayout(2, 1));
                    total = new JLabel("Total Balance: $" + (total1 + total2));
                    panel41.add(total);
                    panel41.add(pay);
                    panel4.add(panel41, BorderLayout.NORTH);

                    sql = "SELECT * FROM pay";
                    ResultSet resultSet3 = stmt.executeQuery(sql);
                    ResultSetMetaData meta3 = resultSet3.getMetaData();
                    colNames3 = new String[meta3.getColumnCount()];
                    for (int i = 0; i < meta3.getColumnCount(); i++) {
                        colNames3[i] = meta3.getColumnName(i + 1);
                    }
                    resultSet3.last(); // Move to last row
                    int numRows3 = resultSet3.getRow(); // Get row number
                    resultSet3.first(); // Move to first row
                    rowData3 = new String[numRows3 + 1][meta3.getColumnCount()];
                    for (int i = 0; i < numRows3; i++) {
                        for (int j = 0; j < meta3.getColumnCount(); j++) {
                            rowData3[i][j] = resultSet3.getString(j + 1);
                        }
                        total3 += resultSet3.getDouble(2);
                        resultSet3.next();
                    }
                    rowData3[numRows3][0] = "总计：";
                    rowData3[numRows3][1] = String.valueOf(total3);
                    myTable3 = new JTable(rowData3, colNames3);
                    scrollPane3 = new JScrollPane(myTable3);
                    panel3.add(scrollPane3);

                    setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Balance is Zero!");
                }

            } catch (SQLException ex) {
                Logger.getLogger(AccountingWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

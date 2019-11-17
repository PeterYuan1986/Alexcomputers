package GUI;

import java.awt.*;
import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class IncomingWindow extends JFrame {

    public static final IncomingWindow INSTANCE = new IncomingWindow();
    final String DB_URL = "jdbc:derby://localhost:1527/AlexComputers";
    String express, riqi, danhao, huowu, shuliang;
    int total;
    Double feiyong;
    TreeMap<String, String> goodslist;
    boolean returned, paid;

    JPanel panel1, panel2, panel3, panel12, panel11, panel13, panel21, panel22, panel23, panel24, panel25, panel26;
    JScrollPane scrollPane;

    JButton submit, cancel, chuanjian, xiugai, shanchu;
    JComboBox year, month, day, nameBox, quantity;
    JCheckBox Return;
    JLabel kuaidi, date, track, Name, Quantity, totalamount, totalbalance;
    JRadioButton dhl, usps, fedex, ups;
    ButtonGroup radioButtonGroup;
    JTextField name, trackno;
    JTable myTable;

    final String[] colNames = {"货物名称", "数量"};
    final String[] Year = {"2018", "2019", "2020"};
    final String[] Month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    final String[] Day = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    final String[] number = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[][] rowData;

    private IncomingWindow() {
        //define original values
        total = 0;
        feiyong = 0.0;
        express = "";
        danhao = "";
        huowu = "";
        shuliang = "";
        paid = false;
        returned = false;
        goodslist = new TreeMap();
        this.setTitle("Incoming");
        setSize(800, 500);
        setLocation(400, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        //top panel in this JFRAME
        panel1 = new JPanel(new GridLayout(3, 1));
        panel1.setBorder(BorderFactory.createTitledBorder("快递信息"));
        panel12 = new JPanel();
        panel11 = new JPanel();
        panel13 = new JPanel();

        kuaidi = new JLabel("快递公司");
        radioButtonGroup = new ButtonGroup();
        usps = new JRadioButton("USPS");
        dhl = new JRadioButton("DHL");
        ups = new JRadioButton("UPS");
        fedex = new JRadioButton("Fedex");
        radioButtonGroup.add(usps);
        radioButtonGroup.add(dhl);
        radioButtonGroup.add(ups);
        radioButtonGroup.add(fedex);
        usps.addActionListener(new RadioButtonListener());
        dhl.addActionListener(new RadioButtonListener());
        ups.addActionListener(new RadioButtonListener());
        fedex.addActionListener(new RadioButtonListener());
        panel11.add(kuaidi);
        panel11.add(usps);
        panel11.add(dhl);
        panel11.add(ups);
        panel11.add(fedex);

        date = new JLabel("入库日期");
        year = new JComboBox(Year);
        month = new JComboBox(Month);
        day = new JComboBox(Day);
        Return = new JCheckBox("退货");
        panel12.add(date);
        panel12.add(year);
        panel12.add(month);
        panel12.add(day);
        panel12.add(Return);

        track = new JLabel("快递单号");
        trackno = new JTextField(35);
        panel13.add(track);
        panel13.add(trackno);

        panel1.add(panel11);
        panel1.add(panel12);
        panel1.add(panel13);

        //the middle panel in this JFRAME
        panel2 = new JPanel(new GridLayout(2, 3));
        panel21 = new JPanel();
        panel22 = new JPanel();
        panel23 = new JPanel();
        panel24 = new JPanel();
        panel25 = new JPanel();
        panel26 = new JPanel();

        name = new JTextField(50);
        Name = new JLabel("货物名称");
        nameBox = new JComboBox();
        nameBox.setEditable(true);
        panel21.add(Name);
        panel21.add(nameBox);
        panel2.add(panel21);

        Quantity = new JLabel("数量");
        quantity = new JComboBox(number);
        panel22.add(Quantity);
        panel22.add(quantity);
        panel2.add(panel22);

        submit = new JButton("添加货物");
        submit.addActionListener(new submitButtonListener());
        cancel = new JButton("删除货物");
        cancel.addActionListener(new cancelButtonListener());
        panel23.add(submit);
        panel23.add(cancel);
        panel2.add(panel23);

        totalamount = new JLabel("货物数量共计:  " + total);
        panel24.add(totalamount);
        panel2.add(panel24);

        totalbalance = new JLabel("费用共计: $" + feiyong);
        panel25.add(totalbalance);
        panel2.add(panel25);

        chuanjian = new JButton("创建订单");
        chuanjian.addActionListener(new chuanjianButtonListener());
        xiugai = new JButton("修改订单");
        xiugai.addActionListener(new xiugaiButtonListener());
        shanchu = new JButton("删除订单");
        shanchu.addActionListener(new shanchuButtonListener());
        shanchu.setBackground(Color.red);
        panel26.add(chuanjian);
        panel26.add(xiugai);
        panel26.add(shanchu);
        panel2.add(panel26);

        //bottom panel in the whole JFRAME
        panel3 = new JPanel(new GridLayout(1, 1));
        rowData = new String[goodslist.size() + 1][2];
        myTable = new JTable(rowData, colNames);
        scrollPane = new JScrollPane(myTable);
        panel3.add(scrollPane);

        add(panel1);
        add(panel2);
        add(panel3);
        setVisible(true);
    }

    public static void getInstance() {
        INSTANCE.validate();
        INSTANCE.setVisible(true);
    }

    boolean getinformation() {
        danhao = trackno.getText();
        riqi = year.getSelectedItem().toString() + "-"
                + month.getSelectedItem().toString() + "-"
                + day.getSelectedItem().toString();
        returned = Return.isSelected();
        boolean flag = true;
        if (express.equals("")) {
            JOptionPane.showMessageDialog(null, "Pleas choose express company!");
            flag = false;
        } else if (danhao.equals("")) {
            JOptionPane.showMessageDialog(null, "Pleas input tracking Number!");
            flag = false;
        } else if (goodslist.size() == 0) {
            JOptionPane.showMessageDialog(null, "The goods list is empty, pleas input goods information");
            flag = false;
        }
        return flag;
    }

    private class xiugaiButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new shanchuButtonListener().actionPerformed(e);
            new chuanjianButtonListener().actionPerformed(e);
        }
    }

    private class shanchuButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            danhao = trackno.getText();
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                conn.setAutoCommit(false);
                try {
                    Statement stmt = conn.createStatement();
                    String sql1 = "SELECT * FROM Z" + danhao;
                    ResultSet resultFromZ = stmt.executeQuery(sql1);
                    ResultSet resultFromI = null;
                    while (resultFromZ.next()) {
                        int qty_newFromZ = resultFromZ.getInt(2);
                        int qty_oldFromZ = resultFromZ.getInt(3);
                        String name = resultFromZ.getString(1);
                        String sql2 = "SELECT * FROM INVENTORY WHERE NAME='" + name + "'";
                        Statement stmt1 = conn.createStatement();
                        resultFromI = stmt1.executeQuery(sql2);
                        resultFromI.next();
                        int updatedQty_new = resultFromI.getInt(2) - qty_newFromZ;
                        int updatedQty_old = resultFromI.getInt(3) - qty_oldFromZ;
                        String sql3 = "UPDATE INVENTORY SET QTY_NEW = " + updatedQty_new + " WHERE NAME ='" + name + "'";
                        stmt1.executeUpdate(sql3);
                        String sql4 = "UPDATE INVENTORY SET QTY_old = " + updatedQty_old + " WHERE NAME ='" + name + "'";
                        stmt1.executeUpdate(sql4);
                    }
                    String sql5 = "DELETE FROM INCOMING WHERE trackno ='" + danhao + "'";
                    stmt.executeUpdate(sql5);
                    String sql6 = "DROP TABLE Z" + danhao;
                    stmt.execute(sql6);
                    conn.commit();
                    JOptionPane.showMessageDialog(null, "Order delete!!");
                    conn.close();
                } catch (Exception ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(null, "Order deletion failed, please re-build it!"
                            + "\n Error code: " + ex.getMessage());
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Order deletion failed, please re-build it!"
                        + "\n Error code: " + ex.getMessage());
            }
        }
    }

    private class chuanjianButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getinformation()) {
                try {
                    int updatedValue = 0;
                    int previousValue = 0;
                    Connection conn = DriverManager.getConnection(DB_URL);
                    conn.setAutoCommit(false);
                    Statement stmt = conn.createStatement();
                    try {
                        String sql1 = "CREATE TABLE Z" + danhao + "(Name VARCHAR(20), Qty_new INTEGER, qty_old INTEGER)";
                        stmt.execute(sql1);
                        Iterator ss = goodslist.keySet().iterator();
                        while (ss.hasNext()) {
                            Object temp = ss.next();
                            if (!returned) {
                                String sql2 = "INSERT INTO Z" + danhao + " VALUES ('" + temp.toString() + "', " + Integer.parseInt(goodslist.get(temp.toString())) + ",0)";
                                stmt.executeUpdate(sql2);
                                String sql3 = "SELECT QTY_NEW FROM INVENTORY WHERE NAME = '" + temp.toString() + "'";
                                ResultSet result = stmt.executeQuery(sql3);
                                if (result.next()) {
                                    previousValue = result.getInt("qty_new");
                                    updatedValue = previousValue + Integer.parseInt(goodslist.get(temp.toString()));
                                    String sql4 = "UPDATE INVENTORY SET qty_new =" + updatedValue + " WHERE NAME='" + temp.toString() + "'";
                                    stmt.executeUpdate(sql4);
                                } else {
                                    updatedValue = Integer.parseInt(goodslist.get(temp.toString()));
                                    String sql5 = "INSERT INTO INVENTORY (name, qty_new) VALUES('" + temp.toString() + "'," + updatedValue + ")";
                                    stmt.execute(sql5);
                                }
                            } else {
                                String sql9 = "INSERT INTO Z" + danhao + " VALUES ('" + temp.toString() + "', 0," + Integer.parseInt(goodslist.get(temp.toString())) + ")";
                                stmt.executeUpdate(sql9);
                                String sql6 = "SELECT QTY_old FROM INVENTORY WHERE NAME = '" + temp.toString() + "'";
                                ResultSet result = stmt.executeQuery(sql6);
                                if (result.next()) {
                                    previousValue = result.getInt("qty_old");
                                    updatedValue = previousValue + Integer.parseInt(goodslist.get(temp.toString()));
                                    String sql7 = "UPDATE INVENTORY SET qty_old=" + updatedValue + " WHERE NAME='" + temp.toString() + "'";
                                    stmt.executeUpdate(sql7);
                                } else {
                                    updatedValue = Integer.parseInt(goodslist.get(temp.toString()));
                                    String sql8 = "INSERT INTO INVENTORY (name, qty_old) VALUES('" + temp.toString() + "'," + updatedValue + ")";
                                    stmt.execute(sql8);
                                }
                            }
                        }
                        String sql = "INSERT INTO INCOMING VALUES('" + danhao + "', '"
                                + express + "','"
                                + riqi + "',"
                                + total + ","
                                + feiyong + ","
                                + returned + ","
                                + paid + ")";
                        stmt.executeUpdate(sql);
                        conn.commit();
                        JOptionPane.showMessageDialog(null, "Order create successfully!!");
                        conn.close();
                    } catch (Exception ex) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(null, "Order deletion failed, please re-build it!"
                                + "\n Error code: " + ex.getMessage());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Order Creatation failed, please re-build it!"
                            + "\n Error code: " + ex.getMessage());
                }
            }
        }
    }

    private class submitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (nameBox.getSelectedItem() != null) {

                huowu = nameBox.getSelectedItem().toString();
                shuliang = quantity.getSelectedItem().toString();
                int sl = Integer.parseInt(shuliang);
                int originalsl = 0;
                if (goodslist.get(huowu) != null) {
                    originalsl = Integer.parseInt(goodslist.get(huowu));
                }
                goodslist.put(huowu, String.valueOf(originalsl + sl));
                total = total + sl;
                feiyong = total * 0.5;
                rowData = new String[goodslist.size()][2];
                Iterator gl = goodslist.keySet().iterator();
                int index = 0;
                while (gl.hasNext()) {
                    String temp = gl.next().toString();
                    rowData[index][0] = temp;
                    rowData[index][1] = goodslist.get(temp);
                    index++;
                }
                panel3.remove(scrollPane);
                myTable = new JTable(rowData, colNames);
                myTable.setAlignmentX(TOP_ALIGNMENT);
                scrollPane = new JScrollPane(myTable);
                panel3.add(scrollPane);
                panel24.remove(totalamount);
                totalamount = new JLabel("货物数量共计:  " + total);
                panel24.add(totalamount);
                panel25.remove(totalbalance);
                totalbalance = new JLabel("费用共计: $" + feiyong);
                panel25.add(totalbalance);
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Please input goods name");
            }
        }
    }

    private class cancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object Temp = nameBox.getSelectedItem();
            if (Temp != null) {
                if (goodslist.keySet().contains(Temp.toString())) {
                    huowu = Temp.toString();
                    shuliang = quantity.getSelectedItem().toString();
                    int sl = Integer.parseInt(shuliang);
                    int originalsl = Integer.parseInt(goodslist.get(huowu));
                    int value = originalsl - sl > 0 ? originalsl - sl : 0;
                    if (value == 0) {
                        goodslist.remove(huowu);
                    } else {
                        goodslist.put(huowu, String.valueOf(value));
                    }
                    total = total - (sl > originalsl ? originalsl : sl);
                    feiyong = total * 0.5;
                    rowData = new String[goodslist.size()][2];
                    Iterator gl = goodslist.keySet().iterator();
                    int index = 0;
                    while (gl.hasNext()) {
                        String temp = gl.next().toString();
                        rowData[index][0] = temp;
                        rowData[index][1] = goodslist.get(temp);
                        index++;
                    }
                    panel3.remove(scrollPane);
                    myTable = new JTable(rowData, colNames);
                    myTable.setAlignmentX(TOP_ALIGNMENT);
                    scrollPane = new JScrollPane(myTable);
                    panel3.add(scrollPane);
                    panel24.remove(totalamount);
                    totalamount = new JLabel("货物数量共计:  " + total);
                    panel24.add(totalamount);
                    panel25.remove(totalbalance);
                    totalbalance = new JLabel("费用共计: $" + feiyong);
                    panel25.add(totalbalance);
                    setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please input goods name");
            }
        }
    }

    private class RadioButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == usps) {
                express = "USPS";
            }
            if (e.getSource() == dhl);
            {
                express = "DHL";
            }
            if (e.getSource() == ups) {
                express = "UPS";
            }
            if (e.getSource() == fedex) {
                express = "Fedex";
            }
        }
    }

}

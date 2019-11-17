package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {

    private static IncomingWindow padInstance = null; //the singleton instance of your MiniPad
    final int Window_width = 400;
    final int Window_height = 400;

    public MainWindow() {
        setTitle("Alex Computers Storage System");
    }

    public void showWindow() {
        setSize(Window_width, Window_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(5, 1));

        JButton button1 = new JButton("Incoming");
        JButton button2 = new JButton("Outcoming");
        JButton button3 = new JButton("Inventory");
        JButton button4 = new JButton("Accounting");
        JButton button5 = new JButton("Statistic");

        JPanel panel1 = new JPanel(new BorderLayout(50, 50));
        JPanel panel2 = new JPanel(new BorderLayout(50, 50));
        JPanel panel3 = new JPanel(new BorderLayout(50, 50));
        JPanel panel4 = new JPanel(new BorderLayout(50, 50));
        JPanel panel5 = new JPanel(new BorderLayout(50, 50));

        panel1.add(button1);
        panel2.add(button2);
        panel3.add(button3);
        panel4.add(button4);
        panel5.add(button5);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        add(panel5);

        button1.addActionListener(new button1AL());
        button2.addActionListener(new button2AL());
        button3.addActionListener(new button3AL());
        button4.addActionListener(new button4AL());
        button5.addActionListener(new button5AL());

        setVisible(true);
    }

    private class button1AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (padInstance == null) {
                IncomingWindow.getInstance();
            }
        }

    }

    private class button2AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            OutcomingWindow.getInstance();
        }
    }

    private class button3AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            InventoryWindow.getInstance();
        }
    }

    private class button4AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AccountingWindow.getInstance();
        }
    }

    private class button5AL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StatisticWindow.getInstance();
        }
    }

}

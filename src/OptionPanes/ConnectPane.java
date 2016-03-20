package OptionPanes;

import Application.GUI;
import Arp.ArpHelper;
import MetroComponents.*;
import Packet.DeviceManager;
import Packet.PacketManager;

import java.awt.*;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ConnectPane extends JDialog {

    public ConnectPane(Frame parent) {
        super(parent);

        PacketManager packetManager = (PacketManager) GUI.getComponent(GUI.ID.PacketManager);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Connect");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,150);

        Container pane = getContentPane();
        pane.setBackground(MetroColors.DARKER_GRAY);

        JLabel label = new JLabel("IP");
        label.setForeground(MetroColors.SPECIAL_GREEN);

        MetroTextField textField = new MetroTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);

        SpringLayout layout = new SpringLayout();

        // select button
        MetroButton btn = new MetroButton(" Connect ");
        btn.addActionListener(e -> {
            packetManager.connect(textField.getText());
            this.setVisible(false);
            this.dispose();
        });
        Font font = btn.getFont();
        Font newFont = new Font(font.getName(),font.getStyle(),15);
        btn.setFont(newFont);
        btn.setMargin(new Insets(5,5,5,5));

        ArpHelper arpHelper = new ArpHelper();
        MetroTable table = new MetroTable(new DefaultTableModel(
                new Vector<>(Arrays.asList("IP", "MAC","Description")),0));
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // populate device list with reachable devices on the network
        new Thread(()->{
            try {
                for (Arp.Arp entry : arpHelper.refreshCache()){
                    model.addRow(new Object[]{entry.getIp(), entry.getMac(), entry.getDef()});
                }
                table.updateUI();
            }catch (Exception e){}

        }).start();

        // selection listener
        // sets the ip field with the selected device
        table.getSelectionModel().addListSelectionListener(e -> textField.setText(table.getValueAt(table.getSelectedRow(), 0).toString()));

        MetroScrollPane scrollPane = new MetroScrollPane(table);

        layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, pane);

        layout.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.NORTH, pane);
        layout.putConstraint(SpringLayout.EAST, textField, -5, SpringLayout.WEST, btn);

        layout.putConstraint(SpringLayout.EAST, btn, -5, SpringLayout.EAST, pane);
        layout.putConstraint(SpringLayout.NORTH, btn, 5, SpringLayout.NORTH, pane);

        layout.putConstraint(SpringLayout.NORTH, scrollPane, 12, SpringLayout.SOUTH, label);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.EAST, pane);
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, 5, SpringLayout.SOUTH, pane);

        pane.setLayout(layout);
        pane.add(label);
        pane.add(textField);
        pane.add(btn);
        pane.add(scrollPane);


        this.setLocationRelativeTo(parent);
        this.setVisible(true);

    }
}
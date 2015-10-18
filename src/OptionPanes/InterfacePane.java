package OptionPanes;

import MetroComponents.*;
import Packet.Device;
import Packet.DeviceManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class InterfacePane extends JDialog {

    public InterfacePane(Frame parent,DeviceManager deviceManager){
        super(parent);

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("Connect");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,150);

        Container pane = getContentPane();
        pane.setBackground(MetroColors.DARKER_GRAY);

        MetroLabel textLabel = new MetroLabel("Interface");
        textLabel.setForeground(MetroColors.SPECIAL_GREEN);

        MetroTextField textField = new MetroTextField();

        MetroButton button = new MetroButton(" Select ");
        Font font = button.getFont();
        Font newFont = new Font(font.getName(),font.getStyle(),15);
        button.setFont(newFont);
        button.addActionListener(e -> {
            deviceManager.chooseNetworkInterface(textField.getText());
            this.setVisible(false);
            this.dispose();
        });

        // update table with all network devices
        MetroTable table = new MetroTable(new DefaultTableModel(new Vector<>(Arrays.asList("Interface","Description")),0));
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for(Device device : deviceManager.getAllDevices()){
            model.addRow(new Object[]{device.name,device.displayName});
        }
        table.updateUI();

        table.getSelectionModel().addListSelectionListener(e -> textField.setText(table.getValueAt(table.getSelectedRow(), 0).toString()));

        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.WEST, textLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, textLabel, 5, SpringLayout.NORTH, pane);

        layout.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.EAST, textLabel);
        layout.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.NORTH, pane);
        layout.putConstraint(SpringLayout.EAST, textField, -5, SpringLayout.WEST, button);

        layout.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, pane);
        layout.putConstraint(SpringLayout.EAST, button, -5, SpringLayout.EAST, pane);

        layout.putConstraint(SpringLayout.WEST, table, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, table, 5, SpringLayout.SOUTH, textField);
        layout.putConstraint(SpringLayout.EAST, table, -5, SpringLayout.EAST, pane);

        pane.setLayout(layout);
        pane.add(textLabel);
        pane.add(textField);
        pane.add(button);
        pane.add(table);

        this.setLocationRelativeTo(parent);
        this.setVisible(true);

    }

}

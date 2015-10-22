package OptionPanes;

import MetroComponents.MetroColors;
import Packet.CurrentInstance;
import Packet.Packet;

import javax.swing.*;
import java.awt.*;

public class InfoPane extends JDialog {

    public InfoPane(Frame parent) {
        super(parent);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Info");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 150);

        Container pane = getContentPane();
        pane.setBackground(MetroColors.DARKER_GRAY);

        JLabel pcapLabel = new JLabel(String.format("%-15s %s","Pcap version:" , CurrentInstance.getPcap()));
        pcapLabel.setForeground(MetroColors.SPECIAL_GREEN);

        JLabel myIpLabel = new JLabel(String.format("%-15s %s","My IP:" , Packet.ipToString(CurrentInstance.getMyIp())));
        myIpLabel.setForeground(MetroColors.SPECIAL_GREEN);

        JLabel myMacLabel = new JLabel(String.format("%-15s %s","My MAC:" , Packet.macToString(CurrentInstance.getMyMac())));
        myMacLabel.setForeground(MetroColors.SPECIAL_GREEN);

        JLabel gateIpLabel = new JLabel(String.format("%-15s %s","Gateway IP:" , Packet.ipToString(CurrentInstance.getGateIp())));
        gateIpLabel.setForeground(MetroColors.SPECIAL_GREEN);

        JLabel gateMacLabel = new JLabel(String.format("%-15s %s","Gateway MAC:" , Packet.macToString(CurrentInstance.getGateMac())));
        gateMacLabel.setForeground(MetroColors.SPECIAL_GREEN);

        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.WEST, pcapLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, pcapLabel, 5, SpringLayout.NORTH, pane);

        layout.putConstraint(SpringLayout.WEST, myIpLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, myIpLabel, 5, SpringLayout.SOUTH, pcapLabel);

        layout.putConstraint(SpringLayout.WEST, myMacLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, myMacLabel, 5, SpringLayout.SOUTH, myIpLabel);

        layout.putConstraint(SpringLayout.WEST, gateIpLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, gateIpLabel, 5, SpringLayout.SOUTH, myMacLabel);

        layout.putConstraint(SpringLayout.WEST, gateMacLabel, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, gateMacLabel, 5, SpringLayout.SOUTH, gateIpLabel);

        pane.setLayout(layout);
        pane.add(pcapLabel);
        pane.add(myIpLabel);
        pane.add(myMacLabel);
        pane.add(gateIpLabel);
        pane.add(gateMacLabel);

        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
}
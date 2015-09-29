package OptionPanes;

import Arp.ArpHelper;
import IPStresser.IPStresserManager;
import MetroComponents.*;
import Packet.PacketManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Mikes Gaming on 5/25/2015.
 */
public class StresserSettingsPane extends JDialog{

    public StresserSettingsPane(Frame parent, IPStresserManager ipStresserManager){
        super(parent);

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("Stresser Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,150);

        Container pane = getContentPane();
        pane.setBackground(Color.BLACK);

        JLabel label = new JLabel("Email");
        label.setForeground(Color.WHITE);
        MetroTextField emailField = new MetroTextField();

        JLabel label2 = new JLabel("Password");
        label2.setForeground(Color.WHITE);
        MetroPasswordField passwordField = new MetroPasswordField();

        SpringLayout layout = new SpringLayout();

        MetroButton btn = new MetroButton(" Login ");
        Font font = btn.getFont();
        Font newFont = new Font(font.getName(),font.getStyle(),15);
        btn.setFont(newFont);
        btn.setMargin(new Insets(5,5,5,5));
        btn.addActionListener(e -> {
            ipStresserManager.login(emailField.getText(),String.valueOf(passwordField.getPassword()));
        });

        MetroButton btn2 = new MetroButton(" Test ");

        layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, pane);

        layout.putConstraint(SpringLayout.WEST, emailField, 5, SpringLayout.EAST, label2);
        layout.putConstraint(SpringLayout.NORTH, emailField, 5, SpringLayout.NORTH, pane);
        layout.putConstraint(SpringLayout.EAST, emailField, -5, SpringLayout.EAST, pane);

        layout.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, label2, 10, SpringLayout.SOUTH, label);

        layout.putConstraint(SpringLayout.EAST, btn, -5, SpringLayout.EAST, pane);
        layout.putConstraint(SpringLayout.NORTH, btn, 5, SpringLayout.SOUTH, emailField);

        layout.putConstraint(SpringLayout.WEST, passwordField, 5, SpringLayout.EAST, label2);
        layout.putConstraint(SpringLayout.NORTH, passwordField, 5, SpringLayout.SOUTH, emailField);
        layout.putConstraint(SpringLayout.EAST, passwordField, -5, SpringLayout.WEST, btn);

        layout.putConstraint(SpringLayout.WEST, btn2, 5, SpringLayout.WEST, pane);
        layout.putConstraint(SpringLayout.NORTH, btn2, 5, SpringLayout.SOUTH, passwordField);

        pane.setLayout(layout);
        pane.add(label);
        pane.add(emailField);
        pane.add(label2);
        pane.add(passwordField);
        pane.add(btn);
        pane.add(btn2);


        this.setLocationRelativeTo(parent);
        this.setVisible(true);


    }

}

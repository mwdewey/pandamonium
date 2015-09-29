package OptionPanes;

import Arp.ArpPoison;
import MetroComponents.MetroLabel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mikes Gaming on 7/28/2015.
 */
public class ARPSettingsPane extends JDialog {

    public ARPSettingsPane(Frame parent, ArpPoison arpPoison){
        super(parent);

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("Arp Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,150);

        Container pane = getContentPane();
        pane.setBackground(Color.BLACK);

        //MetroLabel

    }

}

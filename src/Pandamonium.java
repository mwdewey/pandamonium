import MetroComponents.*;
import OptionPanes.InterfacePane;
import OptionPanes.ConnectPane;
import Packet.DeviceManager;
import Packet.PacketManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class Pandamonium extends JFrame {

    Pandamonium() {
        super("Pandamonium");
        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        ImageIcon img = new ImageIcon("icon.png");
        this.setIconImage(img.getImage());

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.GRAY);
        this.add(centerPanel, BorderLayout.CENTER);

        MetroMenuBar menuBar = new MetroMenuBar();
        MetroMenu menuSettings = new MetroMenu("File");
        MetroLabel statusLabel = new MetroLabel("Target: unknown");
        MetroLabel statusLabel2 = new MetroLabel("Interface: unknown ");
        statusLabel.setForeground(new Color(29, 185, 84));
        statusLabel2.setForeground(new Color(29, 185, 84));
        menuBar.add(menuSettings);
        MetroItem interfaceItem = new MetroItem("Interface");
        menuSettings.add(interfaceItem);
        MetroItem connectItem = new MetroItem("Connect");
        menuSettings.add(connectItem);

        MetroMenu menuSettings2 = new MetroMenu("Settings");
        menuBar.add(menuSettings2);
        MetroItem buttonPacketSettings = new MetroItem("Packet");
        menuSettings2.add(buttonPacketSettings);
        MetroItem buttonStresserSettings = new MetroItem("Stresser");
        menuSettings2.add(buttonStresserSettings);

        MetroButton clearButton = new MetroButton("Clear");
        //menuBar.add(clearButton);

        this.add(menuBar, BorderLayout.NORTH);

        MetroMenuBar menuBarBottom = new MetroMenuBar();
        menuBarBottom.add(statusLabel2);
        menuBarBottom.add(statusLabel);
        this.add(menuBarBottom, BorderLayout.SOUTH);

        MetroMenuBar menuBarBottom2 = new MetroMenuBar();
        menuBarBottom2.add(new JLabel("X"));
        //this.add(menuBarBottom2, BorderLayout.WEST);

        MetroTable table = new MetroTable(new DefaultTableModel(
                new Vector<String>(Arrays.asList("Ip", "Port", "Sent", "Received", "pSent", "pReceived", "Host")), 0));

        for (int i = 0; i < 20; i++) {
            //((DefaultTableModel)table.getModel()).addRow(new Object[]{"Ip", "Port","Sent","Received","pSent","pReceived","Host"});
        }

        MetroScrollPane pane = new MetroScrollPane(table);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pane, BorderLayout.CENTER);

        centerPanel.updateUI();


        // Managers
        DeviceManager deviceManager = new DeviceManager(statusLabel2);
        PacketManager packetManager = new PacketManager(table, statusLabel, deviceManager);

        connectItem.addActionListener(e -> new ConnectPane(this, packetManager, deviceManager));
        interfaceItem.addActionListener(e -> new InterfacePane(this, deviceManager));
        clearButton.addActionListener(e -> packetManager.clear());
    }


    public static void main(String[] argv) throws Exception {
        UIManager.put("ScrollBar.background", MetroColors.DARK_GRAY);
        UIManager.put("ScrollBar.foreground", MetroColors.DARK_GRAY);
        UIManager.put("ScrollBar.darkShadow", MetroColors.DARK_GRAY);
        UIManager.put("ScrollBar.highlight", MetroColors.DARK_GRAY);
        UIManager.put("ScrollBar.shadow", MetroColors.DARK_GRAY);

        UIManager.put("TableHeader.background", MetroColors.DARK_GRAY);
        UIManager.put("TableHeader.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("TableHeader.cellBorder", new MatteBorder(0, 1, 0, 1, MetroColors.SPECIAL_GREEN));

        UIManager.put("Table.background", MetroColors.DARK_GRAY);
        UIManager.put("Table.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("Table.selectionBackground", MetroColors.SPECIAL_GREEN);
        UIManager.put("Table.selectionForeground", Color.BLACK);

        UIManager.put("Menu.background", MetroColors.DARKER_GRAY);
        UIManager.put("Menu.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("Menu.selectionBackground", MetroColors.SPECIAL_GREEN);
        UIManager.put("Menu.selectionForeground", Color.BLACK);

        UIManager.put("MenuItem.background", MetroColors.DARKER_GRAY);
        UIManager.put("MenuItem.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("MenuItem.selectionBackground", MetroColors.SPECIAL_GREEN);
        UIManager.put("MenuItem.selectionForeground", Color.BLACK);

        UIManager.put("MenuBar.background", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("MenuBar.borderColor", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.darkShadow", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.shadow", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.highlight", MetroColors.DARKER_GRAY);

        Pandamonium test = new Pandamonium();
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

    }
}

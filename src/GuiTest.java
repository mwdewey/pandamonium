import MetroComponents.*;
import OptionPanes.InterfacePane;
import OptionPanes.LoginPane;
import Packet.DeviceManager;
import Packet.PacketManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class GuiTest extends JFrame {

    GuiTest(){
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
        statusLabel.setForeground(new Color(29,185,84));
        statusLabel2.setForeground(new Color(29,185,84));
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
                new Vector<String>(Arrays.asList("Ip", "Port","Sent","Received","pSent","pReceived","Host")),0));

        for(int i=0; i < 20; i++){
            //((DefaultTableModel)table.getModel()).addRow(new Object[]{"Ip", "Port","Sent","Received","pSent","pReceived","Host"});
        }

        MetroScrollPane pane = new MetroScrollPane(table);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pane, BorderLayout.CENTER);

        centerPanel.updateUI();


        // Managers
        DeviceManager deviceManager = new DeviceManager(statusLabel2);
        PacketManager packetManager = new PacketManager(table,statusLabel,deviceManager);

        connectItem.addActionListener(e -> new LoginPane(this, packetManager));
        interfaceItem.addActionListener(e -> new InterfacePane(this,deviceManager));
        //buttonStresserSettings.addActionListener(e -> new StresserSettingsPane(this,ipStresserManager));
        clearButton.addActionListener(e -> packetManager.clear());
    }







    public static void main(String[] argv) throws Exception{
        
        Color darkGray = new Color(40,40,40);
        Color darkerGray = new Color(30,30,30);
        Color specialGreen = new Color(29,185,84);
        Color specialGreenDark = new Color(3, 170, 60);
        Color specialText = new Color(190,190,190);

        UIManager.put("ScrollBar.background", darkGray);
        UIManager.put("ScrollBar.foreground", darkGray);
        UIManager.put("ScrollBar.darkShadow", darkGray);
        UIManager.put("ScrollBar.highlight", darkGray);
        UIManager.put("ScrollBar.shadow", darkGray);

        UIManager.put("TableHeader.background", darkGray);
        UIManager.put("TableHeader.foreground", specialText);
        UIManager.put("TableHeader.cellBorder" , new MatteBorder(0,1,0,1, specialGreen));

        UIManager.put("Table.background", darkGray);
        UIManager.put("Table.foreground", specialText);
        UIManager.put("Table.selectionBackground", specialGreen);
        UIManager.put("Table.selectionForeground", Color.BLACK);

        UIManager.put("Menu.background", darkerGray);
        UIManager.put("Menu.foreground", specialText);
        UIManager.put("Menu.selectionBackground", specialGreen);
        UIManager.put("Menu.selectionForeground", Color.BLACK);

        UIManager.put("MenuItem.background", darkerGray);
        UIManager.put("MenuItem.foreground", specialText);
        UIManager.put("MenuItem.selectionBackground", specialGreen);
        UIManager.put("MenuItem.selectionForeground", Color.BLACK);

        UIManager.put("MenuBar.background", darkerGray);
        UIManager.put("MenuBar.foreground", specialText);
        UIManager.put("MenuBar.borderColor", darkerGray);
        UIManager.put("MenuBar.darkShadow", darkerGray);
        UIManager.put("MenuBar.shadow", darkerGray);
        UIManager.put("MenuBar.highlight", darkerGray);

        GuiTest test = new GuiTest();
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

    }
}

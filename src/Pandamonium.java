import MetroComponents.*;
import OptionPanes.InfoPane;
import OptionPanes.InterfacePane;
import OptionPanes.ConnectPane;
import Packet.CurrentInstance;
import Packet.DeviceManager;
import Packet.PacketManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        MetroItem buttonInfoSettings = new MetroItem("Info");
        menuSettings2.add(buttonInfoSettings);
        MetroItem clearButton = new MetroItem("Clear Effects");
        menuSettings2.add(clearButton);
        MetroItem clearPacketsButton = new MetroItem("Clear Packets");
        menuSettings2.add(clearPacketsButton);

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

        MetroRightClickMenu rightClickMenu = new MetroRightClickMenu();
        MetroItem bMetroItem = new MetroItem("Bandwidth");
        MetroItem lMetroItem = new MetroItem("Latency");
        MetroItem dMetroItem = new MetroItem("Drop");
        MetroItem oMetroItem = new MetroItem("Order");
        MetroItem iMetroItem = new MetroItem("Inject");
        MetroItem clearMetroItem = new MetroItem("Clear");

        rightClickMenu.add(bMetroItem);
        rightClickMenu.add(lMetroItem);
        rightClickMenu.add(dMetroItem);
        rightClickMenu.add(oMetroItem);
        rightClickMenu.add(iMetroItem);
        rightClickMenu.add(clearMetroItem);

        bMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(Color.MAGENTA,Color.BLACK,row);
                }

            }
        });

        lMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(Color.CYAN,Color.BLACK,row);
                }

            }
        });

        dMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(Color.ORANGE,Color.BLACK,row);
                }

            }
        });

        oMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(Color.pink,Color.BLACK,row);
                }

            }
        });

        iMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(MetroColors.SPECIAL_TEXT,Color.BLACK,row);
                }

            }
        });

        clearMetroItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();

                for(int row : rows){
                    table.setColorAtIndex(MetroColors.DARK_GRAY,MetroColors.SPECIAL_TEXT,row);
                }

            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if( e.getButton() == MouseEvent.BUTTON1){
                    System.out.println("button left");
                }
                else if( e.getButton() == MouseEvent.BUTTON3){
                    System.out.println("button right");

                    rightClickMenu.show(table,e.getX(),e.getY());

                }

                else System.out.println("not a button");
            }

            public void mouseReleased(MouseEvent e) {

            }
        });

        /*for (int i = 0; i < 20; i++) {
            table.colors.add(MetroColors.DARK_GRAY);
            table.textColors.add(MetroColors.SPECIAL_TEXT);
            ((DefaultTableModel)table.getModel()).addRow(new Object[]{"123.123.123.123", "80", "45678", "23456", "23", "12", "www.google.com"});
        }*/

        MetroScrollPane pane = new MetroScrollPane(table);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pane, BorderLayout.CENTER);

        centerPanel.updateUI();

        // init arp cache
        CurrentInstance.setArpCache(new HashMap<>());


        // Managers
        DeviceManager deviceManager = new DeviceManager(statusLabel2);
        PacketManager packetManager = new PacketManager(table, statusLabel, deviceManager);

        connectItem.addActionListener(e -> new ConnectPane(this, packetManager, deviceManager));
        interfaceItem.addActionListener(e -> new InterfacePane(this, deviceManager));
        buttonInfoSettings.addActionListener(e -> new InfoPane(this));
        clearButton.addActionListener(e -> {
            int size = table.colors.size();

            for(int i = 0; i < size; i++){
                table.setColorAtIndex(MetroColors.DARK_GRAY,MetroColors.SPECIAL_TEXT,i);
            }

            table.invalidate();
        });

        clearPacketsButton.addActionListener((e) -> {
            DefaultTableModel dModel = (DefaultTableModel) table.getModel();
            int rows = dModel.getRowCount();

            dModel.setRowCount(0);
            table.colors.clear();
            table.textColors.clear();
            packetManager.packetStreams.clear();

        });
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

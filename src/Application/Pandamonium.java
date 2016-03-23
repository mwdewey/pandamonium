package Application;

import MetroComponents.*;
import OptionPanes.InfoPane;
import OptionPanes.InterfacePane;
import OptionPanes.ConnectPane;
import Packet.CurrentInstance;
import Packet.DeviceManager;
import Packet.PacketManager;
import Table.MainTableModel;
import Table.TableObject;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Pandamonium extends JFrame {

    Pandamonium() {
        super("Pandamonium");
        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(false);
        ImageIcon img = new ImageIcon("icon.png");
        this.setIconImage(img.getImage());

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.GRAY);
        this.add(centerPanel, BorderLayout.CENTER);

        MetroMenuBar menuBar = new MetroMenuBar();
        MetroMenu menuSettings = new MetroMenu("File");
        MetroLabel statusLabel = new MetroLabel("Target: unknown");
        GUI.setComponent(GUI.ID.MainTargetStatus,statusLabel);
        MetroLabel statusLabel2 = new MetroLabel("Interface: unknown ");
        GUI.setComponent(GUI.ID.MainInterfaceStatus,statusLabel2);
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

        MetroTable table = new MetroTable(new MainTableModel());
        GUI.setComponent(GUI.ID.MainPacketTable,table);

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

        for (int i = 0; i < 20; i++) {
            table.colors.add(MetroColors.DARK_GRAY);
            table.textColors.add(MetroColors.SPECIAL_TEXT);
            ((MainTableModel)table.getModel()).addElement(new TableObject("192.168.0.1",80,50,50,1,1,"local",i));
        }

        MetroScrollPane pane = new MetroScrollPane(table);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(pane, BorderLayout.CENTER);

        centerPanel.updateUI();

        // init arp cache
        CurrentInstance.setArpCache(new HashMap<>());


        // Managers
        GUI.setComponent(GUI.ID.DeviceManager,new DeviceManager());
        GUI.setComponent(GUI.ID.PacketManager,new PacketManager());

        connectItem.addActionListener(e -> new ConnectPane(this));
        interfaceItem.addActionListener(e -> new InterfacePane(this));
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
            ((PacketManager) GUI.getComponent(GUI.ID.PacketManager)).packetStreams.clear();

        });

        setVisible(true);
    }


    public static void main(String[] argv) throws Exception {
        Pandamonium test = new Pandamonium();


    }
}

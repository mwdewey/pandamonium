package Packet;

import Application.GUI;
import Arp.ArpProxy;
import MetroComponents.MetroColors;
import MetroComponents.MetroTable;
import PacketHandlers.ARPHandler;
import PacketHandlers.GUIHandler;
import PacketHandlers.ICMPHandler;
import PacketHandlers.PacketHandler;
import Table.MainTableModel;
import Table.TableObject;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketManager {
    MetroTable table;
    MainTableModel model;
    JLabel statusLabel;
    DeviceManager deviceManager;

    public List<PacketStream> packetStreams;
    boolean connected = false;
    String targetIp;

    List<PacketHandler> handlers;

    public PacketManager(){
        this.table = (MetroTable) GUI.getComponent(GUI.ID.MainPacketTable);
        this.model = (MainTableModel) table.getModel();
        this.statusLabel = (JLabel) GUI.getComponent(GUI.ID.MainTargetStatus);
        this.deviceManager = (DeviceManager) GUI.getComponent(GUI.ID.DeviceManager);

        this.packetStreams = new ArrayList<>();

        // add all packet handlers to handlers
        handlers = new ArrayList<>();
        handlers.add(new ARPHandler());
        handlers.add(new ICMPHandler());
        handlers.add(new GUIHandler());
    }

    public void connect(String ip){

        targetIp = ip;
        connected = true;
        statusLabel.setText("Target: " + targetIp);

        // target to arp proxy is selected, start the proxy
        ArpProxy arpProxy = new ArpProxy(CurrentInstance.getPcap()
                ,Packet.ipStringToByte(targetIp),
                CurrentInstance.getArpCache().get(ByteBuffer.wrap(Packet.ipStringToByte(targetIp))).array(),
                CurrentInstance.getMyIp(),
                CurrentInstance.getMyMac(),
                CurrentInstance.getGateIp(),
                CurrentInstance.getGateMac());

        //arpProxy.startProxy(5000);
    }

    public String chooseDevice(){
        Device selectedDevice = deviceManager.selectedDevice;

        List<PcapIf> alldevs = new ArrayList<>();
        StringBuilder errbuf = new StringBuilder();
        Pcap.findAllDevs(alldevs, errbuf);

        String selectedDeviceFullName = null;
        for (PcapIf device : alldevs) {
            try {
                if (Arrays.equals(selectedDevice.ip,device.getAddresses().get(0).getAddr().getData())) {
                    selectedDeviceFullName = device.getName();
                }
            }catch (Exception e){}
        }

        return selectedDeviceFullName;
    }


    public synchronized void receivePacket(Packet p){

        // skip ip packets that are ipv6
        if(p.isIp && p.isIpv6) return;

        //for(PacketHandler ph : handlers) ph.getPacket(p);
        handlers.get(0).getPacket(p);

    }

    public void clear(){
        System.out.println("List cleared");
        packetStreams.clear();
        model.clear();

    }
}

package Packet;

import Application.GUI;
import Arp.ArpProxy;
import MetroComponents.MetroColors;
import MetroComponents.MetroTable;
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
    DefaultTableModel model;
    JLabel statusLabel;
    DeviceManager deviceManager;

    public List<PacketStream> packetStreams;
    boolean connected = false;
    String targetIp;

    public PacketManager(){
        this.table = (MetroTable) GUI.getComponent(GUI.ID.MainPacketTable);
        this.model = (DefaultTableModel) table.getModel();
        this.statusLabel = (JLabel) GUI.getComponent(GUI.ID.MainTargetStatus);
        this.deviceManager = (DeviceManager) GUI.getComponent(GUI.ID.DeviceManager);

        this.packetStreams = new ArrayList<>();
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

        arpProxy.startProxy(5000);
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
                    System.out.println(device.getName());
                    selectedDeviceFullName = device.getName();
                }
            }catch (Exception e){}
        }

        return selectedDeviceFullName;
    }


    public synchronized void receivePacket(Packet p){
        try {
            // if packet doesn't match target, drop
            //System.out.println(Packet.ipToString(p.ipSrc) + "|" + Packet.ipToString(p.ipDst));
            if (!(Packet.ipToString(p.ipSrc).equals(targetIp) || Packet.ipToString(p.ipDst).equals(targetIp))) return;

            // check if packet is received or sent by target

            byte[] tempIp;
            byte[] tempPort;
            if (Packet.ipToString(p.ipSrc).equals(targetIp)) {
                tempIp = p.ipDst;
                tempPort = p.portDst;
            } else {
                tempIp = p.ipSrc;
                tempPort = p.portSrc;
            }

            boolean found = false;
            for (PacketStream ps : packetStreams) {
                if (Arrays.equals(ps.ip, tempIp) && Arrays.equals(ps.port, tempPort)) {
                    found = true;

                    if (Packet.ipToString(p.ipSrc).equals(targetIp)) {
                        ps.bytesOut += p.length;
                        model.setValueAt(ps.bytesOut, ps.row, 2);
                        ps.pSent++;
                        model.setValueAt(ps.pSent, ps.row, 4);
                    } else {
                        ps.bytesIn += p.length;
                        model.setValueAt(ps.bytesIn, ps.row, 3);
                        ps.pReceived++;
                        model.setValueAt(ps.pReceived, ps.row, 5);
                    }

                }
            }

            if (!found) {
                PacketStream ps = new PacketStream(tempIp, tempPort, p.length, model.getRowCount(), targetIp);
                packetStreams.add(ps);

                model.addRow(new Object[]{Packet.ipToString(tempIp), Packet.portToInt(tempPort), ps.bytesOut, ps.bytesIn, ps.pSent, ps.pReceived, ps.hostName});
                table.colors.add(MetroColors.DARK_GRAY);
                table.textColors.add(MetroColors.SPECIAL_TEXT);
                new Thread(() -> {
                    String temp = Packet.getHostName(Packet.ipToString(tempIp));
                    if(Packet.ipToString(tempIp).equals(temp)) temp = "-";
                    model.setValueAt(temp, ps.row, 6);
                }).start();

            }
        }catch (Exception e){}
    }

    public void clear(){
        System.out.println("List cleared");
        packetStreams.clear();
        model.setRowCount(0);

    }
}

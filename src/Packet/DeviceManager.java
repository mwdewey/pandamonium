package Packet;

import Application.GUI;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;

import javax.swing.*;
import java.net.NetworkInterface;
import java.util.*;

public class DeviceManager {

    Device selectedDevice;
    JLabel statusLabel;
    Pcap pcap;

    public DeviceManager(){

        this.statusLabel = (JLabel) GUI.getComponent(GUI.ID.MainInterfaceStatus);
        selectedDevice = null;

    }

    public List<Device> getAllDevices(){
        List<Device> devices = new ArrayList<>();
        List<PcapIf> alldevs = new ArrayList<>();
        StringBuilder errbuf = new StringBuilder();
        Pcap.findAllDevs(alldevs, errbuf);

        for(PcapIf device : alldevs){
            try {
                devices.add(new Device(device));

            }catch (Exception e){e.printStackTrace();}
        }

        return devices;
    }

    // the name is the device we are connecting to
    public void chooseNetworkInterface(String name){

        // get selected device from list of devices
        List<Device> devices = getAllDevices();
        Device selectedDevice = null;
        for(Device device : devices){
            if(name.equals(device.name)) selectedDevice = device;
        }
        this.selectedDevice = selectedDevice;
        statusLabel.setText("Interface: " + name + " ");

        // set the current instance to be used elsewhere in the application
        CurrentInstance.setPcap(name);
        CurrentInstance.setMyIp(this.selectedDevice.ip);
        CurrentInstance.setMyMac(this.selectedDevice.mac);
        CurrentInstance.setGateIp(Packet.getGateIp(this.selectedDevice.ip));
        CurrentInstance.setGateMac(Packet.getGateMac(this.selectedDevice.ip));
        CurrentInstance.setNetMask(this.selectedDevice.netMask);
        CurrentInstance.setBroadcastIp(this.selectedDevice.broadcastIp);

        // start pcap
        PacketSniffer packetSniffer = new PacketSniffer(name);
        new Thread(packetSniffer).start();
    }

}

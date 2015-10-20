package Packet;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import java.net.NetworkInterface;
import java.util.*;

/**
 * Created by Mikes Gaming on 6/1/2015.
 */
public class DeviceManager {

    Device selectedDevice;
    JLabel statusLabel;
    Pcap pcap;

    public DeviceManager(JLabel statusLabel){

        this.statusLabel = statusLabel;
        selectedDevice = null;

    }

    public List<Device> getAllDevices(){
        List<Device> devices = new ArrayList<>();
        Enumeration<NetworkInterface> nets;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        }catch (Exception e){return null;}

        List<NetworkInterface> netList = Collections.list(nets);
        for(NetworkInterface device : netList){
            try {
                if(device.isUp() && !device.isLoopback()) devices.add(new Device(device));
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

        List<PcapIf> alldevs = new ArrayList<>();
        StringBuilder errbuf = new StringBuilder();
        Pcap.findAllDevs(alldevs, errbuf);

        String selectedDeviceFullName = null;
        for (PcapIf device : alldevs) {
            try {
                if (Arrays.equals(this.selectedDevice.ip,device.getAddresses().get(0).getAddr().getData())) {
                    selectedDeviceFullName = device.getName();
                }
            }catch (Exception e){}
        }

        // set the current instance to be used elsewhere in the application
        CurrentInstance.setPcap(selectedDeviceFullName);
        CurrentInstance.setMyIp(this.selectedDevice.ip);
        CurrentInstance.setMyMac(this.selectedDevice.mac);
        CurrentInstance.setGateIp(Packet.getGateIp(this.selectedDevice.ip));
        CurrentInstance.setGateMac(Packet.getGateMac(this.selectedDevice.ip));

    }

}

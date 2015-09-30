package Packet;

import javax.swing.*;
import java.net.NetworkInterface;
import java.util.*;

/**
 * Created by Mikes Gaming on 6/1/2015.
 */
public class DeviceManager {

    Device selectedDevice;
    JLabel statusLabel;

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

    public void setSelectedDeviceName(String name){

        List<Device> devices = getAllDevices();

        Device selectedDevice = null;
        for(Device device : devices){
            if(name.equals(device.name)) selectedDevice = device;
        }
        this.selectedDevice = selectedDevice;

        statusLabel.setText("Interface: " + name + " ");
    }

}

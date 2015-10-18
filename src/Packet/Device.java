package Packet;

import java.net.NetworkInterface;
import java.net.SocketException;

public class Device {
    public String name;
    public String displayName;
    public byte[] mac;
    public byte[] ip;

    public Device(NetworkInterface networkInterface){
        this.name = networkInterface.getName();
        this.displayName = networkInterface.getDisplayName();
        try {
            this.mac = networkInterface.getHardwareAddress();
        }catch (SocketException e){
            System.out.print(displayName);
        }

        try {
            this.ip = networkInterface.getInterfaceAddresses().get(0).getAddress().getAddress();
        }catch (Exception e){}

    }

}

package Packet;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by Mikes Gaming on 6/1/2015.
 */
public class Device {
    public String name;
    public String displayName;
    public byte[] mac;
    public String ip;

    public Device(NetworkInterface networkInterface){
        this.name = networkInterface.getName();
        this.displayName = networkInterface.getDisplayName();
        try {
            this.mac = networkInterface.getHardwareAddress();
        }catch (SocketException e){
            System.out.print(displayName);
        }

        try {
            this.ip = networkInterface.getInterfaceAddresses().get(0).getAddress().getHostAddress();
        }catch (Exception e){}

    }

}

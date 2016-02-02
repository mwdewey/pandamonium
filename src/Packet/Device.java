package Packet;

import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

public class Device {
    public String name;
    public String displayName;
    public byte[] mac;
    public byte[] ip;
    public byte[] netMask;
    public byte[] broadcastIp;

    public Device(PcapIf networkInterface){
        this.name = networkInterface.getName();
        this.displayName = networkInterface.getDescription();
        try {
            this.mac = networkInterface.getHardwareAddress();
        }catch (IOException e){
            System.out.print(displayName);
        }

        try {
            this.ip = null;
            for (PcapAddr addr : networkInterface.getAddresses()){
                if(addr.getAddr().getData().length == 4){
                    this.ip = addr.getAddr().getData();
                    this.netMask = addr.getNetmask().getData();
                    this.broadcastIp = addr.getBroadaddr().getData();
                }
                System.out.println(addr.toString());
            }
        }catch (Exception e){}

    }

}

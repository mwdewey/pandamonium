package Packet;

import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;

import java.nio.ByteBuffer;

public class PacketSniffer implements Runnable{

    String deviceName;
    PacketManager packetManager;
    Pcap pcap;

    PacketSniffer(String deviceName,PacketManager packetManager){
        this.deviceName = deviceName;
        this.packetManager = packetManager;
    }

    public void run(){
        StringBuilder errbuf = new StringBuilder();
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_NON_PROMISCUOUS;
        int timeout = 10 * 1000;
        pcap = Pcap.openLive(deviceName, snaplen, flags, timeout, errbuf);

        ByteBufferHandler<PacketManager> bbh = (PcapHeader packet,ByteBuffer b, PacketManager pm) -> {
            byte[] temp = new byte[b.remaining()];
            b.get(temp);
            Packet p = new Packet(temp);

            packetManager.receivePacket(p);
        };

        pcap.loop(0, bbh, null);

        pcap.close();
    }


}

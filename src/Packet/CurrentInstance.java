package Packet;

import Arp.Arp;
import org.jnetpcap.Pcap;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class CurrentInstance {
    // shared pcap instance
    private static Pcap pcap;

    // should be static, not change
    private static byte[] myIp;
    private static byte[] myMac;
    private static byte[] gateIp;
    private static byte[] gateMac;
    private static byte[] netMask;
    private static byte[] broadcastIp;

    // application's arp table, used because window doesn't update arp cache unless a packet is sent to the machine
    private static Map<ByteBuffer,ByteBuffer> arpCache;

    public static void setPcap(Pcap p){
        pcap = p;
    }

    public static void setPcap(String deviceName){
        StringBuilder errbuf = new StringBuilder();
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_NON_PROMISCUOUS;
        int timeout = 10 * 1000;

        pcap = Pcap.openLive(deviceName, snaplen, flags, timeout, errbuf);
    }

    public static Pcap getPcap(){
        return pcap;
    }

    public static Pcap getTestPcap(){
        StringBuilder errbuf = new StringBuilder();
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_NON_PROMISCUOUS;
        int timeout = 10 * 1000;
        String name = "\\Device\\NPF_{6CF302CF-235C-4A1E-86B2-937687018777}";

        return Pcap.openLive(name, snaplen, flags, timeout, errbuf);
    }

    public static byte[] getMyIp(){ return myIp; }

    public static void setMyIp(byte[] ip){ myIp = ip;}

    public static byte[] getMyMac(){ return myMac; }

    public static void setMyMac(byte[] mac){ myMac = mac; }

    public static byte[] getGateIp(){ return gateIp; }

    public static void setGateIp(byte[] ip){ gateIp = ip; }

    public static byte[] getGateMac(){ return gateMac; }

    public static void setGateMac(byte[] mac){ gateMac = mac; }

    public static byte[] getNetMask(){ return netMask; }

    public static void setNetMask(byte[] nm){ netMask = nm; }

    public static byte[] getBroadcastIp(){ return broadcastIp; }

    public static void setBroadcastIp(byte[] bIp){ broadcastIp = bIp; }

    public static Map<ByteBuffer,ByteBuffer> getArpCache(){ return arpCache; }

    public static void setArpCache(Map<ByteBuffer,ByteBuffer> cache){ arpCache = cache; }

    public static void updateArpEntry(byte[] mac, byte[] ip){
        arpCache.put(ByteBuffer.wrap(mac),ByteBuffer.wrap(ip));
        arpCache.put(ByteBuffer.wrap(ip),ByteBuffer.wrap(mac));
    }


}

package Packet;

import org.jnetpcap.Pcap;

public class CurrentInstance {
    private static Pcap pcap;

    private static byte[] myIp;
    private static byte[] myMac;
    private static byte[] gateIp;
    private static byte[] gateMac;

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

    public static byte[] getMyIp(){
        return myIp;
    }

    public static void setMyIp(byte[] ip){
        myIp = ip;
    }

    public static byte[] getMyMac(){
        return myMac;
    }

    public static void setMyMac(byte[] mac){
        myMac = mac;
    }

    public static byte[] getGateIp(){
        return gateIp;
    }

    public static void setGateIp(byte[] ip){
        gateIp = ip;
    }

    public static byte[] getGateMac(){
        return gateMac;
    }

    public static void setGateMac(byte[] mac){
        gateMac = mac;
    }


}

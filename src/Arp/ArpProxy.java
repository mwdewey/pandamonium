package Arp;

import org.jnetpcap.Pcap;

public class ArpProxy {
    Pcap pcap;
    byte[] targIp;
    byte[] targMac;
    byte[] myIp;
    byte[] myMac;
    byte[] gateIp;
    byte[] gateMac;

    ArpPacket targReply;
    ArpPacket targRequest;
    ArpPacket gateReply;
    ArpPacket gateRequest;

    boolean runProxy = false;

    public ArpProxy(Pcap pcap,byte[] targIp,byte[] targMac,byte[] myIp,byte[] myMac,byte[] gateIp,byte[] gateMac){
        this.pcap = pcap;
        this.targIp = targIp;
        this.targMac = targMac;
        this.myIp = myIp;
        this.myMac = myMac;
        this.gateIp = gateIp;
        this.gateMac = gateMac;

        targRequest = new ArpPacket(ArpPacket.Opcode.REQUEST,myMac,myIp,targIp,targMac);
        targReply = new ArpPacket(ArpPacket.Opcode.REPLY,myMac,myIp,targIp,targMac);

        gateReply = new ArpPacket(ArpPacket.Opcode.REPLY,myMac,myIp,targIp,targMac);
        gateRequest = new ArpPacket(ArpPacket.Opcode.REQUEST,myMac,myIp,targIp,targMac);
    }

    public void startProxy(long interval){
        runProxy = true;

        while(runProxy) {

            // pause for next iteration
            try {
                Thread.sleep(interval);
            }catch (Exception e){}
        }


    }

    public void endProxy(){

    }

}

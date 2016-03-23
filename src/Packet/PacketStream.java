package Packet;

public class PacketStream {
    byte[] ip;
    byte[] port;

    String hostName;
    int bytesIn;
    int bytesOut;
    int pReceived;
    int pSent;

    int row;
    boolean isIncomming;

    PacketStream(byte[] ipRaw,byte[] portRaw, int length, int row, String ipTarget){
        this.ip = ipRaw;
        this.port = portRaw;
        this.row = row;

        bytesIn = 0;
        bytesOut = 0;
        pReceived = 0;
        pSent = 0;

        if(isIncomming) bytesIn = length;
        else bytesOut = length;

        if(isIncomming) pReceived = 1;
        else pSent = 1;

    }

}

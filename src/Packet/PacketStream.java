package Packet;

public class PacketStream {
    public byte[] ip;
    public byte[] port;

    public String hostName;
    public int bytesIn;
    public int bytesOut;
    public int pReceived;
    public int pSent;

    public int row;
    public boolean isIncomming;

    public PacketStream(byte[] ipRaw,byte[] portRaw, int length, int row, String ipTarget){
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

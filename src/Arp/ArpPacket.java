package Arp;

import java.nio.ByteBuffer;

public class ArpPacket {
    private byte[] arpPacket;

    private static final byte[] macDst = {(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
    private byte[] macSrc;
    private static final byte[] type = {0x08,0x06};
    private static final byte[] hardType = {0x00,0x01};
    private static final byte[] protType = {0x08,0x00};
    private static final byte[] hardSize = {0x06};
    private static final byte[] protSize = {0x04};
    private static final byte[] opCode = {0x00,0x01};
    private byte[] sendIp;
    private static final byte[] targMac = {0x00,0x00,0x00,0x00,0x00,0x00};
    private byte[] targIp;


    public ArpPacket(byte[] macSrc, byte[] sendIp, byte[] targIp){

        arpPacket = new byte[42];

        this.macSrc = macSrc;
        this.sendIp = sendIp;
        this.targIp = targIp;

        constructPacket();
    }

    public byte[] getBytes(){
        return arpPacket;
    }

    public void setTargIp(byte[] targIp){
        this.targIp = targIp;
        constructPacket();
    }

    private void constructPacket(){
        ByteBuffer target = ByteBuffer.wrap(arpPacket);
        target.put(macDst);
        target.put(this.macSrc);
        target.put(type);
        target.put(hardType);
        target.put(protType);
        target.put(hardSize);
        target.put(protSize);
        target.put(opCode);
        target.put(this.macSrc);
        target.put(this.sendIp);
        target.put(targMac);
        target.put(this.targIp);
    }

}

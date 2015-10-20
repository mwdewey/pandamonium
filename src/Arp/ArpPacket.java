package Arp;

import java.nio.ByteBuffer;

public class ArpPacket {
    public enum Opcode {
        REQUEST,
        REPLY
    }

    private byte[] arpPacket;

    private byte[] macDst;
    private byte[] sendMac;
    private static final byte[] type = {0x08, 0x06};
    private static final byte[] hardType = {0x00, 0x01};
    private static final byte[] protType = {0x08, 0x00};
    private static final byte[] hardSize = {0x06};
    private static final byte[] protSize = {0x04};
    private byte[] opCode;
    private byte[] sendIp;
    private byte[] targMac;
    private byte[] targIp;


    public ArpPacket(Opcode opCode,byte[] sendMac, byte[] sendIp, byte[] targIp, byte[] targMac) {

        arpPacket = new byte[42];

        switch (opCode){
            case REQUEST : {
                this.opCode = new byte[]{0x00, 0x01};
                this.targMac = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                this.macDst = new byte[]{-1, -1, -1, -1, -1, -1};
                break;
            }
            case REPLY : {
                this.opCode = new byte[]{0x00, 0x02};
                this.targMac = targMac != null ? targMac : new byte[6];
                this.macDst = targMac;
                break;
            }
            default: break;
        }

        this.sendMac = sendMac != null ? sendMac : new byte[6];
        this.sendIp = sendIp != null ? sendIp : new byte[4];
        this.targIp = targIp != null ? targIp : new byte[4];

        constructPacket();
    }

    public byte[] getBytes() {
        return arpPacket;
    }

    public void setTargIp(byte[] targIp) {
        this.targIp = targIp;
        constructPacket();
    }

    private void constructPacket() {
        ByteBuffer target = ByteBuffer.wrap(arpPacket);
        target.put(macDst);
        target.put(this.sendMac);
        target.put(type);
        target.put(hardType);
        target.put(protType);
        target.put(hardSize);
        target.put(protSize);
        target.put(opCode);
        target.put(this.sendMac);
        target.put(this.sendIp);
        target.put(this.targMac);
        target.put(this.targIp);
    }

}

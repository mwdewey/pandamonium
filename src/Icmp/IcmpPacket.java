package Icmp;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class IcmpPacket {
    private byte[] icmpPacket;

    private byte[] destinationMac;
    private byte[] sourceMac;
    private static final byte[] type = {0x08, 0x06};

    private static final byte[] version = {0x04};
    private static final byte[] diffServiceField = {0x00};
    private static final byte[] totalLength = {0x00,0x00};
    private byte[] id;
    private static final byte[] flags = {0x02};
    private static final byte[] frag_offset = {0x00};
    private static final byte[] ttl = {0x40};
    private static final byte[] protocol = {0x01};
    private byte[] headerChecksum;
    private byte[] sourceIp;
    private byte[] destinationIp;

    private byte[] icmpType;
    private static final byte[] code = {0x00};
    private byte[] checksum;
    private static final byte[] id_be = {0x0F,0x5A};
    private static final byte[] id_le = {0x5A,0x0F};
    private byte[] seq_be;
    private byte[] seq_le;
    private static final byte[] data = new byte[56];

    public IcmpPacket(byte[] destinationMac,byte[] sourceMac,byte[] destinationIp,byte[] sourceIp, byte[] seq_be) {

        icmpPacket = new byte[98];

        this.destinationMac = destinationMac;
        this.sourceMac = sourceMac;

        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;

        this.icmpType = new byte[]{0x08};
        this.seq_be = seq_be;
        this.seq_le = new byte[2]; this.seq_le[0] = seq_be[1]; this.seq_le[1] = seq_be[0];

        constructPacket();
    }

    public IcmpPacket(byte[] frameData){

    }

    public boolean isReply(){
        return this.icmpType[0] != 0x08;
    }

    public byte[] getSourceIp(){
        return this.sourceIp;
    }

    public short getId(){
        byte[] bytes = this.seq_be;
        int r = bytes[1] & 0xFF; r = (r << 8) | (bytes[0] & 0xFF);
        return (short)r;
    }

    public byte[] getBytes() {
        return icmpPacket;
    }

    private void computeIPv4Checksum(){

        // buff size is # of 32-bit words, minus 2 bytes for not including checksum
        ByteBuffer target = ByteBuffer.allocate((version[0] & 0x0f) * 4 - 2);

        target.put(version);
        target.put(diffServiceField);
        target.put(totalLength);
        target.put(id);
        target.put(flags);
        target.put(frag_offset);
        target.put(ttl);
        target.put(protocol);
        target.put(sourceIp);
        target.put(destinationIp);

        short sum = 0;
        ShortBuffer values = target.asShortBuffer();
        while (values.hasRemaining()) sum += values.get();

        headerChecksum = new byte[]{(byte)(sum & 0xff),(byte)((sum >> 8) & 0xff)};
    }

    private void computeIcmpChecksum(){
        ByteBuffer target = ByteBuffer.allocate(1000);

        target.put(icmpType);
        target.put(code);
        target.put(id_be);
        target.put(id_le);
        target.put(seq_be);
        target.put(seq_le);
        target.put(data);

        short sum = 0;
        ShortBuffer values = target.asShortBuffer();
        while (values.hasRemaining()) sum += values.get();

        checksum = new byte[]{(byte)(sum & 0xff),(byte)((sum >> 8) & 0xff)};

    }

    private void constructPacket() {

        computeIPv4Checksum();
        computeIcmpChecksum();

        ByteBuffer target = ByteBuffer.wrap(icmpPacket);
        target.put(destinationMac);
        target.put(sourceMac);
        target.put(type);

        target.put(version);
        target.put(diffServiceField);
        target.put(totalLength);
        target.put(id);
        target.put(flags);
        target.put(frag_offset);
        target.put(ttl);
        target.put(protocol);
        target.put(headerChecksum);
        target.put(sourceIp);
        target.put(destinationIp);

        target.put(icmpType);
        target.put(code);
        target.put(checksum);
        target.put(id_be);
        target.put(id_le);
        target.put(seq_be);
        target.put(seq_le);
        target.put(data);

    }

}

package Table;

public class ARPTableObject {

    public byte[] ip;
    public byte[] mac;
    public String desc;

    public ARPTableObject(byte[] ip, byte[] mac, String desc) {
        this.ip = ip;
        this.mac = mac;
        this.desc = desc;
    }
}

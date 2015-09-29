package Arp;

/**
 * Created by Mikes Gaming on 5/17/2015.
 */
public class Arp {
    String mac;
    String ip;
    String def;
    
    public Arp(String mac,String ip,String def){
        this.mac = mac;
        this.ip = ip;
        this.def = def;
    }

    public String getMac(){
        return mac;
    }

    public String getIp(){
        return ip;
    }

    public String getDef(){
        return def;
    }
}

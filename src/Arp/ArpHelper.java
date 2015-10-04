package Arp;

import Packet.Packet;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ArpHelper {
    Map<String,String> ouiList;

    public ArpHelper(){

        List<String> ouiEntries = new ArrayList<>();
        ouiList = new HashMap<>();
        try {
            Files.lines(Paths.get("ouidb.txt")).forEach(ouiEntries::add);
        }catch (IOException e){e.printStackTrace();}
        for(String oui : ouiEntries){
            String[] tokens = oui.split("[\t ]");
            String macAddress = "";
            String shortDef = "";
            String longDef = "";

            if(tokens.length > 0) macAddress = tokens[0].replace(":","");
            if(tokens.length > 1) shortDef = tokens[1];

            if(tokens.length > 2){
                for(int i = 4; i < tokens.length; i++){
                    longDef += tokens[i] + " ";
                }
            }

            if(longDef.length() != 0) ouiList.put(macAddress.toLowerCase(),longDef.trim().replace("# ",""));
            else ouiList.put(macAddress.toLowerCase(),shortDef);

            //System.out.printf("mac: %s def: %s\n",macAddress.toLowerCase(),longDef);
            //System.out.println(oui);
        }
    }

    public List<Arp> getAll(){
        List<Arp> response = new ArrayList<>();

        String rawArpCache = getARPCache().replace("-", "");
        String[] cacheEntries = rawArpCache.split("\n");

        if(cacheEntries.length < 2) return null;

        for(int i = 0; i < cacheEntries.length; i++){
            String tempOUI = cacheEntries[i].substring(0,6);
            String[] tokens = cacheEntries[i].split(",");

            String tempIp = tokens[1];
            String tempMac = tokens[0];

            if(ouiList.containsKey(tempOUI)){
                response.add(new Arp(tempMac,tempIp,ouiList.get(tempOUI)));
            }

        }

        return response;
    }

    public String getARPCache() {
        String cmd = "arp -a";
        Runtime run = Runtime.getRuntime();
        String result = "";

        try {
            Process proc = run.exec(cmd);
            BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                if(line.length() == 0) continue;

                line = line.trim();

                if(line.split("[ ]+").length != 3) continue;

                result += line.split("[ ]+")[1] + "," + line.split("[ ]+")[0] + "\n";
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return (result);
    }

    public List<Arp> refreshCache() throws Exception{
        byte[] IP = InetAddress.getLocalHost().getAddress();

        byte[] mac = {0x3c,(byte)0xa9,(byte)0xf4,0x56,(byte)0xa2,(byte)0xac};
        byte[] sendIp = InetAddress.getLocalHost().getAddress();
        byte[] targip = InetAddress.getLocalHost().getAddress();

        ArpPacket arpPacket = new ArpPacket(mac,sendIp,targip);

        StringBuilder errbuf = new StringBuilder();
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_NON_PROMISCUOUS;
        int timeout = 10 * 1000;
        Pcap pcap = Pcap.openLive("\\Device\\NPF_{6CF302CF-235C-4A1E-86B2-937687018777}", snaplen, flags, timeout, errbuf);

        List<Arp> arpList = new ArrayList<>();

        ByteBufferHandler<List<Arp>> bbh = (PcapHeader packet,ByteBuffer b, List<Arp> arpListLoop) -> {
            byte[] temp = new byte[b.remaining()];
            b.get(temp);

            Packet p = new Packet(temp);

            // check if arp packet
            if(temp[12] == 8 && temp[13] == 6){
                //System.out.println("Pass");
                //check if reply
                if(temp[21] == 2){
                    byte[] senderMac = Arrays.copyOfRange(temp, 22, 28);
                    byte[] senderIp = Arrays.copyOfRange(temp, 28, 32);

                    String macString = Packet.macToString(senderMac);
                    String ouiMac = macString.substring(0,6);
                    String desc = "-";

                    if(ouiList.containsKey(ouiMac)) desc = ouiList.get(ouiMac);

                    arpListLoop.add(new Arp(macString,Packet.ipToString(senderIp),desc));
                }
            }
        };

        new Thread(()->{
            pcap.loop(0, bbh, arpList);
        }).start();
        new Thread(()-> {
            int temp = targip[2];
            for (int i = -3; i < 4; i++) {
                targip[2] = (byte)((temp+i) & 0xff);

                for (int j = 1; j < 256; j++) {
                    targip[3] = (byte) (j & (0xff));
                    arpPacket.setTargIp(targip);
                    byte[] sendPacket = arpPacket.getBytes();

                    pcap.sendPacket(sendPacket);
                }
            }
        }).start();

        Thread.sleep(2000);
        pcap.breakloop();
        pcap.close();

        return arpList;
    }

}
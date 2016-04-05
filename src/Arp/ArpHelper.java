package Arp;

import Packet.*;
import Table.ARPTableObject;
import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import sun.security.jca.GetInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ArpHelper {
    private static ArpHelper instance;

    Map<String, String> ouiList = new HashMap<>();

    public static ArpHelper GetInstance(){
        if(instance == null) instance = new ArpHelper();
        return instance;
    }

    private ArpHelper() {
        ouiList = new HashMap<>();

        createOuiList();
    }

    private void createOuiList(){
        List<String> ouiEntries = new ArrayList<>();
        try {
            Files.lines(Paths.get("ouidb.txt")).forEach(ouiEntries::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String oui : ouiEntries) {
            String[] tokens = oui.split("[\t ]");
            String macAddress = "";
            String shortDef = "";
            String longDef = "";

            if (tokens.length > 0) macAddress = tokens[0];

            // if can't parse mac, entry is invalid
            if (macAddress.equals("")) continue;

            // 36-bit OUI def
            if (macAddress.contains("/36")) macAddress = macAddress.substring(0, 13);

            if (tokens.length > 1) shortDef = tokens[1];

            if (tokens.length > 2) {
                for (int i = 4; i < tokens.length; i++) {
                    longDef += tokens[i] + " ";
                }
            }

            if (longDef.length() != 0) ouiList.put(macAddress, longDef.trim().replace("# ", ""));
            else ouiList.put(macAddress, shortDef);
        }
    }

    public String getDefinition(byte[] mac){
        String macString = Packet.macToString(mac);
        if(macString == null) macString = "";
        String desc = "-";

        String ouiMac = macString.substring(0, 13);
        if (ouiList.containsKey(ouiMac)) desc = ouiList.get(ouiMac);
        else {
            ouiMac = macString.substring(0, 8);

            if (ouiList.containsKey(ouiMac)) desc = ouiList.get(ouiMac);
        }

        return desc;
    }

    public void refreshCache() {

        Pcap pcap = CurrentInstance.getPcap();
        byte[] mac = CurrentInstance.getMyMac();
        byte[] myIp = CurrentInstance.getMyIp();
        byte[] targetIp = new byte[4];

        // init arp request packet that will be used to collect all devices on the network
        ArpPacket arpPacket = new ArpPacket(ArpPacket.Opcode.REQUEST,mac, myIp.clone(), targetIp, null);

        new Thread(() -> {
            byte[] initIp = Arrays.copyOf(Packet.getInitIp(),Packet.getInitIp().length);
            double numIps = Math.pow(2, 32 - Packet.getPrefixLength(CurrentInstance.getNetMask()));
            int currIp = 0;

            while (currIp < numIps) {

                if (initIp[3] == (byte) (0xFF)) {
                    initIp[3] = 1;
                    initIp[2]++;
                } else initIp[3]++;

                arpPacket.setTargIp(initIp);
                byte[] sendPacket = arpPacket.getBytes();
                pcap.sendPacket(sendPacket);

                currIp++;
            }

        }).start();
    }

    public List<ARPTableObject> getCache(){
        List<ARPTableObject> arpTableObjects = new ArrayList<>();

        // add descriptions to arp list
        for(ByteBuffer macBuffer : CurrentInstance.getArpCache().keySet()){
            if(macBuffer.array().length == 6){
                ByteBuffer ipBuff = CurrentInstance.getArpCache().get(macBuffer);

                String desc = getDefinition(macBuffer.array());

                arpTableObjects.add(new ARPTableObject(ipBuff.array(),macBuffer.array(), desc));

            }
        }

        // sort arp list by ip
        arpTableObjects = arpTableObjects.stream().sorted((arp1,arp2) -> Integer.compare(
                ByteBuffer.wrap(arp1.ip).getInt(),
                ByteBuffer.wrap(arp2.ip).getInt())
        ).collect(Collectors.toList());

        return arpTableObjects;
    }


}

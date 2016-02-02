package Arp;

import Packet.*;
import org.jnetpcap.ByteBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;

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
    Map<String, String> ouiList;

    public ArpHelper() {

        List<String> ouiEntries = new ArrayList<>();
        ouiList = new HashMap<>();
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

            // skip mac addresses with submac addressing
            if(macAddress.contains("/") || macAddress.equals("")) continue;

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

    public List<Arp> getAll() {
        List<Arp> response = new ArrayList<>();

        String rawArpCache = getARPCache().replace("-", "");
        String[] cacheEntries = rawArpCache.split("\n");

        if (cacheEntries.length < 2) return null;

        for (int i = 0; i < cacheEntries.length; i++) {
            String tempOUI = cacheEntries[i].substring(0, 6);
            String[] tokens = cacheEntries[i].split(",");

            String tempIp = tokens[1];
            String tempMac = tokens[0];

            if (ouiList.containsKey(tempOUI)) {
                response.add(new Arp(tempMac, tempIp, ouiList.get(tempOUI)));
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
                if (line.length() == 0) continue;

                line = line.trim();

                if (line.split("[ ]+").length != 3) continue;

                result += line.split("[ ]+")[1] + "," + line.split("[ ]+")[0] + "\n";
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return (result);
    }

    public List<Arp> refreshCache() throws Exception {

        Pcap pcap = CurrentInstance.getPcap();
        byte[] mac = CurrentInstance.getMyMac();
        byte[] sendIp = CurrentInstance.getMyIp();

        ArpPacket arpPacket = new ArpPacket(ArpPacket.Opcode.REQUEST,mac, sendIp, null, null);

        List<Arp> arpList = new ArrayList<>();

        ByteBufferHandler<List<Arp>> bbh = (PcapHeader packet, ByteBuffer b, List<Arp> arpListLoop) -> {
            byte[] temp = new byte[b.remaining()];
            b.get(temp);

            // check if arp packet
            if (temp[12] == 8 && temp[13] == 6) {
                //check if reply
                if (temp[21] == 2) {
                    byte[] senderMac = Arrays.copyOfRange(temp, 22, 28);
                    byte[] senderIp = Arrays.copyOfRange(temp, 28, 32);

                    CurrentInstance.updateArpEntry(senderMac,senderIp);
                }
            }
        };


        new Thread(() -> {
            pcap.loop(0, bbh, null);
        }).start();
        new Thread(() -> {
            byte[] initIp = Packet.getInitIp();
            System.out.println(Packet.ipToString(initIp));
            //double numIps = Math.pow(2, 32 - Packet.getPrefixLength());
            double numIps = 1;
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

        Thread.sleep(2000);
        pcap.breakloop();

        // add descriptions to arp list
        for(ByteBuffer macBuffer : CurrentInstance.getArpCache().keySet()){
            if(macBuffer.array().length == 6){
                ByteBuffer ipBuff = CurrentInstance.getArpCache().get(macBuffer);

                String macString = Packet.macToString(macBuffer.array());
                String ouiMac = macString.substring(0, 8);
                String desc = "-";

                if (ouiList.containsKey(ouiMac)) desc = ouiList.get(ouiMac);

                arpList.add(new Arp(macString, Packet.ipToString(ipBuff.array()), desc));
            }
        }

        // sort arp list by ip
        arpList = arpList.stream().sorted((arp1,arp2) -> Integer.compare(
                ByteBuffer.wrap(Packet.ipStringToByte(arp1.getIp())).getInt(),
                ByteBuffer.wrap(Packet.ipStringToByte(arp2.getIp())).getInt())
        ).collect(Collectors.toList());

        return arpList;
    }


}

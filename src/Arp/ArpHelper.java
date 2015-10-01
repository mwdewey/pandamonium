package Arp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void refreshCache() throws Exception{
        byte[] IP = InetAddress.getLocalHost().getAddress();
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(new byte[]{}, 0, InetAddress.getLocalHost(), 57);

        for(int j = 0; j < 255; j++) {
            IP[3] = (byte) (j & (0xff));
            packet.setAddress(InetAddress.getByAddress(IP));
            datagramSocket.send(packet);
        }

    }

}

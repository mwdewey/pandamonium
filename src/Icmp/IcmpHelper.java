package Icmp;

import Packet.CurrentInstance;
import Packet.Packet;
import org.jnetpcap.Pcap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IcmpHelper {

    private static Map<String,List<Long>> ipToLatencyList = new HashMap<>();
    private static Map<Short,IcmpRequest> idToRequest = new HashMap<>();
    private static Map<String,Long> ipToLatency = new HashMap<>();

    private static short globalUniqueID = 0;
    private static final int QUEUE_DEPTH = 5;

    public static void sendPing(byte[] targetIp){

        List<Long> requestList;
        String ipString = Packet.ipToString(targetIp);
        Pcap pcap = CurrentInstance.getPcap();

        // add request queue if one does not exist
        if(ipToLatencyList.containsKey(ipString)) requestList = ipToLatencyList.get(ipString);
        else {
            requestList = new ArrayList<>();
            ipToLatencyList.put(ipString,requestList);
        }

        // create id
        byte[] id = new byte[2];
        id[0] = (byte)(globalUniqueID & 0xff);
        id[1] = (byte)((globalUniqueID >> 8) & 0xff);
        globalUniqueID++;

        IcmpPacket icmpPacket = new IcmpPacket(CurrentInstance.getGateMac(),
                CurrentInstance.getMyMac(),targetIp,
                CurrentInstance.getMyIp(), id);


        pcap.sendPacket(icmpPacket.getBytes());
    }

    public static void replyReceived(IcmpPacket replyPacket){
        byte[] sourceIp = replyPacket.getSourceIp();
        String ipString = Packet.ipToString(sourceIp);
        IcmpRequest request = idToRequest.get(replyPacket.getId());
        List<Long> latencyList = ipToLatencyList.get(ipString);

        // notify request that a reply has been made
        request.replyReceived();

        // recalculate latency
        long latency = request.getLatency();

        latencyList.add(latency);
        if(latencyList.size() > QUEUE_DEPTH) latencyList.remove(0);

        long sum = 0;
        for(Long l : latencyList) sum += l;
        long avgLatency = sum/latencyList.size();

        ipToLatency.put(ipString,avgLatency);
    }

}

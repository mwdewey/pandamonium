package Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.*;

public class Packet {
    public byte[] ipV;
    public byte[] ipSrc;
    public byte[] ipDst;
    public byte[] portSrc;
    public byte[] portDst;

    boolean isIpv6;
    int length;

    public Packet(byte[] raw){
        ipV     = Arrays.copyOfRange(raw, 14, 15);
        ipSrc   = Arrays.copyOfRange(raw, 26, 30);
        ipDst   = Arrays.copyOfRange(raw, 30, 34);
        portSrc = Arrays.copyOfRange(raw, 34, 36);
        portDst = Arrays.copyOfRange(raw, 36, 38);

        isIpv6 = ipV[0] != 69;
        length = raw.length;
    }

    public static String ipToString(byte[] raw){
        if(raw.length != 4) return null;
        return String.format("%d.%d.%d.%d",raw[0]&0xFF,raw[1]&0xFF,raw[2]&0xFF,raw[3]&0xFF);
    }

    public static String macToString(byte[] raw){
        if(raw.length != 6) return null;

        return String.format("%02X:%02X:%02X:%02X:%02X:%02X",raw[0]&0xff,raw[1]&0xff,raw[2]&0xff,raw[3]&0xff,raw[4]&0xff,raw[5]&0xff);
    }

    public static byte[] ipStringToByte(String ipString){
        // assume format is n.n.n.n

        byte[] ipbytes = new byte[4];

        String[] tokens = ipString.split("\\.");

        if(tokens.length == 4){
            for(int i=0; i<4; i++){
                ipbytes[i] = (byte) Integer.parseInt(tokens[i]);
            }

        }

        return ipbytes;
    }

    public static byte[] macStringToByte(String macString){
        // assume MAC string is HH:HH:HH:HH:HH:HH seperated with : or -

        byte[] macbytes = new byte[6];
        String[] tokens = macString.split("[-:]+");

        if(tokens.length == 6){
            for(int i=0; i<6; i++){
                macbytes[i] = (byte) Integer.parseInt(tokens[i],16);
            }

        }

        return macbytes;
    }

    public static int portToInt(byte[] raw){
        if(raw.length != 2) return -1;
        short srcP = ByteBuffer.wrap(raw).getShort();
        return srcP&0xFFFF;
    }

    public static String getHostName(final String ip)
    {
        String retVal = null;
        final String[] bytes = ip.split("\\.");
        if (bytes.length == 4)
        {
            try
            {
                final java.util.Hashtable<String, String> env = new java.util.Hashtable<String, String>();
                env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
                final javax.naming.directory.DirContext ctx = new javax.naming.directory.InitialDirContext(env);
                final String reverseDnsDomain = bytes[3] + "." + bytes[2] + "." + bytes[1] + "." + bytes[0] + ".in-addr.arpa";
                final javax.naming.directory.Attributes attrs = ctx.getAttributes(reverseDnsDomain, new String[]
                        {
                                "PTR",
                        });
                for (final javax.naming.NamingEnumeration<? extends javax.naming.directory.Attribute> ae = attrs.getAll(); ae.hasMoreElements();)
                {
                    final javax.naming.directory.Attribute attr = ae.next();
                    final String attrId = attr.getID();
                    for (final java.util.Enumeration<?> vals = attr.getAll(); vals.hasMoreElements();)
                    {
                        String value = vals.nextElement().toString();

                        if ("PTR".equals(attrId))
                        {
                            final int len = value.length();
                            if (value.charAt(len - 1) == '.')
                            {
                                // Strip out trailing period
                                value = value.substring(0, len - 1);
                            }
                            retVal = value;
                        }
                    }
                }
                ctx.close();
            }
            catch (final javax.naming.NamingException e) {}
        }

        if (null == retVal)
        {
            try
            {
                retVal = java.net.InetAddress.getByName(ip).getCanonicalHostName();
            }
            catch (final java.net.UnknownHostException e1)
            {
                retVal = ip;
            }
        }

        return retVal;
    }

    public static short getPrefixLength(){
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            return networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        }catch (Exception e){return 0;}
    }

    public static byte[] getNetMask(){
        short prefixLength = getPrefixLength();
        byte[] netMask = new byte[4];

        for(int i = 0; i < 4; i++){
            byte b = 0;
            for(int j = 0; j < 8; j++){
                if(i*8+j < prefixLength) b = (byte)(b | 1<<(7-j));
            }
            netMask[i] = b;
        }

        return netMask;
    }

    // first ip to list
    public static byte[] getInitIp(){
        byte[] netMask = CurrentInstance.getNetMask();

        byte[] initIp = CurrentInstance.getMyIp();

        for(int i = 0; i < 4; i++){
            initIp[i] = (byte)(initIp[i] & netMask[i]);
        }

        return initIp;
    }

    // get the gateway's ip for a specific interface
    public static byte[] getGateIp(byte[] ip){

        String gateIpString = "";

        try
        {
            // get gateip from ipv4 route table
            Process pro = Runtime.getRuntime().exec("route -n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

            String line;
            while((line = bufferedReader.readLine())!=null)
            {
                // remove leading and trailing spaces
                line = line.trim();

                // split on space and remove consecutive spaces
                String [] tokens = line.split("[ ]+");

                // an example of a route table entry
                //Network Destination        Netmask          Gateway        Interface  Metric
                //            0.0.0.0        0.0.0.0   129.161.67.254   129.161.66.206      25
                if(tokens.length == 8 && tokens[0].equals("0.0.0.0") && !tokens[1].equals("0.0.0.0") )
                {
                    gateIpString = tokens[1];
                }
            }
        }
        catch(Exception e){}

        return ipStringToByte(gateIpString);
    }

    // get the gateway's mac for a specific interface
    public static byte[] getGateMac(byte[] ip){

        // get gateway ip
        String gateIpString = ipToString(getGateIp(ip));

        // use arp table to do a reverse arp lookup for the mac
        Runtime run = Runtime.getRuntime();
        String gateMacString = "";

        try {
            Process proc = run.exec("arp -n");
            BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                if (line.length() != 0) {
                    line = line.trim();

                    String[] tokens = line.split("[ ]+");
                    if (tokens.length == 5) {
                        // now we are looking at entries that look like this:
                        // 129.161.67.254        40-55-39-24-27-41     dynamic
                        // we want to match the ip with the mac
                        if (tokens[0].equals(gateIpString)) {
                            gateMacString = tokens[2];
                        }
                    }
                }

            }
        } catch (Exception e){e.printStackTrace();}

        return macStringToByte(gateMacString);
    }

}

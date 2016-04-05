package PacketHandlers;

import Application.GUI;
import MetroComponents.MetroColors;
import MetroComponents.MetroTable;
import Packet.Packet;
import Table.MainTableModel;
import Table.TableObject;
import Packet.PacketStream;
import Packet.CurrentInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIHandler implements PacketHandler {

    MetroTable table;
    MainTableModel model;
    List<TableObject> tableObjects;
    List<PacketStream> packetStreams;

    byte[] targetIp;

    public GUIHandler(){
        table = (MetroTable) GUI.getComponent(GUI.ID.MainPacketTable);
        model = (MainTableModel) table.getModel();
        tableObjects = model.list;

        packetStreams = new ArrayList<>();
        targetIp = CurrentInstance.getMyIp();
    }

    @Override
    public void getPacket(Packet p) {
        // if packet doesn't match target, drop
        //System.out.println(Packet.ipToString(p.ipSrc) + "|" + Packet.ipToString(p.ipDst));

        // check if packet is received or sent by target

        byte[] tempIp;
        byte[] tempPort;
        if (Arrays.equals(p.ipSrc,targetIp)) {
            tempIp = p.ipDst;
            tempPort = p.portDst;
        } else {
            tempIp = p.ipSrc;
            tempPort = p.portSrc;
        }

        PacketStream ps = packetStreams.stream().filter(ps_ -> Arrays.equals(ps_.ip, tempIp)).findFirst().get();

        if(ps != null){
            if (Arrays.equals(p.ipSrc,targetIp)) {
                ps.bytesOut += p.length;
                model.setValueAt(ps.bytesOut, ps.row, 2);
                ps.pSent++;
                model.setValueAt(ps.pSent, ps.row, 4);
            } else {
                ps.bytesIn += p.length;
                model.setValueAt(ps.bytesIn, ps.row, 3);
                ps.pReceived++;
                model.setValueAt(ps.pReceived, ps.row, 5);
            }
        }

        else {
            ps = new PacketStream(tempIp, tempPort, p.length, model.getRowCount(), Packet.ipToString(targetIp));
            packetStreams.add(ps);

            model.addElement(new TableObject(Packet.ipToString(tempIp), Packet.portToInt(tempPort), ps.bytesOut, ps.bytesIn, ps.pSent, ps.pReceived, ps.hostName,0));
            table.colors.add(MetroColors.DARK_GRAY);
            table.textColors.add(MetroColors.SPECIAL_TEXT);
            PacketStream ps2 = ps;
            new Thread(() -> {
                String temp = Packet.getHostName(Packet.ipToString(tempIp));
                if(Arrays.equals(p.ipSrc,targetIp)) temp = "-";
                model.setValueAt(temp, ps2.row, 6);
            }).start();
        }
    }
}

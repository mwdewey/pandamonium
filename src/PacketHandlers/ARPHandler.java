package PacketHandlers;

import Application.GUI;
import Arp.ArpHelper;
import MetroComponents.MetroTable;
import Packet.Packet;

import java.util.Arrays;
import Packet.CurrentInstance;
import Table.ARPTableModel;
import Table.ARPTableObject;

public class ARPHandler implements PacketHandler {

    public ARPHandler(){

    }

    @Override
    public void getPacket(Packet p) {
        byte[] temp = p.raw;
        if (temp[12] == 8 && temp[13] == 6) {
            //check if reply
            if (temp[21] == 2) {

                byte[] senderMac = Arrays.copyOfRange(temp, 22, 28);
                byte[] senderIp = Arrays.copyOfRange(temp, 28, 32);

                CurrentInstance.updateArpEntry(senderMac,senderIp);
                String def = ArpHelper.GetInstance().getDefinition(senderMac);
                updateUI(new ARPTableObject(senderIp,senderMac,def));
            }
        }
    }

    private void updateUI(ARPTableObject arpTableObject){
        MetroTable table = (MetroTable) GUI.getComponent(GUI.ID.ARPTable);

        // if table does not exist, don't update it
        if(table == null) return;

        ARPTableModel model = (ARPTableModel) table.getModel();

        if(model != null){
            model.addElement(arpTableObject);
        }
    }


}

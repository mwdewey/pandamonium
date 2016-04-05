package Table;

import Packet.Packet;

import javax.swing.table.AbstractTableModel;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ARPTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Ip", "Mac", "Description"};
    public List<ARPTableObject> list;

    public ARPTableModel() {
        list = new LinkedList<>();
    }

    public void addElement(ARPTableObject object) {
        int index = 0;
        int ipValue = ByteBuffer.wrap(object.ip).getInt();

        for(ARPTableObject obj : list){
            if(ByteBuffer.wrap(obj.ip).getInt() >= ipValue) break;
            else index++;
        }

        if(index < list.size()){
            if(Arrays.equals(list.get(index).ip,object.ip)){
                list.set(index,object);
                fireTableRowsUpdated(index,index);
            }
            else {
                list.add(index, object);
                fireTableRowsInserted(index, index);
            }

        }

        else {
            list.add(object);
            fireTableRowsInserted(index,index);
        }

    }

    public void clear(){
        int size = list.size();
        list.clear();
        if(size > 0) fireTableRowsDeleted(0,size-1);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        ARPTableObject row = list.get(rowIndex);
        String returnValue;

        switch (columnIndex){
            case 0 : returnValue = Packet.ipToString(row.ip); break;
            case 1 : returnValue = Packet.macToString(row.mac); break;
            case 2 : returnValue = row.desc; break;
            default: returnValue = ""; break;
        }

        return returnValue;

    }

}
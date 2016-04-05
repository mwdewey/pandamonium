package Table;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

public class MainTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Ip", "Port", "Sent", "Received", "pSent", "pReceived", "Host", "Ping"};
    public List<TableObject> list;

    public MainTableModel() {
        list = new LinkedList<>();
    }

    public void addElement(TableObject object) {
        // Adds the element in the last position in the list
        list.add(object);
        fireTableRowsInserted(list.size()-1, list.size()-1);
    }

    public void clear(){
        int size = list.size();
        list.clear();
        fireTableRowsDeleted(0,size-1);
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
        TableObject row = list.get(rowIndex);
        String returnValue;

        switch (columnIndex){
            case 0 : returnValue = row.ip; break;
            case 1 : returnValue = String.valueOf(row.port); break;
            case 2 : returnValue = String.valueOf(row.sent); break;
            case 3 : returnValue = String.valueOf(row.received); break;
            case 4 : returnValue = String.valueOf(row.pSent); break;
            case 5 : returnValue = String.valueOf(row.pReceived); break;
            case 6 : returnValue = row.host; break;
            case 7 : returnValue = String.valueOf(row.ping); break;
            default: returnValue = ""; break;
        }

        return returnValue;

    }

}
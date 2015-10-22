package MetroComponents;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class MetroTable extends JTable {

    public MetroTable(TableModel model){
        super(model);

        this.setFocusable(false);
        this.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 15));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setTableHeader(new MetroTableHeader(this.getColumnModel()));
        this.setGridColor(MetroColors.LIGHT_GRAY);
        this.setShowGrid(true);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setRowHeight(20);
    }

    public boolean isCellEditable(int row, int column) { return false; }

}

class MetroTableHeader extends JTableHeader {

    public MetroTableHeader(TableColumnModel model){
        super(model);

        this.setFocusable(false);
        this.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 14));
        this.setBackground(MetroColors.DARKER_GRAY);
        this.setBorder(new MatteBorder(1,1,1,1, MetroColors.DARKER_GRAY));
        this.setReorderingAllowed(false);
        this.setResizingAllowed(true);
        this.setForeground(MetroColors.SPECIAL_GREEN);

    }
}

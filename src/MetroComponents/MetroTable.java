package MetroComponents;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MetroTable extends JTable {
    public List<Color> colors;
    public List<Color> textColors;

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

        colors = new ArrayList<>();
        textColors = new ArrayList<>();

        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Color backgroundColor;
                Color foregroundColor;


                if(row < colors.size()) {
                    backgroundColor = colors.get(row);
                    foregroundColor = textColors.get(row);
                }

                else {
                    backgroundColor = MetroColors.DARK_GRAY;
                    foregroundColor = MetroColors.SPECIAL_TEXT;
                }

                if(isSelected) {
                    backgroundColor = MetroColors.SPECIAL_GREEN;
                    foregroundColor = Color.BLACK;
                }


                if(row % 2 == 0){
                    backgroundColor = backgroundColor.darker();
                }

                c.setBackground(backgroundColor);
                c.setForeground(foregroundColor);

                return c;
            }
        });
    }

    public void setColorAtIndex(Color c,Color c2, int index){
        if(index < colors.size()){
            colors.set(index,c);
            textColors.set(index,c2);
            ((DefaultTableModel)this.getModel()).fireTableRowsUpdated(index,index);
        }
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


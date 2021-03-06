package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroMenu extends JMenu {

    public MetroMenu(String text){
        super(text);

        UIManager.put("Menu.background", MetroColors.DARKER_GRAY);
        UIManager.put("Menu.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("Menu.selectionBackground", MetroColors.SPECIAL_GREEN);
        UIManager.put("Menu.selectionForeground", Color.BLACK);
        SwingUtilities.updateComponentTreeUI(this);

        this.setOpaque(true);
        this.setFocusable(false);
        this.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.getPopupMenu().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setHorizontalAlignment(JTextField.LEFT);
        this.setRolloverEnabled(true);
    }


}

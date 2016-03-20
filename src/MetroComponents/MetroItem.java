package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroItem extends JMenuItem {
    Color hoverBackgroundColor = new Color(10,194,22);
    Color pressedBackgroundColor = new Color(9,170,20);
    Color backgroundColor = new Color(255, 255, 255);

    public MetroItem(String text){
        super(text);

        UIManager.put("MenuItem.background", MetroColors.DARKER_GRAY);
        UIManager.put("MenuItem.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("MenuItem.selectionBackground", MetroColors.SPECIAL_GREEN);
        UIManager.put("MenuItem.selectionForeground", Color.BLACK);
        SwingUtilities.updateComponentTreeUI(this);

        this.setFocusable(false);
        this.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setHorizontalAlignment(JTextField.LEFT);
        this.setRolloverEnabled(true);
    }
}

package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroMenuBar extends JMenuBar {

    Color backgroundColor = new Color(200,200,200);

    public MetroMenuBar(){
        super();

        UIManager.put("MenuBar.background", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.foreground", MetroColors.SPECIAL_TEXT);
        UIManager.put("MenuBar.borderColor", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.darkShadow", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.shadow", MetroColors.DARKER_GRAY);
        UIManager.put("MenuBar.highlight", MetroColors.DARKER_GRAY);
        SwingUtilities.updateComponentTreeUI(this);

        setFocusable(false);
        setFont(new Font("Segoe UI Semilight", Font.BOLD, 25));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }


}

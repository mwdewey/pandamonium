package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroMenuBar extends JMenuBar {

    Color backgroundColor = new Color(200,200,200);

    public MetroMenuBar(){
        super();

        setFocusable(false);
        setFont(new Font("Segoe UI Semilight", Font.BOLD, 25));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }


}

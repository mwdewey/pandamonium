package MetroComponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mikes Gaming on 5/16/2015.
 */
public class MetroTextField extends JTextField {
    
    public MetroTextField(){
        super();

        setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.WHITE));
        setFont(new Font("Segoe UI Semilight",Font.PLAIN,15));
        setBackground(Color.WHITE);

    }
    
}

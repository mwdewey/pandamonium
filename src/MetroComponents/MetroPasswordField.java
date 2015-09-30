package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroPasswordField extends JPasswordField {

    public MetroPasswordField(){
        super();

        setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.WHITE));
        setFont(new Font("Segoe UI Semilight",Font.BOLD,15));
        setBackground(Color.WHITE);
        setEchoChar('•');

    }
    
}

package MetroComponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mike on 6/3/2015.
 */
public class MetroLabel extends JLabel {

    public MetroLabel(String text){
        super(text);

        setFocusable(false);
        setFont(new Font("Segoe UI Semilight",Font.PLAIN,15));
        setForeground(new Color(160,160,160));
    }

}

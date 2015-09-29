package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroScrollPane extends JScrollPane {

    public MetroScrollPane(Component view){
        super(view);

        this.getViewport().setBackground(new Color(30,30,30));
        JViewport jViewport = new JViewport();
        jViewport.setBackground(new Color(30,30,30));
        this.setColumnHeader(jViewport);

        this.setVerticalScrollBar(new MetroScrollBar(Adjustable.VERTICAL));
        this.setHorizontalScrollBar(new MetroScrollBar(Adjustable.HORIZONTAL));

        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30,30,30));
        this.setCorner(JScrollPane.UPPER_TRAILING_CORNER, panel);


    }

}

package MetroComponents;

import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;

public class MetroScrollBar extends JScrollBar {

    public MetroScrollBar(int orientation) {

        super(orientation);

        this.setUI(new CustomUI());


    }

}

class CustomUI extends MetalScrollBarUI {
    private JButton b = new JButton() {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }

    };

    CustomUI() {
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        g.setColor(new Color(29,185,84));
        g.fillRect(thumbRect.x, thumbRect.y, r.width, r.height);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.fillRect(0, 0, r.width, r.height);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return b;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return b;
    }

}

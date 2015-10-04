package MetroComponents;

import javax.net.ssl.HostnameVerifier;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MetroButton extends JButton{
    enum STATE{
        OFF,
        HOVER,
        PRESSED
    }

    Color pressedBackgroundColor = new Color(29,185,84);
    Color hoverBackgroundColor = new Color(3, 170, 60);
    Color backgroundColor = new Color(30,30,30);

    Color darkGray = new Color(40,40,40);
    Color darkerGray = new Color(30,30,30);
    Color specialGreen = new Color(29,185,84);
    Color specialGreenDark = new Color(3, 170, 60);
    Color specialText = new Color(190,190,190);

    STATE state = STATE.OFF;
    STATE prevState = STATE.OFF;

    public MetroButton(String text){
        super(text);
        super.setContentAreaFilled(false);

        this.setSelected(false);
        this.setBackground(darkerGray);
        this.setFocusable(false);
        this.setForeground(specialGreen);
        this.setFont(new Font("Segoe UI Semilight",Font.PLAIN,15));
        this.setBorder(new MatteBorder(2, 2, 2, 2, specialGreen));
        this.setHorizontalAlignment(JTextField.CENTER);

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) state = STATE.PRESSED;
        else if (getModel().isRollover()) state = STATE.HOVER;
        else state = STATE.OFF;

        if(state != prevState){
            if(state==STATE.OFF) this.setForeground(specialGreen);
            else this.setForeground(Color.BLACK);

            if(state==STATE.PRESSED) this.setBorder(new MatteBorder(2, 2, 2, 2, pressedBackgroundColor));
            else if(state==STATE.HOVER) this.setBorder(new MatteBorder(2, 2, 2, 2, hoverBackgroundColor));
            else this.setBorder(new MatteBorder(2, 2, 2, 2, specialGreen));
        }
        prevState = state;

        if (state == STATE.PRESSED) g.setColor(pressedBackgroundColor);
        else if (state == STATE.HOVER) g.setColor(hoverBackgroundColor);
        else g.setColor(backgroundColor);

        g.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {}

}

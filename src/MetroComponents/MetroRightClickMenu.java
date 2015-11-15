package MetroComponents;

import javax.swing.*;
import java.awt.*;

public class MetroRightClickMenu extends JPopupMenu{

    public MetroRightClickMenu(){

        super();

        this.setOpaque(true);
        this.setFocusable(false);
        this.setFont(new Font("Segoe UI Light", Font.PLAIN, 15));
        this.setBorder(BorderFactory.createLineBorder(MetroColors.SPECIAL_GREEN,5));


    }

    public void showPopup(Component invoker, int x, int y){
        show(invoker,x,y);
    }


}

package app.ui.frame;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {
    private int widthMargin = 50;
    private int heightMargin = 50;

    public BaseFrame() {
        super();
        setResizable(true);
    }

    protected void setBaseSize() {
        pack();
        setSize(getWidth() + widthMargin, getHeight() + heightMargin);
    }

    protected void setLocationCenter() {
        Dimension frameSize = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

}

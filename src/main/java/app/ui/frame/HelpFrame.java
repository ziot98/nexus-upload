package app.ui.frame;

import app.ui.contents.HelpContent;
import app.ui.contents.OptionContent;
import app.ui.creator.GridBagCreator;

import javax.swing.*;
import java.awt.*;

public class HelpFrame extends BaseFrame {
    private int lineNum = 0;

    public HelpFrame() {
        super();
        
        setTitle("Help");

        JPanel helpPanel = new JPanel();
        GridBagCreator gridBagCreator = new GridBagCreator(helpPanel);
        new HelpContent(gridBagCreator, lineNum);

        add(helpPanel);

        setBaseSize();
        setSize(new Dimension(700, 600));
        setLocationCenter();

        setVisible(true);
    }
}

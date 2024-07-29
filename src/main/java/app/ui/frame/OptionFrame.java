package app.ui.frame;

import app.ui.contents.OptionContent;
import app.ui.creator.GridBagCreator;

import javax.swing.*;
import java.awt.*;

public class OptionFrame extends BaseFrame {
    private int lineNum = 0;

    public OptionFrame() {
        super();
        
        setTitle("Option");

        JPanel optionPanel = new JPanel();
        GridBagCreator gridBagCreator = new GridBagCreator(optionPanel);
        new OptionContent(gridBagCreator, lineNum);

        add(optionPanel);

        setBaseSize();
        setSize(new Dimension(700, 210));
        setLocationCenter();

        setVisible(true);
    }
}

package app.ui.menuBar;

import app.ui.frame.HelpFrame;
import app.ui.frame.OptionFrame;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {
    public MainMenuBar() {
        JMenu optionMenu = new JMenu("Settings");
        JMenuItem optionMenuItem = new JMenuItem("Options");
        JMenuItem helpMenuItem = new JMenuItem("Help");

        optionMenuItem.addActionListener((e -> new OptionFrame()));
        helpMenuItem.addActionListener(e -> new HelpFrame());

        optionMenu.add(optionMenuItem);
        optionMenu.add(helpMenuItem);

        add(optionMenu);
    }
}

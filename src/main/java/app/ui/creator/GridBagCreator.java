package app.ui.creator;

import javax.swing.*;
import java.awt.*;

public class GridBagCreator {
    private GridBagLayout gLayout;
    private GridBagConstraints gConstraints;
    private JPanel gridPanel;


    public GridBagCreator(JPanel jPanel) {
        this.gLayout = new GridBagLayout();
        this.gConstraints = new GridBagConstraints();
        this.gridPanel = jPanel;
        gridPanel.setLayout(gLayout);
    }


    public void insertGrid(Component component, int x, int y, int w, int h, double wx, double wy,
                            int anchor, int fill, Insets insets) {
        gConstraints.gridx = x;
        gConstraints.gridy = y;
        gConstraints.anchor = anchor;
        gConstraints.fill = fill;
        gConstraints.gridwidth = w;
        gConstraints.gridheight = h;
        gConstraints.weightx = wx;
        gConstraints.weighty = wy;
        gConstraints.insets = insets;
        gridPanel.add(component, gConstraints);
        gLayout.setConstraints(component, gConstraints);
    }


    public void insertGrid(Component component, int x, int y, int w, int h, double wx, double wy,
                            Insets insets) {
        gConstraints.gridx = x;
        gConstraints.gridy = y;
        gConstraints.anchor = (x == 0) ? GridBagConstraints.EAST : GridBagConstraints.WEST;
        gConstraints.fill = (x == 0) ? GridBagConstraints.NONE : GridBagConstraints.BOTH;
        gConstraints.gridwidth = w;
        gConstraints.gridheight = h;
        gConstraints.weightx = wx;
        gConstraints.weighty = wy;
        gConstraints.insets = insets;
        gridPanel.add(component, gConstraints);
        gLayout.setConstraints(component, gConstraints);
    }

    public GridBagLayout getLayout() {
        return gLayout;
    }

    public void setLayout(GridBagLayout gLayout) {
        this.gLayout = gLayout;
    }

}

package app.ui.contents;

import app.ui.creator.GridBagCreator;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ResultAreaContent extends Content {
    private JTextArea resultTextArea;

    public ResultAreaContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        resultTextArea = createTextArea(0, 4, 1, 15, 50, 1, weightY2);
        addMenuButton();
    }

    private void addMenuButton() {
        JPopupMenu pMenu = new JPopupMenu();
        JMenuItem clearItem = new JMenuItem("Clear");
        JCheckBoxMenuItem autoScrollItem = new JCheckBoxMenuItem("AutoScroll");
        pMenu.add(clearItem);
        pMenu.add(autoScrollItem);

        clearItem.addActionListener(e -> resultTextArea.setText(null));
        autoScrollItem.addItemListener(e -> {
            option.setAutoScroll(e.getStateChange() == ItemEvent.SELECTED);
        });

        autoScrollItem.setSelected(option.isAutoScroll());


        resultTextArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    pMenu.show(resultTextArea, e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    public JTextArea getResultTextArea() {
        return resultTextArea;
    }
}

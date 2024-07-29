package app.ui.component;

import app.entity.UploadOption;

import javax.swing.*;

public class AutoScrollJTextArea extends JTextArea {
    public AutoScrollJTextArea(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public void append(String str) {
        super.append(str);
        if (UploadOption.getInstance().isAutoScroll()) {
            int endLength = getDocument().getLength();
            select(endLength, endLength);
        }
    }
}

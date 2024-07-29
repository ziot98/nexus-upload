package app.ui.contents;

import app.entity.UploadOption;
import app.ui.component.AutoScrollJTextArea;
import app.ui.component.JTooltipTable;
import app.ui.creator.GridBagCreator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class Content {
    protected GridBagCreator gridBagCreator;
    protected int lineNum;
    protected UploadOption option;
    protected double weightX1 = 0.05;
    protected double weightX2 = 0.95;
    protected double weightY1 = 0.037;
    protected double weightY2 = 0.11;


    public Content(GridBagCreator gridBagCreator, int lineNum) {
        this.gridBagCreator = gridBagCreator;
        this.lineNum = lineNum;
        this.option = UploadOption.getInstance();
        createContents();
    }

    protected JLabel createLabel(String labelString, int colNum, int width, int height, double weightX, double weightY) {
        JLabel label = new JLabel(labelString);
        gridBagCreator.insertGrid(label, colNum, lineNum, width, height, weightX, weightY, new Insets(2, 2, 2, 2));
        return label;
    }

    protected JTextField createInputBox(String defaultString, int colNum, int width, int height, int anchor, int fill, double weightX, double weightY) {
        JTextField textField = new JTextField(defaultString);

        gridBagCreator.insertGrid(textField, colNum, lineNum, width, height, weightX, weightY, anchor, fill, new Insets(2, 2, 2, 2));
        return textField;
    }

    protected JTextArea createTextArea(int colNum, int width, int height, int rows, int columns, double weightX, double weightY) {
        JTextArea textArea = new AutoScrollJTextArea(rows, columns);
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JScrollPane resultScrollPane = new JScrollPane(textArea);
        resultScrollPane.setBackground(Color.blue);

        gridBagCreator.insertGrid(resultScrollPane, colNum, lineNum, width, height, weightX, weightY,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2));

        return textArea;
    }

    protected JRadioButton[] createRadioButtons(String[] buttonStrings, int colNum, int width, int height, double weightX, double weightY) {
        JRadioButton radioButtons[] = new JRadioButton[buttonStrings.length];
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new JRadioButton(buttonStrings[i]);
            buttonGroup.add(radioButtons[i]);
            boxPanel.add(radioButtons[i]);
        }

        gridBagCreator.insertGrid(boxPanel, colNum, lineNum, width, height, weightX, weightY, new Insets(2, 2, 2, 2));

        return radioButtons;
    }

    protected JButton createButton(String buttonString, int colNum, int width, int height, double weightX, double weightY, int anchor, int fill) {
        JButton button = new JButton(buttonString);
        gridBagCreator.insertGrid(button, colNum, lineNum, width, height, weightX, weightY, anchor, fill, new Insets(2, 2, 2, 2));

        return button;
    }

    protected JTable createTable(String[] columnNames, int colNum, int width, int height, int tableWidth, int tableHeight, double weightX, double weightY) {

        DefaultTableModel model = null;
        model = new DefaultTableModel(null, columnNames);

        JTooltipTable fileListTable = new JTooltipTable(model);

        JScrollPane headerScrollPane = new JScrollPane(fileListTable);
        headerScrollPane.setPreferredSize(new Dimension(tableWidth, tableHeight));

        gridBagCreator.insertGrid(headerScrollPane, colNum, lineNum, width, height, weightX, weightY,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2));

        return fileListTable;
    }


    public abstract void createContents();
}

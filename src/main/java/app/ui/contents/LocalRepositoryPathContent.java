package app.ui.contents;

import app.ui.creator.GridBagCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class LocalRepositoryPathContent extends Content {

    public LocalRepositoryPathContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        JLabel jLabel = createLabel("Local Repository Path :", 0, 1, 1, 0.1, 0);
        //jLabel.setMaximumSize(new Dimension(jLabel.getWidth(), jLabel.getHeight()));

        JTextField inputTextField = createInputBox(option.getLocalRepositoryPath(), 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 0.8, 0);
        //inputTextField.setMaximumSize(new Dimension(inputTextField.getWidth(), inputTextField.getHeight()));

        JButton browseButton = createButton("Browse", 3, 1, 1, 0.1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL);
        //browseButton.setMaximumSize(new Dimension(browseButton.getWidth(), browseButton.getHeight()));

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setLocalRepositoryPath(inputTextField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setLocalRepositoryPath(inputTextField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setLocalRepositoryPath(inputTextField.getText());
            }
        };

        inputTextField.getDocument().addDocumentListener(dl);

        browseButton.addActionListener(e -> {
            JFileChooser jc = new JFileChooser();
            jc.setFileHidingEnabled(false);
            jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rVal = jc.showOpenDialog(browseButton);

            if (rVal == JFileChooser.APPROVE_OPTION) {
                inputTextField.setText(jc.getCurrentDirectory().toString() + File.separator +  jc.getSelectedFile().getName());
            }
        });
    }
}

package app.ui.contents;

import app.ui.creator.GridBagCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class OptionContent extends Content {
    public OptionContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        createUploadMode();

        lineNum++;
        createRepoNameContent();

        lineNum++;
        createRepoUrl();

        lineNum++;
        createIsForceUpload();

        lineNum++;
        createId();

        lineNum++;
        createPassword();

        lineNum++;
        createMavenPathContent();

        lineNum++;
        createMavenConfigPathContent();

        //lineNum++;
        //createClassifierContent();
    }

    private void createUploadMode() {
        createLabel("Upload Mode :", 0, 1, 1, 0.1, weightY1);
        JRadioButton[] uploadModeButtons = createRadioButtons(new String[]{"Http Post", "Maven"}, 1, 2, 1, weightX2, 0);

        uploadModeButtons[0].addActionListener((e -> option.setHttpUpload(true)));
        uploadModeButtons[1].addActionListener((e -> option.setHttpUpload(false)));

        if (option.isHttpUpload()) {
            uploadModeButtons[0].setSelected(true);
        } else {
            uploadModeButtons[1].setSelected(true);
        }

    }

    private void createIsForceUpload() {
        createLabel("Force upload :", 0, 1, 1, 0.1, weightY1);
        JRadioButton[] isForceSelectButtons = createRadioButtons(new String[]{"True", "False"}, 1, 2, 1, weightX2, 0);

        isForceSelectButtons[0].addActionListener((e -> option.setForceUpload(true)));
        isForceSelectButtons[1].addActionListener((e -> option.setForceUpload(false)));

        if (option.isForceUpload()) {
            isForceSelectButtons[0].setSelected(true);
        } else {
            isForceSelectButtons[1].setSelected(true);
        }

    }

    private void createPassword() {
        createLabel("Nexus Password : ", 0, 1,1, 0.1, weightY1);
        JTextField passwordField = createInputBox(option.getPassword(), 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.9, weightY1);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setPassword(passwordField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setPassword(passwordField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setPassword(passwordField.getText());
            }
        };

        passwordField.getDocument().addDocumentListener(dl);

    }

    private void createId() {
        createLabel("Nexus ID : ", 0, 1,1, 0.1, weightY1);
        JTextField idField = createInputBox(option.getId(), 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.9, weightY1);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setId(idField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setId(idField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setId(idField.getText());
            }
        };

        idField.getDocument().addDocumentListener(dl);

    }

    private void createRepoUrl() {
        createLabel("Repository URL : ", 0, 1,1, 0.1, weightY1);
        JTextField repoUrlField = createInputBox(option.getRepositoryURL(), 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.9, weightY1);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setRepositoryURL(repoUrlField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setRepositoryURL(repoUrlField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setRepositoryURL(repoUrlField.getText());
            }
        };

        repoUrlField.getDocument().addDocumentListener(dl);
    }

    private void createClassifierContent() {
        createLabel("Classifiers : ", 0, 1,1, 0.1, weightY1);
        JTextField classifiersField = createInputBox(option.getClassifiers(), 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.9, weightY1);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setClassifiers(classifiersField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setClassifiers(classifiersField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setClassifiers(classifiersField.getText());
            }
        };

        classifiersField.getDocument().addDocumentListener(dl);
    }

    private void createRepoNameContent() {
        createLabel("Repository Name : ", 0, 1,1, 0.1, weightY1);
        JTextField repoNameField = createInputBox(option.getRepositoryName(), 1, 3, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.9, weightY1);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setRepositoryName(repoNameField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setRepositoryName(repoNameField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setRepositoryName(repoNameField.getText());
            }
        };

        repoNameField.getDocument().addDocumentListener(dl);
    }

    private void createMavenPathContent() {
        createLabel("Maven Path :", 0, 1, 1, 0.1, weightY1);
        JTextField inputTextField = createInputBox(option.getMavenPath(), 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.8, weightY1);
        JButton browseButton = createButton("Browse", 3, 1, 1, 0.1, weightY1, GridBagConstraints.EAST, GridBagConstraints.NONE);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setMavenPath(inputTextField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setMavenPath(inputTextField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setMavenPath(inputTextField.getText());
            }
        };

        inputTextField.getDocument().addDocumentListener(dl);

        browseButton.addActionListener(e -> {
            JFileChooser jc = new JFileChooser();
            jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int rVal = jc.showOpenDialog(browseButton);

            if (rVal == JFileChooser.APPROVE_OPTION) {
                inputTextField.setText(jc.getCurrentDirectory().toString() + File.separator +  jc.getSelectedFile().getName());
            }
        });
    }

    private void createMavenConfigPathContent() {
        createLabel("Maven Config Path :", 0, 1, 1, 0.1, weightY1);
        JTextField inputTextField = createInputBox(option.getMavenConfigPath(), 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.8, weightY1);
        JButton browseButton = createButton("Browse", 3, 1, 1, 0.1, weightY1, GridBagConstraints.EAST, GridBagConstraints.NONE);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { option.setMavenConfigPath(inputTextField.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) {
                option.setMavenConfigPath(inputTextField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                option.setMavenConfigPath(inputTextField.getText());
            }
        };

        inputTextField.getDocument().addDocumentListener(dl);

        browseButton.addActionListener(e -> {
            JFileChooser jc = new JFileChooser();
            jc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int rVal = jc.showOpenDialog(browseButton);

            if (rVal == JFileChooser.APPROVE_OPTION) {
                inputTextField.setText(jc.getCurrentDirectory().toString() + File.separator +  jc.getSelectedFile().getName());
            }
        });
    }
}

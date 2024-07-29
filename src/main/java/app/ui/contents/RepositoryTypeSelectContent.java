package app.ui.contents;

import app.ui.creator.GridBagCreator;

import javax.swing.*;

public class RepositoryTypeSelectContent extends Content {
    public RepositoryTypeSelectContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        createLabel("Repository Type :", 0, 1, 1, weightX1, 0);
        JRadioButton[] repoTypeSelectButtons = createRadioButtons(new String[]{"SnapShot", "Release"}, 1, 2, 1, weightX2, 0);

        repoTypeSelectButtons[0].addActionListener((e -> option.setSnapshot(true)));
        repoTypeSelectButtons[1].addActionListener((e -> option.setSnapshot(false)));

        if (option.isSnapshot()) {
            repoTypeSelectButtons[0].setSelected(true);
        } else {
            repoTypeSelectButtons[1].setSelected(true);
        }
    }
}

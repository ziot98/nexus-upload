package app.ui.frame;

import app.ui.contents.*;
import app.ui.creator.GridBagCreator;
import app.ui.menuBar.MainMenuBar;

import javax.swing.*;

public class MainFrame extends BaseFrame {
    private JPanel mainPanel;
    private GridBagCreator gridBagCreator;
    private int lineNum = 0;

    public MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Nexus Uploader");

        mainPanel = new JPanel();
        gridBagCreator = new GridBagCreator(mainPanel);

        setJMenuBar(new MainMenuBar());
        new LocalRepositoryPathContent(gridBagCreator, lineNum);

        lineNum++;
        new RepositoryTypeSelectContent(gridBagCreator, lineNum);

        lineNum++;
        GetButtonContent getButtonContent = new GetButtonContent(gridBagCreator, lineNum);

        lineNum++;
        FileListContent fileListContent = new FileListContent(gridBagCreator, lineNum);

        lineNum++;
        ResultAreaContent resultAreaContent = new ResultAreaContent(gridBagCreator, lineNum);

        lineNum++;
        UploadButtonContent uploadButtonContent = new UploadButtonContent(gridBagCreator, lineNum);

        getButtonContent.setFileListContent(fileListContent);
        uploadButtonContent.setFileListContent(fileListContent);
        uploadButtonContent.setResultAreaContent(resultAreaContent);

        add(mainPanel);


        setBaseSize();
        setLocationCenter();

        fileListContent.resizeTableColumn(getWidth());

        setVisible(true);
    }
}

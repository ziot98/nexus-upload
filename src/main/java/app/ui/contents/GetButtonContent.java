package app.ui.contents;

import app.provider.RepoFileInfoProvider;
import app.ui.creator.GridBagCreator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GetButtonContent extends Content {
    private FileListContent fileListContent;
    public GetButtonContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }
    private Thread processRunThread;
    private Timer timer;
    private String buttonText = "Get Files";
    private boolean isRunning = false;

    public void setFileListContent(FileListContent fileListContent) {
        this.fileListContent = fileListContent;
    }

    @Override
    public void createContents() {
        JButton selectButton = createButton("Get Files", 1, 1, 1, 0.3, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH);
        selectButton.addActionListener((e -> {
            if (isRunning) {
                processRunThread.interrupt();
                timer.cancel();
                isRunning = false;
                buttonText = "Canceled. Retry";
                selectButton.setText(buttonText);

            } else {
                isRunning = true;
                timer = getTimer(selectButton);

                processRunThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        //clear
                        JTable jTable = fileListContent.getFileListTable();
                        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                        model.setRowCount(0);

                        RepoFileInfoProvider repoFileInfoProvider = RepoFileInfoProvider.getInstance();
                        try {
                            buttonText = "Analyzing.. " + "(0s)";
                            selectButton.setText(buttonText);
                            repoFileInfoProvider.createRepoFileInfos(option.getLocalRepositoryPath());
                            fileListContent.addFileRows(repoFileInfoProvider.getMavenCompMap());
                        } catch (Exception ex) {
                        	timer.cancel();
                        	buttonText = buttonText.replace("Analyzing..", "Done.");
                            selectButton.setText(buttonText);
                            isRunning = false;
                            JOptionPane.showMessageDialog(null, "Analyzing failed. Check Error Log! \n Message : " + ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        timer.cancel();

                        buttonText = buttonText.replace("Analyzing..", "Done.");
                        selectButton.setText(buttonText);

                        JOptionPane.showMessageDialog(null, fileListContent.getRowCount()+ " item is analyzed. "
                        		+ repoFileInfoProvider.getFailCount() + " is failed.", "info", JOptionPane.INFORMATION_MESSAGE);
                        isRunning = false;
                    }
                });


                processRunThread.start();
            }
        }));
    }

    private Timer getTimer(JButton selectButton) {
        TimerTask task = new TimerTask() {
            private int seconds = 1;
            public void run() {
                buttonText = "Analyzing.. " + "(" + seconds + "s)";
                selectButton.setText(buttonText);
                seconds++;
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 1000;
        long period = 1000;
        timer.scheduleAtFixedRate(task, delay, period);
        return timer;
    }
}

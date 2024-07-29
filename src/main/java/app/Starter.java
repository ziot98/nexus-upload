package app;

import app.executor.HttpPostUploadExecutor;
import app.executor.RedirectProcessExecutor;
import app.provider.RepoFileInfoProvider;
import app.entity.UploadOption;
import app.ui.frame.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Starter {
    private final static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> init());
    }

    private static void init() {
        logger.info("start nexus uploader..");

        UploadOption uploadOption = new UploadOption();
        RepoFileInfoProvider repoFileInfoProvider = new RepoFileInfoProvider();
        RedirectProcessExecutor redirectProcessExecutor = new RedirectProcessExecutor();
        HttpPostUploadExecutor httpPostUploadExecutor = new HttpPostUploadExecutor();
        MainFrame mainFrame = new MainFrame();
    }
}

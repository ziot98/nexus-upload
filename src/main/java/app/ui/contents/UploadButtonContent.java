package app.ui.contents;

import app.entity.Artifact;
import app.entity.MavenComp;
import app.entity.UploadOption;
import app.executor.HttpPostUploadExecutor;
import app.executor.RedirectProcessExecutor;
import app.provider.RepoFileInfoProvider;
import app.ui.creator.GridBagCreator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessResult;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class UploadButtonContent extends Content {
    public UploadButtonContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    private FileListContent fileListContent;
    private ResultAreaContent resultAreaContent;
    private RedirectProcessExecutor redirectProcessExecutor = RedirectProcessExecutor.getInstance();
    private UploadOption option = UploadOption.getInstance();
    private String buttonText = "Upload";
    private Thread processRunThread;
    private Timer timer;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isRunning = false;
    private int successCnt = 0;
    private int failCnt = 0;


    @Override
    public void createContents() {
        JButton uploadButton = createButton("Upload", 0, 4, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        uploadButton.addActionListener((e -> {
            if (fileListContent.checkEmpty()){
                JOptionPane.showMessageDialog(null, "Empty Cell Detected. GroupId, ArtifactId, Version Must Not Be Empty.", "error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (isRunning) {
                    processRunThread.interrupt();
                    timer.cancel();
                    isRunning = false;
                    buttonText = "Canceled. Retry";
                    uploadButton.setText(buttonText);

                } else {
                    isRunning = true;
                    successCnt = 0;
                    failCnt = 0;
                    timer = getTimer(uploadButton);
                    Map<String, Artifact> artifactMap = RepoFileInfoProvider.getInstance().createArtifactInfo(fileListContent.getFileListTable());

                    processRunThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                buttonText = "Analyzing.. " + "(0s)";
                                uploadButton.setText(buttonText);
                                if (option.isHttpUpload()) {
                                    httpUpload(artifactMap);
                                } else {
                                    mavenUpload(artifactMap);
                                }
                            } catch (Exception ex) {
                                logger.error("upload Error", ex);
                                timer.cancel();
                                buttonText = buttonText.replace("Uploading..", "Failed.");
                                uploadButton.setText(buttonText);
                                isRunning = false;
                                JOptionPane.showMessageDialog(null, "Upload failed. Check Error Log! \n Message : " + ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            timer.cancel();

                            buttonText = buttonText.replace("Uploading..", "Complete.");

                            uploadButton.setText(buttonText);

                            JOptionPane.showMessageDialog(null, successCnt + " Uploaded. "
                                    + failCnt + " is failed.", "info", JOptionPane.INFORMATION_MESSAGE);
                            isRunning = false;
                        }
                    });


                    processRunThread.start();
                }
            }

        }));
    }

    public void setFileListContent(FileListContent fileListContent) {
        this.fileListContent = fileListContent;
    }

    public void setResultAreaContent(ResultAreaContent resultAreaContent) {
        this.resultAreaContent = resultAreaContent;
    }
    private void httpUpload(Map<String, Artifact> artifactMap) throws InterruptedException {
        checkHttpOption();

        String URL = String.format("%s/service/rest/v1/components?repository=%s", option.getRepositoryURL(), option.getRepositoryName());

        JTextArea resultTextArea = resultAreaContent.getResultTextArea();
        resultTextArea.setText(null);

        resultTextArea.append("**** Start Upload. Upload Mode is HTTP POST. ****\n");
        resultTextArea.append(String.format("[info] Is Force Upload : %s \n", String.valueOf(option.isForceUpload())));
        resultTextArea.append(String.format("[info] URL : %s\n", URL));


        for (Map.Entry<String, Artifact> entry : artifactMap.entrySet()) {
            Artifact value = entry.getValue();
            String groupId = value.getGroupId();
            String artifactId = value.getArtifactId();
            String version = value.getVersion();
            List<MavenComp> mavenComps = value.getMavenComps();

            resultTextArea.append("*** Try HTTP Post Upload Artifact. ***\n");
            resultTextArea.append(String.format("[info] Artifact Information : \n%s", value));


            Map<String,String> formDataMap = getBaseFormData(groupId, artifactId, version);

            int assetNum = 1;

            for (MavenComp mavenComp : mavenComps) {
                boolean isPass = false;
                String extension = FilenameUtils.getExtension(mavenComp.getPath());
                if (!option.isForceUpload()) {
                    isPass = checkIsAlreadyExist(groupId, artifactId, version, mavenComp.getClassifier(), extension);
                }

                if (!isPass) {
                    formDataMap.put("maven2.asset" + assetNum + "-File", mavenComp.getPath());
                    formDataMap.put("maven2.asset" + assetNum + ".extension", extension);
                    if (!mavenComp.getClassifier().equals("")) formDataMap.put("maven2.asset" + assetNum + ".classifier", mavenComp.getClassifier());
                    assetNum++;
                    resultTextArea.append(String.format("[info] %s Will Be Uploaded.\n",
                            getMavenFileName(artifactId, version, mavenComp.getClassifier(), extension)));
                } else {
                    resultTextArea.append(String.format("[info] %s Is Already Exist. Pass.\n",
                            getMavenFileName(artifactId, version, mavenComp.getClassifier(), extension)));
                }
            }


            if (value.getPomFile() != null) {
                boolean isPass = false;

                if (!option.isForceUpload()) {
                    isPass = checkIsAlreadyExist(groupId, artifactId, version, "", "pom");
                }

                if (!isPass) {
                    formDataMap.put("maven2.asset" + assetNum + "-File", value.getPomFile());
                    formDataMap.put("maven2.asset" + assetNum + ".extension", "pom");
                    assetNum++;
                    resultTextArea.append(String.format("[info] %s Will Be Uploaded.\n",
                            getMavenFileName(artifactId, version, "", "pom")));
                } else {
                    resultTextArea.append(String.format("[info] %s Is Already Exist. Pass.\n",
                            getMavenFileName(artifactId, version, "", "pom")));
                }
            }

            if (value.getJavadocFile() != null) {
                boolean isPass = false;

                if (!option.isForceUpload()) {
                    isPass = checkIsAlreadyExist(groupId, artifactId, version, "javadoc", "jar");
                }

                if (!isPass) {
                    formDataMap.put("maven2.asset" + assetNum + "-File", value.getJavadocFile());
                    formDataMap.put("maven2.asset" + assetNum + ".extension", "jar");
                    formDataMap.put("maven2.asset" + assetNum + ".classifier", "javadoc");
                    assetNum++;
                    resultTextArea.append(String.format("[info] %s Will Be Uploaded.\n",
                            getMavenFileName(artifactId, version, "javadoc", "jar")));
                } else {
                    resultTextArea.append(String.format("[info] %s Is Already Exist. Pass. \n",
                            getMavenFileName(artifactId, version, "javadoc", "jar")));
                }
            }

            if (value.getSourceFile() != null) {

                boolean isPass = false;

                if (!option.isForceUpload()) {
                    isPass = checkIsAlreadyExist(groupId, artifactId, version, "sources", "jar");
                }

                if (!isPass) {
                    formDataMap.put("maven2.asset" + assetNum + "-File", value.getSourceFile());
                    formDataMap.put("maven2.asset" + assetNum + ".extension", "jar");
                    formDataMap.put("maven2.asset" + assetNum + ".classifier", "sources");
                    resultTextArea.append(String.format("[info] %s Will Be Uploaded.\n",
                            getMavenFileName(artifactId, version, "sources", "jar")));
                } else {
                    resultTextArea.append(String.format("[info] %s Is Already Exist. Pass. \n",
                            getMavenFileName(artifactId, version, "sources", "jar")));

                }
            }

            boolean isFileExist = false;

            for (Map.Entry<String, String> formDataEntry : formDataMap.entrySet()) {
                String key = formDataEntry.getKey();
                if (key.contains("-File")) {
                    isFileExist = true;
                    break;
                }
            }

            if (isFileExist) {
                int result = HttpPostUploadExecutor.getInstance().httpFormPost(URL, formDataMap, resultTextArea);

                if (result < 0) {
                    failCnt++;
                    resultTextArea.append(String.format("[info] HTTP Post Artifact %s.%s-%s Failed!\n", groupId, artifactId, version));

                } else {
                    resultTextArea.append(String.format("[info] HTTP Post Artifact %s.%s-%s Success!\n", groupId, artifactId, version));
                    successCnt++;
                }
            } else {
                resultTextArea.append(String.format("[info] No File Exist In %s.%s-%s. Pass Artifact.\n", groupId, artifactId, version));
            }
        }

        resultTextArea.append("**** HTTP POST Upload Finish. ****");
    }

    private boolean checkIsAlreadyExist(String groupId, String artifactId, String version, String classifier, String extension) {
        String groupPath = groupId.replace(".", "/");
        String fileName = getMavenFileName(artifactId, version, classifier, extension);

        String url = String.format("%s/%s/%s/%s/%s/%s/%s",
                option.getRepositoryURL(), "repository", option.getRepositoryName(), groupPath, artifactId, version, fileName);

        int result = HttpPostUploadExecutor.getInstance().httpGet(url, resultAreaContent.getResultTextArea());

        if (result == 404) {
            return false;
        } else if (result == 200) {
            return true;
        } else {
            String failString = String.format("[info] CheckFail. %s / %s / %s / %s", groupId, artifactId, version, classifier);
            resultAreaContent.getResultTextArea().append(failString);
            logger.error(failString);
            return false;
        }
    }

    private String getMavenFileName(String artifactId, String version, String classifier, String extension) {
        StringBuffer s = new StringBuffer();

        s.append(artifactId);
        s.append("-");
        s.append(version);

        if (classifier.equals("")) {
            s.append(".");
            s.append(extension);
        } else {
            s.append("-");
            s.append(classifier);
            s.append(".");
            s.append(extension);
        }

        return s.toString();
    }

    private void checkHttpOption() {
        if (option.getId() == null || option.getId().equals("")) {
            throw new RuntimeException("Nexus Login ID Option Is Empty.");
        }

        if (option.getPassword() == null || option.getPassword().equals("")) {
            throw new RuntimeException("Nexus Login Password Option Is Empty.");
        }

        if (option.getRepositoryURL() == null || option.getRepositoryURL().equals("")) {
            throw new RuntimeException("Repository URL Option Is Empty.");
        }

        if (option.getRepositoryName() == null || option.getRepositoryName().equals("")) {
            throw new RuntimeException("Repository Id Option Is Empty.");
        }

        if (option.isSnapshot()) {
            throw new RuntimeException("Http Post Mode Can not Upload Snapshot Version.");
        }
    }


    private void mavenUpload(Map<String, Artifact> artifactMap) throws InterruptedException {
        checkMavenOption();

        redirectProcessExecutor.setFirst(true);

        JTextArea resultTextArea = resultAreaContent.getResultTextArea();
        String URL = String.format("%s/repository/%s/", option.getRepositoryURL(), option.getRepositoryName());

        resultTextArea.append("**** Start Upload. Upload Mode is Maven Deploy. ****");
        resultTextArea.append("[info] URL : " + URL);



        for (Map.Entry<String, Artifact> entry : artifactMap.entrySet()) {
            Artifact value = entry.getValue();
            String groupId = value.getGroupId();
            String artifactId = value.getArtifactId();
            String version = value.getVersion();
            List<MavenComp> mavenComps = value.getMavenComps();

            List<String> command = getBaseCommand(groupId, artifactId, version);
            
            if (logger.isDebugEnabled()) {
				command.add("-X");
			}

            resultTextArea.append("*** Try Maven Deploy Artifact. *** \n");
            resultTextArea.append("[info] Artifact Information : \n");
            resultTextArea.append(value.toString());

            if (mavenComps.size() == 0 ) {
                if (value.getPomFile() != null) command.add("-Dfile=" + value.getPomFile());
            } else if (mavenComps.size() == 1) {
                String classifier = mavenComps.get(0).getClassifier();
                command.add("-Dfile=" + mavenComps.get(0).getPath());
                if (!classifier.equals("")) command.add("-Dclassifier=" + classifier);
            } else {
                StringJoiner fileJoiner = new StringJoiner(",");
                StringJoiner classifierJoiner = new StringJoiner(",");
                StringJoiner typeJoiner = new StringJoiner(",");

                int noClassifierIndex = -1;

                for (int i = 0; i < mavenComps.size(); i++) {
                    if (mavenComps.get(i).getClassifier().equals("")) noClassifierIndex = i;
                }

                if (noClassifierIndex < 0) {
                    command.add("-Dfile=" + mavenComps.get(0).getPath());
                    command.add("-Dclassifier=" + mavenComps.get(0).getClassifier());

                    for (int i = 1; i < mavenComps.size(); i++) {
                        fileJoiner.add(mavenComps.get(i).getPath());
                        classifierJoiner.add(mavenComps.get(i).getClassifier());
                        typeJoiner.add(FilenameUtils.getExtension(mavenComps.get(i).getPath()));
                    }
                } else {
                    command.add("-Dfile=" + mavenComps.get(noClassifierIndex).getPath());
                    for (int i = 0; i < mavenComps.size(); i++) {
                        if (i != noClassifierIndex) {
                            fileJoiner.add(mavenComps.get(i).getPath());
                            classifierJoiner.add(mavenComps.get(i).getClassifier());
                            typeJoiner.add(FilenameUtils.getExtension(mavenComps.get(i).getPath()));
                        }
                    }
                }

                command.add("-Dfiles=" + fileJoiner.toString());
                command.add("-Dclassifiers=" + classifierJoiner.toString());
                command.add("-Dtypes=" + typeJoiner.toString());
            }

            if (value.getPomFile() != null) command.add("-DpomFile=" + value.getPomFile());
            if (value.getJavadocFile() != null) command.add("-Djavadoc" + value.getJavadocFile());
            if (value.getSourceFile() != null) command.add("-Dsources=" + value.getSourceFile());

            String[] commandArray = command.stream().toArray(String[]::new);

            ProcessResult result = redirectProcessExecutor.run(commandArray, resultTextArea);

            if (result.exitValue() < 0) {
                failCnt++;
                resultTextArea.append(String.format("[info] Maven Deploy Artifact %s.%s-%s Failed!\n", groupId, artifactId, version));
            } else {
                successCnt++;
                resultTextArea.append(String.format("[info] Maven Deploy Artifact %s.%s-%s Success!\n", groupId, artifactId, version));
            }
        }
    }

    private void checkMavenOption() {
        if (option.getMavenPath() == null || option.getMavenPath().equals("")) {
            throw new RuntimeException("Maven Path Option Is Empty.");
        }

        if (option.getMavenConfigPath() == null || option.getMavenConfigPath().equals("")) {
            throw new RuntimeException("Maven Config Path Option Is Empty.");
        }

        if (option.getRepositoryName() == null || option.getRepositoryName().equals("")) {
            throw new RuntimeException("Repository Id Option Is Empty.");
        }

        if (option.getRepositoryURL() == null || option.getRepositoryURL().equals("")) {
            throw new RuntimeException("Repository URL Option Is Empty.");
        }
    }

    private List<String> getBaseCommand(String groupId, String artifactId, String version) {
        List<String> command = new ArrayList<>();

        command.add(option.getMavenPath());
        command.add("-s");
        command.add(option.getMavenConfigPath());
        command.add("deploy:deploy-file");
        command.add("-DgroupId=" + groupId);
        command.add("-DartifactId=" + artifactId);
        command.add("-Dversion=" + version);
        command.add("-DrepositoryId=" + option.getRepositoryName());

        String url = String.format("%s/repository/%s/", option.getRepositoryURL(), option.getRepositoryName());

        command.add("-Durl=" + url);


        command.add("-DgeneratePom=false");

        return command;
    }

    private Map<String, String> getBaseFormData(String groupId, String artifactId, String version) {
        Map<String, String> baseFormDataMap = new HashMap<>();

        baseFormDataMap.put("maven2.groupId", groupId);
        baseFormDataMap.put("maven2.artifactId", artifactId);
        baseFormDataMap.put("maven2.version", version);
        baseFormDataMap.put("maven2.generate-pom", "false");

        return baseFormDataMap;
    }


    private java.util.Timer getTimer(JButton selectButton) {
        TimerTask task = new TimerTask() {
            private int seconds = 1;
            public void run() {
                buttonText = "Uploading.. " + "(" + seconds + "s)";
                selectButton.setText(buttonText);
                seconds++;
            }
        };

        java.util.Timer timer = new Timer("Timer");
        long delay = 1000;
        long period = 1000;
        timer.scheduleAtFixedRate(task, delay, period);
        return timer;
    }

}

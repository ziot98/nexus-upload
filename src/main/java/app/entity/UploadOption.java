package app.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import static app.constants.OptionConst.*;

public class UploadOption {
    private String repositoryURL = "";
    private String repositoryName = "";
    private String localRepositoryPath = "";
    private boolean isForceUpload = true;
    private boolean isSnapshot = true;
    private boolean isAutoScroll = true;
    private boolean isHttpUpload = true;
    private String mavenPath = "";
    private String mavenConfigPath = "";
    private String classifiers = "";
    private String id = "";
    private String password = "";
    private static UploadOption instance;
    private static Properties properties;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UploadOption() {
        this.instance = this;

        properties = new Properties();

        try {
            properties.load(new FileInputStream(PROP_PATH));
            this.repositoryURL = properties.getProperty(REPOSITORY_URL);
            this.repositoryName = properties.getProperty(REPOSITORY_NAME);
            this.localRepositoryPath = properties.getProperty(LOCAL_REPOSITORY_PATH);
            this.isForceUpload = Boolean.parseBoolean(properties.getProperty(IS_FORCE_UPLOAD));
            this.isSnapshot = Boolean.parseBoolean(properties.getProperty(IS_SNAPSHOT));
            this.isHttpUpload = Boolean.parseBoolean(properties.getProperty(IS_HTTP_UPLOAD));
            this.isAutoScroll = Boolean.parseBoolean(properties.getProperty(IS_AUTO_SCROLL));
            this.mavenPath = properties.getProperty(MAVEN_PATH);
            this.mavenConfigPath = properties.getProperty(MAVEN_CONFIG_PATH);
            this.classifiers = properties.getProperty(CLASSIFIERS);
            this.id = properties.getProperty(ID);
            this.password = properties.getProperty(PASSWORD);

        } catch (IOException e) {
            properties.setProperty(REPOSITORY_URL, "");
            properties.setProperty(REPOSITORY_NAME, "");
            properties.setProperty(LOCAL_REPOSITORY_PATH, "");
            properties.setProperty(IS_FORCE_UPLOAD, "");
            properties.setProperty(IS_HTTP_UPLOAD, "");
            properties.setProperty(IS_SNAPSHOT, "");
            properties.setProperty(IS_AUTO_SCROLL, "");
            properties.setProperty(MAVEN_PATH, "");
            properties.setProperty(MAVEN_CONFIG_PATH, "");
            properties.setProperty(CLASSIFIERS, "sources,tests,javadoc");
            properties.setProperty(ID, "");
            properties.setProperty(PASSWORD, "");

            try {
                properties.save(new FileOutputStream(PROP_PATH), "nexus uploader config. " + LocalDateTime.now());
            } catch (FileNotFoundException ex) {
                logger.error("property save error.", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    public void save() {
        try {
            properties.save( new FileOutputStream(PROP_PATH), "nexus uploader config. " + LocalDateTime.now());
        } catch (FileNotFoundException e) {
            logger.error("property save error.", e);
            throw new RuntimeException(e);
        }
    }

    public static UploadOption getInstance() {
        return instance;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
        properties.setProperty(REPOSITORY_URL, repositoryURL);
        save();
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        properties.setProperty(REPOSITORY_NAME, repositoryName);
        save();
    }

    public String getLocalRepositoryPath() {
        return localRepositoryPath;
    }

    public void setLocalRepositoryPath(String localRepositoryPath) {
        this.localRepositoryPath = localRepositoryPath;
        properties.setProperty(LOCAL_REPOSITORY_PATH, localRepositoryPath);
        save();
    }

    public boolean isForceUpload() {
        return isForceUpload;
    }

    public void setForceUpload(boolean forceUpload) {
        isForceUpload = forceUpload;
        properties.setProperty(IS_FORCE_UPLOAD, String.valueOf(forceUpload));
        save();
    }

    public boolean isSnapshot() {
        return isSnapshot;
    }

    public void setSnapshot(boolean snapshot) {
        isSnapshot = snapshot;
        properties.setProperty(IS_SNAPSHOT, String.valueOf(snapshot));
        save();
    }

    public String getMavenPath() {
        return mavenPath;
    }

    public void setMavenPath(String mavenPath) {
        this.mavenPath = mavenPath;
        properties.setProperty(MAVEN_PATH, mavenPath);
        save();
    }

    public String getMavenConfigPath() {
        return mavenConfigPath;
    }

    public void setMavenConfigPath(String mavenConfigPath) {
        this.mavenConfigPath = mavenConfigPath;
        properties.setProperty(MAVEN_CONFIG_PATH, mavenConfigPath);
        save();
    }

    public String getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(String classifiers) {
        this.classifiers = classifiers;
        properties.setProperty(CLASSIFIERS, classifiers);
        save();
    }
    public String[] getClassifiersAsArray() {
        if (classifiers != null) return classifiers.split(",");
        else return new String[]{};
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        properties.setProperty(ID, id);
        save();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        properties.setProperty(PASSWORD, password);
        save();
    }

    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    public void setAutoScroll(boolean autoScroll) {
        isAutoScroll = autoScroll;
        properties.setProperty(IS_AUTO_SCROLL, String.valueOf(autoScroll));
        save();
    }

    public boolean isHttpUpload() {
        return isHttpUpload;
    }

    public void setHttpUpload(boolean httpUpload) {
        isHttpUpload = httpUpload;
        properties.setProperty(IS_HTTP_UPLOAD, String.valueOf(httpUpload));
        save();
    }
}

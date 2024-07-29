package app.entity;

import java.nio.file.Path;

public class MavenComp {
    private String path;
    private String artifactId;
    private String groupId;
    private String version;
    private String fileName;
    private String extension;
    private String classifier;


    public MavenComp() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    @Override
    public String toString() {
        return "MavenComp{" +
                "path='" + path + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", version='" + version + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                ", classifier='" + classifier + '\'' +
                '}';
    }
}

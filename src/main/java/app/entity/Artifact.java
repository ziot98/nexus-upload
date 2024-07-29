package app.entity;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class Artifact {
    private String version;
    private String groupId;
    private String artifactId;
    private List<MavenComp> mavenComps = new ArrayList<>();
    private String pomFile = null;
    private String sourceFile = null;
    private String javadocFile = null;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getPomFile() {
        return pomFile;
    }

    public void setPomFile(String pomFile) {
        this.pomFile = pomFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getJavadocFile() {
        return javadocFile;
    }

    public void setJavadocFile(String javadocFile) {
        this.javadocFile = javadocFile;
    }

    public List<MavenComp> getMavenComps() {
        return mavenComps;
    }

    public void setMavenComps(List<MavenComp> mavenComps) {
        this.mavenComps = mavenComps;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer().append(String.format("[info] GroupId : %s, ArtifactId : %s, Version : %s\n", groupId, artifactId, version));
        if (pomFile != null) s.append(String.format("[info] PomFile : %s\n", FilenameUtils.getName(pomFile)));
        if (javadocFile != null) s.append(String.format("[info] JavadocFile : %s\n", FilenameUtils.getName(javadocFile)));
        if (sourceFile != null) s.append(String.format("[info] SourceFile : %s\n", FilenameUtils.getName(sourceFile)));
        mavenComps.stream().forEach(mavenComp -> s.append(String.format("[info] AttachedFile : %s\n", FilenameUtils.getName(mavenComp.getPath()))));

        return s.toString();
    }
}

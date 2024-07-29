package app.provider;

import app.entity.Artifact;
import app.entity.MavenComp;
import app.entity.UploadOption;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessResult;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class RepoFileInfoProvider {
    private static RepoFileInfoProvider instance;
    private String rootFilePath;
    private Map<String, String> artifactInfoCacheMap = new HashMap<>();
    private Map<String, MavenComp> mavenCompMap = new HashMap<>();
    private boolean isSnapShot = false;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PathMatcher compMatcher = FileSystems.getDefault().getPathMatcher("glob:*.{jar,pom}");
    private Path lastPathinfo = null;
    private String pattern = Pattern.quote(System.getProperty("file.separator"));
    private int failCount = 0;


    public RepoFileInfoProvider() {
        this.instance = this;
    }

    public static RepoFileInfoProvider getInstance() {
        return instance;
    }

    public void createRepoFileInfos(String rootFilePath) throws IOException {
    	failCount = 0;
        mavenCompMap.clear();
        artifactInfoCacheMap.clear();
        this.isSnapShot = UploadOption.getInstance().isSnapshot();
        this.rootFilePath = rootFilePath;
        Path rootPath = Paths.get(rootFilePath);
        try (Stream<Path> paths = Files.walk(rootPath)){
            paths.filter(Files::isRegularFile)
                    .filter(path -> compMatcher.matches(path.getFileName()))
                    .forEach(path -> {
                    	RepoFileInfoProvider.getInstance().setLastPathinfo(path);
                        try {
                            getArtifactInfos(path.toString());
                        } catch (XmlPullParserException | IOException e) {
                            logger.error("getArtifactInfos error. Path : {}", path, e);
                            failCount++;
                        } catch (InterruptedException interruptedException) {
                            throw new RuntimeException(interruptedException);
                        }
                    });
        } catch (IOException e) {
            logger.error("Ger File Error. Root Path : {}", RepoFileInfoProvider.getInstance().getLastPathinfo(), e);
            throw e;
        } catch (PatternSyntaxException e) {
            logger.error("Ger File Error. Root Path : {}", RepoFileInfoProvider.getInstance().getLastPathinfo(), e);
            throw e;
        }
    }

    private void getArtifactInfos(String filePath) throws XmlPullParserException, IOException, InterruptedException {
        Boolean isInterrupted = Thread.currentThread().isInterrupted();
        if (isInterrupted) {
            throw new InterruptedException();
        }
        String ext = FilenameUtils.getExtension(filePath);
        String fileName = FilenameUtils.getBaseName(filePath);

        MavenComp mavenComp = new MavenComp();
        mavenComp.setPath(filePath);
        mavenComp.setFileName(fileName);
        mavenComp.setExtension(ext);

        if (ext.equalsIgnoreCase("pom")) {
            if (!readPom(mavenComp)) readFromPath(mavenComp);
            putMavenComp(mavenComp);
            if (!artifactInfoCacheMap.containsKey(mavenComp.getArtifactId())) {
                artifactInfoCacheMap.put(mavenComp.getArtifactId(), mavenComp.getGroupId());
            }
        } else {
            if (!readJar(mavenComp)) readFromPath(mavenComp);
            putMavenComp(mavenComp);
        }
    }

    private boolean readPom(MavenComp mavenComp)  {
        String path = mavenComp.getPath();
        try (InputStream inputStream = new FileInputStream(path)) {
            return readPom(mavenComp, inputStream);
        } catch (FileNotFoundException e) {
            logger.error("read Pom error : {}", path, e);
        } catch (IOException e) {
            logger.error("read Pom error : {}", path, e);
        }

        return false;
    }


    private boolean readPom(MavenComp mavenComp, InputStream inputStream)  {

        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(inputStream);
            String groupId = model.getGroupId();
            String version = model.getVersion();

            mavenComp.setArtifactId(model.getArtifactId());
            mavenComp.setClassifier(null);

            Parent parent = model.getParent();
            if (groupId == null) groupId = parent.getGroupId();
            if (version == null) version = parent.getVersion();

            mavenComp.setVersion(version);
            mavenComp.setGroupId(groupId);

            logger.debug("MavenComp Generated : {}", mavenComp.toString());
        } catch (IOException | XmlPullParserException e ) {
            logger.error("read Pom error : {}", mavenComp.getPath(), e);
            return false;
        }

        return true;
    }

    private boolean readFromPath(MavenComp mavenComp) {
        String path = mavenComp.getPath();
        String m2Path = path.replace(rootFilePath + File.separator, "");
        
        String[] m2paths = m2Path.split(pattern);

        if (m2paths.length < 4) {
            return false;
        } else {
            String version = m2paths[m2paths.length - 2];
            String artifactId = m2paths[m2paths.length - 3];
            String groupId = null;
            String classifier = getClassifier(mavenComp.getFileName(), artifactId, version);
            StringJoiner sj = new StringJoiner(".");

            for (int i = 0; i < m2paths.length-3; i++) {
                sj.add(m2paths[i]);
            }

            groupId = sj.toString();

            mavenComp.setVersion(version);
            mavenComp.setArtifactId(artifactId);
            mavenComp.setGroupId(groupId);
            mavenComp.setClassifier(classifier);

            return true;
        }
    }

    private String getClassifier(String fileName, String artifactString, String versionString) {
        String classifier = null;
        classifier = fileName.replace(artifactString + "-", "");
        classifier = classifier.replace(versionString, "");
        if (classifier.startsWith("-")) {
            classifier = classifier.substring(1, classifier.length());
        }

        return classifier;
    }

    private boolean readJar(MavenComp mavenComp) throws IOException {
        String jarPath = mavenComp.getPath();
        ZipFile jarFile = new ZipFile(jarPath);

        try (ZipInputStream jarInputStream = new ZipInputStream(new FileInputStream(jarPath))) {
            ZipEntry zipEntry = jarInputStream.getNextEntry();
            while (zipEntry != null) {
                String zipEntryName = zipEntry.getName();
                if (zipEntryName.startsWith("META-INF/maven")) {
                	
                	if (jarPath.contains("with-deps") && !zipEntryName.contains(mavenComp.getFileName())) {
                		break;
                	}
                	
                    if (zipEntryName.endsWith("pom.properties")) {
                        logger.info(zipEntryName);
                        logger.debug(zipEntryName);
                        try (InputStream stream = jarFile.getInputStream(zipEntry)) {
                            Properties pomProperties = new Properties();
                            pomProperties.load(stream);
                            String artifactId = pomProperties.getProperty("artifactId");
                            String groupId = pomProperties.getProperty("groupId");
                            String version = pomProperties.getProperty("version");
                            String classifier = getClassifier(mavenComp.getFileName(), artifactId, version);

                            mavenComp.setArtifactId(artifactId);
                            mavenComp.setGroupId(groupId);
                            mavenComp.setVersion(version);
                            mavenComp.setClassifier(classifier);

                            return true;
                        } catch (Exception e) {
                            logger.error("read Error jar : {}", mavenComp.getPath(), e);
                        }
                    }

                    if (zipEntryName.endsWith("pom.xml")) {
                        try (InputStream stream = jarFile.getInputStream(zipEntry)) {
                            if (readPom(mavenComp, stream)) {
                                String classifier = getClassifier(mavenComp.getFileName(), mavenComp.getArtifactId(), mavenComp.getVersion());
                                mavenComp.setClassifier(classifier);
                            }
                        } catch (Exception e) {
                            logger.error("read Error jar : {}", mavenComp.getPath(), e);
                        }
                    }
                }
                zipEntry = jarInputStream.getNextEntry();
            }
        }

        return false;
    }

    private void putMavenComp(MavenComp mavenComp) {
    	String version = mavenComp.getVersion();
        if (isSnapShot) {
            
            if (version.contains("SNAPSHOT")) {
                mavenCompMap.put(mavenComp.getPath(), mavenComp);
            }
        } else {
            if (!version.contains("SNAPSHOT")) {
                mavenCompMap.put(mavenComp.getPath(), mavenComp);
            }
        }

    }

    public Map<String, MavenComp> getMavenCompMap() {
        return mavenCompMap;
    }

	public Path getLastPathinfo() {
		return lastPathinfo;
	}

	public void setLastPathinfo(Path lastPathinfo) {
		this.lastPathinfo = lastPathinfo;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

    public Map<String, Artifact> createArtifactInfo(JTable jTable) {

        Map<String, Artifact> artifactInfoMap = new HashMap<>();

        for (int i = 0; i < jTable.getRowCount(); i++) {

            String groupId = (String) jTable.getValueAt(i, 0);
            String artifactId = (String) jTable.getValueAt(i, 1);
            String version = (String) jTable.getValueAt(i, 2);
            String filePath = (String) jTable.getValueAt(i, 3);
            Object classifierObj = jTable.getValueAt(i, 4);
            String classifier = (classifierObj == null) ? "" : (String) classifierObj;

            Artifact artifact = null;
            String mapId = groupId + "." + artifactId + "-" + version;
            if (artifactInfoMap.containsKey(mapId)) {
                artifact = artifactInfoMap.get(mapId);
            } else {
                artifact = new Artifact();
                artifactInfoMap.put(mapId, artifact);
            }

            if (artifact.getArtifactId() == null) artifact.setArtifactId(artifactId);
            if (artifact.getGroupId() == null) artifact.setGroupId(groupId);
            if (artifact.getVersion() == null) artifact.setVersion(version);

            if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("pom")) {
                artifact.setPomFile(filePath);
            } else if (classifier.equalsIgnoreCase("javadoc")) {
                artifact.setJavadocFile(filePath);
            } else if (classifier.equalsIgnoreCase("sources")) {
                artifact.setSourceFile(filePath);
            } else {
                MavenComp mavenComp = new MavenComp();
                mavenComp.setPath(filePath);
                mavenComp.setClassifier(classifier);
                artifact.getMavenComps().add(mavenComp);
            }
        }

        return artifactInfoMap;
    }
    
}

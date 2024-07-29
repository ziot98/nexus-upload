# nexus*upload
Since Nexus 3 version, repository files are stored in the database rather than the file system, making it difficult to upload library files in the local M2 repository directly to the server. Therefore, I created a JAVA*based GUI program so that it can be easily uploaded through RestAPI provided by Nexus and Maven's Deploy plugin.


##Reqirements
* Java 8 or higher

##How to Use
* Run the Jar file.
* Set the local m2 repository path to Local Repository Path. ex) /Users/A/.m2/repository/
* Select the Repository Type to upload. SnapShot only selects SNAPSHOT versions of libraries.
* Click the Get Files button to retrieve the library list.
* After checking and modifying the contents in the list, click the Upload button to upload.
* The result text area can be cleared by right*clicking, or AutoScroll can be set.

##Option Description
* Upload Mode: Supports Http Post and Maven. Http Post is uploaded using Rest API. Maven uploads through Maven Deploy. For Snapshot versions, only Maven Deploy can upload.
* Repository Name: This is the Repository ID you want to upload. When uploading to Maven, you must use the ID defined in servers in settings.xml. When uploading to Http Post, it is Nexus' Repository ID.
* Repository URL: Nexus URL to upload. ex) http://xxx.xxx.xx.xx:8081
* Force upload: Whether to force upload when a file already exists in the repository. Applicable only in Http Post Mode.
* Nexus ID: Nexus authentication ID, applicable only in Http Post Mode. Maven uses the authentication information defined on servers in settings.xml.
* Nexus Password: Nexus authentication password and only applicable in Http Post Mode. Maven uses the authentication information defined on servers in settings.xml.
* Maven Path: Path to the mvn executable file to run in Maven mode. You must specify the path to the mvn file for Mac or mvn.cmd for Windows.
* Maven Path: Path to the settings.xml file to run in Maven mode.
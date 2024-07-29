package app.ui.contents;

import app.ui.creator.GridBagCreator;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HelpContent extends Content {


    public HelpContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        JTextArea HelpTextArea = createTextArea(0, 1, 1, 30, 70, 1, weightY2);
        HelpTextArea.append("Created By LGR. 2024 ver 1.0\n");
        HelpTextArea.append("\n");
        HelpTextArea.append("* 이 프로그램은 로컬에 있는 라이브러리를 Nexus3에 업로드 하기 위한 프로그램입니다.\n");
        HelpTextArea.append("* 사용 방법\n");
        HelpTextArea.append("- 로컬 m2 레파지토리 경로를 Local Repository Path에 설정합니다. ex) /Users/A/.m2/repository\n");
        HelpTextArea.append("- 업로드 할 Repositㅇory Type를 선택합니다. SnapShot은 SNAPSHOT 버전의 라이브러리만 선택합니다.\n");
        HelpTextArea.append("- Get Files 버튼을 눌러 라이브러리 목록을 불러옵니다.\n");
        HelpTextArea.append("- 목록에서 내용을 확인 및 수정한 후 Upload 버튼을 눌러 Upload 합니다.\n");
        HelpTextArea.append("- 결과 창은 오른쪽 클릭을 하여 Clear하거나, AutoScroll을 설정할 수 있습니다.\n");
        HelpTextArea.append("\n");
        HelpTextArea.append("* 옵션 설명\n");
        HelpTextArea.append("- Upload Mode : Http Post와 Maven를 지원합니다. Http Post는 Rest API로 업로드 합니다. Maven은 Maven Deploy를 통해 업로드 합니다. Snapshot 버전의 경우 Maven Deploy만 업로드 가능합니다.\n");
        HelpTextArea.append("- Repository Name : 업로드 하고자 하는 Repository ID로 Maven 업로드 시 settings.xml의 servers에 정의 된 ID를 사용해야 합니다. Http Post 업로드 시에는 Nexus의 Repository ID 입니다.\n");
        HelpTextArea.append("- Repository URL : 업로드 하고자 하는 Nexus의 URL. ex) http://xxx.xxx.xx.xx:8170\n");
        HelpTextArea.append("- Force upload : 레파지토리에 이미 파일이 존재 할 때 강제 업로드 할지 여부로 Http Post Mode 에서만 해당합니다.\n");
        HelpTextArea.append("- Nexus ID : 넥서스 인증 ID로 Http Post Mode 에서만 해당합니다. Maven은 settings.xml의 servers에 정의 된 인증 정보를 사용합니다.\n");
        HelpTextArea.append("- Nexus Password : 넥서스 인증 비밀번호로 Http Post Mode 에서만 해당합니다. Maven은 settings.xml의 servers에 정의 된 인증 정보를 사용합니다.\n");
        HelpTextArea.append("- Maven Path : Maven 모드에서 실행 할 mvn 실행 파일 경로 입니다. mac은 mvn, 윈도우의 경우 mvn.cmd 파일 경로를 지정해야 합니다. \n");
        HelpTextArea.append("- Maven Path : Maven 모드에서 실행 할 settings.xml 파일 경로 입니다.");
        HelpTextArea.append("\n");
        HelpTextArea.append("\n");
        HelpTextArea.append("\n");
        HelpTextArea.append("\n");
        HelpTextArea.append("\n");

    }

}

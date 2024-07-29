package app.ui.contents;

import app.entity.MavenComp;
import app.provider.RepoFileInfoProvider;
import app.ui.component.ButtonColumn;
import app.ui.component.DeleteAction;
import app.ui.component.HeaderRenderer;
import app.ui.creator.GridBagCreator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.Map;
import java.util.Set;

public class FileListContent extends Content {
    private JTable fileListTable;
    public FileListContent(GridBagCreator gridBagCreator, int lineNum) {
        super(gridBagCreator, lineNum);
    }

    @Override
    public void createContents() {
        String[] columnNames = {"GroupId", "ArtifactId", "Ver", "FilePath", "Classifier", ""};
        fileListTable = createTable(columnNames, 0, 4, 1, 700, 200, 1, weightY2);
        fileListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        fileListTable.setAutoCreateRowSorter(true);

        JTableHeader header = fileListTable.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(fileListTable));


        ButtonColumn deleteButtonColumn = new ButtonColumn(fileListTable,
                new DeleteAction(), 5);
    }

    public void addFileRows(Map<String, MavenComp> mavenCompMap) {
        for (Map.Entry<String, MavenComp> entry : mavenCompMap.entrySet()) {
            MavenComp value = entry.getValue();

            String[] newRow = new String[]{value.getGroupId(), value.getArtifactId(), value.getVersion(), value.getPath(), value.getClassifier(), "DELETE"};
            ((DefaultTableModel)fileListTable.getModel()).addRow(newRow);
        }
    }

    public void resizeTableColumn(int width) {

        fileListTable.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(width * 0.15));
        //fileListTable.getColumnModel().getColumn(0).setMaxWidth((int) Math.round(width * 0.15));

        fileListTable.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(width * 0.15));
        //fileListTable.getColumnModel().getColumn(1).setMaxWidth((int) Math.round(width * 0.1));

        fileListTable.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(width * 0.05));
        //fileListTable.getColumnModel().getColumn(2).setMaxWidth((int) Math.round(width * 0.1));

        fileListTable.getColumnModel().getColumn(3).setPreferredWidth((int) Math.round(width * 1.0));

        fileListTable.getColumnModel().getColumn(4).setPreferredWidth((int) Math.round(width * 0.1));
        //fileListTable.getColumnModel().getColumn(4).setMaxWidth((int) Math.round(width * 0.1));

        fileListTable.getColumnModel().getColumn(5).setPreferredWidth((int) Math.round(width * 0.13));
        //fileListTable.getColumnModel().getColumn(5).setMaxWidth((int) Math.round(width * 0.13));

    }

    public int getRowCount() {
        return fileListTable.getRowCount();
    }

    public boolean checkEmpty() {
        for (int i = 0; i < getRowCount(); i++) {
            Object groupId = fileListTable.getValueAt(i, 0);
            Object artifactId = fileListTable.getValueAt(i, 1);
            Object version = fileListTable.getValueAt(i, 2);

            if (groupId == null) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,0,false, false);
                return true;
            } else if (((String) groupId).equals("")) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,0,false, false);
                return true;
            }

            if (artifactId == null) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,1,false, false);
                return true;
            } else if (((String) artifactId).equals("")) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,1,false, false);
                return true;
            }

            if (version == null) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,2,false, false);
                return true;
            } else if (((String) version).equals("")) {
                fileListTable.requestFocus();
                fileListTable.changeSelection(i,2,false, false);
                return true;
            }

        }

        return false;
    }

    public JTable getFileListTable() {
        return fileListTable;
    }
}

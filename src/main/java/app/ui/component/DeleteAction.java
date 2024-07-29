package app.ui.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class DeleteAction extends AbstractAction {
	
    public void actionPerformed(ActionEvent e)
    {
        JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
    }
}

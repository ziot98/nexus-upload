package app.ui.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTooltipTable extends JTable {

    public JTooltipTable(DefaultTableModel model) {
        super(model);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Object value = getValueAt(row, column);
            if (value != null && value instanceof String) {
                if (!value.equals("DELETE"))
                jc.setToolTipText((String) value);
            }
        }
        return c;
    }
}

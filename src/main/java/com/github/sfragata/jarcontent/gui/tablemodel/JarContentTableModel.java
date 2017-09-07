/**
 *
 */
package com.github.sfragata.jarcontent.gui.tablemodel;

import java.util.Locale;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Class to specifies the methods the <code>JTable</code> will use to
 * interrogate a tabular data model.
 *
 * @author Fragata da Silva, Silvio
 * @see javax.swing.table.DefaultTableModel
 *
 */
@Component
public class JarContentTableModel
    extends DefaultTableModel {

    /**
     *
     */
    private static final long serialVersionUID = -4223874201981670143L;

    /**
     *
     */
    @Autowired
    public JarContentTableModel(final MessageSource messageSource) {
        super.setColumnCount(0);
        super.setColumnIdentifiers(new Object[] { messageSource.getMessage("JAR", null, Locale.getDefault()),
            messageSource.getMessage("CLASS", null, Locale.getDefault()) });
    }

    /**
     * @param rowCount
     * @param columnCount
     */
    public JarContentTableModel(final int rowCount, final int columnCount) {
        super(rowCount, columnCount);
    }

    /**
     * @param columnNames
     * @param rowCount
     */
    @SuppressWarnings("rawtypes")
    public JarContentTableModel(final Vector columnNames, final int rowCount) {
        super(columnNames, rowCount);
    }

    /**
     * @param columnNames
     * @param rowCount
     */
    public JarContentTableModel(final Object[] columnNames, final int rowCount) {
        super(columnNames, rowCount);
    }

    /**
     * @param data
     * @param columnNames
     */
    @SuppressWarnings("rawtypes")
    public JarContentTableModel(final Vector data, final Vector columnNames) {
        super(data, columnNames);
    }

    /**
     * @param data
     * @param columnNames
     */
    public JarContentTableModel(final Object[][] data, final Object[] columnNames) {
        super(data, columnNames);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean isCellEditable(
        final int row,
        final int column) {

        return false;
    }
}

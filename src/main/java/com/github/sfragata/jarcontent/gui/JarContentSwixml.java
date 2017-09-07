package com.github.sfragata.jarcontent.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.swixml.SwingEngine;

import com.github.sfragata.jarcontent.gui.tablemodel.JarContentTableModel;
import com.github.sfragata.jarcontent.listener.EventListener;
import com.github.sfragata.jarcontent.main.JarContent;
import com.github.sfragata.jarcontent.to.JarContentTO;

@Component
@Qualifier("eventListener")
/**
 * Desktop class (with Swixml framework)
 *
 * @author Fragata da Silva, Silvio
 */
public class JarContentSwixml
    implements ActionListener, EventListener {

    private static final String PROGRESS_XML = "com/github/sfragata/jarcontent/gui/progress.xml";

    private static final String JARCONTENT_XML = "com/github/sfragata/jarcontent/gui/jarcontent.xml";

    private static Logger logger = LoggerFactory.getLogger(JarContentSwixml.class);

    private int dirFileLength;

    private int maximumProgress = 100;

    private final SwingEngine swixml;

    private final SwingEngine swixmlDialog;

    private final JDialog dialog;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private JarContent jarContent;

    @Autowired
    public JarContentSwixml(
        final SwingEngine swixml,
        final SwingEngine swixmlDialog,
        final JarContentTableModel jarContentTableModel) throws Exception {
        this.swixml = swixml;
        swixml.render(JARCONTENT_XML);
        swixml.setActionListener(swixml.getRootComponent(), this);
        swixml.getRootComponent().setVisible(true);
        this.swixmlDialog = swixmlDialog;
        this.dialog = (JDialog) swixmlDialog.render(PROGRESS_XML);
        getTable().setModel(jarContentTableModel);
    }

    @Override
    public void actionPerformed(
        final ActionEvent e) {

        final String command = e.getActionCommand();
        if ("QUIT".equals(command)) {
            System.exit(0);
        } else if ("OPEN".equals(command)) {
            open();
        } else if ("SEARCH".equals(command)) {
            search();
        }
    }

    private JTable getTable() {

        return (JTable) this.swixml.find("tableFiles");
    }

    private JTextField getTextFieldClass() {

        return (JTextField) this.swixml.find("textFieldClass");
    }

    private JTextField getTextFieldDir() {

        return (JTextField) this.swixml.find("textFieldDir");
    }

    private JCheckBox getCheckBoxIgnoreCase() {

        return (JCheckBox) this.swixml.find("ignoreCase");
    }

    private void setTextField(
        final String textField,
        final String value) {

        ((JTextField) this.swixml.find(textField)).setText(value);
    }

    private void clearTable() {

        ((DefaultTableModel) getTable().getModel()).setRowCount(0);
    }

    private void clearResult() {

        clearTable();
        if (getJProgressBar() != null) {
            setProgressValue(0);
        }
    }

    private void search() {

        clearResult();
        final String dir = getTextFieldDir().getText();
        final String clazz = convertClassName(getTextFieldClass().getText());
        final boolean ignoreCase = getCheckBoxIgnoreCase().isSelected();
        try {
            if (StringUtils.isNotBlank(dir) && new File(dir).exists() && StringUtils.isNotBlank(clazz)) {
                openProgressDialog();
                setStatus(this.messageSource.getMessage("SEARCHING", null, Locale.getDefault()));
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JarContentSwixml.this.jarContent.findJars(dir, clazz, ignoreCase);
                    }
                });
                thread.setName("JarContent");
                thread.start();

            } else {
                setError(this.messageSource.getMessage("REQUIRED_FIELDS", null, Locale.getDefault()));
            }
        } catch (final Exception e) {
            error(e);
        }
    }

    private void open() {

        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(
                final File pathname) {

                return pathname.isDirectory();
            }

            @Override
            public String getDescription() {

                return JarContentSwixml.this.messageSource.getMessage("DIR_JAR", null, Locale.getDefault());
            }
        });
        chooser.setDialogTitle(this.messageSource.getMessage("OPEN", null, Locale.getDefault()));
        chooser.setFileHidingEnabled(false);
        chooser.setMultiSelectionEnabled(false);
        final String currentDir = getCurrentDir();
        chooser.setCurrentDirectory(new File(currentDir));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final int returnVal = chooser.showOpenDialog(null);
        if (returnVal == 0) {
            setTextField("textFieldDir", chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private String getCurrentDir() {

        if (getTextFieldDir().getText() != null) {
            return getTextFieldDir().getText();
        }
        return ".";
    }

    @Override
    public void error(
        final Exception e) {

        try (final StringWriter s = new StringWriter()) {
            e.printStackTrace(new PrintWriter(s));
            s.flush();
            s.close();
            setError(s.getBuffer().toString());
        } catch (final IOException ex) {
            logger.error("Error", ex);
        }
    }

    void setError(
        final String msg) {

        JOptionPane.showMessageDialog(this.swixml.getRootComponent(), msg,
            this.messageSource.getMessage("ERROR", null, Locale.getDefault()), JOptionPane.ERROR_MESSAGE);
    }

    void setMessage(
        final String msg) {

        JOptionPane.showMessageDialog(this.swixml.getRootComponent(), msg,
            this.messageSource.getMessage("INFO", null, Locale.getDefault()), JOptionPane.INFORMATION_MESSAGE);
    }

    private String convertClassName(
        final String clazz) {

        if (StringUtils.isBlank(clazz)) {
            return clazz;
        }
        final String clazzString = clazz.replaceAll("\\.", "/");
        return clazzString;
    }

    int calculateProgressValue() {

        return this.maximumProgress / this.dirFileLength;
    }

    private JProgressBar getJProgressBar() {

        return (JProgressBar) this.swixmlDialog.find("progress");
    }

    private void setProgressValue(
        final int value) {

        getJProgressBar().setValue(value);
        getJProgressBar().setString(value + "%");
    }

    @Override
    public void addResult(
        final JarContentTO contentTO) {

        final DefaultTableModel defaultTableModel = (DefaultTableModel) getTable().getModel();
        final String[] values = new String[] { contentTO.getJarName(), contentTO.getClassName() };
        defaultTableModel.addRow(values);
    }

    @Override
    public void increaseProgress() {

        int status = calculateProgressValue();
        this.maximumProgress -= status;
        this.dirFileLength--;
        status += getJProgressBar().getValue();
        if (status >= 100) {
            status = 100;
            this.maximumProgress = 100;
        }
        setProgressValue(status);
    }

    @Override
    public void setCollectionLength(
        final int length) {

        this.dirFileLength = length;
    }

    @Override
    public void setStatus(
        final String msg) {

        setLableTextResult("labelResult", msg);
    }

    private void setLableTextResult(
        final String label,
        final String value) {

        ((JLabel) this.swixmlDialog.find(label)).setText(value);
    }

    private void openProgressDialog() {

        try {
            this.dialog.setVisible(true);
        } catch (final Exception ex) {
            logger.error("Error", ex);
        }
    }
}

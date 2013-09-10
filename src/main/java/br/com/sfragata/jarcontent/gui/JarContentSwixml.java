package br.com.sfragata.jarcontent.gui;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.swixml.SwingEngine;

import br.com.sfragata.jarcontent.JarContent;
import br.com.sfragata.jarcontent.gui.tablemodel.JarContentTableModel;
import br.com.sfragata.jarcontent.listener.EventListener;
import br.com.sfragata.jarcontent.to.JarContentTO;

@Component
@Qualifier("eventListener")
/**
 * Desktop class (with Swixml framework)
 * 
 * @author Fragata da Silva, Silvio
 */
public class JarContentSwixml implements ActionListener, EventListener {

	private static Log logger = LogFactory.getLog(JarContentSwixml.class);

	private int dirFileLength;
	private int maximumProgress = 100;
	private SwingEngine swixml;
	private SwingEngine swixmlDialog;
	private JDialog dialog;

	@Autowired
	MessageSource messageSource;
	@Autowired
	private JarContent jarContent;

	@Autowired
	public JarContentSwixml(SwingEngine swixml, SwingEngine swixmlDialog,
			JarContentTableModel jarContentTableModel) throws Exception {
		this.swixml = swixml;
		swixml.render("br/com/sfragata/jarcontent/gui/jarcontent.xml");
		swixml.setActionListener(swixml.getRootComponent(), this);
		swixml.getRootComponent().setVisible(true);
		this.swixmlDialog = swixmlDialog;
		this.dialog = (JDialog) swixmlDialog
				.render("br/com/sfragata/jarcontent/gui/progress.xml");
		getTable().setModel(jarContentTableModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("QUIT".equals(command)) {
			System.exit(0);
		} else if ("OPEN".equals(command)) {
			open();
		} else if ("SEARCH".equals(command)) {
			search();
		}
	}

	private JTable getTable() {
		return (JTable) swixml.find("tableFiles");
	}

	private JTextField getTextFieldClass() {
		return (JTextField) swixml.find("textFieldClass");
	}

	private JTextField getTextFieldDir() {
		return (JTextField) swixml.find("textFieldDir");
	}

	private JCheckBox getCheckBoxIgnoreCase() {
		return (JCheckBox) swixml.find("ignoreCase");
	}

	private void setTextField(String textField, String value) {
		((JTextField) swixml.find(textField)).setText(value);
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
			if (StringUtils.isNotBlank(dir) && new File(dir).exists()
					&& StringUtils.isNotBlank(clazz)) {
				openProgressDialog();
				setStatus(messageSource.getMessage("SEARCHING", null,
						Locale.getDefault()));
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						jarContent.findJars(dir, clazz, ignoreCase);
					}
				});
				thread.setName("JarContent");
				thread.start();

			} else {
				setError(messageSource.getMessage("REQUIRED_FIELDS", null,
						Locale.getDefault()));
			}
		} catch (Exception e) {
			error(e);
		}
	}

	private void open() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}

			@Override
			public String getDescription() {
				return messageSource.getMessage("DIR_JAR", null,
						Locale.getDefault());
			}
		});
		chooser.setDialogTitle(messageSource.getMessage("OPEN", null,
				Locale.getDefault()));
		chooser.setFileHidingEnabled(false);
		chooser.setMultiSelectionEnabled(false);
		String currentDir = getCurrentDir();
		chooser.setCurrentDirectory(new File(currentDir));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == 0) {
			setTextField("textFieldDir", chooser.getSelectedFile()
					.getAbsolutePath());
		}
	}

	private String getCurrentDir() {
		if (getTextFieldDir().getText() != null) {
			return getTextFieldDir().getText();
		}
		return ".";
	}

	@Override
	public void error(Exception e) {
		try {
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			s.flush();
			s.close();
			setError(s.getBuffer().toString());
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	void setError(String msg) {
		JOptionPane.showMessageDialog(swixml.getRootComponent(), msg,
				messageSource.getMessage("ERROR", null, Locale.getDefault()),
				JOptionPane.ERROR_MESSAGE);
	}

	void setMessage(String msg) {
		JOptionPane.showMessageDialog(swixml.getRootComponent(), msg,
				messageSource.getMessage("INFO", null, Locale.getDefault()),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private String convertClassName(String clazz) {
		if (StringUtils.isBlank(clazz)) {
			return clazz;
		}
		clazz = clazz.replaceAll("\\.", "/");
		return clazz;
	}

	int calculateProgressValue() {
		return maximumProgress / dirFileLength;
	}

	private JProgressBar getJProgressBar() {
		return (JProgressBar) swixmlDialog.find("progress");
	}

	private void setProgressValue(int value) {
		getJProgressBar().setValue(value);
		getJProgressBar().setString(value + "%");
	}

	@Override
	public void addResult(JarContentTO contentTO) {
		DefaultTableModel defaultTableModel = (DefaultTableModel) getTable()
				.getModel();
		String[] values = new String[] { contentTO.getJarName(),
				contentTO.getClassName() };
		defaultTableModel.addRow(values);
	}

	@Override
	public void increaseProgress() {
		int status = calculateProgressValue();
		maximumProgress -= status;
		dirFileLength--;
		status += getJProgressBar().getValue();
		if (status >= 100) {
			status = 100;
			maximumProgress = 100;
		}
		setProgressValue(status);
	}

	@Override
	public void setCollectionLength(int length) {
		dirFileLength = length;
	}

	@Override
	public void setStatus(String msg) {
		setLableTextResult("labelResult", msg);
	}

	private void setLableTextResult(String label, String value) {
		((JLabel) swixmlDialog.find(label)).setText(value);
	}

	private void openProgressDialog() {
		try {
			dialog.setVisible(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
}

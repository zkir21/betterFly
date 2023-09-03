package com.unitedthinkers.fly.settings;

import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/07/11
 */
public class AppSettingsComponent {

	private static final String JBOSS_PATH = "JBoss path: ";
	private static final String MODULE_ARTIFACT_ID = "Module artifact Id: ";
	private static final String GOOGLE_KEY_FILE = "Key file: ";
	private static final String GOOGLE_SHEETS_DATA_FILE = "Sheets Data file Id: ";
	private static final String GOOGLE_SHEETS_TAG_FILE = "Sheets Tag file Id: ";
	private static final String GOOGLE = "Google";
	private static final String UNIPAY = "Unipay";

	private final JPanel mainPanel;
	private final JBTextField jbossPath = new JBTextField();
	private final JBTextField moduleArtifactId = new JBTextField();
	private final JBTextField googleKeyFile = new JBTextField();
	private final JBTextField googleSheetsDataFile = new JBTextField();
	private final JBTextField googleSheetsTagFile = new JBTextField();

	public AppSettingsComponent() {
		mainPanel = FormBuilder.createFormBuilder()
				.addComponent(new TitledSeparator(UNIPAY))
				.addLabeledComponent(new JBLabel(JBOSS_PATH), jbossPath, 1, false)
				.addLabeledComponent(new JBLabel(MODULE_ARTIFACT_ID), moduleArtifactId, 1, false)
				.addComponent(new TitledSeparator(GOOGLE))
				.addLabeledComponent(new JBLabel(GOOGLE_KEY_FILE), googleKeyFile, 1, false)
				.addLabeledComponent(new JBLabel(GOOGLE_SHEETS_DATA_FILE), googleSheetsDataFile, 1, false)
				.addLabeledComponent(new JBLabel(GOOGLE_SHEETS_TAG_FILE), googleSheetsTagFile, 1, false)
				.addComponentFillVertically(new JPanel(), 0)
				.getPanel();
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public JComponent getPreferredFocusedComponent() {
		return jbossPath;
	}

	@NotNull
	public String getJbossPathText() {
		return jbossPath.getText();
	}

	public void setJbossPathText(@NotNull String newText) {
		jbossPath.setText(newText);
	}

	@NotNull
	public String getModuleArtifactIdText() {
		return moduleArtifactId.getText();
	}

	public void setModuleArtifactIdText(@NotNull String newText) {
		moduleArtifactId.setText(newText);
	}

	@NotNull
	public String getGoogleKeyFileText() {
		return googleKeyFile.getText();
	}

	public void setGoogleKeyFileText(@NotNull String newText) {
		googleKeyFile.setText(newText);
	}

	@NotNull
	public String getGoogleSheetsFileText() {
		return googleSheetsDataFile.getText();
	}

	public void setGoogleSheetsFileText(@NotNull String newText) {
		googleSheetsDataFile.setText(newText);
	}

	@NotNull
	public String getGoogleSheetsTagText() {
		return googleSheetsTagFile.getText();
	}

	public void setGoogleSheetsTagText(@NotNull String newText) {
		googleSheetsTagFile.setText(newText);
	}

}

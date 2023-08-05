package com.unitedthinkers.fly.settings;

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

	private final JPanel mainPanel;
	private final JBTextField jbossPath = new JBTextField();
	private final JBTextField moduleArtifactId = new JBTextField();

	public AppSettingsComponent() {
		mainPanel = FormBuilder.createFormBuilder()
				.addLabeledComponent(new JBLabel("Enter JBoss path: "), jbossPath, 1, false)
				.addLabeledComponent(new JBLabel("Enter module artifact Id: "), moduleArtifactId, 1, false)
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

}

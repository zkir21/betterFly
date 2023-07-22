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
	private final JBTextField unipayJbossPath = new JBTextField();
	private final JBTextField unipayArtifactId = new JBTextField();

	public AppSettingsComponent() {
		mainPanel = FormBuilder.createFormBuilder()
				.addLabeledComponent(new JBLabel("Enter unipay JBoss path: "), unipayJbossPath, 1, false)
				.addLabeledComponent(new JBLabel("Enter unipay artifact Id: "), unipayArtifactId, 1, false)
				.addComponentFillVertically(new JPanel(), 0)
				.getPanel();
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public JComponent getPreferredFocusedComponent() {
		return unipayJbossPath;
	}

	@NotNull
	public String getUnipayJbossPathText() {
		return unipayJbossPath.getText();
	}

	public void setUnipayJbossPathText(@NotNull String newText) {
		unipayJbossPath.setText(newText);
	}

	@NotNull
	public String getUnipayArtifactIdText() {
		return unipayArtifactId.getText();
	}

	public void setUnipayArtifactIdText(@NotNull String newText) {
		unipayArtifactId.setText(newText);
	}

}

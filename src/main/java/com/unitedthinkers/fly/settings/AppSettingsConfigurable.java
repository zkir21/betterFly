package com.unitedthinkers.fly.settings;

import com.intellij.openapi.options.Configurable;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/07/11
 */
public class AppSettingsConfigurable implements Configurable {

	private AppSettingsComponent mySettingsComponent;

	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() {
		return "BetterFly: Plugin Settings";
	}

	@Override
	public JComponent getPreferredFocusedComponent() {
		return mySettingsComponent.getPreferredFocusedComponent();
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		mySettingsComponent = new AppSettingsComponent();
		return mySettingsComponent.getPanel();
	}

	@Override
	public boolean isModified() {
		AppSettingsState settings = AppSettingsState.getInstance();
		boolean modified = !mySettingsComponent.getUnipayJbossPathText().equals(settings.unipayJbossPath);
		modified |= mySettingsComponent.getUnipayArtifactIdText() != settings.unipayArtifactId;
		return modified;
	}

	@Override
	public void apply() {
		AppSettingsState settings = AppSettingsState.getInstance();
		settings.unipayJbossPath = mySettingsComponent.getUnipayJbossPathText();
		settings.unipayArtifactId = mySettingsComponent.getUnipayArtifactIdText();
	}

	@Override
	public void reset() {
		AppSettingsState settings = AppSettingsState.getInstance();
		mySettingsComponent.setUnipayJbossPathText(settings.unipayJbossPath);
		mySettingsComponent.setUnipayArtifactIdText(settings.unipayArtifactId);
	}

	@Override
	public void disposeUIResources() {
		mySettingsComponent = null;
	}
}

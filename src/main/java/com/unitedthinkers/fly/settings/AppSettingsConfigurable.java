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
		boolean modified = !mySettingsComponent.getJbossPathText().equals(settings.jbossPath);
		modified |= mySettingsComponent.getModuleArtifactIdText() != settings.moduleArtifactId;
		modified |= mySettingsComponent.getGoogleKeyFileText() != settings.googleKeyFile;
		modified |= mySettingsComponent.getGoogleSheetsFileText() != settings.googleSheetsDataFile;
		modified |= mySettingsComponent.getGoogleSheetsFileText() != settings.googleSheetsTagFile;
		return modified;
	}

	@Override
	public void apply() {
		AppSettingsState settings = AppSettingsState.getInstance();
		settings.jbossPath = mySettingsComponent.getJbossPathText();
		settings.moduleArtifactId = mySettingsComponent.getModuleArtifactIdText();
		settings.googleKeyFile = mySettingsComponent.getGoogleKeyFileText();
		settings.googleSheetsDataFile = mySettingsComponent.getGoogleSheetsFileText();
		settings.googleSheetsTagFile = mySettingsComponent.getGoogleSheetsTagText();
	}

	@Override
	public void reset() {
		AppSettingsState settings = AppSettingsState.getInstance();
		mySettingsComponent.setJbossPathText(settings.jbossPath);
		mySettingsComponent.setModuleArtifactIdText(settings.moduleArtifactId);
		mySettingsComponent.setGoogleKeyFileText(settings.googleKeyFile);
		mySettingsComponent.setGoogleSheetsFileText(settings.googleSheetsDataFile);
		mySettingsComponent.setGoogleSheetsTagText(settings.googleSheetsTagFile);
	}

	@Override
	public void disposeUIResources() {
		mySettingsComponent = null;
	}
}

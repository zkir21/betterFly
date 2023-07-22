package com.unitedthinkers.fly.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/07/11
 */
@State(
		name = "com.unitedthinkers.fly.settings.AppSettingsState",
		storages = @Storage("FlySettingsPlugin.xml")
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

	public String unipayJbossPath = "/unipay/jboss7";
	public String unipayArtifactId = "unipay-ear";

	public static AppSettingsState getInstance() {
		return ApplicationManager.getApplication().getService(AppSettingsState.class);
	}

	@Nullable
	@Override
	public AppSettingsState getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull AppSettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}

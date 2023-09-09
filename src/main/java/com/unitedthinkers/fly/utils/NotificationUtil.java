package com.unitedthinkers.fly.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.unitedthinkers.fly.settings.AppSettingsConfigurable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/09/03
 */
public class NotificationUtil {

	private static final String NOTIFICATION_STATUS = "Status";
	private static final String NOTIFICATION_GROUP = "Custom Notification Group";

	public enum Messages {
		WRONG_VFS_DIRECTORY("Wrong VFS Directory: %s"),
		NO_VFS_DEPLOYMENT_DIRECTORY_FOUND("No VFS Deployment Directory found"),
		MORE_THEN_ONE_VFS_DEPLOYMENT_DIRECTORIES("There are more then one VFS Deployment Directories"),
		REDEPLOYED_SUCCESSFULLY("%s: redeployed successfully"),
		MODULE_DIRECTORY_NOT_FOUND("Module Directory mot found"),
		CANNOT_COPY_FILE("Cannot copy file: %s"),
		SAVED_SUCCESSFULLY("Saved successfully");

		private final String value;

		Messages(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static void notify(@NotNull AnActionEvent e, String content, NotificationType type) {
		notify(e, content, type, false);
	}

	public static void notify(@NotNull AnActionEvent e, String content, NotificationType type, boolean showSettings) {
		Notification notification = NotificationGroupManager.getInstance()
				.getNotificationGroup(NOTIFICATION_GROUP)
				.createNotification(NOTIFICATION_STATUS, content, type);

		if (showSettings) {
			notification.addAction(new NotificationAction("Settings") {
				@Override
				public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
					ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), AppSettingsConfigurable.class);
					notification.expire();
				}
			});
		}

		notification.notify(e.getProject());
	}
}

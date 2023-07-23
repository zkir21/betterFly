package com.unitedthinkers.fly;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.unitedthinkers.fly.settings.AppSettingsState;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/07/09
 */
public class SaveAction extends AnAction {

	private final static Logger LOG = Logger.getInstance(SaveAction.class);

	private static final String VFS_PATH = "/standalone/tmp/vfs/deployment";
	private static final String POM_XML = "pom.xml";
	private static final String EMPTY = "";
	private static final String NOTIFICATION_STATUS = "Status";
	private static final String NOTIFICATION_GROUP = "Custom Notification Group";
	private static final String SRC_MAIN_JAVA = "/src/main/java/".replaceAll("/", File.separator);
	private static final String SRC_MAIN_JAVA_REPLACE_WITH = "/WEB-INF/classes/".replaceAll("/", File.separator);
	private static final Map<Path, Path> PROCESSED_FILE = new HashMap<>();

	private enum PomTags {
		MODULES ("modules"),
		BUNDLE_FILE_NAME("bundleFileName"),
		ARTIFACT_ID("artifactId"),
		WAR_SOURCE_DIRECTORY("warSourceDirectory"),
		MAVEN_WAR_PLUGIN("maven-war-plugin");

		private final String value;

		PomTags(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private enum Messages {
		WRONG_VFS_DIRECTORY("Wrong VFS Directory: %s"),
		NO_VFS_DEPLOYMENT_DIRECTORY_FOUND("No VFS Deployment Directory found"),
		MORE_THEN_ONE_VFS_DEPLOYMENT_DIRECTORIES("There are more then one VFS Deployment Directories"),
		REDEPLOYED_SUCCESSFULLY("%s: redeployed successfully"),
		MODULE_DIRECTORY_NOT_FOUND("Module Directory mot found"),
		CANNOT_COPY_FILE("Cannot copy file: %s");

		private final String value;

		Messages(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
		Path originalPath = psiFile.getVirtualFile().toNioPath();
		Path copiedPath = PROCESSED_FILE.get(originalPath);

		if (copiedPath == null || !Files.exists(copiedPath)) {
			AppSettingsState settings = AppSettingsState.getInstance();
			File vfsDirectory = new File(getVfsPath(settings.unipayJbossPath));

			File vfsDeploymentDirectory = getVfsDeploymentDirectory(event, vfsDirectory);
			if (vfsDeploymentDirectory == null) {
				return;
			}
			final String artifactId = settings.unipayArtifactId;
			final String basePath = event.getData(CommonDataKeys.PROJECT).getBasePath();
			final String pomFilePath = basePath.concat(File.separator).concat(artifactId).concat(File.separator).concat(POM_XML);

			Map<String, String> moduleInfos = getModulesByArtifactId(pomFilePath);
			ModuleInfo moduleInfo = getFileModuleInfo(psiFile, vfsDeploymentDirectory, moduleInfos);
			Optional<File> moduleDirectory = moduleInfo.getVfsModuleDirectory();

			if (!moduleDirectory.isPresent()) {
				notify(event, Messages.MODULE_DIRECTORY_NOT_FOUND.getValue(), NotificationType.ERROR);
				return;
			}
			copiedPath = Paths.get(moduleDirectory.get().getAbsolutePath() + psiFile.getVirtualFile().getCanonicalPath().replace(moduleInfo.getModuleDirectory(), EMPTY).replace(moduleInfo.getSourceDirectory(), EMPTY).replace(SRC_MAIN_JAVA, SRC_MAIN_JAVA_REPLACE_WITH));
			PROCESSED_FILE.put(originalPath, copiedPath);
		}
		checkFileSaved(psiFile.getVirtualFile());
		try {
			Files.copy(originalPath, copiedPath, StandardCopyOption.REPLACE_EXISTING);
			notify(event, String.format(Messages.REDEPLOYED_SUCCESSFULLY.getValue(), psiFile.getName()), NotificationType.INFORMATION);
		} catch (IOException ex) {
			final String message = String.format(Messages.CANNOT_COPY_FILE.getValue(), originalPath);
			notify(event, message, NotificationType.ERROR);
			LOG.error(message, ex);
		}
	}

	@Nullable
	private static File getVfsDeploymentDirectory(@NotNull AnActionEvent event, File vfsDirectory) {
		if (!vfsDirectory.exists()) {
			notify(event, String.format(Messages.WRONG_VFS_DIRECTORY.getValue(), vfsDirectory.getAbsolutePath()), NotificationType.ERROR);
			return null;
		}
		File[] directories = vfsDirectory.listFiles(File::isDirectory);
		if (directories.length != 1) {
			Messages message;
			if (directories.length == 0) {
				message = Messages.NO_VFS_DEPLOYMENT_DIRECTORY_FOUND;
			} else {
				message = Messages.MORE_THEN_ONE_VFS_DEPLOYMENT_DIRECTORIES;
			}
			notify(event, message.getValue(), NotificationType.ERROR);
			return null;
		}
		return directories[0];
	}

	@Override
	public void update(AnActionEvent event) {
		DataContext dataContext = event.getDataContext();
		PsiFile psiFile = CommonDataKeys.PSI_FILE.getData(dataContext);
		boolean isEnabled = false;
		if (psiFile != null) {
			FileType fileType = psiFile.getFileType();
			isEnabled = isActionEnabledForFileType(fileType);
		} else {
			isEnabled = isActionEnabledForFileType(event.getData(CommonDataKeys.VIRTUAL_FILE).getFileType());
		}
		event.getPresentation().setEnabledAndVisible(isEnabled);
	}

	private boolean isActionEnabledForFileType(FileType fileType) {
		return !fileType.getName().equals("JAVA");
	}

	private static void notify(@NotNull AnActionEvent e, String content, NotificationType type) {
		NotificationGroupManager.getInstance()
				.getNotificationGroup(NOTIFICATION_GROUP)
				.createNotification(NOTIFICATION_STATUS, content, type)
				.notify(e.getProject());
	}

	private static String getVfsPath(String root) {
		if (root.endsWith(File.separator)) {
			root = root.substring(0, root.length() - 1);
		}
		return root + VFS_PATH;
	}

	private static Map<String, String> getModulesByArtifactId(String pomFilePath) {
		Map<String, String> moduleInfos = new HashMap<>();
		MavenXpp3Reader reader = new MavenXpp3Reader();

		try (Reader fileReader = new FileReader(pomFilePath)) {
			Model model = reader.read(fileReader);
			for (Xpp3Dom dom : ((Xpp3Dom) model.getBuild().getPlugins().get(0).getConfiguration()).getChild(PomTags.MODULES.getValue()).getChildren()) {
				moduleInfos.put(((Xpp3Dom) dom.getChild(PomTags.ARTIFACT_ID.getValue())).getValue(), ((Xpp3Dom) dom.getChild(PomTags.BUNDLE_FILE_NAME.getValue())).getValue());
			}
		} catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
		return moduleInfos;
	}

	private static ModuleInfo getFileModuleInfo(PsiFile psiFile, File vfsDeploymentDirectory, Map<String, String> moduleInfos) {
		String artifactId = null;
		String sourceDirectory = null;
		PsiFile pomFile = null;
		Optional<File> vfsModuleDirectory;
		PsiDirectory psiDirectory = psiFile.getParent();

		while (psiDirectory != null) {
			pomFile = psiDirectory.findFile(POM_XML);
			if (pomFile != null) {
				break;
			}
			psiDirectory = psiDirectory.getParent();
		}

		MavenXpp3Reader reader = new MavenXpp3Reader();
		String pomFilePath = pomFile.getVirtualFile().getCanonicalPath();
		
		try (Reader fileReader = new FileReader(pomFilePath)) {
			Model model = reader.read(fileReader);
			artifactId = model.getArtifactId();
			List<Plugin> plugins = model.getBuild().getPlugins();
			Optional<Plugin> warPlugin = plugins.stream()
					.filter(plugin -> PomTags.MAVEN_WAR_PLUGIN.getValue().equals(plugin.getArtifactId()))
					.findFirst();
			if (warPlugin.isPresent()) {
				sourceDirectory = ((Xpp3Dom) warPlugin.get().getConfiguration()).getChild(PomTags.WAR_SOURCE_DIRECTORY.getValue()).getValue();
			}
			String vfsModuleBeginWithName = moduleInfos.get(artifactId);
			vfsModuleDirectory = Stream.of(vfsDeploymentDirectory.listFiles())
					.filter(file -> file.isDirectory() && file.getName().startsWith(vfsModuleBeginWithName))
					.findFirst();
		} catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
		return new ModuleInfo(artifactId, sourceDirectory, psiDirectory.getVirtualFile().getCanonicalPath(), vfsModuleDirectory);
	}

	private static void checkFileSaved(VirtualFile file) {
		FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
		Document document = fileDocumentManager.getDocument(file);
		if (file.isWritable() && document != null && fileDocumentManager.isDocumentUnsaved(document)) {
			fileDocumentManager.saveDocument(document);
		}
	}

}

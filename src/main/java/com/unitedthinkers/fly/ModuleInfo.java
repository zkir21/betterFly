package com.unitedthinkers.fly;

import java.io.File;
import java.util.Optional;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/07/15
 */
public class ModuleInfo {

	private String artifactId;
	private String sourceDirectory;
	private String moduleDirectory;
	private Optional<File> vfsModuleDirectory;

	public ModuleInfo(String artifactId, String sourceDirectory, String moduleDirectory, Optional<File> vfsModuleDirectory) {
		this.artifactId = artifactId;
		this.sourceDirectory = sourceDirectory;
		this.moduleDirectory = moduleDirectory;
		this.vfsModuleDirectory = vfsModuleDirectory;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public String getModuleDirectory() {
		return moduleDirectory;
	}

	public Optional<File> getVfsModuleDirectory() {
		return vfsModuleDirectory;
	}

}

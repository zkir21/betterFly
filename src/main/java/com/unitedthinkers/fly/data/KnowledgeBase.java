package com.unitedthinkers.fly.data;

import java.util.Date;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/20
 */
public class KnowledgeBase {

	private String userName;
	private String task;
	private String tags;
	private String comment;
	private String sourceFile;
	private Date createDate;
	private String sourceCode;

	public KnowledgeBase(String userName, String task, String sourceFile, String sourceCode, String tags) {
		this.userName = userName;
		this.task = task;
		this.sourceFile = sourceFile;
		this.sourceCode = sourceCode;
		this.tags = tags;
		createDate = new Date();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
}

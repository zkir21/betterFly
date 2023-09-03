package com.unitedthinkers.fly;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiFile;
import com.intellij.vcs.log.VcsUser;
import com.unitedthinkers.fly.data.KnowledgeBase;
import com.unitedthinkers.fly.exception.BetterFlyException;
import com.unitedthinkers.fly.utils.NotificationUtil;
import com.unitedthinkers.fly.utils.code_as_html.HtmlHelper;
import com.unitedthinkers.fly.utils.NotificationUtil.Messages;
import com.unitedthinkers.fly.forms.KnowlegeBaseDialog;
import com.unitedthinkers.fly.utils.google.GoogleSheetsLive;
import git4idea.repo.GitRepository;
import java.awt.Container;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/12
 */
public class KnowledgeBaseAction extends AnAction {

	private static final String KNOWLEDGE_BASE = "Knowledge Base";


	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		HtmlHelper htmlHelper = new HtmlHelper();
		try {
			String sourceCode = htmlHelper.getCodeAsHtml(event);

			List<GitRepository> repositories = git4idea.repo.GitRepositoryManager.getInstance(event.getProject()).getRepositories();
			Optional<GitRepository> repository = repositories.stream().filter(r -> r.getCurrentBranch() != null).findFirst();

			String task = null;
			String userName = null;

			if (repository.isPresent()) {
				VcsUser vcsUser = git4idea.GitUserRegistry.getInstance(event.getProject()).getUser(repository.get().getRoot());
				if (vcsUser != null) {
					userName = vcsUser.getName();
				}
				String[] tasksInBranchName = repository.get().getCurrentBranch().getName().split("_");
				if (tasksInBranchName.length > 1) {
					task = tasksInBranchName[tasksInBranchName.length - 1];
				}
			}
			PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

			KnowledgeBase data = new KnowledgeBase(userName, task, psiFile.getName(), sourceCode, getTags(psiFile));

			DialogWrapper wrapper = new KnowlegeBaseDialog(event.getProject(), KNOWLEDGE_BASE, data);

			Container contentPane = wrapper.getContentPane();
			contentPane.revalidate();
			contentPane.repaint();

			if (wrapper.showAndGet()) {
				GoogleSheetsLive.add(data);
				NotificationUtil.notify(event, Messages.SAVED_SUCCESSFULLY.getValue(), NotificationType.INFORMATION);
			}
		} catch (BetterFlyException e) {
			NotificationUtil.notify(event, e.getUiMessage(), NotificationType.ERROR);
		}
	}

	private static String getTags(PsiFile psiFile) {
		return GoogleSheetsLive.getTags().get().entrySet()
				.stream()
				.filter(e -> psiFile.getVirtualFile().getCanonicalPath().replace("\\", "/").contains(e.getKey()))
				.map(e -> e.getValue())
				.collect(Collectors.joining(", "));

	}
}

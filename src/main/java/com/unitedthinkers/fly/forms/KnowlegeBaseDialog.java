package com.unitedthinkers.fly.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.unitedthinkers.fly.data.KnowledgeBase;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLEditorKit;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/08/12
 */
public class KnowlegeBaseDialog extends DialogWrapper {

  public static final String FIELD_CAN_NOT_BE_EMPTY = "Field %s can not be empty";
  private JPanel myContentPane;
  private JTextArea txbComment;
  private JEditorPane pnlHtml;
  private JLabel lblUserName;
  private JTextField txbTask;
  private JTextField txbTags;

  private final KnowledgeBase data;
  private final Project project;

  public KnowlegeBaseDialog(@NotNull final Project project, @NotNull @Nls final String caption, final KnowledgeBase data) {
    super(project, true);
    this.project = project;
    this.data = data;

    setTitle(caption);
    init();
    setupDialog(data);
  }

  private void setupDialog(KnowledgeBase data) {
    HTMLEditorKit kit = new HTMLEditorKit();
    pnlHtml.setEditorKit(kit);
    pnlHtml.setPreferredSize(new Dimension(-1, 100));
    pnlHtml.setText(data.getSourceCode());

    lblUserName.setText(data.getUserName());
    txbTask.setText(data.getTask());
    txbTags.setText(data.getTags());

    txbComment.setBorder(BorderFactory.createCompoundBorder(
            txbComment.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    txbComment.setLineWrap(true);
  }

  @Override
  protected JComponent createCenterPanel() {
    return myContentPane;
  }

  public Project getProject() {
    return project;
  }

  @Override
  @Nullable
  protected ValidationInfo doValidate() {
    if (txbTask.getText().isEmpty()) {
      return new ValidationInfo(String.format(FIELD_CAN_NOT_BE_EMPTY, "Task"), txbTask);
    }
    if (txbComment.getText().isEmpty()) {
      return new ValidationInfo(String.format(FIELD_CAN_NOT_BE_EMPTY, "Comment"), txbComment);
    }
    if (txbTags.getText().isEmpty()) {
      return new ValidationInfo(String.format(FIELD_CAN_NOT_BE_EMPTY, "Tags"), txbTags);
    }
    return null;
  }

  protected void doOKAction() {
    data.setComment(txbComment.getText());
    data.setTags(txbTags.getText());
    data.setTask(txbTask.getText());
    super.doOKAction();
  }

}

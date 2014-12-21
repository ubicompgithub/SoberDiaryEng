package ubicomp.soberdiaryeng.statistic.ui;

import android.content.Context;
import ubicomp.soberdiaryeng.main.ui.EnablePage;

public interface QuestionnaireDialogCaller extends EnablePage {
	public void setQuestionAnimation();
	public void updateSelfHelpCounter();
	public Context getContext();
}

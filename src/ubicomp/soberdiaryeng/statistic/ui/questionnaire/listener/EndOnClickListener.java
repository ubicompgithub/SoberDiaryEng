package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import android.view.View;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;

public class EndOnClickListener extends QuestionnaireOnClickListener {

	public EndOnClickListener(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_END);
		msgBox.closeDialog();
	}

}

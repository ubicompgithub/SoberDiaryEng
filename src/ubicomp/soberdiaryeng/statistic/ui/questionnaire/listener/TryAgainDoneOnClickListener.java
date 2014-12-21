package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import android.view.View;
import ubicomp.soberdiaryeng.main.MainActivity;
import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;
import ubicomp.soberdiaryeng.system.config.PreferenceControl;

public class TryAgainDoneOnClickListener extends QuestionnaireOnClickListener {

	public TryAgainDoneOnClickListener(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_TRYAGAIN);
		seq.add(8);
		msgBox.closeDialog(R.string.try_again_toast);
		PreferenceControl.setUpdateDetection(true);
		MainActivity.getMainActivity().changeTab(0);
	}

}

package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import android.util.Log;
import android.view.View;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;

public class MusicEndOnClickListener extends QuestionnaireOnClickListener {

	
	public MusicEndOnClickListener(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_END);
		Log.d("CONTENT","MUSIC END");
		msgBox.closeDialog();
	}

}

package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.content.CallCheckContent;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;
import android.view.View;

public class HotLineOnClickListener extends QuestionnaireOnClickListener {

	public HotLineOnClickListener(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_HOTLINE);
		seq.add(5);
		String emotion_hot_line = msgBox.getContext().getString(R.string.call_check_help_emotion_hot_line);
		contentSeq.add(new CallCheckContent(msgBox,emotion_hot_line,"0800788995",true));
		contentSeq.get(contentSeq.size()-1).onPush();
	}

}

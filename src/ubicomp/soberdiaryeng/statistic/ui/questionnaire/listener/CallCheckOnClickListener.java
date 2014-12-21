package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.content.CallCheckContent;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;
import android.view.View;

public class CallCheckOnClickListener extends QuestionnaireOnClickListener {

	private String name,phone;
	public CallCheckOnClickListener(QuestionnaireDialog msgBox,String name,String phone) {
		super(msgBox);
		this.name = name;
		this.phone = phone;
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_CALL_CHECK);
		contentSeq.add(new CallCheckContent(msgBox,name,phone));
		contentSeq.get(contentSeq.size()-1).onPush();
	}

}

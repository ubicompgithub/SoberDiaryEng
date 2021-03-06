package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import android.content.Intent;
import android.view.View;
import ubicomp.soberdiaryeng.main.EmotionActivity;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;

public class EmotionDIYOnClickListener extends QuestionnaireOnClickListener {

	public EmotionDIYOnClickListener(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	public void onClick(View v) {
		ClickLog.Log(ClickLogId.STATISTIC_QUESTION_EMOTIONDIY);
		msgBox.closeBoxNull();
		seq.add(2);
		int[] seqInt = new int[seq.size()];
		for (int i=0;i<seqInt.length;++i)
			seqInt[i] = seq.get(i);
		int type = msgBox.getType();
		
		Intent intent = new Intent(msgBox.getContext(),EmotionActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("seq", seqInt);
		msgBox.getContext().startActivity(intent);
	}

}

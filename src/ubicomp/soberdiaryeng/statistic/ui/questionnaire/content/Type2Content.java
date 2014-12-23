package ubicomp.soberdiaryeng.statistic.ui.questionnaire.content;

import ubicomp.soberdiaryeng.data.database.DatabaseControl;
import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener.EmotionDIYOnClickListener;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener.FamilyCallOnClickListener;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener.SelectedListener;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener.SelfOnClickListener;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener.TryAgainDoneOnClickListener;
import ubicomp.soberdiaryeng.system.config.PreferenceControl;

public class Type2Content extends QuestionnaireContent {

	public Type2Content(QuestionnaireDialog msgBox) {
		super(msgBox);
	}

	@Override
	protected void setContent() {
		msgBox.setNextButton("", null);
		seq.clear();
		msgBox.showDialog();
		setHelp(R.string.question_type2_help);
		DatabaseControl db = new DatabaseControl();
		if (db.canTryAgain() && PreferenceControl.questionnaireShowUpdateDetection())
			setSelectItem(R.string.try_again, new SelectedListener(msgBox, new TryAgainDoneOnClickListener(msgBox),
					R.string.next));
		setSelectItem(R.string.self_help, new SelectedListener(msgBox, new SelfOnClickListener(msgBox), R.string.next));
		setSelectItem(R.string.connect_to_family, new SelectedListener(msgBox, new FamilyCallOnClickListener(msgBox),
				R.string.next));
		setSelectItem(R.string.start_emotion_diy_help, new SelectedListener(msgBox, new EmotionDIYOnClickListener(
				msgBox), R.string.next));
		msgBox.showQuestionnaireLayout(true);
	}

}

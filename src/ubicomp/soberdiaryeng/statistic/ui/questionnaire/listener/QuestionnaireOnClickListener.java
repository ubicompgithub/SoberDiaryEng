package ubicomp.soberdiaryeng.statistic.ui.questionnaire.listener;

import java.util.ArrayList;

import ubicomp.soberdiaryeng.statistic.ui.QuestionnaireDialog;
import ubicomp.soberdiaryeng.statistic.ui.questionnaire.content.QuestionnaireContent;
import android.view.View.OnClickListener;

public abstract class QuestionnaireOnClickListener implements OnClickListener {

	protected QuestionnaireDialog msgBox;
	protected ArrayList<Integer>seq;
	protected ArrayList <QuestionnaireContent> contentSeq;
	
	public QuestionnaireOnClickListener (QuestionnaireDialog msgBox){
		this.msgBox = msgBox;
		this.seq = msgBox.getClickSequence();
		this.contentSeq = msgBox.getQuestionSequence();
	}
	
}

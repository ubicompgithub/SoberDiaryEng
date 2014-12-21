package ubicomp.soberdiaryeng.storytelling.ui;

import android.view.View;
import ubicomp.soberdiaryeng.data.structure.TimeValue;

public interface RecorderCallee {
	public View getRecordBox(TimeValue tv, int selected_button);

	public void enableRecordBox(boolean enable);

	public void cleanRecordBox();
}

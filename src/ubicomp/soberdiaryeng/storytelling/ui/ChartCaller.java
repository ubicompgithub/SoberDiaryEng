package ubicomp.soberdiaryeng.storytelling.ui;

import ubicomp.soberdiaryeng.data.structure.TimeValue;

public interface ChartCaller {
	public void setChartType(int type);
	public void closeRecorder();
	public void openRecordBox(TimeValue tv, int selected_button);
}

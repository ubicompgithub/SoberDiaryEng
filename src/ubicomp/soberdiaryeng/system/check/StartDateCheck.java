package ubicomp.soberdiaryeng.system.check;

import java.util.Calendar;

import ubicomp.soberdiaryeng.system.config.PreferenceControl;

public class StartDateCheck {
	public static boolean afterStartDate() {
		Calendar now = Calendar.getInstance();
		Calendar start_date = PreferenceControl.getStartDate();

		return now.after(start_date);
	}
}

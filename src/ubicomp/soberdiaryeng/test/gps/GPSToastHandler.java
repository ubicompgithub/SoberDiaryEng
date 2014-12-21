package ubicomp.soberdiaryeng.test.gps;

import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.main.ui.toast.CustomToastSmall;
import android.os.Handler;
import android.os.Message;

/**
 * Handler for showing toast of the GPS message
 * 
 * @author Stanley Wang
 */
public class GPSToastHandler extends Handler {

	public void handleMessage(Message msg) {
		CustomToastSmall.generateToast(R.string.open_gps);
	}
}

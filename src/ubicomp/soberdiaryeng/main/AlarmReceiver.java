package ubicomp.soberdiaryeng.main;

import ubicomp.soberdiaryeng.system.check.DefaultCheck;
import ubicomp.soberdiaryeng.system.check.LockCheck;
import ubicomp.soberdiaryeng.system.config.Config;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**BraodcastReceiver for receive alarm from the Android system
 * 
 * @author Stanley Wang*/
public class AlarmReceiver extends BroadcastReceiver {

	/**TAG for logcat*/
	private static final String TAG = "ALARM_RECEIVER";
	
	@Override
	/**Receive the alarm message from the Android system*/
	public void onReceive(Context context, Intent intent) {
		if(DefaultCheck.check() || LockCheck.check())
			return;
		
		if (intent.getAction()=="") return;
		if (intent.getAction().equals(Config.ACTION_REGULAR_NOTIFICATION)){
			Log.d(TAG,Config.ACTION_REGULAR_NOTIFICATION);
			Intent a_intent = new Intent(context,AlarmService.class);
			context.startService(a_intent);
		} else if (intent.getAction().equals(Config.ACTION_REGULAR_CHECK)){
			Log.d(TAG,Config.ACTION_REGULAR_CHECK);
			Intent a_intent = new Intent(context,UploadService.class);
			context.startService(a_intent);
		}
	}

}

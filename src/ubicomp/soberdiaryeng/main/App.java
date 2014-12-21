package ubicomp.soberdiaryeng.main;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Common used resources of SoberDiary Application
 * 
 * @author Stanley Wang
 */
public class App extends Application {
	/** Application Context */
	private static Context context;
	/** SharedPreferences */
	private static SharedPreferences sp;

	@Override
	/**Triggered when the Application onCreate*/
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Get Application context
	 * 
	 * @return ApplicationContext
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * Get SharedPreferences (DefaultSharedPreferences)
	 * 
	 * @return DefaultSharedPreferences
	 */
	public static SharedPreferences getSp() {
		return sp;
	}
}

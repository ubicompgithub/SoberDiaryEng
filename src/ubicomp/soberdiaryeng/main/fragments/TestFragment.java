package ubicomp.soberdiaryeng.main.fragments;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

import ubicomp.soberdiaryeng.data.file.MainStorage;
import ubicomp.soberdiaryeng.main.App;
import ubicomp.soberdiaryeng.main.GPSService;
import ubicomp.soberdiaryeng.main.MainActivity;
import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.main.TutorialActivity;
import ubicomp.soberdiaryeng.main.UploadService;
import ubicomp.soberdiaryeng.main.ui.EnablePage;
import ubicomp.soberdiaryeng.main.ui.LoadingDialogControl;
import ubicomp.soberdiaryeng.main.ui.ScaleOnTouchListener;
import ubicomp.soberdiaryeng.main.ui.ScreenSize;
import ubicomp.soberdiaryeng.main.ui.Typefaces;
import ubicomp.soberdiaryeng.main.ui.toast.CustomToastSmall;
import ubicomp.soberdiaryeng.system.check.DefaultCheck;
import ubicomp.soberdiaryeng.system.clicklog.ClickLog;
import ubicomp.soberdiaryeng.system.clicklog.ClickLogId;
import ubicomp.soberdiaryeng.system.config.Config;
import ubicomp.soberdiaryeng.system.config.PreferenceControl;
import ubicomp.soberdiaryeng.test.bluetooth.Bluetooth;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothACVMMode;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothAVMMode;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothCaller;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothDebugger;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothInitHandler;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothMessageUpdater;
import ubicomp.soberdiaryeng.test.bluetooth.BluetoothReadTask;
import ubicomp.soberdiaryeng.test.bluetooth.SimpleBluetooth;
import ubicomp.soberdiaryeng.test.camera.CameraCaller;
import ubicomp.soberdiaryeng.test.camera.CameraInitHandler;
import ubicomp.soberdiaryeng.test.camera.CameraRecorder;
import ubicomp.soberdiaryeng.test.camera.CameraRunHandler;
import ubicomp.soberdiaryeng.test.data.BracDataHandler;
import ubicomp.soberdiaryeng.test.data.BracDataHandlerACVMMode;
import ubicomp.soberdiaryeng.test.data.BracDataHandlerAVMMode;
import ubicomp.soberdiaryeng.test.data.BracValueDebugHandler;
import ubicomp.soberdiaryeng.test.data.BracValueFileHandler;
import ubicomp.soberdiaryeng.test.data.ImageFileHandler;
import ubicomp.soberdiaryeng.test.data.QuestionFile;
import ubicomp.soberdiaryeng.test.gps.GPSInitTask;
import ubicomp.soberdiaryeng.test.gps.GPSInterface;
import ubicomp.soberdiaryeng.test.ui.AdditionalQuestionDialog;
import ubicomp.soberdiaryeng.test.ui.FeedbackDialog;
import ubicomp.soberdiaryeng.test.ui.FeedbackDialogCaller;
import ubicomp.soberdiaryeng.test.ui.NotificationDialog;
import ubicomp.soberdiaryeng.test.ui.NotificationDialogCaller;
import ubicomp.soberdiaryeng.test.ui.TestQuestionCaller;
import ubicomp.soberdiaryeng.test.ui.TestQuestionDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class TestFragment extends Fragment implements GPSInterface, TestQuestionCaller, BluetoothDebugger,
		BluetoothMessageUpdater, BluetoothCaller, CameraCaller, EnablePage, NotificationDialogCaller, FeedbackDialogCaller {

	private static final String TAG = "TEST_PAGE";

	private Activity activity;
	private TestFragment testFragment;
	private View view;
	private TextView messageView;
	private long timestamp = 0;

	private final boolean[] INIT_PROGRESS = { false, false, false };
	private final boolean[] DONE_PROGRESS = { false, false, false };

	private static final long TEST_GAP_DURATION_LONG = Config.TEST_GAP_DURATION_LONG;
	private static final long TEST_GAP_DURATION_SHORT = Config.TEST_GAP_DURATION_SHORT;
	private static final int COUNT_DOWN_SECOND = Config.COUNT_DOWN_SECOND;
	private static final int COUNT_DOWN_SECOND_DEVELOP = Config.COUNT_DOWN_SECOND_DEBUG;

	// GPS
	private LocationManager locationManager;
	private GPSInitTask gpsInitTask;
	private boolean gps_state = false;

	// Bluetooth
	private Bluetooth bt;
	private BluetoothInitHandler btInitHandler;
	private BluetoothReadTask btRunTask;

	// Camera
	private CameraInitHandler cameraInitHandler;
	private CameraRecorder cameraRecorder;
	private CameraRunHandler cameraRunHandler;

	// File
	private File mainDirectory;
	private BracValueFileHandler bracFileHandler;
	private ImageFileHandler imgFileHandler;
	private BracValueDebugHandler bracDebugHandler;

	// Uploader
	private BracDataHandler BDH;

	private RelativeLayout main_layout;
	private TestQuestionDialog msgBox;
	private FeedbackDialog feedbackBox;

	private FailMessageHandler failBgHandler;
	private MsgLoadingHandler msgLoadingHandler;
	private TestHandler testHandler;
	private ChangeTabsHandler changeTabsHandler;

	private ImageView startButton, testCircle;
	private TextView startText;
	private TextView countDownText;
	private ImageView helpButton;

	private static Object init_lock = new Object();
	private static Object done_lock = new Object();

	private ScrollView debugScrollView;
	private EditText debugMsg;
	private ChangeMsgHandler msgHandler;
	private TextView debugBracValueView;

	private static final int[] BLOW_RESOURCE = { 0, R.drawable.test_progress_1, R.drawable.test_progress_2,
			R.drawable.test_progress_3, R.drawable.test_progress_4, R.drawable.test_progress_5,
			R.drawable.test_progress_5 };
	private ImageView face;

	private QuestionFile questionFile;
	private Typeface digitTypefaceBold, wordTypefaceBold;
	private DecimalFormat format;

	private String[] test_guide_msg;

	private static SoundPool soundpool;
	private static int soundId;

	private AdditionalQuestionDialog addBox;

	private NotificationDialog notificationDialog;

	private CountDownTimer testCountDownTimer = null;
	private CountDownTimer openSensorMsgTimer = null;
	private boolean showCountDown = true;
	
	private LinearLayout middleLayout;

	private TextView guideTop, guideBottom;

	private Animation startButtonAnimation = AnimationUtils.loadAnimation(App.getContext(),
			R.anim.animation_start_button);

		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this.getActivity();
		testFragment = this;
		format = new DecimalFormat();
		format.setMaximumIntegerDigits(1);
		format.setMinimumIntegerDigits(1);
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		digitTypefaceBold = Typefaces.getDigitTypeface();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		if (soundpool == null) {
			soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
			soundId = soundpool.load(this.getActivity(), R.raw.short_beep, 1);
		}
		msgLoadingHandler = new MsgLoadingHandler();
		failBgHandler = new FailMessageHandler();
		testHandler = new TestHandler();
		changeTabsHandler = new ChangeTabsHandler();
		test_guide_msg = getResources().getStringArray(R.array.test_guide_msg);
	}

	public static final int STATE_INIT = 0;

	public void setState(int state) {
		switch (state) {
		case STATE_INIT:
			MainActivity.getMainActivity().enableTabAndClick(true);
			testCircle.setImageDrawable(null);
			setGuideMessage(R.string.test_guide_start_top, R.string.test_guide_start_bottom);
			messageView.setText("");
			startButton.setOnClickListener(new StartOnClickListener());
			startButton.setEnabled(true);
			startButton.setVisibility(View.VISIBLE);
			startText.setVisibility(View.VISIBLE);
			startText.setText(R.string.start);
			helpButton.setOnClickListener(new TutorialOnClickListener());
			face.setVisibility(View.INVISIBLE);
			break;
		}
	}

	public void onDestory() {
		if (msgBox != null) {
			msgBox.clear();
			msgBox = null;
		}
		if (feedbackBox != null) {
			feedbackBox.clear();
			feedbackBox = null;
		}
		super.onDestroy();
	}

	public void onPause() {
		ClickLog.Log(ClickLogId.TEST_LEAVE);
		SimpleBluetooth.closeConnection();
		stop();
		if (addBox != null) {
			addBox.clear();
			addBox = null;
		}
		if (notificationDialog != null)
			notificationDialog.clear();
		if (startText != null) {
			startText.setAnimation(null);
			startButtonAnimation.cancel();
		}
		super.onPause();
	}

	public void onResume() {
		super.onResume();
		ClickLog.Log(ClickLogId.TEST_ENTER);
		checkDebug(PreferenceControl.isDebugMode(), PreferenceControl.debugType());
		setState(STATE_INIT);
		showCountDown = true;
		LoadingDialogControl.dismiss();

		// FOR TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Should be deleted
		// PreferenceControl.setShowAdditonalQuestionnaire();
		// addBox = new AdditionalQuestionDialog(main_layout, testFragment);
		// addBox.show();

		// FOR TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Should be restored
		if (PreferenceControl.showAdditionalQuestionnaire()) {
			PreferenceControl.setShowAdditonalQuestionnaire();
			addBox = new AdditionalQuestionDialog(main_layout, testFragment);
			addBox.show();
		} else
			notificationDialog.initialize();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x10) {
			runGPS();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_test, container, false);
		main_layout = (RelativeLayout) view.findViewById(R.id.test_fragment_main_layout);
		startButton = (ImageView) view.findViewById(R.id.test_start_button);
		guideTop = (TextView) view.findViewById(R.id.test_guide_top);
		guideBottom = (TextView) view.findViewById(R.id.test_guide_bottom);
		startText = (TextView) view.findViewById(R.id.test_start_button_text);
		helpButton = (ImageView) view.findViewById(R.id.help_background);
		testCircle = (ImageView) view.findViewById(R.id.test_start_circle);
		face = (ImageView) view.findViewById(R.id.test_face);
		countDownText = (TextView) view.findViewById(R.id.test_start_count_down_text);
		messageView = (TextView) view.findViewById(R.id.test_message);
		debugBracValueView = (TextView) view.findViewById(R.id.debug_brac_value);

		guideTop.setTypeface(wordTypefaceBold);
		guideBottom.setTypeface(wordTypefaceBold);
		startText.setTypeface(wordTypefaceBold);
		countDownText.setTypeface(digitTypefaceBold);
		messageView.setTypeface(wordTypefaceBold);

		debugScrollView = (ScrollView) view.findViewById(R.id.debug_scroll_view);
		debugMsg = (EditText) view.findViewById(R.id.debug_msg);

		middleLayout = (LinearLayout) view.findViewById(R.id.test_bg_middle);
		Point screen = ScreenSize.getScreenSize();
		RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) middleLayout.getLayoutParams();
		param.width = screen.x;
		param.height = screen.x * 262 / 480;
		middleLayout.invalidate();

		helpButton.setOnTouchListener(new ScaleOnTouchListener());

		msgBox = new TestQuestionDialog(testFragment, testFragment, main_layout);
		feedbackBox = new FeedbackDialog(testFragment, main_layout);

		notificationDialog = new NotificationDialog(testFragment.getActivity(), main_layout, testFragment, testFragment);

		return view;
	}

	private void reset() {
		SimpleBluetooth.closeConnection();

		timestamp = System.currentTimeMillis();
		MainActivity.getMainActivity().closeTimers();
		setGuideMessage(R.string.test_guide_reset_top, R.string.test_guide_reset_bottom);

		if (MainActivity.getMainActivity().canUpdate())
			PreferenceControl.setUpdateDetectionTimestamp(timestamp);
		else
			PreferenceControl.setUpdateDetectionTimestamp(0);

		setStorage();
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		cameraRecorder = new CameraRecorder(testFragment, imgFileHandler);

		cameraRunHandler = new CameraRunHandler(cameraRecorder);
		Boolean debug = PreferenceControl.isDebugMode();
		Boolean debug_type = PreferenceControl.debugType();

		prev_drawable_time = -1;
		
		if (debug) {
			if (debug_type)
				bt = new BluetoothAVMMode(testFragment, testFragment, cameraRunHandler, bracFileHandler,
						bracDebugHandler);
			else
				bt = new BluetoothACVMMode(testFragment, testFragment, cameraRunHandler, bracFileHandler,
						bracDebugHandler);
		} else
			bt = new Bluetooth(testFragment, testFragment, cameraRunHandler, bracFileHandler, true);
		for (int i = 0; i < 3; ++i)
			INIT_PROGRESS[i] = DONE_PROGRESS[i] = false;
	}

	// GPSInterface
	@Override
	public void runGPS() {
		if (gps_state) {
			gpsInitTask.cancel(true);
			Log.d(TAG, "GPS: start the service");
			Intent gpsIntent = new Intent(activity, GPSService.class);
			Bundle data = new Bundle();
			data.putString("directory", String.valueOf(timestamp));
			gpsIntent.putExtras(data);
			activity.startService(gpsIntent);
			PreferenceControl.setGPSTime(System.currentTimeMillis(), timestamp);
			updateDoneState(_GPS);
		} else {
			PreferenceControl.resetGPSTime();
			updateDoneState(_GPS);
		}
	}

	@Override
	public void callGPSActivity() {
		Intent gpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(gpsIntent, 0x10);
	}

	@Override
	public void initializeGPS(boolean enable) {
		msgBox.showWaiting();
		if (enable) {
			gps_state = true;
			Object[] gps_enable = { gps_state };
			gpsInitTask = new GPSInitTask(testFragment, locationManager);
			gpsInitTask.execute(gps_enable);
		} else {
			gps_state = false;
			runGPS();
		}
	}

	// TestQuestionBox
	public void writeQuestionFile(int emotion, int craving) {
		questionFile.write(emotion, craving);
	}

	// BluetoothInterface
	public void startBT() {
		// initialize bt task
		btInitHandler = new BluetoothInitHandler(testFragment, bt);
		btInitHandler.sendEmptyMessage(0);
		// initialize camera task
		cameraInitHandler = new CameraInitHandler(testFragment, cameraRecorder);
		cameraInitHandler.sendEmptyMessage(0);
	}

	public void runBT() {
		if (testHandler == null)
			testHandler = new TestHandler();
		testHandler.sendEmptyMessage(0);
	}

	public void failBT() {
		messageView.setText("");
		countDownText.setText("");
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putInt("msg", R.string.test_guide_not_turn_on);
		msg.setData(data);
		msg.what = 0;
		if (failBgHandler != null)
			failBgHandler.sendMessage(msg);
	}

	// Camera
	@Override
	public FrameLayout getPreviewFrameLayout() {
		return (FrameLayout) this.getView().findViewById(R.id.test_camera_preview_layout);
	}

	@Override
	public Point getPreviewSize() {
		int left = startButton.getLeft();
		int right = startButton.getRight();
		int top = startButton.getTop();
		int bottom = startButton.getBottom();
		return new Point(right - left, bottom - top);
	}

	private class StartOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			Point p = getPreviewSize();
			Log.d(TAG, "IMAGE_SIZE=" + p.toString());

			ClickLog.Log(ClickLogId.TEST_START_BUTTON);

			if (!MainActivity.getMainActivity().getClickable())
				return;

			if (DefaultCheck.check()) {
				CustomToastSmall.generateToast(R.string.default_forbidden);
				return;
			}

			helpButton.setOnClickListener(null);
			if (PreferenceControl.isFirstTime()) {
				PreferenceControl.setAfterFirstTime();
				showTutorial();
			} else {

				startText.setAnimation(null);
				startButtonAnimation.cancel();

				long lastTime = PreferenceControl.getLastTestTime();
				long curTime = System.currentTimeMillis();
				Boolean debug = PreferenceControl.isDebugMode();
				boolean testFail = PreferenceControl.isTestFail();
				long TEST_GAP_DURATION = testFail ? TEST_GAP_DURATION_SHORT : TEST_GAP_DURATION_LONG;
				long time = curTime - lastTime;
				if (time > TEST_GAP_DURATION || debug) {
					MainActivity.getMainActivity().closeTimers();
					startButton.setOnClickListener(null);
					startButton.setEnabled(false);
					startText.setVisibility(View.INVISIBLE);
					startText.setText("");
					reset();
					openSensorMsgTimer = new OpenSensorMsgTimer();
					openSensorMsgTimer.start();
				} else
					CustomToastSmall.generateToast(R.string.testTimeCheckToast);
			}
		}
	}

	private class EndTestOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			ClickLog.Log(ClickLogId.TEST_END_BUTTON);
			stopDueToInit();
			setState(STATE_INIT);
			showCountDown = true;
		}
	}

	private void setStorage() {
		File dir = MainStorage.getMainStorageDirectory();

		mainDirectory = new File(dir, String.valueOf(timestamp));
		if (!mainDirectory.exists())
			if (!mainDirectory.mkdirs()) {
				return;
			}

		bracFileHandler = new BracValueFileHandler(mainDirectory, String.valueOf(timestamp));
		bracDebugHandler = new BracValueDebugHandler(mainDirectory, String.valueOf(timestamp));
		imgFileHandler = new ImageFileHandler(mainDirectory, String.valueOf(timestamp));
		questionFile = new QuestionFile(mainDirectory);
	}

	public void updateInitState(int type) {
		synchronized (init_lock) {
			if (INIT_PROGRESS[type] == true)
				return;
			INIT_PROGRESS[type] = true;
			if (INIT_PROGRESS[_BT] && INIT_PROGRESS[_CAMERA]) {
				btInitHandler.removeMessages(0);
				cameraInitHandler.removeMessages(0);
				btRunTask = new BluetoothReadTask(this, bt);
				btRunTask.execute();
				setGuideMessage(R.string.test_guide_init_top, R.string.test_guide_init_bottom);
				showDebug("Device launched");

				if (PreferenceControl.isDebugMode())
					testCountDownTimer = new TestCountDownTimer(COUNT_DOWN_SECOND_DEVELOP);
				else
					testCountDownTimer = new TestCountDownTimer(COUNT_DOWN_SECOND);
				Random rand = new Random();
				int idx = rand.nextInt(test_guide_msg.length);
				messageView.setText(test_guide_msg[idx]);
				testCountDownTimer.start();
			}
		}
	}

	public void updateDoneState(int type) {
		synchronized (done_lock) {
			if (DONE_PROGRESS[type] == true)
				return;
			DONE_PROGRESS[type] = true;

			if (!DONE_PROGRESS[_GPS] && DONE_PROGRESS[_BT] && DONE_PROGRESS[_CAMERA]) {
				stop();
				if (msgLoadingHandler == null)
					msgLoadingHandler = new MsgLoadingHandler();
				Log.d(TAG, "DONE_ALL_PROGRESS");
				msgLoadingHandler.sendEmptyMessage(0);
			}
		}
		if (DONE_PROGRESS[_GPS] && DONE_PROGRESS[_BT] && DONE_PROGRESS[_CAMERA]) {
			if (PreferenceControl.isDebugMode()) {
				if (PreferenceControl.debugType())
					BDH = new BracDataHandlerAVMMode(timestamp);
				else
					BDH = new BracDataHandlerACVMMode(timestamp);
			} else
				BDH = new BracDataHandler(timestamp);
			BDH.start();

			if (!gps_state)
				UploadService.startUploadService(this.activity);

			changeTabsHandler.sendEmptyMessage(0);
		}
	}

	public void stopDueToInit() {
		if (cameraRecorder != null)
			cameraRecorder.close();

		if (bt != null)
			bt = null;
		if (gpsInitTask != null)
			gpsInitTask.cancel(true);
		if (btInitHandler != null)
			btInitHandler.removeMessages(0);
		if (cameraInitHandler != null)
			cameraInitHandler.removeMessages(0);
		if (btRunTask != null)
			btRunTask.cancel(true);

		if (testHandler != null)
			testHandler.removeMessages(0);

		if (testCountDownTimer != null)
			testCountDownTimer.cancel();
		if (openSensorMsgTimer != null)
			openSensorMsgTimer.cancel();
	}

	public void stop() {
		if (cameraRecorder != null)
			cameraRecorder.close();

		if (bt != null)
			bt.closeSuccess();
		if (gpsInitTask != null)
			gpsInitTask.cancel(true);
		if (btInitHandler != null)
			btInitHandler.removeMessages(0);
		if (cameraInitHandler != null)
			cameraInitHandler.removeMessages(0);
		if (btRunTask != null)
			btRunTask.cancel(true);
		if (msgLoadingHandler != null) {
			msgLoadingHandler.removeMessages(0);
		}
		if (testHandler != null) {
			testHandler.removeMessages(0);
		}
		if (failBgHandler != null) {
			failBgHandler.removeMessages(0);
		}
		if (msgHandler != null) {
			msgHandler.removeMessages(0);
		}
		if (changeTabsHandler != null) {
			changeTabsHandler.removeMessages(0);
		}

		if (testCountDownTimer != null) {
			testCountDownTimer.cancel();
			testCountDownTimer = null;
		}
		if (openSensorMsgTimer != null) {
			openSensorMsgTimer.cancel();
			openSensorMsgTimer = null;
		}
	}

	@SuppressLint("HandlerLeak")
	private class MsgLoadingHandler extends Handler {
		public void handleMessage(Message msg) {
			startButton.setOnClickListener(null);
			startButton.setEnabled(false);
			Log.d(TAG, "MsgLoadingHandler");
			if (PreferenceControl.getUpdateDetectionTimestamp() > 0) {
				feedbackBox.initialize();
				feedbackBox.show(true);
			} else {
				msgBox.initialize();
				msgBox.show();
			}
			messageView.setText(R.string.test_guide_msg_box);
		}
	}

	@Override
	public void feedbackToTestQuestionDialog() {
		feedbackBox.close();
		msgBox.initialize();
		msgBox.show();
	}

	@Override
	public void feedbackToFail() {
		feedbackBox.close();
		startButton.setOnClickListener(new EndTestOnClickListener());
	}

	@SuppressLint("HandlerLeak")
	private class FailMessageHandler extends Handler {

		public void handleMessage(Message msg) {
			
			showCountDown = false;
			
			if (msgLoadingHandler != null) {
				msgLoadingHandler.removeMessages(0);
				msgLoadingHandler = null;
			}
			if (testHandler != null) {
				testHandler.removeMessages(0);
				testHandler = null;
			}

			messageView.setText("");
			countDownText.setText("");
			testCircle.setImageResource(BLOW_RESOURCE[0]);

			int msg_str_id = msg.getData().getInt("msg");

			startButton.setOnClickListener(new EndTestOnClickListener());
			startButton.setEnabled(true);
			startButton.setVisibility(View.VISIBLE);
			startText.setVisibility(View.VISIBLE);
			startText.setText(R.string.ok);
			face.setVisibility(View.INVISIBLE);

			if (testCountDownTimer != null)
				testCountDownTimer.cancel();

			setGuideMessage(R.string.test_guide_end, msg_str_id);

			if (PreferenceControl.getUpdateDetectionTimestamp() > 0 && !PreferenceControl.getUpdateDetection()) {
				feedbackBox.initialize();
				feedbackBox.show(false);
				startButton.setOnClickListener(null);
			} else
				MainActivity.getMainActivity().setTimers();

			MainActivity.getMainActivity().enableTabAndClick(true);
		}
	}

	@SuppressLint("HandlerLeak")
	private class TestHandler extends Handler {
		public void handleMessage(Message msg) {
			startButton.setOnClickListener(null);
			startButton.setEnabled(false);

			face.setVisibility(View.VISIBLE);

			setGuideMessage(R.string.test_guide_testing_top, R.string.test_guide_testing_bottom);

			if (bt != null && cameraRecorder != null) {
				bt.start();
				cameraRecorder.start();
			}
		}
	}

	private int prev_drawable_time = -1;
	
	@Override
	public void changeBluetoothCircle(int time) {
		if (time >= BLOW_RESOURCE.length)
			time = BLOW_RESOURCE.length - 1;
		if (prev_drawable_time < time){
			prev_drawable_time = time;
			//testCircle.setBackgroundResource(BLOW_RESOURCE[time]);
			testCircle.setImageResource(BLOW_RESOURCE[time]);
			testCircle.invalidate();
		}
	}
	
	@Override
	public void changeBluetoothValue(float value) {
			debugBracValueView.setText(String.valueOf(value));
			debugBracValueView.invalidate();
	}

	public void stopByFail(int fail) {
		Message msg = new Message();
		Bundle data = new Bundle();
		switch (fail) {
		case 0:
			data.putInt("msg", R.string.test_guide_connect_fail);
			break;
		case 1:
			data.putInt("msg", R.string.test_guide_test_fail);
			break;
		case 2:
			data.putInt("msg", R.string.test_guide_test_timeout);
			break;
		case 3:
			data.putInt("msg", R.string.test_guide_multi_blow);
			break;
		case 4:
			data.putInt("msg", R.string.test_guide_zero_duration);
			break;
		case 5:
			data.putInt("msg", R.string.test_guide_pressure_error);
			break;
		case 6:
			data.putInt("msg", R.string.test_guide_high_initial);
			break;
		default:
			data.putInt("msg", R.string.test_guide_test_timeout);
			break;
		}
		msg.setData(data);
		msg.what = 0;
		if (failBgHandler != null)
			failBgHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	private class ChangeTabsHandler extends Handler {
		public void handleMessage(Message msg) {
			if (msgBox != null)
				msgBox.close();
			MainActivity.getMainActivity().enableTabAndClick(true);
			MainActivity.getMainActivity().changeTab(1);
		}
	}

	private class TutorialOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			if (!MainActivity.getMainActivity().getClickable())
				return;
			ClickLog.Log(ClickLogId.TEST_TUTORIAL_BUTTON);
			showTutorial();
		}
	}

	private void showTutorial() {
		Intent intent = new Intent();
		intent.setClass(activity, TutorialActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void setPairMessage() {
		setGuideMessage(R.string.test_guide_pair_top, R.string.test_guide_pair_bottom);
		messageView.setText("");
		countDownText.setText("");
		startButton.setOnClickListener(new EndTestOnClickListener());
		startButton.setEnabled(true);
		startButton.setVisibility(View.VISIBLE);
		startText.setVisibility(View.VISIBLE);
		startText.setText(R.string.ok);
	}

	@Override
	public void enablePage(boolean enable) {
		startButton.setEnabled(enable);
		helpButton.setEnabled(enable);
	}

	private class TestCountDownTimer extends CountDownTimer {

		private static final int SECOND_FIX = 1300;
		private long prevSecond = 99;

		public TestCountDownTimer(long second) {
			super(second * SECOND_FIX, 100);
		}

		@Override
		public void onFinish() {
			startButton.setVisibility(View.INVISIBLE);
			countDownText.setText("");
			showDebug(">Start to run the  device");
			runBT();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long displaySecond = millisUntilFinished / SECOND_FIX;
			if (displaySecond < prevSecond) {
				soundpool.play(soundId, 0.6f, 0.6f, 0, 0, 1.f);
				prevSecond = displaySecond;
				countDownText.setText(String.valueOf(displaySecond));
			}
		}
	}

	private class OpenSensorMsgTimer extends CountDownTimer {

		public OpenSensorMsgTimer() {
			super(100, 50);
		}

		@Override
		public void onFinish() {
			showDebug(">Try to start the device");
			startBT();
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	}

	public void setGuideMessage(int str_id_top, int str_id_bottom) {
		if (guideTop != null)
			guideTop.setText(str_id_top);
		if (guideBottom != null)
			guideBottom.setText(str_id_bottom);
	}

	public void setStartButtonText(int str_id) {
		startText.setText(str_id);
	}

	public void enableStartButton(boolean enable) {
		startButton.setEnabled(enable);
	}

	@Override
	public void notifyStartButton() {
		startText.startAnimation(startButtonAnimation);
	}

	@Override
	public void notifyAdditionalQuestionnaire() {
		PreferenceControl.setShowAdditonalQuestionnaire();
		addBox = new AdditionalQuestionDialog(main_layout, testFragment);
		addBox.show();
	}
	
	public boolean getShowCountDown(){
		return showCountDown;
	}

	// Debug
	// --------------------------------------------------------------------------------------------------------

	private void checkDebug(boolean debug, boolean debug_type) {
		SimpleBluetooth.closeConnection();
		RelativeLayout debugLayout = (RelativeLayout) view.findViewById(R.id.debugLayout);
		if (debug) {
			debugLayout.setVisibility(View.VISIBLE);
			msgHandler = new ChangeMsgHandler();
			debugMsg.setText("");
			debugMsg.setOnKeyListener(null);
			TextView debugText = (TextView) view.findViewById(R.id.debug_mode_text);

			Button modeButton = (Button) view.findViewById(R.id.debug_mode_change);

			modeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					PreferenceControl.setDebugType(!PreferenceControl.debugType());
					checkDebug(PreferenceControl.isDebugMode(), PreferenceControl.debugType());
				}
			});
			if (!debug_type) {
				modeButton.setText("->avm");
				debugText.setText("Training(acvm)");
			} else {
				modeButton.setText("->acvm");
				debugText.setText("Testing(avm)");
			}
			Button[] conditionButtons = new Button[4];
			conditionButtons[0] = (Button) view.findViewById(R.id.debug_button_1);
			conditionButtons[1] = (Button) view.findViewById(R.id.debug_button_2);
			conditionButtons[2] = (Button) view.findViewById(R.id.debug_button_3);
			conditionButtons[3] = (Button) view.findViewById(R.id.debug_button_4);
			for (int i = 0; i < 4; ++i)
				conditionButtons[i].setOnClickListener(new ConditionOnClickListener(i));

			Button volButton = (Button) view.findViewById(R.id.debug_voltage);
			volButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SimpleBluetooth.showVoltage(testFragment);
				}
			});
			TextView vol_tv = (TextView) view.findViewById(R.id.debug_voltage_value);
			vol_tv.setText("NULL");

		} else {
			debugLayout.setVisibility(View.INVISIBLE);
			return;
		}

	}

	private class ConditionOnClickListener implements View.OnClickListener {

		private int cond;

		public ConditionOnClickListener(int cond) {
			this.cond = cond;
		}

		@Override
		public void onClick(View v) {
			PreferenceControl.setTestResult(cond);
			PreferenceControl.setLatestTestCompleteTime(System.currentTimeMillis());
			MainActivity.getMainActivity().changeTab(1);
		}
	}

	public void showDebugVoltage(String message) {
		TextView vol_tv = (TextView) view.findViewById(R.id.debug_voltage_value);
		vol_tv.setText(message);
		vol_tv.invalidate();
	}

	public void showDebug(String message, int type) {
		Boolean debug = PreferenceControl.isDebugMode();
		if (debug && msgHandler != null) {
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putInt("type", type);
			data.putString("message", message);
			msg.setData(data);
			msg.what = 0;
			msgHandler.sendMessage(msg);
		}
	}

	public void showDebug(String message) {
		showDebug(message, 0);
	}

	@SuppressLint("HandlerLeak")
	private class ChangeMsgHandler extends Handler {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			int type = data.getInt("type");
			if (type == 0) {
				debugMsg.append("\n" + data.getString("message"));
				debugScrollView.scrollTo(0, debugMsg.getBottom() + 100);
				debugMsg.invalidate();
			} else if (type == 1) {
				showDebugVoltage(data.getString("message"));
			}
		}
	}

}

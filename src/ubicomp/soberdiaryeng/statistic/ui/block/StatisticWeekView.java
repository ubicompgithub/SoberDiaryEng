package ubicomp.soberdiaryeng.statistic.ui.block;


import java.util.Calendar;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import ubicomp.soberdiaryeng.data.database.DatabaseControl;
import ubicomp.soberdiaryeng.data.structure.Detection;
import ubicomp.soberdiaryeng.main.App;
import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.main.ui.Typefaces;
import ubicomp.soberdiaryeng.system.check.TimeBlock;
import ubicomp.soberdiaryeng.system.config.PreferenceControl;

public class StatisticWeekView extends StatisticPageView {

	private DatabaseControl db;
	private TextView[] time_labels;
	private TextView[] date_labels;
	private Drawable[] circleDrawables;
	private ImageView[] circles;

	private LinearLayout dateLayout;
	private LinearLayout timeLayout;
	private GridLayout blockLayout;
	private LinearLayout labelLayout;
	
	private TextView[] labels;
	private ImageView[] labelImgs;
	
	private TextView title;
	
	private static final int nBlocks = 3;
	private static final int nDays = 7;
	
	private static final int[] blockHint = {R.string.morning_short,R.string.noon_short,R.string.night_short};
	private static final int[] labelHint = {R.string.test_pass,R.string.test_fail,R.string.test_none}; 
	
	private Typeface digitTypefaceBold;
	private Typeface wordTypefaceBold;
	private Typeface wordTypeface;
	
	private Calendar startDate;
	
	private static final int text_color = App.getContext().getResources().getColor(R.color.text_gray);
	private static final float ALPHA = 0.4F;
	
	public StatisticWeekView() {
		super(R.layout.statistic_week_view);
		db = new DatabaseControl();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		wordTypeface = Typefaces.getWordTypeface();
		startDate = PreferenceControl.getStartDate();
		
		dateLayout = (LinearLayout) view.findViewById(R.id.statistic_week_date_label_layout);
		timeLayout = (LinearLayout) view.findViewById(R.id.statistic_week_timeblock_label_layout);
		blockLayout = (GridLayout) view.findViewById(R.id.statistic_week_block_layout);
		
		title= (TextView) view.findViewById(R.id.statistic_week_title);
		title.setTypeface(wordTypefaceBold);
		
		labelLayout = (LinearLayout) view.findViewById(R.id.statistic_week_label_layout);
		
		circleDrawables = new Drawable[3];
		circleDrawables[0] = context.getResources().getDrawable(R.drawable.statistic_week_none);
		circleDrawables[1] = context.getResources().getDrawable(R.drawable.statistic_week_fail);
		circleDrawables[2] = context.getResources().getDrawable(R.drawable.statistic_week_pass);
	}


	@Override
	public void clear() {
	}
	
	@Override
	public void load() {
		int textSize1 = (int) App.getContext().getResources().getDimensionPixelSize(R.dimen.n_text_size);
		int textSize2 = (int) App.getContext().getResources().getDimensionPixelSize(R.dimen.normal_text_size);
		time_labels = new TextView[nBlocks];
		
		for (int i=0;i<nBlocks;++i){
			time_labels[i] = new TextView(context);
			time_labels[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize1);
			time_labels[i].setTextColor(text_color);
			time_labels[i].setSingleLine(true); // FOR TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// time_labels[i].setPadding(-20, -10, 10, -10); // FOR TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// time_labels[i].setFadingEdgeLength(50); // FOR TEST!!!!!!!!!!!!!!!!!!!!!!!!!
			// time_labels[i].setMinEms(500); // FOR TEST!!!!!!!!!!!!!!!
			// time_labels[i].setWidth(500);  // FOR TEST!!!!!!!!!!!!!!!
			time_labels[i].setText(blockHint[i]);
			time_labels[i].setTypeface(wordTypeface);
			time_labels[i].setGravity(Gravity.CENTER);
			timeLayout.addView(time_labels[i]);
		}

		
		date_labels = new TextView[nDays];
		for (int i=0;i<nDays;++i){
			date_labels[i] = new TextView(context);
			date_labels[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize2);
			date_labels[i].setTextColor(text_color);
			date_labels[i].setGravity(Gravity.CENTER);
			date_labels[i].setTypeface(digitTypefaceBold);
			dateLayout.addView(date_labels[i]);
		}
		
		circles = new ImageView[nBlocks*nDays];
		
		for (int i=0;i<nBlocks*nDays;++i){
			circles[i] = new ImageView(context);
			blockLayout.addView(circles[i]);
			circles[i].setScaleType(ScaleType.CENTER);
		}
		
		// Layout of "Pass", "Not Pass" and "Incomplete"
		labels = new TextView[3];
		labelImgs = new ImageView[3];
		for (int i=0;i<3;++i){
			labelImgs[i] = new ImageView(context);
			labelImgs[i].setScaleType(ScaleType.CENTER);
			labelLayout.addView(labelImgs[i]);
			labels[i] = new TextView(context);
			labels[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize2);
			labels[i].setTextColor(text_color);
			labels[i].setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			labels[i].setTypeface(wordTypefaceBold);
			labels[i].setText(labelHint[i]);
			labelLayout.addView(labels[i]);
		}

		int c_width =  context.getResources().getDimensionPixelSize(R.dimen.week_circle_width);
		int c_height =  context.getResources().getDimensionPixelSize(R.dimen.week_circle_height);
		
		for (int i=0;i<nBlocks;++i){
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) time_labels[i].getLayoutParams();
			param.width = c_width;
			param.height = c_height;
		}
		
		for (int i=0;i<nDays;++i){
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) date_labels[i].getLayoutParams();
			param.width = c_width;
			param.height = c_height;
		}
		
		for (int i=0;i<nBlocks*nDays;++i){
			GridLayout.LayoutParams cParam = (GridLayout.LayoutParams) circles[i].getLayoutParams();
			cParam.width = c_width;
			cParam.height = c_height;
		}
		
		for (int i=0;i<3;++i){
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) labels[i].getLayoutParams();
			param.width = c_width*3/2;
			param.height = c_height;
			param = (LinearLayout.LayoutParams) labelImgs[i].getLayoutParams();
			param.width = c_width*3/4;
			param.height = c_height;
		}
		
		Float[] bracs = db.getMultiDaysPrimeBrac(nDays);
		
		int cur_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		
		for (int i=0;i<bracs.length;++i){
			int idx = (i%nBlocks)*nDays + i/nBlocks;
			if (bracs[i] == null){
				circles[idx].setImageDrawable(circleDrawables[0]);
				if (i >= bracs.length - nBlocks && TimeBlock.isEmpty(i%nBlocks, cur_hour))
						circles[idx].setAlpha(ALPHA);
			}
			else if (bracs[i] < Detection.BRAC_THRESHOLD)
				circles[idx].setImageDrawable(circleDrawables[2]);
			else
				circles[idx].setImageDrawable(circleDrawables[1]);
		}
		
		Calendar cal = Calendar.getInstance();
		for (int i=6;i>0;--i){
			int date = cal.get(Calendar.DAY_OF_MONTH);
			String label = String.valueOf(date);
			date_labels[i].setText(label);
			if (cal.before(startDate)){
				int max = i+21;
				for (int j=i;j<max;j+=7){
					circles[j].setAlpha(ALPHA);
				}
			}
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		if (cal.before(startDate)){
			for (int j=0;j<21;j+=7){
				circles[j].setAlpha(ALPHA);
			}
		}
		
		int month = cal.get(Calendar.MONTH)+1;
		int date = cal.get(Calendar.DAY_OF_MONTH);
		String month_label = month+"/"+date;
		date_labels[0].setText(month_label);
		
		labelImgs[0].setImageDrawable(circleDrawables[2]);
		labelImgs[1].setImageDrawable(circleDrawables[1]);
		labelImgs[2].setImageDrawable(circleDrawables[0]);
		
	}


	@Override
	public void onCancel() {
		clear();
	}
	
	
}

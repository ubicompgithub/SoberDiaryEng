package ubicomp.soberdiaryeng.storytelling.ui;

import ubicomp.soberdiaryeng.main.App;
import ubicomp.soberdiaryeng.main.R;
import ubicomp.soberdiaryeng.main.ui.Typefaces;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class ChartLabelView extends View {

	private Paint emotion_paint = new Paint();
	private Paint desire_paint = new Paint();
	private Paint brac_paint = new Paint();
	private Paint text_paint = new Paint();
	private Paint paint_pass = new Paint();
	private Paint paint_fail = new Paint();

	private String[] type_str = new String[3];
	private String[] pass_str = new String[2];
	
	private int textSize = App.getContext().getResources().getDimensionPixelSize(R.dimen.sn_text_size);
	private int base = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_label_base);
	private int gap = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_label_gap);
	private int top = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_label_top);
	private int top2 = App.getContext().getResources().getDimensionPixelSize(R.dimen.chart_label_top2);
	
	private int chart_type = 0;
	
	private static final int text_color = App.getContext().getResources().getColor(R.color.chart_text);
	private static final int green = App.getContext().getResources().getColor(R.color.green);
	private static final int orange = App.getContext().getResources().getColor(R.color.lite_orange);
	private static final int white = App.getContext().getResources().getColor(R.color.white);
	
	public ChartLabelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		text_paint.setColor(text_color );
		text_paint.setTextAlign(Align.CENTER);
		text_paint.setTextSize(textSize);
		text_paint.setTypeface(Typefaces.getWordTypefaceBold());

		emotion_paint.setColor(green);
		desire_paint.setColor(orange);
		brac_paint.setColor(white);

		paint_pass.setColor(green);
		paint_fail.setColor(orange);

		type_str[0] = getResources().getString(R.string.emotion_short);
		type_str[1] = getResources().getString(R.string.craving_short);
		type_str[2] = getResources().getString(R.string.brac_result_short);
		pass_str[0] = getResources().getString(R.string.test_pass);
		pass_str[1] = getResources().getString(R.string.test_fail);
	}

	public void setChartType(int type){
		chart_type = type;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int from = 0;
		int gap1 = gap*27/3;
		int gap2 = gap;
		int line_len = base * 2;                                             // Base means the side length of the square (13dp)
		if (chart_type == 3) {
			canvas.drawRect(from, top, from + base, base + top, emotion_paint);  // Draw a square with 13dp side length
			from += base + gap1;
			canvas.drawText(type_str[0], from, top2, text_paint);
			from += line_len + gap2;
			
			canvas.drawRect(from, top, from + base, base + top, desire_paint);
			from += base + gap1*0.9;
			canvas.drawText(type_str[1], from, top2, text_paint);
			from += line_len + gap2;
			
			canvas.drawRect(from, top, from + base, base + top, brac_paint);
			from += base + gap1*0.7;
			canvas.drawText(type_str[2], from, top2, text_paint);
		} else {
			from += gap*58/3;

			canvas.drawRect(from, top, from + base, base + top, paint_pass);
			from += base + gap1*0.7;
			canvas.drawText(pass_str[0], from, top2, text_paint);
			from += line_len*0.9 + gap2;

			canvas.drawRect(from, top, from + base, base + top, paint_fail);
			from += base + gap1*1.1;
			canvas.drawText(pass_str[1], from, top2, text_paint);
		}
	}
}

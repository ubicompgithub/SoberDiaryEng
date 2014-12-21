package ubicomp.soberdiaryeng.main.ui;

import ubicomp.soberdiaryeng.main.App;
import android.graphics.Typeface;

/**
 * Class for loading custom typefaces
 * 
 * @author Stanley Wang
 */
public class Typefaces {
	private static Typeface wordTypeface, digitTypeface, wordTypefaceBold, wordTypefaceBold2, digitTypefaceBold;    // Reserve wordTypefaceBold2 temporaily!!!!!!!!!!!!!!!!!!!!!!!

	/**
	 * Get typeface for nomal digits
	 * 
	 * @return Typeface for normal digits
	 */
	public static Typeface getDigitTypeface() {
		if (digitTypeface == null)
			digitTypeface = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/dinproregular.ttf");
		return digitTypeface;
	}

	/**
	 * Get typeface for bold digits
	 * 
	 * @return Typeface for bold digits
	 */
	public static Typeface getDigitTypefaceBold() {
		if (digitTypefaceBold == null)
			digitTypefaceBold = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/dinpromedium.ttf");
		return digitTypefaceBold;
	}

	/**
	 * Get typeface for nomal words
	 * 
	 * @return Typeface for normal words
	 */
	public static Typeface getWordTypeface() {
		if (wordTypeface == null)
			wordTypeface = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/dinproregular.ttf");
		return wordTypeface;
	}

	/**
	 * Get typeface for bold words
	 * 
	 * @return Typeface for bold words
	 */
	public static Typeface getWordTypefaceBold() {
		if (wordTypefaceBold == null)
			wordTypefaceBold = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/dinpromedium.ttf");
		return wordTypefaceBold;
	}

	/**
	 * Get typeface for bolder words
	 * 
	 * @return Typeface for bolder words
	 */
	public static Typeface getWordTypefaceBold2() {    // Reserve it temporaily!!!!!!!!!!!!!!!!!!!!!!!
		if (wordTypefaceBold2 == null)
			wordTypefaceBold2 = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/dinprobold.ttf");
		return wordTypefaceBold2;
	}

	/** For initializing all the typefaces */
	public static void initAll() {
		getDigitTypeface();
		getDigitTypefaceBold();
		getWordTypeface();
		getWordTypefaceBold();
		getWordTypefaceBold2();  // Reserve it temporaily!!!!!!!!!!!!!!!!!!!!!!!
	}
}

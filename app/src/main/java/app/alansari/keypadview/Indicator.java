package app.alansari.keypadview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Parveen Dala on 06 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class Indicator extends LinearLayout {

    private Dot[] dots;
    private int number = 0;

    public Indicator(Context context) {
        super(context);
    }

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPasswordLength(int length) {
        removeAllViews();
        dots = new Dot[length];

        for (int i = 0; i < length; i++) {
            Dot view = new Dot(getContext());

            view.setBackgroundResource(app.alansari.R.drawable.indicator_background);

            LayoutParams params = new LayoutParams(
                    30,
                    30
            );
            params.setMargins(20, 10, 20, 10);
            addView(view, params);
            dots[i] = view;
        }
    }

    /**
     * Add a dot.
     */
    public void add() {
        if (number == dots.length) return;
        dots[number++].setSelected(true);
    }

    /**
     * Delete a dot.
     */
    public void delete() {
        if (number == 0) return;
        dots[--number].setSelected(false);
    }

    /**
     * Clear all dots.
     */
    public void clear() {
        number = 0;
        for (int i = 0; i < dots.length; i++) dots[i].setSelected(false);
    }

}

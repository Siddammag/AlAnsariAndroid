package app.alansari.Utils;

import app.alansari.preferences.SharedPreferenceManger;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by Parveen Dala on 22 May, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TapTargetViewUtils {

    public static TapTarget getTargetView(View view, String title, String text) {
        return TapTarget.forView(view, title, text)
                .outerCircleColor(app.alansari.R.color.colorBlack)
                .outerCircleAlpha(0.84f)
                .targetCircleColor(app.alansari.R.color.colorBlack)
                .titleTextSize(18)
                .titleTextColor(app.alansari.R.color.colorWhite)
                .descriptionTextSize(12)
                .descriptionTextColor(app.alansari.R.color.colorWhite_220)
                .textTypeface(Typeface.SANS_SERIF)
                .dimColor(app.alansari.R.color.colorBlack)
                .drawShadow(true)
                .cancelable(false)
                .tintTarget(false)
                .transparentTarget(true)
                .targetRadius(60);
    }

    public static TapTargetSequence.Listener getTapTargetListener(final Context context, final String tag) {
        return new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                setTutStatus(context, tag);
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        };
    }

    public static Boolean getTutStatus(Context context, String key) {
        return (Boolean) SharedPreferenceManger.getPrefVal(key, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setTutStatus(Context context, String key) {
        SharedPreferenceManger.setPrefVal(key, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }
}
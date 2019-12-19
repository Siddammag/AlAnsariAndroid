package app.alansari.keypadview;

import app.alansari.keypadview.Eases.EaseType;
import android.view.animation.Interpolator;


/**
 * Created by Parveen Dala on 06 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class InterpolatorFactory {

    public static BLVInterpolator getInterpolator(EaseType easeType) {
        return new BLVInterpolator(easeType);
    }

    public static class BLVInterpolator implements Interpolator {

        private EaseType easeType;

        public BLVInterpolator(EaseType easeType) {
            this.easeType = easeType;
        }

        @Override
        public float getInterpolation(float input) {
            return easeType.getOffset(input);
        }
    }

}

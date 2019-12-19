package app.alansari.textdrawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Parveen Dala on 05 April, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class PrefixTextDrawable extends Drawable {


    private final String text;
    private final Paint paint;

    public PrefixTextDrawable(String text, int color, float size) {
        this.text = text;
        this.paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, 18, 18, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

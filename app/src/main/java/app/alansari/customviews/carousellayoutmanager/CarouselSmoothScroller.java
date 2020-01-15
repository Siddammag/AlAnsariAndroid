package app.alansari.customviews.carousellayoutmanager;

import android.content.Context;
import androidx.recyclerview.widget.LinearSmoothScroller;
import android.view.View;


/**
 * Custom implementation of {@link androidx.recyclerview.widget.RecyclerView.SmoothScroller} that can work only with {@link CarouselLayoutManager}.
 *
 * @see CarouselLayoutManager
 */
public abstract class CarouselSmoothScroller extends LinearSmoothScroller {

    protected CarouselSmoothScroller(final Context context) {
        super(context);
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public int calculateDyToMakeVisible(final View view, final int snapPreference) {
        final CarouselLayoutManager layoutManager = (CarouselLayoutManager) getLayoutManager();
        if (null == layoutManager || !layoutManager.canScrollVertically()) {
            return 0;
        }

        return layoutManager.getOffsetForCurrentView(view);
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public int calculateDxToMakeVisible(final View view, final int snapPreference) {
        final CarouselLayoutManager layoutManager = (CarouselLayoutManager) getLayoutManager();
        if (null == layoutManager || !layoutManager.canScrollHorizontally()) {
            return 0;
        }
        return layoutManager.getOffsetForCurrentView(view);
    }
}
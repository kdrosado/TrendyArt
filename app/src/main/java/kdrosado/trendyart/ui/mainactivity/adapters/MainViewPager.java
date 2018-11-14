package kdrosado.trendyart.ui.mainactivity.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager that has disabled swipe functionality for the Bottom Navigation implementation
 * tutorial: https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f
 */
public class MainViewPager extends ViewPager {

    private boolean mEnabled;


    public MainViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mEnabled) {
            return super.onTouchEvent(ev);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mEnabled) {
            return super.onInterceptTouchEvent(ev);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        mEnabled = enabled;
    }
}

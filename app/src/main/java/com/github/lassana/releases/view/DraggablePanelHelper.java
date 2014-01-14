package com.github.lassana.releases.view;

import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author lassana
 * @since 1/14/14
 */
@SuppressWarnings("ConstantConditions")
public abstract class DraggablePanelHelper {

    private DraggablePanelHelper() {
    }

    public static final int DURATION_SHORT
            = Resources.getSystem().getInteger(android.R.integer.config_shortAnimTime);
    public static final int DURATION_MEDIUM
            = Resources.getSystem().getInteger(android.R.integer.config_mediumAnimTime);
    public static final int DURATION_LONG
            = Resources.getSystem().getInteger(android.R.integer.config_longAnimTime);

    public static void enableInternalScrolling(final ViewGroup viewGroup) {
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewGroup.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        viewGroup.requestDisallowInterceptTouchEvent(false);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}

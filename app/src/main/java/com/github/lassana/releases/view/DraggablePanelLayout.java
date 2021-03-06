package com.github.lassana.releases.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.github.lassana.releases.R;

/**
 * @author lassana
 * @since 1/13/14
 */
@SuppressWarnings("ConstantConditions")
public class DraggablePanelLayout extends FrameLayout
        implements View.OnTouchListener, DraggablePanel {

    private static final float PARALLAX_FACTOR = 0.2f;
    public static final int DEFAULT_PEEK_HEIGHT = 100;
    public static final int AUTO_SCROLLING_DURATION = DraggablePanelHelper.DURATION_MEDIUM;

    private static DecelerateInterpolator sDecelerator = new DecelerateInterpolator();

    private float mParallaxFactor;

    private DraggablePanelListener mPanelListener;

    private View mBottomPanel;
    private View mSlidingPanel;

    private int mBottomPanelPeekHeight;
    private Drawable mShadowDrawable;
    private boolean mPreventTouchEvents;

    private boolean mIsSlidingPanelOpened = false;

    private float mTouchY;
    private boolean mTouching;
    private VelocityTracker velocityTracker;
    private boolean mAnimating = false;
    private boolean mWillDrawShadow = false;
    private int mTouchSlop;
    private boolean mIsBeingDragged = false;

    public DraggablePanelLayout(Context context) {
        super(context);

        mBottomPanelPeekHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PEEK_HEIGHT, getResources().getDisplayMetrics());
        mParallaxFactor = PARALLAX_FACTOR;

        if (!isInEditMode()) {
            mShadowDrawable = getResources().getDrawable(R.drawable.shadow_np);
            mWillDrawShadow = true;
        }
        mPreventTouchEvents = false;

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public DraggablePanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public DraggablePanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    protected final void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DraggablePanelLayout, 0, 0);

        try {
            mParallaxFactor = array.getFloat(R.styleable.DraggablePanelLayout_parallax_factor, PARALLAX_FACTOR);
            if (mParallaxFactor < 0.1 || mParallaxFactor > 0.9) {
                mParallaxFactor = PARALLAX_FACTOR;
            }

            int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PEEK_HEIGHT, getResources().getDisplayMetrics());
            mBottomPanelPeekHeight = array.getDimensionPixelSize(R.styleable.DraggablePanelLayout_bottom_panel_height, defaultHeight);
            int shadowDrawableId = array.getResourceId(R.styleable.DraggablePanelLayout_shadow_drawable, -1);
            if (shadowDrawableId != -1) {
                mShadowDrawable = getResources().getDrawable(shadowDrawableId);
                mWillDrawShadow = true;
                setWillNotDraw(!mWillDrawShadow);
            }

            mPreventTouchEvents = array.getBoolean(R.styleable.DraggablePanelLayout_prevent_touch_events, false);
        } finally {
            array.recycle();
        }

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof DraggablePanelSavedState) {
            DraggablePanelSavedState savedState = (DraggablePanelSavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            mIsSlidingPanelOpened = savedState.isOpened;
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        DraggablePanelSavedState savedState = new DraggablePanelSavedState(super.onSaveInstanceState());
        savedState.isOpened = mIsSlidingPanelOpened;
        return savedState;
    }

    @Override
    public void setPanelListener(DraggablePanelListener panelListener) {
        mPanelListener = panelListener;
    }

    @Override
    public boolean isSlidingPanelOpened() {
        return mIsSlidingPanelOpened;
    }

    @Override
    public boolean isBottomPanelOpened() {
        return !mIsSlidingPanelOpened;
    }

    @Override
    public void switchState() {
        if ( mAnimating ) return;
        if (mIsSlidingPanelOpened) {
            openBottomPanel();
        } else {
            closeBottomPanel();
        }
    }

    @Override
    public void closeBottomPanel() {
        if ( mAnimating ) return;
        animatePanel(true, calculateDistance(true), AUTO_SCROLLING_DURATION);
    }

    @Override
    public void openBottomPanel() {
        if ( mAnimating ) return;
        mBottomPanel.setVisibility(View.VISIBLE);
        mBottomPanel.setTranslationY(-(getMeasuredHeight() - mBottomPanelPeekHeight) * mParallaxFactor);
        allowShadow();
        animatePanel(false, calculateDistance(false), AUTO_SCROLLING_DURATION);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!isInEditMode() && mWillDrawShadow) {
            int top = (int) (mSlidingPanel.getTop() + mSlidingPanel.getTranslationY());
            mShadowDrawable.setBounds(0, top - mShadowDrawable.getIntrinsicHeight(), getMeasuredWidth(), top);
            mShadowDrawable.draw(canvas);

        }
        if (mAnimating) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() != 2) {
            throw new IllegalStateException("DraggedPanelLayout must have 2 children!");
        }

        mBottomPanel = getChildAt(0);
        mBottomPanel.layout(left, top, right, bottom - mBottomPanelPeekHeight);

        mSlidingPanel = getChildAt(1);
        if (!mIsSlidingPanelOpened) {
            int panelMeasuredHeight = mSlidingPanel.getMeasuredHeight();
            mSlidingPanel.layout(
                    left,
                    bottom - mBottomPanelPeekHeight,
                    right,
                    bottom - mBottomPanelPeekHeight + panelMeasuredHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mPreventTouchEvents) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mTouchY - event.getY()) > mTouchSlop) {
                    mIsBeingDragged = true;
                    startDragging(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
            default:
                return super.onInterceptTouchEvent(event);
        }
        return mIsBeingDragged;
    }

    protected void startDragging(MotionEvent event) {
        mTouchY = event.getY();
        mTouching = true;

        mBottomPanel.setVisibility(View.VISIBLE);

        obtainVelocityTracker();
        velocityTracker.addMovement(event);
        allowShadow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startDragging(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mTouching) {
                    velocityTracker.addMovement(event);

                    float translation = event.getY() - mTouchY;
                    translation = boundTranslation(translation);

                    mSlidingPanel.setTranslationY(translation);
                    mBottomPanel.setTranslationY(mIsSlidingPanelOpened
                            ? -(getMeasuredHeight() - mBottomPanelPeekHeight - translation) * mParallaxFactor
                            : translation * mParallaxFactor);

                    if (mWillDrawShadow) {
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mIsBeingDragged = false;
                mTouching = false;

                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1);
                float velocityY = velocityTracker.getYVelocity();
                velocityTracker.recycle();
                velocityTracker = null;

                finishAnimateToFinalPosition(velocityY);
                break;
            }
        }
        return true;
    }

    protected float boundTranslation(float translation) {
        if (!mIsSlidingPanelOpened) {
            if (translation > 0) {
                translation = 0;
            }
            if (Math.abs(translation) >= mSlidingPanel.getMeasuredHeight() - mBottomPanelPeekHeight) {
                translation = -mSlidingPanel.getMeasuredHeight() + mBottomPanelPeekHeight;
            }
        } else {
            if (translation < 0) {
                translation = 0;
            }
            if (translation >= mSlidingPanel.getMeasuredHeight() - mBottomPanelPeekHeight) {
                translation = mSlidingPanel.getMeasuredHeight() - mBottomPanelPeekHeight;
            }
        }
        return translation;
    }

    protected void obtainVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    protected void finishAnimateToFinalPosition(float velocityY) {
        final boolean flinging = Math.abs(velocityY) > 0.5;

        boolean opening;
        float distY;
        long duration;

        if (flinging) {
            // If fling velocity is fast enough we continue the motion starting
            // with the current speed

            opening = velocityY < 0;

            distY = calculateDistance(opening);
            duration = Math.abs(Math.round(distY / velocityY));

            animatePanel(opening, distY, duration);
        } else {
            // If user motion is slow or stopped we check if half distance is
            // traveled and based on that complete the motion

            boolean halfway = Math.abs(mSlidingPanel.getTranslationY()) >= (getMeasuredHeight() - mBottomPanelPeekHeight) / 2;
            opening = mIsSlidingPanelOpened ? !halfway : halfway;

            distY = calculateDistance(opening);
            duration = Math.round(AUTO_SCROLLING_DURATION * Math.abs((double) mSlidingPanel.getTranslationY()) / (double) (getMeasuredHeight() - mBottomPanelPeekHeight));
        }

        animatePanel(opening, distY, duration);
    }

    protected float calculateDistance(boolean opening) {
        float distY;
        if (mIsSlidingPanelOpened) {
            distY = opening
                    ? -mSlidingPanel.getTranslationY()
                    : getMeasuredHeight() - mBottomPanelPeekHeight - mSlidingPanel.getTranslationY();
        } else {
            distY = opening
                    ? -(getMeasuredHeight() - mBottomPanelPeekHeight + mSlidingPanel.getTranslationY())
                    : -mSlidingPanel.getTranslationY();
        }

        return distY;
    }

    protected void animatePanel(final boolean opening, float distY, long duration) {
        ObjectAnimator slidingPanelAnimator = ObjectAnimator.ofFloat(mSlidingPanel, View.TRANSLATION_Y, mSlidingPanel.getTranslationY(), mSlidingPanel.getTranslationY() + distY);
        ObjectAnimator bottomPanelAnimator = ObjectAnimator.ofFloat(mBottomPanel, View.TRANSLATION_Y, mBottomPanel.getTranslationY(), mBottomPanel.getTranslationY() + (distY * mParallaxFactor));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(slidingPanelAnimator, bottomPanelAnimator);
        set.setDuration(duration);
        set.setInterpolator(sDecelerator);
        set.addListener(new MyAnimListener(opening));
        set.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return onTouchEvent(event);
    }

    private class MyAnimListener implements Animator.AnimatorListener {

        int oldLayerTypeOne;
        int oldLayerTypeTwo;
        boolean opening;

        public MyAnimListener(boolean opening) {
            super();
            this.opening = opening;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            oldLayerTypeOne = mSlidingPanel.getLayerType();
            oldLayerTypeOne = mBottomPanel.getLayerType();

            mSlidingPanel.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mBottomPanel.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            mBottomPanel.setVisibility(View.VISIBLE);

            if (mWillDrawShadow) {
                mAnimating = true;
                ViewCompat.postInvalidateOnAnimation(DraggablePanelLayout.this);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setOpenedState(opening);

            if ( mPanelListener != null ) {
                if ( opening ) {
                    mPanelListener.onBottomPanelClosed();
                } else {
                    mPanelListener.onBottomPanelOpened();
                }
            }

            mBottomPanel.setTranslationY(0);
            mSlidingPanel.setTranslationY(0);

            mSlidingPanel.setLayerType(oldLayerTypeOne, null);
            mBottomPanel.setLayerType(oldLayerTypeTwo, null);

            requestLayout();

            mAnimating = false;
            if (mWillDrawShadow) {
                ViewCompat.postInvalidateOnAnimation(DraggablePanelLayout.this);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mAnimating = false;
            if (mWillDrawShadow) {
                ViewCompat.postInvalidateOnAnimation(DraggablePanelLayout.this);
            }
        }
    }

    private void setOpenedState(boolean opened) {
        this.mIsSlidingPanelOpened = opened;
        mBottomPanel.setVisibility(opened ? View.GONE : View.VISIBLE);
        hideShadowIfNotNeeded();
    }

    private void allowShadow() {
        mWillDrawShadow = mShadowDrawable != null;
        setWillNotDraw(!mWillDrawShadow);
    }

    private void hideShadowIfNotNeeded() {
        mWillDrawShadow = mShadowDrawable != null && !mIsSlidingPanelOpened;
        setWillNotDraw(!mWillDrawShadow);
    }

}
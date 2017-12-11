package com.semeshky.kvgspotter.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.semeshky.kvgspotter.R;

public final class StationActivityIndicator extends View {

    private final Paint mLinePaint = new Paint();
    private final Paint mCirclePaint = new Paint();
    private final Paint mCircleInactivePaint = new Paint();
    private boolean mActive = false;
    private int mStepperLineMargin;
    private int mStepperCircleSize;
    private int mStepperLineWidth;
    private boolean mFirst = false;
    private boolean mLast = false;

    public StationActivityIndicator(Context context) {
        super(context);
        this.init(context, null);
    }

    public StationActivityIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public StationActivityIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    public StationActivityIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    @ColorInt
    private int getPrimaryColor(@NonNull Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public boolean isFirst() {
        return mFirst;
    }

    public void setFirst(boolean first) {
        mFirst = first;
    }

    public boolean isLast() {
        return mLast;
    }

    public void setLast(boolean last) {
        mLast = last;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - this.mStepperCircleSize + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - this.mStepperCircleSize, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mCirclePaint.setARGB(255, 211, 47, 47);
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        this.mLinePaint.setStyle(Paint.Style.FILL);
        this.mCircleInactivePaint.setStyle(Paint.Style.FILL);
        if (isInEditMode()) {
            this.mActive = true;
            this.mLinePaint.setARGB(255, 189, 189, 189);
            this.mCircleInactivePaint.setARGB(38, 255, 255, 255);
            this.mStepperLineMargin = 16;
            this.mStepperCircleSize = 48;
            this.mStepperLineWidth = 2;
        } else {
            if (attrs != null) {
                TypedArray a = context.getTheme().obtainStyledAttributes(
                        attrs,
                        R.styleable.StationActivityIndicator,
                        0, 0);

                try {
                    this.mActive = a.getBoolean(R.styleable.StationActivityIndicator_active, false);
                    this.mLast = a.getBoolean(R.styleable.StationActivityIndicator_last, false);
                    this.mFirst = a.getBoolean(R.styleable.StationActivityIndicator_first, false);
                } finally {
                    a.recycle();
                }
            }
            this.mCirclePaint.setColor(this.getPrimaryColor(context));
            this.mLinePaint.setColor(context.getResources().getColor(R.color.stepper_line));
            this.mCircleInactivePaint.setColor(context.getResources().getColor(R.color.stepper_circle_inactive));
            this.mStepperLineMargin = context.getResources().getDimensionPixelSize(R.dimen.stepper_line_margin);
            this.mStepperCircleSize = context.getResources().getDimensionPixelSize(R.dimen.stepper_circle_size);
            this.mStepperLineWidth = context.getResources().getDimensionPixelSize(R.dimen.stepper_line_width);
        }
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        if (active == this.mActive) {
            return;
        }
        this.mActive = active;
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        final int lineOffsetX = (width - this.mStepperLineWidth) / 2;
        final int topLineLength = (height - this.mStepperCircleSize) / 2 - this.mStepperLineMargin;
        if (!this.mFirst)
            canvas.drawRect(lineOffsetX, 0, width - lineOffsetX, topLineLength, this.mLinePaint);
        if (!this.mLast)
            canvas.drawRect(lineOffsetX, height - topLineLength, width - lineOffsetX, height, this.mLinePaint);
        canvas.drawCircle(width / 2, height / 2, this.mStepperCircleSize / 2, this.mActive ? this.mCirclePaint : this.mCircleInactivePaint);
        //canvas.drawCircle(width / 2, height / 2, circleDiameter / 2 * 0.8f, this.mActive ? this.mInnerCirclePaint : this.mInnerCircleInactivePaint);
    }
}

package com.microsoft.kapp.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class GaugeView extends View {
    private static final float DEGREES_180 = 180.0f;
    private static final float DEGREES_90 = 90.0f;
    private static final int NUM_OF_SECTORS = 5;
    private int mAnimationDurationMilliseconds;
    private float mArrowAngle;
    private ValueAnimator mArrowAnimator;
    private float mArrowCenterY;
    private RectF mArrowCircleRect;
    private int mArrowColor;
    private float mArrowLength;
    private Paint mArrowPaint;
    private Path mArrowPath;
    private float mArrowRadius;
    private float mArrowVerticalOffset;
    private float mCenterX;
    private float mCenterY;
    private RectF mInnerCircleRect;
    private float mInnerCircleScale;
    private float mInnerRadius;
    private Paint mOuterCirclePaint;
    private RectF mOuterCircleRect;
    private int mOuterCircleStrokeColor;
    private float mOuterRadius;
    private RectF mSectorOuterCircleRect;
    private float mSectorOuterRadius;
    private float mSectorPaddingCircumference;
    private Paint mSectorPaint;
    private Path[] mSectorPaths;
    private Region[] mSectorRegions;
    private int mSectorSelectedColor;
    private float mSectorSweepAngle;
    private int mSectorUnselectedColor;
    private float mStrokeWidth;

    public GaugeView(Context context) {
        super(context);
    }

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GaugeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setAttributes(attrs);
        this.mSectorPaths = new Path[5];
        this.mSectorRegions = new Region[5];
        for (int i = 0; i < 5; i++) {
            this.mSectorPaths[i] = new Path();
            this.mSectorRegions[i] = new Region();
        }
        this.mSectorSweepAngle = 36.0f;
        this.mArrowAngle = convertSelectedIndexToArrowAngle(0);
        this.mOuterCirclePaint = new Paint(1);
        this.mOuterCirclePaint.setColor(this.mOuterCircleStrokeColor);
        this.mOuterCirclePaint.setStrokeWidth(this.mStrokeWidth);
        this.mOuterCirclePaint.setStyle(Paint.Style.STROKE);
        this.mSectorPaint = new Paint(1);
        this.mSectorPaint.setStyle(Paint.Style.FILL);
        this.mArrowPaint = new Paint(1);
        this.mArrowPaint.setColor(this.mArrowColor);
        this.mArrowPaint.setStyle(Paint.Style.FILL);
        this.mArrowPath = new Path();
    }

    private void endAnimation() {
        if (this.mArrowAnimator != null) {
            this.mArrowAnimator.end();
            this.mArrowAnimator.removeAllUpdateListeners();
            this.mArrowAnimator = null;
        }
    }

    public int getNumberOfSectors() {
        return 5;
    }

    public void setSelectedIndex(int index, boolean animate) {
        validateIndex(index);
        float arrowAngle = convertSelectedIndexToArrowAngle(index);
        if (animate) {
            if (this.mArrowAnimator != null && this.mArrowAnimator.isRunning()) {
                endAnimation();
            }
            this.mArrowAnimator = ValueAnimator.ofFloat(180.0f, arrowAngle);
            this.mArrowAnimator.setDuration((this.mAnimationDurationMilliseconds * (index + 1)) / 5);
            this.mArrowAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            this.mArrowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.microsoft.kapp.views.GaugeView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator animation) {
                    GaugeView.this.mArrowAngle = ((Float) animation.getAnimatedValue()).floatValue();
                    GaugeView.this.invalidate();
                }
            });
            this.mArrowAnimator.start();
            return;
        }
        this.mArrowAngle = arrowAngle;
        invalidate();
    }

    private float convertSelectedIndexToArrowAngle(int index) {
        switch (index) {
            case 0:
                return 170.0f;
            case 1:
                return 130.0f;
            case 2:
                return DEGREES_90;
            case 3:
                return 50.0f;
            case 4:
                return 10.0f;
            default:
                throw new IllegalArgumentException(String.format("index value is %d. Must be less than %d", Integer.valueOf(index), 5));
        }
    }

    private void setAttributes(AttributeSet attrs) {
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GaugeView);
        try {
            this.mOuterCircleStrokeColor = typedAttributes.getColor(0, getResources().getColor(R.color.gauge_outer_circle_stroke_color_default));
            this.mArrowColor = typedAttributes.getColor(1, getResources().getColor(R.color.gauge_arrow_color_default));
            this.mSectorUnselectedColor = typedAttributes.getColor(7, getResources().getColor(R.color.gauge_sector_unselected_color_default));
            this.mSectorSelectedColor = typedAttributes.getColor(8, getResources().getColor(R.color.gauge_sector_selected_color_default));
            this.mInnerCircleScale = typedAttributes.getFloat(9, 0.0f);
            this.mAnimationDurationMilliseconds = typedAttributes.getInteger(10, 0);
            this.mStrokeWidth = typedAttributes.getDimensionPixelSize(2, 0);
            this.mArrowRadius = typedAttributes.getDimensionPixelSize(4, 0);
            this.mArrowLength = typedAttributes.getDimensionPixelSize(3, 0);
            this.mArrowVerticalOffset = typedAttributes.getDimensionPixelSize(5, 0);
            this.mSectorPaddingCircumference = typedAttributes.getDimensionPixelSize(6, 0);
        } finally {
            typedAttributes.recycle();
        }
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= 5) {
            throw new IllegalArgumentException(String.format("Invalid value for index = [%d]. Must be less than %d and greater than or equal to zero.", Integer.valueOf(index), 5));
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.mOuterCircleRect, 0.0f, -180.0f, true, this.mOuterCirclePaint);
        this.mArrowPath.reset();
        this.mArrowPath.addArc(this.mArrowCircleRect, DEGREES_90 - this.mArrowAngle, 180.0f);
        float x = this.mCenterX + ((float) (this.mArrowLength * Math.cos(Math.toRadians(this.mArrowAngle))));
        float y = this.mArrowCenterY - ((float) (this.mArrowLength * Math.sin(Math.toRadians(this.mArrowAngle))));
        this.mArrowPath.lineTo(x, y);
        this.mArrowPath.close();
        for (int i = 0; i < 5; i++) {
            if (this.mSectorRegions[i].contains((int) x, (int) y)) {
                this.mSectorPaint.setColor(this.mSectorSelectedColor);
            } else {
                this.mSectorPaint.setColor(this.mSectorUnselectedColor);
            }
            canvas.drawPath(this.mSectorPaths[i], this.mSectorPaint);
        }
        canvas.drawPath(this.mArrowPath, this.mArrowPaint);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int xPadding = getPaddingLeft() + getPaddingRight();
        int yPadding = getPaddingTop() + getPaddingBottom();
        int width = w - xPadding;
        int height = h - yPadding;
        this.mOuterRadius = (Math.min(width, height * 2) - this.mStrokeWidth) / 2.0f;
        this.mSectorOuterRadius = this.mOuterRadius - (this.mStrokeWidth / 2.0f);
        this.mInnerRadius = this.mOuterRadius * this.mInnerCircleScale;
        this.mCenterX = w / 2;
        this.mCenterY = this.mOuterRadius + (((this.mStrokeWidth + height) - this.mOuterRadius) / 2.0f);
        this.mArrowCenterY = (this.mCenterY - this.mArrowVerticalOffset) - this.mArrowRadius;
        this.mOuterCircleRect = createSquare(this.mCenterX, this.mCenterY, this.mOuterRadius);
        this.mSectorOuterCircleRect = createSquare(this.mCenterX, this.mCenterY, this.mSectorOuterRadius);
        this.mInnerCircleRect = createSquare(this.mCenterX, this.mCenterY, this.mInnerRadius);
        this.mArrowCircleRect = createSquare(this.mCenterX, this.mArrowCenterY, this.mArrowRadius);
        createSectorsPaths();
    }

    private static RectF createSquare(float centerX, float centerY, float radius) {
        float diameter = 2.0f * radius;
        float left = centerX - radius;
        float top = centerY - radius;
        float right = left + diameter;
        float bottom = top + diameter;
        return new RectF(left, top, right, bottom);
    }

    private void createSectorsPaths() {
        float currentSweepAngleInner;
        float currentSweepAngleOuter;
        if (this.mInnerRadius != 0.0f && this.mOuterRadius != 0.0f) {
            float innerCircleAnglePadding = (float) Math.toDegrees(this.mSectorPaddingCircumference / this.mInnerRadius);
            float outerCircleAnglePadding = (float) Math.toDegrees(this.mSectorPaddingCircumference / this.mOuterRadius);
            float initialInnerAngleOffset = (float) Math.toDegrees(Math.atan(this.mStrokeWidth / (2.0f * this.mInnerRadius)));
            float initialOuterAngleOffset = (float) Math.toDegrees(Math.atan(this.mStrokeWidth / (2.0f * this.mSectorOuterRadius)));
            float sweepAngleInner = (-this.mSectorSweepAngle) + innerCircleAnglePadding;
            float sweepAngleOuter = (-this.mSectorSweepAngle) + outerCircleAnglePadding;
            float sweepAngleBoundaryInner = (-this.mSectorSweepAngle) + (innerCircleAnglePadding / 2.0f) + initialInnerAngleOffset;
            float sweepAngleBoundaryOuter = (-this.mSectorSweepAngle) + (outerCircleAnglePadding / 2.0f) + initialOuterAngleOffset;
            float startAngleInner = -initialInnerAngleOffset;
            float startAngleOuter = -initialOuterAngleOffset;
            for (int i = 0; i < 5; i++) {
                if (i == 0 || i == 4) {
                    currentSweepAngleInner = sweepAngleBoundaryInner;
                    currentSweepAngleOuter = sweepAngleBoundaryOuter;
                } else {
                    currentSweepAngleInner = sweepAngleInner;
                    currentSweepAngleOuter = sweepAngleOuter;
                }
                Path path = this.mSectorPaths[i];
                path.reset();
                Region region = this.mSectorRegions[i];
                region.setEmpty();
                path.addArc(this.mInnerCircleRect, startAngleInner, currentSweepAngleInner);
                float x = ((float) (this.mSectorOuterRadius * Math.cos(Math.toRadians(startAngleOuter + currentSweepAngleOuter)))) + this.mCenterX;
                float y = ((float) (this.mSectorOuterRadius * Math.sin(Math.toRadians(startAngleOuter + currentSweepAngleOuter)))) + this.mCenterY;
                path.lineTo(x, y);
                path.arcTo(this.mSectorOuterCircleRect, startAngleOuter + currentSweepAngleOuter, -currentSweepAngleOuter);
                path.close();
                RectF rectF = new RectF();
                path.computeBounds(rectF, true);
                region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
                startAngleInner += currentSweepAngleInner - innerCircleAnglePadding;
                startAngleOuter += currentSweepAngleOuter - outerCircleAnglePadding;
            }
        }
    }
}

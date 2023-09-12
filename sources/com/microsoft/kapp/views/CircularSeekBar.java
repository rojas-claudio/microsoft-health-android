package com.microsoft.kapp.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CircularSeekBar extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final int FULL_ROTATION_ANGLE = 360;
    private static final double HALF_PI = 1.5707963267948966d;
    private static final double PI = 3.141592653589793d;
    private static final int START_ANGLE = -90;
    private static final double TWO_PI = 6.283185307179586d;
    private int mAnimationFrameDelay;
    private float mArcAngle;
    private int mArcColor;
    private Paint mArcPaint;
    private int mArcWidth;
    private int mCenterX;
    private int mCenterY;
    private RectF mCircularSeekBarRect;
    private int mCurrentValue;
    private float mDelta;
    private float mDistance;
    @Inject
    FontManager mFontManager;
    private int mIncrement;
    private int mInnerRingTouchZone;
    private boolean mIsEditMode;
    private boolean mIsPressed;
    private double mLastPolarAngle;
    private int mMaxValue;
    private double mMinDeltaIncrement;
    private double mMinStartPolarAngle;
    private int mMinStartValue;
    private int mMotionType;
    private OnValueChangeListener mOnValueChangedlistener;
    private int mOuterRingTouchZone;
    private int mOuterSeekCircleTouchZone;
    private int mRingColor;
    private Paint mRingPaint;
    private int mRingRadius;
    private int mRingWidth;
    private RectF mSeekCircleRect;
    private int mSeekColor;
    private int mSeekInnerRadius;
    private int mSeekOuterRadius;
    private Paint mSeekPaint;
    private int mSeekPointX;
    private int mSeekPointY;
    private int mTextColor;
    private float mTextPadding;
    private Paint mTextPaint;
    private int mTextSize;
    private int mTouchMargin;
    private ValueAnimator mValueAnimator;

    /* loaded from: classes.dex */
    public interface OnValueChangeListener {
        void CircularSeekBarValueChanged(int i);
    }

    public CircularSeekBar(Context context) {
        super(context);
        this.mIsEditMode = true;
        initialize(context, null);
    }

    public CircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsEditMode = true;
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        KApplicationGraph.getApplicationGraph().inject(this);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.CircularSeekBar);
        try {
            this.mRingColor = attrArray.getColor(0, getResources().getColor(R.color.CircularSeekBar_ring_color));
            this.mRingWidth = attrArray.getDimensionPixelSize(1, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_ring_width));
            this.mIncrement = attrArray.getInteger(2, getResources().getInteger(R.integer.CircularSeekBar_increment));
            this.mMaxValue = attrArray.getInteger(3, getResources().getInteger(R.integer.CircularSeekBar_max));
            this.mMinStartValue = attrArray.getInteger(4, getResources().getInteger(R.integer.CircularSeekBar_min_start_value));
            this.mArcColor = attrArray.getColor(5, getResources().getColor(R.color.CircularSeekBar_arc_color));
            this.mArcWidth = attrArray.getDimensionPixelSize(6, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_arc_width));
            this.mSeekColor = attrArray.getColor(7, getResources().getColor(R.color.CircularSeekBar_seek_color));
            this.mSeekOuterRadius = attrArray.getDimensionPixelSize(8, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_seek_outer_radius));
            this.mSeekInnerRadius = attrArray.getDimensionPixelSize(9, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_seek_inner_radius));
            this.mTextSize = attrArray.getDimensionPixelSize(11, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_text_size));
            this.mTextColor = attrArray.getColor(10, getResources().getColor(R.color.CircularSeekBar_text_color));
            this.mTouchMargin = attrArray.getDimensionPixelSize(12, getResources().getDimensionPixelSize(R.dimen.CircularSeekBar_seek_touch_margin));
            this.mAnimationFrameDelay = attrArray.getInt(13, getResources().getInteger(R.integer.CircularSeekBar_animation_frame_delay));
            attrArray.recycle();
            this.mArcPaint = new Paint(1);
            this.mArcPaint.setColor(this.mArcColor);
            this.mArcPaint.setStrokeWidth(this.mRingWidth);
            this.mArcPaint.setStyle(Paint.Style.STROKE);
            this.mRingPaint = new Paint(1);
            this.mRingPaint.setColor(this.mRingColor);
            this.mRingPaint.setStrokeWidth(this.mRingWidth);
            this.mRingPaint.setStyle(Paint.Style.STROKE);
            this.mSeekPaint = new Paint(1);
            this.mSeekPaint.setColor(this.mSeekColor);
            this.mSeekPaint.setStrokeWidth(Math.abs(this.mSeekOuterRadius - this.mSeekInnerRadius));
            this.mSeekPaint.setStyle(Paint.Style.STROKE);
            this.mTextPaint = new Paint(1);
            this.mTextPaint.setColor(this.mTextColor);
            this.mTextPaint.setTextSize(this.mTextSize);
            CommonUtils.applyCommonStyles(this.mTextPaint, this.mFontManager, context, attrs);
            this.mOuterSeekCircleTouchZone = this.mSeekOuterRadius + this.mTouchMargin;
        } catch (Throwable th) {
            attrArray.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        int xPadding = getPaddingLeft() + getPaddingRight();
        int yPadding = getPaddingTop() + getPaddingBottom();
        int width = (w - xPadding) - (this.mSeekOuterRadius * 2);
        int height = (h - yPadding) - (this.mSeekOuterRadius * 2);
        this.mRingRadius = (Math.min(width, height) - this.mArcWidth) / 2;
        this.mCenterX = w / 2;
        this.mCenterY = h / 2;
        this.mCircularSeekBarRect = createSquare(this.mCenterX, this.mCenterY, this.mRingRadius);
        this.mSeekCircleRect = new RectF();
        this.mDelta = this.mSeekInnerRadius / 2;
        this.mOuterRingTouchZone = this.mRingRadius + this.mOuterSeekCircleTouchZone;
        this.mInnerRingTouchZone = this.mRingRadius - this.mOuterSeekCircleTouchZone;
        this.mTextPadding = (this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f;
        if (this.mIncrement <= 0) {
            this.mIncrement = 1;
        }
        if (this.mMaxValue <= 0) {
            this.mMaxValue = 100;
        }
        if (this.mMinStartValue < 0) {
            this.mMinStartValue = 0;
        }
        this.mMinDeltaIncrement = (this.mIncrement * TWO_PI) / this.mMaxValue;
        this.mMinStartPolarAngle = (this.mMinStartValue * TWO_PI) / this.mMaxValue;
        recalculateCoordinatesFromValue(this.mCurrentValue >= this.mMinStartValue ? this.mCurrentValue : this.mMinStartValue);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.mCircularSeekBarRect, -90.0f, 360.0f, false, this.mRingPaint);
        canvas.drawArc(this.mCircularSeekBarRect, (-90.0f) - this.mDelta, (2.0f * this.mDelta) + this.mArcAngle, false, this.mArcPaint);
        drawSeekCircle(canvas, this.mSeekPaint, this.mSeekCircleRect, this.mSeekPointX, this.mSeekPointY, (this.mSeekOuterRadius + this.mSeekInnerRadius) / 2);
        drawSeekCircle(canvas, this.mRingPaint, this.mSeekCircleRect, this.mSeekPointX, this.mSeekPointY, this.mSeekInnerRadius / 2);
        updateText(canvas, this.mTextPaint);
    }

    protected void updateText(Canvas canvas, Paint textPaint) {
        drawText(canvas, textPaint, String.format("%,d", Integer.valueOf(this.mCurrentValue)));
    }

    private void drawText(Canvas canvas, Paint paint, String text) {
        float textWidth = paint.measureText(text);
        float xPos = this.mCenterX - (textWidth / 2.0f);
        float yPos = (int) (this.mCenterY - this.mTextPadding);
        canvas.drawText(text, xPos, yPos, paint);
    }

    private static RectF createSquare(float centerX, float centerY, float radius) {
        float left = centerX - radius;
        float top = centerY - radius;
        float right = centerX + radius;
        float bottom = centerY + radius;
        return new RectF(left, top, right, bottom);
    }

    private void drawSeekCircle(Canvas canvas, Paint paint, RectF oval, int x, int y, int radius) {
        oval.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawArc(oval, 0.0f, 360.0f, false, paint);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mIsEditMode) {
            float x = event.getX();
            float y = event.getY();
            this.mMotionType = event.getAction();
            adjustBehaviorAndRecalculateCoordinatesFromXY(x, y);
        }
        return true;
    }

    private void adjustBehaviorAndRecalculateCoordinatesFromXY(float x, float y) {
        double polarAngle = HALF_PI - Math.atan2(this.mCenterY - y, x - this.mCenterX);
        if (polarAngle < Constants.SPLITS_ACCURACY) {
            polarAngle += TWO_PI;
        }
        if (this.mIsPressed) {
            if (this.mMotionType == 1) {
                this.mIsPressed = false;
            }
            if (Math.abs(this.mLastPolarAngle - polarAngle) < HALF_PI) {
                if (isInAllowedTouchZone(x, y)) {
                    if (adjustAndCalculateCoordinatesFromAngle(polarAngle, false)) {
                        invalidate();
                        return;
                    }
                    return;
                }
                this.mIsPressed = false;
            }
        } else if (this.mMotionType == 0 && isInSeekCircleTouchZone(x, y)) {
            this.mIsPressed = true;
            if (Math.abs(this.mLastPolarAngle - polarAngle) >= HALF_PI) {
                adjustAndCalculateCoordinatesFromAngle(polarAngle, true);
            } else if (adjustAndCalculateCoordinatesFromAngle(polarAngle, false)) {
                invalidate();
            }
        } else if (this.mMotionType == 0 && isInRingTouchZone(x, y)) {
            adjustAndCalculateCoordinatesFromAngle(polarAngle, true);
        }
    }

    protected boolean isInRingTouchZone(float x, float y) {
        this.mDistance = (float) Math.sqrt(Math.pow(x - this.mCenterX, 2.0d) + Math.pow(y - this.mCenterY, 2.0d));
        return this.mDistance <= ((float) this.mOuterRingTouchZone) && this.mDistance >= ((float) this.mInnerRingTouchZone);
    }

    protected boolean isInSeekCircleTouchZone(float x, float y) {
        this.mDistance = (float) Math.sqrt(Math.pow(x - this.mSeekPointX, 2.0d) + Math.pow(y - this.mSeekPointY, 2.0d));
        return this.mDistance <= ((float) this.mOuterSeekCircleTouchZone);
    }

    protected boolean isInAllowedTouchZone(float x, float y) {
        this.mDistance = (float) Math.sqrt(Math.pow(x - this.mCenterX, 2.0d) + Math.pow(y - this.mCenterY, 2.0d));
        return this.mDistance <= ((float) (this.mRingRadius * 3)) && this.mDistance >= ((float) (this.mRingRadius / 4));
    }

    public int getCurrentValue() {
        return this.mCurrentValue;
    }

    public int setCurrentValue(int value) {
        if (this.mCurrentValue != value) {
            recalculateCoordinatesFromValue(value);
            invalidate();
        }
        return this.mCurrentValue;
    }

    public int setInitialValue(int value) {
        int round = this.mIncrement * Math.round(value / this.mIncrement);
        this.mCurrentValue = round;
        return round;
    }

    private void setInternallyCurrentValue(int value) {
        this.mCurrentValue = value;
        if (this.mOnValueChangedlistener != null) {
            this.mOnValueChangedlistener.CircularSeekBarValueChanged(value);
        }
    }

    private boolean recalculateCoordinatesFromValue(int val) {
        int value = val > this.mMaxValue ? this.mMaxValue : val;
        if (val < this.mMinStartValue) {
            value = this.mMinStartValue;
        }
        int cofficient = Math.round(value / this.mIncrement);
        int value2 = this.mIncrement * cofficient;
        setInternallyCurrentValue(value2);
        this.mLastPolarAngle = this.mMinDeltaIncrement * cofficient;
        calculateArcCoordinates();
        return true;
    }

    private boolean adjustAndCalculateCoordinatesFromAngle(double polarAngle, boolean isAnimated) {
        if (polarAngle < this.mMinStartPolarAngle) {
            polarAngle = this.mMinStartPolarAngle;
        }
        double polarAngle2 = adjustAngle(polarAngle);
        if (this.mLastPolarAngle == polarAngle2) {
            return false;
        }
        if (isAnimated) {
            startAnimation(polarAngle2);
            return false;
        }
        this.mLastPolarAngle = polarAngle2;
        setInternallyCurrentValue(this.mIncrement * ((int) Math.round(this.mLastPolarAngle / this.mMinDeltaIncrement)));
        calculateArcCoordinates();
        return true;
    }

    protected double adjustAngle(double polarAngle) {
        return polarAngle;
    }

    private void calculateCoordinateFromAngle(double polarAngle) {
        if (polarAngle < this.mMinStartPolarAngle) {
            polarAngle = this.mMinStartPolarAngle;
        }
        this.mLastPolarAngle = polarAngle;
        setInternallyCurrentValue(this.mIncrement * ((int) (this.mLastPolarAngle / this.mMinDeltaIncrement)));
        calculateArcCoordinates();
    }

    private void calculateArcCoordinates() {
        this.mArcAngle = (float) Math.toDegrees(this.mLastPolarAngle);
        this.mSeekPointX = this.mCenterX + ((int) Math.round(this.mRingRadius * Math.cos(HALF_PI - this.mLastPolarAngle)));
        this.mSeekPointY = this.mCenterY - ((int) Math.round(this.mRingRadius * Math.sin(HALF_PI - this.mLastPolarAngle)));
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator animation) {
        double angle = ((Float) this.mValueAnimator.getAnimatedValue()).floatValue();
        calculateCoordinateFromAngle(angle);
        invalidate();
    }

    protected void startAnimation(double endPolarAngle) {
        if (this.mValueAnimator == null || !this.mValueAnimator.isRunning()) {
            this.mValueAnimator = ValueAnimator.ofFloat((float) this.mLastPolarAngle, (float) endPolarAngle);
            this.mValueAnimator.addUpdateListener(this);
            this.mValueAnimator.setDuration((long) ((this.mAnimationFrameDelay * Math.abs(endPolarAngle - this.mLastPolarAngle)) / this.mMinDeltaIncrement));
            this.mValueAnimator.setInterpolator(new LinearInterpolator());
            this.mValueAnimator.start();
        }
    }

    protected void endAnimation() {
        if (this.mValueAnimator != null) {
            this.mValueAnimator.end();
            this.mValueAnimator.removeAllUpdateListeners();
        }
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedlistener) {
        this.mOnValueChangedlistener = onValueChangedlistener;
    }

    public void removeOnValueChangedListener() {
        this.mOnValueChangedlistener = null;
    }

    public void setMinStartValue(int minStartValue) {
        this.mMinStartValue = minStartValue;
    }

    public void setIncrementValue(int incrementValue) {
        this.mIncrement = incrementValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public void setEditMode(boolean isEditMode) {
        this.mIsEditMode = isEditMode;
    }

    public int getMinStartValue() {
        return this.mMinStartValue;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }
}

package com.microsoft.kapp.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.microsoft.kapp.R;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class CircularProgressView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static int START_ANGLE = 270;
    private static int SWEEP_ANGLE = 360;
    private float mAnimationProgress;
    private int mArcWidth;
    private int mCenterX;
    private int mCenterY;
    private Paint mCurrentPaint;
    private int mErrorColor;
    private Paint mErrorPaint;
    private boolean mInProgress;
    private boolean mIsSuccess;
    private ValueAnimator mProgressAnimator;
    private int mProgressColor;
    private Paint mProgressPaint;
    private float mProgressPosition;
    private RectF mProgressRect;
    private int mRadius;
    private int mStartColor;
    private Paint mStartPaint;
    private int mSuccessColor;
    private Paint mSuccessPaint;
    private int mTextColor;
    private Paint mTextPaint;
    private int mTextSize;

    public CircularProgressView(Context context) {
        super(context);
        initialize(context, null);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        try {
            this.mProgressColor = a.getColor(0, getResources().getColor(R.color.header_default_text_color));
            this.mErrorColor = a.getColor(1, getResources().getColor(R.color.header_default_text_color));
            this.mSuccessColor = a.getColor(3, getResources().getColor(R.color.header_default_text_color));
            this.mStartColor = a.getColor(4, getResources().getColor(R.color.header_default_text_color));
            this.mTextColor = a.getColor(5, getResources().getColor(R.color.header_default_text_color));
            this.mTextSize = a.getDimensionPixelSize(6, getResources().getDimensionPixelSize(R.dimen.header_title_text_size));
            this.mArcWidth = a.getDimensionPixelSize(2, getResources().getDimensionPixelSize(R.dimen.header_title_text_size));
            a.recycle();
            this.mProgressPaint = new Paint(1);
            this.mProgressPaint.setColor(this.mProgressColor);
            this.mProgressPaint.setStrokeWidth(this.mArcWidth);
            this.mProgressPaint.setStyle(Paint.Style.STROKE);
            this.mStartPaint = new Paint(1);
            this.mStartPaint.setColor(this.mStartColor);
            this.mStartPaint.setStrokeWidth(this.mArcWidth);
            this.mStartPaint.setStyle(Paint.Style.STROKE);
            this.mErrorPaint = new Paint(1);
            this.mErrorPaint.setColor(this.mErrorColor);
            this.mErrorPaint.setStrokeWidth(this.mArcWidth);
            this.mErrorPaint.setStyle(Paint.Style.STROKE);
            this.mSuccessPaint = new Paint(1);
            this.mSuccessPaint.setColor(this.mSuccessColor);
            this.mSuccessPaint.setStrokeWidth(this.mArcWidth);
            this.mSuccessPaint.setStyle(Paint.Style.STROKE);
            this.mCurrentPaint = this.mProgressPaint;
            this.mTextPaint = new Paint(1);
            this.mTextPaint.setColor(this.mTextColor);
            this.mTextPaint.setTypeface(Typeface.DEFAULT);
            this.mTextPaint.setTextSize(this.mTextSize);
        } catch (Throwable th) {
            a.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        int xPadding = getPaddingLeft() + getPaddingRight();
        int yPadding = getPaddingTop() + getPaddingBottom();
        int width = w - xPadding;
        int height = h - yPadding;
        this.mRadius = (Math.min(width, height) - this.mArcWidth) / 2;
        this.mCenterX = w / 2;
        this.mCenterY = h / 2;
        this.mProgressRect = createSquare(this.mCenterX, this.mCenterY, this.mRadius);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        String text;
        super.onDraw(canvas);
        canvas.drawArc(this.mProgressRect, START_ANGLE, SWEEP_ANGLE, false, this.mStartPaint);
        float arcAngle = (this.mProgressPosition / 100.0f) * SWEEP_ANGLE;
        canvas.drawArc(this.mProgressRect, START_ANGLE, arcAngle, false, this.mCurrentPaint);
        if (this.mInProgress) {
            text = String.format(getResources().getString(R.string.sync_progress), Integer.valueOf((int) this.mProgressPosition));
        } else {
            text = this.mIsSuccess ? getResources().getString(R.string.sync_success) : getResources().getString(R.string.sync_error);
        }
        drawText(canvas, this.mTextPaint, text);
    }

    private static RectF createSquare(float centerX, float centerY, float radius) {
        float diameter = 2.0f * radius;
        float left = centerX - radius;
        float top = centerY - radius;
        float right = left + diameter;
        float bottom = top + diameter;
        return new RectF(left, top, right, bottom);
    }

    public void onStart() {
        this.mProgressPosition = 0.0f;
        this.mInProgress = true;
        this.mAnimationProgress = 0.0f;
        this.mCurrentPaint = this.mProgressPaint;
        invalidate();
    }

    public void onComplete(boolean isSuccess) {
        this.mProgressPosition = 100.0f;
        this.mInProgress = false;
        this.mIsSuccess = isSuccess;
        this.mCurrentPaint = this.mIsSuccess ? this.mSuccessPaint : this.mErrorPaint;
        invalidate();
    }

    public void onProgressChanged(int progressPercentage) {
        onProgressChanged(progressPercentage, true);
    }

    public void onProgressChanged(int progressPercentage, boolean animate) {
        if (this.mProgressAnimator != null && this.mProgressAnimator.isRunning()) {
            endAnimation();
        }
        if (progressPercentage > this.mProgressPosition) {
            this.mAnimationProgress = this.mProgressPosition;
            this.mProgressPosition = progressPercentage;
            if (animate) {
                startAnimation();
                return;
            }
            return;
        }
        this.mProgressPosition = progressPercentage;
        this.mAnimationProgress = this.mProgressPosition;
    }

    public void startAnimation() {
        this.mProgressAnimator = ValueAnimator.ofFloat(this.mAnimationProgress, this.mProgressPosition);
        this.mProgressAnimator.addUpdateListener(this);
        this.mProgressAnimator.setDuration(1000L);
        this.mProgressAnimator.setInterpolator(new LinearInterpolator());
        this.mProgressAnimator.start();
    }

    public void endAnimation() {
        if (this.mProgressAnimator != null) {
            this.mProgressAnimator.cancel();
            this.mProgressAnimator.removeAllUpdateListeners();
        }
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator animation) {
        this.mProgressPosition = ((Float) animation.getAnimatedValue()).floatValue();
        invalidate();
    }

    private void drawText(Canvas canvas, Paint paint, String text) {
        if (!StringUtils.isBlank(text)) {
            float textWidth = paint.measureText(text);
            float xPos = canvas.getWidth() / 2;
            float yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2.0f));
            canvas.drawText(text, xPos - (textWidth / 2.0f), yPos, paint);
        }
    }
}

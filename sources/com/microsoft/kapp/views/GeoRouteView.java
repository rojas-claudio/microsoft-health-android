package com.microsoft.kapp.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.GeoCoordinate;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GeoRouteView extends View {
    private static final float DEGREES_360 = 360.0f;
    private boolean mAnimate;
    private int mAnimationDurationMilliseconds;
    private int mCircleMarkerFillColor;
    private Paint mCircleMarkerFillPaint;
    private int mCircleMarkerRadius;
    private Paint mCircleMarkerStrokePaint;
    private int mCircleMarkerStrokeWidth;
    private double mDrawableSquareSide;
    private PointF mEndPoint;
    private Path mGeoPath;
    private double mHeight;
    private int mPathColor;
    private List<GeoCoordinate> mPathCoordinates;
    private float mPathDrawFraction;
    private Paint mPathPaint;
    private List<PointF> mPathPoints;
    private int mPathStrokeWidth;
    private PointF mStartPoint;
    private float mTotalLength;
    private ValueAnimator mValueAnimator;
    private double mWidth;

    public GeoRouteView(Context context) {
        super(context);
    }

    public GeoRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GeoRouteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void setAttributes(AttributeSet attrs) {
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GeoRouteView);
        try {
            this.mPathColor = typedAttributes.getColor(0, getResources().getColor(R.color.geo_path_color_default));
            this.mCircleMarkerFillColor = typedAttributes.getColor(1, getResources().getColor(R.color.geo_path_circle_marker_fill_color_default));
            this.mCircleMarkerStrokeWidth = typedAttributes.getDimensionPixelSize(2, 0);
            this.mPathStrokeWidth = typedAttributes.getDimensionPixelSize(3, 0);
            this.mAnimationDurationMilliseconds = typedAttributes.getInteger(5, 0);
            this.mCircleMarkerRadius = typedAttributes.getDimensionPixelSize(4, 0);
        } finally {
            typedAttributes.recycle();
        }
    }

    private void init(AttributeSet attrs) {
        setAttributes(attrs);
        this.mGeoPath = new Path();
        this.mPathPoints = new ArrayList();
        this.mPathPaint = new Paint();
        this.mPathPaint.setAntiAlias(true);
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        this.mPathPaint.setColor(this.mPathColor);
        this.mPathPaint.setStrokeWidth(this.mPathStrokeWidth);
        this.mCircleMarkerFillPaint = new Paint();
        this.mCircleMarkerFillPaint.setAntiAlias(true);
        this.mCircleMarkerFillPaint.setStyle(Paint.Style.FILL);
        this.mCircleMarkerFillPaint.setColor(this.mCircleMarkerFillColor);
        this.mCircleMarkerStrokePaint = new Paint();
        this.mCircleMarkerStrokePaint.setAntiAlias(true);
        this.mCircleMarkerStrokePaint.setStyle(Paint.Style.STROKE);
        this.mCircleMarkerStrokePaint.setColor(this.mPathColor);
        this.mCircleMarkerStrokePaint.setStrokeWidth(this.mCircleMarkerStrokeWidth);
    }

    private static PointF convertToPoint(GeoCoordinate coordinate, double squareCanvasSide) {
        float x = (float) (((coordinate.getLongitude() * squareCanvasSide) / 360.0d) + (squareCanvasSide / 2.0d));
        double tan = Math.tan(0.7853981633974483d + (Math.toRadians(coordinate.getLatitude()) / 2.0d));
        double mercatorNorthing = Math.log(tan);
        float y = (float) ((squareCanvasSide - ((squareCanvasSide * mercatorNorthing) / 3.141592653589793d)) / 2.0d);
        return new PointF(x, y);
    }

    private RectF getBounds(List<GeoCoordinate> coordinates, double side) {
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        for (GeoCoordinate g : coordinates) {
            PointF point = convertToPoint(g, side);
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }
        return new RectF(minX, minY, maxX, maxY);
    }

    private static PointF scaleToBounds(PointF point, RectF bounds, double viewWidth, double viewHeight, double mapSize) {
        float boundsWidth = bounds.right - bounds.left;
        float boundsHeight = bounds.bottom - bounds.top;
        if (CommonUtils.areDoublesEqual(boundsWidth, Constants.SPLITS_ACCURACY) || CommonUtils.areDoublesEqual(boundsHeight, Constants.SPLITS_ACCURACY)) {
            return new PointF(((float) viewWidth) / 2.0f, ((float) viewHeight) / 2.0f);
        }
        double scaleFactor = boundsWidth > boundsHeight ? mapSize / boundsWidth : mapSize / boundsHeight;
        double scaledWidth = scaleFactor * boundsWidth;
        double scaledHeight = scaleFactor * boundsHeight;
        return new PointF((float) (((point.x - bounds.left) * scaleFactor) + ((viewWidth - scaledWidth) / 2.0d)), (float) (((point.y - bounds.top) * scaleFactor) + ((viewHeight - scaledHeight) / 2.0d)));
    }

    private static float computeLength(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2.0d) + Math.pow(Math.abs(p1.y - p2.y), 2.0d));
    }

    public void setPathCoordinates(List<GeoCoordinate> pathCoordinates, boolean animate) {
        Validate.notNull(pathCoordinates, "pathCoordinates");
        if (pathCoordinates.size() < 2) {
            throw new IllegalArgumentException("pathCoordinates must have at least 2 elements.");
        }
        this.mPathCoordinates = pathCoordinates;
        this.mAnimate = animate;
        createAndAnimateRoute();
    }

    private void createAndAnimateRoute() {
        if (this.mPathCoordinates != null && this.mWidth > Constants.SPLITS_ACCURACY && this.mHeight > Constants.SPLITS_ACCURACY && this.mDrawableSquareSide > Constants.SPLITS_ACCURACY) {
            createPathPoints();
            if (!CommonUtils.areDoublesEqual(this.mTotalLength, Constants.SPLITS_ACCURACY)) {
                createPath(this.mAnimate);
                this.mAnimate = false;
            }
        }
    }

    private void createPath(boolean animate) {
        if (animate) {
            stopAnimations();
            this.mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            this.mValueAnimator.setDuration(this.mAnimationDurationMilliseconds);
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.microsoft.kapp.views.GeoRouteView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator animation) {
                    GeoRouteView.this.mPathDrawFraction = ((Float) animation.getAnimatedValue()).floatValue();
                    GeoRouteView.this.populatePath();
                    GeoRouteView.this.invalidate();
                }
            });
            this.mValueAnimator.start();
            return;
        }
        this.mPathDrawFraction = 1.0f;
        populatePath();
        invalidate();
    }

    private void createPathPoints() {
        if (this.mPathCoordinates == null) {
            throw new IllegalStateException("mPathCoordinates cannot be null.");
        }
        if (this.mPathCoordinates.size() < 2) {
            throw new IllegalStateException("mPathCoordinates must have at least 2 coordinates.");
        }
        if (CommonUtils.areDoublesEqual(this.mWidth, Constants.SPLITS_ACCURACY)) {
            throw new IllegalStateException("mWidth cannot be zero.");
        }
        if (CommonUtils.areDoublesEqual(this.mHeight, Constants.SPLITS_ACCURACY)) {
            throw new IllegalStateException("mHeight cannot be zero.");
        }
        if (CommonUtils.areDoublesEqual(this.mDrawableSquareSide, Constants.SPLITS_ACCURACY)) {
            throw new IllegalStateException("mDrawableSquareSide cannot be zero.");
        }
        this.mPathPoints.clear();
        this.mTotalLength = 0.0f;
        RectF bounds = getBounds(this.mPathCoordinates, this.mDrawableSquareSide);
        this.mStartPoint = scaleToBounds(convertToPoint(this.mPathCoordinates.get(0), this.mDrawableSquareSide), bounds, this.mWidth, this.mHeight, this.mDrawableSquareSide);
        this.mEndPoint = scaleToBounds(convertToPoint(this.mPathCoordinates.get(this.mPathCoordinates.size() - 1), this.mDrawableSquareSide), bounds, this.mWidth, this.mHeight, this.mDrawableSquareSide);
        PointF previousPoint = this.mStartPoint;
        for (GeoCoordinate g : this.mPathCoordinates) {
            PointF point = scaleToBounds(convertToPoint(g, this.mDrawableSquareSide), bounds, this.mWidth, this.mHeight, this.mDrawableSquareSide);
            this.mPathPoints.add(point);
            this.mTotalLength += computeLength(point, previousPoint);
            previousPoint = point;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populatePath() {
        double deltaX;
        double deltaY;
        if (CommonUtils.areDoublesEqual(this.mTotalLength, Constants.SPLITS_ACCURACY)) {
            throw new IllegalStateException("mTotalLength cannot be zero");
        }
        this.mGeoPath.reset();
        this.mGeoPath.moveTo(this.mStartPoint.x, this.mStartPoint.y);
        if (CommonUtils.areDoublesEqual(this.mPathDrawFraction, 1.0d)) {
            for (PointF point : this.mPathPoints) {
                this.mGeoPath.lineTo(point.x, point.y);
            }
            return;
        }
        PointF previousPoint = this.mStartPoint;
        float targetLength = this.mTotalLength * this.mPathDrawFraction;
        float accumulatedLength = 0.0f;
        for (PointF point2 : this.mPathPoints) {
            float length = computeLength(point2, previousPoint);
            if (accumulatedLength + length <= targetLength) {
                this.mGeoPath.lineTo(point2.x, point2.y);
                accumulatedLength += length;
                previousPoint = point2;
            } else {
                float diffX = point2.x - previousPoint.x;
                float diffY = point2.y - previousPoint.y;
                float lengthNeeded = targetLength - accumulatedLength;
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    double theta = Math.atan(diffY / diffX);
                    deltaX = Math.abs(lengthNeeded * Math.cos(theta));
                    deltaY = Math.abs(lengthNeeded * Math.sin(theta));
                } else {
                    double theta2 = Math.atan(diffX / diffY);
                    deltaX = Math.abs(lengthNeeded * Math.sin(theta2));
                    deltaY = Math.abs(lengthNeeded * Math.cos(theta2));
                }
                if (diffX < 0.0f) {
                    deltaX *= -1.0d;
                }
                if (diffY < 0.0f) {
                    deltaY *= -1.0d;
                }
                PointF interpolatedPoint = new PointF((float) (previousPoint.x + deltaX), (float) (previousPoint.y + deltaY));
                this.mGeoPath.lineTo(interpolatedPoint.x, interpolatedPoint.y);
                return;
            }
        }
    }

    private void stopAnimations() {
        if (this.mValueAnimator != null && this.mValueAnimator.isRunning()) {
            this.mValueAnimator.end();
            this.mValueAnimator.removeAllUpdateListeners();
            this.mValueAnimator = null;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.mGeoPath, this.mPathPaint);
        if (this.mStartPoint != null) {
            canvas.drawCircle(this.mStartPoint.x, this.mStartPoint.y, this.mCircleMarkerRadius, this.mCircleMarkerFillPaint);
            canvas.drawCircle(this.mStartPoint.x, this.mStartPoint.y, this.mCircleMarkerRadius, this.mCircleMarkerStrokePaint);
        }
        if (this.mEndPoint != null && CommonUtils.areDoublesEqual(this.mPathDrawFraction, 1.0d)) {
            canvas.drawCircle(this.mEndPoint.x, this.mEndPoint.y, this.mCircleMarkerRadius, this.mCircleMarkerFillPaint);
            canvas.drawCircle(this.mEndPoint.x, this.mEndPoint.y, this.mCircleMarkerRadius, this.mCircleMarkerStrokePaint);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int extraPadding = (this.mCircleMarkerRadius * 2) + this.mCircleMarkerStrokeWidth;
        int xPadding = getPaddingLeft() + getPaddingRight() + extraPadding;
        int yPadding = getPaddingTop() + getPaddingBottom() + extraPadding;
        this.mWidth = w;
        this.mHeight = h;
        this.mDrawableSquareSide = Math.min(this.mWidth - xPadding, this.mHeight - yPadding);
        createAndAnimateRoute();
    }
}

package com.microsoft.blackbirdkeyboard;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class CoordinateTransform {
    private Matrix mInputTransform;
    private Matrix mOutputTransform;
    private Matrix mScaleDpi = new Matrix();
    private Matrix mTransformLayoutToDevice = new Matrix();
    private Matrix mTransformDeviceToDeviceWithMargins = new Matrix();
    private Point mMarginTopLeft = new Point();
    private float[] mDebugMatrix = new float[9];
    private float[] mVector2D = new float[2];

    public static Matrix getLayoutToDeviceTransform(int layoutWidth, int layoutHeight, int layoutEffectiveHeight, int deviceWidth, int deviceHeight) {
        double s;
        double tx;
        double ty;
        if (layoutWidth <= 0 || layoutHeight <= 0 || layoutEffectiveHeight <= 0 || deviceWidth <= 0 || deviceHeight <= 0) {
            throw new IllegalArgumentException("Layout and device dimensions must be positive");
        }
        if (layoutEffectiveHeight > layoutHeight) {
            throw new IllegalArgumentException("Layout effective height must be smaller or equal to layout height");
        }
        if (deviceHeight * layoutWidth < deviceWidth * layoutEffectiveHeight) {
            s = deviceHeight / layoutHeight;
            tx = 0.5d * (deviceWidth - (layoutWidth * s));
            ty = Constants.SPLITS_ACCURACY;
        } else if (deviceWidth * layoutHeight < deviceHeight * layoutWidth) {
            s = deviceWidth / layoutWidth;
            tx = Constants.SPLITS_ACCURACY;
            ty = 0.5d * (deviceHeight - (layoutHeight * s));
        } else {
            s = deviceWidth / layoutWidth;
            tx = Constants.SPLITS_ACCURACY;
            ty = Constants.SPLITS_ACCURACY;
        }
        Matrix Sout = new Matrix();
        Sout.setScale((float) s, (float) s);
        Sout.postTranslate((float) tx, (float) ty);
        return Sout;
    }

    public static void adjustMargins(int deviceWidth, int deviceHeight, Rect marginRect) {
        double dx = (deviceWidth - marginRect.left) - marginRect.right;
        double dy = (deviceHeight - marginRect.top) - marginRect.bottom;
        double cx = marginRect.left + (dx / 2.0d);
        double cy = marginRect.top + (dy / 2.0d);
        if (deviceWidth * dy > deviceHeight * dx) {
            dy = (deviceHeight * dx) / deviceWidth;
        } else {
            dx = (deviceWidth * dy) / deviceHeight;
        }
        int newLeft = (int) Math.round(cx - (dx / 2.0d));
        int newRight = (int) Math.round((dx / 2.0d) + cx);
        int newTop = (int) Math.round(cy - (dy / 2.0d));
        int newBottom = (int) Math.round((dy / 2.0d) + cy);
        marginRect.left = Math.max(0, newLeft);
        marginRect.right = Math.max(0, deviceWidth - newRight);
        marginRect.top = Math.max(0, newTop);
        marginRect.bottom = Math.max(0, deviceHeight - newBottom);
    }

    public static Matrix getDeviceToDeviceWithMarginTransform(int deviceWidth, int deviceHeight, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        double tx = marginLeft;
        double ty = marginTop;
        double sx = 1.0d - ((marginRight + marginLeft) / deviceWidth);
        double sy = 1.0d - ((marginBottom + marginTop) / deviceHeight);
        Matrix Mout = new Matrix();
        Mout.setScale((float) sx, (float) sy);
        Mout.postTranslate((float) tx, (float) ty);
        return Mout;
    }

    private synchronized String printTransform(Matrix transform) {
        transform.getValues(this.mDebugMatrix);
        return "{" + this.mDebugMatrix[0] + ", " + this.mDebugMatrix[1] + ", " + this.mDebugMatrix[2] + "; " + this.mDebugMatrix[3] + ", " + this.mDebugMatrix[4] + ", " + this.mDebugMatrix[5] + "; " + this.mDebugMatrix[6] + ", " + this.mDebugMatrix[7] + ", " + this.mDebugMatrix[8] + "}";
    }

    private void cacheInputOutputTransforms() {
        this.mOutputTransform = new Matrix(this.mTransformLayoutToDevice);
        this.mOutputTransform.postConcat(this.mTransformDeviceToDeviceWithMargins);
        if (this.mInputTransform == null) {
            this.mInputTransform = new Matrix();
        }
        this.mOutputTransform.invert(this.mInputTransform);
    }

    public CoordinateTransform(WearableDeviceInfo wearableDeviceInfo) {
        float dpiScale = wearableDeviceInfo.getCurrentDeviceDpiScale();
        this.mScaleDpi.setScale(dpiScale, dpiScale);
        Log.d("CoordinateTransform", "T_dpi2pix=" + printTransform(this.mScaleDpi));
        cacheInputOutputTransforms();
    }

    public void layoutInitialization(WearableDeviceInfo wearableDeviceInfo, KeyboardLayoutInfo keyboardLayoutInfo) {
        this.mTransformLayoutToDevice = getLayoutToDeviceTransform(keyboardLayoutInfo.getKeyboardWidth(), keyboardLayoutInfo.getKeyboardHeight(), keyboardLayoutInfo.getKeyboardYmax() + 1, wearableDeviceInfo.getCurrentDeviceSize().x, wearableDeviceInfo.getCurrentDeviceSize().y);
        Log.d("CoordinateTransform", "T_layout2pix=" + printTransform(this.mTransformLayoutToDevice));
        Rect marginRect = new Rect(wearableDeviceInfo.getCurrentDevice().getMarginRect());
        adjustMargins(wearableDeviceInfo.getCurrentDeviceSize().x, wearableDeviceInfo.getCurrentDeviceSize().y, marginRect);
        Log.d("CoordinateTransform", "margin={" + wearableDeviceInfo.getCurrentDevice().mMarginLeft + "," + wearableDeviceInfo.getCurrentDevice().mMarginTop + "," + wearableDeviceInfo.getCurrentDevice().mMarginRight + "," + wearableDeviceInfo.getCurrentDevice().mMarginBottom + "} adjusted={" + marginRect.left + "," + marginRect.top + "," + marginRect.right + "," + marginRect.bottom + "}");
        this.mTransformDeviceToDeviceWithMargins = getDeviceToDeviceWithMarginTransform(wearableDeviceInfo.getCurrentDeviceSize().x, wearableDeviceInfo.getCurrentDeviceSize().y, marginRect.left, marginRect.top, marginRect.right, marginRect.bottom);
        float[] m = new float[9];
        this.mTransformDeviceToDeviceWithMargins.getValues(m);
        this.mMarginTopLeft.x = Math.round(m[2]);
        this.mMarginTopLeft.y = Math.round(m[5]);
        Log.d("CoordinateTransform", "T_pix2marg=" + printTransform(this.mTransformDeviceToDeviceWithMargins));
        cacheInputOutputTransforms();
        Log.d("CoordinateTransform", "T_output=" + printTransform(this.mOutputTransform));
        Log.d("CoordinateTransform", "T_input=" + printTransform(this.mInputTransform));
    }

    public Matrix getTotalImageTransform() {
        Matrix totalTransform = new Matrix(this.mScaleDpi);
        totalTransform.postConcat(getOutputTransform());
        Log.d("CoordinateTransform", "T_totalImage=" + printTransform(totalTransform));
        return totalTransform;
    }

    private Matrix getOutputTransform() {
        return this.mOutputTransform;
    }

    private Matrix getInputTransform() {
        return this.mInputTransform;
    }

    public synchronized float outputTransformHorizontalDistance(float dx) {
        this.mVector2D[0] = dx;
        this.mVector2D[1] = 0.0f;
        getOutputTransform().mapVectors(this.mVector2D);
        return this.mVector2D[0];
    }

    public synchronized float outputTransformVerticalDistance(float dy) {
        this.mVector2D[0] = 0.0f;
        this.mVector2D[1] = dy;
        getOutputTransform().mapVectors(this.mVector2D);
        return this.mVector2D[1];
    }

    public void outputTransformPoint(float[] point) {
        getOutputTransform().mapPoints(point);
    }

    public void inputTransformPoint(float[] point) {
        getInputTransform().mapPoints(point);
    }

    public Point getMarginTopLeft() {
        return this.mMarginTopLeft;
    }
}

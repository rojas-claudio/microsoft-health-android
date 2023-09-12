package com.microsoft.band.device.keyboard;

import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class KeyboardEventPushMessage {
    private static final int MAX_KBD_STROKE_POINTS = 10;
    private int mLocale;
    private int mLocaleLanguage;
    private int mNumPoints;
    private ArrayList<CompactTouchSample> mPoints;
    private KeyboardMessageType mType;
    private byte mWordIndex;
    private static final String TAG = KeyboardEventPushMessage.class.getSimpleName();
    private static final String KEYBOARD_TAG = TAG + ": " + InternalBandConstants.KEYBOARD_BASE_TAG;

    public KeyboardEventPushMessage(ByteBuffer buffer) {
        KDKLog.d(KEYBOARD_TAG, "Got Keyboard event push message");
        this.mType = KeyboardMessageType.valueOf(buffer.get());
        this.mWordIndex = buffer.get();
        if (this.mType == KeyboardMessageType.Stroke) {
            this.mNumPoints = buffer.getInt();
            KDKLog.d(KEYBOARD_TAG, "Got message " + this.mType + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + ((int) this.mWordIndex) + " mNumPoints " + this.mNumPoints);
            this.mPoints = new ArrayList<>();
            for (int i = 0; i < this.mNumPoints && i < 10; i++) {
                CompactTouchSample sample = new CompactTouchSample(buffer);
                this.mPoints.add(sample);
            }
        }
        if (this.mType == KeyboardMessageType.PreInitV2) {
            this.mLocale = buffer.getShort();
            this.mLocaleLanguage = buffer.getShort();
            KDKLog.d(KEYBOARD_TAG, "User Locale " + this.mLocale + " lanauge " + this.mLocaleLanguage);
        }
    }

    public KeyboardMessageType getType() {
        return this.mType;
    }

    public byte getWordIndex() {
        return this.mWordIndex;
    }

    public ArrayList<CompactTouchSample> getPoints() {
        return this.mPoints;
    }

    public int getLocale() {
        return this.mLocale;
    }

    public int getLocaleLanguage() {
        return this.mLocaleLanguage;
    }
}

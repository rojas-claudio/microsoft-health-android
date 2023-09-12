package com.microsoft.blackbirdkeyboard;

import android.content.res.AssetManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BlackbirdDecoder {
    private AssetManager mAssetManager;
    private long mNativeHandle;
    private final boolean mMarkStrokeBoundaries = false;
    private final float[] mBoundaryPacket = {-500.0f, -500.0f};

    private native long NativeCreateDecoder(Object obj, String str, String str2, float f);

    private native void NativeDisposeDecoder(long j);

    private native void NativeDisposeResults(long j);

    private native int NativeGetCountResults(long j);

    private native long NativeGetResults(long j, boolean z);

    private native double NativeGetScoreResults(long j, int i);

    private native String NativeGetTextResults(long j, int i);

    private native void NativeSetContextDecoder(long j, String str);

    private native int NativeUpdateDecoder(long j, float f, float f2, int i);

    /* loaded from: classes.dex */
    public enum DecoderStatus {
        CandidateWordAvailable,
        RecognitionAvailable,
        NoMatch;

        public int toInt() {
            switch (this) {
                case CandidateWordAvailable:
                    return 0;
                case RecognitionAvailable:
                    return 1;
                case NoMatch:
                    return 2;
                default:
                    throw new IndexOutOfBoundsException("DecoderStatus unknown enum value");
            }
        }

        public static DecoderStatus fromInt(int i) {
            switch (i) {
                case 0:
                    return CandidateWordAvailable;
                case 1:
                    return RecognitionAvailable;
                case 2:
                    return NoMatch;
                default:
                    throw new IndexOutOfBoundsException("DecoderStatus unknown enum value");
            }
        }
    }

    /* loaded from: classes.dex */
    public enum TouchEvent {
        TouchDown,
        TouchMove,
        TouchUp;

        public int toInt() {
            switch (this) {
                case TouchDown:
                    return 0;
                case TouchMove:
                    return 1;
                case TouchUp:
                    return 2;
                default:
                    throw new IndexOutOfBoundsException("TouchEvent unknown enum value");
            }
        }

        public static TouchEvent fromInt(int i) {
            switch (i) {
                case 0:
                    return TouchDown;
                case 1:
                    return TouchMove;
                case 2:
                    return TouchUp;
                default:
                    throw new IndexOutOfBoundsException("TouchEvent unknown enum value");
            }
        }
    }

    static {
        System.loadLibrary("blackbirddecoder");
    }

    public BlackbirdDecoder(AssetManager assetManager, String layout, String language, float languageModelWeight) {
        if (assetManager == null) {
            throw new RuntimeException("Cannot construct Blackbird Decoder without AssetManager");
        }
        this.mAssetManager = assetManager;
        this.mNativeHandle = NativeCreateDecoder(assetManager, layout, language, languageModelWeight);
        if (this.mNativeHandle == 0) {
            throw new RuntimeException("Failed to construct Blackbird Decoder native object");
        }
    }

    public synchronized void dispose() {
        NativeDisposeDecoder(this.mNativeHandle);
        this.mNativeHandle = 0L;
        this.mAssetManager = null;
    }

    public synchronized void setContext(String context) {
        if (this.mNativeHandle == 0) {
            throw new RuntimeException("Attempted setContext() call to zombie object");
        }
        NativeSetContextDecoder(this.mNativeHandle, context);
    }

    public synchronized DecoderStatus update(float x, float y, TouchEvent event) {
        if (this.mNativeHandle == 0) {
            throw new RuntimeException("Attempted update() call to zombie object");
        }
        return DecoderStatus.fromInt(NativeUpdateDecoder(this.mNativeHandle, x, y, event.toInt()));
    }

    public synchronized List<RecognitionResult> getResults(boolean isEndOfWord) {
        List<RecognitionResult> recoResults;
        if (this.mNativeHandle == 0) {
            throw new RuntimeException("Attempted getResults() call to zombie object");
        }
        recoResults = new ArrayList<>();
        long nativeHandleResults = NativeGetResults(this.mNativeHandle, isEndOfWord);
        if (nativeHandleResults != 0) {
            int nAlternates = NativeGetCountResults(nativeHandleResults);
            for (int iAlternate = 0; iAlternate < nAlternates; iAlternate++) {
                RecognitionResult recoResult = new RecognitionResult();
                recoResult.text = NativeGetTextResults(nativeHandleResults, iAlternate);
                recoResult.score = NativeGetScoreResults(nativeHandleResults, iAlternate);
                recoResults.add(recoResult);
            }
            NativeDisposeResults(nativeHandleResults);
        } else {
            recoResults = null;
        }
        return recoResults;
    }
}

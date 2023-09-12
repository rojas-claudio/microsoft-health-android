package com.microsoft.blackbirdkeyboardtest;

import android.content.res.AssetManager;
import com.microsoft.blackbirdkeyboard.BlackbirdDecoder;
import com.microsoft.blackbirdkeyboard.RecognitionResult;
import com.microsoft.blackbirdkeyboardtest.Phrase;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ApiTest {
    static final /* synthetic */ boolean $assertionsDisabled;
    private BlackbirdDecoder mDecoder;
    private Phrases mPhrases;

    static {
        $assertionsDisabled = !ApiTest.class.desiredAssertionStatus();
    }

    public ApiTest(AssetManager assetManager, String phraseset) {
        String layout;
        if (phraseset == "gearlive") {
            layout = "layout_rect";
        } else if (phraseset == "moto360") {
            layout = "layout_round";
        } else {
            throw new IllegalArgumentException("Unknown phrase set " + phraseset);
        }
        this.mDecoder = new BlackbirdDecoder(assetManager, layout, "English", 2.0f);
        this.mPhrases = new Phrases(assetManager, phraseset + ".txt");
    }

    public int numTestCases() {
        return this.mPhrases.mPhrases.size();
    }

    public String runTestCase(int idx) {
        BlackbirdDecoder.TouchEvent touchEvent;
        if (idx < 0 || idx >= this.mPhrases.mPhrases.size()) {
            throw new RuntimeException("Unknown test case " + idx);
        }
        List<List<RecognitionResult>> phraseResult = new ArrayList<>();
        String context = "";
        this.mDecoder.setContext("");
        List<List<int[]>> strokes = this.mPhrases.mPhrases.get(idx).Strokes;
        if ($assertionsDisabled || this.mPhrases.mPhrases.get(idx).PtFormat == Phrase.PointFormat.X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure || this.mPhrases.mPhrases.get(idx).PtFormat == Phrase.PointFormat.X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure_Ax_Ay_Az_Atms) {
            for (int iStroke = 0; iStroke < strokes.size(); iStroke++) {
                List<int[]> packets = strokes.get(iStroke);
                for (int iPacket = 0; iPacket < packets.size(); iPacket++) {
                    int[] packet = packets.get(iPacket);
                    float x = packet[0];
                    float y = packet[1];
                    int contactState = packet[3];
                    switch (contactState) {
                        case 1:
                            touchEvent = BlackbirdDecoder.TouchEvent.TouchMove;
                            break;
                        case 2:
                            touchEvent = BlackbirdDecoder.TouchEvent.TouchUp;
                            break;
                        case 3:
                            touchEvent = BlackbirdDecoder.TouchEvent.TouchDown;
                            break;
                        default:
                            throw new RuntimeException("Unknown ContactState " + contactState);
                    }
                    BlackbirdDecoder.DecoderStatus decoderStatus = this.mDecoder.update(x, y, touchEvent);
                    if (decoderStatus == BlackbirdDecoder.DecoderStatus.RecognitionAvailable) {
                        List<RecognitionResult> wordResults = this.mDecoder.getResults(true);
                        if (wordResults.size() > 0) {
                            phraseResult.add(wordResults);
                            context = context + wordResults.get(0).text;
                            if (!context.endsWith(".") && !context.endsWith(",")) {
                                context = context + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
                            }
                        }
                        this.mDecoder.setContext(context);
                    }
                }
            }
            return context;
        }
        throw new AssertionError();
    }

    public void dispose() {
        this.mDecoder.dispose();
    }
}

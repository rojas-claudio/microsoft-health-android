package com.microsoft.band.device.keyboard;

import android.content.res.AssetManager;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.device.PushServicePayload;
import com.microsoft.blackbirdkeyboard.BlackbirdDecoder;
import com.microsoft.blackbirdkeyboard.RecognitionResult;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class KeyboardManager {
    private static final String DanishLanguageName = "Danish";
    private static final String DutchLanguageName = "Dutch";
    private static final String EnglishLanguageName = "English";
    private static final String FinnishLanguageName = "Finnish";
    private static final String FrenchLanguageName = "French";
    private static final String GermanLanguageName = "German";
    private static final String ItalianLanguageName = "Italian";
    private static final String JapaneseLanguageName = "Japanese";
    private static final String KoreanLanguageName = "Korean";
    private static final String NorwegianLanguageName = "Norwegian";
    private static final String PortugueseLanguageName = "Portuguese";
    private static final char PtSeparator = '|';
    private static final String SimplifiedChineseLanguageName = "SimplifiedChinese";
    private static final String SpanishLanguageName = "Spanish";
    private static final char StrokeSeparator = '/';
    private static final String SwedishLanguageName = "Swedish";
    private static final String TraditionalChineseLanguageName = "TraditionalChinese";
    private static final char XYSeparator = ',';
    private DeviceServiceProvider mServiceProvider;
    private static final String TAG = KeyboardManager.class.getSimpleName();
    private static final String KEYBOARD_TAG = TAG + ": " + InternalBandConstants.KEYBOARD_BASE_TAG;
    private static HashMap<Number, String> LanguageIdToNameMap = new HashMap<Number, String>() { // from class: com.microsoft.band.device.keyboard.KeyboardManager.1
        private static final long serialVersionUID = 1;

        {
            put(1, KeyboardManager.EnglishLanguageName);
            put(2, KeyboardManager.EnglishLanguageName);
            put(3, KeyboardManager.FrenchLanguageName);
            put(4, KeyboardManager.FrenchLanguageName);
            put(5, KeyboardManager.GermanLanguageName);
            put(6, KeyboardManager.ItalianLanguageName);
            put(7, KeyboardManager.SpanishLanguageName);
            put(8, KeyboardManager.SpanishLanguageName);
            put(9, KeyboardManager.SpanishLanguageName);
            put(10, KeyboardManager.DanishLanguageName);
            put(11, KeyboardManager.FinnishLanguageName);
            put(12, KeyboardManager.NorwegianLanguageName);
            put(13, KeyboardManager.DutchLanguageName);
            put(14, KeyboardManager.PortugueseLanguageName);
            put(15, KeyboardManager.SwedishLanguageName);
            put(16, KeyboardManager.EnglishLanguageName);
            put(17, KeyboardManager.SimplifiedChineseLanguageName);
            put(18, KeyboardManager.TraditionalChineseLanguageName);
            put(19, KeyboardManager.JapaneseLanguageName);
            put(20, KeyboardManager.KoreanLanguageName);
        }
    };
    private static ArrayList<String> supportedLanguages = new ArrayList<String>() { // from class: com.microsoft.band.device.keyboard.KeyboardManager.2
        private static final long serialVersionUID = 1;

        {
            add(KeyboardManager.EnglishLanguageName);
            add(KeyboardManager.FrenchLanguageName);
            add(KeyboardManager.ItalianLanguageName);
            add(KeyboardManager.GermanLanguageName);
            add(KeyboardManager.SpanishLanguageName);
        }
    };
    private final ExecutorService mExecutorService = new ThreadPoolExecutor(0, 1, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private KeyboardMessageType mPrevMessageType = KeyboardMessageType.TryReleaseClient;
    private StringBuilder mRecoSoFar = new StringBuilder();
    private int mStroke = 0;
    private String mKeyboardLanguage = EnglishLanguageName;
    private String mTapsForLastWord = "";
    private BlackbirdDecoder mKeyboardDecoder = null;

    public KeyboardManager(DeviceServiceProvider serviceProvider) {
        this.mServiceProvider = serviceProvider;
    }

    private void setLanguage(String languageName) {
        KDKLog.v(KEYBOARD_TAG, "Setting keyboard language to " + languageName);
        if (!this.mKeyboardLanguage.equals(languageName)) {
            releaseDecoder();
            this.mKeyboardLanguage = languageName;
        }
        loadDecoder();
    }

    private void releaseDecoder() {
        if (this.mKeyboardDecoder != null) {
            KDKLog.d(KEYBOARD_TAG, "Releasing decoder");
            this.mKeyboardDecoder.dispose();
            this.mKeyboardDecoder = null;
        }
    }

    private void loadDecoder() {
        try {
            if (this.mKeyboardDecoder == null) {
                AssetManager assetManager = this.mServiceProvider.getContext().getAssets();
                KDKLog.v(KEYBOARD_TAG, "Loading decoder");
                float languageModelWeight = 1.0f;
                if (this.mKeyboardLanguage == EnglishLanguageName) {
                    languageModelWeight = 1.0f;
                } else if (this.mKeyboardLanguage == SpanishLanguageName) {
                    languageModelWeight = 4.0f;
                } else if (this.mKeyboardLanguage == FrenchLanguageName) {
                    languageModelWeight = 5.0f;
                } else if (this.mKeyboardLanguage == ItalianLanguageName) {
                    languageModelWeight = 6.0f;
                } else if (this.mKeyboardLanguage == GermanLanguageName || this.mKeyboardLanguage == PortugueseLanguageName) {
                    languageModelWeight = 8.0f;
                }
                this.mKeyboardDecoder = new BlackbirdDecoder(assetManager, "neon", this.mKeyboardLanguage, languageModelWeight);
                KDKLog.v(KEYBOARD_TAG, "decoder loaded");
                return;
            }
            KDKLog.v(KEYBOARD_TAG, "decoder already loaded");
        } catch (CargoServiceException e) {
            KDKLog.e(KEYBOARD_TAG, e, "Exception when loading decoder", new Object[0]);
        }
    }

    public void processContextPushMessage(final PushServicePayload payload) {
        this.mExecutorService.execute(new Runnable() { // from class: com.microsoft.band.device.keyboard.KeyboardManager.3
            @Override // java.lang.Runnable
            public void run() {
                ByteBuffer payloadDataBuffer = ByteBuffer.wrap(payload.getData());
                payloadDataBuffer.order(ByteOrder.LITTLE_ENDIAN);
                KeyboardContextPushMessage message = new KeyboardContextPushMessage(payloadDataBuffer);
                KeyboardManager.this.setContext(message.getContext());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setContext(String context) {
        KDKLog.d(KEYBOARD_TAG, "start Keyboard/set context");
        KDKLog.i(KEYBOARD_TAG, "Keyboard Set context: " + context);
        this.mKeyboardDecoder.setContext(context);
        KDKLog.d(KEYBOARD_TAG, "end Keyboard/set context");
        this.mRecoSoFar.setLength(0);
        this.mRecoSoFar.append(context);
        this.mPrevMessageType = KeyboardMessageType.Init;
        this.mStroke = 0;
    }

    public void processKeyboardEventPushMessage(final PushServicePayload payload) {
        this.mExecutorService.execute(new Runnable() { // from class: com.microsoft.band.device.keyboard.KeyboardManager.4
            @Override // java.lang.Runnable
            public void run() {
                ByteBuffer payloadDataBuffer = ByteBuffer.wrap(payload.getData());
                payloadDataBuffer.order(ByteOrder.LITTLE_ENDIAN);
                KeyboardEventPushMessage message = new KeyboardEventPushMessage(payloadDataBuffer);
                KeyboardManager.this.processKeyboardEventPushMessage(message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processKeyboardEventPushMessage(KeyboardEventPushMessage msg) {
        switch (msg.getType()) {
            case Init:
                ProcessInitMessage(msg);
                return;
            case Stroke:
                ProcessStroke(msg);
                return;
            case CandidatesForNextWord:
                ProcessCandidatesForNextWordMessage(msg);
                return;
            case CandidatesForWord:
                ProcessCandidatesForWordMessage(msg);
                return;
            case End:
                ProcessEndMessage(msg);
                return;
            case PreInitV2:
                processPreInitMessage(msg);
                return;
            case PreInit:
            case TryReleaseClient:
            default:
                return;
        }
    }

    private void processPreInitMessage(KeyboardEventPushMessage msg) {
        KDKLog.d(KEYBOARD_TAG, "Previous message " + this.mPrevMessageType);
        this.mPrevMessageType = msg.getType();
        KDKLog.d(KEYBOARD_TAG, "start Keyboard/preInit");
        int languageId = msg.getLocaleLanguage();
        if (!LanguageIdToNameMap.containsKey(Integer.valueOf(languageId))) {
            KDKLog.e(KEYBOARD_TAG, "Got an invalid languague key. Defaulting to english");
            languageId = 1;
        }
        String languageName = LanguageIdToNameMap.get(Integer.valueOf(languageId));
        setLanguage(languageName);
        KDKLog.i(KEYBOARD_TAG, "end Keyboard/preInit");
    }

    private void ProcessInitMessage(KeyboardEventPushMessage msg) {
        KDKLog.d(KEYBOARD_TAG, "Previous message " + this.mPrevMessageType);
        this.mPrevMessageType = msg.getType();
        KDKLog.d(KEYBOARD_TAG, "start Keyboard/session");
        KDKLog.d(KEYBOARD_TAG, "start Keyboard/init");
        if (!supportedLanguages.contains(this.mKeyboardLanguage)) {
            KDKLog.e(KEYBOARD_TAG, "Got init with unsupported language. Not sending Inited Message");
            return;
        }
        loadDecoder();
        KeyboardCommand messageData = NewInitedMessage();
        ForwardKeyboardMessage(messageData);
        KDKLog.i(KEYBOARD_TAG, "keyboard init sent");
        KDKLog.i(KEYBOARD_TAG, "initing keyboard");
        EnsureRecognizer();
        KDKLog.i(KEYBOARD_TAG, "keyboard inited");
        KDKLog.i(KEYBOARD_TAG, "end Keyboard/init");
        ClearState();
        this.mKeyboardDecoder.update(-500.0f, -500.0f, BlackbirdDecoder.TouchEvent.TouchDown);
    }

    private void ClearState() {
        this.mRecoSoFar.setLength(0);
        this.mTapsForLastWord = "";
        this.mKeyboardDecoder.setContext("");
    }

    private void EnsureRecognizer() {
    }

    private void ForwardKeyboardMessage(KeyboardCommand messageData) {
        this.mServiceProvider.processCommand(messageData);
    }

    private KeyboardCommand NewInitedMessage() {
        KeyboardCommand command = new KeyboardCommand(KeyboardMessageType.Init);
        return command;
    }

    private void ProcessEndMessage(KeyboardEventPushMessage msg) {
        this.mPrevMessageType = msg.getType();
        ClearState();
        KDKLog.d(KEYBOARD_TAG, "end Keyboard/session");
    }

    private void ProcessCandidatesForWordMessage(KeyboardEventPushMessage msg) {
        this.mPrevMessageType = msg.getType();
        List<RecognitionResult> completionResults = this.mKeyboardDecoder.getResults(false);
        List<String> completionCandidates = new ArrayList<>(10);
        KDKLog.d(KEYBOARD_TAG, "end Keyboard/word, get completions");
        KDKLog.i(KEYBOARD_TAG, "Keyboard/Number of taps Received " + this.mStroke);
        if (completionResults != null && completionResults.size() > 0) {
            for (RecognitionResult predCand : completionResults) {
                completionCandidates.add(predCand.text);
            }
            this.mRecoSoFar.append(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
            this.mRecoSoFar.append(completionResults.get(0).text);
            KDKLog.d(KEYBOARD_TAG, "start Keyboard/set context");
            KDKLog.i(KEYBOARD_TAG, "Keyboard Set context: [" + this.mRecoSoFar.toString() + "]");
            this.mKeyboardDecoder.setContext(this.mRecoSoFar.toString());
            KDKLog.d(KEYBOARD_TAG, "end Keyboard/set context");
        }
        this.mStroke = 0;
        KeyboardCommand completionCandidatesMsg = NewCandidatesMessage(KeyboardMessageType.CandidatesForWord, completionCandidates);
        this.mPrevMessageType = completionCandidatesMsg.getKeyboardMsgType();
        ForwardKeyboardMessage(completionCandidatesMsg);
    }

    private void ProcessCandidatesForNextWordMessage(KeyboardEventPushMessage msg) {
        KDKLog.d(KEYBOARD_TAG, "ProcessCandidatesForNextWordMessage");
    }

    private void ProcessStroke(KeyboardEventPushMessage msg) {
        this.mPrevMessageType = msg.getType();
        if (this.mStroke == 0) {
            KDKLog.d(KEYBOARD_TAG, "start Keyboard/word");
        }
        this.mStroke++;
        KDKLog.d(KEYBOARD_TAG, "start Keyboard/stroke");
        KDKLog.i(KEYBOARD_TAG, "Keyboard Received stroke: " + this.mStroke);
        List<String> candidates = SendStrokeToRecognizer(msg);
        KDKLog.d(KEYBOARD_TAG, "end Keyboard/stroke");
        if (candidates != null && candidates.size() > 0) {
            this.mTapsForLastWord = "";
            this.mRecoSoFar.append(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
            this.mRecoSoFar.append(candidates.get(0));
            KDKLog.d(KEYBOARD_TAG, "end Keyboard/word");
            KDKLog.i(KEYBOARD_TAG, "Keyboard/Number of taps Received " + this.mStroke + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + this.mTapsForLastWord);
            KeyboardCommand candmessageData = NewCandidatesMessage(KeyboardMessageType.CandidatesForWord, candidates);
            this.mPrevMessageType = candmessageData.getKeyboardMsgType();
            KDKLog.d(KEYBOARD_TAG, "start Keyboard/set context");
            this.mKeyboardDecoder.setContext(this.mRecoSoFar.toString());
            KDKLog.d(KEYBOARD_TAG, "end Keyboard/set context");
            this.mStroke = 0;
            if (candidates.get(0).trim().length() > 0) {
                ForwardKeyboardMessage(candmessageData);
            }
        }
    }

    private KeyboardCommand NewCandidatesMessage(KeyboardMessageType messageType, List<String> candidates) {
        KDKLog.d(KEYBOARD_TAG, "Keyboard Sending candidates: message: " + messageType);
        KeyboardCommand result = new KeyboardCommand(messageType, candidates);
        return result;
    }

    private List<String> SendStrokeToRecognizer(KeyboardEventPushMessage msg) {
        List<RecognitionResult> results;
        EnsureRecognizer();
        StringBuilder ptsSb = new StringBuilder();
        if (msg.getType() == KeyboardMessageType.Stroke) {
            ArrayList<CompactTouchSample> points = msg.getPoints();
            for (int iPt = 0; iPt < points.size(); iPt++) {
                CompactTouchSample point = points.get(iPt);
                float x = point.getX();
                float y = point.getY();
                if (ptsSb.length() > 0) {
                    ptsSb.append(PtSeparator);
                }
                ptsSb.append((int) x);
                ptsSb.append(XYSeparator);
                ptsSb.append((int) y);
                if (point.getTouchType() == KTouchType.Down) {
                    this.mKeyboardDecoder.update(-500.0f, -500.0f, BlackbirdDecoder.TouchEvent.TouchDown);
                }
                this.mKeyboardDecoder.update(x, y, BlackbirdDecoder.TouchEvent.TouchMove);
                if (point.getTouchType() == KTouchType.Up) {
                    BlackbirdDecoder.DecoderStatus status = this.mKeyboardDecoder.update(-500.0f, -500.0f, BlackbirdDecoder.TouchEvent.TouchUp);
                    KDKLog.i(KEYBOARD_TAG, "Keyboard Points: " + ptsSb.toString());
                    this.mTapsForLastWord += ptsSb.toString() + StrokeSeparator;
                    if (status == BlackbirdDecoder.DecoderStatus.RecognitionAvailable && (results = this.mKeyboardDecoder.getResults(false)) != null && results.size() > 0) {
                        List<String> candidates = new ArrayList<>(results.size());
                        for (RecognitionResult result : results) {
                            candidates.add(result.text);
                        }
                        return candidates;
                    }
                }
            }
        }
        return null;
    }
}

package com.microsoft.blackbirdkeyboard;

import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class PhraseResults {
    private List<List<RecognitionResult>> mPhraseResult = new ArrayList();
    private List<Integer> mAltSelection = new ArrayList();

    public void RecognitionResults() {
    }

    public String getString() {
        String context = "";
        for (int iWord = 0; iWord < this.mPhraseResult.size(); iWord++) {
            List<RecognitionResult> wordResults = this.mPhraseResult.get(iWord);
            int iAlt = this.mAltSelection.get(iWord).intValue();
            if (wordResults.size() > 0) {
                context = context + wordResults.get(iAlt).text;
                if (!context.endsWith(".") && !context.endsWith(",") && !context.endsWith(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE)) {
                    context = context + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
                }
            }
        }
        return context;
    }

    public void clear() {
        this.mPhraseResult.clear();
        this.mAltSelection.clear();
    }

    private boolean areAllSpaces(String text) {
        for (int iChar = 0; iChar < text.length(); iChar++) {
            if (text.charAt(iChar) != ' ') {
                return false;
            }
        }
        return true;
    }

    public void add(List<RecognitionResult> wordResults) {
        if (wordResults.size() > 0) {
            int iResultAlt = 0;
            for (int iAlt = 0; iAlt < wordResults.size(); iAlt++) {
                if (wordResults.get(iAlt).score > wordResults.get(iResultAlt).score) {
                    iResultAlt = iAlt;
                }
            }
            if (this.mPhraseResult.size() > 0 && wordResults.size() == 1 && areAllSpaces(wordResults.get(iResultAlt).text)) {
                int iWord = this.mPhraseResult.size() - 1;
                for (int iAlt2 = 0; iAlt2 < this.mPhraseResult.get(iWord).size(); iAlt2++) {
                    RecognitionResult result = this.mPhraseResult.get(iWord).get(iAlt2);
                    result.text += wordResults.get(0).text;
                }
                return;
            }
            this.mPhraseResult.add(wordResults);
            this.mAltSelection.add(Integer.valueOf(iResultAlt));
        }
    }

    public void deleteLast() {
        if (this.mPhraseResult.size() > 0) {
            this.mPhraseResult.remove(this.mPhraseResult.size() - 1);
            this.mAltSelection.remove(this.mAltSelection.size() - 1);
        }
    }

    static int getUIfromEngineAltIdxGeneric(int iEngAlt, int N) {
        return iEngAlt % 2 == 1 ? ((N / 2) - (iEngAlt / 2)) - 1 : (N / 2) + (iEngAlt / 2);
    }

    public int getUIfromEngineAltIdx(int iEngAlt, int iWord) {
        return getUIfromEngineAltIdxGeneric(iEngAlt, this.mPhraseResult.get(iWord).size());
    }

    static int getEngineFromUIAltIdxGeneric(int iUIAlt, int N) {
        return iUIAlt < N / 2 ? ((((N / 2) - iUIAlt) - 1) * 2) + 1 : (iUIAlt - (N / 2)) * 2;
    }

    public int getEngineFromUIAltIdx(int iUIAlt, int iWord) {
        return getEngineFromUIAltIdxGeneric(iUIAlt, this.mPhraseResult.get(iWord).size());
    }

    public int size() {
        return this.mPhraseResult.size();
    }

    public List<RecognitionResult> get(int iWord) {
        return this.mPhraseResult.get(iWord);
    }

    public int getSelectedAlt(int iWord) {
        return this.mAltSelection.get(iWord).intValue();
    }

    public void setSelectedAlt(int iWord, int iAlt) {
        this.mAltSelection.set(iWord, Integer.valueOf(iAlt));
    }
}

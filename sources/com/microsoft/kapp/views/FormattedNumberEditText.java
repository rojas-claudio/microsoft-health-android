package com.microsoft.kapp.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
/* loaded from: classes.dex */
public class FormattedNumberEditText extends CustomFontEditText {
    private static char separatorChar = ' ';
    private static String seperatorCharAsString = MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
    private static int SEGMENT_LENGTH = 4;

    public FormattedNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormattedNumberEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FormattedNumberEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() { // from class: com.microsoft.kapp.views.FormattedNumberEditText.1
            String mNewValue;
            int mSelection = 0;
            boolean mIsUserInitiatedTextChange = true;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence startLength, int start, int before, int count) {
                int i = 1;
                String originalText = checkForDeletedSeparator(before, count, start, startLength.toString());
                String cleanedValue = originalText.replace(FormattedNumberEditText.seperatorCharAsString, "");
                this.mNewValue = addDividersToText(cleanedValue);
                int i2 = (count == 1 && start % (FormattedNumberEditText.SEGMENT_LENGTH + 1) == FormattedNumberEditText.SEGMENT_LENGTH) ? 1 : 0;
                if (count != 0 || (start % (FormattedNumberEditText.SEGMENT_LENGTH + 1) != 0 && start % (FormattedNumberEditText.SEGMENT_LENGTH + 1) != FormattedNumberEditText.SEGMENT_LENGTH)) {
                    i = 0;
                }
                int separatorChange = i2 - i;
                if (this.mIsUserInitiatedTextChange) {
                    this.mSelection = start + count + separatorChange;
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                if (this.mIsUserInitiatedTextChange) {
                    this.mIsUserInitiatedTextChange = false;
                    FormattedNumberEditText.this.setText(this.mNewValue);
                    this.mSelection = Math.min(this.mSelection, this.mNewValue.length());
                    this.mSelection = Math.max(this.mSelection, 0);
                    FormattedNumberEditText.this.setSelection(this.mSelection);
                    this.mIsUserInitiatedTextChange = true;
                }
            }

            private String addDividersToText(String cleanedValue) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < cleanedValue.length(); i++) {
                    if (i > 0 && i % FormattedNumberEditText.SEGMENT_LENGTH == 0) {
                        sb.append(FormattedNumberEditText.separatorChar);
                    }
                    sb.append(cleanedValue.charAt(i));
                }
                return sb.toString();
            }

            private String checkForDeletedSeparator(int before, int count, int start, String incomingText) {
                if (before == 1 && before > count && this.mNewValue != null && this.mNewValue.charAt(start) == FormattedNumberEditText.separatorChar && start > 0 && start < incomingText.length()) {
                    String firstPart = incomingText.substring(0, start - 1);
                    String lastPart = incomingText.substring(start, incomingText.length());
                    int i = start - 1;
                    return firstPart.concat(lastPart);
                }
                return incomingText;
            }
        });
    }

    public void setNumber(String starbucksCardNumber) {
        setText(starbucksCardNumber);
    }

    public String getNumber() {
        String currentText = getText().toString();
        if (currentText != null) {
            return currentText.replace(seperatorCharAsString, "");
        }
        return null;
    }
}

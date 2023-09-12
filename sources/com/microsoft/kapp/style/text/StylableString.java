package com.microsoft.kapp.style.text;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.SparseArray;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class StylableString {
    public static final int DEFAULT_STYLE_RES_ID = -1;
    private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static Pattern fsPattern = Pattern.compile(formatSpecifier);

    public static SpannableString format(Context context, int resourceStringId, int styleArrayResId, Object... argsValues) {
        return format(context, context.getString(resourceStringId), styleArrayResId, argsValues);
    }

    public static SpannableString format(Context context, String resourceString, int styleArrayResId, Object... argValues) {
        int argumentIndex;
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        if (argValues == null || argValues.length < 1) {
            throw new IllegalArgumentException("argValues");
        }
        SparseArray<List<CharacterStyle>> styleArray = new SparseArray<>();
        if (styleArrayResId != -1) {
            styleArray = getStringStyles(context, styleArrayResId);
        }
        SpannableStringBuilder sb = new SpannableStringBuilder(resourceString);
        int i = 0;
        int argumentIndex2 = 0;
        while (i < sb.length()) {
            Matcher m = fsPattern.matcher(sb);
            if (!m.find(i)) {
                break;
            }
            int i2 = m.start();
            int end = m.end();
            String[] groups = new String[6];
            for (int j = 0; j < m.groupCount(); j++) {
                groups[j] = m.group(j + 1);
            }
            String argumentIndexString = groups[0];
            if (!TextUtils.isEmpty(argumentIndexString)) {
                argumentIndex = Integer.parseInt(argumentIndexString.substring(0, argumentIndexString.length() - 1));
            } else {
                argumentIndex = argumentIndex2 + 1;
            }
            argumentIndex2 = argumentIndex - 1;
            Object argValue = argValues[argumentIndex2];
            StringBuilder sbr = new StringBuilder();
            sbr.append("%");
            for (int j2 = 1; j2 < groups.length; j2++) {
                if (!TextUtils.isEmpty(groups[j2])) {
                    sbr.append(groups[j2]);
                }
            }
            String format = sbr.toString();
            String formattedValue = String.format(format, argValue);
            sb.replace(i2, end, (CharSequence) formattedValue);
            if (argValue instanceof Spanned) {
                Spanned spanned = (Spanned) argValue;
                TextUtils.copySpansFrom(spanned, 0, spanned.length(), Object.class, sb, i2);
            } else {
                List<CharacterStyle> applyStyles = styleArray.get(argumentIndex2);
                if (applyStyles != null && applyStyles.size() > 0) {
                    for (CharacterStyle style : applyStyles) {
                        sb.setSpan(style, i2, formattedValue.length() + i2, 34);
                    }
                }
            }
            i = i2 + formattedValue.length();
        }
        return new SpannableString(sb);
    }

    private static SparseArray<List<CharacterStyle>> getStringStyles(Context context, int styleArrayId) {
        CustomStyleToSpan styleToSpan = new CustomStyleToSpan();
        SparseArray<List<CharacterStyle>> styleArray = new SparseArray<>();
        String[] styleDefinedArray = context.getResources().getStringArray(styleArrayId);
        for (String item : styleDefinedArray) {
            String[] split = item.split("\\|");
            int index = Integer.parseInt(split[0]);
            String individualStyle = split[1];
            List<CharacterStyle> spans = styleToSpan.getSpans(context, individualStyle);
            styleArray.put(index, spans);
        }
        return styleArray;
    }
}

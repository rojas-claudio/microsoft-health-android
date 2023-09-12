package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class DottedLineView extends View {
    public DottedLineView(Context context, int lineColor) {
        super(context);
        setLayerType(1, null);
        GradientDrawable background = (GradientDrawable) getResources().getDrawable(R.drawable.shinobicharts_line);
        background.setStroke(getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_stroke), lineColor, getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_dash_width), getResources().getDimensionPixelSize(R.dimen.shinobicharts_average_line_dash_gap));
        setBackground(background);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }
}

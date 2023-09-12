package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class FlagView extends ViewGroup {
    private FlagPlacementStrategy flagPlacementStrategy;
    private FlagPointerDrawingStrategy flagPointerDrawingStrategy;
    private FlagMeasuringStrategy flagSizingStrategy;
    private final RectF placementRect;
    private final Paint pointerPaint;

    public FlagView(Context context) {
        super(context);
        this.pointerPaint = new Paint();
        this.placementRect = new RectF();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.shinobicharts_flag_text, (ViewGroup) this, true);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View flagTextView = getChildAt(0);
        measureChild(flagTextView, widthMeasureSpec, heightMeasureSpec);
        int childWidth = flagTextView.getMeasuredWidth();
        int childHeight = flagTextView.getMeasuredHeight();
        Point measurements = this.flagSizingStrategy.getMeasurements(widthMeasureSpec, heightMeasureSpec, childWidth, childHeight, getResources());
        this.placementRect.set(0.0f, 0.0f, measurements.x, measurements.y);
        setMeasuredDimension(measurements.x, measurements.y);
        this.flagPlacementStrategy.updatePlacementRect(this.placementRect, childWidth, childHeight, getResources());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View flagTextView = getChildAt(0);
        flagTextView.layout(0, 0, flagTextView.getMeasuredWidth(), flagTextView.getMeasuredHeight());
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.clipRect(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
        int flagTextViewWidth = getChildAt(0).getMeasuredWidth();
        int flagTextViewHeight = getChildAt(0).getMeasuredHeight();
        this.flagPointerDrawingStrategy.drawPointer(canvas, this.pointerPaint, flagTextViewWidth, flagTextViewHeight, this.placementRect, getResources());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(this.placementRect.left, this.placementRect.top);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public FlagMeasuringStrategy getFlagSizingStrategy() {
        return this.flagSizingStrategy;
    }

    public void setFlagSizingStrategy(FlagMeasuringStrategy flagSizingStrategy) {
        this.flagSizingStrategy = flagSizingStrategy;
    }

    public FlagPointerDrawingStrategy getFlagPointerDrawingStrategy() {
        return this.flagPointerDrawingStrategy;
    }

    public void setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy flagPointerDrawingStrategy) {
        this.flagPointerDrawingStrategy = flagPointerDrawingStrategy;
    }

    public FlagPlacementStrategy getFlagPlacementStrategy() {
        return this.flagPlacementStrategy;
    }

    public void setFlagPlacementStrategy(FlagPlacementStrategy flagPlacementStrategy) {
        this.flagPlacementStrategy = flagPlacementStrategy;
    }

    public void setCornerRadius(float cornerRadius) {
        View flagTextView = getChildAt(0);
        GradientDrawable background = (GradientDrawable) flagTextView.getBackground();
        background.setCornerRadius(cornerRadius);
    }

    public void setFlagColor(int color) {
        View flagTextView = getChildAt(0);
        GradientDrawable background = (GradientDrawable) flagTextView.getBackground();
        background.setColor(color);
        this.pointerPaint.setColor(color);
    }

    public void setTitle(String title) {
        TextView titleTextView = (TextView) findViewById(R.id.flag_text_title);
        if (title == null) {
            titleTextView.setText("");
            titleTextView.setVisibility(8);
        }
        titleTextView.setText(title);
    }

    public void setSymbol(String symbol) {
        TextView symbolTextView = (TextView) findViewById(R.id.flag_text_symbol);
        if (symbol == null) {
            symbolTextView.setText("");
            symbolTextView.setVisibility(8);
        }
        symbolTextView.setText(symbol);
    }

    public void setValue(String value) {
        TextView valueTextView = (TextView) findViewById(R.id.flag_text_value);
        valueTextView.setText(value);
    }

    public void setTitleColor(int color) {
        TextView titleTextView = (TextView) findViewById(R.id.flag_text_title);
        titleTextView.setTextColor(color);
    }

    public void setSymbolColor(int color) {
        TextView symbolTextView = (TextView) findViewById(R.id.flag_text_symbol);
        symbolTextView.setTextColor(color);
    }

    public void setValueColor(int color) {
        TextView valueTextView = (TextView) findViewById(R.id.flag_text_value);
        valueTextView.setTextColor(color);
    }

    public void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = (TextView) findViewById(R.id.flag_text_title);
        titleTextView.setTypeface(typeface);
    }

    public void setSymbolTypeface(Typeface typeface) {
        TextView symbolTextView = (TextView) findViewById(R.id.flag_text_symbol);
        symbolTextView.setTypeface(typeface);
    }

    public void setValueTypeface(Typeface typeface) {
        TextView valueTextView = (TextView) findViewById(R.id.flag_text_value);
        valueTextView.setTypeface(typeface);
    }

    public void setTitleTextSize(float textSize) {
        TextView titleTextView = (TextView) findViewById(R.id.flag_text_title);
        titleTextView.setTextSize(textSize);
    }

    public void setSymbolTextSize(float textSize) {
        TextView symbolTextView = (TextView) findViewById(R.id.flag_text_symbol);
        symbolTextView.setTextSize(textSize);
    }

    public void setValueTextSize(float textSize) {
        TextView valueTextView = (TextView) findViewById(R.id.flag_text_value);
        valueTextView.setTextSize(textSize);
    }
}

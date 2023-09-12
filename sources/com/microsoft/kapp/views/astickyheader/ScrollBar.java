package com.microsoft.kapp.views.astickyheader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.views.astickyheader.PinnedSectionListView;
/* loaded from: classes.dex */
public class ScrollBar {
    private static final int INDEX_BAR_MARGIN = 10;
    private static final int INDEX_BAR_PADDING_SIZE = 5;
    private static final int INDEX_BAR_SIZE = 18;
    private static final int PREVIEW_TEXT_SIZE = 50;
    private float mDensity;
    private float mIndexbarMargin;
    private RectF mIndexbarRect;
    private float mIndexbarWidth;
    private ListView mListView;
    private int mListViewHeight;
    private int mListViewWidth;
    private float mPreviewPadding;
    private float mScaledDensity;
    private boolean mVisible = false;
    private int mCurrentSection = -1;
    private boolean mIsIndexing = false;
    private PinnedSectionListView.PinnedSectionListAdapter mIndexer = null;
    private String[] mSections = null;

    public ScrollBar(Context context, ListView lv) {
        this.mListView = null;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.mListView = lv;
        setAdapter(this.mListView.getAdapter());
        this.mIndexbarWidth = 18.0f * this.mDensity;
        this.mIndexbarMargin = 10.0f * this.mDensity;
        this.mPreviewPadding = 5.0f * this.mDensity;
    }

    public void draw(Canvas canvas) {
        if (this.mVisible) {
            Paint mainBackgroundPaint = new Paint();
            mainBackgroundPaint.setColor(-16777216);
            mainBackgroundPaint.setAlpha(this.mVisible ? Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD : 0);
            mainBackgroundPaint.setAntiAlias(true);
            canvas.drawRoundRect(this.mIndexbarRect, 5.0f * this.mDensity, 5.0f * this.mDensity, mainBackgroundPaint);
            if (this.mSections != null && this.mSections.length > 0) {
                if (this.mCurrentSection >= 0) {
                    Paint previewPaint = new Paint();
                    previewPaint.setColor(-16777216);
                    previewPaint.setAlpha(Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD);
                    previewPaint.setAntiAlias(true);
                    Paint previewTextPaint = new Paint();
                    previewTextPaint.setColor(-1);
                    previewTextPaint.setAntiAlias(true);
                    previewTextPaint.setTextSize(50.0f * this.mScaledDensity);
                    float previewTextWidth = previewTextPaint.measureText(this.mSections[this.mCurrentSection]);
                    float previewSize = ((2.0f * this.mPreviewPadding) + previewTextPaint.descent()) - previewTextPaint.ascent();
                    RectF previewRect = new RectF((this.mListViewWidth - previewSize) / 2.0f, (this.mListViewHeight - previewSize) / 2.0f, ((this.mListViewWidth - previewSize) / 2.0f) + previewSize, ((this.mListViewHeight - previewSize) / 2.0f) + previewSize);
                    canvas.drawRoundRect(previewRect, 5.0f * this.mDensity, 5.0f * this.mDensity, previewPaint);
                    canvas.drawText(this.mSections[this.mCurrentSection], (previewRect.left + ((previewSize - previewTextWidth) / 2.0f)) - 1.0f, ((previewRect.top + this.mPreviewPadding) - previewTextPaint.ascent()) + 1.0f, previewTextPaint);
                }
                Paint indexPaint = new Paint();
                indexPaint.setColor(-1);
                indexPaint.setAlpha(this.mVisible ? 255 : 0);
                indexPaint.setAntiAlias(true);
                indexPaint.setTextSize(12.0f * this.mScaledDensity);
                float sectionHeight = (this.mIndexbarRect.height() - (2.0f * this.mIndexbarMargin)) / this.mSections.length;
                float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2.0f;
                for (int i = 0; i < this.mSections.length; i++) {
                    float paddingLeft = (this.mIndexbarWidth - indexPaint.measureText(this.mSections[i])) / 2.0f;
                    canvas.drawText(this.mSections[i], this.mIndexbarRect.left + paddingLeft, (((this.mIndexbarRect.top + this.mIndexbarMargin) + (i * sectionHeight)) + paddingTop) - indexPaint.ascent(), indexPaint);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                if (this.mVisible && contains(ev.getX(), ev.getY())) {
                    this.mIsIndexing = true;
                    this.mCurrentSection = getSectionByPoint(ev.getY());
                    this.mListView.setSelection(this.mIndexer.getPositionForSection(this.mCurrentSection));
                    return true;
                }
                break;
            case 1:
                if (this.mIsIndexing) {
                    this.mIsIndexing = false;
                    this.mCurrentSection = -1;
                    break;
                }
                break;
            case 2:
                if (this.mIsIndexing) {
                    if (contains(ev.getX(), ev.getY())) {
                        this.mCurrentSection = getSectionByPoint(ev.getY());
                        this.mListView.setSelection(this.mIndexer.getPositionForSection(this.mCurrentSection));
                        return true;
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h) {
        this.mListViewWidth = w;
        this.mListViewHeight = h;
        this.mIndexbarRect = new RectF((w - this.mIndexbarMargin) - this.mIndexbarWidth, this.mIndexbarMargin, w - this.mIndexbarMargin, h - this.mIndexbarMargin);
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof PinnedSectionListView.PinnedSectionListAdapter) {
            this.mIndexer = (PinnedSectionListView.PinnedSectionListAdapter) adapter;
            this.mSections = this.mIndexer.getSections();
        }
    }

    private void setVisible(boolean visible) {
        this.mVisible = visible;
        this.mListView.invalidate();
    }

    private boolean contains(float x, float y) {
        return x >= this.mIndexbarRect.left && y >= this.mIndexbarRect.top && y <= this.mIndexbarRect.top + this.mIndexbarRect.height();
    }

    private int getSectionByPoint(float y) {
        if (this.mSections == null || this.mSections.length == 0 || y < this.mIndexbarRect.top + this.mIndexbarMargin) {
            return 0;
        }
        if (y >= (this.mIndexbarRect.top + this.mIndexbarRect.height()) - this.mIndexbarMargin) {
            return this.mSections.length - 1;
        }
        return (int) (((y - this.mIndexbarRect.top) - this.mIndexbarMargin) / ((this.mIndexbarRect.height() - (2.0f * this.mIndexbarMargin)) / this.mSections.length));
    }
}

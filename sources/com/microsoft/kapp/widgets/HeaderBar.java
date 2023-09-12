package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.event.SyncStartedEvent;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.utils.ViewUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class HeaderBar extends ViewGroup implements SyncStatusListener {
    private static final int DEFAULT_BACKGROUND_COLOR = 0;
    private static final int DEFAULT_TITLE_TEXT_COLOR = -1;
    private static final int DEFAULT_TITLE_TEXT_SIZE = 20;
    public static final int LEFT_BUTTON_TYPE_BACK = 2;
    public static final int LEFT_BUTTON_TYPE_MENU = 1;
    public static final int LEFT_BUTTON_TYPE_NONE = 0;
    private ImageView mBackImageView;
    private int mBackgroundColor;
    private boolean mDisableSyncButton;
    private final View mHeaderBarView;
    private boolean mIsFirstTimeDisplayed;
    private FrameLayout mLeftButtonFrameLayout;
    private int mLeftButtonType;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    private boolean mSupportInitialProgressAnimation;
    private FrameLayout mSyncButtonFrameLayout;
    private String mTitleText;
    private int mTitleTextColor;
    private float mTitleTextSize;
    private TextView mTitleTextView;

    public HeaderBar(Context context) {
        this(context, null);
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.headerBarStyle);
    }

    public HeaderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            ((KApplication) context.getApplicationContext()).inject(this);
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar, defStyle, 0);
        try {
            this.mBackgroundColor = typedArray.getColor(0, 0);
            this.mLeftButtonType = typedArray.getInt(1, 0);
            this.mDisableSyncButton = typedArray.getBoolean(5, false);
            this.mTitleText = typedArray.getString(2);
            this.mTitleTextSize = typedArray.getDimension(3, 20.0f);
            this.mTitleTextColor = typedArray.getColor(4, -1);
            typedArray.recycle();
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mHeaderBarView = inflater.inflate(R.layout.header_bar, (ViewGroup) this, false);
            this.mHeaderBarView.setBackgroundColor(this.mBackgroundColor);
            addView(this.mHeaderBarView);
            this.mLeftButtonFrameLayout = (FrameLayout) ViewUtils.getValidView(this.mHeaderBarView, R.id.left_button_frame_layout, FrameLayout.class);
            this.mSyncButtonFrameLayout = (FrameLayout) ViewUtils.getValidView(this.mHeaderBarView, R.id.sync_button_frame_layout, FrameLayout.class);
            this.mBackImageView = (ImageView) ViewUtils.getValidView(this.mHeaderBarView, R.id.back_image_view, ImageView.class);
            this.mTitleTextView = (TextView) ViewUtils.getValidView(this.mHeaderBarView, R.id.title_text_view, TextView.class);
            this.mBackImageView.setVisibility(this.mLeftButtonType == 2 ? 0 : 8);
            this.mTitleTextView.setText(this.mTitleText);
            this.mTitleTextView.setTextColor(this.mTitleTextColor);
            updateLeftButtonType();
            this.mTitleTextView.setTextSize(0, this.mTitleTextSize);
            this.mSyncButtonFrameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.widgets.HeaderBar.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    HeaderBar.this.mMultiDeviceManager.startSync();
                }
            });
            setDisableSyncButton(this.mDisableSyncButton);
            this.mIsFirstTimeDisplayed = true;
            this.mSupportInitialProgressAnimation = true;
        } catch (Throwable th) {
            typedArray.recycle();
            throw th;
        }
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    @Override // android.view.View
    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        this.mHeaderBarView.setBackgroundColor(backgroundColor);
    }

    public int getLeftButtonType() {
        return this.mLeftButtonType;
    }

    public void setLeftButtonType(int leftButtonType) {
        Validate.isTrue(leftButtonType >= 0 && leftButtonType <= 2, "leftButtonType is invalid.");
        this.mLeftButtonType = leftButtonType;
        updateLeftButtonType();
    }

    public boolean getDisableSyncButton() {
        return this.mDisableSyncButton;
    }

    public void setDisableSyncButton(boolean disableSyncButton) {
        this.mDisableSyncButton = disableSyncButton;
        this.mSyncButtonFrameLayout.setVisibility(disableSyncButton ? 8 : 0);
    }

    public String getTitleText() {
        return this.mTitleText;
    }

    public void setTitleText(String titleText) {
        this.mTitleText = titleText;
        this.mTitleTextView.setText(this.mTitleText);
    }

    public float getTitleTextSize() {
        return this.mTitleTextSize;
    }

    public void setTitleTextSize(float titleTextSize) {
        this.mTitleTextSize = titleTextSize;
        this.mTitleTextView.setTextSize(this.mTitleTextSize);
    }

    public int getTitleTextColor() {
        return this.mTitleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.mTitleTextColor = titleTextColor;
        this.mTitleTextView.setTextColor(this.mTitleTextColor);
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncStarted(SyncStartedEvent e) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncCompleted(SyncCompletedEvent e) {
        Validate.notNull(e, "e");
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncPreComplete(SyncCompletedEvent e) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncProgress(int progressPercentage) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncTerminated() {
    }

    public void onResume() {
        this.mIsFirstTimeDisplayed = false;
    }

    public void onPause() {
    }

    public void setLeftButtonOnClickListener(View.OnClickListener listener) {
        this.mLeftButtonFrameLayout.setOnClickListener(listener);
    }

    public void setSupportInitialProgressAnimation(boolean supportAnimation) {
        this.mSupportInitialProgressAnimation = supportAnimation;
    }

    public void removeCustomRightButton(View view) {
        Validate.notNull(view, "view");
        ((ViewGroup) view.getParent()).removeView(view);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(this.mHeaderBarView, widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mHeaderBarView.layout(0, 0, r, b - t);
    }

    private void updateLeftButtonType() {
        this.mBackImageView.setVisibility(this.mLeftButtonType == 2 ? 0 : 8);
    }

    public View getView() {
        return this.mHeaderBarView;
    }
}

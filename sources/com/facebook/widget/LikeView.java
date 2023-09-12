package com.facebook.widget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.android.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.LikeActionController;
import com.facebook.internal.LikeBoxCountView;
import com.facebook.internal.LikeButton;
import com.facebook.internal.Utility;
/* loaded from: classes.dex */
public class LikeView extends FrameLayout {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition = null;
    private static final int NO_FOREGROUND_COLOR = -1;
    private AuxiliaryViewPosition auxiliaryViewPosition;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout containerView;
    private LikeActionControllerCreationCallback creationCallback;
    private int edgePadding;
    private int foregroundColor;
    private HorizontalAlignment horizontalAlignment;
    private int internalPadding;
    private LikeActionController likeActionController;
    private LikeBoxCountView likeBoxCountView;
    private LikeButton likeButton;
    private Style likeViewStyle;
    private String objectId;
    private OnErrorListener onErrorListener;
    private TextView socialSentenceView;

    /* loaded from: classes.dex */
    public interface OnErrorListener {
        void onError(Bundle bundle);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition() {
        int[] iArr = $SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition;
        if (iArr == null) {
            iArr = new int[AuxiliaryViewPosition.valuesCustom().length];
            try {
                iArr[AuxiliaryViewPosition.BOTTOM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AuxiliaryViewPosition.INLINE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AuxiliaryViewPosition.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition = iArr;
        }
        return iArr;
    }

    /* loaded from: classes.dex */
    public enum Style {
        STANDARD("standard", 0),
        BUTTON("button", 1),
        BOX_COUNT("box_count", 2);
        
        private int intValue;
        private String stringValue;
        static Style DEFAULT = STANDARD;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Style[] valuesCustom() {
            Style[] valuesCustom = values();
            int length = valuesCustom.length;
            Style[] styleArr = new Style[length];
            System.arraycopy(valuesCustom, 0, styleArr, 0, length);
            return styleArr;
        }

        static Style fromInt(int enumValue) {
            Style[] valuesCustom;
            for (Style style : valuesCustom()) {
                if (style.getValue() == enumValue) {
                    return style;
                }
            }
            return null;
        }

        Style(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    /* loaded from: classes.dex */
    public enum HorizontalAlignment {
        CENTER("center", 0),
        LEFT("left", 1),
        RIGHT("right", 2);
        
        static HorizontalAlignment DEFAULT = CENTER;
        private int intValue;
        private String stringValue;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static HorizontalAlignment[] valuesCustom() {
            HorizontalAlignment[] valuesCustom = values();
            int length = valuesCustom.length;
            HorizontalAlignment[] horizontalAlignmentArr = new HorizontalAlignment[length];
            System.arraycopy(valuesCustom, 0, horizontalAlignmentArr, 0, length);
            return horizontalAlignmentArr;
        }

        static HorizontalAlignment fromInt(int enumValue) {
            HorizontalAlignment[] valuesCustom;
            for (HorizontalAlignment horizontalAlignment : valuesCustom()) {
                if (horizontalAlignment.getValue() == enumValue) {
                    return horizontalAlignment;
                }
            }
            return null;
        }

        HorizontalAlignment(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    /* loaded from: classes.dex */
    public enum AuxiliaryViewPosition {
        BOTTOM("bottom", 0),
        INLINE("inline", 1),
        TOP("top", 2);
        
        static AuxiliaryViewPosition DEFAULT = BOTTOM;
        private int intValue;
        private String stringValue;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static AuxiliaryViewPosition[] valuesCustom() {
            AuxiliaryViewPosition[] valuesCustom = values();
            int length = valuesCustom.length;
            AuxiliaryViewPosition[] auxiliaryViewPositionArr = new AuxiliaryViewPosition[length];
            System.arraycopy(valuesCustom, 0, auxiliaryViewPositionArr, 0, length);
            return auxiliaryViewPositionArr;
        }

        static AuxiliaryViewPosition fromInt(int enumValue) {
            AuxiliaryViewPosition[] valuesCustom;
            for (AuxiliaryViewPosition auxViewPosition : valuesCustom()) {
                if (auxViewPosition.getValue() == enumValue) {
                    return auxViewPosition;
                }
            }
            return null;
        }

        AuxiliaryViewPosition(String stringValue, int value) {
            this.stringValue = stringValue;
            this.intValue = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.stringValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getValue() {
            return this.intValue;
        }
    }

    public static boolean handleOnActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        return LikeActionController.handleOnActivityResult(context, requestCode, resultCode, data);
    }

    public LikeView(Context context) {
        super(context);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        initialize(context);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        parseAttributes(attrs);
        initialize(context);
    }

    public void setObjectId(String objectId) {
        String objectId2 = Utility.coerceValueIfNullOrEmpty(objectId, null);
        if (!Utility.areObjectsEqual(objectId2, this.objectId)) {
            setObjectIdForced(objectId2);
            updateLikeStateAndLayout();
        }
    }

    public void setLikeViewStyle(Style likeViewStyle) {
        if (likeViewStyle == null) {
            likeViewStyle = Style.DEFAULT;
        }
        if (this.likeViewStyle != likeViewStyle) {
            this.likeViewStyle = likeViewStyle;
            updateLayout();
        }
    }

    public void setAuxiliaryViewPosition(AuxiliaryViewPosition auxiliaryViewPosition) {
        if (auxiliaryViewPosition == null) {
            auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        }
        if (this.auxiliaryViewPosition != auxiliaryViewPosition) {
            this.auxiliaryViewPosition = auxiliaryViewPosition;
            updateLayout();
        }
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        if (horizontalAlignment == null) {
            horizontalAlignment = HorizontalAlignment.DEFAULT;
        }
        if (this.horizontalAlignment != horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
            updateLayout();
        }
    }

    public void setForegroundColor(int foregroundColor) {
        if (this.foregroundColor != foregroundColor) {
            this.socialSentenceView.setTextColor(foregroundColor);
        }
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        setObjectId(null);
        super.onDetachedFromWindow();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a;
        if (attrs != null && getContext() != null && (a = getContext().obtainStyledAttributes(attrs, R.styleable.com_facebook_like_view)) != null) {
            this.objectId = Utility.coerceValueIfNullOrEmpty(a.getString(1), null);
            this.likeViewStyle = Style.fromInt(a.getInt(2, Style.DEFAULT.getValue()));
            if (this.likeViewStyle == null) {
                throw new IllegalArgumentException("Unsupported value for LikeView 'style'");
            }
            this.auxiliaryViewPosition = AuxiliaryViewPosition.fromInt(a.getInt(3, AuxiliaryViewPosition.DEFAULT.getValue()));
            if (this.auxiliaryViewPosition == null) {
                throw new IllegalArgumentException("Unsupported value for LikeView 'auxiliary_view_position'");
            }
            this.horizontalAlignment = HorizontalAlignment.fromInt(a.getInt(4, HorizontalAlignment.DEFAULT.getValue()));
            if (this.horizontalAlignment == null) {
                throw new IllegalArgumentException("Unsupported value for LikeView 'horizontal_alignment'");
            }
            this.foregroundColor = a.getColor(0, -1);
            a.recycle();
        }
    }

    private void initialize(Context context) {
        this.edgePadding = getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_edge_padding);
        this.internalPadding = getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_internal_padding);
        if (this.foregroundColor == -1) {
            this.foregroundColor = getResources().getColor(R.color.com_facebook_likeview_text_color);
        }
        setBackgroundColor(0);
        this.containerView = new LinearLayout(context);
        FrameLayout.LayoutParams containerViewLayoutParams = new FrameLayout.LayoutParams(-2, -2);
        this.containerView.setLayoutParams(containerViewLayoutParams);
        initializeLikeButton(context);
        initializeSocialSentenceView(context);
        initializeLikeCountView(context);
        this.containerView.addView(this.likeButton);
        this.containerView.addView(this.socialSentenceView);
        this.containerView.addView(this.likeBoxCountView);
        addView(this.containerView);
        setObjectIdForced(this.objectId);
        updateLikeStateAndLayout();
    }

    private void initializeLikeButton(Context context) {
        this.likeButton = new LikeButton(context, this.likeActionController != null ? this.likeActionController.isObjectLiked() : false);
        this.likeButton.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.widget.LikeView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LikeView.this.toggleLike();
            }
        });
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(-2, -2);
        this.likeButton.setLayoutParams(buttonLayout);
    }

    private void initializeSocialSentenceView(Context context) {
        this.socialSentenceView = new TextView(context);
        this.socialSentenceView.setTextSize(0, getResources().getDimension(R.dimen.com_facebook_likeview_text_size));
        this.socialSentenceView.setMaxLines(2);
        this.socialSentenceView.setTextColor(this.foregroundColor);
        this.socialSentenceView.setGravity(17);
        LinearLayout.LayoutParams socialSentenceViewLayout = new LinearLayout.LayoutParams(-2, -1);
        this.socialSentenceView.setLayoutParams(socialSentenceViewLayout);
    }

    private void initializeLikeCountView(Context context) {
        this.likeBoxCountView = new LikeBoxCountView(context);
        LinearLayout.LayoutParams likeCountViewLayout = new LinearLayout.LayoutParams(-1, -1);
        this.likeBoxCountView.setLayoutParams(likeCountViewLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleLike() {
        if (this.likeActionController != null) {
            Activity activity = (Activity) getContext();
            this.likeActionController.toggleLike(activity, getAnalyticsParameters());
        }
    }

    private Bundle getAnalyticsParameters() {
        Bundle params = new Bundle();
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_STYLE, this.likeViewStyle.toString());
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_AUXILIARY_POSITION, this.auxiliaryViewPosition.toString());
        params.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_HORIZONTAL_ALIGNMENT, this.horizontalAlignment.toString());
        params.putString("object_id", Utility.coerceValueIfNullOrEmpty(this.objectId, ""));
        return params;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setObjectIdForced(String newObjectId) {
        tearDownObjectAssociations();
        this.objectId = newObjectId;
        if (!Utility.isNullOrEmpty(newObjectId)) {
            this.creationCallback = new LikeActionControllerCreationCallback(this, null);
            LikeActionController.getControllerForObjectId(getContext(), newObjectId, this.creationCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void associateWithLikeActionController(LikeActionController likeActionController) {
        this.likeActionController = likeActionController;
        this.broadcastReceiver = new LikeControllerBroadcastReceiver(this, null);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_UPDATED);
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR);
        filter.addAction(LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_RESET);
        localBroadcastManager.registerReceiver(this.broadcastReceiver, filter);
    }

    private void tearDownObjectAssociations() {
        if (this.broadcastReceiver != null) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
            localBroadcastManager.unregisterReceiver(this.broadcastReceiver);
            this.broadcastReceiver = null;
        }
        if (this.creationCallback != null) {
            this.creationCallback.cancel();
            this.creationCallback = null;
        }
        this.likeActionController = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLikeStateAndLayout() {
        if (this.likeActionController == null) {
            this.likeButton.setLikeState(false);
            this.socialSentenceView.setText((CharSequence) null);
            this.likeBoxCountView.setText(null);
        } else {
            this.likeButton.setLikeState(this.likeActionController.isObjectLiked());
            this.socialSentenceView.setText(this.likeActionController.getSocialSentence());
            this.likeBoxCountView.setText(this.likeActionController.getLikeCountString());
        }
        updateLayout();
    }

    private void updateLayout() {
        int viewGravity;
        View auxView;
        FrameLayout.LayoutParams containerViewLayoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
        LinearLayout.LayoutParams buttonLayoutParams = (LinearLayout.LayoutParams) this.likeButton.getLayoutParams();
        if (this.horizontalAlignment == HorizontalAlignment.LEFT) {
            viewGravity = 3;
        } else {
            viewGravity = this.horizontalAlignment == HorizontalAlignment.CENTER ? 1 : 5;
        }
        containerViewLayoutParams.gravity = viewGravity | 48;
        buttonLayoutParams.gravity = viewGravity;
        this.socialSentenceView.setVisibility(8);
        this.likeBoxCountView.setVisibility(8);
        if (this.likeViewStyle == Style.STANDARD && this.likeActionController != null && !Utility.isNullOrEmpty(this.likeActionController.getSocialSentence())) {
            auxView = this.socialSentenceView;
        } else if (this.likeViewStyle == Style.BOX_COUNT && this.likeActionController != null && !Utility.isNullOrEmpty(this.likeActionController.getLikeCountString())) {
            updateBoxCountCaretPosition();
            auxView = this.likeBoxCountView;
        } else {
            return;
        }
        auxView.setVisibility(0);
        LinearLayout.LayoutParams auxViewLayoutParams = (LinearLayout.LayoutParams) auxView.getLayoutParams();
        auxViewLayoutParams.gravity = viewGravity;
        this.containerView.setOrientation(this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE ? 0 : 1);
        if (this.auxiliaryViewPosition == AuxiliaryViewPosition.TOP || (this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE && this.horizontalAlignment == HorizontalAlignment.RIGHT)) {
            this.containerView.removeView(this.likeButton);
            this.containerView.addView(this.likeButton);
        } else {
            this.containerView.removeView(auxView);
            this.containerView.addView(auxView);
        }
        switch ($SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition()[this.auxiliaryViewPosition.ordinal()]) {
            case 1:
                auxView.setPadding(this.edgePadding, this.internalPadding, this.edgePadding, this.edgePadding);
                return;
            case 2:
                if (this.horizontalAlignment == HorizontalAlignment.RIGHT) {
                    auxView.setPadding(this.edgePadding, this.edgePadding, this.internalPadding, this.edgePadding);
                    return;
                } else {
                    auxView.setPadding(this.internalPadding, this.edgePadding, this.edgePadding, this.edgePadding);
                    return;
                }
            case 3:
                auxView.setPadding(this.edgePadding, this.edgePadding, this.edgePadding, this.internalPadding);
                return;
            default:
                return;
        }
    }

    private void updateBoxCountCaretPosition() {
        LikeBoxCountView.LikeBoxCountViewCaretPosition likeBoxCountViewCaretPosition;
        switch ($SWITCH_TABLE$com$facebook$widget$LikeView$AuxiliaryViewPosition()[this.auxiliaryViewPosition.ordinal()]) {
            case 1:
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.TOP);
                return;
            case 2:
                LikeBoxCountView likeBoxCountView = this.likeBoxCountView;
                if (this.horizontalAlignment == HorizontalAlignment.RIGHT) {
                    likeBoxCountViewCaretPosition = LikeBoxCountView.LikeBoxCountViewCaretPosition.RIGHT;
                } else {
                    likeBoxCountViewCaretPosition = LikeBoxCountView.LikeBoxCountViewCaretPosition.LEFT;
                }
                likeBoxCountView.setCaretPosition(likeBoxCountViewCaretPosition);
                return;
            case 3:
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.BOTTOM);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LikeControllerBroadcastReceiver extends BroadcastReceiver {
        private LikeControllerBroadcastReceiver() {
        }

        /* synthetic */ LikeControllerBroadcastReceiver(LikeView likeView, LikeControllerBroadcastReceiver likeControllerBroadcastReceiver) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            Bundle extras = intent.getExtras();
            boolean shouldRespond = true;
            if (extras != null) {
                String broadcastObjectId = extras.getString(LikeActionController.ACTION_OBJECT_ID_KEY);
                shouldRespond = Utility.isNullOrEmpty(broadcastObjectId) || Utility.areObjectsEqual(LikeView.this.objectId, broadcastObjectId);
            }
            if (shouldRespond) {
                if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_UPDATED.equals(intentAction)) {
                    LikeView.this.updateLikeStateAndLayout();
                } else if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR.equals(intentAction)) {
                    if (LikeView.this.onErrorListener != null) {
                        LikeView.this.onErrorListener.onError(extras);
                    }
                } else if (LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_RESET.equals(intentAction)) {
                    LikeView.this.setObjectIdForced(LikeView.this.objectId);
                    LikeView.this.updateLikeStateAndLayout();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LikeActionControllerCreationCallback implements LikeActionController.CreationCallback {
        private boolean isCancelled;

        private LikeActionControllerCreationCallback() {
        }

        /* synthetic */ LikeActionControllerCreationCallback(LikeView likeView, LikeActionControllerCreationCallback likeActionControllerCreationCallback) {
            this();
        }

        public void cancel() {
            this.isCancelled = true;
        }

        @Override // com.facebook.internal.LikeActionController.CreationCallback
        public void onComplete(LikeActionController likeActionController) {
            if (!this.isCancelled) {
                LikeView.this.associateWithLikeActionController(likeActionController);
                LikeView.this.updateLikeStateAndLayout();
                LikeView.this.creationCallback = null;
            }
        }
    }
}

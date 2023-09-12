package com.microsoft.kapp.views;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.utils.StrappUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
@SuppressLint({"ClickableViewAccessibility"})
/* loaded from: classes.dex */
public class HorizontalSortableStrappListView extends HorizontalScrollView implements View.OnLongClickListener, View.OnTouchListener {
    private static final long SPACE_ANIMATION_DURATION_MILLIS = 200;
    private static final String TAG = HorizontalSortableStrappListView.class.getSimpleName();
    private static final int mScrollCoefficient = 8;
    private static final int mScrollRepeatDelay = 5;
    private static final double mScrollStartPercent = 0.33d;
    private int mColor;
    private LinearLayout mContainerLayout;
    private int mCurrentTouchIndex;
    private int mCurrentTouchLocationCenter;
    private int mCurrentTouchScreenXLocation;
    private int mCurrentTouchXLocation;
    private ArrayList<StrappState> mElements;
    private int mGlyphSize;
    private boolean mIsItemMoving;
    private boolean mIsScrollHandlerActive;
    private int mMargins;
    private RelativeLayout mOverviewLayout;
    private StrappState mRemovedElement;
    private int mScrollAmount;
    private Handler mScrollHandler;
    private Runnable mScrollRunnable;
    private int mShadowColor;
    private CustomGlyphViewWithBackground mShadowView;
    private float mShadowViewAlpha;
    private WeakReference<OnDragSortListener> mSortListener;
    private FrameLayout.LayoutParams mStrappImageLayoutParams;
    private int mStrappSize;
    private ArrayList<View> mViews;

    /* loaded from: classes.dex */
    public interface OnDragSortListener {
        void onDragSort();
    }

    public HorizontalSortableStrappListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mElements = new ArrayList<>();
        this.mViews = new ArrayList<>();
        this.mIsItemMoving = false;
        this.mScrollAmount = 0;
        this.mStrappSize = 0;
        this.mCurrentTouchXLocation = 0;
        this.mCurrentTouchScreenXLocation = 0;
        initializeAttributes(attrs, context);
        this.mScrollHandler = new Handler();
        this.mOverviewLayout = new RelativeLayout(context);
        this.mOverviewLayout.setClipChildren(false);
        this.mOverviewLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mOverviewLayout);
        this.mContainerLayout = new LinearLayout(context);
        this.mContainerLayout.setClipChildren(false);
        this.mContainerLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.mOverviewLayout.addView(this.mContainerLayout);
        this.mContainerLayout.setOnTouchListener(this);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setOnTouchListener(this);
        setOverScrollMode(2);
        createShadowView("");
        this.mOverviewLayout.addView(this.mShadowView);
        this.mShadowView.setVisibility(8);
        this.mScrollRunnable = new Runnable() { // from class: com.microsoft.kapp.views.HorizontalSortableStrappListView.1
            @Override // java.lang.Runnable
            public void run() {
                if (!HorizontalSortableStrappListView.this.mIsItemMoving || !HorizontalSortableStrappListView.this.mIsScrollHandlerActive) {
                    HorizontalSortableStrappListView.this.mIsScrollHandlerActive = false;
                    return;
                }
                HorizontalSortableStrappListView.this.scrollBy(HorizontalSortableStrappListView.this.mScrollAmount, 0);
                HorizontalSortableStrappListView.this.mScrollHandler.postDelayed(this, 5L);
                int eventXLocation = HorizontalSortableStrappListView.this.mCurrentTouchXLocation;
                HorizontalSortableStrappListView.this.checkForExpandingViews(eventXLocation);
                HorizontalSortableStrappListView.this.moveShadowToLocation(HorizontalSortableStrappListView.this.mCurrentTouchScreenXLocation + HorizontalSortableStrappListView.this.getScrollX());
            }
        };
    }

    private void initializeAttributes(AttributeSet attrs, Context context) {
        TypedArray typedAttributes = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSortableStrappListView);
        try {
            this.mStrappSize = typedAttributes.getDimensionPixelSize(1, context.getResources().getInteger(R.integer.default_strapp_size));
            this.mMargins = typedAttributes.getDimensionPixelSize(2, context.getResources().getInteger(R.integer.default_element_margin));
            this.mGlyphSize = typedAttributes.getInteger(4, context.getResources().getInteger(R.integer.default_glyph_size));
            this.mShadowViewAlpha = typedAttributes.getFloat(0, context.getResources().getInteger(R.integer.default_shadow_view_alpha));
            this.mStrappImageLayoutParams = new FrameLayout.LayoutParams(this.mStrappSize, this.mStrappSize);
            this.mStrappImageLayoutParams.gravity = 17;
        } finally {
            typedAttributes.recycle();
        }
    }

    public void addElement(StrappState element) {
        this.mElements.add(element);
        initializeView();
    }

    public void addElement(int index, StrappState element) {
        this.mElements.add(index, element);
        initializeView();
    }

    public void addElements(List<StrappState> elements) {
        for (int i = 0; i < elements.size(); i++) {
            this.mElements.add(elements.get(i));
        }
        initializeView();
    }

    public void addElements(LinkedHashMap<UUID, StrappState> elements) {
        for (StrappState strappState : elements.values()) {
            this.mElements.add(strappState);
        }
        initializeView();
    }

    public void removeElement(int location) throws Exception {
        if (location < 0 || location >= this.mElements.size()) {
            throw new Exception("Location out of bounds!");
        }
        this.mElements.remove(location);
        initializeView();
    }

    public void clear() {
        this.mElements.clear();
        initializeView();
    }

    public void removeElement(UUID id) {
        Iterator i$ = this.mElements.iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            StrappState element = i$.next();
            if (element.getDefinition().getStrappId().compareTo(id) == 0) {
                this.mElements.remove(element);
                break;
            }
        }
        initializeView();
    }

    public void removeElement(StrappState element) {
        if (this.mElements.contains(element)) {
            try {
                removeElement(this.mElements.indexOf(element));
            } catch (Exception e) {
                KLog.e(TAG, "Element could not be removed!", e);
            }
            initializeView();
        }
    }

    public ArrayList<StrappState> getCurrentElements() {
        return this.mElements;
    }

    public LinkedHashMap<UUID, StrappState> getElementsAsLinkedHashMap() {
        LinkedHashMap<UUID, StrappState> map = new LinkedHashMap<>();
        Iterator i$ = this.mElements.iterator();
        while (i$.hasNext()) {
            StrappState element = i$.next();
            map.put(element.getDefinition().getStrappId(), element);
        }
        return map;
    }

    private void initializeView() {
        this.mContainerLayout.removeAllViews();
        this.mViews.clear();
        if (this.mElements.size() > 0) {
            int viewId = 0;
            Iterator i$ = this.mElements.iterator();
            while (i$.hasNext()) {
                StrappState strapp = i$.next();
                addSpaceToList(viewId);
                String strappGlyph = StrappUtils.lookupGlyphForUuid(strapp.getDefinition().getStrappId(), getContext());
                String strappBackGlyph = StrappUtils.lookupBackGlyphForUuid(strapp.getDefinition().getStrappId(), getContext());
                int strappBackColor = StrappUtils.lookupGlyhpBackgroundColorForUUID(strapp.getDefinition().getStrappId(), getContext());
                if (strappGlyph != null) {
                    addStrappGlyphToList(strappGlyph, strappBackGlyph, strappBackColor, viewId);
                } else if (strapp.getDefinition().isThirdPartyStrapp()) {
                    addStrappImageToList(strapp.getDefinition().getIcon(), viewId);
                }
                viewId++;
            }
            addSpaceToList(viewId);
        }
    }

    private void addStrappImageToList(Bitmap icon, int id) {
        CustomGlyphViewWithBackground strappImage = new CustomGlyphViewWithBackground(getContext(), null);
        strappImage.setId(id);
        strappImage.setLayoutParams(this.mStrappImageLayoutParams);
        strappImage.setOnLongClickListener(this);
        if (icon != null) {
            strappImage.setBackgroundImage(new BitmapDrawable(getResources(), icon));
        }
        strappImage.setBackgroundColor(this.mColor);
        addViewToList(strappImage);
    }

    private void addStrappGlyphToList(String glyph, String backGlyph, int backgroundColor, int id) {
        CustomGlyphViewWithBackground strappImage = new CustomGlyphViewWithBackground(getContext(), null);
        strappImage.setId(id);
        strappImage.setLayoutParams(this.mStrappImageLayoutParams);
        strappImage.setOnLongClickListener(this);
        strappImage.setBackgroundColor(this.mColor);
        strappImage.setTextSize(this.mGlyphSize);
        strappImage.setTextColor(-1);
        strappImage.setGravity(17);
        strappImage.setText(glyph);
        strappImage.setBackgroundText(backGlyph);
        if (backgroundColor != 0) {
            strappImage.setBackColor(backgroundColor);
        }
        addViewToList(strappImage);
    }

    private void addSpaceToList(int id) {
        Space space = new Space(getContext());
        space.setId(id);
        FrameLayout.LayoutParams strappSpaceLayoutParams = new FrameLayout.LayoutParams(this.mMargins, -2);
        space.setLayoutParams(strappSpaceLayoutParams);
        addViewToList(space);
    }

    private void addViewToList(View view) {
        this.mViews.add(view);
        this.mContainerLayout.addView(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveShadowToLocation(float xLocation) {
        this.mShadowView.setX(xLocation - (this.mStrappSize / 2));
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        if (this.mIsItemMoving) {
            switch (event.getAction()) {
                case 1:
                    this.mIsItemMoving = false;
                    this.mShadowView.setVisibility(8);
                    addElement(this.mCurrentTouchIndex, this.mRemovedElement);
                    initializeView();
                    this.mRemovedElement = null;
                    if (this.mSortListener != null && this.mSortListener.get() != null) {
                        this.mSortListener.get().onDragSort();
                    }
                    return true;
                case 2:
                    this.mCurrentTouchXLocation = Math.round(event.getX() + v.getScrollX());
                    this.mCurrentTouchScreenXLocation = Math.round(event.getX());
                    int scrollArea = (int) (getWidth() * mScrollStartPercent);
                    if (event.getX() < scrollArea) {
                        scrollX(Math.round((-8.0f) * ((scrollArea - event.getX()) / scrollArea)));
                    } else if (event.getX() > getWidth() - scrollArea) {
                        scrollX(Math.round(8.0f * (1.0f - ((getWidth() - event.getX()) / scrollArea))));
                    } else {
                        this.mIsScrollHandlerActive = false;
                    }
                    checkForExpandingViews(this.mCurrentTouchXLocation);
                    moveShadowToLocation(this.mCurrentTouchXLocation);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public void scrollX(int scrollValue) {
        if (canScrollHorizontally(scrollValue)) {
            this.mScrollAmount = scrollValue;
            if (!this.mIsScrollHandlerActive) {
                this.mScrollHandler.postDelayed(this.mScrollRunnable, 5L);
                this.mIsScrollHandlerActive = true;
            }
        }
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View v) {
        if (this.mElements.size() > 1 && ((v instanceof PicassoImageView) || (v instanceof CustomGlyphViewWithBackground))) {
            this.mIsItemMoving = true;
            int viewLocation = v.getId();
            if (this.mRemovedElement != null) {
                addElement(this.mCurrentTouchIndex, this.mRemovedElement);
            }
            this.mRemovedElement = this.mElements.get(viewLocation);
            this.mCurrentTouchIndex = viewLocation;
            this.mCurrentTouchLocationCenter = getCenterX(v);
            moveShadowToLocation(v.getLeft() + (this.mStrappSize / 2));
            String strappGlyph = StrappUtils.lookupGlyphForUuid(this.mElements.get(viewLocation).getDefinition().getStrappId(), getContext());
            String strappBackGlyph = StrappUtils.lookupBackGlyphForUuid(this.mElements.get(viewLocation).getDefinition().getStrappId(), getContext());
            int strappBackColor = StrappUtils.lookupGlyhpBackgroundColorForUUID(this.mElements.get(viewLocation).getDefinition().getStrappId(), getContext());
            if (strappGlyph != null) {
                createShadowView(strappGlyph, strappBackGlyph, strappBackColor);
            } else {
                createShadowView(this.mElements.get(viewLocation).getDefinition().getIcon());
            }
            this.mShadowView.setVisibility(0);
            try {
                removeElement(viewLocation);
            } catch (Exception e) {
                KLog.e(TAG, "Element could not be removed!", e);
            }
            initializeView();
            setSpaceWidth(this.mCurrentTouchIndex, this.mStrappSize + (this.mMargins * 2), false);
        }
        return true;
    }

    private static int getCenterX(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkForExpandingViews(int currTouchXLoc) {
        int yDelta = Math.abs(this.mCurrentTouchLocationCenter - currTouchXLoc);
        if (yDelta > this.mStrappSize) {
            int oldDragLocation = this.mCurrentTouchIndex;
            if (currTouchXLoc > this.mCurrentTouchLocationCenter) {
                if (this.mCurrentTouchIndex < this.mElements.size()) {
                    this.mCurrentTouchIndex++;
                    this.mCurrentTouchLocationCenter += this.mStrappSize;
                    setSpaceWidth(this.mCurrentTouchIndex, this.mStrappSize + (this.mMargins * 2));
                    setSpaceWidth(oldDragLocation, this.mMargins);
                }
            } else if (this.mCurrentTouchIndex > 0) {
                this.mCurrentTouchIndex--;
                this.mCurrentTouchLocationCenter -= this.mStrappSize;
                setSpaceWidth(this.mCurrentTouchIndex, this.mStrappSize + (this.mMargins * 2));
                setSpaceWidth(oldDragLocation, this.mMargins);
            }
        }
    }

    private void setSpaceWidth(int spaceLoc, int width) {
        setSpaceWidth(spaceLoc, width, true);
    }

    private void setSpaceWidth(int spaceLoc, int width, boolean useAnimation) {
        final Space space = (Space) this.mViews.get(spaceLoc * 2);
        if (useAnimation) {
            ValueAnimator anim = ValueAnimator.ofInt(space.getMeasuredWidth(), width);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.microsoft.kapp.views.HorizontalSortableStrappListView.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    ViewGroup.LayoutParams layoutParams = space.getLayoutParams();
                    layoutParams.width = val;
                    space.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(200L);
            anim.start();
            return;
        }
        ViewGroup.LayoutParams lp = space.getLayoutParams();
        lp.width = width;
        space.setLayoutParams(lp);
    }

    public void setOnDragSortListener(OnDragSortListener listener) {
        this.mSortListener = new WeakReference<>(listener);
    }

    public void setStrappBackgroundColor(int tileColor) {
        this.mColor = tileColor;
        int alpha = (int) (((float) ((tileColor & 4278190080L) >> 24)) * this.mShadowViewAlpha);
        this.mShadowColor = (alpha << 24) | (16777215 & tileColor);
        this.mShadowView.setBackgroundColor(this.mShadowColor);
    }

    public CustomGlyphViewWithBackground initializeShadowView() {
        CustomGlyphViewWithBackground view = new CustomGlyphViewWithBackground(getContext(), null);
        FrameLayout.LayoutParams strappImageLayoutParams = new FrameLayout.LayoutParams(this.mStrappSize, this.mStrappSize);
        view.setTextSize(this.mGlyphSize);
        view.setLayoutParams(strappImageLayoutParams);
        view.setBackgroundColor(this.mShadowColor);
        return view;
    }

    public void createShadowView(Bitmap bitmap) {
        if (this.mShadowView == null) {
            this.mShadowView = initializeShadowView();
        }
        this.mShadowView.setText("");
        this.mShadowView.setBackgroundText("");
        this.mShadowView.setBackgroundImage(new BitmapDrawable(getResources(), bitmap));
        this.mShadowView.setBackgroundVisibility(0);
    }

    public void createShadowView(String glyphData) {
        createShadowView(glyphData, "", 0);
    }

    public void createShadowView(String glyphData, String strappBackGlyph, int strappBackColor) {
        if (this.mShadowView == null) {
            this.mShadowView = initializeShadowView();
        }
        this.mShadowView.setText(glyphData);
        this.mShadowView.setTextColor(-1);
        this.mShadowView.setGravity(17);
        this.mShadowView.setBackgroundText(strappBackGlyph);
        if (strappBackColor != 0) {
            this.mShadowView.setBackColor(strappBackColor);
        }
        this.mShadowView.setBackgroundVisibility(4);
    }
}

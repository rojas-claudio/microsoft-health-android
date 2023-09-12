package com.microsoft.kapp.navigations;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeActivityAnimationListener;
import com.microsoft.kapp.activities.HomeTileStateChangedListener;
import com.microsoft.kapp.activities.HomeTileStateChangedNotifier;
import com.microsoft.kapp.animations.ExponentInterpolator;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.MainContentFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.views.BlockableScrollView;
import com.microsoft.kapp.widgets.BaseTile;
import com.microsoft.kapp.widgets.HomeTile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class HomeTilesNavigationManager {
    private static final String TAG = HomeTilesNavigationManager.class.getSimpleName();
    private Animation mContentCloseAnimation;
    private Animation mContentOpenAnimation;
    private final Context mContext;
    private String mCurrentEventID;
    private WeakReference<Fragment> mCurrentFragment;
    private List<Pair<Integer, Class<? extends BaseFragment>>> mCurrentFragmentsData;
    private int mCurrentHeaderbackgroundColor;
    private BaseTile mCurrentOpenTile;
    private List<Pair<Integer, Class<? extends BaseFragment>>> mCurrentfragmentsData;
    private ViewGroup mFragmentContainer;
    private FragmentManager mFragmentManager;
    private int mHeaderTileVisibleHeight;
    private final HomeActivityAnimationListener mHomeActivityAnimationListener;
    private boolean mIsLayoutListenerRegistered;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    private int mScreenHeight;
    private BlockableScrollView mScrollView;
    private RelativeLayout mTileContainer;
    private final int mTileHeight;
    private LinearLayout mTilesContainer;
    private OpenState mOpenState = OpenState.CLOSED;
    private ArrayList<BaseTile> mHomeTiles = new ArrayList<>();
    private ArrayList<Float> mHomeTilesYPositions = new ArrayList<>();
    private int mTopMenuHeight = getHomeActivity().getTopMenuHeight();
    private int mTilesAnimationOpeningOutDuration = getInteger(R.integer.home_tiles_animation_duration);
    private int mTilesAnimationClosingInDuration = getInteger(R.integer.home_tiles_animation_duration);
    private int mTopMenuTranslationUpDuration = getInteger(R.integer.home_tiles_animation_duration);
    private int mTopMenuTranslationUpStartDelay = getInteger(R.integer.top_menu_translation_up_start_delay_milliseconds);
    private int mTopMenuTranslationDownDuration = getInteger(R.integer.top_menu_translation_down_duration_milliseconds);
    private int mTopMenuTranslationDownStartDelay = getInteger(R.integer.top_menu_translation_down_start_delay_milliseconds);
    private int mMainContentTranslationUpStartDelay = getInteger(R.integer.main_content_translation_up_start_delay_milliseconds);
    private int mMainContentTranslationDownDuration = getInteger(R.integer.main_content_translation_down_duration_milliseconds);
    private int mMainContentTranslationDownStartDelay = getInteger(R.integer.main_content_translation_down_start_delay_milliseconds);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum OpenState {
        CLOSED,
        OPENING,
        OPEN,
        CLOSING
    }

    public HomeTilesNavigationManager(Context context, HomeActivityAnimationListener homeActivityAnimationListener, LinearLayout tilesContainer, ViewGroup fragmentContainer, RelativeLayout tileContainer, BlockableScrollView scrollView) {
        this.mTilesContainer = tilesContainer;
        this.mFragmentContainer = fragmentContainer;
        this.mScrollView = scrollView;
        this.mTileContainer = tileContainer;
        this.mContext = context;
        this.mHomeActivityAnimationListener = homeActivityAnimationListener;
        this.mTileHeight = context.getResources().getDimensionPixelSize(R.dimen.home_tile_height);
        this.mHeaderTileVisibleHeight = context.getResources().getDimensionPixelSize(R.dimen.header_tile_visible_height);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.mScreenHeight = size.y;
        this.mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.navigations.HomeTilesNavigationManager.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                HomeTilesNavigationManager.this.collectInitialTilePositions();
                HomeTilesNavigationManager.this.mTilesContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                HomeTilesNavigationManager.this.mIsLayoutListenerRegistered = false;
            }
        };
        this.mContentCloseAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_out_up);
        this.mContentCloseAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.microsoft.kapp.navigations.HomeTilesNavigationManager.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation anim) {
                HomeTilesNavigationManager.this.mOpenState = OpenState.CLOSED;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation anim) {
                if (HomeTilesNavigationManager.this.mCurrentFragment != null && HomeTilesNavigationManager.this.mCurrentFragment.get() != null) {
                    Fragment fragment = (Fragment) HomeTilesNavigationManager.this.mCurrentFragment.get();
                    FragmentTransaction fragTransaction = HomeTilesNavigationManager.this.mFragmentManager.beginTransaction();
                    fragTransaction.setTransition(0);
                    fragTransaction.remove(fragment);
                    if (HomeTilesNavigationManager.this.isSafeToCommitFragmentTransactions()) {
                        try {
                            fragTransaction.commit();
                        } catch (IllegalStateException ex) {
                            KLog.w(HomeTilesNavigationManager.TAG, "Failed to commit transaction", ex);
                        }
                    }
                    HomeTilesNavigationManager.this.mCurrentFragment = null;
                }
                for (int i = 0; i < HomeTilesNavigationManager.this.mHomeTiles.size(); i++) {
                    BaseTile tile = (BaseTile) HomeTilesNavigationManager.this.mHomeTiles.get(i);
                    if (tile instanceof HomeTile) {
                        ((HomeTile) tile).setDividerVisibility(0);
                    }
                    float y = ((Float) HomeTilesNavigationManager.this.mHomeTilesYPositions.get(i)).floatValue();
                    HomeTilesNavigationManager.this.animateTile(tile, y, HomeTilesNavigationManager.this.mTilesAnimationClosingInDuration);
                }
                HomeTilesNavigationManager.this.getHomeActivity().animateTopMenu(0.0f, HomeTilesNavigationManager.this.mTopMenuTranslationDownDuration, HomeTilesNavigationManager.this.mTopMenuTranslationDownStartDelay, null);
                HomeTilesNavigationManager.this.getHomeActivity().animateMainViewContainer(HomeTilesNavigationManager.this.mTopMenuHeight, HomeTilesNavigationManager.this.mMainContentTranslationDownDuration, HomeTilesNavigationManager.this.mMainContentTranslationDownStartDelay, null);
                HomeTilesNavigationManager.this.getHomeActivity().revertMainContentHeight(HomeTilesNavigationManager.this.mTopMenuTranslationDownDuration);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation anim) {
            }
        });
        this.mContentOpenAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_in_down);
        this.mContentOpenAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.microsoft.kapp.navigations.HomeTilesNavigationManager.3
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                MainContentFragment fragment = new MainContentFragment(HomeTilesNavigationManager.this.mCurrentEventID, HomeTilesNavigationManager.this.mCurrentfragmentsData, HomeTilesNavigationManager.this.mCurrentHeaderbackgroundColor);
                FragmentTransaction fragTransaction = HomeTilesNavigationManager.this.mFragmentManager.beginTransaction();
                fragTransaction.replace(HomeTilesNavigationManager.this.mFragmentContainer.getId(), fragment);
                if (HomeTilesNavigationManager.this.isSafeToCommitFragmentTransactions()) {
                    fragTransaction.commit();
                    HomeTilesNavigationManager.this.mCurrentFragment = new WeakReference(fragment);
                    ((HomeTileStateChangedNotifier) HomeTileStateChangedNotifier.class.cast(HomeTilesNavigationManager.this.mCurrentOpenTile)).setHomeTileStateChangedListenerWeakRef(fragment);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HomeTilesNavigationManager.this.mOpenState = OpenState.OPEN;
                if (HomeTilesNavigationManager.this.mCurrentFragment != null && HomeTilesNavigationManager.this.mCurrentFragment.get() != null) {
                    ((MainContentFragment) HomeTilesNavigationManager.this.mCurrentFragment.get()).onHomeTileAnimationComplete();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void collectInitialTilePositions() {
        this.mHomeTilesYPositions.clear();
        Iterator i$ = this.mHomeTiles.iterator();
        while (i$.hasNext()) {
            BaseTile tile = i$.next();
            float y = tile.getY();
            this.mHomeTilesYPositions.add(Float.valueOf(y));
        }
    }

    public void addTile(BaseTile tile) {
        if (!this.mIsLayoutListenerRegistered) {
            this.mIsLayoutListenerRegistered = true;
            this.mTilesContainer.getViewTreeObserver().addOnGlobalLayoutListener(this.mLayoutListener);
        }
        this.mTilesContainer.addView(tile);
        this.mHomeTiles.add(tile);
    }

    private void showFragment(String eventId, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, FragmentManager fragmentManager, int headerBackgroundColor) {
        this.mCurrentEventID = eventId;
        this.mFragmentManager = fragmentManager;
        this.mCurrentfragmentsData = fragmentsData;
        this.mCurrentHeaderbackgroundColor = headerBackgroundColor;
        this.mFragmentContainer.startAnimation(this.mContentOpenAnimation);
    }

    private void refreshFragment(List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, FragmentManager fragmentManager, int headerBackgroundColor, boolean waitForAnimationThenLoad) {
        MainContentFragment fragment = new MainContentFragment(this.mCurrentEventID, fragmentsData, headerBackgroundColor, waitForAnimationThenLoad);
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.replace(this.mFragmentContainer.getId(), fragment);
        if (isSafeToCommitFragmentTransactions()) {
            fragTransaction.commit();
            this.mCurrentFragment = new WeakReference<>(fragment);
        }
    }

    private void hideFragment(FragmentManager fragmentManager) {
        if (this.mCurrentFragment != null && this.mCurrentFragment.get() != null) {
            this.mFragmentManager = fragmentManager;
            this.mFragmentContainer.startAnimation(this.mContentCloseAnimation);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateTile(View view, float yPosition, int animationDuration) {
        view.animate().y(yPosition).setDuration(animationDuration).setInterpolator(new ExponentInterpolator()).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveTileToY(View view, int yPosition) {
        int y = view.getTop();
        view.setTranslationY(yPosition - y);
    }

    public void refresh(FragmentManager fragmentManager) {
        if (this.mOpenState == OpenState.OPEN) {
            refreshFragment(this.mCurrentFragmentsData, fragmentManager, this.mCurrentOpenTile.getBackgroundColor(), false);
        }
    }

    public boolean isOpen() {
        return this.mOpenState == OpenState.OPEN;
    }

    public void toggle(BaseTile homeTile, String eventID, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, FragmentManager fragmentManager) {
        HashMap<String, String> telemetryProperties = new HashMap<>();
        telemetryProperties.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME, homeTile.getTelemetryName());
        telemetryProperties.put("Action", isOpen() ? TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_CLOSE : TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_OPEN);
        telemetryProperties.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.FIRST_TIME_USE, "No");
        Telemetry.logEvent(TelemetryConstants.Events.LogHomeTileTap.EVENT_NAME, telemetryProperties, null);
        if (this.mOpenState == OpenState.CLOSED) {
            this.mOpenState = OpenState.OPENING;
            boolean isPreceedingTile = true;
            this.mCurrentOpenTile = homeTile;
            this.mCurrentFragmentsData = fragmentsData;
            this.mScrollView.setIsScrollEnabled(false);
            this.mTileContainer.setBackgroundColor(homeTile.getBackgroundColor());
            getHomeActivity().expandMainContentToFullHeight(this.mTopMenuTranslationUpDuration);
            showFragment(eventID, fragmentsData, fragmentManager, homeTile.getBackgroundColor());
            int currentScrollY = this.mScrollView.getScrollY();
            int newHomeTileScrollY = currentScrollY;
            if (currentScrollY != 0) {
                int mainContentNextHeightValue = getHomeActivity().getMainContentHeight() + this.mTopMenuHeight;
                int scrollViewLeftBottomSpaceHight = this.mScrollView.getChildAt(0).getHeight() - currentScrollY;
                int delta = scrollViewLeftBottomSpaceHight - mainContentNextHeightValue;
                if (delta >= 0) {
                    delta = 0;
                }
                newHomeTileScrollY += delta;
            }
            if (homeTile instanceof HomeTile) {
                ((HomeTile) homeTile).setDividerVisibility(8);
            }
            for (int i = 0; i < this.mHomeTiles.size(); i++) {
                BaseTile tile = this.mHomeTiles.get(i);
                if (homeTile == tile) {
                    isPreceedingTile = false;
                    animateTile(tile, newHomeTileScrollY - (this.mTileHeight - this.mHeaderTileVisibleHeight), this.mTilesAnimationOpeningOutDuration);
                } else if (isPreceedingTile) {
                    animateTile(tile, newHomeTileScrollY - this.mTileHeight, this.mTilesAnimationOpeningOutDuration);
                } else {
                    animateTile(tile, this.mScreenHeight + newHomeTileScrollY, this.mTilesAnimationOpeningOutDuration);
                }
            }
            getHomeActivity().animateTopMenu(-this.mTopMenuHeight, this.mTopMenuTranslationUpDuration, this.mTopMenuTranslationUpStartDelay, null);
            getHomeActivity().animateMainViewContainer(0.0f, this.mTopMenuTranslationUpDuration, this.mMainContentTranslationUpStartDelay, null);
            return;
        }
        hideContent(fragmentManager);
    }

    private void hideContent(FragmentManager fragmentManager) {
        this.mOpenState = OpenState.CLOSING;
        this.mScrollView.setIsScrollEnabled(true);
        hideFragment(fragmentManager);
    }

    public void close(FragmentManager fragmentManager) {
        if (this.mOpenState == OpenState.OPEN) {
            hideContent(fragmentManager);
        }
    }

    private int getInteger(int resource) {
        return getResources().getInteger(resource);
    }

    protected Resources getResources() {
        return this.mContext.getResources();
    }

    protected HomeActivityAnimationListener getHomeActivity() {
        return this.mHomeActivityAnimationListener;
    }

    public boolean isSafeToCommitFragmentTransactions() {
        return ((FragmentNavigationCommandV1.FragmentNavigationListener) FragmentNavigationCommandV1.FragmentNavigationListener.class.cast(this.mContext)).isSafeToCommitFragmentTransactions();
    }

    public void restoreOpenTileState(final BaseTile homeTile, String eventID, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, FragmentManager fragmentManager, int currentScrollY, int contentSize) {
        this.mOpenState = OpenState.OPEN;
        this.mCurrentOpenTile = homeTile;
        this.mCurrentEventID = eventID;
        this.mCurrentFragmentsData = fragmentsData;
        this.mScrollView.setIsScrollEnabled(false);
        int newHomeTileScrollY = currentScrollY;
        Fragment fragment = fragmentManager.findFragmentById(R.id.home_tiles_fragment_container);
        this.mCurrentFragment = new WeakReference<>(fragment);
        ((HomeTileStateChangedNotifier) HomeTileStateChangedNotifier.class.cast(this.mCurrentOpenTile)).setHomeTileStateChangedListenerWeakRef((HomeTileStateChangedListener) HomeTileStateChangedListener.class.cast(fragment));
        if (currentScrollY != 0) {
            int mainContentNextHeightValue = getHomeActivity().getMainContentHeight() + this.mTopMenuHeight;
            int scrollViewLeftBottomSpaceHight = contentSize - currentScrollY;
            int delta = scrollViewLeftBottomSpaceHight - mainContentNextHeightValue;
            if (delta >= 0) {
                delta = 0;
            }
            newHomeTileScrollY += delta;
        }
        final int finalNewHomeTileScrollY = newHomeTileScrollY;
        ViewTreeObserver viewTreeObserver = this.mTilesContainer.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.navigations.HomeTilesNavigationManager.4
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int positionOfTile;
                HomeTilesNavigationManager.this.mTilesContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                boolean isPreceedingTile = true;
                for (int i = 0; i < HomeTilesNavigationManager.this.mHomeTiles.size(); i++) {
                    BaseTile tile = (BaseTile) HomeTilesNavigationManager.this.mHomeTiles.get(i);
                    if (homeTile == tile) {
                        isPreceedingTile = false;
                        positionOfTile = finalNewHomeTileScrollY - (HomeTilesNavigationManager.this.mTileHeight - HomeTilesNavigationManager.this.mHeaderTileVisibleHeight);
                    } else {
                        positionOfTile = isPreceedingTile ? -HomeTilesNavigationManager.this.mTileHeight : HomeTilesNavigationManager.this.mScreenHeight + finalNewHomeTileScrollY;
                    }
                    HomeTilesNavigationManager.this.moveTileToY(tile, positionOfTile);
                }
            }
        };
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        }
        HomeActivityAnimationListener homeActivityAnimationListener = getHomeActivity();
        homeActivityAnimationListener.moveTopMenuToY(-this.mTopMenuHeight);
        homeActivityAnimationListener.moveMainViewContainerToY(0);
        homeActivityAnimationListener.expandMainContentToFullHeight(0);
        homeActivityAnimationListener.invalidateAllViews();
    }

    public void clear() {
        this.mHomeTiles.clear();
        this.mTilesContainer.removeAllViews();
        this.mTilesContainer.invalidate();
    }

    public boolean isTransitioning() {
        return this.mOpenState == OpenState.CLOSING || this.mOpenState == OpenState.OPENING;
    }
}

package com.microsoft.kapp.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.microsoft.kapp.FirmwareVersionSaveTask;
import com.microsoft.kapp.OnEventModifiedListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.HomeTilesFragment;
import com.microsoft.kapp.fragments.LeftNavigationFragment;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.fragments.TopMenuFragment;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.GooglePlayUtils;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.UpgradeUtils;
import com.microsoft.krestsdk.models.AppConfiguration;
import com.microsoft.krestsdk.models.EventType;
import java.util.List;
import javax.inject.Inject;
import net.hockeyapp.android.UpdateManager;
/* loaded from: classes.dex */
public class HomeActivity extends BaseFragmentActivity implements TopMenuFragment.OnTopMenuClickListener, FragmentNavigationCommandV1.FragmentNavigationListener, LeftNavigationFragment.SyncButtonClickListener, HomeActivityAnimationListener, TopMenuFragment.TopMenuConfiguration, MainLayoutContainerViewProvider, OnEventModifiedListener, NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged {
    private static final String MAIN_CONTENT_HEIGHT = "mMainContentHeight";
    private static final String MAIN_CONTENT_SAVED_HEIGHT = "mMainContentSavedHeight";
    private static final int NOTIFICATION_ID = 1234;
    public static final String REFERRER = "Referrer";
    public static final String STARTING_PAGE = "StartingPage";
    private static final String TAG = HomeActivity.class.getSimpleName();
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private DrawerLayout mDrawerLayout;
    private FirmwareVersionSaveTask mFirmwareVersionSaveTask;
    @Inject
    GooglePlayUtils mGooglePlayUtils;
    private RelativeLayout mMainContentContainer;
    private int mMainContentHeight;
    private int mMainContentSavedHeight;
    private LinearLayout mMainLayoutContainer;
    private NetworkConnectivityChangedReceiver mNetworkChangedReceiver;
    private FrameLayout mTopMenuContainer;
    private int mTopMenuHeight;
    @Inject
    UserProfileFetcher mUserProfileFetcher;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mMainContentHeight = savedInstanceState.getInt(MAIN_CONTENT_HEIGHT);
            this.mMainContentSavedHeight = savedInstanceState.getInt(MAIN_CONTENT_SAVED_HEIGHT);
        }
        this.mTopMenuHeight = getResources().getDimensionPixelSize(R.dimen.app_header_bar_height);
        if (!HomeData.getInstance().isInitialized()) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            initViews();
        } else {
            String startingActivity = getIntent().getStringExtra(STARTING_PAGE);
            if (startingActivity != null) {
                if (startingActivity.equals(ManageTilesFragment.class.getSimpleName())) {
                    addHomeContent(new ManageTilesFragment());
                } else if (startingActivity.equals(BrowseGuidedWorkoutsFragment.class.getSimpleName())) {
                    addHomeContent(new BrowseGuidedWorkoutsFragment());
                } else if (startingActivity.equals(GolfLandingPageFragment.class.getSimpleName())) {
                    addHomeContent(GolfLandingPageFragment.newInstance(getIntent().getStringExtra("Referrer")));
                } else {
                    addHomeContent();
                }
            } else {
                addHomeContent();
            }
            addNavigationMenus();
        }
        ViewTreeObserver viewTreeObserver = this.mMainLayoutContainer.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.activities.HomeActivity.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                HomeActivity.this.mMainLayoutContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                HomeActivity.this.mMainContentHeight = HomeActivity.this.mMainContentContainer.getHeight();
                HomeActivity.this.mMainContentSavedHeight = HomeActivity.this.mMainContentHeight;
            }
        };
        if (viewTreeObserver.isAlive() && this.mMainContentHeight == 0) {
            viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        }
        if (Compatibility.shouldIntegrateHockeyApp(this)) {
            UpdateManager.register(this, Constants.HOCKEYAPP_APP_ID);
        }
        this.mUserProfileFetcher.updateLocallyStoredValuesAsync();
        this.mFirmwareVersionSaveTask = new FirmwareVersionSaveTask(this.mCargoConnection, this.mSettingsProvider);
        this.mFirmwareVersionSaveTask.saveFirmwareVersionToSettingsAsync();
        UpgradeUtils.updateAppJustUpgraded(this.mSettingsProvider, this);
        AppConfiguration config = this.mAppConfigurationManager.getAppConfiguration();
        Log.i(TAG, "config" + config.getName());
    }

    private void initViews() {
        setContentView(R.layout.activity_home);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerLayout.setScrimColor(0);
        this.mMainLayoutContainer = (LinearLayout) findViewById(R.id.home_page_area);
        this.mTopMenuContainer = (FrameLayout) findViewById(R.id.top_menu_container);
        this.mMainContentContainer = (RelativeLayout) findViewById(R.id.home_content_area);
        this.mDrawerLayout.setDrawerListener(new NavigationDrawerListener(this.mMainLayoutContainer, (FrameLayout) findViewById(R.id.left_nav_container)));
        this.mDrawerLayout.setDrawerLockMode(1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        if (!FreUtils.freRedirect(this, this.mSettingsProvider)) {
            Context context = getApplicationContext();
            SyncUtils.ensurePeriodicTasksSchedule(context, this.mSettingsProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity
    public void onResumeFragments() {
        super.onResumeFragments();
        this.mNetworkChangedReceiver = new NetworkConnectivityChangedReceiver(this);
        this.mNetworkChangedReceiver.register(this);
        this.mGooglePlayUtils.ensureGooglePlayAvailable(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mNetworkChangedReceiver.unregister(this);
        stopNoInternetNotification();
        if (Compatibility.shouldIntegrateHockeyApp(this)) {
            UpdateManager.unregister();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
            toggleLeftNav();
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment baseFragment = (BaseFragment) BaseFragment.class.cast(fragmentManager.findFragmentById(R.id.home_content_area));
        if (baseFragment != null && !baseFragment.handleBackButton()) {
            revertAnimationChanges();
            super.onBackPressed();
            return;
        }
        revertAnimationChanges();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int actionCode, Intent data) {
        FragmentManager fragmentManager;
        List<Fragment> allFragments;
        if (requestCode == 109 && actionCode == 0) {
            boolean isUserEventUpdate = data.getBooleanExtra(Constants.INTENT_USER_EVENT_UPDATE, false);
            if (isUserEventUpdate && (allFragments = (fragmentManager = getSupportFragmentManager()).getFragments()) != null && allFragments.size() > 0) {
                Fragment childFragment = fragmentManager.findFragmentById(R.id.home_content_area);
                if (childFragment != null) {
                    childFragment.onActivityResult(requestCode, actionCode, data);
                    return;
                }
                return;
            }
        }
        super.onActivityResult(requestCode, actionCode, data);
    }

    private void revertAnimationChanges() {
        animateTopMenu(0.0f, 0, 0, null);
        animateMainViewContainer(this.mTopMenuContainer.getHeight(), 0, 0, null);
        if (this.mMainContentContainer.getLayoutParams().height != this.mMainContentSavedHeight) {
            revertMainContentHeight(0);
        }
    }

    private void addHomeContent() {
        addHomeContent(HomeTilesFragment.newInstance());
    }

    private void addHomeContent(Fragment startingFragment) {
        initViews();
        if (isSafeToCommitFragmentTransactions()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.top_menu_container, TopMenuFragment.newInstance());
            fragmentTransaction.add(R.id.home_content_area, startingFragment, getClassTag(startingFragment.getClass()));
            fragmentTransaction.commit();
            return;
        }
        KLog.i(TAG, "Fragment Transaction was blocked to prevent a state loss");
    }

    @Override // com.microsoft.kapp.activities.MainLayoutContainerViewProvider
    public ViewGroup getMainLayoutContainerView() {
        return this.mMainLayoutContainer;
    }

    private void addNavigationMenus() {
        if (isSafeToCommitFragmentTransactions()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.left_nav_container, LeftNavigationFragment.newInstance());
            fragmentTransaction.commit();
            return;
        }
        KLog.i(TAG, "Fragment Transaction was blocked to prevent a state loss");
    }

    @Override // com.microsoft.kapp.fragments.TopMenuFragment.OnTopMenuClickListener
    public void onLeftNavClick() {
        toggleLeftNav();
    }

    private void toggleLeftNav() {
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
            this.mDrawerLayout.closeDrawer(8388611);
        } else {
            this.mDrawerLayout.openDrawer(8388611);
        }
    }

    @Override // com.microsoft.kapp.navigations.FragmentNavigationCommandV1.FragmentNavigationListener
    public void navigateToFragment(Class fragmentClass, Bundle args, boolean addToBackStack) {
        navigateToFragment(fragmentClass, args, addToBackStack, true);
    }

    @Override // com.microsoft.kapp.navigations.FragmentNavigationCommandV1.FragmentNavigationListener
    public void navigateToFragment(Class fragmentClass, Bundle args, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        revertAnimationChanges();
        if (shouldToggleSlidingMenu) {
            toggleLeftNav();
        }
        BaseFragment baseFragment = (BaseFragment) BaseFragment.class.cast(fragmentManager.findFragmentById(R.id.home_content_area));
        if (baseFragment == null || !baseFragment.handleNavigateToFragment(fragmentClass, addToBackStack, shouldToggleSlidingMenu)) {
            String fragmentTag = getClassTag(fragmentClass);
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment == null || !fragment.isVisible()) {
                try {
                    Fragment fragment2 = (Fragment) Fragment.class.cast(fragmentClass.newInstance());
                    if (fragment2 != null && isSafeToCommitFragmentTransactions()) {
                        fragment2.setArguments(args);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.home_content_area, fragment2, fragmentTag);
                        if (addToBackStack) {
                            fragmentTransaction.addToBackStack(null);
                        }
                        fragmentTransaction.commit();
                        return;
                    }
                    KLog.i(TAG, "Fragment Transaction was blocked to prevent a state loss");
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Could not instantiate fragment.", ex);
                } catch (InstantiationException ex2) {
                    throw new IllegalStateException("Could not instantiate fragment.", ex2);
                }
            }
        }
    }

    private ViewGroup getMainContentContainer() {
        return this.mMainContentContainer;
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void animateTopMenu(float yPosition, int animationDuration, int startDelay, Runnable postAction) {
        if (Validate.isActivityAlive(this)) {
            animateView(this.mTopMenuContainer, yPosition, animationDuration, startDelay, postAction);
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void animateMainViewContainer(float yPosition, int animationDuration, int startDelay, Runnable postAction) {
        if (Validate.isActivityAlive(this)) {
            animateView(getMainContentContainer(), yPosition, animationDuration, startDelay, postAction);
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void setTopMenuY(float yPosition) {
        if (Validate.isActivityAlive(this)) {
            this.mTopMenuContainer.setY(yPosition);
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void setMainContentContainerY(float yPosition) {
        if (Validate.isActivityAlive(this)) {
            this.mMainContentContainer.setY(yPosition);
        }
    }

    private void animateView(View view, float yPosition, int animationDuration, int startDelay, Runnable postAction) {
        view.animate().y(yPosition).setDuration(animationDuration).setInterpolator(new DecelerateInterpolator()).setStartDelay(startDelay).withEndAction(postAction).start();
    }

    @Override // com.microsoft.kapp.fragments.LeftNavigationFragment.SyncButtonClickListener
    public void onSyncButtonClick() {
        toggleLeftNav();
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public int getMainContentHeight() {
        if (this.mMainContentHeight == 0) {
            this.mMainContentHeight = this.mMainContentContainer.getHeight();
        }
        return this.mMainContentHeight;
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public int getTopMenuHeight() {
        return this.mTopMenuHeight;
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void expandMainContentToFullHeight(int animationDuration) {
        if (Validate.isActivityAlive(this)) {
            this.mMainContentSavedHeight = this.mMainContentContainer.getLayoutParams().height;
            Animation anidelta = new ResizeAnimation(this.mMainContentContainer, getMainContentHeight(), getMainContentHeight() + getTopMenuHeight());
            anidelta.setDuration(animationDuration);
            anidelta.setInterpolator(new DecelerateInterpolator());
            this.mMainContentContainer.startAnimation(anidelta);
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void revertMainContentHeight(int animationDuration) {
        if (Validate.isActivityAlive(this)) {
            this.mMainContentContainer.getLayoutParams().height = this.mMainContentSavedHeight;
            Animation anidelta = new ResizeAnimation(this.mMainContentContainer, getMainContentHeight() + getTopMenuHeight(), getMainContentHeight());
            anidelta.setDuration(animationDuration);
            anidelta.setInterpolator(new DecelerateInterpolator());
            this.mMainContentContainer.startAnimation(anidelta);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MAIN_CONTENT_HEIGHT, this.mMainContentHeight);
        outState.putInt(MAIN_CONTENT_SAVED_HEIGHT, this.mMainContentSavedHeight);
    }

    private String getClassTag(Class objectClass) {
        return objectClass.getSimpleName();
    }

    @Override // com.microsoft.kapp.OnEventModifiedListener
    public void onEventRenamed(EventType eventType, String eventName, String eventID) {
        refreshTileName(eventType, eventName);
    }

    @Override // com.microsoft.kapp.OnEventModifiedListener
    public void onEventDeleted(EventType eventType, String eventID) {
        refreshTiles();
    }

    public void refreshTiles() {
        HomeTilesFragment homeTilesFragment = (HomeTilesFragment) getSupportFragmentManager().findFragmentByTag(HomeTilesFragment.class.getSimpleName());
        if (homeTilesFragment != null) {
            homeTilesFragment.refresh(true);
        }
    }

    public void refreshTileName(EventType type, String name) {
        HomeTilesFragment homeTilesFragment = (HomeTilesFragment) getSupportFragmentManager().findFragmentByTag(HomeTilesFragment.class.getSimpleName());
        if (homeTilesFragment != null) {
            homeTilesFragment.refreshName(type, name);
        }
    }

    @Override // com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkDisconnected() {
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this).setTicker(getString(R.string.error_offline_status_bar)).setContentTitle(getString(R.string.error_offline_status_bar)).setSmallIcon(R.drawable.ic_stat_warning).setAutoCancel(false).setOngoing(true).setContentIntent(contentIntent).build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override // com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkConnected() {
        stopNoInternetNotification();
    }

    private void stopNoInternetNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService("notification");
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    @Override // com.microsoft.kapp.fragments.TopMenuFragment.TopMenuConfiguration
    public void setTopMenuColor(int color) {
        TopMenuFragment topMenuFragment = (TopMenuFragment) getSupportFragmentManager().findFragmentById(R.id.top_menu_container);
        if (topMenuFragment != null) {
            topMenuFragment.setBackgroundColor(color);
        }
    }

    @Override // com.microsoft.kapp.fragments.TopMenuFragment.TopMenuConfiguration
    public void setTopMenuDividerVisibility(int visibility) {
        TopMenuFragment topMenuFragment = (TopMenuFragment) getSupportFragmentManager().findFragmentById(R.id.top_menu_container);
        if (topMenuFragment != null) {
            topMenuFragment.setDividerVisibility(visibility);
        }
    }

    /* loaded from: classes.dex */
    public class ResizeAnimation extends Animation {
        private int deltaHeight;
        private int startHeight;
        private View view;

        public ResizeAnimation(View v, int startHeight, int endHeight) {
            this.view = v;
            this.startHeight = startHeight;
            this.deltaHeight = endHeight - startHeight;
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            this.view.getLayoutParams().height = (int) (this.startHeight + (this.deltaHeight * interpolatedTime));
            this.view.requestLayout();
        }

        @Override // android.view.animation.Animation
        public void setDuration(long durationMillis) {
            super.setDuration(durationMillis);
        }

        @Override // android.view.animation.Animation
        public boolean willChangeBounds() {
            return true;
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void moveTopMenuToY(int yPosition) {
        moveViewToY(this.mTopMenuContainer, yPosition);
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void moveMainViewContainerToY(int yPosition) {
        moveViewToY(this.mMainContentContainer, yPosition);
    }

    public void moveViewToY(final View view, final int yPosition) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.activities.HomeActivity.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int y = view.getTop();
                view.setTranslationY(yPosition - y);
            }
        };
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        }
    }

    @Override // com.microsoft.kapp.activities.HomeActivityAnimationListener
    public void invalidateAllViews() {
        this.mMainLayoutContainer.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class NavigationDrawerListener extends DrawerLayout.SimpleDrawerListener {
        private View homePageArea;
        private FrameLayout navContainer;

        public NavigationDrawerListener(View homePageArea, FrameLayout navContainer) {
            this.navContainer = navContainer;
            this.homePageArea = homePageArea;
        }

        @Override // android.support.v4.widget.DrawerLayout.SimpleDrawerListener, android.support.v4.widget.DrawerLayout.DrawerListener
        public void onDrawerSlide(View drawerView, float slideOffset) {
            this.homePageArea.setTranslationX(this.navContainer.getWidth() * slideOffset);
        }
    }
}

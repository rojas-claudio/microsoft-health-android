package com.microsoft.kapp.fragments.whatsnew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.models.whatsnew.WhatsNewCardDataModel;
import com.microsoft.kapp.models.whatsnew.WhatsNewCards;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.PageIndicatorView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WhatsNewFragment extends BaseFragment {
    private List<WhatsNewCardDataModel> mCards = new ArrayList();
    private CarouselPagerAdapter mCarouselPagerAdapter;
    private PageIndicatorView mPageIndicatorView;
    @Inject
    SettingsProvider mSettingsProvider;
    private ViewPager mViewPager;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsnew, container, false);
        this.mViewPager = (ViewPager) ViewUtils.getValidView(rootView, R.id.whatsnew_carousel, ViewPager.class);
        this.mPageIndicatorView = (PageIndicatorView) ViewUtils.getValidView(rootView, R.id.whatsnew_page_indicator, PageIndicatorView.class);
        this.mCarouselPagerAdapter = new CarouselPagerAdapter(getChildFragmentManager());
        this.mViewPager.setAdapter(this.mCarouselPagerAdapter);
        this.mPageIndicatorView.setPageCount(0);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                WhatsNewFragment.this.mPageIndicatorView.setCurrentPage(position);
                WhatsNewCardDataModel card = (WhatsNewCardDataModel) WhatsNewFragment.this.mCards.get(position);
                WhatsNewFragment.this.sendTelemetryWhenCardLoaded(card);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        updateCarousel(WhatsNewCards.getCards(getResources()));
        clearNotificationDot();
        int appSessionCount = this.mSettingsProvider.getAppSessionCountBeforeClickWhatsNew();
        if (appSessionCount > 0) {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("Sessions", Integer.toString(appSessionCount));
            Telemetry.logEvent(TelemetryConstants.Events.WhatsNew.SESSIONS, properties, null);
            this.mSettingsProvider.setAppSessionCountBeforeClickWhatsNew(0);
        }
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
        Telemetry.logEvent(TelemetryConstants.Events.WhatsNew.OPEN);
        if (this.mCards.size() > 0) {
            sendTelemetryWhenCardLoaded(this.mCards.get(0));
        }
        setTopMenuBarColor(getResources().getColor(R.color.PrimaryMediumColor));
    }

    public void updateCarousel(List<WhatsNewCardDataModel> cards) {
        this.mCards = cards;
        this.mPageIndicatorView.setPageCount(this.mCarouselPagerAdapter.getCount());
        this.mCarouselPagerAdapter.notifyDataSetChanged();
    }

    public void clearNotificationDot() {
        this.mSettingsProvider.setNotificationWhatsNewEnabled(false);
        Intent intent = new Intent(Constants.LEFTNAV_NOTIFICATION_BROADCAST_FILTER);
        intent.putExtra(Constants.LEFTNAV_NOTIFICATION_CLEAR, getResources().getString(R.string.navigation_item_title_what_s_new));
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTelemetryWhenCardLoaded(WhatsNewCardDataModel card) {
        String telemetryCardName = card.getCardType();
        if (!TextUtils.isEmpty(telemetryCardName)) {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("Card", telemetryCardName);
            Telemetry.logEvent(TelemetryConstants.Events.WhatsNew.Cards.CARDS, properties, null);
        }
    }

    /* loaded from: classes.dex */
    public class CarouselPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> _mFragments;

        public CarouselPagerAdapter(FragmentManager fm) {
            super(fm);
            this._mFragments = new ArrayList<>();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int index) {
            WhatsNewCardDataModel item = (WhatsNewCardDataModel) WhatsNewFragment.this.mCards.get(index);
            Fragment fragment = WhatsNewCardFragment.newInstance(item);
            this._mFragments.add(fragment);
            return fragment;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return WhatsNewFragment.this.mCards.size();
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 0;
    }
}

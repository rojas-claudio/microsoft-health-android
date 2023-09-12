package com.microsoft.kapp.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.EventType;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class HomeTileNoDataFragment extends BaseHomeTileFragment {
    private ImageView mMainImage;
    private TextView mOverviewText;
    @Inject
    SettingsProvider mSettingsProvider;
    private EventName mType;

    /* loaded from: classes.dex */
    public enum EventName {
        NO_ID,
        RUN,
        BIKE,
        SLEEP,
        WORKOUT,
        GUIDED_WORKOUT,
        GOLF
    }

    public static BaseFragment newInstance(String type, Boolean isL2View) {
        HomeTileNoDataFragment fragment = new HomeTileNoDataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_TILE_NO_DATA_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        try {
            this.mType = EventName.valueOf(savedBundle.getString(Constants.KEY_HOME_TILE_NO_DATA_TYPE));
        } catch (IllegalArgumentException e) {
            this.mType = EventName.NO_ID;
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.KEY_HOME_TILE_NO_DATA_TYPE, this.mType.toString());
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int image;
        String text;
        View rootView = inflater.inflate(R.layout.home_tile_no_data_fragment, container, false);
        this.mMainImage = (ImageView) ViewUtils.getValidView(rootView, R.id.main_image, ImageView.class);
        this.mOverviewText = (TextView) ViewUtils.getValidView(rootView, R.id.home_tile_no_data_overview_text, TextView.class);
        LinearLayout linearLayout = (LinearLayout) ViewUtils.getValidView(rootView, R.id.ftu_container, LinearLayout.class);
        switch (this.mType) {
            case RUN:
                image = R.drawable.run_ftu;
                Resources resources = getResources();
                Object[] objArr = new Object[1];
                objArr[0] = getResources().getString(this.mSettingsProvider.isDistanceHeightMetric() ? R.string.kilometers_abbreviation : R.string.mile_simple_text);
                text = resources.getString(R.string.home_tile_ftu_state_run_description, objArr);
                break;
            case BIKE:
                image = R.drawable.bike_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_bike_description);
                break;
            case SLEEP:
                image = R.drawable.sleep_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_sleep_description);
                break;
            case WORKOUT:
                image = R.drawable.exercise_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_workout_description);
                break;
            case GUIDED_WORKOUT:
                image = R.drawable.gw_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_guided_workout_description);
                break;
            case GOLF:
                image = R.drawable.golf_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_golf_description);
                break;
            default:
                image = R.drawable.gw_ftu;
                text = getResources().getString(R.string.home_tile_ftu_state_guided_workout_description);
                break;
        }
        this.mMainImage.setImageDrawable(getResources().getDrawable(image));
        this.mOverviewText.setText(text);
        if (!this.mMultiDeviceManager.hasEverHadBand()) {
            Button marketingText = (Button) ViewUtils.getValidView(rootView, R.id.home_tile_no_data_marketing_text, Button.class);
            View.OnClickListener planOverViewClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.HomeTileNoDataFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.BAND_HOME_URL));
                    HomeTileNoDataFragment.this.startActivity(browserIntent);
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put(TelemetryConstants.Events.FtuClick.Dimensions.PAGE, HomeTileNoDataFragment.this.mType.toString());
                    Telemetry.logEvent(TelemetryConstants.Events.FtuClick.EVENT_NAME, properties, null);
                }
            };
            marketingText.setOnClickListener(planOverViewClickListener);
            marketingText.setVisibility(0);
        }
        if (EventType.Golf.name().equalsIgnoreCase(this.mType.name())) {
            TextView homeTileLink = (TextView) ViewUtils.getValidView(rootView, R.id.home_tile_link, TextView.class);
            homeTileLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.HomeTileNoDataFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(BaseFragment.ARG_REFERRER, TelemetryConstants.PageViews.Referrers.FTU);
                    ((FragmentNavigationCommandV1.FragmentNavigationListener) HomeTileNoDataFragment.this.getActivity()).navigateToFragment(GolfLandingPageFragment.class, args, true, false);
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put(TelemetryConstants.Events.FtuClick.Dimensions.PAGE, HomeTileNoDataFragment.this.mType.toString());
                    Telemetry.logEvent(TelemetryConstants.Events.FtuClick.GOLF_EVENT_NAME, properties, null);
                }
            });
            homeTileLink.setVisibility(0);
        }
        HashMap<String, String> telemetryProperties = new HashMap<>();
        telemetryProperties.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME, getMappedTelemetryPropertyName(this.mType));
        telemetryProperties.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.FIRST_TIME_USE, "Yes");
        Telemetry.logEvent(TelemetryConstants.Events.LogHomeTileTap.EVENT_NAME, telemetryProperties, null);
        setState(1234);
        return rootView;
    }

    private String getMappedTelemetryPropertyName(EventName name) {
        switch (name) {
            case RUN:
                return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_RUN;
            case BIKE:
                return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_BIKE;
            case SLEEP:
                return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_SLEEP;
            case WORKOUT:
                return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_WORKOUT;
            case GUIDED_WORKOUT:
                return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_GW;
            default:
                return "";
        }
    }
}

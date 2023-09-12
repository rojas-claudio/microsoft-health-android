package com.microsoft.kapp.fragments.golf;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.TMAGConnectActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.ScorecardModel;
import com.microsoft.kapp.providers.golf.ScorecardProvider;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.TMAG.TMAGService;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.PicassoImageView;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfScorecardFragment extends BaseHomeTileFragment {
    public static final String GOLF_EVENT_ID = "golf_event_id";
    private String mGolfEventId;
    @Inject
    GolfService mGolfService;
    private TextView mLongestDriveHeader;
    private ExpandableListView mScorecardList;
    @Inject
    ScorecardProvider mScorecardProvider;
    private View mScoredcardListViewHeader;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    TMAGService mTMAGService;
    private PicassoImageView mTaylorMadeConnectImage;
    private TextView mTaylorMadeConnectLink;
    private TextView mTeesPlayedText;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        GolfScorecardFragment fragment = new GolfScorecardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOLF_EVENT_ID, eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mGolfEventId = savedBundle.getString(GOLF_EVENT_ID);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_SCORECARD);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.golf_scorecard_layout_fragment, container, false);
        this.mScorecardList = (ExpandableListView) ViewUtils.getValidView(view, R.id.golf_scorecard_listview, ExpandableListView.class);
        this.mScoredcardListViewHeader = inflater.inflate(R.layout.golf_scorecard_header, (ViewGroup) this.mScorecardList, false);
        this.mTaylorMadeConnectImage = (PicassoImageView) ViewUtils.getValidView(this.mScoredcardListViewHeader, R.id.taylormade_connect_image, PicassoImageView.class);
        this.mTaylorMadeConnectLink = (TextView) ViewUtils.getValidView(this.mScoredcardListViewHeader, R.id.connect_to_taylormade_link, TextView.class);
        this.mLongestDriveHeader = (TextView) ViewUtils.getValidView(view, R.id.golf_header_2, TextView.class);
        this.mScorecardList.addHeaderView(this.mScoredcardListViewHeader);
        View listFooter = inflater.inflate(R.layout.golf_scorecard_footer, (ViewGroup) this.mScorecardList, false);
        this.mTeesPlayedText = (TextView) ViewUtils.getValidView(listFooter, R.id.golf_tees_played_text, TextView.class);
        this.mScorecardList.addFooterView(listFooter);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        this.mScorecardProvider.getScoreCardForEvent(this.mGolfEventId, new ActivityScopedCallback(this, new Callback<ScorecardModel>() { // from class: com.microsoft.kapp.fragments.golf.GolfScorecardFragment.1
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GolfScorecardFragment.this.TAG, "getting golf scorecard failed.", ex);
                GolfScorecardFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(ScorecardModel scorecard) {
                if (scorecard == null) {
                    KLog.d(GolfScorecardFragment.this.TAG, "getting golf scorecard failed.");
                    GolfScorecardFragment.this.setState(1235);
                    return;
                }
                if (GolfScorecardFragment.this.mSettingsProvider.isDistanceHeightMetric()) {
                    GolfScorecardFragment.this.mLongestDriveHeader.setText(GolfScorecardFragment.this.getActivity().getString(R.string.golf_header_value_meters));
                } else {
                    GolfScorecardFragment.this.mLongestDriveHeader.setText(GolfScorecardFragment.this.getActivity().getString(R.string.golf_header_value_yards));
                }
                GolfScorecardFragment.this.showTaylorMadeLink();
                GolfScorecardFragment.this.displayScorecard(scorecard);
            }
        }));
    }

    protected void showTaylorMadeLink() {
        showTaylorMadeConnectStatus(8);
        this.mGolfService.isPartnerConnected(new ActivityScopedCallback(this, new Callback<Boolean>() { // from class: com.microsoft.kapp.fragments.golf.GolfScorecardFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(Boolean connected) {
                if (!connected.booleanValue()) {
                    GolfScorecardFragment.this.showTaylorMadeConnectStatus(0);
                    GolfScorecardFragment.this.mTaylorMadeConnectLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfScorecardFragment.2.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            Intent intent = new Intent(GolfScorecardFragment.this.getActivity(), TMAGConnectActivity.class);
                            intent.putExtra(GolfLandingPageFragment.IS_PARTNER_CONNECTED, true);
                            GolfScorecardFragment.this.startActivity(intent);
                        }
                    });
                }
                GolfScorecardFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.e(GolfScorecardFragment.this.TAG, "error getting TMAG connection status", ex);
                GolfScorecardFragment.this.setState(1234);
            }
        }));
    }

    protected void displayScorecard(ScorecardModel scorecard) {
        this.mTeesPlayedText.setText(scorecard.getScorecardTeesPlayed());
        ExpandableListAdapter listAdapter = new ScorecardExpandableListAdapter(getActivity(), this.mSettingsProvider, scorecard);
        this.mScorecardList.setAdapter(listAdapter);
        this.mScorecardList.setVisibility(0);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GOLF_EVENT_ID, this.mGolfEventId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTaylorMadeConnectStatus(int visibility) {
        if (visibility == 0) {
            Picasso.with(getActivity()).load(R.drawable.golf_noimage).fit().centerCrop().into(this.mTaylorMadeConnectImage);
        }
        if (8 == visibility) {
            this.mScorecardList.removeHeaderView(this.mScoredcardListViewHeader);
        } else {
            this.mScorecardList.addHeaderView(this.mScoredcardListViewHeader);
        }
    }
}

package com.microsoft.kapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackTypeFragment;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.logging.models.FeedbackDescription;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class FeedbackActivity extends BaseFragmentActivity implements FeedbackTypeFragment.FeedbackTypeListener, FeedbackDescriptionEditFragment.FeedbackDescriptionListener, FeedbackDescriptionFragment.FeedbackSummaryListener, FeedbackSummaryFragment.FeedbackConfirmationListener {
    public static final String KEY_CATEGORY = "feedback_category";
    public static final String KEY_DESCRIPTION_PROPERTIES = "description_properties";
    private static final String KEY_FEEDBACK_DESCRIPTION = "feedback_description";
    private static final String KEY_FEEDBACK_STATE = "feedback_state";
    public static final String KEY_HEADER_TEXT = "feedback_header";
    public static final String KEY_SCREENSHOT = "feedback_screenshot";
    public static final String KEY_SENDER = "feedback_sender";
    public static final String KEY_SKIP_INITIAL_PAGE = "skip_introductory_page";
    public static final String KEY_SUBCATEGORY = "feedback_subcategory";
    private static final String TAG = FeedbackActivity.class.getSimpleName();
    private View mBaseLayout;
    @Inject
    CredentialsManager mCredentialsManager;
    private String mCurrentDescription;
    private FeedbackState mCurrentFeedbackState = FeedbackState.START;
    private HashMap<String, String> mDescriptionProperties;
    private FeedbackDescription.FeedbackCategory mFeedbackCategory;
    private String mFeedbackSender;
    @Inject
    FeedbackService mFeedbackService;
    private FeedbackDescription.FeedbackSubcategory mFeedbackSubCategory;
    @Inject
    FiddlerLogger mFiddlerLogger;
    private String mHeaderText;
    private TextView mHeaderTextview;
    private View mHeaderView;
    private ArrayList<Uri> mImageUris;
    private Interstitial mLoadingScreen;
    private boolean mShouldSkipInitialPage;

    /* loaded from: classes.dex */
    public enum FeedbackState {
        START,
        DESCRIPTION,
        DESCRIPTION_EDIT,
        SUMMARY
    }

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        this.mLoadingScreen = (Interstitial) findViewById(R.id.loading_screen);
        this.mHeaderTextview = (TextView) findViewById(R.id.txtFeedbackHeaderTitle);
        this.mHeaderView = findViewById(R.id.feedbackHeaderLayout);
        this.mBaseLayout = findViewById(R.id.base_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loadValuesFromBundle(extras);
            if (this.mShouldSkipInitialPage) {
                this.mCurrentFeedbackState = FeedbackState.DESCRIPTION;
            }
        }
        HashMap<String, String> properties = new HashMap<>();
        properties.put("Referrer", this.mImageUris != null ? TelemetryConstants.PageViews.Referrers.LEFT_NAV : TelemetryConstants.PageViews.Referrers.SHAKE);
        Telemetry.logEvent(TelemetryConstants.PageViews.SEND_FEEDBACK_LAUNCH, properties, null);
        if (savedInstanceState != null) {
            this.mCurrentFeedbackState = FeedbackState.values()[savedInstanceState.getInt(KEY_FEEDBACK_STATE)];
            loadValuesFromBundle(savedInstanceState);
        } else {
            gotoFeedbackState(this.mCurrentFeedbackState);
        }
        launchLogCleanupService();
    }

    private void loadValuesFromBundle(Bundle bundle) {
        this.mCurrentDescription = bundle.getString(KEY_FEEDBACK_DESCRIPTION);
        this.mFeedbackSender = bundle.getString(KEY_SENDER);
        this.mHeaderText = bundle.getString(KEY_HEADER_TEXT);
        this.mShouldSkipInitialPage = bundle.getBoolean(KEY_SKIP_INITIAL_PAGE);
        ArrayList<String> imageUri = bundle.getStringArrayList(KEY_SCREENSHOT);
        if (imageUri != null) {
            this.mImageUris = new ArrayList<>();
            Iterator i$ = imageUri.iterator();
            while (i$.hasNext()) {
                String uri = i$.next();
                this.mImageUris.add(Uri.parse(uri));
            }
        }
        Serializable category = bundle.getSerializable(KEY_CATEGORY);
        if (category != null && (category instanceof FeedbackDescription.FeedbackCategory)) {
            this.mFeedbackCategory = (FeedbackDescription.FeedbackCategory) category;
        }
        Serializable subcategory = bundle.getSerializable(KEY_SUBCATEGORY);
        if (subcategory != null && (subcategory instanceof FeedbackDescription.FeedbackSubcategory)) {
            this.mFeedbackSubCategory = (FeedbackDescription.FeedbackSubcategory) subcategory;
        }
        Serializable descriptionProperties = bundle.getSerializable(KEY_DESCRIPTION_PROPERTIES);
        if (descriptionProperties != null && (descriptionProperties instanceof HashMap)) {
            this.mDescriptionProperties = (HashMap) descriptionProperties;
        }
    }

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_FEEDBACK_STATE, this.mCurrentFeedbackState.ordinal());
        outState.putBoolean(KEY_SKIP_INITIAL_PAGE, this.mShouldSkipInitialPage);
        outState.putString(KEY_FEEDBACK_DESCRIPTION, this.mCurrentDescription);
        outState.putString(KEY_SENDER, this.mFeedbackSender);
        outState.putString(KEY_SENDER, this.mHeaderText);
        outState.putSerializable(KEY_CATEGORY, this.mFeedbackCategory);
        outState.putSerializable(KEY_SUBCATEGORY, this.mFeedbackSubCategory);
        outState.putSerializable(KEY_FEEDBACK_DESCRIPTION, this.mDescriptionProperties);
        super.onSaveInstanceState(outState);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        goBack();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        FileUtils.clearLocalImageStorage(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack /* 2131099748 */:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    public FeedbackState getCurrentState() {
        return this.mCurrentFeedbackState;
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment.FeedbackConfirmationListener
    public void sendFeedback() {
        if (this.mCurrentFeedbackState == FeedbackState.SUMMARY) {
            final FeedbackSummaryFragment fragment = (FeedbackSummaryFragment) getSupportFragmentManager().findFragmentByTag(FeedbackState.SUMMARY.toString());
            Validate.notNull(fragment, "feedback attachments fragment");
            this.mLoadingScreen.setSlide(5000);
            Callback callback = new ActivityScopedCallback(this, new Callback<Boolean>() { // from class: com.microsoft.kapp.activities.FeedbackActivity.1
                @Override // com.microsoft.kapp.Callback
                public void callback(Boolean success) {
                    FeedbackActivity.this.mLoadingScreen.setSlide(Interstitial.SLIDE_GONE);
                    if (success.booleanValue()) {
                        HashMap<String, String> properties = new HashMap<>();
                        properties.put(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION, (FeedbackActivity.this.mCurrentDescription == null || FeedbackActivity.this.mCurrentDescription.length() == 0) ? "No" : "Yes");
                        properties.put(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.IMAGES, (FeedbackActivity.this.mImageUris == null || FeedbackActivity.this.mImageUris.size() == 0) ? "No" : "Yes");
                        properties.put(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.LOGS, fragment.shouldIncludeLogs() ? "Yes" : "No");
                        Telemetry.logEvent(TelemetryConstants.Events.SendFeedbackComplete.EVENT_NAME, properties, null);
                        FeedbackActivity.this.launchLogCleanupService();
                        FeedbackActivity.this.finish();
                        return;
                    }
                    FeedbackActivity.this.getDialogManager().showNetworkErrorDialog(FeedbackActivity.this);
                    Telemetry.logEvent(TelemetryConstants.Events.SendFeedbackFailure.EVENT_NAME);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    FeedbackActivity.this.mLoadingScreen.setSlide(Interstitial.SLIDE_GONE);
                    FeedbackActivity.this.getDialogManager().showNetworkErrorDialog(FeedbackActivity.this);
                    Telemetry.logEvent(TelemetryConstants.Events.SendFeedbackFailure.EVENT_NAME);
                }
            });
            FeedbackDescription feedbackDescription = new FeedbackDescription(this.mCurrentDescription, this.mFeedbackCategory, this.mFeedbackSubCategory, this.mDescriptionProperties);
            this.mFeedbackService.sendFeedbackAsync(this, this.mFeedbackSender != null ? this.mFeedbackSender : FeedbackActivity.class.getCanonicalName(), feedbackDescription, this.mCurrentDescription, this.mImageUris, this.mSettingsProvider.getUserProfile(), fragment.shouldIncludeLogs(), callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchLogCleanupService() {
        Intent logCleanup = new Intent(this, KAppBroadcastReceiver.class);
        logCleanup.setAction(Constants.INTENT_LOG_CLEANUP);
        sendBroadcast(logCleanup);
    }

    private void goBack() {
        switch (this.mCurrentFeedbackState) {
            case START:
                finish();
                break;
            case DESCRIPTION:
                if (this.mShouldSkipInitialPage) {
                    finish();
                }
                this.mCurrentFeedbackState = FeedbackState.START;
                break;
            case DESCRIPTION_EDIT:
                this.mCurrentFeedbackState = FeedbackState.DESCRIPTION;
                setHeaderVisibility(0);
                this.mBaseLayout.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case SUMMARY:
                this.mCurrentFeedbackState = FeedbackState.DESCRIPTION;
                break;
        }
        gotoFeedbackState(this.mCurrentFeedbackState);
    }

    private void gotoFeedbackState(FeedbackState state) {
        Fragment fragment;
        switch (state) {
            case START:
                fragment = FeedbackTypeFragment.newInstance(getResources().getStringArray(R.array.feedback_types_array));
                this.mHeaderTextview.setText(getResources().getString(R.string.feedback_send));
                break;
            case DESCRIPTION:
                FeedbackDescriptionFragment summaryFragment = FeedbackDescriptionFragment.newInstance();
                summaryFragment.setAttachments(this.mImageUris);
                fragment = summaryFragment;
                this.mHeaderTextview.setText(this.mHeaderText != null ? this.mHeaderText : getResources().getString(R.string.feedback_report_a_problem_title));
                setHeaderVisibility(0);
                this.mBaseLayout.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case DESCRIPTION_EDIT:
                fragment = FeedbackDescriptionEditFragment.newInstance();
                setHeaderVisibility(8);
                this.mBaseLayout.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
                break;
            case SUMMARY:
                fragment = FeedbackSummaryFragment.newInstance();
                break;
            default:
                return;
        }
        this.mCurrentFeedbackState = state;
        String tag = this.mCurrentFeedbackState.toString();
        getSupportFragmentManager().beginTransaction().replace(R.id.feedbackContainer, fragment, tag).commit();
    }

    private void setHeaderVisibility(int visibility) {
        this.mHeaderView.setVisibility(visibility);
        this.mHeaderTextview.setVisibility(visibility);
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackTypeFragment.FeedbackTypeListener
    public void onFeedbackTypeSelected(String type) {
        gotoFeedbackState(FeedbackState.DESCRIPTION);
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment.FeedbackDescriptionListener
    public void onFeedbackDescriptionEntered(String description) {
        this.mCurrentDescription = description;
        gotoFeedbackState(FeedbackState.DESCRIPTION);
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.FeedbackSummaryListener
    public void onNextButtonPressed() {
        gotoFeedbackState(FeedbackState.SUMMARY);
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.FeedbackSummaryListener
    public void setAttachments(ArrayList<Uri> imageUris) {
        this.mImageUris = imageUris;
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.FeedbackSummaryListener
    public void onEditTextButtonPressed() {
        gotoFeedbackState(FeedbackState.DESCRIPTION_EDIT);
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment.FeedbackDescriptionListener, com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment.FeedbackSummaryListener
    public String getFeedbackText() {
        return this.mCurrentDescription;
    }

    @Override // com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment.FeedbackConfirmationListener
    public String getFeedbackSummaryText() {
        StringBuilder feedbackSummary = new StringBuilder();
        boolean containsImages = (this.mImageUris == null || this.mImageUris.size() == 0) ? false : true;
        if (this.mCurrentDescription != null) {
            feedbackSummary.append(this.mCurrentDescription);
            if (containsImages) {
                feedbackSummary.append(System.getProperty("line.separator"));
            }
        }
        if (containsImages) {
            feedbackSummary.append(String.format(getString(R.string.feedback_number_of_images), Integer.valueOf(this.mImageUris.size())));
        }
        return feedbackSummary.toString();
    }
}

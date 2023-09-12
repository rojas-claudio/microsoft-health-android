package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.microsoft.kapp.OnEventModifiedListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver;
import com.microsoft.kapp.activities.UserEventDetailsActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.factories.ShareObject;
import com.microsoft.kapp.fragments.EventHistorySummaryFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.EventData;
import com.microsoft.kapp.models.UserEventSummary;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.parsers.BasicRaisedInsightFilters;
import com.microsoft.kapp.parsers.InsightsDisplayFilter;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.EventDeleteTaskV1;
import com.microsoft.kapp.tasks.EventRenameTaskV1;
import com.microsoft.kapp.tasks.RestQueryTaskBaseV1;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.HistoryUtils;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.InsightsWidget;
import com.microsoft.kapp.views.TrackableScrollView;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.ExerciseEventBase;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.charts.ShinobiChart;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.inject.Inject;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public abstract class BaseEventSummaryFragment extends BaseHomeTileFragment implements EventDeleteTaskV1.OnEventDeleteTaskListener, EventRenameTaskV1.OnEventRenameTaskListener, NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged, EventHistorySummaryFragment.NotificationsReceiver, ShinobiChart.OnSnapshotDoneListener {
    public static final int ALL_EVENTS = 1;
    public static final int ALL_ONES_INTEGER = -1;
    public static final String ARG_OUT_EVENT_NEW_NAME = "arg_out_event_new_name";
    protected static final int BIKE = 4;
    public static final int DELETE_EVENT = 8;
    protected static final int EXERCISE = 1;
    protected static final int GOLF = 5;
    public static final int RENAME_EVENT = 4;
    protected static final int RUN = 0;
    public static final int SHARE_EVENT = 2;
    protected static final int SLEEP = 2;
    protected static final String USER_EVENT_ID = "user_event_id";
    protected static final int WORKOUT = 3;
    private int mCurrentEventsOption;
    private TextView mDeleteButton;
    private ViewGroup mEventContainer;
    private EventHistorySummaryFragment mEventHistoryFragment;
    private ViewGroup mHistoryContainer;
    private InsightsWidget mInsightWidget;
    protected boolean mIsL2View;
    private NetworkConnectivityChangedReceiver mNetworkChangedReceiver;
    private View mProgressView;
    private TextView mRenameButton;
    private View mRenameContainer;
    private EditText mRenameEditText;
    private CustomGlyphView mRenameStateButton;
    @Inject
    protected RestService mRestService;
    private TrackableScrollView mScrollView;
    @Inject
    protected SettingsProvider mSettings;
    private TextView mShareButton;
    private String mTempEventName;
    protected String mUserEventId;
    private boolean mHasReferenceToViews = false;
    private int mEventsOptionPending = -1;

    protected abstract int getEventType();

    protected abstract UserEvent getUserEvent();

    public abstract View onCreateEventView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        if (bundle != null) {
            this.mIsL2View = bundle.getBoolean(Constants.EVENT_L2_VIEW);
            this.mUserEventId = bundle.getString(USER_EVENT_ID);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public final View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_event_summary, container, false);
        this.mRenameContainer = rootView.findViewById(R.id.event_rename_container);
        this.mRenameEditText = (EditText) ViewUtils.getValidView(rootView, R.id.event_rename_edit_text, EditText.class);
        this.mRenameStateButton = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.event_rename_state, CustomGlyphView.class);
        this.mEventContainer = (ViewGroup) ViewUtils.getValidView(rootView, R.id.event_summary_conteiner, ViewGroup.class);
        this.mHistoryContainer = (ViewGroup) ViewUtils.getValidView(rootView, R.id.event_history_summary_container, ViewGroup.class);
        this.mShareButton = (TextView) ViewUtils.getValidView(rootView, R.id.event_share_button, TextView.class);
        this.mRenameButton = (TextView) ViewUtils.getValidView(rootView, R.id.event_rename_button, TextView.class);
        this.mDeleteButton = (TextView) ViewUtils.getValidView(rootView, R.id.event_delete_button, TextView.class);
        this.mProgressView = rootView.findViewById(R.id.progress_container);
        this.mScrollView = (TrackableScrollView) ViewUtils.getValidView(rootView, R.id.event_summary_scroll, TrackableScrollView.class);
        this.mInsightWidget = (InsightsWidget) ViewUtils.getValidView(rootView, R.id.insight_widget, InsightsWidget.class);
        processPendingChanges();
        this.mRenameStateButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BaseEventSummaryFragment.this.renameEvent();
            }
        });
        this.mDeleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BaseEventSummaryFragment.this.onDeleteEventPressed();
            }
        });
        this.mRenameButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (CommonUtils.isNetworkAvailable(BaseEventSummaryFragment.this.getActivity())) {
                    BaseEventSummaryFragment.this.onRenameEventPressed();
                } else {
                    BaseEventSummaryFragment.this.getDialogManager().showNetworkErrorDialog(BaseEventSummaryFragment.this.getActivity());
                }
            }
        });
        this.mShareButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (CommonUtils.isNetworkAvailable(BaseEventSummaryFragment.this.getActivity())) {
                    BaseEventSummaryFragment.this.onShareEventPressed();
                } else {
                    BaseEventSummaryFragment.this.getDialogManager().showNetworkErrorDialog(BaseEventSummaryFragment.this.getActivity());
                }
            }
        });
        View childView = onCreateEventView(inflater, this.mEventContainer, savedInstanceState);
        if (childView != null) {
            this.mEventContainer.addView(childView);
        } else {
            this.mEventContainer.setVisibility(8);
        }
        setHistoryView();
        this.mProgressView.setOnClickListener(null);
        return rootView;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int actionCode, Intent data) {
        super.onActivityResult(requestCode, actionCode, data);
        if (data != null && requestCode == 109 && actionCode == 0) {
            boolean isUserEventUpdate = data.getBooleanExtra(Constants.INTENT_USER_EVENT_UPDATE, false);
            if (isUserEventUpdate) {
                this.mEventHistoryFragment.downloadHistoryData();
            }
        }
    }

    private void setHistoryView() {
        if (!this.mIsL2View) {
            if (hasHistoryView()) {
                int filterType = getFilterType();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                this.mEventHistoryFragment = EventHistorySummaryFragment.newInstance(filterType, this.mUserEventId, this);
                ft.replace(R.id.event_history_summary_container, this.mEventHistoryFragment);
                ft.commit();
            } else {
                this.mHistoryContainer.setVisibility(8);
            }
            if (this.mEventHistoryFragment != null) {
                this.mScrollView.setOnHitBottomListener(new TrackableScrollView.OnHitBottomListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.5
                    @Override // com.microsoft.kapp.views.TrackableScrollView.OnHitBottomListener
                    public void onHitBottom() {
                        BaseEventSummaryFragment.this.mEventHistoryFragment.fetchMoreEvents();
                    }
                });
            }
        }
    }

    protected boolean hasHistoryView() {
        return false;
    }

    protected int getFilterType() {
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mNetworkChangedReceiver = new NetworkConnectivityChangedReceiver(this);
        this.mNetworkChangedReceiver.register(getActivity());
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mNetworkChangedReceiver.unregister(getActivity());
    }

    private void processPendingChanges() {
        this.mHasReferenceToViews = true;
        enableEventOption(this.mEventsOptionPending);
        disableEventOption(this.mEventsOptionPending ^ (-1));
        this.mCurrentEventsOption = this.mEventsOptionPending;
    }

    public void setCurrentEventName(String currentEventName) {
        if (isEventOptionEnabled(4)) {
            this.mRenameButton.setText(TextUtils.isEmpty(currentEventName) ? R.string.name_event : R.string.rename_event);
            this.mRenameEditText.addTextChangedListener(new TextWatcher() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.6
                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editTextContent) {
                    BaseEventSummaryFragment.this.mRenameStateButton.setGlyph(R.string.glyph_arrow_up_circle);
                }
            });
            this.mRenameEditText.setText(currentEventName);
        }
    }

    public void disableEventOption(int eventOption) {
        if ((eventOption & 1) == 1) {
            eventOption = -1;
        }
        if (!this.mHasReferenceToViews) {
            this.mEventsOptionPending ^= eventOption;
            return;
        }
        this.mCurrentEventsOption ^= eventOption;
        changeVisibilityOfOptions(eventOption, 8);
    }

    public void enableEventOption(int eventOption) {
        if (eventOption == 1) {
            eventOption = -1;
        }
        if (!this.mHasReferenceToViews) {
            this.mEventsOptionPending |= eventOption;
            return;
        }
        this.mCurrentEventsOption |= eventOption;
        changeVisibilityOfOptions(eventOption, 0);
    }

    private boolean isEventOptionEnabled(int eventOption) {
        return (this.mCurrentEventsOption & eventOption) != 0;
    }

    private void changeVisibilityOfOptions(int eventOption, int visibility) {
        if ((eventOption & 2) != 0) {
            this.mShareButton.setVisibility(visibility);
        }
        if ((eventOption & 4) != 0) {
            this.mRenameButton.setVisibility(visibility);
        }
        if ((eventOption & 8) != 0) {
            this.mDeleteButton.setVisibility(visibility);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameEvent() {
        this.mTempEventName = this.mRenameEditText.getText().toString();
        ViewUtils.closeSoftKeyboard(getActivity(), this.mRenameEditText);
        this.mProgressView.setVisibility(0);
        RestQueryTaskBaseV1 task = new EventRenameTaskV1(getUserEvent().getEventId(), this.mTempEventName, this.mRestService, this, this);
        task.execute();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRenameEventPressed() {
        int top = this.mRenameContainer.getTop();
        this.mScrollView.scrollTo(0, top);
        this.mRenameContainer.setVisibility(0);
        ViewUtils.openSoftKeyboard(this.mRenameEditText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeleteEventPressed() {
        DialogInterface.OnClickListener deleteEventListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseEventSummaryFragment.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                BaseEventSummaryFragment.this.mProgressView.setVisibility(0);
                RestQueryTaskBaseV1 task = new EventDeleteTaskV1(BaseEventSummaryFragment.this.getUserEvent().getEventId(), BaseEventSummaryFragment.this.mRestService, BaseEventSummaryFragment.this, BaseEventSummaryFragment.this);
                task.execute();
                String telemetryString = BaseEventSummaryFragment.this.getDeleteTelemetryForEvent(BaseEventSummaryFragment.this.getUserEvent());
                if (BaseEventSummaryFragment.this.isAdded() && telemetryString != null) {
                    Telemetry.logEvent(telemetryString);
                }
            }
        };
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.delete_event_confirmation_message), (Integer) null, deleteEventListener, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onShareEventPressed() {
        this.mProgressView.setVisibility(0);
        onShareRequested();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void shareEventWithMap(String chooserTitle, ExerciseEventBase event, String shareTitle, String shareTag, View bingMapView) {
        ShareObject.Builder builder = new ShareObject.Builder();
        String eventStartTime = KAppDateFormatter.formatToMonthDay(event.getStartTime());
        String title = String.format(Locale.getDefault(), shareTitle, eventStartTime, event.getMainMetric(getActivity(), this.mSettings.isDistanceHeightMetric()).toString());
        builder.setChooserTitle(chooserTitle);
        builder.setTitle(title);
        DateTimeFormatter fileNamePosfixFormat = DateTimeFormat.forPattern(getString(R.string.time_format_temp_files));
        String fileName = shareTag + "_map_" + fileNamePosfixFormat.print(event.getStartTime()) + ".jpg";
        Bitmap bitmap = Bitmap.createBitmap(bingMapView.getWidth(), bingMapView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bingMapView.draw(canvas);
        Bitmap mapBitmap = CommonUtils.drawTextOnBitmap(bitmap, title);
        try {
            File tmpImage = FileUtils.saveBitmapToDisk(mapBitmap, fileName);
            builder.addImage(Uri.fromFile(tmpImage));
        } catch (IOException e) {
            KLog.e(this.TAG, "The Bitmap failed to be saved to disk");
        }
        shareEvent(builder.build(getActivity()), event.getEventType().name());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void shareEventWithoutMap(ExerciseEventBase event, String chooserTitle, String shareTitle, String distance, String distanceUnits, String typeTag, String titleTag) {
        String eventStartTime = KAppDateFormatter.formatToMonthDay(event.getStartTime());
        String title = String.format(Locale.getDefault(), shareTitle, eventStartTime, event.getMainMetric(getActivity(), this.mSettings.isDistanceHeightMetric()).toString());
        ShareObject.Builder builder = new ShareObject.Builder();
        OpenGraphObject course = OpenGraphObject.Factory.createForPost("fitness.course");
        course.setProperty("og:title", "course");
        if (distance != null) {
            course.setProperty("fitness:distance:value", distance);
        }
        if (distanceUnits != null) {
            course.setProperty("fitness:distance:units", distanceUnits);
        }
        OpenGraphAction action = OpenGraphAction.Factory.createForPost("fitness." + typeTag);
        action.setProperty("og:title", titleTag);
        action.setProperty("course", course);
        builder.setChooserTitle(chooserTitle);
        builder.setTitle(title);
        builder.setOpenGraph(action, "course");
        shareEvent(builder.build(getActivity()), event.getEventType().name());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void shareEvent(ShareObject shareObject) {
        shareEvent(shareObject, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void shareEvent(ShareObject shareObject, String eventType) {
        if (shareObject != null) {
            shareObject.share(getActivity());
        }
        if (eventType != null) {
            HashMap<String, String> properties = new HashMap<>();
            properties.put(TelemetryConstants.Events.Share.Dimensions.EVENT_TYPE, eventType);
            Telemetry.logEvent(TelemetryConstants.Events.Share.EVENT_NAME, properties, null);
        }
        this.mProgressView.setVisibility(8);
    }

    @Override // com.microsoft.kapp.tasks.EventDeleteTaskV1.OnEventDeleteTaskListener
    public void onEventDeleted() {
        HomeData homeData = HomeData.getInstance();
        switch (getEventType()) {
            case 0:
                homeData.setRunEvent(null);
                break;
            case 1:
                homeData.setExerciseEvent(null);
                break;
            case 2:
                homeData.setSleepEvent(null);
                break;
            case 3:
                homeData.deleteLastGuidedWorkoutEventLocaly();
                break;
            case 4:
                homeData.setBikeEvent(null);
                break;
            case 5:
                homeData.setGolfEvent(null);
                break;
            default:
                Log.e(this.TAG, "Unkown event type");
                break;
        }
        homeData.fetchAsync();
        Activity activity = getActivity();
        if (activity instanceof OnEventModifiedListener) {
            ((OnEventModifiedListener) activity).onEventDeleted(getUserEvent().getEventType(), getUserEvent().getEventId());
        }
        ActivityUtils.performBackButton(activity);
    }

    @Override // com.microsoft.kapp.tasks.EventRenameTaskV1.OnEventRenameTaskListener
    public void onEventRenamed() {
        this.mProgressView.setVisibility(8);
        this.mRenameContainer.setVisibility(8);
        HomeData homeData = HomeData.getInstance();
        UserEvent event = null;
        switch (getEventType()) {
            case 0:
                event = homeData.getRunEvent();
                break;
            case 1:
                event = homeData.getExerciseEvent();
                break;
            case 2:
                event = homeData.getSleepEvent();
                break;
            case 3:
                event = homeData.getGuidedWorkoutevent();
                break;
            case 4:
                event = homeData.getBikeEvent();
                break;
            case 5:
                event = homeData.getGolfEvent();
                break;
            default:
                Log.e(this.TAG, "Unkown event type");
                break;
        }
        if (event != null && TextUtils.equals(event.getEventId(), getUserEvent().getEventId())) {
            event.setName(this.mTempEventName);
        }
        getUserEvent().setName(this.mTempEventName);
        this.mRenameButton.setText(TextUtils.isEmpty(this.mTempEventName) ? R.string.name_event : R.string.rename_event);
        Activity activity = getActivity();
        if (activity instanceof OnEventModifiedListener) {
            ((OnEventModifiedListener) activity).onEventRenamed(getUserEvent().getEventType(), this.mTempEventName, getUserEvent().getEventId());
        }
    }

    @Override // com.microsoft.kapp.fragments.EventHistorySummaryFragment.NotificationsReceiver
    public void OnItemsLoaded(EventData eventData) {
        if (this.mEventHistoryFragment == null) {
        }
    }

    @Override // com.microsoft.kapp.fragments.EventHistorySummaryFragment.NotificationsReceiver
    public void OnLoadError() {
        setState(1235);
    }

    @Override // com.microsoft.kapp.tasks.OnTaskStateChangedListenerV1
    public void onTaskFailed(int task, Exception ex) {
        this.mProgressView.setVisibility(8);
        getDialogManager().showNetworkErrorDialog(getActivity());
    }

    protected void onShareRequested() {
        shareEvent(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getUserAge(SettingsProvider settingsProvider) throws NullPointerException {
        return ProfileUtils.getUserAge(settingsProvider);
    }

    @Override // com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkDisconnected() {
        this.mRenameContainer.setVisibility(8);
    }

    @Override // com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkConnected() {
    }

    protected void navigateToUserEvent(UserEventSummary userEventSummary) {
        Intent intent = new Intent(getActivity(), UserEventDetailsActivity.class);
        intent.putExtra("eventId", userEventSummary.getEventId());
        intent.putExtra(Constants.KEY_EVENT_TYPE_ID, HistoryUtils.getIdForType(userEventSummary.getUserEvent()));
        intent.putStringArrayListExtra(Constants.KEY_PERSONALBEST, userEventSummary.getPersonalBests());
        startActivityForResult(intent, Constants.USER_EVENT_DETAILS_UPDATE_REQUEST);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void loadInsight() {
        List<RaisedInsight> raisedInsights;
        RaisedInsight insight;
        if (this.mSettings.isRaisedInsightsEnabled() && (raisedInsights = getInsights()) != null && !raisedInsights.isEmpty() && (insight = getInsight(raisedInsights)) != null) {
            showInsight(insight);
        }
    }

    protected List<RaisedInsight> getInsights() {
        return null;
    }

    protected RaisedInsight getInsight(List<RaisedInsight> raisedInsights) {
        Validate.notNull(raisedInsights, "raisedInsights");
        RaisedInsight insight = InsightsDisplayFilter.getRaisedInsight(raisedInsights, BasicRaisedInsightFilters.TONE_CAUTION, BasicRaisedInsightFilters.COMPARISION_SELF, BasicRaisedInsightFilters.COMPARISION_PEOPLE_LIKE_YOU);
        return insight;
    }

    protected void showInsight(RaisedInsight insight) {
        if (insight == null) {
            this.mInsightWidget.setVisibility(8);
            return;
        }
        String insightMessage = insight.getIMMsg();
        String insightActionMessage = insight.getIMActionMsg();
        if (!TextUtils.isEmpty(insightMessage)) {
            this.mInsightWidget.setMessage(insightMessage);
            if (!TextUtils.isEmpty(insightActionMessage)) {
                this.mInsightWidget.setActionMessage(insightActionMessage);
            }
            this.mInsightWidget.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.EVENT_L2_VIEW, this.mIsL2View);
        outState.putString(USER_EVENT_ID, this.mUserEventId);
    }

    public String getDeleteTelemetryForEvent(UserEvent event) {
        if (event.getEventType().equals(EventType.Sleeping)) {
            boolean isAutoDetected = false;
            if (event instanceof SleepEvent) {
                isAutoDetected = ((SleepEvent) event).getIsAutoSleep();
            }
            if (isAutoDetected) {
                return TelemetryConstants.Events.SleepEvent.AutoDetectDelete.FITNESS_SLEEP_AUTO_DETECT_DELETE;
            }
            return null;
        }
        return null;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
    public void onSnapshotDone(Bitmap bitmap) {
        Activity activity = getActivity();
        if (!Validate.isActivityAlive(activity)) {
            shareEvent(null);
        }
        ShareObject.Builder builder = new ShareObject.Builder();
        builder.setChooserTitle(getString(R.string.chooser_share_exercise));
        builder.setTitle(getShareTitle());
        if (bitmap != null) {
            Bitmap bitmap2 = CommonUtils.drawTextOnBitmap(bitmap, getShareTitle());
            DateTimeFormat.forPattern(getString(R.string.time_format_temp_files));
            try {
                File tmpChart = FileUtils.saveBitmapToDisk(bitmap2, "exercise_hr_chart_" + UUID.randomUUID().toString() + ".jpg");
                builder.addImage(Uri.fromFile(tmpChart));
            } catch (IOException e) {
                Log.e(this.TAG, "The Bitmap failed to be saved to disk");
            }
        }
        shareEvent(builder.build(getActivity()));
    }

    protected String getShareTitle() {
        return "";
    }
}

package com.facebook.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.android.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.model.GraphUser;
import com.facebook.widget.PickerFragment;
import com.google.android.gms.appstate.AppStateClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class FriendPickerFragment extends PickerFragment<GraphUser> {
    public static final String FRIEND_PICKER_TYPE_KEY = "com.facebook.widget.FriendPickerFragment.FriendPickerType";
    private static final String ID = "id";
    public static final String MULTI_SELECT_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.MultiSelect";
    private static final String NAME = "name";
    public static final String USER_ID_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.UserId";
    private FriendPickerType friendPickerType;
    private boolean multiSelect;
    private List<String> preSelectedFriendIds;
    private String userId;

    /* loaded from: classes.dex */
    public enum FriendPickerType {
        FRIENDS("/friends", true),
        TAGGABLE_FRIENDS("/taggable_friends", false),
        INVITABLE_FRIENDS("/invitable_friends", false);
        
        private final boolean requestIsCacheable;
        private final String requestPath;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FriendPickerType[] valuesCustom() {
            FriendPickerType[] valuesCustom = values();
            int length = valuesCustom.length;
            FriendPickerType[] friendPickerTypeArr = new FriendPickerType[length];
            System.arraycopy(valuesCustom, 0, friendPickerTypeArr, 0, length);
            return friendPickerTypeArr;
        }

        FriendPickerType(String path, boolean cacheable) {
            this.requestPath = path;
            this.requestIsCacheable = cacheable;
        }

        String getRequestPath() {
            return this.requestPath;
        }

        boolean isCacheable() {
            return this.requestIsCacheable;
        }
    }

    public FriendPickerFragment() {
        this(null);
    }

    @SuppressLint({"ValidFragment"})
    public FriendPickerFragment(Bundle args) {
        super(GraphUser.class, R.layout.com_facebook_friendpickerfragment, args);
        this.multiSelect = true;
        this.friendPickerType = FriendPickerType.FRIENDS;
        this.preSelectedFriendIds = new ArrayList();
        setFriendPickerSettingsFromBundle(args);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getMultiSelect() {
        return this.multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        if (this.multiSelect != multiSelect) {
            this.multiSelect = multiSelect;
            setSelectionStrategy(createSelectionStrategy());
        }
    }

    public void setFriendPickerType(FriendPickerType type) {
        this.friendPickerType = type;
    }

    public void setSelectionByIds(List<String> userIds) {
        this.preSelectedFriendIds.addAll(userIds);
    }

    public void setSelectionByIds(String... userIds) {
        setSelectionByIds(Arrays.asList(userIds));
    }

    public void setSelection(GraphUser... graphUsers) {
        setSelection(Arrays.asList(graphUsers));
    }

    public void setSelection(List<GraphUser> graphUsers) {
        List<String> userIds = new ArrayList<>();
        for (GraphUser graphUser : graphUsers) {
            userIds.add(graphUser.getId());
        }
        setSelectionByIds(userIds);
    }

    public List<GraphUser> getSelection() {
        return getSelectedGraphObjects();
    }

    @Override // com.facebook.widget.PickerFragment, android.support.v4.app.Fragment
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.com_facebook_friend_picker_fragment);
        setMultiSelect(a.getBoolean(0, this.multiSelect));
        a.recycle();
    }

    @Override // com.facebook.widget.PickerFragment
    public void setSettingsFromBundle(Bundle inState) {
        super.setSettingsFromBundle(inState);
        setFriendPickerSettingsFromBundle(inState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.facebook.widget.PickerFragment
    public void saveSettingsToBundle(Bundle outState) {
        super.saveSettingsToBundle(outState);
        outState.putString(USER_ID_BUNDLE_KEY, this.userId);
        outState.putBoolean(MULTI_SELECT_BUNDLE_KEY, this.multiSelect);
    }

    @Override // com.facebook.widget.PickerFragment
    PickerFragment<GraphUser>.PickerFragmentAdapter<GraphUser> createAdapter() {
        PickerFragment<GraphUser>.PickerFragmentAdapter<GraphUser> adapter = new PickerFragment<GraphUser>.PickerFragmentAdapter<GraphUser>(this, getActivity()) { // from class: com.facebook.widget.FriendPickerFragment.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.facebook.widget.GraphObjectAdapter
            public int getGraphObjectRowLayoutId(GraphUser graphObject) {
                return R.layout.com_facebook_picker_list_row;
            }

            @Override // com.facebook.widget.GraphObjectAdapter
            protected int getDefaultPicture() {
                return R.drawable.com_facebook_profile_default_icon;
            }
        };
        adapter.setShowCheckbox(true);
        adapter.setShowPicture(getShowPictures());
        adapter.setSortFields(Arrays.asList("name"));
        adapter.setGroupByField("name");
        return adapter;
    }

    @Override // com.facebook.widget.PickerFragment
    PickerFragment<GraphUser>.LoadingStrategy createLoadingStrategy() {
        return new ImmediateLoadingStrategy(this, null);
    }

    @Override // com.facebook.widget.PickerFragment
    PickerFragment<GraphUser>.SelectionStrategy createSelectionStrategy() {
        return this.multiSelect ? new PickerFragment.MultiSelectionStrategy() : new PickerFragment.SingleSelectionStrategy();
    }

    @Override // com.facebook.widget.PickerFragment
    Request getRequestForLoadData(Session session) {
        if (this.adapter == null) {
            throw new FacebookException("Can't issue requests until Fragment has been created.");
        }
        String userToFetch = this.userId != null ? this.userId : "me";
        return createRequest(userToFetch, this.extraFields, session);
    }

    @Override // com.facebook.widget.PickerFragment
    String getDefaultTitleText() {
        return getString(R.string.com_facebook_choose_friends);
    }

    @Override // com.facebook.widget.PickerFragment
    void logAppEvents(boolean doneButtonClicked) {
        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity(), getSession());
        Bundle parameters = new Bundle();
        String outcome = doneButtonClicked ? AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_COMPLETED : "Unknown";
        parameters.putString(AnalyticsEvents.PARAMETER_DIALOG_OUTCOME, outcome);
        parameters.putInt("num_friends_picked", getSelection().size());
        logger.logSdkEvent(AnalyticsEvents.EVENT_FRIEND_PICKER_USAGE, null, parameters);
    }

    @Override // com.facebook.widget.PickerFragment
    public void loadData(boolean forceReload) {
        super.loadData(forceReload);
        setSelectedGraphObjects(this.preSelectedFriendIds);
    }

    private Request createRequest(String userID, Set<String> extraFields, Session session) {
        Request request = Request.newGraphPathRequest(session, String.valueOf(userID) + this.friendPickerType.getRequestPath(), null);
        Set<String> fields = new HashSet<>(extraFields);
        String[] requiredFields = {"id", "name"};
        fields.addAll(Arrays.asList(requiredFields));
        String pictureField = this.adapter.getPictureFieldSpecifier();
        if (pictureField != null) {
            fields.add(pictureField);
        }
        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);
        return request;
    }

    private void setFriendPickerSettingsFromBundle(Bundle inState) {
        if (inState != null) {
            if (inState.containsKey(USER_ID_BUNDLE_KEY)) {
                setUserId(inState.getString(USER_ID_BUNDLE_KEY));
            }
            setMultiSelect(inState.getBoolean(MULTI_SELECT_BUNDLE_KEY, this.multiSelect));
            if (inState.containsKey(FRIEND_PICKER_TYPE_KEY)) {
                try {
                    this.friendPickerType = FriendPickerType.valueOf(inState.getString(FRIEND_PICKER_TYPE_KEY));
                } catch (Exception e) {
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private class ImmediateLoadingStrategy extends PickerFragment<GraphUser>.LoadingStrategy {
        private ImmediateLoadingStrategy() {
            super();
        }

        /* synthetic */ ImmediateLoadingStrategy(FriendPickerFragment friendPickerFragment, ImmediateLoadingStrategy immediateLoadingStrategy) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.facebook.widget.PickerFragment.LoadingStrategy
        public void onLoadFinished(GraphObjectPagingLoader<GraphUser> loader, SimpleGraphObjectCursor<GraphUser> data) {
            super.onLoadFinished(loader, data);
            if (data != null && !loader.isLoading()) {
                if (data.areMoreObjectsAvailable()) {
                    followNextLink();
                    return;
                }
                FriendPickerFragment.this.hideActivityCircle();
                if (data.isFromCache()) {
                    loader.refreshOriginalRequest(data.getCount() == 0 ? AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION : 0);
                }
            }
        }

        @Override // com.facebook.widget.PickerFragment.LoadingStrategy
        protected boolean canSkipRoundTripIfCached() {
            return FriendPickerFragment.this.friendPickerType.isCacheable();
        }

        private void followNextLink() {
            FriendPickerFragment.this.displayActivityCircle();
            this.loader.followNextLink();
        }
    }
}

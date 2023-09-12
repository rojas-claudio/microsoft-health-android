package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.exceptions.LocationDisabledException;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.services.LocationCallbacks;
import com.microsoft.kapp.services.LocationService;
import com.microsoft.kapp.services.golf.CourseFilters;
import java.util.concurrent.Semaphore;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfNearbySearchResultsFragment extends GolfSearchResultsFragment implements LocationCallbacks {
    private static final String TAG = GolfNearbySearchResultsFragment.class.getSimpleName();
    private Callback<GolfSearchResultsModel> mCallback;
    @Inject
    Context mContext;
    private CourseFilters mCourseFilters;
    private Location mLocation;
    @Inject
    LocationService mLocationService;
    private int mPage;
    private boolean mHasMoreData = true;
    private Semaphore mLocationLock = new Semaphore(1);

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    protected void getData(int page, CourseFilters coursefilters, Callback<GolfSearchResultsModel> callback) {
        if (this.mLocation != null) {
            getCourseData(page, this.mLocation.getLatitude(), this.mLocation.getLongitude(), coursefilters, callback);
            return;
        }
        try {
            if (this.mLocationService.isLocationServiceAvailable(getActivity())) {
                if (this.mLocationLock.tryAcquire()) {
                    this.mPage = page;
                    this.mCourseFilters = coursefilters;
                    this.mCallback = callback;
                    this.mLocationService.getCurrentLocation(this.mContext, this);
                } else {
                    callback.onError(new IllegalStateException());
                }
            }
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception updating weather.", exception);
        }
    }

    private void getCourseData(int page, double latitude, double longitude, CourseFilters coursefilters, final Callback<GolfSearchResultsModel> callback) {
        this.mGolfService.getNearbyCourses(latitude, longitude, coursefilters, page, 10, new ActivityScopedCallback(getActivity(), new Callback<GolfSearchResultsModel>() { // from class: com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfSearchResultsModel result) {
                if (result != null && result.getItems() != null && result.getItems().size() < 10) {
                    GolfNearbySearchResultsFragment.this.mHasMoreData = false;
                }
                callback.callback(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Fitness/Golf/FindCourse");
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean isFilterEnabled() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean hasMoreData() {
        return this.mHasMoreData;
    }

    @Override // com.microsoft.kapp.services.LocationCallbacks
    public void onLocationFound(Location location) {
        this.mLocation = location;
        this.mLocationLock.release();
        getCourseData(this.mPage, this.mLocation.getLatitude(), this.mLocation.getLongitude(), this.mCourseFilters, this.mCallback);
    }

    @Override // com.microsoft.kapp.services.LocationCallbacks
    public void onError() {
        this.mLocationLock.release();
        showEnableLocationDialog();
    }

    private void showEnableLocationDialog() {
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                GolfNearbySearchResultsFragment.this.startActivity(intent);
                GolfNearbySearchResultsFragment.this.mCallback.callback(new GolfSearchResultsModel());
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                GolfNearbySearchResultsFragment.this.mCallback.onError(new LocationDisabledException("location is disabled"));
            }
        };
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.location_disabled_on_phone_header), Integer.valueOf((int) R.string.location_disabled_on_phone_message), R.string.ok, okListener, R.string.cancel, cancelListener, DialogPriority.HIGH);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public boolean shouldshowResultCount() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public void clearData() {
        this.mHasMoreData = true;
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment
    public String getReferralName() {
        return TelemetryConstants.PageViews.Referrers.GOLF_SEARCH_NEARBY;
    }
}

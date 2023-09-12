package com.microsoft.kapp.models.golf;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.Pair;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StringUtils;
import com.microsoft.kapp.views.SingleTrackerStat;
import com.microsoft.krestsdk.models.GolfCourse;
import com.microsoft.krestsdk.models.GolfCourseType;
import com.microsoft.krestsdk.models.GolfTee;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CourseDetails {
    private Spannable mAddress;
    private Context mContext;
    private String mCourseId;
    private String mCourseName;
    private SingleTrackerStat mCourseRatingStat;
    private SingleTrackerStat mCourseTypeStat;
    private GolfCourse mGolfCourse;
    private boolean mHasCourseBeenSynced;
    private boolean mHasTees;
    private String mPhoneNumber;
    @Inject
    SettingsProvider mSettingsProvider;
    private SingleTrackerStat mSlopeRatingStat;
    private String mSyncTeeText;
    private SingleTrackerStat mTotalNumberOfHolesStat;
    private Spannable mWebsite;

    public CourseDetails() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    public CourseDetails(GolfCourse golfCourse, Context context) {
        this();
        Validate.notNull(golfCourse, "golfCourseDetail");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mGolfCourse = golfCourse;
        this.mContext = context;
        this.mCourseName = this.mGolfCourse.getName();
        populateStats();
        populateWebsite();
        this.mPhoneNumber = golfCourse.getPhoneNumber();
        populateAddress();
        Pair<String, String> syncedGolfCourse = this.mSettingsProvider.getSyncedGolfCourse();
        if (!TextUtils.isEmpty((CharSequence) syncedGolfCourse.first) && !TextUtils.isEmpty((CharSequence) syncedGolfCourse.first) && this.mCourseId.equalsIgnoreCase((String) syncedGolfCourse.first)) {
            setHasCourseBeenSynced(true);
        }
        populateSyncTee();
        GolfTee[] tees = golfCourse.getTees();
        this.mHasTees = tees != null && tees.length > 0;
    }

    public SingleTrackerStat getTotalNumberOfHolesStat() {
        return this.mTotalNumberOfHolesStat;
    }

    public SingleTrackerStat getCourseTypeStat() {
        return this.mCourseTypeStat;
    }

    public SingleTrackerStat getCourseRatingStat() {
        return this.mCourseRatingStat;
    }

    public SingleTrackerStat getSlopeRatingStat() {
        return this.mSlopeRatingStat;
    }

    public Spannable getWebsite() {
        return this.mWebsite;
    }

    public String getPhoneNumber() {
        return this.mPhoneNumber;
    }

    public Spannable getAddress() {
        return this.mAddress;
    }

    public boolean hasCourseBeenSynced() {
        return this.mHasCourseBeenSynced;
    }

    public void setHasCourseBeenSynced(boolean hasCourseBeenSynced) {
        this.mHasCourseBeenSynced = hasCourseBeenSynced;
        populateSyncTee();
    }

    public String getCourseName() {
        return this.mCourseName;
    }

    public String getSyncTeeText() {
        return this.mSyncTeeText;
    }

    public ArrayList<GolfTeePair> getUniqueTees() {
        GolfTee[] tees = this.mGolfCourse.getTees();
        HashSet<String> uniqueTees = new HashSet<>();
        ArrayList<GolfTeePair> teePairs = new ArrayList<>();
        if (tees.length != 0) {
            String defaultTeeId = String.valueOf(tees[0].getId());
            for (GolfTee tee : tees) {
                String teeId = String.valueOf(tee.getId());
                if (tee.isDefault()) {
                    defaultTeeId = teeId;
                }
                if (uniqueTees.add(teeId)) {
                    GolfTeePair pair = new GolfTeePair(tee.getName(), teeId);
                    teePairs.add(pair);
                }
            }
            teePairs.add(new GolfTeePair(this.mContext.getString(R.string.golf_tee_pick_default), defaultTeeId));
        }
        return teePairs;
    }

    public boolean hasTees() {
        return this.mHasTees;
    }

    private void populateStats() {
        String courseType;
        this.mCourseId = this.mGolfCourse.getCourseId();
        int numberOfHoles = this.mGolfCourse.getNumberOfHoles();
        GolfCourseType mGolfCourseType = this.mGolfCourse.getCourseType();
        if (mGolfCourseType == null || mGolfCourseType.compareTo(GolfCourseType.UNKNOWN) == 0) {
            courseType = this.mContext.getString(R.string.no_value_locked);
        } else {
            courseType = GolfCourseType.getDisplayValue(this.mContext, mGolfCourseType);
        }
        String courseRating = StringUtils.ifDefaultThenEmpty(Math.floor(this.mGolfCourse.getDifficultTeeRating() * 10.0d) / 10.0d, 0);
        String slopeRating = StringUtils.ifDefaultThenEmpty((int) this.mGolfCourse.getDifficultSlope(), 0);
        this.mTotalNumberOfHolesStat = new SingleTrackerStat(this.mContext, this.mContext.getString(R.string.golf_holes_locked));
        this.mTotalNumberOfHolesStat.ensureValue(StringUtils.ifDefaultThenEmpty(numberOfHoles, 0), this.mContext);
        this.mCourseTypeStat = new SingleTrackerStat(this.mContext, this.mContext.getString(R.string.golf_course_type));
        this.mCourseTypeStat.ensureValue(courseType, this.mContext);
        this.mCourseRatingStat = new SingleTrackerStat(this.mContext, this.mContext.getString(R.string.golf_course_rating));
        this.mCourseRatingStat.ensureValue(courseRating, this.mContext);
        this.mSlopeRatingStat = new SingleTrackerStat(this.mContext, this.mContext.getString(R.string.golf_slope_rating));
        this.mSlopeRatingStat.ensureValue(slopeRating, this.mContext);
    }

    private void populateWebsite() {
        String website = this.mGolfCourse.getWebsite();
        if (!TextUtils.isEmpty(website)) {
            this.mWebsite = new SpannableString(this.mContext.getString(R.string.website));
            boolean doesWebsiteStartWithHtttp = website.matches("^(http|https)://.*");
            if (!doesWebsiteStartWithHtttp) {
                website = "http://" + website;
            }
            this.mWebsite.setSpan(new URLSpan(website), 0, this.mWebsite.length(), 33);
        }
    }

    private void populateAddress() {
        String concatenatedAddress = this.mGolfCourse.getConcatenatedAddress();
        if (!TextUtils.isEmpty(concatenatedAddress)) {
            this.mAddress = new SpannableString(concatenatedAddress);
            double lat = this.mGolfCourse.getLatitude();
            double lon = this.mGolfCourse.getLongitude();
            String location = "geo:0,0?q=" + lat + "," + lon;
            this.mAddress.setSpan(new URLSpan(location), 0, concatenatedAddress.length(), 33);
        }
    }

    private void populateSyncTee() {
        if (hasCourseBeenSynced()) {
            Pair<String, String> syncedGolfCourse = this.mSettingsProvider.getSyncedGolfCourse();
            String teedId = (String) syncedGolfCourse.second;
            ArrayList<GolfTeePair> teePair = getUniqueTees();
            Iterator i$ = teePair.iterator();
            while (i$.hasNext()) {
                GolfTeePair tee = i$.next();
                if (tee.getTeeId().equalsIgnoreCase(teedId)) {
                    this.mSyncTeeText = tee.getTeeName();
                    return;
                }
            }
        }
    }
}

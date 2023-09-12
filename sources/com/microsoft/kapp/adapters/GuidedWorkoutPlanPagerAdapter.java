package com.microsoft.kapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment;
import com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1;
import com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutPlanPagerAdapter extends FragmentStatePagerAdapter implements TitlePagerAdapter {
    private static final int COUNT = 2;
    private Context mContext;
    private boolean mIsHomeTileMode;
    private boolean mIsPlanSubscriptionDisabled;
    private boolean mIsSingleWorkout;
    private List<ScheduledWorkout> mPlanSchedule;
    private WorkoutPlan mWorkoutPlan;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GuidedWorkoutPlanPagerAdapter(FragmentManager fragmentManager, Context context, WorkoutPlan workoutPlan, List<ScheduledWorkout> planSchedule, boolean isHomeTileMode, boolean forcePlanSubscriptionDisabled) {
        super(fragmentManager);
        boolean z = false;
        this.mContext = context;
        this.mWorkoutPlan = workoutPlan;
        this.mPlanSchedule = planSchedule;
        this.mIsHomeTileMode = isHomeTileMode;
        this.mIsSingleWorkout = this.mPlanSchedule != null && this.mPlanSchedule.size() == 1;
        this.mIsPlanSubscriptionDisabled = (forcePlanSubscriptionDisabled || isPlanSubscriptionDisabled(planSchedule)) ? true : z;
    }

    @Override // android.support.v4.app.FragmentStatePagerAdapter
    public Fragment getItem(int position) {
        if (this.mIsSingleWorkout) {
            ScheduledWorkout scheduledWorkout = this.mPlanSchedule.get(0);
            if (position == 0 && scheduledWorkout != null) {
                return WorkoutPlanSummaryFragmentV1.newInstance(this.mWorkoutPlan, scheduledWorkout, this.mIsHomeTileMode, this.mIsPlanSubscriptionDisabled);
            }
            if (position == 1 && scheduledWorkout != null) {
                return GuidedWorkoutNextFragment.newInstance(scheduledWorkout, false, false, false);
            }
        }
        if (position == 0) {
            return WorkoutPlanSummaryFragmentV1.newInstance(this.mWorkoutPlan, this.mIsHomeTileMode, this.mIsPlanSubscriptionDisabled);
        }
        return WorkoutPlanScheduleFragmentV1.newInstance(this.mWorkoutPlan, this.mPlanSchedule, this.mIsHomeTileMode, this.mIsPlanSubscriptionDisabled);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return 2;
    }

    @Override // android.support.v4.view.PagerAdapter
    public String getPageTitle(int position) {
        if (position == 0) {
            return this.mContext.getString(R.string.guided_workout_plan_overview);
        }
        if (this.mIsSingleWorkout) {
            return this.mContext.getString(R.string.guided_workout_details_title_locked);
        }
        return this.mContext.getString(R.string.guided_workout_plan_schedule);
    }

    private boolean isPlanSubscriptionDisabled(List<ScheduledWorkout> planSchedule) {
        if (planSchedule == null) {
            return false;
        }
        for (ScheduledWorkout currentScheduledWorkout : planSchedule) {
            if (currentScheduledWorkout != null && currentScheduledWorkout.getWeekId() == 1 && currentScheduledWorkout.getDay() == 1) {
                return false;
            }
        }
        return true;
    }
}

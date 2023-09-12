package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class GuidedWorkoutsBrowsePlanFragment extends GuidedWorkoutsBrowsePlanBaseFragment {
    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment
    protected int getLayoutResourceId() {
        return R.layout.guided_workout_browse_plan_activity;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment
    protected int getPagerTitleStripColor() {
        return R.color.PrimaryMediumColor;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment
    protected boolean getIsHomeTileMode() {
        return false;
    }
}

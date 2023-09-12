package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GuidedWorkoutHomeTile extends HomeTile {
    @Inject
    SettingsProvider mSettingsProvider;

    public GuidedWorkoutHomeTile(Context context) {
        this(context, null);
    }

    public GuidedWorkoutHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public GuidedWorkoutHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_guided_workout);
        Resources currentResourses = getResources();
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_guided_workout));
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_guided_workout));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_guided_workout));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_guided_workout));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_guided_workout));
        this.mOldContentThreshold = context.getResources().getInteger(R.integer.home_tile_run_bike_workout_old_content_in_days_threshold);
    }

    public void setData(GuidedWorkoutEvent event) {
        HomeTile.TileState tileState;
        boolean z = false;
        if (event == null) {
            setPersonalBestStatus(false);
            setTileData(HomeTile.TileState.VIEWED, null, getContext().getString(R.string.guided_workout_not_viewed_message), null);
            return;
        }
        DateTime workoutTime = event.getStartTime();
        String tileDate = workoutTime != null ? KAppDateFormatter.formatToMonthWithDay(getContext().getResources(), workoutTime) : "";
        if (this.mSettingsProvider.isSinceLastGuidedWorkoutClickedTime(workoutTime)) {
            tileState = HomeTile.TileState.NOT_VIEWED;
        } else {
            tileState = HomeTile.TileState.VIEWED;
        }
        if (event.getPersonalBests() != null && !event.getPersonalBests().isEmpty()) {
            z = true;
        }
        setPersonalBestStatus(z);
        setTileData(tileState, tileDate, event.getName(), event.getMainMetric(getContext(), R.array.MerticInvertedValueFormat, this.mSettingsProvider.isDistanceHeightMetric()));
    }

    public void setNewSinceLastVisit() {
        setState(HomeTile.TileState.VIEWED);
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_GW;
    }
}

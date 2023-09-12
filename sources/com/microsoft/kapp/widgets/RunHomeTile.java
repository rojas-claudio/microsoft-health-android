package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.krestsdk.models.RunEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class RunHomeTile extends HomeTile {
    @Inject
    SettingsProvider mSettingsProvider;

    public RunHomeTile(Context context) {
        this(context, null);
    }

    public RunHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public RunHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_run);
        Resources currentResourses = getResources();
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_run));
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_run));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_run));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_run));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_run));
        this.mOldContentThreshold = currentResourses.getInteger(R.integer.home_tile_run_bike_workout_old_content_in_days_threshold);
    }

    public void setData(RunEvent event) {
        HomeTile.TileState tileState;
        boolean z = false;
        if (event == null) {
            setPersonalBestStatus(false);
            setTileData(HomeTile.TileState.VIEWED, null, getContext().getString(R.string.home_tile_run_not_viewed_message), null);
            return;
        }
        DateTime runTime = event.getStartTime();
        String tileDate = KAppDateFormatter.formatToMonthWithDay(getContext().getResources(), runTime);
        if (this.mSettingsProvider.isSinceLastRunClickedTime(runTime)) {
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

    @Override // com.microsoft.kapp.widgets.BaseTile
    public boolean hasData() {
        return HomeData.getInstance().getRunEvent() != null;
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_RUN;
    }
}

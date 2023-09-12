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
import com.microsoft.krestsdk.models.SleepEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SleepHomeTile extends HomeTile {
    @Inject
    SettingsProvider mSettingsProvider;

    public SleepHomeTile(Context context) {
        this(context, null);
    }

    public SleepHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public SleepHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_sleep);
        Resources currentResourses = getResources();
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_sleep));
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_sleep));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_sleep));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_sleep));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_sleep));
        this.mOldContentThreshold = currentResourses.getInteger(R.integer.home_tile_sleep_old_content_in_days_threshold);
    }

    public void setData(SleepEvent event) {
        HomeTile.TileState tileState;
        if (event == null) {
            setTileData(HomeTile.TileState.VIEWED, null, getContext().getString(R.string.home_tile_sleep_not_viewed_message), null);
            return;
        }
        DateTime sleepTime = event.getStartTime();
        String tileDate = KAppDateFormatter.formatToMonthWithDay(getContext().getResources(), sleepTime.minusHours(5));
        if (this.mSettingsProvider.isSinceLastSleepClickedTime(sleepTime)) {
            tileState = HomeTile.TileState.NOT_VIEWED;
        } else {
            tileState = HomeTile.TileState.VIEWED;
        }
        setTileData(tileState, tileDate, getContext().getString(event.getIsAutoSleep() ? R.string.user_event_sleep_auto_detect_default_name : R.string.user_event_sleep_default_name), event.getMainMetric(getContext(), R.array.MerticInvertedValueFormat, this.mSettingsProvider.isDistanceHeightMetric()));
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public boolean hasData() {
        return HomeData.getInstance().getSleepEvent() != null;
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_SLEEP;
    }
}

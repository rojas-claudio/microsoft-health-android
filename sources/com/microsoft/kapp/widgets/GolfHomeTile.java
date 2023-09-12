package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.util.AttributeSet;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.krestsdk.models.GolfEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GolfHomeTile extends HomeTile {
    @Inject
    SettingsProvider mSettingsProvider;

    public GolfHomeTile(Context context) {
        this(context, null);
    }

    public GolfHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public GolfHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_golf);
        Resources currentResourses = getResources();
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_golf));
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_golf));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_golf));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_golf));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_golf));
        this.mOldContentThreshold = currentResourses.getInteger(R.integer.home_tile_run_bike_workout_old_content_in_days_threshold);
    }

    public void setData(GolfEvent event) {
        if (event == null) {
            setPersonalBestStatus(false);
        }
        HomeTileData golfTileData = getHomeTileData(event);
        Spannable formattedScore = null;
        if (event != null) {
            formattedScore = Formatter.formatGolfScore(getContext(), R.array.MerticInvertedValueFormat, golfTileData.getTileMetric(), golfTileData.getTileUnit());
        }
        setTileData(golfTileData.getTileState(), golfTileData.getTileDate(), golfTileData.getTileName(), formattedScore);
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public boolean hasData() {
        return HomeData.getInstance().getGolfEvent() != null;
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return "Golf";
    }

    public HomeTileData getHomeTileData(GolfEvent event) {
        HomeTile.TileState tileState;
        if (event == null) {
            HomeTileData data = new HomeTileData();
            data.setTileState(HomeTile.TileState.VIEWED);
            data.setTileName(getContext().getString(R.string.home_tile_golf_not_viewed_message));
            return data;
        }
        DateTime eventTime = event.getStartTime();
        String tileDate = KAppDateFormatter.formatToMonthWithDay(getContext().getResources(), eventTime);
        if (this.mSettingsProvider.isSinceLastGolfClickedTime(eventTime)) {
            tileState = HomeTile.TileState.NOT_VIEWED;
        } else {
            tileState = HomeTile.TileState.VIEWED;
        }
        int totalScore = event.getTotalScore();
        int parForHolesPlayed = event.getParForHolesPlayed();
        int difference = totalScore - parForHolesPlayed;
        HomeTileData data2 = new HomeTileData();
        data2.setTileDate(tileDate);
        data2.setTileState(tileState);
        data2.setTileMetric(totalScore);
        data2.setTileUnit(difference);
        data2.setTileName(event.getDisplayName());
        return data2;
    }
}

package com.microsoft.kapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappBarcode;
import com.microsoft.band.device.StrappData;
import com.microsoft.band.device.StrappIconbox;
import com.microsoft.band.device.StrappLayout;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.StrappTextbox;
import com.microsoft.band.tiles.pages.BarcodeType;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappDefinition;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.finance.Stock;
import com.microsoft.kapp.services.weather.WeatherDay;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;
import org.slf4j.Marker;
/* loaded from: classes.dex */
public class StrappUtils {
    public static final int NOT_USED = 0;
    private static final String TAG = StrappUtils.class.getSimpleName();
    public static Comparator<StrappState> StrappStateComparator = new Comparator<StrappState>() { // from class: com.microsoft.kapp.utils.StrappUtils.1
        @Override // java.util.Comparator
        public int compare(StrappState strapp1, StrappState strapp2) {
            return strapp1.getDefinition().getThirdPartyName().compareToIgnoreCase(strapp2.getDefinition().getThirdPartyName());
        }
    };

    public static StrappStateCollection createStrappStateCollection(Collection<StrappDefinition> strappDefinitions, boolean isEnabled) {
        Validate.notNull(strappDefinitions, "strappDefinitions");
        StrappStateCollection strapps = new StrappStateCollection();
        for (StrappDefinition definition : strappDefinitions) {
            StrappState strapp = new StrappState(definition);
            strapp.setIsEnabled(isEnabled);
            strapps.put(strapp);
        }
        return strapps;
    }

    public static StrappLayout readLayoutBlob(InputStream input) throws Exception {
        try {
            try {
                byte[] buffer = new byte[input.available()];
                input.read(buffer);
                StreamUtils.closeQuietly(input);
                StrappLayout strappLayout = new StrappLayout(buffer);
                return strappLayout;
            } catch (Exception e) {
                KLog.e(TAG, "Error while reading Strapp raw LayoutBlob file: %s", e);
                throw e;
            }
        } catch (Throwable th) {
            StreamUtils.closeQuietly(input);
            throw th;
        }
    }

    public static ArrayList<StrappPageElement> createLastUpdatedStrappElement(String finalText, Context context) {
        ArrayList<StrappPageElement> pageElements = new ArrayList<>();
        String nowFormatted = KAppDateFormatter.formatToMonthDayTime(DateTime.now());
        pageElements.add(new StrappTextbox(11, context.getString(R.string.last_updated)));
        pageElements.add(new StrappTextbox(21, nowFormatted));
        pageElements.add(new StrappTextbox(31, finalText));
        return pageElements;
    }

    public static String lookupBackGlyphForUuid(UUID uuid, Context context) {
        return DefaultStrappUUID.STRAPP_STARBUCKS.equals(uuid) ? context.getResources().getString(R.string.glyph_sync_circle) : "";
    }

    public static int lookupGlyhpBackgroundColorForUUID(UUID uuid, Context context) {
        if (DefaultStrappUUID.STRAPP_STARBUCKS.equals(uuid)) {
            return context.getResources().getColor(R.color.starbucks_background_color);
        }
        return 0;
    }

    public static String lookupGlyphForUuid(UUID uuid, Context context) {
        if (DefaultStrappUUID.STRAPP_CALLS.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_phone);
        }
        if (DefaultStrappUUID.STRAPP_MESSAGING.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_messaging);
        }
        if (DefaultStrappUUID.STRAPP_RUN.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_run);
        }
        if (DefaultStrappUUID.STRAPP_BIKE.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_bike);
        }
        if (DefaultStrappUUID.STRAPP_SLEEP.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_sleep);
        }
        if (DefaultStrappUUID.STRAPP_EXERCISE.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_workout);
        }
        if (DefaultStrappUUID.STRAPP_GOLF.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_golf);
        }
        if (DefaultStrappUUID.STRAPP_ALARM_STOPWATCH.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_alarm);
        }
        if (DefaultStrappUUID.STRAPP_CALENDAR.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_calendar);
        }
        if (DefaultStrappUUID.STRAPP_BING_WEATHER.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_bing_weather);
        }
        if (DefaultStrappUUID.STRAPP_BING_FINANCE.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_bing_finance);
        }
        if (DefaultStrappUUID.STRAPP_STARBUCKS.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_starbucks_reverse);
        }
        if (DefaultStrappUUID.STRAPP_EMAIL.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_email);
        }
        if (DefaultStrappUUID.STRAPP_FACEBOOK.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_facebook);
        }
        if (DefaultStrappUUID.STRAPP_TWITTER.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_twitter);
        }
        if (DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_guided_workout);
        }
        if (DefaultStrappUUID.STRAPP_UV.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_UV);
        }
        if (DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_FBMessenger);
        }
        if (DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER.equals(uuid)) {
            return context.getResources().getString(R.string.glyph_NotificationCenter);
        }
        return null;
    }

    private static ArrayList<StrappPageElement> createWeatherStrappElement(WeatherDay weatherDay, Context context) {
        ArrayList<StrappPageElement> pageElements = new ArrayList<>();
        if (weatherDay.isCurrentDay()) {
            pageElements.add(new StrappTextbox(22, String.valueOf(weatherDay.getCurrentTemperature()) + context.getResources().getString(R.string.weather_degree_symbol)));
            pageElements.add(new StrappTextbox(11, context.getResources().getString(R.string.weather_now_header_title)));
            pageElements.add(new StrappTextbox(12, context.getResources().getString(R.string.weather_now_header_divider)));
            pageElements.add(new StrappTextbox(13, String.valueOf(weatherDay.getConditionPlaintext())));
        } else {
            pageElements.add(new StrappTextbox(22, String.valueOf(weatherDay.getHigh()) + context.getResources().getString(R.string.weather_degree_symbol)));
            pageElements.add(new StrappTextbox(23, "/" + String.valueOf(weatherDay.getLow()) + context.getResources().getString(R.string.weather_degree_symbol)));
            if (weatherDay.getDate().isBefore(DateTime.now().plusDays(1).toLocalDate())) {
                pageElements.add(new StrappTextbox(11, context.getResources().getString(R.string.weather_day_today_text)));
            } else if (weatherDay.getDate().isBefore(DateTime.now().plusDays(2).toLocalDate())) {
                pageElements.add(new StrappTextbox(11, context.getResources().getString(R.string.weather_day_tomorrow_text)));
            } else {
                pageElements.add(new StrappTextbox(11, weatherDay.getDate().dayOfWeek().getAsText()));
            }
        }
        pageElements.add(new StrappIconbox(21, weatherDay.getIconCode()));
        return pageElements;
    }

    public static ArrayList<ArrayList<StrappPageElement>> createWeatherStrappElements(List<WeatherDay> dailyWeather, Context context) {
        ArrayList<ArrayList<StrappPageElement>> strappValues = new ArrayList<>();
        for (int i = 0; i < dailyWeather.size(); i++) {
            strappValues.add(createWeatherStrappElement(dailyWeather.get(i), context));
        }
        return strappValues;
    }

    public CargoStrapp createWeatherStrapp(Context context) {
        List<StrappLayout> weatherLayoutBloblist = new ArrayList<>();
        try {
            weatherLayoutBloblist.add(readLayoutBlob(context.getResources().openRawResource(R.raw.last_updated)));
            weatherLayoutBloblist.add(readLayoutBlob(context.getResources().openRawResource(getWeatherLayout())));
            List<Bitmap> images = getWeatherImages(context);
            return new CargoStrapp(DefaultStrappUUID.STRAPP_BING_WEATHER, context.getResources().getString(R.string.strapp_bing_weather), 1, 0L, images, (short) 0, weatherLayoutBloblist);
        } catch (Exception ex) {
            KLog.e(TAG, "Failed to create weather strapp!", ex);
            return null;
        }
    }

    protected int getWeatherLayout() {
        return R.raw.weather;
    }

    private List<Bitmap> getWeatherImages(Context context) {
        List<Bitmap> images = new ArrayList<>(10);
        images.add(0, getWeatherTileIcon(context));
        images.add(1, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_1_clear_night)));
        images.add(2, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_2_cloudy)));
        images.add(3, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_3_rain)));
        images.add(4, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_4_thunderstorm)));
        images.add(5, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_5_rain_snow)));
        images.add(6, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_6_snow)));
        images.add(7, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_7_fog)));
        images.add(8, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_8_smoke)));
        images.add(9, BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.weather_9_squall)));
        return images;
    }

    protected Bitmap getWeatherTileIcon(Context context) {
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.neon_weather_tile));
    }

    public static CargoStrapp createGenericStrapp(int bitmapTileID, int bitmapAlertId, int bitmapNotificationId, String strappName, List<StrappLayout> StrappLayoutBloblist, short tileIndex, UUID strappUUID, Context context) throws Exception {
        List<Bitmap> images = new ArrayList<>();
        images.add(readBitmapImage(bitmapTileID, context));
        images.add(readBitmapImage(bitmapAlertId, context));
        if (bitmapNotificationId != 0) {
            images.add(readBitmapImage(bitmapNotificationId, context));
        }
        CargoStrapp strapp = new CargoStrapp(strappUUID, strappName, 3, 0L, images, tileIndex, StrappLayoutBloblist);
        if (bitmapNotificationId != 0) {
            strapp.setNotificationImageIndex(2);
        }
        return strapp;
    }

    private static Bitmap readBitmapImage(int rawFileRessourceId, Context context) throws Exception {
        Bitmap bitmap = null;
        int kdkImageDimensions = context.getResources().getInteger(R.integer.kdk_image_dimension_base_2);
        InputStream input = context.getResources().openRawResource(rawFileRessourceId);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            StreamUtils.closeQuietly(input);
            if (onlyBoundsOptions.outWidth != -1 && onlyBoundsOptions.outHeight != -1) {
                int size = onlyBoundsOptions.outHeight;
                int ratio = 1;
                while (size >= kdkImageDimensions) {
                    size >>= 1;
                    ratio *= 2;
                }
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = ratio;
                input = context.getResources().openRawResource(rawFileRessourceId);
                try {
                    bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                } finally {
                }
            }
            return bitmap;
        } finally {
        }
    }

    public CargoStrapp createFinanceStrapp(Context context) {
        List<StrappLayout> financeLayoutBloblist = new ArrayList<>();
        try {
            financeLayoutBloblist.add(readLayoutBlob(context.getResources().openRawResource(R.raw.last_updated)));
            financeLayoutBloblist.add(readLayoutBlob(context.getResources().openRawResource(getFinanceDownLayout())));
            financeLayoutBloblist.add(readLayoutBlob(context.getResources().openRawResource(getFinanceUpLayout())));
            List<Bitmap> images = new ArrayList<>();
            images.add(getFinanceTileIcon(context));
            images.add(BitmapFactory.decodeStream(context.getResources().openRawResource(getFinanceUpIcon())));
            images.add(BitmapFactory.decodeStream(context.getResources().openRawResource(getFinanceDownIcon())));
            return new CargoStrapp(DefaultStrappUUID.STRAPP_BING_FINANCE, context.getResources().getString(R.string.strapp_bing_finance), 1, 0L, images, (short) 0, financeLayoutBloblist);
        } catch (Exception ex) {
            KLog.e(TAG, "Failed to create finance strapp!", ex);
            return null;
        }
    }

    protected int getFinanceUpIcon() {
        return R.raw.finance_up_icon;
    }

    protected int getFinanceDownIcon() {
        return R.raw.finance_down_icon;
    }

    protected int getFinanceDownLayout() {
        return R.raw.finance_down;
    }

    protected int getFinanceUpLayout() {
        return R.raw.finance_up;
    }

    protected Bitmap getFinanceTileIcon(Context context) {
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.strapp_icon_finance));
    }

    public static ArrayList<ArrayList<StrappPageElement>> createFinanceStrappElements(List<Stock> stocks) {
        ArrayList<ArrayList<StrappPageElement>> strappValues = new ArrayList<>();
        for (int i = 0; i < stocks.size(); i++) {
            strappValues.add(createFinanceStrappElement(stocks.get(i)));
        }
        return strappValues;
    }

    public static ArrayList<StrappPageElement> createFinanceStrappElement(Stock stock) {
        ArrayList<StrappPageElement> pageElements = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (stock.getChange() > 0) {
            builder.append(Marker.ANY_NON_NULL_MARKER);
        }
        builder.append(stock.getChange() / 100.0d).append("%");
        DecimalFormat stockFinanceFormat = new DecimalFormat("#0.00;(#0.00)");
        pageElements.add(new StrappTextbox(11, stock.getCompanyName()));
        pageElements.add(new StrappTextbox(12, "|"));
        pageElements.add(new StrappTextbox(13, builder.toString()));
        pageElements.add(new StrappTextbox(22, stockFinanceFormat.format(stock.getValue() / 100.0d)));
        if (stock.getChange() > 0) {
            pageElements.add(new StrappIconbox(21, 1));
        } else {
            pageElements.add(new StrappIconbox(21, 2));
        }
        return pageElements;
    }

    public CargoStrapp createStarbucksStrapp(Context context) {
        try {
            List<Bitmap> images_coffee = new ArrayList<>();
            images_coffee.add(BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.starbucks_logo)));
            images_coffee.add(BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.starbucks_drop)));
            StrappLayout coffeeLayout = readLayoutBlob(context.getResources().openRawResource(getStarbucksLayout()));
            StrappLayout noCardLayout = readLayoutBlob(context.getResources().openRawResource(R.raw.starbucks_no_card_layout));
            ArrayList<StrappLayout> layouts = new ArrayList<>();
            layouts.add(coffeeLayout);
            layouts.add(noCardLayout);
            return new CargoStrapp(DefaultStrappUUID.STRAPP_STARBUCKS, context.getResources().getString(R.string.strapp_starbucks), 0, 0L, images_coffee, (short) 0, layouts);
        } catch (Exception e) {
            KLog.e(TAG, "Failed to create starbucks strapp!", e);
            return null;
        }
    }

    protected int getStarbucksLayout() {
        return R.raw.starbucks_card_barcode_layout;
    }

    public static ArrayList<StrappPageElement> createStarbucksNoCardPage(String messageLine1, String messageLine2, String messageLine3) {
        ArrayList<StrappPageElement> pageElements = new ArrayList<>();
        StrappTextbox textOne = new StrappTextbox(11, messageLine1);
        StrappTextbox textTwo = new StrappTextbox(21, messageLine2);
        StrappTextbox textThree = new StrappTextbox(31, messageLine3);
        pageElements.add(textOne);
        pageElements.add(textTwo);
        pageElements.add(textThree);
        return pageElements;
    }

    public static ArrayList<StrappPageElement> createStarbucksPage(String cardNumber) {
        ArrayList<StrappPageElement> pageElements = new ArrayList<>();
        StrappBarcode barcodeOne = new StrappBarcode(11, BarcodeType.PDF417, cardNumber);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append(' ');
            }
            sb.append(cardNumber.charAt(i));
        }
        StrappTextbox textTwo = new StrappTextbox(21, sb.toString());
        pageElements.add(barcodeOne);
        pageElements.add(textTwo);
        return pageElements;
    }

    public static void clearStrappAndCalendarCacheData(SettingsProvider settingsProvider, CargoConnection mCargoConnection) {
        mCargoConnection.clearLastSentCalendarEventsCache();
        List<UUID> uuids = settingsProvider.getUUIDsOnDevice();
        if (uuids != null) {
            for (UUID uuid : uuids) {
                settingsProvider.clearLastSyncDataForCustomStrapp(uuid);
            }
        }
    }

    public static StrappStateCollection createRetrievedStrappStateCollection(StartStrip retrievedStrip, StrappStateCollection defaultStrapps) {
        StrappStateCollection retrievedSSC = new StrappStateCollection();
        for (CargoStrapp strapp : retrievedStrip.getAppList()) {
            StrappData strappData = strapp.getStrappData();
            if (strappData != null) {
                StrappState strappState = defaultStrapps.get(strappData.getAppId());
                if (strappState != null) {
                    strappState.setIsEnabled(false);
                    retrievedSSC.put(strappState);
                } else {
                    StrappState state = new StrappState(new StrappDefinition(strappData.getAppId(), strapp.getName(), strapp.getTileImage()));
                    state.setIsEnabled(false);
                    retrievedSSC.put(state);
                }
            }
        }
        return retrievedSSC;
    }

    public static void createOrderedLists(StrappStateCollection collection, ArrayList<StrappState> orderedList, ArrayList<StrappState> orderedThirdPartyList, Collection<StrappDefinition> strappDefinitions) {
        HashMap<UUID, Boolean> strappAdded = new HashMap<>();
        for (StrappDefinition strapp : strappDefinitions) {
            if (collection.containsKey(strapp.getStrappId())) {
                StrappState state = collection.get(strapp.getStrappId());
                orderedList.add(state);
                strappAdded.put(strapp.getStrappId(), true);
            }
        }
        for (Map.Entry<UUID, StrappState> strapp2 : collection.entrySet()) {
            if (!strappAdded.containsKey(strapp2.getKey()) && !DefaultStrappUUID.STRAPP_THIRD_PARTY_FILTER.contains(strapp2.getKey())) {
                orderedThirdPartyList.add(strapp2.getValue());
            }
        }
        Collections.sort(orderedThirdPartyList, StrappStateComparator);
    }

    public static boolean isSettingsEnabledStrapp(UUID id) {
        return id.equals(DefaultStrappUUID.STRAPP_MESSAGING) || id.equals(DefaultStrappUUID.STRAPP_EMAIL) || id.equals(DefaultStrappUUID.STRAPP_CALENDAR) || id.equals(DefaultStrappUUID.STRAPP_CALLS) || id.equals(DefaultStrappUUID.STRAPP_RUN) || id.equals(DefaultStrappUUID.STRAPP_BING_FINANCE) || id.equals(DefaultStrappUUID.STRAPP_STARBUCKS) || id.equals(DefaultStrappUUID.STRAPP_FACEBOOK) || id.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER) || id.equals(DefaultStrappUUID.STRAPP_TWITTER) || id.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER) || id.equals(DefaultStrappUUID.STRAPP_BIKE);
    }

    public static void confirmLocalDeviceBackgroundDataPopulated(SettingsProvider settingsProvider, CargoConnection cargoConnection) throws CargoException {
        if (!settingsProvider.isDevicePersonalizationInitialized()) {
            int wallpaperPatternId = cargoConnection.getDeviceWallpaperId();
            settingsProvider.setCurrentWallpaperId(wallpaperPatternId);
            settingsProvider.setIsDevicePersonalizationInitialized(true);
        }
    }

    public int getTileIconForTile(UUID strappId) {
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return R.raw.facebook_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return R.raw.fb_messenger_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return R.raw.notification_center_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return R.raw.email_tile;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return R.raw.twitter_tile;
        }
        return 0;
    }

    public int getBadgingIconForTile(UUID strappId) {
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return R.raw.facebook_alert;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return R.raw.fb_messenger_badge;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return R.raw.notification_center_badge;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return R.raw.email_alert;
        }
        if (strappId.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return R.raw.twitter_alert;
        }
        return 0;
    }

    public int getNotificiationIconForTile(UUID strappId) {
        return 0;
    }
}

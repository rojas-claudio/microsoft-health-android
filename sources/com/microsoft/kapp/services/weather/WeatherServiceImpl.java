package com.microsoft.kapp.services.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.ServiceException;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StrappConstants;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
/* loaded from: classes.dex */
public class WeatherServiceImpl implements WeatherService {
    private static final String TAG = WeatherService.class.getSimpleName();
    private NetworkProvider mNetworkProvider;

    public WeatherServiceImpl(NetworkProvider provider) {
        this.mNetworkProvider = provider;
    }

    @Override // com.microsoft.kapp.services.weather.WeatherService
    public List<WeatherDay> getDailyWeather(double latitude, double longitude, String units) throws ServiceException {
        try {
            String weatherOverviewUrl = String.format(Locale.US, Constants.WEATHER_OVERVIEW_URL_FORMAT, Double.valueOf(latitude), Double.valueOf(longitude), units);
            String weatherResponseString = this.mNetworkProvider.executeHttpGet(weatherOverviewUrl, null);
            List<WeatherDay> result = parseWeatherJson(weatherResponseString, latitude, longitude);
            return result;
        } catch (IOException exception) {
            throw new ServiceException("IO Error calling service.", exception);
        } catch (URISyntaxException exception2) {
            throw new ServiceException("URL syntax error calling service.", exception2);
        }
    }

    private List<WeatherDay> parseWeatherJson(String weatherResponseString, double latitude, double longitude) {
        List<WeatherDay> days = new ArrayList<>();
        JsonObject baseObject = new JsonParser().parse(weatherResponseString).getAsJsonObject();
        JsonArray responses = baseObject.getAsJsonArray("responses");
        JsonObject weatherObject = responses.get(0).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject();
        JsonObject currentWeather = weatherObject.getAsJsonObject("current");
        Integer currentTemp = Integer.valueOf(Float.valueOf(currentWeather.getAsJsonPrimitive("temp").getAsString()).intValue());
        String icon = currentWeather.getAsJsonPrimitive("icon").getAsString();
        int iconCode = StrappConstants.getWeatherKeys().get(icon).intValue();
        String conditionPlaintext = StrappConstants.getWeatherSimpleText().get(icon);
        String location = getCurrentLocation(latitude, longitude);
        days.add(new WeatherDay(currentTemp, iconCode, location, conditionPlaintext));
        JsonObject forecast = weatherObject.getAsJsonObject("forecast");
        JsonArray forecastDays = forecast.getAsJsonArray("days");
        for (int i = 0; i < forecastDays.size(); i++) {
            try {
                JsonObject day = forecastDays.get(i).getAsJsonObject();
                String tempHigh = day.get("tempHi").getAsString();
                String tempLow = day.get("tempLo").getAsString();
                int iconCode2 = StrappConstants.getWeatherKeys().get(day.get("icon").getAsString()).intValue();
                String date = day.get("valid").getAsString();
                String conditionPlaintext2 = day.getAsJsonPrimitive("cap").getAsString();
                DateTime tmpDate = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(date);
                WeatherDay thisDay = new WeatherDay(new DateTime(ISODateTimeFormat.date().print(tmpDate)).toLocalDate(), Float.valueOf(tempHigh).intValue(), Float.valueOf(tempLow).intValue(), iconCode2, conditionPlaintext2);
                days.add(thisDay);
            } catch (Exception e) {
                KLog.d(TAG, "Weather Day not parseable!");
            }
        }
        return days;
    }

    public String getCurrentLocation(double latitude, double longitude) {
        String weatherOverviewUrl = String.format(Locale.US, Constants.LOCATION_URL_FORMAT, Double.valueOf(latitude), Double.valueOf(longitude));
        try {
            String weatherResponseString = this.mNetworkProvider.executeHttpGet(weatherOverviewUrl, null);
            JsonObject baseObject = new JsonParser().parse(weatherResponseString).getAsJsonObject();
            JsonArray responses = baseObject.getAsJsonArray("responses");
            JsonArray locationObject = responses.get(0).getAsJsonObject().getAsJsonArray("locations");
            String locationName = locationObject.get(0).getAsJsonObject().get("displayName").getAsString();
            return locationName;
        } catch (Exception ex) {
            KLog.w(TAG, "Could not get location", ex);
            return MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
        }
    }
}

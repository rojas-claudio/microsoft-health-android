package com.microsoft.krestsdk.services;

import com.google.gson.JsonSyntaxException;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import java.util.Map;
import org.apache.http.client.HttpResponseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
/* loaded from: classes.dex */
public class KRestServiceUtils {
    public static String wrapToken(String kAccessToken) {
        if (kAccessToken != null) {
            return "WRAP access_token=\"" + kAccessToken + "\"";
        }
        return kAccessToken;
    }

    public static void addParameter(String key, String value, Map<String, String> urlParams) {
        if (!urlParams.containsKey(key)) {
            urlParams.put(key, value);
        }
    }

    public static boolean addKRestQueryHeaders(Map<String, String> headers, Map<String, String> urlParams, KCredential credentials) {
        if (credentials != null) {
            addParameter("baseUrl", credentials.getEndPoint(), urlParams);
            headers.put(Constants.AUTHORIZATION_HEADER_NAME, wrapToken(credentials.getAccessToken()));
            return true;
        }
        return false;
    }

    public static void logException(Exception exception, String method, String requestUrl) {
        if (exception instanceof JsonSyntaxException) {
            KLog.logPrivate("[" + method + " REQUEST]", "JSON parsing exception\nURL: " + requestUrl, exception);
        } else if (exception instanceof HttpResponseException) {
            HttpResponseException httpException = (HttpResponseException) exception;
            String message = "HTTP error: " + httpException.getStatusCode() + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + httpException.getMessage() + "\nURL: " + requestUrl + "\n\n";
            KLog.logPrivate("[" + method + " REQUEST]", message, (Throwable) httpException);
        } else {
            KLog.logPrivate("[" + method + " REQUEST]", "Network exception\nURL: " + requestUrl, exception);
        }
    }

    public static String populateUrl(String url, Map<String, String> urlParams) {
        StringBuilder completeUrl = new StringBuilder();
        completeUrl.append(url);
        if (urlParams != null) {
            for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                String Key = "{" + entry.getKey() + "}";
                int indexOfKey = completeUrl.indexOf(Key);
                if (indexOfKey != -1) {
                    completeUrl.replace(indexOfKey, indexOfKey + Key.length(), entry.getValue());
                }
            }
        }
        return completeUrl.toString();
    }

    public static void addDateParameter(String key, LocalDate date, Map<String, String> urlParams) {
        if (!urlParams.containsKey(key)) {
            urlParams.put(key, formatDate(date));
        }
    }

    public static void addDateTimeNoMillisParameter(String key, LocalDate date, Map<String, String> urlParams) {
        addDateTimeNoMillisParameter(key, date.toDateTimeAtStartOfDay(), urlParams);
    }

    public static void addDateTimeNoMillisParameter(String key, DateTime date, Map<String, String> urlParams) {
        if (!urlParams.containsKey(key)) {
            urlParams.put(key, formatDateTime(date));
        }
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter dateFormatter = ISODateTimeFormat.date();
        return dateFormatter.print(date);
    }

    public static String formatDateTime(DateTime dateTime) {
        DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTimeNoMillis();
        return dateFormatter.print(dateTime);
    }

    public static String formatDateTime(LocalDate date) {
        return formatDateTime(date.toDateTimeAtStartOfDay());
    }
}

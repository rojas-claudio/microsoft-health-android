package com.microsoft.kapp.cache;

import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CacheUtils {
    public static final String APPCONFIGINFOTAG = "APPCONFIGINFOTAG";
    public static final String APPCONFIGTAG = "APPCONFIGTAG";
    public static final String CONNECTEDAPPS = "CONNECTEDAPPS";
    public static final int DEFAULT_CACHE_EXPIRATION_TIME = 10080;
    public static Map<String, Integer> DefaultExpirationTimesInMins = new HashMap();
    private static final String EVENTIDTAG = "EventId_%s";
    public static final String EVENTSTAG = "EVENTS";
    private static final String EVENTTYPETAG = "EventType_%s";
    public static final String FEATUREDWORKOUTSTAG = "FEATUREDWORKOUTS";
    public static final String GOALSTAG = "GOALS";
    public static final String GOLFCOURSEDETAILS = "GOLFCOURSEDETAILS";
    public static final String GOLFEVENTSLOWCACHE = "GOLFEVENTS";
    public static final String HNFCUSTOMWORKOUTLISTTAG = "HNFCUSTOMWORKOUTLIST";
    public static final String HNFWORKOUTLISTTAG = "HNFWORKOUTLIST";
    public static final String HNFWORKOUTTAG = "HNFWORKOUT";
    public static final String LASTSYNCEDWORKOUT = "LASTSYNCEDWORKOUT";
    public static final String SYNCTAG = "SYNC";
    public static final String TMAGTAG = "TMAG";
    public static final String USERPROFILETAG = "USERPROFILE";
    public static final String WORKOUTFAVORITESTAG = "WORKOUTFAVORITES";
    public static final String WORKOUTPLANTAG = "WORKOUTPLAN";

    static {
        DefaultExpirationTimesInMins.put(SYNCTAG, 60);
        DefaultExpirationTimesInMins.put(EVENTSTAG, 1440);
        DefaultExpirationTimesInMins.put(USERPROFILETAG, 1440);
        DefaultExpirationTimesInMins.put(FEATUREDWORKOUTSTAG, 60);
        DefaultExpirationTimesInMins.put(GOALSTAG, 1440);
        DefaultExpirationTimesInMins.put(WORKOUTFAVORITESTAG, 1440);
        DefaultExpirationTimesInMins.put(WORKOUTPLANTAG, 60);
        DefaultExpirationTimesInMins.put(HNFWORKOUTTAG, 1440);
        DefaultExpirationTimesInMins.put(HNFWORKOUTLISTTAG, 60);
        DefaultExpirationTimesInMins.put(HNFCUSTOMWORKOUTLISTTAG, 30);
        DefaultExpirationTimesInMins.put(LASTSYNCEDWORKOUT, 1440);
        DefaultExpirationTimesInMins.put(CONNECTEDAPPS, 1);
        DefaultExpirationTimesInMins.put(GOLFEVENTSLOWCACHE, 1);
        DefaultExpirationTimesInMins.put("TMAG", 30);
        DefaultExpirationTimesInMins.put(GOLFCOURSEDETAILS, 1440);
        DefaultExpirationTimesInMins.put(APPCONFIGTAG, 10080);
        DefaultExpirationTimesInMins.put(APPCONFIGINFOTAG, 1);
    }

    public static String getEventTag(String eventId) {
        return String.format(EVENTIDTAG, eventId);
    }

    public static String getEventTypeTag(String eventType) {
        return String.format(EVENTTYPETAG, eventType);
    }
}

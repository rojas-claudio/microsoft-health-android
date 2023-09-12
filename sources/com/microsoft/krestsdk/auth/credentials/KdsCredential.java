package com.microsoft.krestsdk.auth.credentials;

import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.krestsdk.auth.ServiceInfo;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class KdsCredential extends AbstractCredential {
    private static String TAG = KdsCredential.class.getSimpleName();
    private String mEndPoint;
    private String mFUSEndPoint;
    private String mHnFEndPoint;
    private String mHnFQueryParameters;
    private String mKdsUrl;
    private String mLoginUrl;
    private LocalDate mProfileCreatedOn;
    private String mUserId;

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential, com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public /* bridge */ /* synthetic */ String getAccessToken() {
        return super.getAccessToken();
    }

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential, com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public /* bridge */ /* synthetic */ boolean isTokenExpired() {
        return super.isTokenExpired();
    }

    public KdsCredential(String accessToken, String endPoint, String kdsUrl, String userId, String loginUrl, String fusEndPoint, String hnfEndPoint, String hnfQueryParameters, String profileCreation) {
        super(accessToken);
        this.mEndPoint = endPoint;
        this.mKdsUrl = kdsUrl;
        this.mUserId = userId;
        this.mLoginUrl = loginUrl;
        this.mFUSEndPoint = fusEndPoint;
        this.mHnFEndPoint = hnfEndPoint;
        this.mHnFQueryParameters = hnfQueryParameters;
        try {
            Date profileCreatedDate = ProfileUtils.getDateFromCloudTime(profileCreation);
            this.mProfileCreatedOn = new LocalDate(profileCreatedDate.getTime());
        } catch (ParseException e) {
            KLog.e(TAG, "Credentials could not be created!  Using fallback time of 2 weeks ago.");
            this.mProfileCreatedOn = LocalDate.now().plusWeeks(-2);
        }
    }

    public KdsCredential(ServiceInfo serviceInfo, String kdsUrl, String loginUrl) {
        super(serviceInfo.AccessToken);
        this.mEndPoint = serviceInfo.PodAddress;
        this.mKdsUrl = kdsUrl;
        this.mUserId = serviceInfo.UserId;
        this.mLoginUrl = loginUrl;
        this.mFUSEndPoint = serviceInfo.FUSEndpoint;
        this.mHnFEndPoint = serviceInfo.AuthedHnFEndPoint;
        this.mHnFQueryParameters = serviceInfo.AuthedHnFQueryParameters;
        try {
            Date profileCreatedDate = ProfileUtils.getDateFromCloudTime(serviceInfo.ProfileCreatedDate);
            this.mProfileCreatedOn = new LocalDate(profileCreatedDate.getTime());
        } catch (ParseException e) {
            this.mProfileCreatedOn = LocalDate.now().plusWeeks(-2);
        }
    }

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential
    public DateTime getTokenExpiration() {
        String accessToken = getAccessToken();
        if (accessToken != null) {
            Map<String, String> parameters = paramsToMap(accessToken);
            String expirationDate = parameters.get("ExpiresOn");
            if (expirationDate != null) {
                long secondsSince1970 = Long.parseLong(expirationDate);
                return new DateTime(1000 * secondsSince1970);
            }
        }
        return new DateTime();
    }

    private Map<String, String> paramsToMap(String url) {
        Map<String, String> pairs = new HashMap<>();
        String[] parameters = url.split("&");
        for (String parameter : parameters) {
            int equalsIndex = parameter.indexOf(SimpleComparison.EQUAL_TO_OPERATION);
            if (equalsIndex >= 0) {
                String key = parameter.substring(0, equalsIndex);
                String value = parameter.substring(equalsIndex + 1);
                pairs.put(key, value);
            }
        }
        return pairs;
    }

    public String getEndPoint() {
        return this.mEndPoint;
    }

    public String getKdsUrl() {
        return this.mKdsUrl;
    }

    public String getLoginUrl() {
        return this.mLoginUrl;
    }

    public String getFUSEndPoint() {
        return this.mFUSEndPoint;
    }

    public String getHnFEndPoint() {
        return this.mHnFEndPoint;
    }

    public String getHnFQueryParameters() {
        return this.mHnFQueryParameters;
    }

    public String getUserId() {
        return this.mUserId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalDate getProfileCreationDate() {
        return this.mProfileCreatedOn;
    }
}

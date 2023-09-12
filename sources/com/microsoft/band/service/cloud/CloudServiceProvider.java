package com.microsoft.band.service.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.service.CargoClientSession;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.CargoServiceProvider;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.service.device.SensorLogDownload;
import com.microsoft.band.service.util.FileHelper;
import com.microsoft.band.util.StreamUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.joda.time.DateTimeConstants;
import org.json.JSONException;
/* loaded from: classes.dex */
public class CloudServiceProvider extends CargoServiceProvider {
    protected static final String ACCEPT_ENCODING = "Accept-Encoding";
    protected static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String DEFAULT_SDK_VERSION = "1.0.0.0";
    private static final String GET_EPHEMERIS_ENDPOINT = "api/ephemeris";
    private static final String GET_FIRMWARE_ENDPOINT = "api/FirmwareQuery/?deviceFamily=%s&publishType=Latest&OneBL=%s&TwoUp=%s&currentFirmwareVersion=%s&IsForcedUpdate=%s";
    private static final String GET_TIMEZONE_ENDPOINT = "api/TimeZone";
    protected static final String GZIP = "gzip";
    protected static final String HEADER_ACCEPTED_LANGUAGE = "Accept-Language";
    protected static final String HEADER_NAME_AUTH = "Authorization";
    protected static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
    protected static final String HEADER_NAME_HOST = "Host";
    protected static final String HEADER_NAME_MSBLOB_TYPE = "x-ms-blob-type";
    protected static final String HEADER_REGION = "Region";
    protected static final String HEADER_USER_AGENT = "User-Agent";
    protected static final String HEADER_VAL_BLOCKBLOB = "BlockBlob";
    protected static final String HEADER_VAL_CONTENT_JSON = "application/json";
    private static final int HTTP_DEFAULT_TIMEOUT = 20000;
    private static final int HTTP_DOWNLOAD_FILE_TIMEOUT = 120000;
    private static final int HTTP_GET_PROFILE_TIMEOUT = 60000;
    protected static final String HTTP_REQUEST_GET = "GET";
    private static final int HTTP_SAVE_PROFILE_TIMEOUT = 60000;
    private static final int HTTP_SO_TIMEOUT = 60000;
    private static final String PROFILE_ENDPOINT = "/v1/userprofiles";
    public static String TAG = CloudServiceProvider.class.getSimpleName();
    private CargoServiceInfo mApiInfo;

    public CloudServiceProvider(CargoServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            throw new NullPointerException("serviceInfo");
        }
        this.mApiInfo = serviceInfo;
    }

    public UserProfileInfo getCloudProfile() throws CargoServiceException {
        try {
            UserProfileInfo cloudProfile = UserProfileInfo.getCloudProfileFromJson(getCloudData(PROFILE_ENDPOINT));
            return cloudProfile;
        } catch (CargoException e) {
            throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    protected String getCloudData(String endpoint) throws CargoServiceException {
        HttpGet get = new HttpGet();
        get.setHeader(ACCEPT_ENCODING, GZIP);
        initializeHttpRequest(this.mApiInfo, get, endpoint, "application/json");
        HttpResponse response = executeRequest(get, DateTimeConstants.MILLIS_PER_MINUTE, true);
        return getDataFromHttpResponse(response);
    }

    protected <T extends HttpRequestBase> void initializeHttpRequest(CargoServiceInfo info, T request, String endpoint, String contentType) throws CargoServiceException {
        if (endpoint != null) {
            String url = info.getUrl().toExternalForm() + endpoint;
            try {
                request.setURI(new URI(url));
                request.setHeader(HEADER_NAME_HOST, info.getHostName());
            } catch (URISyntaxException e) {
                throw new CargoServiceException(String.format("Endpoint '%s' is malformed.", url), e, BandServiceMessage.Response.SERVICE_CLOUD_INVALID_URL_ERROR);
            }
        }
        request.setHeader("Authorization", info.getAccessToken());
        if (contentType != null) {
            request.setHeader("Content-Type", contentType);
        }
    }

    protected HttpResponse executeRequest(HttpUriRequest request) throws CargoServiceException {
        return executeRequest(request, HTTP_DEFAULT_TIMEOUT, true);
    }

    protected HttpResponse executeRequest(HttpUriRequest request, int timeout, boolean addUserAgent) throws CargoServiceException {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(basicHttpParams, DateTimeConstants.MILLIS_PER_MINUTE);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        addRequestTimeout(request, timeout);
        if (addUserAgent) {
            try {
                if (this.mApiInfo != null && this.mApiInfo.getUserAgent() != null && !this.mApiInfo.getUserAgent().isEmpty()) {
                    request.setHeader(HEADER_USER_AGENT, this.mApiInfo.getUserAgent());
                } else {
                    request.setHeader(HEADER_USER_AGENT, String.format("KDK/%s (Android/%s; Brand/%s; Model/%s; Device/%s)", DEFAULT_SDK_VERSION, Integer.valueOf(Build.VERSION.SDK_INT), Build.BRAND, Build.MODEL, Build.DEVICE));
                }
            } catch (SocketTimeoutException e) {
                throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_TIMEOUT, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_TIMEOUT_ERROR);
            } catch (IOException e2) {
                if (request.isAborted()) {
                    throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_TIMEOUT, e2.getMessage()), e2, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_TIMEOUT_ERROR);
                }
                throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_IO, e2.getMessage()), e2, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
            } catch (Exception e3) {
                throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION, e3.getMessage()), e3, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            }
        }
        HttpResponse response = defaultHttpClient.execute(request);
        checkGetResponse(request, response);
        return response;
    }

    private void addRequestTimeout(final HttpUriRequest request, int timeout) {
        TimerTask task = new TimerTask() { // from class: com.microsoft.band.service.cloud.CloudServiceProvider.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (request != null) {
                    request.abort();
                }
            }
        };
        new Timer(true).schedule(task, timeout);
    }

    private void checkGetResponse(HttpUriRequest request, HttpResponse response) throws CargoServiceException {
        if (request.getMethod().equals("GET") && response.getStatusLine().getStatusCode() != 200) {
            throw new CargoServiceException(String.format("Failed to execute HttpGet due to %s", response.getStatusLine().getReasonPhrase()), BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
        }
    }

    protected String getDataFromHttpResponse(HttpResponse response) throws CargoServiceException {
        BufferedReader reader = null;
        try {
            try {
                HttpEntity entity = response.getEntity();
                StringBuilder builder = new StringBuilder();
                InputStream stream = entity.getContent();
                if (response.containsHeader(CONTENT_ENCODING) && GZIP.equals(response.getFirstHeader(CONTENT_ENCODING).getValue())) {
                    GZIPInputStream gZipStream = new GZIPInputStream(stream);
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(gZipStream));
                    reader = reader2;
                } else {
                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(stream));
                    reader = reader3;
                }
                while (true) {
                    String currentLine = reader.readLine();
                    if (currentLine != null) {
                        builder.append(currentLine.concat(System.getProperty("line.separator")));
                    } else {
                        String result = builder.toString();
                        return result;
                    }
                }
            } catch (IOException e) {
                throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_IO, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
            } catch (Exception e2) {
                throw new CargoServiceException(e2.getMessage(), e2, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
            }
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void saveUserProfile(CloudJSONDataModel cloudProfileInfo) throws CargoServiceException {
        if (cloudProfileInfo == null) {
            throw new IllegalArgumentException("cloudProfileInfo is required.");
        }
        try {
            HttpPut put = new HttpPut();
            initializeHttpRequest(this.mApiInfo, put, "/v1/userprofiles/put", "application/json");
            StringEntity params = new StringEntity(cloudProfileInfo.toJSONString(), "UTF-8");
            put.setEntity(params);
            HttpResponse response = executeRequest(put, DateTimeConstants.MILLIS_PER_MINUTE, true);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new CargoServiceException(String.format("Failed to upload profile to the cloud. %s", response.getStatusLine().getReasonPhrase()), BandServiceMessage.Response.SERVICE_CLOUD_OPERATION_FAILED_ERROR);
            }
        } catch (Exception ex) {
            throw new CargoServiceException(ex.getMessage(), ex, BandServiceMessage.Response.SERVICE_CLOUD_OPERATION_FAILED_ERROR);
        }
    }

    public BandServiceMessage.Response saveUserProfileFromSync(UserProfileInfo cloudProfileInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        try {
            saveUserProfile(cloudProfileInfo);
            return response;
        } catch (CargoServiceException e) {
            BandServiceMessage.Response response2 = e.getResponse();
            KDKLog.e(TAG, "saveUserProfileFromSync caught error: " + e.getMessage(), e);
            return response2;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo netInfo = connectMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public CargoFirmwareUpdateInfo getCargoFirmwareUpdateInfo(String deviceFamily, String currentVersionApp, String currentVersionBL, String currentVersionUP, boolean fwOnDeviceValidation, String queryParams) throws CargoServiceException {
        Validation.validateNullParameter(deviceFamily, "deviceFamily not specified");
        StringBuilder append = new StringBuilder().append(this.mApiInfo.getFileUpdateServiceAddress());
        Object[] objArr = new Object[5];
        objArr[0] = deviceFamily;
        if (currentVersionBL == null) {
            currentVersionBL = "none";
        }
        objArr[1] = currentVersionBL;
        if (currentVersionUP == null) {
            currentVersionUP = "none";
        }
        objArr[2] = currentVersionUP;
        objArr[3] = currentVersionApp == null ? "none" : currentVersionApp;
        objArr[4] = Boolean.valueOf(!fwOnDeviceValidation);
        String url = append.append(String.format(GET_FIRMWARE_ENDPOINT, objArr)).toString();
        if (queryParams != null) {
            url = url + queryParams;
        }
        HttpGet get = buildHttpGetWithAuthentication(url);
        HttpResponse response = executeRequest(get);
        try {
            CargoFirmwareUpdateInfo cargoFirmwareUpdateInfo = CargoFirmwareUpdateInfo.getCloudFirmwareFromJson(getDataFromHttpResponse(response), deviceFamily, currentVersionApp);
            KDKLog.i(TAG, "FirmwareInfo: %s", cargoFirmwareUpdateInfo.toString());
            return cargoFirmwareUpdateInfo;
        } catch (CargoException e) {
            throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    public BandServiceMessage.Response downloadFirmware(CargoClientSession clientSession, CargoFirmwareUpdateInfo firmwareUpdateInfo) {
        BandServiceMessage.Response response;
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession not specified");
        }
        if (firmwareUpdateInfo == null) {
            throw new IllegalArgumentException("firmwareUpdateInfo not specified");
        }
        BandServiceMessage.Response response2 = BandServiceMessage.Response.SUCCESS;
        if (firmwareUpdateInfo.isFirmwareUpdateAvailable()) {
            File tmpFirmwareFile = null;
            try {
                try {
                    File firmwareFile = FileHelper.makeFirmwareFile(CargoClientSession.getDirectoryPath(), firmwareUpdateInfo.getUniqueVersion(), false);
                    if (!firmwareFile.exists()) {
                        FileHelper.deleteAllFilesInDirectory(firmwareFile.getParentFile(), false);
                        tmpFirmwareFile = FileHelper.makeFirmwareFile(CargoClientSession.getDirectoryPath(), firmwareUpdateInfo.getUniqueVersion(), true);
                        long contentLength = downloadContentToFile(tmpFirmwareFile, firmwareUpdateInfo.getPrimaryUrl(), firmwareUpdateInfo.getFallbackUrl(), firmwareUpdateInfo.getMirrorUrl());
                        if (firmwareUpdateInfo.getSizeInBytes() == contentLength) {
                            FileHelper.renameFileTo(tmpFirmwareFile, firmwareFile);
                            KDKLog.i(TAG, "Downloaded firmware version %s written to %s.", firmwareUpdateInfo.getFirmwareVersion(), firmwareFile);
                        } else {
                            throw new CargoServiceException(String.format("Failed to downloaded firmware version %s from cloud", firmwareUpdateInfo.getFirmwareVersion()), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
                        }
                    }
                    try {
                        return response2;
                    } catch (CargoServiceException e) {
                        return response;
                    }
                } finally {
                    try {
                        FileHelper.deleteFile(null);
                    } catch (CargoServiceException e2) {
                        KDKLog.e(TAG, "download firmware delete file exception: " + e2.getMessage(), e2);
                        e2.getResponse();
                    }
                }
            } catch (CargoServiceException e3) {
                KDKLog.e(TAG, "download firmware caught exception: " + e3.getMessage(), e3);
                BandServiceMessage.Response response3 = e3.getResponse();
                try {
                    FileHelper.deleteFile(null);
                    return response3;
                } catch (CargoServiceException e4) {
                    KDKLog.e(TAG, "download firmware delete file exception: " + e4.getMessage(), e4);
                    BandServiceMessage.Response response4 = e4.getResponse();
                    return response4;
                }
            }
        }
        KDKLog.e(TAG, "Requested firmware upgrade for device (family: %s, version: %s) not available from cloud", firmwareUpdateInfo.getDeviceFamily(), firmwareUpdateInfo.getCurrentVersion());
        BandServiceMessage.Response response5 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_NOT_AVAILABLE_ERROR;
        return response5;
    }

    public TimeZoneSettingsUpdateInfo getTimeZoneUpdateInfo(String localeName) throws CargoServiceException {
        String url = this.mApiInfo.getFileUpdateServiceAddress() + GET_TIMEZONE_ENDPOINT;
        HttpGet get = buildHttpGetWithAuthentication(url);
        addLanguageAndRegionHeaders(localeName, get);
        HttpResponse response = executeRequest(get);
        try {
            return TimeZoneSettingsUpdateInfo.getTimeZoneSettingsFromJson(getDataFromHttpResponse(response));
        } catch (CargoException e) {
            throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    public BandServiceMessage.Response downloadTimeZoneSettings(CargoClientSession clientSession, String localeName) {
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession not specified");
        }
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        try {
            long lastTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_timezone_check_time, 0L);
            if (lastTime < System.currentTimeMillis() - 86400000) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.TIMEZONE_DOWNLOAD_NOT_REQUIRED;
                TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfo = clientSession.getCloudProvider().getTimeZoneUpdateInfo(localeName);
                clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_timezone_check_time, System.currentTimeMillis()).commit();
                TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfoOld = TimeZoneSettingsUpdateInfo.fromSharedPreferences(clientSession.getSharedPreferences());
                if (timeZoneSettingsUpdateInfo.getLastModifiedDateTime().getTime() > timeZoneSettingsUpdateInfoOld.getLastModifiedDateTime().getTime()) {
                    BandServiceMessage.Response response3 = downloadTimeZoneSettingsFile(clientSession, timeZoneSettingsUpdateInfo);
                    return response3;
                }
                return response2;
            }
            return response;
        } catch (CargoException e1) {
            BandServiceMessage.Response response4 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
            KDKLog.e(TAG, "Timezone Download CargoException: " + e1.getMessage(), e1);
            return response4;
        } catch (CargoServiceException e2) {
            BandServiceMessage.Response response5 = e2.getResponse();
            KDKLog.e(TAG, "Timezone Download CargoServiceException: " + e2.getMessage(), e2);
            return response5;
        }
    }

    public BandServiceMessage.Response downloadTimeZoneSettingsFile(CargoClientSession clientSession, TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfo) {
        File timezoneFile;
        long contentLength;
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession not specified");
        }
        if (timeZoneSettingsUpdateInfo == null) {
            throw new IllegalArgumentException("TimeZoneSettingsUpdateInfo not specified");
        }
        Bundle bundle = clientSession.getToken().toBundle();
        bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, timeZoneSettingsUpdateInfo);
        clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_STARTED, 0, bundle);
        File tmpTZFile = null;
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        try {
            try {
                timezoneFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.TIMEZONE_DIR, FileHelper.ACTUAL_DATA, false);
                tmpTZFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.TIMEZONE_DIR, FileHelper.ACTUAL_DATA, true);
                contentLength = downloadContentToFile(tmpTZFile, timeZoneSettingsUpdateInfo.getURL());
            } catch (CargoServiceException e) {
                KDKLog.e(TAG, "download timezone file caught exception: " + e.getMessage(), e);
                response = e.getResponse();
                try {
                    FileHelper.deleteFile(tmpTZFile);
                } catch (CargoServiceException e2) {
                    KDKLog.e(TAG, "download timezone file delete file exception: " + e2.getMessage(), e2);
                    response = e2.getResponse();
                }
            }
            if (5000 < contentLength) {
                FileHelper.renameFileTo(tmpTZFile, timezoneFile);
                KDKLog.i(TAG, "Downloaded timezone file that was last updated on %s to %s.", timeZoneSettingsUpdateInfo.getLMTString(), timezoneFile);
                if (response == BandServiceMessage.Response.SUCCESS) {
                    clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_timezone_download_time, System.currentTimeMillis()).commit();
                    try {
                        clientSession.getSharedPreferences().edit().putString(CargoConstants.last_timezone_json, timeZoneSettingsUpdateInfo.toJSONString()).commit();
                    } catch (JSONException e3) {
                        KDKLog.e(TAG, "timeZoneSettingsUpdateInfo does not parse itself into json correctly", e3);
                        response = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
                    }
                }
                clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_COMPLETED, response.getCode(), bundle);
                return response;
            }
            throw new CargoServiceException(String.format("Failed to downloaded timezone file last updated on %s from cloud", timeZoneSettingsUpdateInfo.getLastModifiedDateTime()), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        } finally {
            try {
                FileHelper.deleteFile(tmpTZFile);
            } catch (CargoServiceException e4) {
                KDKLog.e(TAG, "download timezone file delete file exception: " + e4.getMessage(), e4);
                e4.getResponse();
            }
        }
    }

    public EphemerisUpdateInfo getEphemerisUpdateInfo() throws CargoServiceException {
        String url = this.mApiInfo.getFileUpdateServiceAddress() + GET_EPHEMERIS_ENDPOINT;
        HttpGet get = buildHttpGetWithAuthentication(url);
        HttpResponse response = executeRequest(get);
        try {
            return EphemerisUpdateInfo.getEphemerisInfoFromJson(getDataFromHttpResponse(response));
        } catch (CargoException e) {
            throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    public BandServiceMessage.Response downloadEphemeris(CargoClientSession clientSession) {
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession not specified");
        }
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        try {
            long lastTime = clientSession.getSharedPreferences().getLong(CargoConstants.last_ephemeris_check_time, 0L);
            if (lastTime < System.currentTimeMillis() - 21600000) {
                BandServiceMessage.Response response2 = BandServiceMessage.Response.EPHEMERIS_DOWNLOAD_NOT_REQUIRED;
                EphemerisUpdateInfo ephemerisUpdateInfo = clientSession.getCloudProvider().getEphemerisUpdateInfo();
                clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_ephemeris_check_time, System.currentTimeMillis()).commit();
                EphemerisUpdateInfo ephemerisUpdateInfoOld = EphemerisUpdateInfo.fromSharedPreferences(clientSession.getSharedPreferences());
                if (ephemerisUpdateInfo.getLastModifiedDateTime().getTime() > ephemerisUpdateInfoOld.getLastModifiedDateTime().getTime()) {
                    BandServiceMessage.Response response3 = downloadEphemerisFile(clientSession, ephemerisUpdateInfo);
                    return response3;
                }
                return response2;
            }
            return response;
        } catch (CargoException e1) {
            BandServiceMessage.Response response4 = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
            KDKLog.e(TAG, "Ephemeris Download CargoException: " + e1.getMessage(), e1);
            return response4;
        } catch (CargoServiceException e2) {
            BandServiceMessage.Response response5 = e2.getResponse();
            KDKLog.e(TAG, "Ephemeris Download CargoServiceException: " + e2.getMessage(), e2);
            return response5;
        }
    }

    public BandServiceMessage.Response downloadEphemerisFile(CargoClientSession clientSession, EphemerisUpdateInfo ephemerisUpdateInfo) {
        File ephemerisFile;
        long contentLength;
        if (clientSession == null) {
            throw new IllegalArgumentException("clientSession not specified");
        }
        if (ephemerisUpdateInfo == null) {
            throw new IllegalArgumentException("ephemerisUpdateInfo not specified");
        }
        Bundle bundle = clientSession.getToken().toBundle();
        bundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, ephemerisUpdateInfo);
        clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_STARTED, 0, bundle);
        BandServiceMessage.Response response = BandServiceMessage.Response.SUCCESS;
        File tmpEphemerisFile = null;
        try {
            try {
                ephemerisFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.EPHEMERIS_DIR, FileHelper.ACTUAL_DATA, false);
                tmpEphemerisFile = FileHelper.makeFile(CargoClientSession.getDirectoryPath(), FileHelper.EPHEMERIS_DIR, FileHelper.ACTUAL_DATA, true);
                contentLength = downloadContentToFile(tmpEphemerisFile, ephemerisUpdateInfo.getURL());
            } catch (CargoServiceException e) {
                KDKLog.e(TAG, "download ephemeris file caught exception: " + e.getMessage(), e);
                response = e.getResponse();
                try {
                    FileHelper.deleteFile(tmpEphemerisFile);
                } catch (CargoServiceException e2) {
                    KDKLog.e(TAG, "download ephemeris file delete file exception: " + e2.getMessage(), e2);
                    response = e2.getResponse();
                }
            }
            if (5000 < contentLength) {
                FileHelper.renameFileTo(tmpEphemerisFile, ephemerisFile);
                KDKLog.i(TAG, "Downloaded ephemeris file that was last updated on %s to %s.", ephemerisUpdateInfo.getLMTString(), ephemerisFile);
                if (response == BandServiceMessage.Response.SUCCESS) {
                    clientSession.getSharedPreferences().edit().putLong(CargoConstants.last_ephemeris_download_time, System.currentTimeMillis()).commit();
                    try {
                        clientSession.getSharedPreferences().edit().putString(CargoConstants.last_ephemeris_json, ephemerisUpdateInfo.toJSONString()).commit();
                    } catch (JSONException e3) {
                        KDKLog.e(TAG, "ephemerisUpdateInfo does not parse itself into json correctly", e3);
                        response = BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR;
                    }
                }
                clientSession.sendServiceMessage(BandServiceMessage.DOWNLOAD_NOTIFICATION, BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED, response.getCode(), bundle);
                return response;
            }
            throw new CargoServiceException(String.format("Failed to downloaded ephemeris file last updated on %s from cloud", ephemerisUpdateInfo.getLastModifiedDateTime()), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        } finally {
            try {
                FileHelper.deleteFile(tmpEphemerisFile);
            } catch (CargoServiceException e4) {
                KDKLog.e(TAG, "download ephemeris file delete file exception: " + e4.getMessage(), e4);
                e4.getResponse();
            }
        }
    }

    public BandServiceMessage.Response uploadBytesToCloud(byte[] data, CloudDataResource cdr) throws CargoServiceException {
        if (data == null) {
            throw new IllegalArgumentException("Data not specified");
        }
        if (cdr == null) {
            throw new IllegalArgumentException("CloudDataResource not specified");
        }
        if (cdr.getLogFileType() == CloudDataResource.LogFileTypes.UNKNOWN) {
            throw new IllegalArgumentException("LogFileType is unknown");
        }
        if (cdr.getLogFileType() == CloudDataResource.LogFileTypes.BANDBINARY) {
            KDKLog.d(TAG, "Uploading Sensor Log File: seqID[%d, %d]", Integer.valueOf(cdr.getMetaData().getStartSequenceId()), Integer.valueOf(cdr.getMetaData().getEndSequenceId()));
        }
        CloudRequest syncSensorLogRequest = new SyncSensorLogRequest(this.mApiInfo, cdr, data);
        CloudResponse<?> syncSensorLogResponse = syncSensorLogRequest.execute();
        return syncSensorLogResponse.getResponse();
    }

    public BandServiceMessage.Response updateCloudDataResource(List<CloudDataResource> cdrList, List<String> completedUploadIds) {
        CloudRequest getUploadStatusRequest = new GetUploadStatusRequest(this.mApiInfo, cdrList);
        try {
            GetUploadStatusResponse uploadStatusResponse = (GetUploadStatusResponse) getUploadStatusRequest.execute();
            BandServiceMessage.Response response = uploadStatusResponse.getResponse();
            if (response == BandServiceMessage.Response.SUCCESS) {
                Set<String> completedIds = uploadStatusResponse.getResponseData();
                for (CloudDataResource cdr : cdrList) {
                    String uploadId = cdr.getUploadId();
                    if (completedIds.contains(uploadId)) {
                        completedUploadIds.add(uploadId);
                    }
                }
                return response;
            }
            return response;
        } catch (CargoServiceException e) {
            return e.getResponse();
        }
    }

    public BandServiceMessage.Response sendCrashDumpFilesToCloud(CargoClientSession clientSession) {
        return sendFilesToCloud(CargoConstants.CrashDumps, CloudDataResource.LogFileTypes.CRASHDUMP, clientSession.getUploadMetadata());
    }

    public BandServiceMessage.Response sendTelemetryFilesToCloud(CargoClientSession clientSession) {
        return sendFilesToCloud(CargoConstants.Instrumentation, CloudDataResource.LogFileTypes.TELEMETRY, clientSession.getUploadMetadata());
    }

    public BandServiceMessage.Response sendFilesToCloud(String name, CloudDataResource.LogFileTypes fileType, UploadMetadata meta) {
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
        File directoryPath = new File(CargoClientSession.getDirectoryPath());
        String[] files = directoryPath.list();
        for (String fileName : files) {
            if (fileName.startsWith(name + "-")) {
                response = sendFileToCloud(CargoClientSession.getDirectoryPath() + File.separator + fileName, fileType, meta);
            }
        }
        return response;
    }

    public BandServiceMessage.Response sendFileToCloud(String fileName, CloudDataResource.LogFileTypes fileType, UploadMetadata meta) {
        BandServiceMessage.Response response = BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR;
        try {
            File f = new File(fileName);
            CloudDataResource cdr = new CloudDataResource();
            cdr.setUploadId(new Date());
            cdr.setLogFileType(fileType);
            cdr.setMetaData(meta);
            byte[] fileContents = FileHelper.readDataFromFile(f);
            BandServiceMessage.Response response2 = uploadBytesToCloud(fileContents, cdr);
            if (response2 == BandServiceMessage.Response.SUCCESS) {
                KDKLog.d(TAG, "%s file uploaded to cloud", fileName);
                FileHelper.deleteFile(f);
                return response2;
            }
            return response2;
        } catch (CargoServiceException e) {
            KDKLog.e(TAG, "Send File %s to cloud failed with %s.", fileName, e.getMessage(), e);
            return e.getResponse();
        }
    }

    public BandServiceMessage.Response uploadSensorLog(SensorLogDownload log, UploadMetadata uploadMetadata, CloudDataResource cdr) {
        if (log == null) {
            throw new IllegalArgumentException("SensorLog not specified");
        }
        if (uploadMetadata == null) {
            throw new IllegalArgumentException("UploadMetadata not specified");
        }
        cdr.setLogFileType(CloudDataResource.LogFileTypes.BANDBINARY);
        cdr.setUploadId(new Date());
        uploadMetadata.setStartSequenceId(log.getMeta().getStartingSeqNumber());
        uploadMetadata.setEndSequenceId(log.getMeta().getEndingSeqNumber());
        cdr.setMetaData(uploadMetadata);
        BandServiceMessage.Response response = BandServiceMessage.Response.SERVICE_CLOUD_OPERATION_FAILED_ERROR;
        try {
            BandServiceMessage.Response response2 = uploadBytesToCloud(log.getData(), cdr);
            return response2;
        } catch (CargoServiceException e) {
            BandServiceMessage.Response response3 = e.getResponse();
            KDKLog.e(TAG, "Upload Sensorlog to cloud failed with %s.", e.getMessage(), e);
            return response3;
        }
    }

    private long downloadContentToFile(File destFile, String... urls) throws CargoServiceException {
        long contentLength = 0;
        for (String url : urls) {
            if (!StringUtil.isNullOrEmpty(url)) {
                contentLength = downloadContentToFile(destFile, url);
                if (contentLength > 0) {
                    break;
                }
            }
        }
        return contentLength;
    }

    private long downloadContentToFile(File destFile, String url) throws CargoServiceException {
        OutputStream outStream;
        if (destFile == null) {
            throw new IllegalArgumentException("destFile not specified");
        }
        long contentLength = 0;
        HttpGet get = buildHttpGet(url);
        HttpResponse response = executeRequest(get, HTTP_DOWNLOAD_FILE_TIMEOUT, false);
        KDKLog.d(TAG, "Downloaded content from '%s', status: %s", url, response.getStatusLine());
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream inStream = null;
            OutputStream outStream2 = null;
            try {
                try {
                    inStream = entity.getContent();
                    outStream = new BufferedOutputStream(new FileOutputStream(destFile));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
                e = e;
            } catch (IllegalStateException e2) {
                e = e2;
            }
            try {
                contentLength = FileHelper.copyStreams(outStream, inStream);
                KDKLog.d(TAG, "Wrote downloaded content to %s, content length = %d", destFile, Long.valueOf(contentLength));
                StreamUtils.closeQuietly(inStream);
                StreamUtils.closeQuietly(outStream);
            } catch (IOException e3) {
                e = e3;
                throw new CargoServiceException(e.getMessage(), BandServiceMessage.Response.SERVICE_FILE_IO_ERROR);
            } catch (IllegalStateException e4) {
                e = e4;
                throw new CargoServiceException(e.getMessage(), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            } catch (Throwable th2) {
                th = th2;
                outStream2 = outStream;
                StreamUtils.closeQuietly(inStream);
                StreamUtils.closeQuietly(outStream2);
                throw th;
            }
        }
        return contentLength;
    }

    private HttpGet buildHttpGet(String url) throws CargoServiceException {
        HttpGet get = new HttpGet();
        try {
            get.setURI(new URI(url));
            return get;
        } catch (URISyntaxException e) {
            throw new CargoServiceException(String.format("Endpoint '%s' is malformed.", url), e, BandServiceMessage.Response.SERVICE_CLOUD_INVALID_URL_ERROR);
        }
    }

    private HttpGet buildHttpGetWithAuthentication(String url) throws CargoServiceException {
        HttpGet get = new HttpGet();
        try {
            get.setURI(new URI(url));
            get.setHeader("Authorization", this.mApiInfo.getAccessToken());
            return get;
        } catch (URISyntaxException e) {
            throw new CargoServiceException(String.format("Endpoint '%s' is malformed.", url), e, BandServiceMessage.Response.SERVICE_CLOUD_INVALID_URL_ERROR);
        }
    }

    private void addLanguageAndRegionHeaders(String localeName, HttpGet get) throws CargoServiceException {
        try {
            get.setHeader(HEADER_ACCEPTED_LANGUAGE, localeName);
            String[] languageThenRegion = localeName.split("-");
            get.setHeader(HEADER_REGION, languageThenRegion[1].toUpperCase());
        } catch (Exception e) {
            throw new CargoServiceException(e.getMessage(), e, BandServiceMessage.Response.DEVICE_DATA_ERROR);
        }
    }
}

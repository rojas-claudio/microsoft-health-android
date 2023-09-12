package com.microsoft.krestsdk.auth.credentials;

import android.content.Context;
import com.microsoft.band.service.crypto.CryptoException;
import com.microsoft.band.service.crypto.CryptoProvider;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class CredentialStoreImpl implements CredentialStore {
    private static final String ACCESS_TOKEN = "AcsToken";
    private static final String CREDENTIALS_FILE_NAME_FORMAT = "KCredentials_%s.json";
    private static final String EXPIRES_IN = "ExpiresIn";
    private static final String FUS_END_POINT = "FUSEndPoint";
    private static final String KDS_ADDRESS = "KdsAddress";
    private static final String K_ACCESS_TOKEN = "KAccessToken";
    private static final String LOGIN_URL = "LoginUrl";
    private static final String POD_ADDRESS = "PodAddress";
    private static final String PROFILE_CREATED_DATE = "CreatedOn";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String TAG = CredentialStoreImpl.class.getSimpleName();
    private static final String USER_ID = "UserId";
    private KCredential mCachedCredentials;
    private Context mContext;
    private CryptoProvider mCryptoProvider;
    private SettingsProvider mSettingsProvider;

    public CredentialStoreImpl(Context context, SettingsProvider settingsProvider, CryptoProvider cryptoProvider) {
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
        this.mCryptoProvider = cryptoProvider;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public synchronized KCredential getCredentials() {
        KCredential kCredential;
        BufferedReader reader;
        if (this.mCachedCredentials != null) {
            kCredential = this.mCachedCredentials;
        } else {
            FileInputStream inputStream = null;
            BufferedReader reader2 = null;
            try {
                try {
                    inputStream = this.mContext.openFileInput(getCredentialsFileName());
                    reader = new BufferedReader(new InputStreamReader(inputStream), 16384);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (CryptoException e) {
            } catch (FileNotFoundException e2) {
            } catch (IOException e3) {
            } catch (JSONException e4) {
            }
            try {
                StringBuilder builder = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    builder.append(line).append("\n");
                }
                JSONObject object = new JSONObject(this.mCryptoProvider.decrypt(builder.toString(), 0));
                String accessToken = object.getString(ACCESS_TOKEN);
                String refreshToken = object.getString(REFRESH_TOKEN);
                DateTime expiresIn = new DateTime(object.getLong(EXPIRES_IN));
                MsaCredential msaCredential = new MsaCredential(accessToken, refreshToken, expiresIn);
                String accessToken2 = object.getString(K_ACCESS_TOKEN);
                String endPoint = object.getString(POD_ADDRESS);
                String kdsUrl = object.getString(KDS_ADDRESS);
                String userId = object.getString(USER_ID);
                String loginUrl = object.getString(LOGIN_URL);
                String fusEndPoint = object.getString(FUS_END_POINT);
                String hnfEndPoint = object.getString("HnFEndPoint");
                String hnfQueryParameters = object.getString("HnFQueryParameters");
                String profileCreation = object.getString(PROFILE_CREATED_DATE);
                KdsCredential kdsCredential = new KdsCredential(accessToken2, endPoint, kdsUrl, userId, loginUrl, fusEndPoint, hnfEndPoint, hnfQueryParameters, profileCreation);
                this.mCachedCredentials = new KCredential(msaCredential, kdsCredential);
                kCredential = this.mCachedCredentials;
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader);
            } catch (CryptoException e5) {
                reader2 = reader;
                KLog.w(TAG, "Unable to decrypt credentials");
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader2);
                kCredential = null;
                return kCredential;
            } catch (FileNotFoundException e6) {
                reader2 = reader;
                KLog.w(TAG, "The Credentials file was not found");
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader2);
                kCredential = null;
                return kCredential;
            } catch (IOException e7) {
                reader2 = reader;
                KLog.w(TAG, "There was an I/O Exception while accessing the Credentials");
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader2);
                kCredential = null;
                return kCredential;
            } catch (JSONException e8) {
                reader2 = reader;
                KLog.w(TAG, "The Json Object is not valid");
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader2);
                kCredential = null;
                return kCredential;
            } catch (Throwable th2) {
                th = th2;
                reader2 = reader;
                StreamUtils.closeQuietly(inputStream);
                StreamUtils.closeQuietly(reader2);
                throw th;
            }
        }
        return kCredential;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public synchronized void setCredentials(KCredential credentials) {
        FileOutputStream outputStream = null;
        JSONObject object = new JSONObject();
        try {
            try {
                try {
                    if (credentials != null) {
                        MsaCredential msaCredential = credentials.getMsaCredential();
                        object.put(ACCESS_TOKEN, msaCredential.getAccessToken());
                        object.put(REFRESH_TOKEN, msaCredential.getRefreshToken());
                        object.put(EXPIRES_IN, msaCredential.getTokenExpiration().getMillis());
                        KdsCredential kdsCredential = credentials.getKdsCredential();
                        object.put(K_ACCESS_TOKEN, kdsCredential.getAccessToken());
                        object.put(POD_ADDRESS, kdsCredential.getEndPoint());
                        object.put(KDS_ADDRESS, kdsCredential.getKdsUrl());
                        object.put(USER_ID, kdsCredential.getUserId());
                        object.put(LOGIN_URL, kdsCredential.getLoginUrl());
                        object.put(FUS_END_POINT, kdsCredential.getFUSEndPoint());
                        object.put("HnFEndPoint", kdsCredential.getHnFEndPoint());
                        object.put("HnFQueryParameters", kdsCredential.getHnFQueryParameters());
                        object.put(PROFILE_CREATED_DATE, kdsCredential.getProfileCreationDate().toDateTimeAtStartOfDay());
                    } else {
                        KLog.w(TAG, "The credentials that where obtained are NULL");
                    }
                    outputStream = this.mContext.openFileOutput(getCredentialsFileName(), 0);
                    outputStream.write(this.mCryptoProvider.encrypt(object.toString(), 0).getBytes());
                    StreamUtils.closeQuietly(outputStream);
                } catch (JSONException e) {
                    KLog.e(TAG, "The Json Object is not valid");
                    StreamUtils.closeQuietly(outputStream);
                }
            } catch (FileNotFoundException e2) {
                KLog.e(TAG, "The Credentials file was not found");
                StreamUtils.closeQuietly(outputStream);
            }
        } catch (CryptoException e3) {
            KLog.e(TAG, "Unable to encrypt credentials before saving");
            StreamUtils.closeQuietly(outputStream);
        } catch (IOException e4) {
            KLog.e(TAG, "There was an I/O Exception while accessing the Credentials");
            StreamUtils.closeQuietly(outputStream);
        }
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public void deleteCredentials() {
        try {
            KLog.i(TAG, "The credentials have been deleted");
            this.mCachedCredentials = null;
            this.mContext.deleteFile(getCredentialsFileName());
        } catch (MalformedURLException e) {
            KLog.e(TAG, "The credentials were not deleted");
        }
    }

    private String getCredentialsFileName() throws MalformedURLException {
        URL authBaseUrl = new URL(this.mSettingsProvider.getAuthUrl());
        return String.format(Locale.US, CREDENTIALS_FILE_NAME_FORMAT, authBaseUrl.getHost().toLowerCase(Locale.US));
    }
}

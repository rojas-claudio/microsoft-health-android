package com.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.Session;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class TestSession extends Session {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String LOG_TAG = "FacebookSDK.TestSession";
    private static Map<String, TestAccount> appTestAccounts = null;
    private static final long serialVersionUID = 1;
    private static String testApplicationId;
    private static String testApplicationSecret;
    private final Mode mode;
    private final List<String> requestedPermissions;
    private final String sessionUniqueUserTag;
    private String testAccountId;
    private String testAccountUserName;
    private boolean wasAskedToExtendAccessToken;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum Mode {
        PRIVATE,
        SHARED;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Mode[] valuesCustom() {
            Mode[] valuesCustom = values();
            int length = valuesCustom.length;
            Mode[] modeArr = new Mode[length];
            System.arraycopy(valuesCustom, 0, modeArr, 0, length);
            return modeArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface TestAccount extends GraphObject {
        String getAccessToken();

        String getId();

        String getName();

        void setName(String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface TestAccountsResponse extends GraphObject {
        GraphObjectList<TestAccount> getData();
    }

    static {
        $assertionsDisabled = !TestSession.class.desiredAssertionStatus();
    }

    TestSession(Activity activity, List<String> permissions, TokenCachingStrategy tokenCachingStrategy, String sessionUniqueUserTag, Mode mode) {
        super(activity, testApplicationId, tokenCachingStrategy);
        Validate.notNull(permissions, "permissions");
        Validate.notNullOrEmpty(testApplicationId, "testApplicationId");
        Validate.notNullOrEmpty(testApplicationSecret, "testApplicationSecret");
        this.sessionUniqueUserTag = sessionUniqueUserTag;
        this.mode = mode;
        this.requestedPermissions = permissions;
    }

    public static TestSession createSessionWithPrivateUser(Activity activity, List<String> permissions) {
        return createTestSession(activity, permissions, Mode.PRIVATE, null);
    }

    public static TestSession createSessionWithSharedUser(Activity activity, List<String> permissions) {
        return createSessionWithSharedUser(activity, permissions, null);
    }

    public static TestSession createSessionWithSharedUser(Activity activity, List<String> permissions, String sessionUniqueUserTag) {
        return createTestSession(activity, permissions, Mode.SHARED, sessionUniqueUserTag);
    }

    public static synchronized String getTestApplicationId() {
        String str;
        synchronized (TestSession.class) {
            str = testApplicationId;
        }
        return str;
    }

    public static synchronized void setTestApplicationId(String applicationId) {
        synchronized (TestSession.class) {
            if (testApplicationId != null && !testApplicationId.equals(applicationId)) {
                throw new FacebookException("Can't have more than one test application ID");
            }
            testApplicationId = applicationId;
        }
    }

    public static synchronized String getTestApplicationSecret() {
        String str;
        synchronized (TestSession.class) {
            str = testApplicationSecret;
        }
        return str;
    }

    public static synchronized void setTestApplicationSecret(String applicationSecret) {
        synchronized (TestSession.class) {
            if (testApplicationSecret != null && !testApplicationSecret.equals(applicationSecret)) {
                throw new FacebookException("Can't have more than one test application secret");
            }
            testApplicationSecret = applicationSecret;
        }
    }

    public final String getTestUserId() {
        return this.testAccountId;
    }

    public final String getTestUserName() {
        return this.testAccountUserName;
    }

    private static synchronized TestSession createTestSession(Activity activity, List<String> permissions, Mode mode, String sessionUniqueUserTag) {
        TestSession testSession;
        synchronized (TestSession.class) {
            if (Utility.isNullOrEmpty(testApplicationId) || Utility.isNullOrEmpty(testApplicationSecret)) {
                throw new FacebookException("Must provide app ID and secret");
            }
            if (Utility.isNullOrEmpty(permissions)) {
                permissions = Arrays.asList("email", "publish_actions");
            }
            testSession = new TestSession(activity, permissions, new TestTokenCachingStrategy(null), sessionUniqueUserTag, mode);
        }
        return testSession;
    }

    private static synchronized void retrieveTestAccountsForAppIfNeeded() {
        synchronized (TestSession.class) {
            if (appTestAccounts == null) {
                appTestAccounts = new HashMap();
                Request.setDefaultBatchApplicationId(testApplicationId);
                Bundle parameters = new Bundle();
                parameters.putString("access_token", getAppAccessToken());
                Request requestTestUsers = new Request(null, "app/accounts/test-users", parameters, null);
                requestTestUsers.setBatchEntryName("testUsers");
                requestTestUsers.setBatchEntryOmitResultOnSuccess(false);
                Bundle testUserNamesParam = new Bundle();
                testUserNamesParam.putString("access_token", getAppAccessToken());
                testUserNamesParam.putString("ids", "{result=testUsers:$.data.*.id}");
                testUserNamesParam.putString("fields", WorkoutSummary.NAME);
                Request requestTestUserNames = new Request(null, "", testUserNamesParam, null);
                requestTestUserNames.setBatchEntryDependsOn("testUsers");
                List<Response> responses = Request.executeBatchAndWait(requestTestUsers, requestTestUserNames);
                if (responses == null || responses.size() != 2) {
                    throw new FacebookException("Unexpected number of results from TestUsers batch query");
                }
                TestAccountsResponse testAccountsResponse = (TestAccountsResponse) responses.get(0).getGraphObjectAs(TestAccountsResponse.class);
                GraphObjectList<TestAccount> testAccounts = testAccountsResponse.getData();
                GraphObject userAccountsMap = responses.get(1).getGraphObject();
                populateTestAccounts(testAccounts, userAccountsMap);
            }
        }
    }

    private static synchronized void populateTestAccounts(Collection<TestAccount> testAccounts, GraphObject userAccountsMap) {
        synchronized (TestSession.class) {
            for (TestAccount testAccount : testAccounts) {
                GraphUser testUser = (GraphUser) userAccountsMap.getPropertyAs(testAccount.getId(), GraphUser.class);
                testAccount.setName(testUser.getName());
                storeTestAccount(testAccount);
            }
        }
    }

    private static synchronized void storeTestAccount(TestAccount testAccount) {
        synchronized (TestSession.class) {
            appTestAccounts.put(testAccount.getId(), testAccount);
        }
    }

    private static synchronized TestAccount findTestAccountMatchingIdentifier(String identifier) {
        TestAccount testAccount;
        synchronized (TestSession.class) {
            retrieveTestAccountsForAppIfNeeded();
            Iterator<TestAccount> it = appTestAccounts.values().iterator();
            while (true) {
                if (it.hasNext()) {
                    testAccount = it.next();
                    if (testAccount.getName().contains(identifier)) {
                        break;
                    }
                } else {
                    testAccount = null;
                    break;
                }
            }
        }
        return testAccount;
    }

    @Override // com.facebook.Session
    public final String toString() {
        String superString = super.toString();
        return "{TestSession testUserId:" + this.testAccountId + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + superString + "}";
    }

    @Override // com.facebook.Session
    void authorize(Session.AuthorizationRequest request) {
        if (this.mode == Mode.PRIVATE) {
            createTestAccountAndFinishAuth();
        } else {
            findOrCreateSharedTestAccount();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.facebook.Session
    public void postStateChange(SessionState oldState, SessionState newState, Exception error) {
        String id = this.testAccountId;
        super.postStateChange(oldState, newState, error);
        if (newState.isClosed() && id != null && this.mode == Mode.PRIVATE) {
            deleteTestAccount(id, getAppAccessToken());
        }
    }

    boolean getWasAskedToExtendAccessToken() {
        return this.wasAskedToExtendAccessToken;
    }

    void forceExtendAccessToken(boolean forceExtendAccessToken) {
        AccessToken currentToken = getTokenInfo();
        setTokenInfo(new AccessToken(currentToken.getToken(), new Date(), currentToken.getPermissions(), currentToken.getDeclinedPermissions(), AccessTokenSource.TEST_USER, new Date(0L)));
        setLastAttemptedTokenExtendDate(new Date(0L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.facebook.Session
    public boolean shouldExtendAccessToken() {
        boolean result = super.shouldExtendAccessToken();
        this.wasAskedToExtendAccessToken = false;
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.facebook.Session
    public void extendAccessToken() {
        this.wasAskedToExtendAccessToken = true;
        super.extendAccessToken();
    }

    void fakeTokenRefreshAttempt() {
        setCurrentTokenRefreshRequest(new Session.TokenRefreshRequest());
    }

    static final String getAppAccessToken() {
        return String.valueOf(testApplicationId) + "|" + testApplicationSecret;
    }

    private void findOrCreateSharedTestAccount() {
        TestAccount testAccount = findTestAccountMatchingIdentifier(getSharedTestAccountIdentifier());
        if (testAccount != null) {
            finishAuthWithTestAccount(testAccount);
        } else {
            createTestAccountAndFinishAuth();
        }
    }

    private void finishAuthWithTestAccount(TestAccount testAccount) {
        this.testAccountId = testAccount.getId();
        this.testAccountUserName = testAccount.getName();
        AccessToken accessToken = AccessToken.createFromString(testAccount.getAccessToken(), this.requestedPermissions, AccessTokenSource.TEST_USER);
        finishAuthOrReauth(accessToken, null);
    }

    private TestAccount createTestAccountAndFinishAuth() {
        Bundle parameters = new Bundle();
        parameters.putString("installed", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        parameters.putString("permissions", getPermissionsString());
        parameters.putString("access_token", getAppAccessToken());
        if (this.mode == Mode.SHARED) {
            parameters.putString(WorkoutSummary.NAME, String.format("Shared %s Testuser", getSharedTestAccountIdentifier()));
        }
        String graphPath = String.format("%s/accounts/test-users", testApplicationId);
        Request createUserRequest = new Request(null, graphPath, parameters, HttpMethod.POST);
        Response response = createUserRequest.executeAndWait();
        FacebookRequestError error = response.getError();
        TestAccount testAccount = (TestAccount) response.getGraphObjectAs(TestAccount.class);
        if (error != null) {
            finishAuthOrReauth(null, error.getException());
            return null;
        } else if ($assertionsDisabled || testAccount != null) {
            if (this.mode == Mode.SHARED) {
                testAccount.setName(parameters.getString(WorkoutSummary.NAME));
                storeTestAccount(testAccount);
            }
            finishAuthWithTestAccount(testAccount);
            return testAccount;
        } else {
            throw new AssertionError();
        }
    }

    private void deleteTestAccount(String testAccountId, String appAccessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("access_token", appAccessToken);
        Request request = new Request(null, testAccountId, parameters, HttpMethod.DELETE);
        Response response = request.executeAndWait();
        FacebookRequestError error = response.getError();
        GraphObject graphObject = response.getGraphObject();
        if (error != null) {
            Log.w(LOG_TAG, String.format("Could not delete test account %s: %s", testAccountId, error.getException().toString()));
        } else if (graphObject.getProperty(Response.NON_JSON_RESPONSE_PROPERTY) == false || graphObject.getProperty(Response.SUCCESS_KEY) == false) {
            Log.w(LOG_TAG, String.format("Could not delete test account %s: unknown reason", testAccountId));
        }
    }

    private String getPermissionsString() {
        return TextUtils.join(",", this.requestedPermissions);
    }

    private String getSharedTestAccountIdentifier() {
        long permissionsHash = getPermissionsString().hashCode() & 4294967295L;
        long sessionTagHash = this.sessionUniqueUserTag != null ? this.sessionUniqueUserTag.hashCode() & 4294967295L : 0L;
        long combinedHash = permissionsHash ^ sessionTagHash;
        return validNameStringFromInteger(combinedHash);
    }

    private String validNameStringFromInteger(long i) {
        char[] charArray;
        String s = Long.toString(i);
        StringBuilder result = new StringBuilder("Perm");
        char lastChar = 0;
        for (char c : s.toCharArray()) {
            if (c == lastChar) {
                c = (char) (c + '\n');
            }
            result.append((char) ((c + 'a') - 48));
            lastChar = c;
        }
        return result.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class TestTokenCachingStrategy extends TokenCachingStrategy {
        private Bundle bundle;

        private TestTokenCachingStrategy() {
        }

        /* synthetic */ TestTokenCachingStrategy(TestTokenCachingStrategy testTokenCachingStrategy) {
            this();
        }

        @Override // com.facebook.TokenCachingStrategy
        public Bundle load() {
            return this.bundle;
        }

        @Override // com.facebook.TokenCachingStrategy
        public void save(Bundle value) {
            this.bundle = value;
        }

        @Override // com.facebook.TokenCachingStrategy
        public void clear() {
            this.bundle = null;
        }
    }
}

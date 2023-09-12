package com.facebook;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
/* loaded from: classes.dex */
public class Response {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    private static final int INVALID_SESSION_FACEBOOK_ERROR_CODE = 190;
    public static final String NON_JSON_RESPONSE_PROPERTY = "FACEBOOK_NON_JSON_RESULT";
    private static final String RESPONSE_CACHE_TAG = "ResponseCache";
    private static final String RESPONSE_LOG_TAG = "Response";
    public static final String SUCCESS_KEY = "success";
    private static FileLruCache responseCache;
    private final HttpURLConnection connection;
    private final FacebookRequestError error;
    private final GraphObject graphObject;
    private final GraphObjectList<GraphObject> graphObjectList;
    private final boolean isFromCache;
    private final String rawResponse;
    private final Request request;

    /* loaded from: classes.dex */
    interface PagedResults extends GraphObject {
        GraphObjectList<GraphObject> getData();

        PagingInfo getPaging();
    }

    /* loaded from: classes.dex */
    public enum PagingDirection {
        NEXT,
        PREVIOUS;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static PagingDirection[] valuesCustom() {
            PagingDirection[] valuesCustom = values();
            int length = valuesCustom.length;
            PagingDirection[] pagingDirectionArr = new PagingDirection[length];
            System.arraycopy(valuesCustom, 0, pagingDirectionArr, 0, length);
            return pagingDirectionArr;
        }
    }

    /* loaded from: classes.dex */
    interface PagingInfo extends GraphObject {
        String getNext();

        String getPrevious();
    }

    static {
        $assertionsDisabled = !Response.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Response(Request request, HttpURLConnection connection, String rawResponse, GraphObject graphObject, boolean isFromCache) {
        this(request, connection, rawResponse, graphObject, null, isFromCache, null);
    }

    Response(Request request, HttpURLConnection connection, String rawResponse, GraphObjectList<GraphObject> graphObjects, boolean isFromCache) {
        this(request, connection, rawResponse, null, graphObjects, isFromCache, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Response(Request request, HttpURLConnection connection, FacebookRequestError error) {
        this(request, connection, null, null, null, false, error);
    }

    Response(Request request, HttpURLConnection connection, String rawResponse, GraphObject graphObject, GraphObjectList<GraphObject> graphObjects, boolean isFromCache, FacebookRequestError error) {
        this.request = request;
        this.connection = connection;
        this.rawResponse = rawResponse;
        this.graphObject = graphObject;
        this.graphObjectList = graphObjects;
        this.isFromCache = isFromCache;
        this.error = error;
    }

    public final FacebookRequestError getError() {
        return this.error;
    }

    public final GraphObject getGraphObject() {
        return this.graphObject;
    }

    public final <T extends GraphObject> T getGraphObjectAs(Class<T> graphObjectClass) {
        if (this.graphObject == null) {
            return null;
        }
        if (graphObjectClass == null) {
            throw new NullPointerException("Must pass in a valid interface that extends GraphObject");
        }
        return (T) this.graphObject.cast(graphObjectClass);
    }

    public final GraphObjectList<GraphObject> getGraphObjectList() {
        return this.graphObjectList;
    }

    public final <T extends GraphObject> GraphObjectList<T> getGraphObjectListAs(Class<T> graphObjectClass) {
        if (this.graphObjectList == null) {
            return null;
        }
        return (GraphObjectList<T>) this.graphObjectList.castToListOf(graphObjectClass);
    }

    public final HttpURLConnection getConnection() {
        return this.connection;
    }

    public Request getRequest() {
        return this.request;
    }

    public String getRawResponse() {
        return this.rawResponse;
    }

    public Request getRequestForPagedResults(PagingDirection direction) {
        String link = null;
        if (this.graphObject != null) {
            PagedResults pagedResults = (PagedResults) this.graphObject.cast(PagedResults.class);
            PagingInfo pagingInfo = pagedResults.getPaging();
            if (pagingInfo != null) {
                link = direction == PagingDirection.NEXT ? pagingInfo.getNext() : pagingInfo.getPrevious();
            }
        }
        if (Utility.isNullOrEmpty(link)) {
            return null;
        }
        if (link == null || !link.equals(this.request.getUrlForSingleRequest())) {
            try {
                return new Request(this.request.getSession(), new URL(link));
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
    }

    public String toString() {
        String responseCode;
        try {
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(this.connection != null ? this.connection.getResponseCode() : 200);
            responseCode = String.format("%d", objArr);
        } catch (IOException e) {
            responseCode = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return "{Response:  responseCode: " + responseCode + ", graphObject: " + this.graphObject + ", error: " + this.error + ", isFromCache:" + this.isFromCache + "}";
    }

    public final boolean getIsFromCache() {
        return this.isFromCache;
    }

    static FileLruCache getResponseCache() {
        Context applicationContext;
        if (responseCache == null && (applicationContext = Session.getStaticContext()) != null) {
            responseCache = new FileLruCache(applicationContext, RESPONSE_CACHE_TAG, new FileLruCache.Limits());
        }
        return responseCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(9:1|(4:3|(2:5|(1:7)(1:8))|9|(3:14|15|(3:17|18|19)(1:22)))|32|33|(1:35)(2:39|(1:45))|36|37|19|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0090, code lost:
        r4 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0091, code lost:
        com.facebook.internal.Logger.log(com.facebook.LoggingBehavior.REQUESTS, com.facebook.Response.RESPONSE_LOG_TAG, "Response <Error>: %s", r4);
        r7 = constructErrorResponses(r13, r12, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00aa, code lost:
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ab, code lost:
        com.facebook.internal.Logger.log(com.facebook.LoggingBehavior.REQUESTS, com.facebook.Response.RESPONSE_LOG_TAG, "Response <Error>: %s", r3);
        r7 = constructErrorResponses(r13, r12, new com.facebook.FacebookException(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ca, code lost:
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00cb, code lost:
        com.facebook.internal.Logger.log(com.facebook.LoggingBehavior.REQUESTS, com.facebook.Response.RESPONSE_LOG_TAG, "Response <Error>: %s", r3);
        r7 = constructErrorResponses(r13, r12, new com.facebook.FacebookException(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00ea, code lost:
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00eb, code lost:
        com.facebook.internal.Logger.log(com.facebook.LoggingBehavior.REQUESTS, com.facebook.Response.RESPONSE_LOG_TAG, "Response <Error>: %s", r3);
        r7 = constructErrorResponses(r13, r12, new com.facebook.FacebookException(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x010a, code lost:
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x010e, code lost:
        throw r7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.facebook.Response> fromHttpConnection(java.net.HttpURLConnection r12, com.facebook.RequestBatch r13) {
        /*
            Method dump skipped, instructions count: 271
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.Response.fromHttpConnection(java.net.HttpURLConnection, com.facebook.RequestBatch):java.util.List");
    }

    static List<Response> createResponsesFromStream(InputStream stream, HttpURLConnection connection, RequestBatch requests, boolean isFromCache) throws FacebookException, JSONException, IOException {
        String responseString = Utility.readStreamToString(stream);
        Logger.log(LoggingBehavior.INCLUDE_RAW_RESPONSES, RESPONSE_LOG_TAG, "Response (raw)\n  Size: %d\n  Response:\n%s\n", Integer.valueOf(responseString.length()), responseString);
        return createResponsesFromString(responseString, connection, requests, isFromCache);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Response> createResponsesFromString(String responseString, HttpURLConnection connection, RequestBatch requests, boolean isFromCache) throws FacebookException, JSONException, IOException {
        JSONTokener tokener = new JSONTokener(responseString);
        Object resultObject = tokener.nextValue();
        List<Response> responses = createResponsesFromObject(connection, requests, resultObject, isFromCache);
        Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response\n  Id: %s\n  Size: %d\n  Responses:\n%s\n", requests.getId(), Integer.valueOf(responseString.length()), responses);
        return responses;
    }

    private static List<Response> createResponsesFromObject(HttpURLConnection connection, List<Request> requests, Object object, boolean isFromCache) throws FacebookException, JSONException {
        if ($assertionsDisabled || connection != null || isFromCache) {
            int numRequests = requests.size();
            List<Response> responses = new ArrayList<>(numRequests);
            if (numRequests == 1) {
                Request request = requests.get(0);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("body", object);
                    int responseCode = connection != null ? connection.getResponseCode() : 200;
                    jsonObject.put(CODE_KEY, responseCode);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    object = jsonArray;
                } catch (IOException e) {
                    responses.add(new Response(request, connection, new FacebookRequestError(connection, e)));
                } catch (JSONException e2) {
                    responses.add(new Response(request, connection, new FacebookRequestError(connection, e2)));
                }
            }
            if (!(object instanceof JSONArray) || ((JSONArray) object).length() != numRequests) {
                FacebookException exception = new FacebookException("Unexpected number of results");
                throw exception;
            }
            JSONArray jsonArray2 = (JSONArray) object;
            for (int i = 0; i < jsonArray2.length(); i++) {
                Request request2 = requests.get(i);
                try {
                    Object obj = jsonArray2.get(i);
                    responses.add(createResponseFromObject(request2, connection, obj, isFromCache, object));
                } catch (FacebookException e3) {
                    responses.add(new Response(request2, connection, new FacebookRequestError(connection, e3)));
                } catch (JSONException e4) {
                    responses.add(new Response(request2, connection, new FacebookRequestError(connection, e4)));
                }
            }
            return responses;
        }
        throw new AssertionError();
    }

    private static Response createResponseFromObject(Request request, HttpURLConnection connection, Object object, boolean isFromCache, Object originalResult) throws JSONException {
        Session session;
        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            FacebookRequestError error = FacebookRequestError.checkResponseAndCreateError(jsonObject, originalResult, connection);
            if (error != null) {
                if (error.getErrorCode() == INVALID_SESSION_FACEBOOK_ERROR_CODE && (session = request.getSession()) != null) {
                    session.closeAndClearTokenInformation();
                }
                return new Response(request, connection, error);
            }
            Object body = Utility.getStringPropertyAsJSON(jsonObject, "body", NON_JSON_RESPONSE_PROPERTY);
            if (body instanceof JSONObject) {
                GraphObject graphObject = GraphObject.Factory.create((JSONObject) body);
                return new Response(request, connection, body.toString(), graphObject, isFromCache);
            } else if (body instanceof JSONArray) {
                GraphObjectList<GraphObject> graphObjectList = GraphObject.Factory.createList((JSONArray) body, GraphObject.class);
                return new Response(request, connection, body.toString(), graphObjectList, isFromCache);
            } else {
                object = JSONObject.NULL;
            }
        }
        if (object == JSONObject.NULL) {
            return new Response(request, connection, object.toString(), (GraphObject) null, isFromCache);
        }
        throw new FacebookException("Got unexpected object type in response, class: " + object.getClass().getSimpleName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Response> constructErrorResponses(List<Request> requests, HttpURLConnection connection, FacebookException error) {
        int count = requests.size();
        List<Response> responses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Response response = new Response(requests.get(i), connection, new FacebookRequestError(connection, error));
            responses.add(response);
        }
        return responses;
    }
}

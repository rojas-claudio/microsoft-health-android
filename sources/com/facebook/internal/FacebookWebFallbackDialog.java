package com.facebook.internal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.facebook.FacebookException;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FacebookWebFallbackDialog extends WebDialog {
    private static final String TAG = FacebookWebFallbackDialog.class.getName();

    public static boolean presentWebFallback(final Context context, String dialogUrl, String applicationId, final FacebookDialog.PendingCall appCall, final FacebookDialog.Callback callback) {
        if (Utility.isNullOrEmpty(dialogUrl)) {
            return false;
        }
        String redirectUrl = String.format("fb%s://bridge/", applicationId);
        FacebookWebFallbackDialog fallbackWebDialog = new FacebookWebFallbackDialog(context, dialogUrl, redirectUrl);
        fallbackWebDialog.setOnCompleteListener(new WebDialog.OnCompleteListener() { // from class: com.facebook.internal.FacebookWebFallbackDialog.1
            @Override // com.facebook.widget.WebDialog.OnCompleteListener
            public void onComplete(Bundle values, FacebookException error) {
                Intent dummyIntent = new Intent();
                if (values == null) {
                    values = new Bundle();
                }
                dummyIntent.putExtras(values);
                FacebookDialog.handleActivityResult(context, appCall, appCall.getRequestCode(), dummyIntent, callback);
            }
        });
        fallbackWebDialog.show();
        return true;
    }

    private FacebookWebFallbackDialog(Context context, String url, String expectedRedirectUrl) {
        super(context, url);
        setExpectedRedirectUrl(expectedRedirectUrl);
    }

    @Override // com.facebook.widget.WebDialog
    protected Bundle parseResponseUri(String url) {
        Uri responseUri = Uri.parse(url);
        Bundle queryParams = Utility.parseUrlQueryString(responseUri.getQuery());
        String bridgeArgsJSONString = queryParams.getString(ServerProtocol.FALLBACK_DIALOG_PARAM_BRIDGE_ARGS);
        queryParams.remove(ServerProtocol.FALLBACK_DIALOG_PARAM_BRIDGE_ARGS);
        if (!Utility.isNullOrEmpty(bridgeArgsJSONString)) {
            try {
                JSONObject bridgeArgsJSON = new JSONObject(bridgeArgsJSONString);
                Bundle bridgeArgs = BundleJSONConverter.convertToBundle(bridgeArgsJSON);
                queryParams.putBundle(NativeProtocol.EXTRA_PROTOCOL_BRIDGE_ARGS, bridgeArgs);
            } catch (JSONException je) {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", je);
            }
        }
        String methodResultsJSONString = queryParams.getString(ServerProtocol.FALLBACK_DIALOG_PARAM_METHOD_RESULTS);
        queryParams.remove(ServerProtocol.FALLBACK_DIALOG_PARAM_METHOD_RESULTS);
        if (!Utility.isNullOrEmpty(methodResultsJSONString)) {
            if (Utility.isNullOrEmpty(methodResultsJSONString)) {
                methodResultsJSONString = "{}";
            }
            try {
                JSONObject methodArgsJSON = new JSONObject(methodResultsJSONString);
                Bundle methodResults = BundleJSONConverter.convertToBundle(methodArgsJSON);
                queryParams.putBundle(NativeProtocol.EXTRA_PROTOCOL_METHOD_RESULTS, methodResults);
            } catch (JSONException je2) {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", je2);
            }
        }
        queryParams.remove(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION);
        queryParams.putInt(NativeProtocol.EXTRA_PROTOCOL_VERSION, NativeProtocol.getLatestKnownVersion());
        return queryParams;
    }
}

package net.hockeyapp.android.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.facebook.Response;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.unnamed.b.atv.model.TreeNode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.utils.Base64;
import net.hockeyapp.android.utils.ConnectionManager;
import net.hockeyapp.android.utils.PrefsUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class LoginTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private Handler handler;
    private final int mode;
    private final Map<String, String> params;
    private ProgressDialog progressDialog;
    private boolean showProgressDialog = true;
    private final String urlString;

    public LoginTask(Context context, Handler handler, String urlString, int mode, Map<String, String> params) {
        this.context = context;
        this.handler = handler;
        this.urlString = urlString;
        this.mode = mode;
        this.params = params;
        if (context != null) {
            Constants.loadFromContext(context);
        }
    }

    public void setShowProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
    }

    public void attach(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void detach() {
        this.context = null;
        this.handler = null;
        this.progressDialog = null;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        if ((this.progressDialog == null || !this.progressDialog.isShowing()) && this.showProgressDialog) {
            this.progressDialog = ProgressDialog.show(this.context, "", "Please wait...", true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... args) {
        HttpClient httpClient = ConnectionManager.getInstance().getHttpClient();
        try {
            HttpUriRequest httpUriRequest = makeRequest(this.mode, this.params);
            HttpResponse response = httpClient.execute(httpUriRequest);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                String responseStr = EntityUtils.toString(resEntity);
                if (!TextUtils.isEmpty(responseStr)) {
                    return Boolean.valueOf(handleResponse(responseStr));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean success) {
        if (this.progressDialog != null) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.handler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Response.SUCCESS_KEY, success.booleanValue());
            msg.setData(bundle);
            this.handler.sendMessage(msg);
        }
    }

    private HttpUriRequest makeRequest(int mode, Map<String, String> params) throws UnsupportedEncodingException {
        if (mode == 1) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            form.setContentEncoding("UTF-8");
            HttpPost httpPost = new HttpPost(this.urlString);
            httpPost.setEntity(form);
            return httpPost;
        } else if (mode == 2) {
            String email = params.get("email");
            String password = params.get("password");
            String authStr = "Basic " + Base64.encodeToString((email + TreeNode.NODES_ID_SEPARATOR + password).getBytes(), 2);
            HttpPost httpPost2 = new HttpPost(this.urlString);
            httpPost2.setHeader(com.microsoft.kapp.utils.Constants.AUTHORIZATION_HEADER_NAME, authStr);
            return httpPost2;
        } else if (mode == 3) {
            String type = params.get("type");
            String id = params.get("id");
            String paramUrl = this.urlString + "?" + type + SimpleComparison.EQUAL_TO_OPERATION + id;
            HttpGet httpGet = new HttpGet(paramUrl);
            return httpGet;
        } else {
            throw new IllegalArgumentException("Login mode " + mode + " not supported.");
        }
    }

    private boolean handleResponse(String responseStr) {
        SharedPreferences prefs = this.context.getSharedPreferences("net.hockeyapp.android.login", 0);
        try {
            JSONObject response = new JSONObject(responseStr);
            String status = response.getString("status");
            if (TextUtils.isEmpty(status)) {
                return false;
            }
            if (this.mode == 1) {
                if (status.equals("identified")) {
                    String iuid = response.getString("iuid");
                    if (TextUtils.isEmpty(iuid)) {
                        return false;
                    }
                    PrefsUtil.applyChanges(prefs.edit().putString("iuid", iuid));
                    return true;
                }
                return false;
            } else if (this.mode == 2) {
                if (status.equals("authorized")) {
                    String auid = response.getString("auid");
                    if (TextUtils.isEmpty(auid)) {
                        return false;
                    }
                    PrefsUtil.applyChanges(prefs.edit().putString("auid", auid));
                    return true;
                }
                return false;
            } else if (this.mode == 3) {
                if (status.equals("validated")) {
                    return true;
                }
                PrefsUtil.applyChanges(prefs.edit().remove("iuid").remove("auid"));
                return false;
            } else {
                throw new IllegalArgumentException("Login mode " + this.mode + " not supported.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}

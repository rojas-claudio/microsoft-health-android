package net.hockeyapp.android.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.EnvironmentCompat;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.telephony.MmsColumns;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.hockeyapp.android.Constants;
import net.hockeyapp.android.utils.ConnectionManager;
import net.hockeyapp.android.utils.SimpleMultipartEntity;
import net.hockeyapp.android.utils.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/* loaded from: classes.dex */
public class SendFeedbackTask extends AsyncTask<Void, Void, HashMap<String, String>> {
    private List<Uri> attachmentUris;
    private Context context;
    private String email;
    private Handler handler;
    private boolean isFetchMessages;
    private String name;
    private ProgressDialog progressDialog;
    private String subject;
    private String text;
    private String token;
    private String urlString;
    private boolean showProgressDialog = true;
    private int lastMessageId = -1;

    public SendFeedbackTask(Context context, String urlString, String name, String email, String subject, String text, List<Uri> attachmentUris, String token, Handler handler, boolean isFetchMessages) {
        this.context = context;
        this.urlString = urlString;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.text = text;
        this.attachmentUris = attachmentUris;
        this.token = token;
        this.handler = handler;
        this.isFetchMessages = isFetchMessages;
        if (context != null) {
            Constants.loadFromContext(context);
        }
    }

    public void setShowProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
    }

    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void attach(Context context) {
        this.context = context;
    }

    public void detach() {
        this.context = null;
        this.progressDialog = null;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        String loadingMessage = "Sending feedback..";
        if (this.isFetchMessages) {
            loadingMessage = "Retrieving discussions...";
        }
        if ((this.progressDialog == null || !this.progressDialog.isShowing()) && this.showProgressDialog) {
            this.progressDialog = ProgressDialog.show(this.context, "", loadingMessage, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public HashMap<String, String> doInBackground(Void... args) {
        File[] listFiles;
        HttpClient httpclient = ConnectionManager.getInstance().getHttpClient();
        if (this.isFetchMessages && this.token != null) {
            return doGet(httpclient);
        }
        if (!this.isFetchMessages) {
            if (this.attachmentUris.isEmpty()) {
                return doPostPut(httpclient);
            }
            HashMap<String, String> result = doPostPutWithAttachments(httpclient);
            String status = result.get("status");
            if (status != null && status.startsWith("2") && this.context != null) {
                File folder = new File(this.context.getCacheDir(), "HockeyApp");
                if (folder.exists()) {
                    for (File file : folder.listFiles()) {
                        file.delete();
                    }
                    return result;
                }
                return result;
            }
            return result;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(HashMap<String, String> result) {
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
            if (result != null) {
                bundle.putString("request_type", result.get("type"));
                bundle.putString("feedback_response", result.get("response"));
                bundle.putString("feedback_status", result.get("status"));
            } else {
                bundle.putString("request_type", EnvironmentCompat.MEDIA_UNKNOWN);
            }
            msg.setData(bundle);
            this.handler.sendMessage(msg);
        }
    }

    private HashMap<String, String> doPostPut(HttpClient httpClient) {
        HashMap<String, String> result = new HashMap<>();
        result.put("type", "send");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair(WorkoutSummary.NAME, this.name));
            nameValuePairs.add(new BasicNameValuePair("email", this.email));
            nameValuePairs.add(new BasicNameValuePair("subject", this.subject));
            nameValuePairs.add(new BasicNameValuePair(MmsColumns.MMS_PART_TEXT, this.text));
            nameValuePairs.add(new BasicNameValuePair("bundle_identifier", Constants.APP_PACKAGE));
            nameValuePairs.add(new BasicNameValuePair("bundle_short_version", Constants.APP_VERSION_NAME));
            nameValuePairs.add(new BasicNameValuePair("bundle_version", Constants.APP_VERSION));
            nameValuePairs.add(new BasicNameValuePair("os_version", Constants.ANDROID_VERSION));
            nameValuePairs.add(new BasicNameValuePair("oem", Constants.PHONE_MANUFACTURER));
            nameValuePairs.add(new BasicNameValuePair("model", Constants.PHONE_MODEL));
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            form.setContentEncoding("UTF-8");
            HttpPost httpPost = null;
            HttpPut httpPut = null;
            if (this.token != null) {
                this.urlString += this.token + "/";
                httpPut = new HttpPut(this.urlString);
            } else {
                httpPost = new HttpPost(this.urlString);
            }
            HttpResponse response = null;
            if (httpPut != null) {
                httpPut.setEntity(form);
                response = httpClient.execute(httpPut);
            } else if (httpPost != null) {
                httpPost.setEntity(form);
                response = httpClient.execute(httpPost);
            }
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                result.put("response", EntityUtils.toString(resEntity));
                result.put("status", "" + response.getStatusLine().getStatusCode());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ClientProtocolException e3) {
            e3.printStackTrace();
        }
        return result;
    }

    private HashMap<String, String> doPostPutWithAttachments(HttpClient httpClient) {
        HashMap<String, String> result = new HashMap<>();
        result.put("type", "send");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair(WorkoutSummary.NAME, this.name));
            nameValuePairs.add(new BasicNameValuePair("email", this.email));
            nameValuePairs.add(new BasicNameValuePair("subject", this.subject));
            nameValuePairs.add(new BasicNameValuePair(MmsColumns.MMS_PART_TEXT, this.text));
            nameValuePairs.add(new BasicNameValuePair("bundle_identifier", Constants.APP_PACKAGE));
            nameValuePairs.add(new BasicNameValuePair("bundle_short_version", Constants.APP_VERSION_NAME));
            nameValuePairs.add(new BasicNameValuePair("bundle_version", Constants.APP_VERSION));
            nameValuePairs.add(new BasicNameValuePair("os_version", Constants.ANDROID_VERSION));
            nameValuePairs.add(new BasicNameValuePair("oem", Constants.PHONE_MANUFACTURER));
            nameValuePairs.add(new BasicNameValuePair("model", Constants.PHONE_MODEL));
            SimpleMultipartEntity entity = new SimpleMultipartEntity();
            entity.writeFirstBoundaryIfNeeds();
            for (NameValuePair pair : nameValuePairs) {
                entity.addPart(pair.getName(), pair.getValue());
            }
            int i = 0;
            while (i < this.attachmentUris.size()) {
                Uri attachmentUri = this.attachmentUris.get(i);
                boolean lastFile = i == this.attachmentUris.size() + (-1);
                InputStream input = this.context.getContentResolver().openInputStream(attachmentUri);
                String filename = attachmentUri.getLastPathSegment();
                entity.addPart("attachment" + i, filename, input, lastFile);
                i++;
            }
            entity.writeLastBoundaryIfNeeds();
            HttpPost httpPost = null;
            HttpPut httpPut = null;
            if (this.token != null) {
                this.urlString += this.token + "/";
                httpPut = new HttpPut(this.urlString);
            } else {
                httpPost = new HttpPost(this.urlString);
            }
            HttpResponse response = null;
            if (httpPut != null) {
                httpPut.setHeader("Content-type", "multipart/form-data; boundary=" + entity.getBoundary());
                httpPut.setEntity(entity);
                response = httpClient.execute(httpPut);
            } else if (httpPost != null) {
                httpPost.setHeader("Content-type", "multipart/form-data; boundary=" + entity.getBoundary());
                httpPost.setEntity(entity);
                response = httpClient.execute(httpPost);
            }
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                result.put("response", EntityUtils.toString(resEntity));
                result.put("status", "" + response.getStatusLine().getStatusCode());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return result;
    }

    private HashMap<String, String> doGet(HttpClient httpClient) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.urlString + Util.encodeParam(this.token));
        if (this.lastMessageId != -1) {
            sb.append("?last_message_id=" + this.lastMessageId);
        }
        HttpGet httpGet = new HttpGet(sb.toString());
        HashMap<String, String> result = new HashMap<>();
        result.put("type", "fetch");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            result.put("response", EntityUtils.toString(responseEntity));
            result.put("status", "" + response.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
        }
        return result;
    }
}

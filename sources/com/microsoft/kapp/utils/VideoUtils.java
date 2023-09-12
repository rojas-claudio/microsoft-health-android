package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.DialogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/* loaded from: classes.dex */
public class VideoUtils {
    public static void playVideo(String url, Context context, Callback<Void> callback) {
        AtomicBoolean loading = new AtomicBoolean(false);
        if (CommonUtils.isNetworkAvailable(context)) {
            if (loading.compareAndSet(false, true)) {
                WindowManager wm = (WindowManager) context.getSystemService("window");
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                playVideo(url, width, height, context, callback);
                return;
            }
            return;
        }
        DialogManagerImpl.getDialogManager(context).showNetworkErrorDialog(context);
    }

    public static void playVideo(String url, double screenWidth, double screenHeight, Context context, Callback<Void> callback) {
        FetchVideo task = new FetchVideo(url, screenWidth, screenHeight, context, callback);
        task.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VideoFetcher {
        private VideoFetcher() {
        }

        public String getUrlOfVideoWithSize(String url, double screenWidth, double screenHeight) throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
            BufferedReader in;
            String videoUrl = null;
            String lastResortVideoUrl = null;
            String videoUrlFormatCode101MP4 = null;
            BufferedReader in2 = null;
            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                HttpResponse response = httpclient.execute(httpget);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (Throwable th) {
                th = th;
            }
            try {
                StringBuilder sb = new StringBuilder();
                String NL = System.getProperty("line.separator");
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line).append(NL);
                }
                String result = sb.toString();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));
                Document doc = db.parse(is);
                NodeList list = doc.getElementsByTagName("videoFiles");
                int formatCode = -1;
                double width = Double.MAX_VALUE;
                double height = Double.MAX_VALUE;
                for (int i = 0; i < list.getLength(); i++) {
                    Node n = list.item(i);
                    int j = 0;
                    while (true) {
                        if (j < n.getChildNodes().getLength()) {
                            Node videoNode = n.getChildNodes().item(j);
                            Node nodeWidth = videoNode.getAttributes().getNamedItem("width");
                            Node nodeHeight = videoNode.getAttributes().getNamedItem("height");
                            if (nodeWidth != null && nodeHeight != null) {
                                width = Double.parseDouble(nodeWidth.getNodeValue());
                                height = Double.parseDouble(nodeHeight.getNodeValue());
                            }
                            Node nodeFormatCode = videoNode.getAttributes().getNamedItem("formatCode");
                            if (nodeFormatCode != null) {
                                formatCode = Integer.parseInt(nodeFormatCode.getNodeValue());
                            }
                            if (width <= screenWidth && height <= screenHeight) {
                                videoUrl = videoNode.getChildNodes().item(0).getTextContent();
                                break;
                            }
                            if (101 == formatCode) {
                                videoUrlFormatCode101MP4 = videoNode.getChildNodes().item(0).getTextContent();
                            } else if (lastResortVideoUrl == null) {
                                lastResortVideoUrl = videoNode.getChildNodes().item(0).getTextContent();
                            }
                            j++;
                        }
                    }
                }
                StreamUtils.closeQuietly(in);
                if (videoUrl == null) {
                    if (videoUrlFormatCode101MP4 != null) {
                        String videoUrl2 = videoUrlFormatCode101MP4;
                        return videoUrl2;
                    }
                    if (lastResortVideoUrl == null) {
                        lastResortVideoUrl = "";
                    }
                    String videoUrl3 = lastResortVideoUrl;
                    return videoUrl3;
                }
                return videoUrl;
            } catch (Throwable th2) {
                th = th2;
                in2 = in;
                StreamUtils.closeQuietly(in2);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FetchVideo extends AsyncTask<Void, Void, String> {
        Context context;
        double height;
        Callback<Void> mCallback;
        Exception mException;
        String url;
        double width;

        public FetchVideo(String url, double screenWidth, double screenHeight, Context context) {
            this.url = url;
            this.width = screenWidth;
            this.height = screenHeight;
            this.context = context;
        }

        public FetchVideo(String url, double screenWidth, double screenHeight, Context context, Callback<Void> callback) {
            this.url = url;
            this.width = screenWidth;
            this.height = screenHeight;
            this.context = context;
            this.mCallback = callback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(Void... params) {
            try {
                String videoUrl = new VideoFetcher().getUrlOfVideoWithSize(this.url, this.width, this.height);
                return videoUrl;
            } catch (Exception exception) {
                this.mException = exception;
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String result) {
            if (this.mException != null || TextUtils.isEmpty(result)) {
                VideoUtils.showVideoFetchingError(this.context);
                if (this.mCallback != null) {
                    this.mCallback.onError(this.mException);
                    return;
                }
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri data = Uri.parse(result);
            intent.setData(data);
            intent.setDataAndType(data, "video/mp4");
            this.context.startActivity(intent);
            if (this.mCallback != null) {
                this.mCallback.callback(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showVideoFetchingError(Context context) {
        DialogManager.showDialog(context, Integer.valueOf((int) R.string.error_fetching_video), Integer.valueOf((int) R.string.error_offline), DialogManager.Priority.LOW);
    }
}

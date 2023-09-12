package com.microsoft.kapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.GsonBuilder;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.models.MeasuredEvent;
import com.microsoft.krestsdk.models.MeasuredEventMapPoint;
import java.util.Locale;
/* loaded from: classes.dex */
public class BingMapView extends WebView {
    private static final String BING_MAP_HTML_LOCAL_URL = "file:///android_asset/BingMap.html";
    private static final int DEFAULT_TOLERANCE = 20;
    private boolean mDisableTouchEvents;
    private float pointX;
    private float pointY;
    private final int tolerance;

    /* loaded from: classes.dex */
    public interface BingMapListener {
        void onMapEmpty();

        void onMapLoaded();
    }

    public BingMapView(Context context) {
        super(context);
        this.mDisableTouchEvents = true;
        this.tolerance = (int) (20.0f * getResources().getDisplayMetrics().density);
    }

    public BingMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDisableTouchEvents = true;
        this.tolerance = (int) (20.0f * getResources().getDisplayMetrics().density);
    }

    public BingMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDisableTouchEvents = true;
        this.tolerance = (int) (20.0f * getResources().getDisplayMetrics().density);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void loadData(BingMapListener listener, final MeasuredEvent event, final boolean shouldHideMarkers, int customSplit, final boolean ignoreGpsLoss, final boolean enableFiltering) {
        Validate.notNull(event, "event");
        final int split = customSplit < 1 ? 1 : customSplit;
        WebSettings websettings = getSettings();
        websettings.setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient() { // from class: com.microsoft.kapp.views.BingMapView.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                BingMapView.this.initMap(event, shouldHideMarkers, split, ignoreGpsLoss, enableFiltering);
            }
        });
        if (listener != null) {
            addJavascriptInterface(new BingMapInterface(listener), "android");
        }
        loadUrl(BING_MAP_HTML_LOCAL_URL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMap(MeasuredEvent event, boolean shouldHideMarkers, int customSplit, boolean ignoreGpsLoss, boolean enableFiltering) {
        MeasuredEventMapPoint[] mapPoints = event.getMapPoints();
        String serializedMapPoints = new GsonBuilder().create().toJson(mapPoints);
        String functionCall = String.format("javascript:initAndDrawPath(%s, \"%s\", %s, %d, %s, %s);", serializedMapPoints, Locale.getDefault(), String.valueOf(shouldHideMarkers), Integer.valueOf(customSplit), String.valueOf(ignoreGpsLoss), String.valueOf(enableFiltering));
        loadUrl(functionCall);
    }

    public void enableTouchEvents() {
        this.mDisableTouchEvents = false;
    }

    public void disableTouchEvents() {
        this.mDisableTouchEvents = true;
    }

    @Override // android.webkit.WebView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDisableTouchEvents) {
            switch (event.getAction()) {
                case 0:
                    this.pointX = event.getX();
                    this.pointY = event.getY();
                    break;
                case 1:
                    boolean sameX = this.pointX + ((float) this.tolerance) > event.getX() && this.pointX - ((float) this.tolerance) < event.getX();
                    boolean sameY = this.pointY + ((float) this.tolerance) > event.getY() && this.pointY - ((float) this.tolerance) < event.getY();
                    if (sameX && sameY) {
                        performClick();
                        break;
                    }
                    break;
                case 2:
                    return false;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /* loaded from: classes.dex */
    public static class BingMapInterface implements BingMapListener {
        private BingMapListener mWrappedListener;

        private BingMapInterface(BingMapListener listener) {
            this.mWrappedListener = listener;
        }

        @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
        @JavascriptInterface
        public void onMapLoaded() {
            if (this.mWrappedListener != null) {
                this.mWrappedListener.onMapLoaded();
            }
        }

        @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
        @JavascriptInterface
        public void onMapEmpty() {
            if (this.mWrappedListener != null) {
                this.mWrappedListener.onMapEmpty();
            }
        }
    }
}

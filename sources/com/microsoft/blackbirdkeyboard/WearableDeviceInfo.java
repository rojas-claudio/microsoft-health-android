package com.microsoft.blackbirdkeyboard;

import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class WearableDeviceInfo {
    private WearableDeviceInfoItem mCurrentDeviceInfoItem;
    private Map<String, WearableDeviceInfoItem> mDeviceInfoMap = new HashMap();
    private Point mCurrentDeviceSize = new Point();
    private DisplayMetrics mCurrentDeviceMetrics = new DisplayMetrics();

    /* loaded from: classes.dex */
    public static class WearableDeviceInfoItem {
        public int mMarginBottom;
        public int mMarginLeft;
        public int mMarginRight;
        public int mMarginTop;
        public String mName;
        public int mScreenHeight;
        public int mScreenWidth;
        public Shape mShape;

        /* loaded from: classes.dex */
        public enum Shape {
            unknown,
            rect,
            round;

            /* JADX INFO: Access modifiers changed from: package-private */
            public static Shape fromString(String string) {
                if (string.equals(unknown.toString())) {
                    return unknown;
                }
                if (string.equals(rect.toString())) {
                    return rect;
                }
                if (string.equals(round.toString())) {
                    return round;
                }
                throw new RuntimeException("Unknown device shape: " + string);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Rect getMarginRect() {
            return new Rect(this.mMarginLeft, this.mMarginTop, this.mMarginRight, this.mMarginBottom);
        }

        boolean hasHorizontalMargin() {
            return this.mMarginLeft > 0 || this.mMarginRight > 0;
        }

        void load(AssetManager assetManager, String name) {
            String deviceDir = "device_" + name;
            ConfigurationFileReader reader = new ConfigurationFileReader(assetManager, deviceDir, "device_info.txt");
            while (true) {
                String[] tokens = reader.parseLine(SimpleComparison.EQUAL_TO_OPERATION);
                if (tokens != null) {
                    if (tokens.length == 2) {
                        String key = tokens[0];
                        String value = tokens[1].trim();
                        this.mName = name;
                        if (key.equals("shape")) {
                            this.mShape = Shape.fromString(value);
                        } else if (key.equals("screen_width")) {
                            this.mScreenWidth = Integer.parseInt(value);
                        } else if (key.equals("screen_height")) {
                            this.mScreenHeight = Integer.parseInt(value);
                        } else if (key.equals("margin_left")) {
                            this.mMarginLeft = Integer.parseInt(value);
                        } else if (key.equals("margin_top")) {
                            this.mMarginTop = Integer.parseInt(value);
                        } else if (key.equals("margin_right")) {
                            this.mMarginRight = Integer.parseInt(value);
                        } else if (key.equals("margin_bottom")) {
                            this.mMarginBottom = Integer.parseInt(value);
                        }
                    }
                } else {
                    reader.close();
                    Log.d("WearableDeviceInfoItem", name + "={" + this.mShape + "," + this.mScreenWidth + "," + this.mScreenHeight + "," + this.mMarginLeft + "," + this.mMarginTop + "," + this.mMarginRight + "," + this.mMarginBottom + "}");
                    return;
                }
            }
        }
    }

    public static String buildKey(String brand, String model, int width, int height) {
        return brand + "," + model + "," + width + "," + height;
    }

    public void load(AssetManager assetManager, WindowManager windowManager) {
        windowManager.getDefaultDisplay().getSize(this.mCurrentDeviceSize);
        windowManager.getDefaultDisplay().getMetrics(this.mCurrentDeviceMetrics);
        ConfigurationFileReader reader = new ConfigurationFileReader(assetManager, "", "device_list.txt");
        while (true) {
            String[] tokens = reader.parseLine(",");
            if (tokens != null) {
                if (tokens.length == 3) {
                    String brand = tokens[0];
                    String model = tokens[1];
                    String name = tokens[2];
                    WearableDeviceInfoItem item = new WearableDeviceInfoItem();
                    item.load(assetManager, name);
                    this.mDeviceInfoMap.put(buildKey(brand, model, item.mScreenWidth, item.mScreenHeight), item);
                }
            } else {
                reader.close();
                return;
            }
        }
    }

    public WearableDeviceInfoItem getCurrentDevice() {
        if (this.mCurrentDeviceInfoItem == null) {
            String key = buildKey(getCurrentDeviceBrand(), getCurrentDeviceModel(), getCurrentDeviceSize().x, getCurrentDeviceSize().y);
            this.mCurrentDeviceInfoItem = this.mDeviceInfoMap.get(key);
            if (this.mCurrentDeviceInfoItem == null) {
                this.mCurrentDeviceInfoItem = new WearableDeviceInfoItem();
                this.mCurrentDeviceInfoItem.mShape = WearableDeviceInfoItem.Shape.unknown;
                this.mCurrentDeviceInfoItem.mScreenWidth = getCurrentDeviceSize().x;
                this.mCurrentDeviceInfoItem.mScreenHeight = getCurrentDeviceSize().y;
                this.mCurrentDeviceInfoItem.mMarginLeft = 0;
                this.mCurrentDeviceInfoItem.mMarginTop = 0;
                this.mCurrentDeviceInfoItem.mMarginRight = 0;
                this.mCurrentDeviceInfoItem.mMarginBottom = 0;
                for (Map.Entry<String, WearableDeviceInfoItem> entry : this.mDeviceInfoMap.entrySet()) {
                    WearableDeviceInfoItem entry_item = entry.getValue();
                    this.mCurrentDeviceInfoItem.mMarginLeft = Math.max(this.mCurrentDeviceInfoItem.mMarginLeft, entry_item.mMarginLeft);
                    this.mCurrentDeviceInfoItem.mMarginTop = Math.max(this.mCurrentDeviceInfoItem.mMarginTop, entry_item.mMarginTop);
                    this.mCurrentDeviceInfoItem.mMarginRight = Math.max(this.mCurrentDeviceInfoItem.mMarginRight, entry_item.mMarginRight);
                    this.mCurrentDeviceInfoItem.mMarginBottom = Math.max(this.mCurrentDeviceInfoItem.mMarginBottom, entry_item.mMarginBottom);
                }
            }
        }
        return this.mCurrentDeviceInfoItem;
    }

    String getCurrentDeviceBrand() {
        return Build.BRAND;
    }

    String getCurrentDeviceModel() {
        return Build.MODEL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point getCurrentDeviceSize() {
        return this.mCurrentDeviceSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCurrentDeviceDpiScale() {
        return 160.0f / this.mCurrentDeviceMetrics.densityDpi;
    }
}

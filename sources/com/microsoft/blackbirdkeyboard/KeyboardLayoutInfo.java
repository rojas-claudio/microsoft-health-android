package com.microsoft.blackbirdkeyboard;

import android.content.res.AssetManager;
import android.util.Log;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.blackbirdkeyboard.WearableDeviceInfo;
/* loaded from: classes.dex */
public class KeyboardLayoutInfo {
    private int mKeyboardHeight;
    private int mKeyboardWidth;
    private int mKeyboardY1stRow;
    private int mKeyboardYmax;
    private int mKeyboardYmin;
    private int mSendCancelEdge;
    private int mSendCancelHeight;
    private WearableDeviceInfo.WearableDeviceInfoItem.Shape mShape;

    public WearableDeviceInfo.WearableDeviceInfoItem.Shape getShape() {
        return this.mShape;
    }

    public int getKeyboardHeight() {
        return this.mKeyboardHeight;
    }

    public int getKeyboardWidth() {
        return this.mKeyboardWidth;
    }

    public int getKeyboardYmin() {
        return this.mKeyboardYmin;
    }

    public int getKeyboardY1stRow() {
        return this.mKeyboardY1stRow;
    }

    public int getKeyboardYmax() {
        return this.mKeyboardYmax;
    }

    public int getSendCancelHeight() {
        return this.mSendCancelHeight;
    }

    public int getSendCancelEdge() {
        return this.mSendCancelEdge;
    }

    void Initialize() {
        this.mShape = WearableDeviceInfo.WearableDeviceInfoItem.Shape.unknown;
        this.mKeyboardHeight = 0;
        this.mKeyboardWidth = 0;
        this.mKeyboardYmin = 0;
        this.mKeyboardY1stRow = 0;
        this.mKeyboardYmax = 0;
        this.mSendCancelHeight = 0;
        this.mSendCancelEdge = 0;
    }

    void Load(String layoutDir, AssetManager assetManager) {
        Initialize();
        ConfigurationFileReader reader = new ConfigurationFileReader(assetManager, layoutDir, "layout_info.txt");
        while (true) {
            String[] tokens = reader.parseLine(SimpleComparison.EQUAL_TO_OPERATION);
            if (tokens != null) {
                if (tokens.length == 2) {
                    String key = tokens[0];
                    String valuestr = tokens[1].trim();
                    if (key.equals("shape")) {
                        this.mShape = WearableDeviceInfo.WearableDeviceInfoItem.Shape.fromString(valuestr);
                    } else {
                        int value = Integer.parseInt(valuestr);
                        if (key.equals("keyboard_width")) {
                            this.mKeyboardWidth = value;
                        } else if (key.equals("keyboard_height")) {
                            this.mKeyboardHeight = value;
                        } else if (key.equals("keyboard_ymin")) {
                            this.mKeyboardYmin = value;
                        } else if (key.equals("keyboard_y1strow")) {
                            this.mKeyboardY1stRow = value;
                        } else if (key.equals("keyboard_ymax")) {
                            this.mKeyboardYmax = value;
                        } else if (key.equals("sendcancel_height")) {
                            this.mSendCancelHeight = value;
                        } else if (key.equals("sendcancel_edge")) {
                            this.mSendCancelEdge = value;
                        }
                    }
                }
            } else {
                reader.close();
                Log.d("KeyboardLayoutInfo", "{" + this.mKeyboardWidth + "," + this.mKeyboardHeight + "," + this.mKeyboardYmin + "," + this.mKeyboardY1stRow + "," + this.mKeyboardYmax + "," + this.mSendCancelHeight + "," + this.mSendCancelEdge + "}");
                return;
            }
        }
    }
}

package com.microsoft.blackbirdkeyboardtest;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class Phrase {
    public String PhraseText;
    public PointFormat PtFormat;
    public List<List<int[]>> Strokes;
    public List<List<int[]>> extraData;
    public ExtraDataFormat extraDataFormat;

    /* loaded from: classes.dex */
    public enum PointFormat {
        X_Y_Z_Tms_ActiveSensors_NumTouches_Ax_Ay_Az,
        X_Y_Tms,
        X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure,
        X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure_Ax_Ay_Az_Atms;

        public static PointFormat fromInt(int i) {
            switch (i) {
                case 0:
                    return X_Y_Z_Tms_ActiveSensors_NumTouches_Ax_Ay_Az;
                case 1:
                    return X_Y_Tms;
                case 2:
                    return X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure;
                case 3:
                    return X_Y_Tms_ContactState_Pid_Hid_AxisLenMajor_AxisLenMinor_Orientation_Pressure_Ax_Ay_Az_Atms;
                default:
                    throw new IndexOutOfBoundsException("PointFormat unknown enum value");
            }
        }
    }

    /* loaded from: classes.dex */
    public enum ExtraDataFormat {
        None,
        Ax_Ay_Az_Atms;

        public static ExtraDataFormat fromInt(int i) {
            switch (i) {
                case 0:
                    return None;
                case 1:
                    return Ax_Ay_Az_Atms;
                default:
                    throw new IndexOutOfBoundsException("ExtraDataFormat unknown enum value");
            }
        }
    }

    public Phrase(String jsonSerialized) {
        try {
            JSONObject jsonMain = new JSONObject(jsonSerialized);
            JSONArray jsonPhraseStrokes = jsonMain.getJSONArray("Strokes");
            this.Strokes = new ArrayList();
            for (int iStroke = 0; iStroke < jsonPhraseStrokes.length(); iStroke++) {
                JSONArray jsonStrokePackets = jsonPhraseStrokes.getJSONArray(iStroke);
                List<int[]> strokePackets = new ArrayList<>();
                for (int iPacket = 0; iPacket < jsonStrokePackets.length(); iPacket++) {
                    JSONArray jsonPacket = jsonStrokePackets.getJSONArray(iPacket);
                    int[] packet = new int[jsonPacket.length()];
                    for (int iProperty = 0; iProperty < jsonPacket.length(); iProperty++) {
                        packet[iProperty] = jsonPacket.getInt(iProperty);
                    }
                    strokePackets.add(packet);
                }
                this.Strokes.add(strokePackets);
            }
            this.PtFormat = PointFormat.fromInt(jsonMain.getInt("PtFormat"));
            this.PhraseText = jsonMain.getString("PhraseText");
            JSONArray jsonPhraseExtraData = jsonMain.getJSONArray("ExtraData");
            this.extraData = new ArrayList();
            for (int iStream = 0; iStream < jsonPhraseExtraData.length(); iStream++) {
                JSONArray jsonStreamPackets = jsonPhraseStrokes.getJSONArray(iStream);
                List<int[]> streamPackets = new ArrayList<>();
                for (int iPacket2 = 0; iPacket2 < jsonStreamPackets.length(); iPacket2++) {
                    JSONArray jsonPacket2 = jsonStreamPackets.getJSONArray(iPacket2);
                    int[] packet2 = new int[jsonPacket2.length()];
                    for (int iProperty2 = 0; iProperty2 < jsonPacket2.length(); iProperty2++) {
                        packet2[iProperty2] = jsonPacket2.getInt(iProperty2);
                    }
                    streamPackets.add(packet2);
                }
                this.extraData.add(streamPackets);
            }
            this.extraDataFormat = ExtraDataFormat.fromInt(jsonMain.getInt("ExtraDataFormat"));
        } catch (JSONException e) {
            Log.d("BlackbirdKeyboardTest", "Failed to deserialize json");
            e.printStackTrace();
            throw new RuntimeException("Failed to deserialize json");
        }
    }
}

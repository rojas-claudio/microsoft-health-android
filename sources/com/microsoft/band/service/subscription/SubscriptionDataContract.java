package com.microsoft.band.service.subscription;

import com.google.android.gms.location.LocationRequest;
import com.microsoft.band.device.subscription.BatteryGaugeData;
import com.microsoft.band.device.subscription.DeviceStatusData;
import com.microsoft.band.device.subscription.TextMessageData;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.device.subscription.ALSData;
import com.microsoft.band.internal.device.subscription.AccelData;
import com.microsoft.band.internal.device.subscription.AccelGyroData;
import com.microsoft.band.internal.device.subscription.BarometerData;
import com.microsoft.band.internal.device.subscription.CaloriesData;
import com.microsoft.band.internal.device.subscription.DeviceContactData;
import com.microsoft.band.internal.device.subscription.DistanceData;
import com.microsoft.band.internal.device.subscription.ElevationData;
import com.microsoft.band.internal.device.subscription.GsrData;
import com.microsoft.band.internal.device.subscription.HeartRateData;
import com.microsoft.band.internal.device.subscription.PedometerData;
import com.microsoft.band.internal.device.subscription.RRIntervalData;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.internal.device.subscription.ThermalData;
import com.microsoft.band.internal.device.subscription.UvData;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.device.PushServicePayload;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class SubscriptionDataContract {
    private static final String TAG = SubscriptionDataContract.class.getSimpleName();
    private static final String STREAMING_TAG = TAG + ": " + InternalBandConstants.STREAM_TAG;
    private static final Map<SensorType, SubscriptionDataConstructor> mHandlers = new EnumMap(SensorType.class);

    static {
        mHandlers.put(SensorType.Accelerometer128MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.1
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelData(buffer);
            }
        });
        mHandlers.put(SensorType.Accelerometer32MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.2
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelData(buffer);
            }
        });
        mHandlers.put(SensorType.Accelerometer16MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.3
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelData(buffer);
            }
        });
        mHandlers.put(SensorType.AccelerometerGyroscope128MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.4
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelGyroData(buffer);
            }
        });
        mHandlers.put(SensorType.AccelerometerGyroscope32MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.5
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelGyroData(buffer);
            }
        });
        mHandlers.put(SensorType.AccelerometerGyroscope16MS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.6
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new AccelGyroData(buffer);
            }
        });
        mHandlers.put(SensorType.Calories, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.7
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new CaloriesData(buffer);
            }
        });
        mHandlers.put(SensorType.Distance, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.8
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new DistanceData(buffer);
            }
        });
        mHandlers.put(SensorType.HeartRate, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.9
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new HeartRateData(buffer);
            }
        });
        mHandlers.put(SensorType.Pedometer, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.10
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new PedometerData(buffer);
            }
        });
        mHandlers.put(SensorType.SkinTemp, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.11
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new ThermalData(buffer);
            }
        });
        mHandlers.put(SensorType.UV, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.12
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new UvData(buffer);
            }
        });
        mHandlers.put(SensorType.DeviceContact, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.13
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new DeviceContactData(buffer);
            }
        });
        mHandlers.put(SensorType.BatteryGauge, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.14
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new BatteryGaugeData(buffer);
            }
        });
        mHandlers.put(SensorType.DeviceStatus, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.15
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new DeviceStatusData(buffer);
            }
        });
        mHandlers.put(SensorType.SmsDismissed, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.16
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new TextMessageData(buffer);
            }
        });
        mHandlers.put(SensorType.CallDismissed, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.17
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new TextMessageData(buffer);
            }
        });
        mHandlers.put(SensorType.Gsr, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.18
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new GsrData(buffer);
            }
        });
        mHandlers.put(SensorType.ALS, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.19
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new ALSData(buffer);
            }
        });
        mHandlers.put(SensorType.RRInterval, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.20
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new RRIntervalData(buffer);
            }
        });
        mHandlers.put(SensorType.Barometer, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.21
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new BarometerData(buffer);
            }
        });
        mHandlers.put(SensorType.Elevation, new SubscriptionDataConstructor() { // from class: com.microsoft.band.service.subscription.SubscriptionDataContract.22
            @Override // com.microsoft.band.service.subscription.SubscriptionDataConstructor
            public SubscriptionDataModel createSubscriptionData(ByteBuffer buffer) {
                return new ElevationData(buffer);
            }
        });
    }

    public static List<SubscriptionDataModel> createDataModels(PushServicePayload payload) {
        ArrayList<SubscriptionDataModel> dataModels = new ArrayList<>();
        ByteBuffer buffer = payload.getDataBuffer();
        SubscriptionDataConstructor constructor = mHandlers.get(payload.getSensorType());
        if (constructor != null) {
            while (buffer.remaining() > 0) {
                SubscriptionDataModel dataModel = constructor.createSubscriptionData(buffer);
                dataModel.setMissedSamples(payload.getMissedSamples());
                dataModel.setTimestamp(payload.getTimestamp());
                dataModels.add(dataModel);
            }
        }
        return dataModels;
    }

    private SubscriptionDataContract() {
        throw new UnsupportedOperationException();
    }

    public static BitSet createBitSet(ByteBuffer data) {
        if (data != null) {
            BitSet set = new BitSet(data.remaining() * 8);
            byte[] bitValues = {1, 2, 4, 8, BandDeviceConstants.GUID_BYTE_LENGTH, 32, 64, Byte.MIN_VALUE};
            int bit = 0;
            StringBuffer sb = new StringBuffer();
            while (data.remaining() > 0) {
                byte bufferDataValue = data.get();
                for (byte value : bitValues) {
                    if ((bufferDataValue & value) == value) {
                        sb.append(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + bit);
                        set.set(bit);
                    }
                    bit++;
                }
            }
            KDKLog.d(STREAMING_TAG, "Device Active Remote Subscriptions: %s", sb.toString());
            return set;
        }
        return new BitSet(SensorType.Unknown.getId());
    }

    /* loaded from: classes.dex */
    public enum SensorType {
        Accelerometer128MS(0, 1),
        Accelerometer32MS(1, 1),
        AccelerometerGyroscope128MS(4, 1),
        AccelerometerGyroscope32MS(5, 1),
        AccelerometerGyroscope2MS(7, 1),
        Distance(13, 1),
        Gsr(15, 2),
        HeartRate(16, 1),
        Pedometer(19, 1),
        SkinTemp(20, 1),
        UV(21, 1),
        HRDebug(24, 1),
        ALS(25, 2),
        RRInterval(26, 2),
        DeviceContact(35, 1),
        BatteryGauge(38, 1),
        Calories(46, 1),
        Accelerometer16MS(48, 1),
        AccelerometerGyroscope16MS(49, 1),
        Barometer(58, 2),
        Elevation(71, 2),
        DeviceStatus(101, 1),
        SmsDismissed(102, 1),
        CallDismissed(Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST, 1),
        KeyboardEvent(LocationRequest.PRIORITY_LOW_POWER, 2),
        KeyboardSetContext(105, 2),
        TileEvent(106, 1),
        Unknown(200, 1);
        
        public static final int REMOTE_SUBSCRIPTION_INDEX_LIMIT = 100;
        private final int mId;
        private final int mVersionAdded;

        SensorType(int id, int versionAdded) {
            this.mId = id;
            this.mVersionAdded = versionAdded;
        }

        public int getId() {
            return this.mId;
        }

        public int getVersionAdded() {
            return this.mVersionAdded;
        }

        public static SensorType lookup(int typeId) {
            SensorType[] arr$ = values();
            for (SensorType entry : arr$) {
                if (entry.mId == typeId) {
                    return entry;
                }
            }
            return Unknown;
        }
    }
}

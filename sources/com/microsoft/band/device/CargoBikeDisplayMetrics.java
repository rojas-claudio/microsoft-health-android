package com.microsoft.band.device;

import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.internal.util.VersionCheck;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class CargoBikeDisplayMetrics {
    private static final int CARGO_STRUCTURE_SIZE = 12;
    private static final int ENVOY_STRUCTURE_SIZE = 14;
    private static final int NUMBER_OF_DISPLAYED_METRICS_CARGO = 6;
    private static final int NUMBER_OF_DISPLAYED_METRICS_ENVOY = 7;

    private CargoBikeDisplayMetrics() {
    }

    public static byte[] toBytesWithValidation(BikeDisplayMetricType[] metrics, int hardwareVersion) {
        Validation.validateNullParameter(metrics, "BikeDisplayMetrics");
        Validation.validateInRange("Bike Metrics Hardware Version", hardwareVersion, 7, 999);
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            if (metrics.length != 7) {
                throw new IllegalArgumentException("For Envoy the number of bike metrics should be 7");
            }
            validateEnvoy(metrics);
        } else if (metrics.length != 6) {
            throw new IllegalArgumentException("For Cargo the number of bike metrics should be 6");
        } else {
            validateCargo(metrics);
        }
        return toBytes(metrics, hardwareVersion);
    }

    private static void validateCargo(BikeDisplayMetricType[] metrics) {
        int[] bucketArray = new int[9];
        for (BikeDisplayMetricType m : metrics) {
            Validation.validateNullParameter(m, "BikeDisplayMetricType");
            if (bucketArray[m.code] == 0) {
                int i = m.code;
                bucketArray[i] = bucketArray[i] + 1;
                if (m.code > 5) {
                    throw new IllegalArgumentException("Invalid metric for Cargo Band");
                }
            } else if (m.code != BikeDisplayMetricType.NONE.code) {
                throw new IllegalArgumentException("There is duplicate metric.");
            }
        }
    }

    private static void validateEnvoy(BikeDisplayMetricType[] metrics) {
        int[] bucketArray = new int[9];
        for (BikeDisplayMetricType m : metrics) {
            Validation.validateNullParameter(m, "BikeDisplayMetricType");
            if (bucketArray[m.code] == 0) {
                int i = m.code;
                bucketArray[i] = bucketArray[i] + 1;
            } else if (m.code != BikeDisplayMetricType.NONE.code) {
                throw new IllegalArgumentException("There is duplicate metric.");
            }
        }
    }

    private static byte[] toBytes(BikeDisplayMetricType[] metrics, int hardwareVersion) {
        ByteBuffer buffer;
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            buffer = BufferUtil.allocateLittleEndian(14);
            for (int i = 0; i < 7; i++) {
                buffer.putShort((short) metrics[i].code);
            }
        } else {
            buffer = BufferUtil.allocateLittleEndian(12);
            for (int i2 = 0; i2 < 6; i2++) {
                buffer.putShort((short) metrics[i2].code);
            }
        }
        return buffer.array();
    }

    public static int getStructSize(int hardwareVersion) {
        return VersionCheck.isV2DeviceOrGreater(hardwareVersion) ? 14 : 12;
    }

    public static BikeDisplayMetricType[] getDisplayMetricsFromData(byte[] data, int hardwareVersion) {
        BikeDisplayMetricType[] metrics;
        Validation.validateInRange("Bike Metrics Hardware Version", hardwareVersion, 7, 999);
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            metrics = new BikeDisplayMetricType[7];
            for (int i = 0; i < 7; i++) {
                metrics[i] = BikeDisplayMetricType.lookup(BitHelper.unsignedShortToInteger(buffer.getShort()));
            }
        } else {
            metrics = new BikeDisplayMetricType[6];
            for (int i2 = 0; i2 < 6; i2++) {
                metrics[i2] = BikeDisplayMetricType.lookup(BitHelper.unsignedShortToInteger(buffer.getShort()));
            }
        }
        return metrics;
    }
}

package com.microsoft.kapp;

import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public class CargoExtensions {

    /* loaded from: classes.dex */
    public static class SingleDeviceCheckFailedException extends CargoException {
        private static final long serialVersionUID = -8460695339995840514L;
        private DeviceErrorState mDeviceErrorState;

        public SingleDeviceCheckFailedException(DeviceErrorState deviceErrorState, BandServiceMessage.Response response) {
            super("SingleDeviceException", response);
            this.mDeviceErrorState = deviceErrorState;
        }

        public DeviceErrorState getDeviceErrorState() {
            return this.mDeviceErrorState;
        }
    }
}

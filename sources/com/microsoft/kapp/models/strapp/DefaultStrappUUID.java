package com.microsoft.kapp.models.strapp;

import com.microsoft.band.device.DeviceConstants;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class DefaultStrappUUID {
    public static final UUID STRAPP_MESSAGING = UUID.fromString(DeviceConstants.GUID_ID_SMS);
    public static final UUID STRAPP_CALLS = UUID.fromString(DeviceConstants.GUID_ID_CALL);
    public static final UUID STRAPP_RUN = UUID.fromString(DeviceConstants.GUID_ID_RUN);
    public static final UUID STRAPP_BIKE = UUID.fromString("96430fcb-0060-41cb-9de2-e00cac97f85d");
    public static final UUID STRAPP_SLEEP = UUID.fromString(DeviceConstants.GUID_ID_SLEEP);
    public static final UUID STRAPP_EXERCISE = UUID.fromString(DeviceConstants.GUID_ID_WORKOUT);
    public static final UUID STRAPP_GOLF = UUID.fromString(DeviceConstants.GUID_ID_GOLF);
    public static final UUID STRAPP_ALARM_STOPWATCH = UUID.fromString(DeviceConstants.GUID_ID_TIMER);
    public static final UUID STRAPP_CALENDAR = UUID.fromString(DeviceConstants.GUID_ID_CALENDAR);
    public static final UUID STRAPP_BING_WEATHER = UUID.fromString(DeviceConstants.GUID_ID_WEATHER);
    public static final List<UUID> STRAPP_BING_WEATHER_PAGES = Arrays.asList(UUID.fromString("b92dcd02-21f2-4208-970a-4bfc9861947e"), UUID.fromString("d0e2bf8a-a2e1-4617-8bba-d60d7b400c34"), UUID.fromString("c05f126f-f5fb-4d9a-88b6-e21c07b475d3"), UUID.fromString("e688378e-ac25-4264-ba53-b3fd9c67eaad"), UUID.fromString("d7b88c9f-5e41-4097-8520-d52c1b2a3b6d"), UUID.fromString("b646345d-1ea4-4872-b2df-b9a610a1b2f6"), UUID.fromString("7fd73cec-edbc-4c3a-b9b4-acde22951131"), UUID.fromString("76150e97-94cd-4d55-847f-ba7e9fc408e6"));
    public static final UUID STRAPP_BING_FINANCE = UUID.fromString(DeviceConstants.GUID_ID_FINANCE);
    public static final List<UUID> STRAPP_BING_FINANCE_PAGES = Arrays.asList(UUID.fromString("efbf331a-fa87-4401-887f-6554d83d6f6f"), UUID.fromString("c8117426-059a-47a7-b7da-dfb76600cd29"), UUID.fromString("9193bbfa-752b-446f-a96b-c841bde04ffd"), UUID.fromString("9126a23f-44f7-44ca-9262-2df577692c16"), UUID.fromString("4f326ae2-75d3-41a7-b41a-8ab11edea571"), UUID.fromString("313ba941-fa5b-4a22-ab35-339396c19973"), UUID.fromString("196021c5-33bb-4aa3-88a7-70eefb5773b1"), UUID.fromString("f375f75e-05b7-42e0-a5ff-9a5c409e9dd9"));
    public static final UUID STRAPP_FACEBOOK = UUID.fromString("FD06B486-BBDA-4DA5-9014-124936386237");
    public static final UUID STRAPP_FACEBOOK_MESSENGER = UUID.fromString("76B08699-2F2E-9041-96C2-1F4BFC7EAB10");
    public static final UUID STRAPP_NOTIFICATION_CENTER = UUID.fromString("4076b009-0455-4af7-a705-6d4acd45a556");
    public static final UUID STRAPP_TWITTER = UUID.fromString("2E76A806-F509-4110-9C03-43DD2359D2AD");
    public static final UUID STRAPP_EMAIL = UUID.fromString("823BA55A-7C98-4261-AD5E-929031289C6E");
    public static final UUID STRAPP_GUIDED_WORKOUTS = UUID.fromString("0281C878-AFA8-40FF-ACFD-BCA06C5C4922");
    public static final UUID STRAPP_UV = UUID.fromString("59976CF5-15C8-4799-9E31-F34C765A6BD1");
    public static final UUID STRAPP_CORTANA = UUID.fromString("d7fb5ff5-906a-4f2c-8269-dde6a75138c4");
    public static final UUID STRAPP_STARBUCKS = UUID.fromString("64a29f65-70bb-4f32-99a2-0f250a05d427");
    public static final List<UUID> STRAPP_STARBUCKS_PAGES = Arrays.asList(UUID.fromString("5fe2048d-7336-684f-901d-dda85118c509"));
    public static final List<UUID> STRAPP_THIRD_PARTY_FILTER = Arrays.asList(STRAPP_CORTANA);
}

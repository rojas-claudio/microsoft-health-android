package com.microsoft.kapp.models;

import android.text.Spannable;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.ArrayList;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserEventSummary extends BaseUserEventSummary {
    private Spannable mMetric;
    private ArrayList<String> mPersonalBestIds;

    public UserEventSummary(String name, String details, DateTime date, UserEvent userEvent, Spannable metric) {
        super(name, details, date, userEvent);
        this.mMetric = metric;
    }

    public Spannable getMetric() {
        return this.mMetric;
    }

    public void setPersonalBests(ArrayList<String> bests) {
        this.mPersonalBestIds = bests;
    }

    public ArrayList<String> getPersonalBests() {
        return this.mPersonalBestIds;
    }
}

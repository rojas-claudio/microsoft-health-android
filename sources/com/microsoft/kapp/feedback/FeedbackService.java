package com.microsoft.kapp.feedback;

import android.app.Activity;
import android.net.Uri;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.models.FeedbackDescription;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface FeedbackService {
    void sendFeedbackAsync(Activity activity, String str, FeedbackDescription feedbackDescription, String str2, ArrayList<Uri> arrayList, CargoUserProfile cargoUserProfile, boolean z, Callback callback);

    boolean uploadArchive(String str);
}

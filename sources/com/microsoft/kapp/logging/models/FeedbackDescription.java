package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.telephony.MmsColumns;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.io.Serializable;
import java.util.HashMap;
/* loaded from: classes.dex */
public class FeedbackDescription implements Parcelable {
    public static final Parcelable.Creator<FeedbackDescription> CREATOR = new Parcelable.Creator<FeedbackDescription>() { // from class: com.microsoft.kapp.logging.models.FeedbackDescription.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackDescription createFromParcel(Parcel in) {
            return new FeedbackDescription(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackDescription[] newArray(int size) {
            return new FeedbackDescription[size];
        }
    };
    @SerializedName(MmsColumns.MMS_PART_TEXT)
    String mCurrentDescription;
    @SerializedName(KCloudConstants.CATEGORY_TYPE)
    FeedbackCategory mFeedbackCategory;
    @SerializedName("subcategory")
    FeedbackSubcategory mFeedbackSubCategory;
    @SerializedName("properties")
    HashMap<String, String> mProperties;

    /* loaded from: classes.dex */
    public enum FeedbackCategory {
        Golf
    }

    /* loaded from: classes.dex */
    public enum FeedbackSubcategory {
        SuggestACourse,
        EventProblem
    }

    public FeedbackDescription(String currentDescription) {
        this.mCurrentDescription = currentDescription;
    }

    public FeedbackDescription(String currentDescription, FeedbackCategory feedbackCategory, FeedbackSubcategory feedbackSubCategory, HashMap<String, String> properties) {
        this.mCurrentDescription = currentDescription;
        this.mFeedbackCategory = feedbackCategory;
        this.mFeedbackSubCategory = feedbackSubCategory;
        this.mProperties = properties;
    }

    public FeedbackDescription(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCurrentDescription);
        dest.writeSerializable(this.mFeedbackCategory);
        dest.writeSerializable(this.mFeedbackSubCategory);
        dest.writeSerializable(this.mProperties);
    }

    public void readFromParcel(Parcel in) {
        this.mCurrentDescription = in.readString();
        Serializable category = in.readSerializable();
        if (category != null && (category instanceof FeedbackCategory)) {
            this.mFeedbackCategory = (FeedbackCategory) category;
        }
        Serializable subcategory = in.readSerializable();
        if (subcategory != null && (subcategory instanceof FeedbackCategory)) {
            this.mFeedbackCategory = (FeedbackCategory) subcategory;
        }
        Serializable properties = in.readSerializable();
        if (properties != null && (properties instanceof HashMap)) {
            this.mProperties = (HashMap) properties;
        }
    }
}

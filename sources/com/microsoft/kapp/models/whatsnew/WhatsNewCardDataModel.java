package com.microsoft.kapp.models.whatsnew;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;
@SuppressLint({"UseSparseArrays"})
/* loaded from: classes.dex */
public class WhatsNewCardDataModel implements Parcelable {
    public static final Parcelable.Creator<WhatsNewCardDataModel> CREATOR = new Parcelable.Creator<WhatsNewCardDataModel>() { // from class: com.microsoft.kapp.models.whatsnew.WhatsNewCardDataModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WhatsNewCardDataModel createFromParcel(Parcel in) {
            return new WhatsNewCardDataModel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WhatsNewCardDataModel[] newArray(int size) {
            return new WhatsNewCardDataModel[size];
        }
    };
    private int mBackgroundColor;
    private String mButtonText;
    private ButtonType mButtonType;
    private String mCardType;
    private String mFeatureClass;
    private int mImageID;
    private WhatsNewSecondaryCardDataModel mSecondCardModel;
    private String mSubtitle;
    private String mTitle;
    private String mUrl;

    public WhatsNewCardDataModel() {
    }

    public WhatsNewCardDataModel(String title, String subtitle, int imageId, ButtonType buttonType, String buttonText, String featureClass, String url, String cardType, int backgroundColor) {
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mImageID = imageId;
        this.mButtonType = buttonType;
        this.mFeatureClass = featureClass;
        setUrl(url);
        this.mButtonText = buttonText;
        this.mCardType = cardType;
        this.mBackgroundColor = backgroundColor;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSubtitle() {
        return this.mSubtitle;
    }

    public void setSubtitle(String subtitle) {
        this.mSubtitle = subtitle;
    }

    public int getImageID() {
        return this.mImageID;
    }

    public void setImageID(int imageID) {
        this.mImageID = imageID;
    }

    public ButtonType getButtonType() {
        return this.mButtonType;
    }

    public void setButtonType(ButtonType buttonType) {
        this.mButtonType = buttonType;
    }

    public String getFeatureClass() {
        return this.mFeatureClass;
    }

    public void setFeatureClass(String featureClass) {
        this.mFeatureClass = featureClass;
    }

    public String getButtonText() {
        return this.mButtonText;
    }

    public void setButtonText(String buttonText) {
        this.mButtonText = buttonText;
    }

    public String getCardType() {
        return this.mCardType;
    }

    public void setCardType(String cardType) {
        this.mCardType = cardType;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public WhatsNewSecondaryCardDataModel getSecondCardModel() {
        return this.mSecondCardModel;
    }

    public void setSecondCardModel(WhatsNewSecondaryCardDataModel secondCardModel) {
        this.mSecondCardModel = secondCardModel;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    /* loaded from: classes.dex */
    public enum ButtonType {
        SECONDARY(0),
        DEEPLINKING(1),
        WEB(2),
        NONE(3);
        
        private static final Map<Integer, ButtonType> intToTypeMap = new HashMap();
        private int value;

        static {
            ButtonType[] arr$ = values();
            for (ButtonType type : arr$) {
                intToTypeMap.put(Integer.valueOf(type.value), type);
            }
        }

        public static ButtonType fromInt(int i) {
            ButtonType type = intToTypeMap.get(Integer.valueOf(i));
            if (type == null) {
                return NONE;
            }
            return type;
        }

        ButtonType(int mValue) {
            this.value = mValue;
        }

        public int getValue() {
            return this.value;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mTitle);
        out.writeString(this.mSubtitle);
        out.writeInt(this.mImageID);
        out.writeInt(this.mButtonType.getValue());
        out.writeString(this.mButtonText);
        out.writeString(this.mCardType);
        out.writeInt(this.mBackgroundColor);
        out.writeString(this.mFeatureClass);
        out.writeParcelable(this.mSecondCardModel, flags);
    }

    private WhatsNewCardDataModel(Parcel in) {
        this.mTitle = in.readString();
        this.mSubtitle = in.readString();
        this.mImageID = in.readInt();
        this.mButtonType = ButtonType.fromInt(in.readInt());
        this.mButtonText = in.readString();
        this.mCardType = in.readString();
        this.mBackgroundColor = in.readInt();
        this.mFeatureClass = in.readString();
        this.mSecondCardModel = (WhatsNewSecondaryCardDataModel) in.readParcelable(WhatsNewSecondaryCardDataModel.class.getClassLoader());
    }
}

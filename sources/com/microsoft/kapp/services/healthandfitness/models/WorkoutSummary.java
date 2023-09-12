package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.krestsdk.models.DisplaySubType;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class WorkoutSummary {
    public static final String BING_IMAGE_ID = "bingimageid";
    public static final String BODY_PART = "bdyparts";
    public static final String DURATION = "duration";
    public static final String FTTYPE = "fttype";
    public static final String GENDER = "gender";
    public static final String GOAL = "goal";
    public static final String HISTOGRAM_FILTER_BODY_PART = "bodypart";
    public static final String ID = "id";
    public static final String IMAGE = "image";
    public static final String LEVEL = "level";
    public static final String NAME = "name";
    public static final String SCENARIO = "scenario";
    public static final String TYPE = "type";
    @SerializedName(BING_IMAGE_ID)
    private String mBingImageId;
    @SerializedName(BODY_PART)
    private String mBodyParts;
    @SerializedName("kprtnrlogourl")
    private String mBrandLogo;
    @SerializedName("kprtnrname")
    private String mBrandName;
    @SerializedName("duration")
    private int mDurationMinutes;
    @SerializedName(FTTYPE)
    private String mFTType;
    @SerializedName(GENDER)
    private String mGender;
    @SerializedName(GOAL)
    private String mGoal;
    @SerializedName("id")
    private String mId;
    @SerializedName(IMAGE)
    private String mImageUrl;
    @SerializedName("iscustomworkout")
    private boolean mIsCustomWorkout;
    @SerializedName("kdisplaysubtype")
    private DisplaySubType mKDisplaySubType;
    @SerializedName("level")
    private String mLevel;
    @SerializedName(NAME)
    private String mName;
    @SerializedName("publishdate")
    private DateTime mPublishDate;
    @SerializedName(SCENARIO)
    private String mScenario;
    @SerializedName("type")
    private String mType;

    public String getBodyParts() {
        return this.mBodyParts;
    }

    public void setBodyParts(String mBodyParts) {
        this.mBodyParts = mBodyParts;
    }

    public String getBingImageId() {
        return this.mBingImageId;
    }

    public void setBingImageId(String mBingImageId) {
        this.mBingImageId = mBingImageId;
    }

    public int getDurationMinutes() {
        return this.mDurationMinutes;
    }

    public void setDurationMinutes(int mDurationMinutes) {
        this.mDurationMinutes = mDurationMinutes;
    }

    public String getFTType() {
        return this.mFTType;
    }

    public void setFTType(String mFTType) {
        this.mFTType = mFTType;
    }

    public String getGender() {
        return this.mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getLevel() {
        return this.mLevel;
    }

    public void setLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getScenario() {
        return this.mScenario;
    }

    public void setScenario(String mScenario) {
        this.mScenario = mScenario;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getGoal() {
        return this.mGoal;
    }

    public void setGoal(String mGoal) {
        this.mGoal = mGoal;
    }

    public String getBrandName() {
        return this.mBrandName;
    }

    public void setBrandName(String brandName) {
        this.mBrandName = brandName;
    }

    public String getBrandLogo() {
        return this.mBrandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.mBrandLogo = brandLogo;
    }

    public DisplaySubType getDisplaySubType() {
        return this.mKDisplaySubType;
    }

    public void setDisplySubType(DisplaySubType displaySubType) {
        this.mKDisplaySubType = displaySubType;
    }

    public boolean getIsCustomWorkout() {
        return this.mIsCustomWorkout;
    }

    public void setIsCustomWorkout(boolean isCustomWorkout) {
        this.mIsCustomWorkout = isCustomWorkout;
    }

    public DateTime getPublishDate() {
        return this.mPublishDate;
    }

    public void setPublishDate(DateTime publishDate) {
        this.mPublishDate = publishDate;
    }

    public String getFieldByName(String name) {
        if ("level".equals(name)) {
            return this.mLevel;
        }
        if (BODY_PART.equals(name) || HISTOGRAM_FILTER_BODY_PART.equals(name)) {
            return this.mBodyParts;
        }
        if (GENDER.equals(name)) {
            return this.mGender;
        }
        if ("duration".equals(name)) {
            return Integer.toString(this.mDurationMinutes);
        }
        if ("type".equals(name)) {
            return this.mType;
        }
        if (NAME.equals(name)) {
            return this.mName;
        }
        if (GOAL.equals(name)) {
            return this.mGoal;
        }
        if (BING_IMAGE_ID.equals(name)) {
            return this.mBingImageId;
        }
        if (FTTYPE.equals(name)) {
            return this.mFTType;
        }
        if (IMAGE.equals(name)) {
            return this.mImageUrl;
        }
        if ("id".equals(name)) {
            return this.mId;
        }
        if (SCENARIO.equals(name)) {
            return this.mScenario;
        }
        return null;
    }
}

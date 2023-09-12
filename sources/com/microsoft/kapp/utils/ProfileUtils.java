package com.microsoft.kapp.utils;

import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.Weight;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.models.UserDailySummary;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Years;
/* loaded from: classes.dex */
public class ProfileUtils {
    static final String TAG = "ProfileUtils";

    public static CargoUserProfile getProfile(SettingsProvider settingsProvider) {
        return settingsProvider.getUserProfile();
    }

    public static String getFirstName(SettingsProvider settingsProvider) {
        CargoUserProfile profile = getProfile(settingsProvider);
        if (profile == null) {
            return null;
        }
        return profile.getFirstName();
    }

    public static String getGenderText(UserProfileInfo.Gender gender) {
        if (gender == UserProfileInfo.Gender.male) {
            return TelemetryConstants.Events.OobeComplete.Dimensions.GENDER_MALE;
        }
        if (gender == UserProfileInfo.Gender.female) {
            return TelemetryConstants.Events.OobeComplete.Dimensions.GENDER_FEMALE;
        }
        return "Unspecified";
    }

    public static Date getBirthdate(int month, int year) {
        GregorianCalendar calBirthdate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calBirthdate.set(5, 1);
        calBirthdate.set(2, month - 1);
        calBirthdate.set(1, year);
        return calBirthdate.getTime();
    }

    public static int getMonth(DateTime date) {
        return date.toDateTime(DateTimeZone.UTC).getMonthOfYear();
    }

    public static int getYear(DateTime date) {
        return date.toDateTime(DateTimeZone.UTC).getYear();
    }

    public static Date getDateFromCloudTime(String cloudTime) throws ParseException {
        SimpleDateFormat cloudDateFormat = new SimpleDateFormat(Constants.CLOUD_DATE_FORMAT, Locale.US);
        return cloudDateFormat.parse(cloudTime);
    }

    public static ArrayList<UserActivitySummary> getUserActivitySummaries(UserActivitySummary.DurationType type, List<UserDailySummary> userDailySummaryList, UserActivitySummary.ActivityType activityType) {
        CommonUtils.sortUserDailySummaryDesc(userDailySummaryList);
        ArrayList<UserActivitySummary> list = new ArrayList<>();
        if (userDailySummaryList != null) {
            for (UserDailySummary userDailySummary : userDailySummaryList) {
                if (userDailySummary != null && userDailySummary.getTimeOfDay() != null) {
                    UserActivitySummary summary = new UserActivitySummary(type, userDailySummary.getTimeOfDay().toLocalDate(), userDailySummary.getTimeOfDay().toLocalDate(), userDailySummary.getStepsTaken(), userDailySummary.getCaloriesBurned(), activityType, userDailySummary.getNumberOfActiveHours());
                    list.add(summary);
                } else {
                    KLog.d(TAG, "Invalid data given in UserDailySummary List");
                }
            }
        } else {
            KLog.e(TAG, "Invalid UserDailySummary List - null");
        }
        return list;
    }

    public static ArrayList<UserActivitySummary> getUserActivitySummaries(UserActivitySummary.DurationType type, LocalDate endDate, LocalDate profileCreatedDate, UserActivitySummary.ActivityType activityType) {
        ArrayList<UserActivitySummary> list = new ArrayList<>();
        if (endDate == null) {
            KLog.e(TAG, "Invalid end date given - null. Return empty UserActivitySummary list");
        } else if (profileCreatedDate == null) {
            KLog.e(TAG, "Invalid profile created date given - null. Return empty UserActivitySummary list");
        } else {
            if (type == null) {
                type = UserActivitySummary.DurationType.DAILY;
                KLog.e(TAG, "Invalid DurationType given - null. Use DAILY by default");
            }
            switch (type) {
                case DAILY:
                    for (int i = 0; i < 10; i++) {
                        LocalDate startDate = endDate.plusDays(-1);
                        if (startDate.isBefore(profileCreatedDate)) {
                            break;
                        } else {
                            UserActivitySummary summary = new UserActivitySummary(type, startDate, endDate, 0, 0, activityType, 0);
                            list.add(summary);
                            endDate = startDate;
                        }
                    }
                    break;
                case WEEKLY:
                    for (int i2 = 0; i2 < 10; i2++) {
                        LocalDate startDate2 = endDate.plusWeeks(-1).plusDays(1);
                        if (endDate.isBefore(profileCreatedDate)) {
                            break;
                        } else {
                            UserActivitySummary summary2 = new UserActivitySummary(type, startDate2, endDate, 0, 0, activityType, 0);
                            list.add(summary2);
                            endDate = startDate2.plusDays(-1);
                        }
                    }
                    break;
            }
        }
        return list;
    }

    public static int getUserAge(SettingsProvider settingsProvider) throws NullPointerException {
        CargoUserProfile profile = settingsProvider.getUserProfile();
        if (profile == null) {
            throw new NullPointerException("profile is null");
        }
        DateTime birthdate = profile.getBirthdate();
        LocalDate birthDate = birthdate.toLocalDate();
        LocalDate now = new LocalDate();
        return Years.yearsBetween(birthDate, now).getYears();
    }

    public static double getModerateExerciseCaloriesBurnPerHour(SettingsProvider settingsProvider) {
        Validate.notNull(settingsProvider, "settingsProvider");
        CargoUserProfile profile = settingsProvider.getUserProfile();
        int weight = 70;
        int height = Constants.DEFAULT_HEIGHT;
        int age = 30;
        UserProfileInfo.Gender gender = Constants.DEFAULT_GENDER;
        if (profile == null || profile.getWeightInGrams() <= 0 || profile.getHeightInMM() <= 0 || profile.getBirthdate() == null || profile.getGender() == null) {
            KLog.logPrivate(TAG, String.format("Unable to retrieve userProfile data locally. Using default values for BMR calculations. weight: %d, height: %d, age: %d, gender: %s ", 70, Integer.valueOf((int) Constants.DEFAULT_HEIGHT), 30, gender.name()));
        } else {
            weight = Weight.fromGrams(profile.getWeightInGrams()).getKilograms();
            height = Length.fromMillimeters(profile.getHeightInMM()).getCentimeters();
            DateTime birthdate = profile.getBirthdate();
            LocalDate birthDate = birthdate.toLocalDate();
            LocalDate now = new LocalDate();
            age = Years.yearsBetween(birthDate, now).getYears();
            gender = profile.getGender();
        }
        float BMR = calculateBMR(gender, weight, height, age);
        float[] ratio = Constants.RATIO_FOR_MODERATE_CALS_BURN_PER_HOUR;
        double moderateCalsBurnPerHour = (BMR / ratio[0]) + ((ratio[1] * BMR) / ratio[2]);
        return moderateCalsBurnPerHour;
    }

    public static float calculateBMR(UserProfileInfo.Gender gender, int weight, float height, int age) {
        float[] ratio;
        if (gender == UserProfileInfo.Gender.male) {
            ratio = Constants.MALE_RATIO_FOR_BMR;
        } else {
            ratio = Constants.FEMALE_RATIO_FOR_BMR;
        }
        float BMR = (((ratio[1] * weight) + (ratio[2] * height)) - (ratio[3] * age)) + ratio[0];
        return BMR;
    }
}

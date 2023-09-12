package com.microsoft.kapp.utils;

import android.util.Pair;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.services.SettingsProvider;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Period;
/* loaded from: classes.dex */
public class HeartRateUtils {
    public static List<Pair<Double, Integer>> getZoneInfo() {
        ArrayList<Pair<Double, Integer>> breakpoints = new ArrayList<>();
        breakpoints.add(new Pair<>(Double.valueOf(0.5d), Integer.valueOf((int) R.string.heart_rate_healthy_heart)));
        breakpoints.add(new Pair<>(Double.valueOf(0.6d), Integer.valueOf((int) R.string.heart_rate_fitness_zone)));
        breakpoints.add(new Pair<>(Double.valueOf(0.7d), Integer.valueOf((int) R.string.heart_rate_aerobic)));
        breakpoints.add(new Pair<>(Double.valueOf(0.8d), Integer.valueOf((int) R.string.heart_rate_anaerobic)));
        breakpoints.add(new Pair<>(Double.valueOf(0.9d), Integer.valueOf((int) R.string.heart_rate_red_line)));
        return breakpoints;
    }

    public static int getUserAge(SettingsProvider settingsProvider) {
        DateTime birthdate;
        CargoUserProfile profile = settingsProvider.getUserProfile();
        if (profile == null || (birthdate = profile.getBirthdate()) == null) {
            return -1;
        }
        Period period = new Period(birthdate, DateTime.now());
        int age = period.getYears();
        return age;
    }
}

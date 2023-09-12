package com.microsoft.kapp.personalization;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Pair;
import com.facebook.internal.NativeProtocol;
import com.microsoft.kapp.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
/* loaded from: classes.dex */
public class NeonPersonalizationManager extends PersonalizationManager {
    public static final int BERRY_ID = 655360;
    public static final String BERRY_NAME = "neon_berry";
    public static final int BLANK_PATTERN_ID = 10;
    public static final String BLANK_PATTERN_NAME = "";
    public static final int CALIFORNIA_ID = 983040;
    public static final String CALIFORNIA_NAME = "neon_california";
    public static final int CARGO_ID = 720896;
    public static final String CARGO_NAME = "neon_cargo";
    public static final int CHEVS_PATTERN_ID = 1;
    public static final String CHEVS_PATTERN_NAME = "_chevs";
    public static final int CORAL_ID = 524288;
    public static final String CORAL_NAME = "neon_coral";
    public static final int CURVES_PATTERN_ID = 2;
    public static final String CURVES_PATTERN_NAME = "_curves";
    public static final int CYBER_ID = 262144;
    public static final String CYBER_NAME = "neon_cyber";
    public static final int DAN_PATTERN_ID = 3;
    public static final String DAN_PATTERN_NAME = "_dan";
    public static final String DISCREET_NAME = "neon_discreet";
    public static final int DJ_ID = 917504;
    public static final String DJ_NAME = "neon_dj";
    public static final int ELECTRIC_ID = 65536;
    public static final String ELECTRIC_NAME = "neon_electric";
    public static final int FAST_PATTERN_ID = 4;
    public static final String FAST_PATTERN_NAME = "_fast";
    public static final int FIBER_PATTERN_ID = 5;
    public static final String FIBER_PATTERN_NAME = "_fiber";
    public static final int FWD_PATTERN_ID = 6;
    public static final String FWD_PATTERN_NAME = "_fwd";
    public static final int KALE_ID = 196608;
    public static final String KALE_NAME = "neon_kale";
    public static final int KILLA_BEE_ID = 1048576;
    public static final String KILLA_BEE_NAME = "neon_killabee";
    public static final int KOOLAID_ID = 589824;
    public static final String KOOLAID_NAME = "neon_koolaid";
    public static final int LASERTAG_ID = 1179648;
    public static final String LASERTAG_NAME = "neon_lasertag";
    public static final int LIME_ID = 327680;
    public static final String LIME_NAME = "neon_lime";
    public static final int NOODS_PATTERN_ID = 7;
    public static final String NOODS_PATTERN_NAME = "_noods";
    public static final int PIZZA_ID = 1114112;
    public static final String PIZZA_NAME = "neon_pizza";
    public static final int PLATES_PATTERN_ID = 8;
    public static final String PLATES_PATTERN_NAME = "_plates";
    public static final int SKYLINE_ID = 131072;
    public static final String SKYLINE_NAME = "neon_skyline";
    public static final int STORM_ID = 851968;
    public static final String STORM_NAME = "neon_storm";
    public static final int TANGERINE_ID = 393216;
    public static final String TANGERINE_NAME = "neon_tangerine";
    public static final int TANG_ID = 458752;
    public static final String TANG_NAME = "neon_tang";
    public static final int TIME_PATTERN_ID = 9;
    public static final String TIME_PATTERN_NAME = "_time";
    public static final int TUXEDO_ID = 786432;
    public static final String TUXEDO_NAME = "neon_tuxedo";

    @SuppressLint({"UseSparseArrays"})
    public NeonPersonalizationManager(Resources resources) {
        this.mThemes = new LinkedHashMap();
        this.mResources = resources;
        this.WALLPAPER_PATTERN_NAMES = new ArrayList();
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(10, ""));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(1, CHEVS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(2, CURVES_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(3, DAN_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(4, FAST_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(5, FIBER_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(6, FWD_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(7, NOODS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(8, PLATES_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(9, TIME_PATTERN_NAME));
        initThemeManager();
    }

    private void initThemeManager() {
        addTheme(65536, ELECTRIC_NAME, R.color.neon_electric_base_color, R.color.neon_electric_highlight_color, R.color.neon_electric_lowlight_color, R.color.neon_electric_medium_color, R.color.neon_electric_high_contrast_color, R.color.neon_electric_muted_color);
        addTheme(131072, SKYLINE_NAME, R.color.neon_skyline_base_color, R.color.neon_skyline_highlight_color, R.color.neon_skyline_lowlight_color, R.color.neon_skyline_medium_color, R.color.neon_skyline_high_contrast_color, R.color.neon_skyline_muted_color);
        addTheme(196608, KALE_NAME, R.color.neon_kale_base_color, R.color.neon_kale_highlight_color, R.color.neon_kale_lowlight_color, R.color.neon_kale_medium_color, R.color.neon_kale_high_contrast_color, R.color.neon_kale_muted_color);
        addTheme(262144, CYBER_NAME, R.color.neon_cyber_base_color, R.color.neon_cyber_highlight_color, R.color.neon_cyber_lowlight_color, R.color.neon_cyber_medium_color, R.color.neon_cyber_high_contrast_color, R.color.neon_cyber_muted_color);
        addTheme(327680, LIME_NAME, R.color.neon_lime_base_color, R.color.neon_lime_highlight_color, R.color.neon_lime_lowlight_color, R.color.neon_lime_medium_color, R.color.neon_lime_high_contrast_color, R.color.neon_lime_muted_color);
        addTheme(393216, TANGERINE_NAME, R.color.neon_tangerine_base_color, R.color.neon_tangerine_highlight_color, R.color.neon_tangerine_lowlight_color, R.color.neon_tangerine_medium_color, R.color.neon_tangerine_high_contrast_color, R.color.neon_tangerine_muted_color);
        addTheme(458752, TANG_NAME, R.color.neon_tang_base_color, R.color.neon_tang_highlight_color, R.color.neon_tang_lowlight_color, R.color.neon_tang_medium_color, R.color.neon_tang_high_contrast_color, R.color.neon_tang_muted_color);
        addTheme(524288, CORAL_NAME, R.color.neon_coral_base_color, R.color.neon_coral_highlight_color, R.color.neon_coral_lowlight_color, R.color.neon_coral_medium_color, R.color.neon_coral_high_contrast_color, R.color.neon_coral_muted_color);
        addTheme(589824, KOOLAID_NAME, R.color.neon_koolaid_base_color, R.color.neon_koolaid_highlight_color, R.color.neon_koolaid_lowlight_color, R.color.neon_koolaid_medium_color, R.color.neon_koolaid_high_contrast_color, R.color.neon_koolaid_muted_color);
        addTheme(655360, BERRY_NAME, R.color.neon_berry_base_color, R.color.neon_berry_highlight_color, R.color.neon_berry_lowlight_color, R.color.neon_berry_medium_color, R.color.neon_berry_high_contrast_color, R.color.neon_berry_muted_color);
        addTheme(720896, CARGO_NAME, R.color.neon_cargo_base_color, R.color.neon_cargo_highlight_color, R.color.neon_cargo_lowlight_color, R.color.neon_cargo_medium_color, R.color.neon_cargo_high_contrast_color, R.color.neon_cargo_muted_color);
        addTheme(786432, TUXEDO_NAME, R.color.neon_discreet_base_color, R.color.neon_tuxedo_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_tuxedo_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_tuxedo_muted_color);
        addTheme(851968, STORM_NAME, R.color.neon_discreet_base_color, R.color.neon_storm_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_storm_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_storm_muted_color);
        addTheme(DJ_ID, DJ_NAME, R.color.neon_discreet_base_color, R.color.neon_dj_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_dj_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_dj_muted_color);
        addTheme(CALIFORNIA_ID, CALIFORNIA_NAME, R.color.neon_discreet_base_color, R.color.neon_california_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_california_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_california_muted_color);
        addTheme(1048576, KILLA_BEE_NAME, R.color.neon_discreet_base_color, R.color.neon_killabee_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_killabee_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_killabee_muted_color);
        addTheme(PIZZA_ID, PIZZA_NAME, R.color.neon_discreet_base_color, R.color.neon_pizza_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_pizza_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_pizza_muted_color);
        addTheme(LASERTAG_ID, LASERTAG_NAME, R.color.neon_discreet_base_color, R.color.neon_lasertag_highlight_color, R.color.neon_discreet_lowlight_color, R.color.neon_lasertag_medium_color, R.color.neon_discreet_high_contrast_color, R.color.neon_lasertag_muted_color);
    }

    @Override // com.microsoft.kapp.personalization.PersonalizationManager
    public int getDefaultWallpaper() {
        return NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REPLY;
    }
}

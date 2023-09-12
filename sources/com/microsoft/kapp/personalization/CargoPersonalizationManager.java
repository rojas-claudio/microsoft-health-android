package com.microsoft.kapp.personalization;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Pair;
import com.facebook.internal.NativeProtocol;
import com.microsoft.kapp.R;
import java.util.LinkedHashMap;
/* loaded from: classes.dex */
public class CargoPersonalizationManager extends PersonalizationManager {
    public static final int ANGLE_PATTERN_ID = 1;
    public static final String ANGLE_PATTERN_NAME = "_angle";
    public static final int BLANK_PATTERN_ID = 12;
    public static final String BLANK_PATTERN_NAME = "_plain";
    public static final int BLINDS_PATTERN_ID = 2;
    public static final String BLINDS_PATTERN_NAME = "_blinds";
    public static final int CORAL_ID = 131072;
    private static final String CORAL_NAME = "coral";
    public static final int CORN_FLOWER_ID = 851968;
    private static final String CORN_FLOWER_NAME = "cornflower";
    public static final int CYBER_ID = 196608;
    private static final String CYBER_NAME = "cyber";
    public static final int ELECTRIC_CYAN_ID = 393216;
    private static final String ELECTRIC_CYAN_NAME = "electric";
    public static final int FLAME_ID = 458752;
    private static final String FLAME_NAME = "flame";
    public static final int FOLDS_PATTERN_ID = 3;
    public static final String FOLDS_PATTERN_NAME = "_folds";
    public static final int FUCHSIA_ID = 524288;
    private static final String FUCHSIA_NAME = "fuchsia";
    public static final int HONEYCOMB_PATTERN_ID = 4;
    public static final String HONEYCOMB_PATTERN_NAME = "_honeycomb";
    public static final int JOULE_ID = 262144;
    private static final String JOULE_NAME = "joule";
    public static final int LIME_ID = 589824;
    private static final String LIME_NAME = "lime";
    public static final int MESH_PATTERN_ID = 5;
    public static final String MESH_PATTERN_NAME = "_mesh";
    public static final int ORCHID_ID = 327680;
    private static final String ORCHID_NAME = "orchid";
    public static final int PENGUIN_ID = 786432;
    private static final String PENGUIN_NAME = "discrete";
    public static final int PETALS_PATTERN_ID = 6;
    public static final String PETALS_PATTERN_NAME = "_petals";
    public static final int PIXELS_PATTERN_ID = 7;
    public static final String PIXELS_PATTERN_NAME = "_pixels";
    public static final int SEQUINS_PATTERN_ID = 8;
    public static final String SEQUINS_PATTERN_NAME = "_sequins";
    public static final int STORM_ID = 655360;
    private static final String STORM_NAME = "discrete";
    public static final int STRIPES_PATTERN_ID = 9;
    public static final String STRIPES_PATTERN_NAME = "_stripes";
    public static final int TRIANGLES_PATTERN_ID = 10;
    public static final String TRIANGLES_PATTERN_NAME = "_triangles";
    public static final int TUXEDO_ID = 720896;
    private static final String TUXEDO_NAME = "discrete";
    public static final int VIOLET_ID = 65536;
    private static final String VIOLET_NAME = "violet";
    public static final int VORTEX_PATTERN_ID = 11;
    public static final String VORTEX_PATTERN_NAME = "_vortex";

    @SuppressLint({"UseSparseArrays"})
    public CargoPersonalizationManager(Resources resources) {
        this.mThemes = new LinkedHashMap();
        this.mResources = resources;
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(12, BLANK_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(1, ANGLE_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(2, BLINDS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(3, FOLDS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(4, HONEYCOMB_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(5, MESH_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(6, PETALS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(7, PIXELS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(8, SEQUINS_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(9, STRIPES_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(10, TRIANGLES_PATTERN_NAME));
        this.WALLPAPER_PATTERN_NAMES.add(new Pair<>(11, VORTEX_PATTERN_NAME));
        initThemeManager();
    }

    private void initThemeManager() {
        addTheme(851968, CORN_FLOWER_NAME, R.color.cornflower_base_color, R.color.cornflower_highlight_color, R.color.cornflower_lowlight_color, R.color.cornflower_secondary_text_color, R.color.cornflower_high_contrast_color, R.color.cornflower_muted_color);
        addTheme(65536, VIOLET_NAME, R.color.violet_base_color, R.color.violet_highlight_color, R.color.violet_lowlight_color, R.color.violet_secondary_text_color, R.color.violet_high_contrast_color, R.color.violet_muted_color);
        addTheme(131072, CORAL_NAME, R.color.coral_base_color, R.color.coral_highlight_color, R.color.coral_lowlight_color, R.color.coral_secondary_text_color, R.color.coral_high_contrast_color, R.color.coral_muted_color);
        addTheme(196608, CYBER_NAME, R.color.cyber_base_color, R.color.cyber_highlight_color, R.color.cyber_lowlight_color, R.color.cyber_secondary_text_color, R.color.cyber_high_contrast_color, R.color.cyber_muted_color);
        addTheme(262144, JOULE_NAME, R.color.joule_base_color, R.color.joule_highlight_color, R.color.joule_lowlight_color, R.color.joule_secondary_text_color, R.color.joule_high_contrast_color, R.color.joule_muted_color);
        addTheme(327680, ORCHID_NAME, R.color.orchid_base_color, R.color.orchid_highlight_color, R.color.orchid_lowlight_color, R.color.orchid_secondary_text_color, R.color.orchid_high_contrast_color, R.color.orchid_muted_color);
        addTheme(393216, ELECTRIC_CYAN_NAME, R.color.electric_base_color, R.color.electric_highlight_color, R.color.electric_lowlight_color, R.color.electric_secondary_text_color, R.color.electric_high_contrast_color, R.color.electric_muted_color);
        addTheme(458752, FLAME_NAME, R.color.flame_base_color, R.color.flame_highlight_color, R.color.flame_lowlight_color, R.color.flame_secondary_text_color, R.color.flame_high_contrast_color, R.color.flame_muted_color);
        addTheme(524288, FUCHSIA_NAME, R.color.fuchsia_base_color, R.color.fuchsia_highlight_color, R.color.fuchsia_lowlight_color, R.color.fuchsia_secondary_text_color, R.color.fuchsia_high_contrast_color, R.color.fuchsia_muted_color);
        addTheme(589824, LIME_NAME, R.color.lime_base_color, R.color.lime_highlight_color, R.color.lime_lowlight_color, R.color.lime_secondary_text_color, R.color.lime_high_contrast_color, R.color.lime_muted_color);
        addTheme(655360, "discrete", R.color.storm_base_color, R.color.storm_highlight_color, R.color.storm_lowlight_color, R.color.storm_secondary_text_color, R.color.storm_high_contrast_color, R.color.storm_muted_color);
        addTheme(720896, "discrete", R.color.tuxedo_base_color, R.color.tuxedo_highlight_color, R.color.tuxedo_lowlight_color, R.color.tuxedo_secondary_text_color, R.color.tuxedo_high_contrast_color, R.color.tuxedo_muted_color);
        addTheme(786432, "discrete", R.color.penguin_base_color, R.color.penguin_highlight_color, R.color.penguin_lowlight_color, R.color.penguin_secondary_text_color, R.color.penguin_high_contrast_color, R.color.penguin_muted_color);
    }

    @Override // com.microsoft.kapp.personalization.PersonalizationManager
    public int getDefaultWallpaper() {
        return NativeProtocol.MESSAGE_GET_INSTALL_DATA_REQUEST;
    }
}

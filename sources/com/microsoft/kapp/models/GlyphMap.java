package com.microsoft.kapp.models;

import java.util.HashMap;
/* loaded from: classes.dex */
public class GlyphMap {
    private static final HashMap<String, String> mIconAndHexcode = new HashMap<>();

    static {
        mIconAndHexcode.put("glyph_slow", "\ue218");
        mIconAndHexcode.put("glyph_fast", "\ue219");
    }

    public static String getGlyphText(String key) throws Exception {
        if (mIconAndHexcode.containsKey(key)) {
            return mIconAndHexcode.get(key);
        }
        throw new Exception("Invalid GlyphId");
    }
}

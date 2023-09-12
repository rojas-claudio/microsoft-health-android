package com.microsoft.blackbirdkeyboardtest;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class Phrases {
    public ArrayList<Phrase> mPhrases = new ArrayList<>();

    public Phrases(AssetManager assetManager, String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(filename)));
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    if (line.startsWith("Phrase\t")) {
                        Phrase phrase = new Phrase(line.substring("Phrase\t".length()));
                        this.mPhrases.add(phrase);
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            Log.d("BlackbirdKeyboardTest", "Failed to load phrase file " + filename);
            e.printStackTrace();
            throw new RuntimeException("Failed to load phrase file " + filename);
        }
    }
}

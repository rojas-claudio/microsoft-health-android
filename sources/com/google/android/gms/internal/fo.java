package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.Scopes;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public class fo {
    private String it;
    private String[] rA;
    private String rB;
    private String rC;
    private String rD;
    private String rE;
    private ArrayList<String> rF = new ArrayList<>();
    private String[] rG;

    public fo(Context context) {
        this.rC = context.getPackageName();
        this.rB = context.getPackageName();
        this.rF.add(Scopes.PLUS_LOGIN);
    }

    public fo Z(String str) {
        this.it = str;
        return this;
    }

    public fo d(String... strArr) {
        this.rF.clear();
        this.rF.addAll(Arrays.asList(strArr));
        return this;
    }

    public fo dg() {
        this.rF.clear();
        return this;
    }

    public fn dh() {
        if (this.it == null) {
            this.it = "<<default account>>";
        }
        return new fn(this.it, (String[]) this.rF.toArray(new String[this.rF.size()]), this.rG, this.rA, this.rB, this.rC, this.rD, this.rE);
    }

    public fo e(String... strArr) {
        this.rG = strArr;
        return this;
    }
}

package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class d implements SafeParcelable {
    public static final e CREATOR = new e();
    private static final a ju = new a(new String[0], null) { // from class: com.google.android.gms.common.data.d.1
    };
    private final int iC;
    private final int iM;
    private final String[] jm;
    Bundle jn;
    private final CursorWindow[] jo;
    private final Bundle jp;
    int[] jq;
    int jr;
    private Object js;
    private boolean jt;
    boolean mClosed;

    /* loaded from: classes.dex */
    public static class a {
        private final String[] jm;
        private final ArrayList<HashMap<String, Object>> jv;
        private final String jw;
        private final HashMap<Object, Integer> jx;
        private boolean jy;
        private String jz;

        private a(String[] strArr, String str) {
            this.jm = (String[]) dm.e(strArr);
            this.jv = new ArrayList<>();
            this.jw = str;
            this.jx = new HashMap<>();
            this.jy = false;
            this.jz = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.jt = true;
        this.iM = i;
        this.jm = strArr;
        this.jo = cursorWindowArr;
        this.iC = i2;
        this.jp = bundle;
    }

    private d(a aVar, int i, Bundle bundle) {
        this(aVar.jm, a(aVar), i, bundle);
    }

    public d(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.jt = true;
        this.iM = 1;
        this.jm = (String[]) dm.e(strArr);
        this.jo = (CursorWindow[]) dm.e(cursorWindowArr);
        this.iC = i;
        this.jp = bundle;
        aJ();
    }

    public static d a(int i, Bundle bundle) {
        return new d(ju, i, bundle);
    }

    private static CursorWindow[] a(a aVar) {
        int i;
        int i2;
        int i3;
        CursorWindow cursorWindow;
        if (aVar.jm.length == 0) {
            return new CursorWindow[0];
        }
        ArrayList arrayList = aVar.jv;
        int size = arrayList.size();
        CursorWindow cursorWindow2 = new CursorWindow(false);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(cursorWindow2);
        cursorWindow2.setNumColumns(aVar.jm.length);
        int i4 = 0;
        int i5 = 0;
        while (i4 < size) {
            try {
                if (cursorWindow2.allocRow()) {
                    i = i5;
                } else {
                    Log.d("DataHolder", "Allocating additional cursor window for large data set (row " + i4 + ")");
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setNumColumns(aVar.jm.length);
                    arrayList2.add(cursorWindow2);
                    if (!cursorWindow2.allocRow()) {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        arrayList2.remove(cursorWindow2);
                        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
                    }
                    i = 0;
                }
                Map map = (Map) arrayList.get(i4);
                boolean z = true;
                for (int i6 = 0; i6 < aVar.jm.length && z; i6++) {
                    String str = aVar.jm[i6];
                    Object obj = map.get(str);
                    if (obj == null) {
                        z = cursorWindow2.putNull(i, i6);
                    } else if (obj instanceof String) {
                        z = cursorWindow2.putString((String) obj, i, i6);
                    } else if (obj instanceof Long) {
                        z = cursorWindow2.putLong(((Long) obj).longValue(), i, i6);
                    } else if (obj instanceof Integer) {
                        z = cursorWindow2.putLong(((Integer) obj).intValue(), i, i6);
                    } else if (obj instanceof Boolean) {
                        z = cursorWindow2.putLong(((Boolean) obj).booleanValue() ? 1L : 0L, i, i6);
                    } else if (!(obj instanceof byte[])) {
                        throw new IllegalArgumentException("Unsupported object for column " + str + ": " + obj);
                    } else {
                        z = cursorWindow2.putBlob((byte[]) obj, i, i6);
                    }
                }
                if (z) {
                    i2 = i + 1;
                    i3 = i4;
                    cursorWindow = cursorWindow2;
                } else {
                    Log.d("DataHolder", "Couldn't populate window data for row " + i4 + " - allocating new window.");
                    cursorWindow2.freeLastRow();
                    CursorWindow cursorWindow3 = new CursorWindow(false);
                    cursorWindow3.setNumColumns(aVar.jm.length);
                    arrayList2.add(cursorWindow3);
                    i3 = i4 - 1;
                    cursorWindow = cursorWindow3;
                    i2 = 0;
                }
                cursorWindow2 = cursorWindow;
                i4 = i3 + 1;
                i5 = i2;
            } catch (RuntimeException e) {
                int size2 = arrayList2.size();
                for (int i7 = 0; i7 < size2; i7++) {
                    ((CursorWindow) arrayList2.get(i7)).close();
                }
                throw e;
            }
        }
        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
    }

    private void b(String str, int i) {
        if (this.jn == null || !this.jn.containsKey(str)) {
            throw new IllegalArgumentException("No such column: " + str);
        }
        if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        }
        if (i < 0 || i >= this.jr) {
            throw new CursorIndexOutOfBoundsException(i, this.jr);
        }
    }

    public static d r(int i) {
        return a(i, null);
    }

    public long a(String str, int i, int i2) {
        b(str, i);
        return this.jo[i2].getLong(i - this.jq[i2], this.jn.getInt(str));
    }

    public void a(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        b(str, i);
        this.jo[i2].copyStringToBuffer(i - this.jq[i2], this.jn.getInt(str), charArrayBuffer);
    }

    public void aJ() {
        this.jn = new Bundle();
        for (int i = 0; i < this.jm.length; i++) {
            this.jn.putInt(this.jm[i], i);
        }
        this.jq = new int[this.jo.length];
        int i2 = 0;
        for (int i3 = 0; i3 < this.jo.length; i3++) {
            this.jq[i3] = i2;
            i2 += this.jo[i3].getNumRows();
        }
        this.jr = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] aK() {
        return this.jm;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CursorWindow[] aL() {
        return this.jo;
    }

    public Bundle aM() {
        return this.jp;
    }

    public int b(String str, int i, int i2) {
        b(str, i);
        return this.jo[i2].getInt(i - this.jq[i2], this.jn.getInt(str));
    }

    public void b(Object obj) {
        this.js = obj;
    }

    public String c(String str, int i, int i2) {
        b(str, i);
        return this.jo[i2].getString(i - this.jq[i2], this.jn.getInt(str));
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.jo.length; i++) {
                    this.jo[i].close();
                }
            }
        }
    }

    public boolean d(String str, int i, int i2) {
        b(str, i);
        return Long.valueOf(this.jo[i2].getLong(i - this.jq[i2], this.jn.getInt(str))).longValue() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte[] e(String str, int i, int i2) {
        b(str, i);
        return this.jo[i2].getBlob(i - this.jq[i2], this.jn.getInt(str));
    }

    public Uri f(String str, int i, int i2) {
        String c = c(str, i, i2);
        if (c == null) {
            return null;
        }
        return Uri.parse(c);
    }

    protected void finalize() throws Throwable {
        try {
            if (this.jt && this.jo.length > 0 && !isClosed()) {
                Log.e("DataHolder", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call close() on all DataBuffer extending objects when you are done with them. (" + (this.js == null ? "internal object: " + toString() : this.js.toString()) + ")");
                close();
            }
        } finally {
            super.finalize();
        }
    }

    public boolean g(String str, int i, int i2) {
        b(str, i);
        return this.jo[i2].isNull(i - this.jq[i2], this.jn.getInt(str));
    }

    public int getCount() {
        return this.jr;
    }

    public int getStatusCode() {
        return this.iC;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public int q(int i) {
        int i2 = 0;
        dm.k(i >= 0 && i < this.jr);
        while (true) {
            if (i2 >= this.jq.length) {
                break;
            } else if (i < this.jq[i2]) {
                i2--;
                break;
            } else {
                i2++;
            }
        }
        return i2 == this.jq.length ? i2 - 1 : i2;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        e.a(this, dest, flags);
    }
}

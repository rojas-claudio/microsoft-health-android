package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes.dex */
public abstract class a<T extends LifecycleDelegate> {
    private T lV;
    private Bundle lW;
    private LinkedList<InterfaceC0003a> lX;
    private final d<T> lY = (d<T>) new d<T>() { // from class: com.google.android.gms.dynamic.a.1
        @Override // com.google.android.gms.dynamic.d
        public void a(T t) {
            a.this.lV = t;
            Iterator it = a.this.lX.iterator();
            while (it.hasNext()) {
                ((InterfaceC0003a) it.next()).b(a.this.lV);
            }
            a.this.lX.clear();
            a.this.lW = null;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.android.gms.dynamic.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public interface InterfaceC0003a {
        void b(LifecycleDelegate lifecycleDelegate);

        int getState();
    }

    private void J(int i) {
        while (!this.lX.isEmpty() && this.lX.getLast().getState() >= i) {
            this.lX.removeLast();
        }
    }

    private void a(Bundle bundle, InterfaceC0003a interfaceC0003a) {
        if (this.lV != null) {
            interfaceC0003a.b(this.lV);
            return;
        }
        if (this.lX == null) {
            this.lX = new LinkedList<>();
        }
        this.lX.add(interfaceC0003a);
        if (bundle != null) {
            if (this.lW == null) {
                this.lW = (Bundle) bundle.clone();
            } else {
                this.lW.putAll(bundle);
            }
        }
        a(this.lY);
    }

    public void a(FrameLayout frameLayout) {
        final Context context = frameLayout.getContext();
        final int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        String b = GooglePlayServicesUtil.b(context, isGooglePlayServicesAvailable, -1);
        String b2 = GooglePlayServicesUtil.b(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(b);
        linearLayout.addView(textView);
        if (b2 != null) {
            Button button = new Button(context);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(b2);
            linearLayout.addView(button);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.gms.dynamic.a.5
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    context.startActivity(GooglePlayServicesUtil.a(context, isGooglePlayServicesAvailable, -1));
                }
            });
        }
    }

    protected abstract void a(d<T> dVar);

    public T bP() {
        return this.lV;
    }

    public void onCreate(final Bundle savedInstanceState) {
        a(savedInstanceState, new InterfaceC0003a() { // from class: com.google.android.gms.dynamic.a.3
            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public void b(LifecycleDelegate lifecycleDelegate) {
                a.this.lV.onCreate(savedInstanceState);
            }

            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public int getState() {
                return 1;
            }
        });
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        a(savedInstanceState, new InterfaceC0003a() { // from class: com.google.android.gms.dynamic.a.4
            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public void b(LifecycleDelegate lifecycleDelegate) {
                frameLayout.removeAllViews();
                frameLayout.addView(a.this.lV.onCreateView(inflater, container, savedInstanceState));
            }

            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public int getState() {
                return 2;
            }
        });
        if (this.lV == null) {
            a(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.lV != null) {
            this.lV.onDestroy();
        } else {
            J(1);
        }
    }

    public void onDestroyView() {
        if (this.lV != null) {
            this.lV.onDestroyView();
        } else {
            J(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle attrs, final Bundle savedInstanceState) {
        a(savedInstanceState, new InterfaceC0003a() { // from class: com.google.android.gms.dynamic.a.2
            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public void b(LifecycleDelegate lifecycleDelegate) {
                a.this.lV.onInflate(activity, attrs, savedInstanceState);
            }

            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public int getState() {
                return 0;
            }
        });
    }

    public void onLowMemory() {
        if (this.lV != null) {
            this.lV.onLowMemory();
        }
    }

    public void onPause() {
        if (this.lV != null) {
            this.lV.onPause();
        } else {
            J(3);
        }
    }

    public void onResume() {
        a((Bundle) null, new InterfaceC0003a() { // from class: com.google.android.gms.dynamic.a.6
            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public void b(LifecycleDelegate lifecycleDelegate) {
                a.this.lV.onResume();
            }

            @Override // com.google.android.gms.dynamic.a.InterfaceC0003a
            public int getState() {
                return 3;
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.lV != null) {
            this.lV.onSaveInstanceState(outState);
        } else if (this.lW != null) {
            outState.putAll(this.lW);
        }
    }
}

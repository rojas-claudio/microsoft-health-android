package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.c;
import com.google.android.gms.dynamic.d;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.internal.q;
import com.google.android.gms.maps.model.RuntimeRemoteException;
/* loaded from: classes.dex */
public class MapView extends FrameLayout {
    private GoogleMap pI;
    private final b pM;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a implements LifecycleDelegate {
        private final ViewGroup pN;
        private final IMapViewDelegate pO;
        private View pP;

        public a(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.pO = (IMapViewDelegate) dm.e(iMapViewDelegate);
            this.pN = (ViewGroup) dm.e(viewGroup);
        }

        public IMapViewDelegate cF() {
            return this.pO;
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onCreate(Bundle savedInstanceState) {
            try {
                this.pO.onCreate(savedInstanceState);
                this.pP = (View) c.b(this.pO.getView());
                this.pN.removeAllViews();
                this.pN.addView(this.pP);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onDestroy() {
            try {
                this.pO.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onLowMemory() {
            try {
                this.pO.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onPause() {
            try {
                this.pO.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onResume() {
            try {
                this.pO.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onSaveInstanceState(Bundle outState) {
            try {
                this.pO.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    /* loaded from: classes.dex */
    static class b extends com.google.android.gms.dynamic.a<a> {
        private final Context mContext;
        protected d<a> pL;
        private final ViewGroup pQ;
        private final GoogleMapOptions pR;

        b(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.pQ = viewGroup;
            this.mContext = context;
            this.pR = googleMapOptions;
        }

        @Override // com.google.android.gms.dynamic.a
        protected void a(d<a> dVar) {
            this.pL = dVar;
            cE();
        }

        public void cE() {
            if (this.pL == null || bP() != null) {
                return;
            }
            try {
                this.pL.a(new a(this.pQ, q.u(this.mContext).a(c.g(this.mContext), this.pR)));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }
    }

    public MapView(Context context) {
        super(context);
        this.pM = new b(this, context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pM = new b(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.pM = new b(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
    }

    public MapView(Context context, GoogleMapOptions options) {
        super(context);
        this.pM = new b(this, context, options);
    }

    public final GoogleMap getMap() {
        if (this.pI != null) {
            return this.pI;
        }
        this.pM.cE();
        if (this.pM.bP() == null) {
            return null;
        }
        try {
            this.pI = new GoogleMap(this.pM.bP().cF().getMap());
            return this.pI;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void onCreate(Bundle savedInstanceState) {
        this.pM.onCreate(savedInstanceState);
        if (this.pM.bP() == null) {
            this.pM.a(this);
        }
    }

    public final void onDestroy() {
        this.pM.onDestroy();
    }

    public final void onLowMemory() {
        this.pM.onLowMemory();
    }

    public final void onPause() {
        this.pM.onPause();
    }

    public final void onResume() {
        this.pM.onResume();
    }

    public final void onSaveInstanceState(Bundle outState) {
        this.pM.onSaveInstanceState(outState);
    }
}

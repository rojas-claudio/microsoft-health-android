package com.microsoft.kapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.SignInActivity;
import com.microsoft.kapp.activities.SplashActivity;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.auth.CredentialsFetcherAsync;
import com.microsoft.krestsdk.auth.SignInContext;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.auth.credentials.KdsCredential;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
import java.io.File;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class DebugActionsFragment extends BaseFragment {
    @Inject
    CacheService mCacheService;
    @Inject
    CredentialsManager mCredentialsManager;
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.debug_fragment_actions, container, false);
        Button flushCacheButton = (Button) rootView.findViewById(R.id.debug_actions_flush_cache);
        Button clearNetworkCacheButton = (Button) rootView.findViewById(R.id.debug_actions_clear_network_cache);
        Button resetFreButton = (Button) rootView.findViewById(R.id.debug_actions_reset_fre);
        Button authenticateButton = (Button) rootView.findViewById(R.id.debug_actions_authenticate);
        Button freButton = (Button) rootView.findViewById(R.id.debug_actions_fre);
        Button registerDeviceButton = (Button) rootView.findViewById(R.id.debug_actions_register_device);
        Button unregisterDeviceButton = (Button) rootView.findViewById(R.id.debug_actions_unregister_device);
        Button invalidateOauthButton = (Button) rootView.findViewById(R.id.debug_actions_invalidate_oauth);
        Button resetOOBECompleteButton = (Button) rootView.findViewById(R.id.debug_actions_reset_oobe_complete);
        Button setOOBECompleteButton = (Button) rootView.findViewById(R.id.debug_actions_set_oobe_complete);
        Button setNotificationListeners = (Button) rootView.findViewById(R.id.debug_actions_set_notification_listeners);
        Button deleteCredentialsButton = (Button) ViewUtils.getValidView(rootView, R.id.debug_actions_delete_credentials, Button.class);
        setOOBECompleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.setOOBEComplete();
            }
        });
        clearNetworkCacheButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.clearNetworkCache();
            }
        });
        resetOOBECompleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.resetOOBEComplete();
            }
        });
        unregisterDeviceButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.unregisterDevice();
            }
        });
        registerDeviceButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.registerDevice();
            }
        });
        flushCacheButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.flushCache();
            }
        });
        resetFreButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.resetFre();
            }
        });
        invalidateOauthButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.invalidateOauth();
            }
        });
        authenticateButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.authenticate();
            }
        });
        freButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.fre();
            }
        });
        setNotificationListeners.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.goToNotificationSettingsPage();
            }
        });
        deleteCredentialsButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.12
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugActionsFragment.this.deleteCredentials();
            }
        });
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteCredentials() {
        this.mCredentialsManager.deleteCredentials(getActivity());
    }

    protected void clearNetworkCache() {
        this.mCacheService.removeAll();
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File file : children) {
                deleteDir(file);
            }
        }
        dir.delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateOauth() {
        CredentialsFetcherAsync credentialsFetcher = new CredentialsFetcherAsync(this.mCredentialsManager);
        credentialsFetcher.execute(new Callback<KCredential>() { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.13
            @Override // com.microsoft.kapp.Callback
            public void callback(KCredential credentials) {
                if (credentials != null) {
                    MsaCredential currentCredential = credentials.getMsaCredential();
                    MsaCredential msaCredential = new MsaCredential(currentCredential.getAccessToken(), currentCredential.getRefreshToken(), new DateTime(0L));
                    KdsCredential kdsCredential = credentials.getKdsCredential();
                    KCredential expiredCredential = new KCredential(msaCredential, kdsCredential);
                    DebugActionsFragment.this.mCredentialsManager.setCredentials(expiredCredential);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authenticate() {
        Intent signInIntent = new Intent(getActivity(), SignInActivity.class);
        signInIntent.putExtra(SignInActivity.ARG_IN_SIGN_CONTEXT, SignInContext.MANUAL);
        getActivity().startActivity(signInIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fre() {
        Intent freIntent = new Intent(getActivity(), SignInActivity.class);
        freIntent.putExtra(SignInActivity.ARG_IN_SIGN_CONTEXT, SignInContext.FRE);
        getActivity().startActivity(freIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void flushCache() {
        File cacheDir = getActivity().getCacheDir();
        if (cacheDir != null) {
            deleteDir(cacheDir);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetOOBEComplete() {
        ScopedAsyncTask<Void, Void, Void> task = new ScopedAsyncTask<Void, Void, Void>(this) { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.14
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public Void doInBackground(Void... params) {
                try {
                    DebugActionsFragment.this.mCargoConnection.resetOOBEComplete();
                    return null;
                } catch (Exception ex) {
                    setException(ex);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Void result) {
                if (getException() != null) {
                    ToastUtils.showLongToast(DebugActionsFragment.this.getActivity(), getException().getMessage());
                } else {
                    ToastUtils.showShortToast(DebugActionsFragment.this.getActivity(), "resetOOBEComplete succeeded.");
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOOBEComplete() {
        ScopedAsyncTask<Void, Void, Void> task = new ScopedAsyncTask<Void, Void, Void>(this) { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.15
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public Void doInBackground(Void... params) {
                try {
                    DebugActionsFragment.this.mCargoConnection.setOOBEComplete();
                    return null;
                } catch (Exception ex) {
                    setException(ex);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Void result) {
                if (getException() != null) {
                    ToastUtils.showLongToast(DebugActionsFragment.this.getActivity(), getException().getMessage());
                } else {
                    ToastUtils.showShortToast(DebugActionsFragment.this.getActivity(), "setOOBEComplete succeeded.");
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDevice() {
        ScopedAsyncTask<Void, Void, Void> task = new ScopedAsyncTask<Void, Void, Void>(this) { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.16
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public Void doInBackground(Void... params) {
                try {
                    DebugActionsFragment.this.mCargoConnection.linkDeviceToProfile(true);
                    return null;
                } catch (Exception ex) {
                    setException(ex);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Void result) {
                if (getException() != null) {
                    ToastUtils.showLongToast(DebugActionsFragment.this.getActivity(), getException().getMessage());
                } else {
                    ToastUtils.showShortToast(DebugActionsFragment.this.getActivity(), "register device succeeded.");
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDevice() {
        ScopedAsyncTask<Void, Void, Void> task = new ScopedAsyncTask<Void, Void, Void>(this) { // from class: com.microsoft.kapp.fragments.DebugActionsFragment.17
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public Void doInBackground(Void... params) {
                try {
                    DebugActionsFragment.this.mCargoConnection.unlinkDeviceToProfile();
                    return null;
                } catch (Exception ex) {
                    setException(ex);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Void result) {
                if (getException() != null) {
                    ToastUtils.showLongToast(DebugActionsFragment.this.getActivity(), getException().getMessage());
                } else {
                    ToastUtils.showShortToast(DebugActionsFragment.this.getActivity(), "unregister device succeeded.");
                }
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetFre() {
        Context context = getActivity();
        this.mCacheService.cleanup();
        this.mCredentialsManager.deleteCredentials(context);
        this.mSettingsProvider.setFreStatus(FreStatus.NOT_SHOWN);
        Intent freIntent = new Intent(context, SplashActivity.class);
        startActivity(freIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToNotificationSettingsPage() {
        Intent intent = new Intent(Constants.INTENT_ANDROID_NOTIFICATION_LISTENER);
        startActivity(intent);
    }
}

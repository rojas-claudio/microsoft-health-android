package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.krestsdk.auth.CredentialsFetcherAsync;
import com.microsoft.krestsdk.auth.TokenOperations;
import com.microsoft.krestsdk.auth.credentials.AccountMetadata;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
import javax.inject.Inject;
import org.joda.time.DateTimeZone;
/* loaded from: classes.dex */
public class CredentialsFragment extends BaseFragment {
    @Inject
    CredentialsManager mCredentialsManager;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.debug_fragment_credential, container, false);
        final TextView endpointView = (TextView) rootView.findViewById(R.id.endpoint);
        final TextView userIdView = (TextView) rootView.findViewById(R.id.user_id);
        final TextView expiresView = (TextView) rootView.findViewById(R.id.expiration);
        final TextView rawKatView = (TextView) rootView.findViewById(R.id.access_token_kat);
        final TextView rawAcsView = (TextView) rootView.findViewById(R.id.access_token_acs);
        final Button mailCredentialButton = (Button) rootView.findViewById(R.id.button_mailcredential);
        CredentialsFetcherAsync credentialsFetcher = new CredentialsFetcherAsync(this.mCredentialsManager);
        credentialsFetcher.execute(new Callback<KCredential>() { // from class: com.microsoft.kapp.fragments.CredentialsFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(final KCredential credentials) {
                if (credentials != null) {
                    final MsaCredential msaCredential = credentials.getMsaCredential();
                    final AccountMetadata accountMetadata = credentials.getAccountMetadata();
                    endpointView.setText(credentials.getEndPoint());
                    userIdView.setText(accountMetadata.getUserId());
                    expiresView.setText(TokenOperations.extractExpirationFromSwt(credentials.getAccessToken()).toDateTime(DateTimeZone.getDefault()).toString());
                    rawKatView.setText(credentials.getAccessToken());
                    rawAcsView.setText(msaCredential.getAccessToken());
                    mailCredentialButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.CredentialsFragment.1.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View arg0) {
                            String.format("Credentials for %s Endpoint %s", accountMetadata.getUserId(), credentials.getEndPoint());
                            String str = "KAT:<br/>" + TextUtils.htmlEncode(credentials.getAccessToken()) + "<br/><br/>ACS Token:<br/>" + TextUtils.htmlEncode(msaCredential.getAccessToken());
                            FeedbackUtilsV1.sendFeedbackAsync(CredentialsFragment.this.getView(), CredentialsFragment.this.getActivity(), CredentialsFragment.this.mCargoConnection);
                        }
                    });
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
            }
        });
        return rootView;
    }
}

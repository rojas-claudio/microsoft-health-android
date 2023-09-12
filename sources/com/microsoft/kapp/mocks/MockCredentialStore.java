package com.microsoft.kapp.mocks;

import com.microsoft.krestsdk.auth.credentials.CredentialStore;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.auth.credentials.KdsCredential;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MockCredentialStore implements CredentialStore {
    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public KCredential getCredentials() {
        MsaCredential msaCredential = new MsaCredential("http%3a%2f%2fschemas.xmlsoap.org%2fws%2f2005%2f05%2fidentity%2fclaims%2fnameidentifier=WwD3j3G0hn141agvCV%2fXDZvh0%2bCLGWXAnTKNc8B777U%3d&http%3a%2f%2fschemas.microsoft.com%2faccesscontrolservice%2f2010%2f07%2fclaims%2fidentityprovider=uri%3aWindowsLiveID&Audience=https%3a%2f%2fuat1001-kds-eus2-0.cloudapp.net%2f&ExpiresOn=1384390972&Issuer=https%3a%2f%2ftoledo.accesscontrol.windows.net%2f&HMACSHA256=7p2L9At3ln2JpcsXBLNJNNdClbYvtd4MnRrDWKq5lxE%3d", "", DateTime.now().plusDays(1));
        KdsCredential kdsCredential = new KdsCredential("ODSUserId=c3baec17-0eb1-49c8-8802-4b53733e54d0&LFSUserId=8c3db956-84b1-46ad-b647-8599fe48afe5&PoolEndpoint=https://uat1001-phs-eus2-0.cloudapp.net&PodId=1&Height=0&Weight=0&Age=33&Gender=Male&FirstName=KUser&LastName=KUser&Email=&NameIdentifier=WwD3j3G0hn141agvCV/XDZvh0+CLGWXAnTKNc8B777U=&IdProv=&CreatedNew=False&ExpiresOn=1384390972&HMACSHA256=BHaKy3SSS9sPRv5sSouNS2/Y3khA9SPboE1vwtDMAJF6WEZPCmkSby5eW98oBrlEON6wfXSSPbFIOTQA+S0wrR17ylDPcemXsUvqFXqUrPizTxR2C1IHy27WWhliXg9Jn3w3XnUBqUdPMSXtSnOn/5lsq7w0iymAi4ge79dMSmHwabNXBT7yOJ8Xz+njTCEvhOfqSXtxQQM4trvXl77TR8i5E4M44JKmFBZag1R1haL4ri5B8yVqS+C9NDgw48qK8RklYp6ZQ6i5iN7Lx+qRRnGm0L9eMC8611lVEpHQ4XbCK2b0YI2zC4e9iErU2aIhpI7/Tz2etY6mbIkg+Et7nQ==", "https://uat1001-phs-eus2-0.cloudapp.net", "https://uat1001-kds-eus2-0.cloudapp.net/", "281a8ae9-2f1a-494b-8593-107ba20378b1", "https://login.live.com/login.srf?wa=wsignin1.0&wtrealm=https%3a%2f%2faccesscontrol.windows.net%2f&wreply=https%3a%2f%2ftoledo.accesscontrol.windows.net%2fv2%2fwsfederation&wp=MBI_FED_SSL&wctx=cHI9amF2YXNjcmlwdG5vdGlmeSZybT1odHRwcyUzYSUyZiUyZnVhdDEwMDEta2RzLWV1czItMC5jbG91ZGFwcC5uZXQlMmY1", "", "", "", "2013-01-01T00:00:00.000+00:00");
        KCredential credentials = new KCredential(msaCredential, kdsCredential);
        return credentials;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public void setCredentials(KCredential credentials) {
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialStore
    public void deleteCredentials() {
    }
}

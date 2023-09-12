package com.microsoft.kapp.version;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.StringReader;
import java.net.URI;
import javax.inject.Inject;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;
/* loaded from: classes.dex */
public class DefaultApplicationVersionRetriever implements ApplicationVersionRetriever {
    private static final String TAG = ApplicationVersionRetriever.class.getSimpleName();
    private static final URI VERSION_CHECK_URI = URI.create(Constants.VERSION_CHECK_URI);
    private final NetworkProvider mProvider;

    @Inject
    public DefaultApplicationVersionRetriever(NetworkProvider provider) {
        Validate.notNull(provider, "provider");
        this.mProvider = provider;
    }

    @Override // com.microsoft.kapp.version.ApplicationVersionRetriever, com.microsoft.kapp.version.VersionRetriever
    public VersionUpdate getLatestVersion() throws VersionCheckException {
        try {
            String content = this.mProvider.executeHttpGet(VERSION_CHECK_URI.toString(), null);
            if (StringUtils.isBlank(content)) {
                KLog.w(TAG, "Version.xml returned empty response.");
                throw new VersionCheckException("Version.xml returned empty response.");
            }
            KLog.d(TAG, "Version.xml: %s", content);
            try {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xPath = factory.newXPath();
                XPathExpression expression = xPath.compile("AppVersion/Min");
                InputSource source = new InputSource(new StringReader(content));
                String version = expression.evaluate(source);
                if (StringUtils.isBlank(version)) {
                    KLog.w(TAG, "Version.xml did not contain any version.");
                    throw new VersionCheckException("Version.xml did not contain any version.");
                }
                try {
                    return new VersionUpdate(Version.parse(version), true);
                } catch (IllegalArgumentException ex) {
                    KLog.w(TAG, "Version.xml contains invalid version.", ex);
                    throw new VersionCheckException("Version.xml contains invalid version.", ex);
                }
            } catch (XPathExpressionException ex2) {
                KLog.w(TAG, "Failed to parse Version.xml.", ex2);
                throw new VersionCheckException("Failed to parse Version.xml.", ex2);
            }
        } catch (Exception ex3) {
            KLog.d(TAG, "Failed to download the latest application version.", ex3);
            throw new VersionCheckException("Failed to download the latest application version.", ex3);
        }
    }
}

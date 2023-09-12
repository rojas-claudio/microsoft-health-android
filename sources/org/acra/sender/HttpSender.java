package org.acra.sender;

import android.net.Uri;
import android.util.Log;
import com.microsoft.kapp.utils.Constants;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.util.HttpRequest;
import org.acra.util.JSONReportBuilder;
/* loaded from: classes.dex */
public class HttpSender implements ReportSender {
    private final Uri mFormUri;
    private final Map<ReportField, String> mMapping;
    private final Method mMethod;
    private final Type mType;

    /* loaded from: classes.dex */
    public enum Method {
        POST,
        PUT
    }

    /* loaded from: classes.dex */
    public enum Type {
        FORM { // from class: org.acra.sender.HttpSender.Type.1
            @Override // org.acra.sender.HttpSender.Type
            public String getContentType() {
                return Constants.APPLICATION_URL_ENCODED;
            }
        },
        JSON { // from class: org.acra.sender.HttpSender.Type.2
            @Override // org.acra.sender.HttpSender.Type
            public String getContentType() {
                return "application/json";
            }
        };

        public abstract String getContentType();
    }

    public HttpSender(Method method, Type type, Map<ReportField, String> mapping) {
        this.mMethod = method;
        this.mFormUri = null;
        this.mMapping = mapping;
        this.mType = type;
    }

    public HttpSender(Method method, Type type, String formUri, Map<ReportField, String> mapping) {
        this.mMethod = method;
        this.mFormUri = Uri.parse(formUri);
        this.mMapping = mapping;
        this.mType = type;
    }

    @Override // org.acra.sender.ReportSender
    public void send(CrashReportData report) throws ReportSenderException {
        String reportAsString;
        try {
            URL reportUrl = this.mFormUri == null ? new URL(ACRA.getConfig().formUri()) : new URL(this.mFormUri.toString());
            Log.d(ACRA.LOG_TAG, "Connect to " + reportUrl.toString());
            String login = ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthLogin()) ? null : ACRA.getConfig().formUriBasicAuthLogin();
            String password = ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthPassword()) ? null : ACRA.getConfig().formUriBasicAuthPassword();
            HttpRequest request = new HttpRequest();
            request.setConnectionTimeOut(ACRA.getConfig().connectionTimeout());
            request.setSocketTimeOut(ACRA.getConfig().socketTimeout());
            request.setMaxNrRetries(ACRA.getConfig().maxNumberOfRequestRetries());
            request.setLogin(login);
            request.setPassword(password);
            request.setHeaders(ACRA.getConfig().getHttpHeaders());
            switch (this.mType) {
                case JSON:
                    reportAsString = report.toJSON().toString();
                    break;
                default:
                    Map<String, String> finalReport = remap(report);
                    reportAsString = HttpRequest.getParamsAsFormString(finalReport);
                    break;
            }
            switch (this.mMethod) {
                case POST:
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown method: " + this.mMethod.name());
                case PUT:
                    reportUrl = new URL(reportUrl.toString() + '/' + report.getProperty(ReportField.REPORT_ID));
                    break;
            }
            request.send(reportUrl, this.mMethod, reportAsString, this.mType);
        } catch (IOException e) {
            throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), e);
        } catch (JSONReportBuilder.JSONReportException e2) {
            throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), e2);
        }
    }

    private Map<String, String> remap(Map<ReportField, String> report) {
        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }
        Map<String, String> finalReport = new HashMap<>(report.size());
        ReportField[] arr$ = fields;
        for (ReportField field : arr$) {
            if (this.mMapping == null || this.mMapping.get(field) == null) {
                finalReport.put(field.toString(), report.get(field));
            } else {
                finalReport.put(this.mMapping.get(field), report.get(field));
            }
        }
        return finalReport;
    }
}

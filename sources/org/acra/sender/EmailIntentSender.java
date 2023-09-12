package org.acra.sender;

import android.content.Context;
import android.content.Intent;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.kapp.telephony.MmsColumns;
import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
/* loaded from: classes.dex */
public class EmailIntentSender implements ReportSender {
    private final Context mContext;

    public EmailIntentSender(Context ctx) {
        this.mContext = ctx;
    }

    @Override // org.acra.sender.ReportSender
    public void send(CrashReportData errorContent) throws ReportSenderException {
        String subject = this.mContext.getPackageName() + " Crash Report";
        String body = buildBody(errorContent);
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.addFlags(268435456);
        emailIntent.setType(MmsColumns.MMS_PART_TYPE_TEXT);
        emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        emailIntent.putExtra("android.intent.extra.TEXT", body);
        emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{ACRA.getConfig().mailTo()});
        this.mContext.startActivity(emailIntent);
    }

    private String buildBody(CrashReportData errorContent) {
        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS;
        }
        StringBuilder builder = new StringBuilder();
        ReportField[] arr$ = fields;
        for (ReportField field : arr$) {
            builder.append(field.toString()).append(SimpleComparison.EQUAL_TO_OPERATION);
            builder.append((String) errorContent.get(field));
            builder.append('\n');
        }
        return builder.toString();
    }
}

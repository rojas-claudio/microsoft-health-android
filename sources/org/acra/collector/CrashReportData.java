package org.acra.collector;

import java.util.EnumMap;
import org.acra.ReportField;
import org.acra.util.JSONReportBuilder;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class CrashReportData extends EnumMap<ReportField, String> {
    private static final long serialVersionUID = 4112578634029874840L;

    public CrashReportData() {
        super(ReportField.class);
    }

    public String getProperty(ReportField key) {
        return (String) super.get(key);
    }

    public JSONObject toJSON() throws JSONReportBuilder.JSONReportException {
        return JSONReportBuilder.buildJSONReport(this);
    }
}

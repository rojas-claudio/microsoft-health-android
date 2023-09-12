package com.microsoft.krestsdk.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes.dex */
class ODataRequest {
    private String mBasePath;
    private Map<String, String> mArguments = new HashMap();
    private Map<String, String> mParameters = new HashMap();
    private Map<String, String> mNonOdataParameters = new HashMap();

    public ODataRequest(String basePath) {
        this.mBasePath = basePath;
    }

    public void addArgument(String name, String value) {
        this.mArguments.put(name, value);
    }

    public void addArgumentQuotes(String name, String value) {
        addArgument(name, "'" + value + "'");
    }

    public void addParameter(String name, String value) {
        this.mParameters.put(name, value);
    }

    public void addNonOdataParameter(String name, String value) {
        this.mNonOdataParameters.put(name, value);
    }

    public void addNonOdataParameterQuotes(String name, String value) {
        this.mNonOdataParameters.put(name, "'" + value + "'");
    }

    public void setFilter(String filter, Object... args) {
        if (this.mParameters.containsKey("filter")) {
            this.mParameters.remove("filter");
        }
        addParameter("filter", String.format(Locale.US, filter, args));
    }

    public String getUrl() {
        StringBuilder builder = new StringBuilder("{baseUrl}" + this.mBasePath);
        if (this.mArguments != null && this.mArguments.size() > 0) {
            builder.append('(');
            appendArgumentsString(builder);
            builder.append(')');
        }
        boolean firstParameter = true;
        Iterator iterator = this.mParameters.entrySet().iterator();
        while (iterator.hasNext()) {
            if (firstParameter) {
                builder.append('?');
                firstParameter = false;
            } else {
                builder.append('&');
            }
            Map.Entry<String, String> pair = iterator.next();
            builder.append('$');
            builder.append(pair.getKey().toString());
            builder.append('=');
            appendUriComponent(builder, pair.getValue().toString());
            iterator.remove();
        }
        Iterator iterator2 = this.mNonOdataParameters.entrySet().iterator();
        while (iterator2.hasNext()) {
            if (firstParameter) {
                builder.append('?');
                firstParameter = false;
            } else {
                builder.append('&');
            }
            Map.Entry<String, String> pair2 = iterator2.next();
            builder.append(pair2.getKey().toString());
            builder.append('=');
            appendUriComponent(builder, pair2.getValue().toString());
            iterator2.remove();
        }
        return builder.toString();
    }

    private void appendArgumentsString(StringBuilder builder) {
        boolean firstArgument = true;
        Iterator iterator = this.mArguments.entrySet().iterator();
        while (iterator.hasNext()) {
            if (!firstArgument) {
                builder.append(',');
            }
            firstArgument = false;
            Map.Entry<String, String> pair = iterator.next();
            builder.append(pair.getKey().toString());
            builder.append('=');
            appendUriComponent(builder, pair.getValue().toString());
            iterator.remove();
        }
    }

    private static void appendUriComponent(StringBuilder builder, String value) {
        try {
            builder.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
    }
}

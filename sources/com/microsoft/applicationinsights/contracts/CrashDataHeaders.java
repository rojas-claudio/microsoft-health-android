package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class CrashDataHeaders implements IJsonSerializable {
    private String applicationBuild;
    private String applicationIdentifier;
    private String applicationPath;
    private int crashThread;
    private String exceptionAddress;
    private String exceptionCode;
    private String exceptionReason;
    private String exceptionType;
    private String id;
    private String parentProcess;
    private int parentProcessId;
    private String process;
    private int processId;

    public CrashDataHeaders() {
        InitializeFields();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getProcess() {
        return this.process;
    }

    public void setProcess(String value) {
        this.process = value;
    }

    public int getProcessId() {
        return this.processId;
    }

    public void setProcessId(int value) {
        this.processId = value;
    }

    public String getParentProcess() {
        return this.parentProcess;
    }

    public void setParentProcess(String value) {
        this.parentProcess = value;
    }

    public int getParentProcessId() {
        return this.parentProcessId;
    }

    public void setParentProcessId(int value) {
        this.parentProcessId = value;
    }

    public int getCrashThread() {
        return this.crashThread;
    }

    public void setCrashThread(int value) {
        this.crashThread = value;
    }

    public String getApplicationPath() {
        return this.applicationPath;
    }

    public void setApplicationPath(String value) {
        this.applicationPath = value;
    }

    public String getApplicationIdentifier() {
        return this.applicationIdentifier;
    }

    public void setApplicationIdentifier(String value) {
        this.applicationIdentifier = value;
    }

    public String getApplicationBuild() {
        return this.applicationBuild;
    }

    public void setApplicationBuild(String value) {
        this.applicationBuild = value;
    }

    public String getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(String value) {
        this.exceptionType = value;
    }

    public String getExceptionCode() {
        return this.exceptionCode;
    }

    public void setExceptionCode(String value) {
        this.exceptionCode = value;
    }

    public String getExceptionAddress() {
        return this.exceptionAddress;
    }

    public void setExceptionAddress(String value) {
        this.exceptionAddress = value;
    }

    public String getExceptionReason() {
        return this.exceptionReason;
    }

    public void setExceptionReason(String value) {
        this.exceptionReason = value;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.IJsonSerializable
    public void serialize(Writer writer) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("writer");
        }
        writer.write(123);
        serializeContent(writer);
        writer.write(125);
    }

    protected String serializeContent(Writer writer) throws IOException {
        writer.write("\"id\":");
        writer.write(JsonHelper.convert(this.id));
        String prefix = ",";
        if (this.process != null) {
            writer.write(",\"process\":");
            writer.write(JsonHelper.convert(this.process));
            prefix = ",";
        }
        if (this.processId != 0) {
            writer.write(prefix + "\"processId\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.processId)));
            prefix = ",";
        }
        if (this.parentProcess != null) {
            writer.write(prefix + "\"parentProcess\":");
            writer.write(JsonHelper.convert(this.parentProcess));
            prefix = ",";
        }
        if (this.parentProcessId != 0) {
            writer.write(prefix + "\"parentProcessId\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.parentProcessId)));
            prefix = ",";
        }
        if (this.crashThread != 0) {
            writer.write(prefix + "\"crashThread\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.crashThread)));
            prefix = ",";
        }
        if (this.applicationPath != null) {
            writer.write(prefix + "\"applicationPath\":");
            writer.write(JsonHelper.convert(this.applicationPath));
            prefix = ",";
        }
        if (this.applicationIdentifier != null) {
            writer.write(prefix + "\"applicationIdentifier\":");
            writer.write(JsonHelper.convert(this.applicationIdentifier));
            prefix = ",";
        }
        if (this.applicationBuild != null) {
            writer.write(prefix + "\"applicationBuild\":");
            writer.write(JsonHelper.convert(this.applicationBuild));
            prefix = ",";
        }
        if (this.exceptionType != null) {
            writer.write(prefix + "\"exceptionType\":");
            writer.write(JsonHelper.convert(this.exceptionType));
            prefix = ",";
        }
        if (this.exceptionCode != null) {
            writer.write(prefix + "\"exceptionCode\":");
            writer.write(JsonHelper.convert(this.exceptionCode));
            prefix = ",";
        }
        if (this.exceptionAddress != null) {
            writer.write(prefix + "\"exceptionAddress\":");
            writer.write(JsonHelper.convert(this.exceptionAddress));
            prefix = ",";
        }
        if (this.exceptionReason != null) {
            writer.write(prefix + "\"exceptionReason\":");
            writer.write(JsonHelper.convert(this.exceptionReason));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

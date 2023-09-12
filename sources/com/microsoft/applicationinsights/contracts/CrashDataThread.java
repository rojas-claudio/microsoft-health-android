package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class CrashDataThread implements IJsonSerializable {
    private List<CrashDataThreadFrame> frames;
    private int id;

    public CrashDataThread() {
        InitializeFields();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public List<CrashDataThreadFrame> getFrames() {
        if (this.frames == null) {
            this.frames = new ArrayList();
        }
        return this.frames;
    }

    public void setFrames(List<CrashDataThreadFrame> value) {
        this.frames = value;
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
        writer.write(JsonHelper.convert(Integer.valueOf(this.id)));
        if (this.frames != null) {
            writer.write(",\"frames\":");
            JsonHelper.writeList(writer, this.frames);
            return ",";
        }
        return ",";
    }

    protected void InitializeFields() {
    }
}

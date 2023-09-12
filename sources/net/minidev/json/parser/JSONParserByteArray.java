package net.minidev.json.parser;

import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReaderI;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class JSONParserByteArray extends JSONParserMemory {
    private byte[] in;

    public JSONParserByteArray(int permissiveMode) {
        super(permissiveMode);
    }

    public Object parse(byte[] in) throws ParseException {
        return parse(in, JSONValue.defaultReader.DEFAULT);
    }

    public <T> T parse(byte[] in, JsonReaderI<T> mapper) throws ParseException {
        this.base = mapper.base;
        this.in = in;
        this.len = in.length;
        return (T) parse(mapper);
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected void extractString(int beginIndex, int endIndex) {
        this.xs = new String(this.in, beginIndex, endIndex - beginIndex);
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected void extractStringTrim(int start, int stop) {
        byte[] val = this.in;
        while (start < stop && val[start] <= 32) {
            start++;
        }
        while (start < stop && val[stop - 1] <= 32) {
            stop--;
        }
        this.xs = new String(this.in, start, stop - start);
    }

    @Override // net.minidev.json.parser.JSONParserMemory
    protected int indexOf(char c, int pos) {
        int i = pos;
        while (pos < this.len) {
            if (this.in[i] != ((byte) c)) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.minidev.json.parser.JSONParserBase
    public void read() {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
        } else {
            this.c = (char) this.in[this.pos];
        }
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readS() {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
        } else {
            this.c = (char) this.in[this.pos];
        }
    }

    @Override // net.minidev.json.parser.JSONParserBase
    protected void readNoEnd() throws ParseException {
        int i = this.pos + 1;
        this.pos = i;
        if (i >= this.len) {
            this.c = (char) 26;
            throw new ParseException(this.pos - 1, 3, "EOF");
        } else {
            this.c = (char) this.in[this.pos];
        }
    }
}

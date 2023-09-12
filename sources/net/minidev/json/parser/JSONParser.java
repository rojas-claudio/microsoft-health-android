package net.minidev.json.parser;

import java.io.InputStream;
import java.io.Reader;
import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReaderI;
/* loaded from: classes.dex */
public class JSONParser {
    public static final int ACCEPT_LEADING_ZERO = 32;
    public static final int ACCEPT_NAN = 4;
    public static final int ACCEPT_NON_QUOTE = 2;
    public static final int ACCEPT_SIMPLE_QUOTE = 1;
    public static final int ACCEPT_TAILLING_DATA = 256;
    public static final int ACCEPT_TAILLING_SPACE = 512;
    public static final int ACCEPT_USELESS_COMMA = 64;
    public static int DEFAULT_PERMISSIVE_MODE = 0;
    public static final int IGNORE_CONTROL_CHAR = 8;
    public static final int MODE_JSON_SIMPLE = 960;
    public static final int MODE_PERMISSIVE = -1;
    public static final int MODE_RFC4627 = 656;
    public static final int MODE_STRICTEST = 144;
    public static final int USE_HI_PRECISION_FLOAT = 128;
    public static final int USE_INTEGER_STORAGE = 16;
    private int mode;
    private JSONParserInputStream pBinStream;
    private JSONParserByteArray pBytes;
    private JSONParserReader pStream;
    private JSONParserString pString;

    static {
        DEFAULT_PERMISSIVE_MODE = System.getProperty("JSON_SMART_SIMPLE") != null ? MODE_JSON_SIMPLE : -1;
    }

    private JSONParserReader getPStream() {
        if (this.pStream == null) {
            this.pStream = new JSONParserReader(this.mode);
        }
        return this.pStream;
    }

    private JSONParserInputStream getPBinStream() {
        if (this.pBinStream == null) {
            this.pBinStream = new JSONParserInputStream(this.mode);
        }
        return this.pBinStream;
    }

    private JSONParserString getPString() {
        if (this.pString == null) {
            this.pString = new JSONParserString(this.mode);
        }
        return this.pString;
    }

    private JSONParserByteArray getPBytes() {
        if (this.pBytes == null) {
            this.pBytes = new JSONParserByteArray(this.mode);
        }
        return this.pBytes;
    }

    public JSONParser() {
        this.mode = DEFAULT_PERMISSIVE_MODE;
    }

    public JSONParser(int permissifMode) {
        this.mode = permissifMode;
    }

    public Object parse(byte[] in) throws ParseException {
        return getPBytes().parse(in);
    }

    public <T> T parse(byte[] in, JsonReaderI<T> mapper) throws ParseException {
        return (T) getPBytes().parse(in, mapper);
    }

    public <T> T parse(byte[] in, Class<T> mapTo) throws ParseException {
        return (T) getPBytes().parse(in, JSONValue.defaultReader.getMapper((Class) mapTo));
    }

    public Object parse(InputStream in) throws ParseException {
        return getPBinStream().parse(in);
    }

    public <T> T parse(InputStream in, JsonReaderI<T> mapper) throws ParseException {
        return (T) getPBinStream().parse(in, mapper);
    }

    public <T> T parse(InputStream in, Class<T> mapTo) throws ParseException {
        return (T) getPBinStream().parse(in, JSONValue.defaultReader.getMapper((Class) mapTo));
    }

    public Object parse(Reader in) throws ParseException {
        return getPStream().parse(in);
    }

    public <T> T parse(Reader in, JsonReaderI<T> mapper) throws ParseException {
        return (T) getPStream().parse(in, mapper);
    }

    public <T> T parse(Reader in, Class<T> mapTo) throws ParseException {
        return (T) getPStream().parse(in, JSONValue.defaultReader.getMapper((Class) mapTo));
    }

    public Object parse(String in) throws ParseException {
        return getPString().parse(in);
    }

    public <T> T parse(String in, JsonReaderI<T> mapper) throws ParseException {
        return (T) getPString().parse(in, mapper);
    }

    public <T> T parse(String in, Class<T> mapTo) throws ParseException {
        return (T) getPString().parse(in, JSONValue.defaultReader.getMapper((Class) mapTo));
    }
}

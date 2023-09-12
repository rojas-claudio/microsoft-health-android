package net.minidev.json.parser;

import android.support.v4.media.TransportMediator;
import com.facebook.internal.ServerProtocol;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.kapp.utils.Constants;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import net.minidev.json.writer.JsonReader;
import net.minidev.json.writer.JsonReaderI;
import org.apache.commons.lang3.CharUtils;
/* loaded from: classes.dex */
abstract class JSONParserBase {
    public static final byte EOI = 26;
    protected static final char MAX_STOP = '~';
    protected static boolean[] stopAll = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];
    protected static boolean[] stopArray = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];
    protected static boolean[] stopKey = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];
    protected static boolean[] stopValue = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];
    protected static boolean[] stopX = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];
    protected final boolean acceptLeadinZero;
    protected final boolean acceptNaN;
    protected final boolean acceptNonQuote;
    protected final boolean acceptSimpleQuote;
    protected final boolean acceptUselessComma;
    JsonReader base;
    protected char c;
    protected final boolean checkTaillingData;
    protected final boolean checkTaillingSpace;
    protected final boolean ignoreControlChar;
    private String lastKey;
    protected int pos;
    protected final MSB sb = new MSB(15);
    protected final boolean useHiPrecisionFloat;
    protected final boolean useIntegerStorage;
    protected Object xo;
    protected String xs;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void read() throws IOException;

    protected abstract void readNQString(boolean[] zArr) throws IOException;

    protected abstract void readNoEnd() throws ParseException, IOException;

    protected abstract Object readNumber(boolean[] zArr) throws ParseException, IOException;

    abstract void readS() throws IOException;

    protected abstract void readString() throws ParseException, IOException;

    static {
        boolean[] zArr = stopKey;
        stopKey[26] = true;
        zArr[58] = true;
        boolean[] zArr2 = stopValue;
        boolean[] zArr3 = stopValue;
        stopValue[26] = true;
        zArr3[125] = true;
        zArr2[44] = true;
        boolean[] zArr4 = stopArray;
        boolean[] zArr5 = stopArray;
        stopArray[26] = true;
        zArr5[93] = true;
        zArr4[44] = true;
        stopX[26] = true;
        boolean[] zArr6 = stopAll;
        stopAll[58] = true;
        zArr6[44] = true;
        boolean[] zArr7 = stopAll;
        boolean[] zArr8 = stopAll;
        stopAll[26] = true;
        zArr8[125] = true;
        zArr7[93] = true;
    }

    public JSONParserBase(int permissiveMode) {
        this.acceptNaN = (permissiveMode & 4) > 0;
        this.acceptNonQuote = (permissiveMode & 2) > 0;
        this.acceptSimpleQuote = (permissiveMode & 1) > 0;
        this.ignoreControlChar = (permissiveMode & 8) > 0;
        this.useIntegerStorage = (permissiveMode & 16) > 0;
        this.acceptLeadinZero = (permissiveMode & 32) > 0;
        this.acceptUselessComma = (permissiveMode & 64) > 0;
        this.useHiPrecisionFloat = (permissiveMode & 128) > 0;
        this.checkTaillingData = (permissiveMode & 768) != 768;
        this.checkTaillingSpace = (permissiveMode & 512) == 0;
    }

    public void checkControleChar() throws ParseException {
        if (!this.ignoreControlChar) {
            int l = this.xs.length();
            for (int i = 0; i < l; i++) {
                char c = this.xs.charAt(i);
                if (c >= 0) {
                    if (c <= 31) {
                        throw new ParseException(this.pos + i, 0, Character.valueOf(c));
                    }
                    if (c == 127) {
                        throw new ParseException(this.pos + i, 0, Character.valueOf(c));
                    }
                }
            }
        }
    }

    public void checkLeadinZero() throws ParseException {
        int len = this.xs.length();
        if (len != 1) {
            if (len == 2) {
                if (this.xs.equals("00")) {
                    throw new ParseException(this.pos, 6, this.xs);
                }
                return;
            }
            char c1 = this.xs.charAt(0);
            char c2 = this.xs.charAt(1);
            if (c1 == '-') {
                char c3 = this.xs.charAt(2);
                if (c2 == '0' && c3 >= '0' && c3 <= '9') {
                    throw new ParseException(this.pos, 6, this.xs);
                }
            } else if (c1 == '0' && c2 >= '0' && c2 <= '9') {
                throw new ParseException(this.pos, 6, this.xs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Number extractFloat() throws ParseException {
        if (!this.acceptLeadinZero) {
            checkLeadinZero();
        }
        if (!this.useHiPrecisionFloat) {
            return Float.valueOf(Float.parseFloat(this.xs));
        }
        if (this.xs.length() > 18) {
            return new BigDecimal(this.xs);
        }
        return Double.valueOf(Double.parseDouble(this.xs));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> T parse(JsonReaderI<T> mapper) throws ParseException {
        this.pos = -1;
        try {
            read();
            T result = (T) readFirst(mapper);
            if (this.checkTaillingData) {
                if (!this.checkTaillingSpace) {
                    skipSpace();
                }
                if (this.c != 26) {
                    throw new ParseException(this.pos - 1, 1, Character.valueOf(this.c));
                }
            }
            this.xs = null;
            this.xo = null;
            return result;
        } catch (IOException e) {
            throw new ParseException(this.pos, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Number parseNumber(String s) throws ParseException {
        boolean neg;
        int max;
        boolean mustCheck;
        boolean isBig;
        int p = 0;
        int l = s.length();
        int max2 = 19;
        if (s.charAt(0) == '-') {
            p = 0 + 1;
            max2 = 19 + 1;
            neg = true;
            if (!this.acceptLeadinZero && l >= 3 && s.charAt(1) == '0') {
                throw new ParseException(this.pos, 6, s);
            }
        } else {
            neg = false;
            if (!this.acceptLeadinZero && l >= 2 && s.charAt(0) == '0') {
                throw new ParseException(this.pos, 6, s);
            }
        }
        if (l < max2) {
            max = l;
            mustCheck = false;
        } else if (l > max2) {
            return new BigInteger(s, 10);
        } else {
            max = l - 1;
            mustCheck = true;
        }
        long r = 0;
        int p2 = p;
        while (p2 < max) {
            r = (10 * r) + ('0' - s.charAt(p2));
            p2++;
        }
        if (mustCheck) {
            if (r > -922337203685477580L) {
                isBig = false;
            } else if (r < -922337203685477580L) {
                isBig = true;
            } else if (neg) {
                isBig = s.charAt(p2) > '8';
            } else {
                isBig = s.charAt(p2) > '7';
            }
            if (isBig) {
                return new BigInteger(s, 10);
            }
            r = (10 * r) + ('0' - s.charAt(p2));
        }
        if (neg) {
            if (this.useIntegerStorage && r >= -2147483648L) {
                return Integer.valueOf((int) r);
            }
            return Long.valueOf(r);
        }
        long r2 = -r;
        if (this.useIntegerStorage && r2 <= 2147483647L) {
            return Integer.valueOf((int) r2);
        }
        return Long.valueOf(r2);
    }

    protected <T> T readArray(JsonReaderI<T> mapper) throws ParseException, IOException {
        Object current = mapper.createArray();
        if (this.c != '[') {
            throw new RuntimeException("Internal Error");
        }
        read();
        boolean needData = false;
        while (true) {
            switch (this.c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    read();
                    break;
                case 26:
                    throw new ParseException(this.pos - 1, 3, "EOF");
                case ',':
                    if (needData && !this.acceptUselessComma) {
                        throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                    }
                    read();
                    needData = true;
                    break;
                    break;
                case ':':
                case '}':
                    throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                case ']':
                    if (needData && !this.acceptUselessComma) {
                        throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                    }
                    read();
                    return mapper.convert(current);
                default:
                    mapper.addValue(current, readMain(mapper, stopArray));
                    needData = false;
                    break;
            }
        }
    }

    protected <T> T readFirst(JsonReaderI<T> mapper) throws ParseException, IOException {
        while (true) {
            switch (this.c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    read();
                case '\"':
                case '\'':
                    readString();
                    return mapper.convert(this.xs);
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    this.xo = readNumber(stopX);
                    return mapper.convert(this.xo);
                case ':':
                case ']':
                case '}':
                    throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                case 'N':
                    readNQString(stopX);
                    if (!this.acceptNaN) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    if ("NaN".equals(this.xs)) {
                        return mapper.convert(Float.valueOf(Float.NaN));
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return mapper.convert(this.xs);
                case '[':
                    return (T) readArray(mapper);
                case 'f':
                    readNQString(stopX);
                    if ("false".equals(this.xs)) {
                        return mapper.convert(Boolean.FALSE);
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return mapper.convert(this.xs);
                case 'n':
                    readNQString(stopX);
                    if ("null".equals(this.xs)) {
                        return null;
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return mapper.convert(this.xs);
                case 't':
                    readNQString(stopX);
                    if (ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(this.xs)) {
                        return mapper.convert(Boolean.TRUE);
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return mapper.convert(this.xs);
                case '{':
                    return (T) readObject(mapper);
                default:
                    readNQString(stopX);
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return mapper.convert(this.xs);
            }
        }
    }

    protected Object readMain(JsonReaderI<?> mapper, boolean[] stop) throws ParseException, IOException {
        while (true) {
            switch (this.c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    read();
                case '\"':
                case '\'':
                    readString();
                    return this.xs;
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return readNumber(stop);
                case ':':
                case ']':
                case '}':
                    throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                case 'N':
                    readNQString(stop);
                    if (!this.acceptNaN) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    if ("NaN".equals(this.xs)) {
                        return Float.valueOf(Float.NaN);
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return this.xs;
                case '[':
                    return readArray(mapper.startArray(this.lastKey));
                case 'f':
                    readNQString(stop);
                    if ("false".equals(this.xs)) {
                        return Boolean.FALSE;
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return this.xs;
                case 'n':
                    readNQString(stop);
                    if ("null".equals(this.xs)) {
                        return null;
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return this.xs;
                case 't':
                    readNQString(stop);
                    if (ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(this.xs)) {
                        return Boolean.TRUE;
                    }
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return this.xs;
                case '{':
                    return readObject(mapper.startObject(this.lastKey));
                default:
                    readNQString(stop);
                    if (!this.acceptNonQuote) {
                        throw new ParseException(this.pos, 1, this.xs);
                    }
                    return this.xs;
            }
        }
    }

    protected <T> T readObject(JsonReaderI<T> mapper) throws ParseException, IOException {
        if (this.c != '{') {
            throw new RuntimeException("Internal Error");
        }
        Object current = mapper.createObject();
        boolean needData = false;
        boolean z = true;
        while (true) {
            read();
            switch (this.c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    break;
                case ',':
                    if (needData && !this.acceptUselessComma) {
                        throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                    }
                    needData = true;
                    z = true;
                    break;
                    break;
                case ':':
                case '[':
                case ']':
                case '{':
                    throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                case '}':
                    if (needData && !this.acceptUselessComma) {
                        throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                    }
                    read();
                    return mapper.convert(current);
                default:
                    if (this.c == '\"' || this.c == '\'') {
                        readString();
                    } else {
                        readNQString(stopKey);
                        if (!this.acceptNonQuote) {
                            throw new ParseException(this.pos, 1, this.xs);
                        }
                    }
                    String key = this.xs;
                    if (!z) {
                        throw new ParseException(this.pos, 1, key);
                    }
                    skipSpace();
                    if (this.c != ':') {
                        if (this.c == 26) {
                            throw new ParseException(this.pos - 1, 3, null);
                        }
                        throw new ParseException(this.pos - 1, 0, Character.valueOf(this.c));
                    }
                    readNoEnd();
                    this.lastKey = key;
                    Object value = readMain(mapper, stopValue);
                    mapper.setValue(current, key, value);
                    this.lastKey = null;
                    skipSpace();
                    if (this.c == '}') {
                        read();
                        return mapper.convert(current);
                    } else if (this.c == 26) {
                        throw new ParseException(this.pos - 1, 3, null);
                    } else {
                        if (this.c == ',') {
                            needData = true;
                            z = true;
                            break;
                        } else {
                            throw new ParseException(this.pos - 1, 1, Character.valueOf(this.c));
                        }
                    }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readString2() throws ParseException, IOException {
        char sep = this.c;
        while (true) {
            read();
            switch (this.c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case '\b':
                case '\t':
                case '\n':
                case 11:
                case '\f':
                case '\r':
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case TransportMediator.KEYCODE_MEDIA_PAUSE /* 127 */:
                    if (!this.ignoreControlChar) {
                        throw new ParseException(this.pos, 0, Character.valueOf(this.c));
                    }
                    break;
                case 26:
                    throw new ParseException(this.pos - 1, 3, null);
                case '\"':
                case '\'':
                    if (sep == this.c) {
                        read();
                        this.xs = this.sb.toString();
                        return;
                    }
                    this.sb.append(this.c);
                    break;
                case '\\':
                    read();
                    switch (this.c) {
                        case '\"':
                            this.sb.append('\"');
                            continue;
                        case '\'':
                            this.sb.append('\'');
                            continue;
                        case '/':
                            this.sb.append('/');
                            continue;
                        case '\\':
                            this.sb.append('\\');
                            continue;
                        case 'b':
                            this.sb.append('\b');
                            continue;
                        case 'f':
                            this.sb.append('\f');
                            continue;
                        case 'n':
                            this.sb.append('\n');
                            continue;
                        case WorkoutActivity.WORKOUT_ACTIVITY_TYPE_DATA_STRUCTURE_SIZE /* 114 */:
                            this.sb.append(CharUtils.CR);
                            continue;
                        case 't':
                            this.sb.append('\t');
                            continue;
                        case 'u':
                            this.sb.append(readUnicode(4));
                            continue;
                        case Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD /* 120 */:
                            this.sb.append(readUnicode(2));
                            continue;
                    }
                default:
                    this.sb.append(this.c);
                    break;
            }
        }
    }

    protected char readUnicode(int totalChars) throws ParseException, IOException {
        int i;
        int value = 0;
        for (int i2 = 0; i2 < totalChars; i2++) {
            int value2 = value * 16;
            read();
            if (this.c <= '9' && this.c >= '0') {
                i = this.c - '0';
            } else if (this.c <= 'F' && this.c >= 'A') {
                i = (this.c - 'A') + 10;
            } else if (this.c >= 'a' && this.c <= 'f') {
                i = (this.c - 'a') + 10;
            } else if (this.c == 26) {
                throw new ParseException(this.pos, 3, "EOF");
            } else {
                throw new ParseException(this.pos, 4, Character.valueOf(this.c));
            }
            value = value2 + i;
        }
        return (char) value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void skipDigits() throws IOException {
        while (this.c >= '0' && this.c <= '9') {
            readS();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void skipNQString(boolean[] stop) throws IOException {
        while (this.c != 26) {
            if (this.c >= 0 && this.c < '~' && stop[this.c]) {
                return;
            }
            readS();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void skipSpace() throws IOException {
        while (this.c <= ' ' && this.c != 26) {
            readS();
        }
    }

    /* loaded from: classes.dex */
    public static class MSB {
        char[] b;
        int p = -1;

        public MSB(int size) {
            this.b = new char[size];
        }

        public void append(char c) {
            this.p++;
            if (this.b.length <= this.p) {
                char[] t = new char[(this.b.length * 2) + 1];
                System.arraycopy(this.b, 0, t, 0, this.b.length);
                this.b = t;
            }
            this.b[this.p] = c;
        }

        public void append(int c) {
            this.p++;
            if (this.b.length <= this.p) {
                char[] t = new char[(this.b.length * 2) + 1];
                System.arraycopy(this.b, 0, t, 0, this.b.length);
                this.b = t;
            }
            this.b[this.p] = (char) c;
        }

        public String toString() {
            return new String(this.b, 0, this.p + 1);
        }

        public void clear() {
            this.p = -1;
        }
    }
}

package net.minidev.json;

import com.facebook.internal.ServerProtocol;
import java.io.IOException;
/* loaded from: classes.dex */
class JStylerObj {
    public static final MPSimple MP_SIMPLE = new MPSimple();
    public static final MPTrue MP_TRUE = new MPTrue();
    public static final MPAgressive MP_AGGRESIVE = new MPAgressive();
    public static final EscapeLT ESCAPE_LT = new EscapeLT();
    public static final Escape4Web ESCAPE4Web = new Escape4Web();

    /* loaded from: classes.dex */
    public interface MustProtect {
        boolean mustBeProtect(String str);
    }

    /* loaded from: classes.dex */
    public interface StringProtector {
        void escape(String str, Appendable appendable);
    }

    JStylerObj() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MPTrue implements MustProtect {
        private MPTrue() {
        }

        @Override // net.minidev.json.JStylerObj.MustProtect
        public boolean mustBeProtect(String s) {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MPSimple implements MustProtect {
        private MPSimple() {
        }

        @Override // net.minidev.json.JStylerObj.MustProtect
        public boolean mustBeProtect(String s) {
            int i;
            if (s == null) {
                return false;
            }
            int len = s.length();
            if (len != 0 && s.trim() == s) {
                char ch = s.charAt(0);
                if ((ch < '0' || ch > '9') && ch != '-') {
                    while (i < len) {
                        char ch2 = s.charAt(i);
                        i = (JStylerObj.isSpace(ch2) || JStylerObj.isSpecial(ch2) || JStylerObj.isSpecialChar(ch2) || JStylerObj.isUnicode(ch2)) ? 0 : i + 1;
                        return true;
                    }
                    return JStylerObj.isKeyword(s);
                }
                return true;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MPAgressive implements MustProtect {
        private MPAgressive() {
        }

        @Override // net.minidev.json.JStylerObj.MustProtect
        public boolean mustBeProtect(String s) {
            if (s == null) {
                return false;
            }
            int len = s.length();
            if (len != 0 && s.trim() == s) {
                char ch = s.charAt(0);
                if (JStylerObj.isSpecial(ch) || JStylerObj.isUnicode(ch)) {
                    return true;
                }
                for (int i = 1; i < len; i++) {
                    char ch2 = s.charAt(i);
                    if (JStylerObj.isSpecialClose(ch2) || JStylerObj.isUnicode(ch2)) {
                        return true;
                    }
                }
                if (JStylerObj.isKeyword(s)) {
                    return true;
                }
                char ch3 = s.charAt(0);
                if ((ch3 < '0' || ch3 > '9') && ch3 != '-') {
                    return false;
                }
                int p = 1;
                while (p < len) {
                    ch3 = s.charAt(p);
                    if (ch3 < '0' || ch3 > '9') {
                        break;
                    }
                    p++;
                }
                if (p == len) {
                    return true;
                }
                if (ch3 == '.') {
                    p++;
                }
                while (p < len) {
                    ch3 = s.charAt(p);
                    if (ch3 < '0' || ch3 > '9') {
                        break;
                    }
                    p++;
                }
                if (p == len) {
                    return true;
                }
                if (ch3 == 'E' || ch3 == 'e') {
                    p++;
                    if (p == len) {
                        return false;
                    }
                    char ch4 = s.charAt(p);
                    if (ch4 == '+' || ch4 == '-') {
                        p++;
                        s.charAt(p);
                    }
                }
                if (p != len) {
                    while (p < len) {
                        char ch5 = s.charAt(p);
                        if (ch5 < '0' || ch5 > '9') {
                            break;
                        }
                        p++;
                    }
                    return p == len;
                }
                return false;
            }
            return true;
        }
    }

    public static boolean isSpace(char c) {
        return c == '\r' || c == '\n' || c == '\t' || c == ' ';
    }

    public static boolean isSpecialChar(char c) {
        return c == '\b' || c == '\f' || c == '\n';
    }

    public static boolean isSpecialOpen(char c) {
        return c == '{' || c == '[' || c == ',' || c == ':';
    }

    public static boolean isSpecialClose(char c) {
        return c == '}' || c == ']' || c == ',' || c == ':';
    }

    public static boolean isSpecial(char c) {
        return c == '{' || c == '[' || c == ',' || c == '}' || c == ']' || c == ':' || c == '\'' || c == '\"';
    }

    public static boolean isUnicode(char c) {
        return (c >= 0 && c <= 31) || (c >= 127 && c <= 159) || (c >= 8192 && c <= 8447);
    }

    public static boolean isKeyword(String s) {
        if (s.length() < 3) {
            return false;
        }
        char c = s.charAt(0);
        if (c == 'n') {
            return s.equals("null");
        }
        if (c == 't') {
            return s.equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        }
        if (c == 'f') {
            return s.equals("false");
        }
        if (c == 'N') {
            return s.equals("NaN");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class EscapeLT implements StringProtector {
        private EscapeLT() {
        }

        @Override // net.minidev.json.JStylerObj.StringProtector
        public void escape(String s, Appendable out) {
            try {
                int len = s.length();
                for (int i = 0; i < len; i++) {
                    char ch = s.charAt(i);
                    switch (ch) {
                        case '\b':
                            out.append("\\b");
                            continue;
                        case '\t':
                            out.append("\\t");
                            continue;
                        case '\n':
                            out.append("\\n");
                            continue;
                        case '\f':
                            out.append("\\f");
                            continue;
                        case '\r':
                            out.append("\\r");
                            continue;
                        case '\"':
                            out.append("\\\"");
                            continue;
                        case '\\':
                            out.append("\\\\");
                            continue;
                        default:
                            if ((ch >= 0 && ch <= 31) || ((ch >= 127 && ch <= 159) || (ch >= 8192 && ch <= 8447))) {
                                out.append("\\u");
                                out.append("0123456789ABCDEF".charAt((ch >> '\f') & 15));
                                out.append("0123456789ABCDEF".charAt((ch >> '\b') & 15));
                                out.append("0123456789ABCDEF".charAt((ch >> 4) & 15));
                                out.append("0123456789ABCDEF".charAt((ch >> 0) & 15));
                                continue;
                            } else {
                                out.append(ch);
                                continue;
                            }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Impossible Exeption");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Escape4Web implements StringProtector {
        private Escape4Web() {
        }

        @Override // net.minidev.json.JStylerObj.StringProtector
        public void escape(String s, Appendable sb) {
            try {
                int len = s.length();
                for (int i = 0; i < len; i++) {
                    char ch = s.charAt(i);
                    switch (ch) {
                        case '\b':
                            sb.append("\\b");
                            continue;
                        case '\t':
                            sb.append("\\t");
                            continue;
                        case '\n':
                            sb.append("\\n");
                            continue;
                        case '\f':
                            sb.append("\\f");
                            continue;
                        case '\r':
                            sb.append("\\r");
                            continue;
                        case '\"':
                            sb.append("\\\"");
                            continue;
                        case '/':
                            sb.append("\\/");
                            continue;
                        case '\\':
                            sb.append("\\\\");
                            continue;
                        default:
                            if ((ch >= 0 && ch <= 31) || ((ch >= 127 && ch <= 159) || (ch >= 8192 && ch <= 8447))) {
                                sb.append("\\u");
                                sb.append("0123456789ABCDEF".charAt((ch >> '\f') & 15));
                                sb.append("0123456789ABCDEF".charAt((ch >> '\b') & 15));
                                sb.append("0123456789ABCDEF".charAt((ch >> 4) & 15));
                                sb.append("0123456789ABCDEF".charAt((ch >> 0) & 15));
                                continue;
                            } else {
                                sb.append(ch);
                                continue;
                            }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Impossible Error");
            }
        }
    }
}

package com.jayway.jsonpath.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class JsonFormatter {
    private static final String INDENT = "   ";
    private static final Logger logger = LoggerFactory.getLogger(JsonFormatter.class);
    private static final String NEW_LINE = System.getProperty("line.separator");

    private static void appendIndent(StringBuilder sb, int count) {
        while (count > 0) {
            sb.append(INDENT);
            count--;
        }
    }

    private static boolean isEscaped(StringBuilder sb, int index) {
        boolean escaped = false;
        int idx = Math.min(index, sb.length());
        while (idx > 0) {
            idx--;
            try {
                if (sb.charAt(idx) != '\\') {
                    break;
                }
                escaped = !escaped;
            } catch (Exception e) {
                logger.warn("Failed to check escaped ", (Throwable) e);
            }
        }
        return escaped;
    }

    public static String prettyPrint(String input) {
        String input2 = input.replaceAll("[\\r\\n]", "");
        StringBuilder output = new StringBuilder(input2.length() * 2);
        boolean quoteOpened = false;
        int depth = 0;
        for (int i = 0; i < input2.length(); i++) {
            char ch = input2.charAt(i);
            switch (ch) {
                case '\"':
                case '\'':
                    output.append(ch);
                    if (quoteOpened) {
                        if (isEscaped(output, i)) {
                            break;
                        } else {
                            quoteOpened = false;
                            break;
                        }
                    } else {
                        quoteOpened = true;
                        break;
                    }
                case ',':
                    output.append(ch);
                    if (quoteOpened) {
                        break;
                    } else {
                        output.append(NEW_LINE);
                        appendIndent(output, depth);
                        break;
                    }
                case ':':
                    if (quoteOpened) {
                        output.append(ch);
                        break;
                    } else {
                        output.append(" : ");
                        break;
                    }
                case '[':
                case '{':
                    output.append(ch);
                    if (quoteOpened) {
                        break;
                    } else {
                        output.append(NEW_LINE);
                        depth++;
                        appendIndent(output, depth);
                        break;
                    }
                case ']':
                case '}':
                    if (quoteOpened) {
                        output.append(ch);
                        break;
                    } else {
                        output.append(NEW_LINE);
                        depth--;
                        appendIndent(output, depth);
                        output.append(ch);
                        break;
                    }
                default:
                    if (quoteOpened || ch != ' ') {
                        output.append(ch);
                        break;
                    } else {
                        break;
                    }
                    break;
            }
        }
        return output.toString();
    }
}

package org.apache.commons.lang3.builder;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
/* loaded from: classes.dex */
public abstract class ToStringStyle implements Serializable {
    private static final long serialVersionUID = -2587890625525655916L;
    public static final ToStringStyle DEFAULT_STYLE = new DefaultToStringStyle();
    public static final ToStringStyle MULTI_LINE_STYLE = new MultiLineToStringStyle();
    public static final ToStringStyle NO_FIELD_NAMES_STYLE = new NoFieldNameToStringStyle();
    public static final ToStringStyle SHORT_PREFIX_STYLE = new ShortPrefixToStringStyle();
    public static final ToStringStyle SIMPLE_STYLE = new SimpleToStringStyle();
    private static final ThreadLocal<WeakHashMap<Object, Object>> REGISTRY = new ThreadLocal<>();
    private boolean useFieldNames = true;
    private boolean useClassName = true;
    private boolean useShortClassName = false;
    private boolean useIdentityHashCode = true;
    private String contentStart = "[";
    private String contentEnd = "]";
    private String fieldNameValueSeparator = SimpleComparison.EQUAL_TO_OPERATION;
    private boolean fieldSeparatorAtStart = false;
    private boolean fieldSeparatorAtEnd = false;
    private String fieldSeparator = ",";
    private String arrayStart = "{";
    private String arraySeparator = ",";
    private boolean arrayContentDetail = true;
    private String arrayEnd = "}";
    private boolean defaultFullDetail = true;
    private String nullText = "<null>";
    private String sizeStartText = "<size=";
    private String sizeEndText = SimpleComparison.GREATER_THAN_OPERATION;
    private String summaryObjectStartText = SimpleComparison.LESS_THAN_OPERATION;
    private String summaryObjectEndText = SimpleComparison.GREATER_THAN_OPERATION;

    static Map<Object, Object> getRegistry() {
        return REGISTRY.get();
    }

    static boolean isRegistered(Object value) {
        Map<Object, Object> m = getRegistry();
        return m != null && m.containsKey(value);
    }

    static void register(Object value) {
        if (value != null) {
            Map<Object, Object> m = getRegistry();
            if (m == null) {
                REGISTRY.set(new WeakHashMap<>());
            }
            getRegistry().put(value, null);
        }
    }

    static void unregister(Object value) {
        Map<Object, Object> m;
        if (value != null && (m = getRegistry()) != null) {
            m.remove(value);
            if (m.isEmpty()) {
                REGISTRY.remove();
            }
        }
    }

    public void appendSuper(StringBuffer buffer, String superToString) {
        appendToString(buffer, superToString);
    }

    public void appendToString(StringBuffer buffer, String toString) {
        int pos1;
        int pos2;
        if (toString != null && (pos1 = toString.indexOf(this.contentStart) + this.contentStart.length()) != (pos2 = toString.lastIndexOf(this.contentEnd)) && pos1 >= 0 && pos2 >= 0) {
            String data = toString.substring(pos1, pos2);
            if (this.fieldSeparatorAtStart) {
                removeLastFieldSeparator(buffer);
            }
            buffer.append(data);
            appendFieldSeparator(buffer);
        }
    }

    public void appendStart(StringBuffer buffer, Object object) {
        if (object != null) {
            appendClassName(buffer, object);
            appendIdentityHashCode(buffer, object);
            appendContentStart(buffer);
            if (this.fieldSeparatorAtStart) {
                appendFieldSeparator(buffer);
            }
        }
    }

    public void appendEnd(StringBuffer buffer, Object object) {
        if (!this.fieldSeparatorAtEnd) {
            removeLastFieldSeparator(buffer);
        }
        appendContentEnd(buffer);
        unregister(object);
    }

    protected void removeLastFieldSeparator(StringBuffer buffer) {
        int len = buffer.length();
        int sepLen = this.fieldSeparator.length();
        if (len > 0 && sepLen > 0 && len >= sepLen) {
            boolean match = true;
            int i = 0;
            while (true) {
                if (i >= sepLen) {
                    break;
                } else if (buffer.charAt((len - 1) - i) == this.fieldSeparator.charAt((sepLen - 1) - i)) {
                    i++;
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                buffer.setLength(len - sepLen);
            }
        }
    }

    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (value == null) {
            appendNullText(buffer, fieldName);
        } else {
            appendInternal(buffer, fieldName, value, isFullDetail(fullDetail));
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendInternal(StringBuffer buffer, String fieldName, Object value, boolean detail) {
        if (isRegistered(value) && !(value instanceof Number) && !(value instanceof Boolean) && !(value instanceof Character)) {
            appendCyclicObject(buffer, fieldName, value);
            return;
        }
        register(value);
        try {
            if (value instanceof Collection) {
                if (detail) {
                    appendDetail(buffer, fieldName, (Collection) value);
                } else {
                    appendSummarySize(buffer, fieldName, ((Collection) value).size());
                }
            } else if (value instanceof Map) {
                if (detail) {
                    appendDetail(buffer, fieldName, (Map) value);
                } else {
                    appendSummarySize(buffer, fieldName, ((Map) value).size());
                }
            } else if (value instanceof long[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (long[]) value);
                } else {
                    appendSummary(buffer, fieldName, (long[]) value);
                }
            } else if (value instanceof int[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (int[]) value);
                } else {
                    appendSummary(buffer, fieldName, (int[]) value);
                }
            } else if (value instanceof short[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (short[]) value);
                } else {
                    appendSummary(buffer, fieldName, (short[]) value);
                }
            } else if (value instanceof byte[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (byte[]) value);
                } else {
                    appendSummary(buffer, fieldName, (byte[]) value);
                }
            } else if (value instanceof char[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (char[]) value);
                } else {
                    appendSummary(buffer, fieldName, (char[]) value);
                }
            } else if (value instanceof double[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (double[]) value);
                } else {
                    appendSummary(buffer, fieldName, (double[]) value);
                }
            } else if (value instanceof float[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (float[]) value);
                } else {
                    appendSummary(buffer, fieldName, (float[]) value);
                }
            } else if (value instanceof boolean[]) {
                if (detail) {
                    appendDetail(buffer, fieldName, (boolean[]) value);
                } else {
                    appendSummary(buffer, fieldName, (boolean[]) value);
                }
            } else if (value.getClass().isArray()) {
                if (detail) {
                    appendDetail(buffer, fieldName, (Object[]) value);
                } else {
                    appendSummary(buffer, fieldName, (Object[]) value);
                }
            } else if (detail) {
                appendDetail(buffer, fieldName, value);
            } else {
                appendSummary(buffer, fieldName, value);
            }
        } finally {
            unregister(value);
        }
    }

    protected void appendCyclicObject(StringBuffer buffer, String fieldName, Object value) {
        ObjectUtils.identityToString(buffer, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        buffer.append(value);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
        buffer.append(coll);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
        buffer.append(map);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, Object value) {
        buffer.append(this.summaryObjectStartText);
        buffer.append(getShortClassName(value.getClass()));
        buffer.append(this.summaryObjectEndText);
    }

    public void append(StringBuffer buffer, String fieldName, long value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, long value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, int value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, int value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, short value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, short value) {
        buffer.append((int) value);
    }

    public void append(StringBuffer buffer, String fieldName, byte value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, byte value) {
        buffer.append((int) value);
    }

    public void append(StringBuffer buffer, String fieldName, char value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, char value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, double value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, double value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, float value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, float value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, boolean value) {
        appendFieldStart(buffer, fieldName);
        appendDetail(buffer, fieldName, value);
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, boolean value) {
        buffer.append(value);
    }

    public void append(StringBuffer buffer, String fieldName, Object[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            Object item = array[i];
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            if (item == null) {
                appendNullText(buffer, fieldName);
            } else {
                appendInternal(buffer, fieldName, item, this.arrayContentDetail);
            }
        }
        buffer.append(this.arrayEnd);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array) {
        buffer.append(this.arrayStart);
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            if (item == null) {
                appendNullText(buffer, fieldName);
            } else {
                appendInternal(buffer, fieldName, item, this.arrayContentDetail);
            }
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, Object[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, long[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, long[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, long[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, int[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, int[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, int[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, short[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, short[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, short[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, byte[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, byte[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, char[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, char[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, char[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, double[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, double[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, double[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, float[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, float[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, float[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    public void append(StringBuffer buffer, String fieldName, boolean[] array, Boolean fullDetail) {
        appendFieldStart(buffer, fieldName);
        if (array == null) {
            appendNullText(buffer, fieldName);
        } else if (isFullDetail(fullDetail)) {
            appendDetail(buffer, fieldName, array);
        } else {
            appendSummary(buffer, fieldName, array);
        }
        appendFieldEnd(buffer, fieldName);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array) {
        buffer.append(this.arrayStart);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(this.arraySeparator);
            }
            appendDetail(buffer, fieldName, array[i]);
        }
        buffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer buffer, String fieldName, boolean[] array) {
        appendSummarySize(buffer, fieldName, array.length);
    }

    protected void appendClassName(StringBuffer buffer, Object object) {
        if (this.useClassName && object != null) {
            register(object);
            if (this.useShortClassName) {
                buffer.append(getShortClassName(object.getClass()));
            } else {
                buffer.append(object.getClass().getName());
            }
        }
    }

    protected void appendIdentityHashCode(StringBuffer buffer, Object object) {
        if (isUseIdentityHashCode() && object != null) {
            register(object);
            buffer.append('@');
            buffer.append(Integer.toHexString(System.identityHashCode(object)));
        }
    }

    protected void appendContentStart(StringBuffer buffer) {
        buffer.append(this.contentStart);
    }

    protected void appendContentEnd(StringBuffer buffer) {
        buffer.append(this.contentEnd);
    }

    protected void appendNullText(StringBuffer buffer, String fieldName) {
        buffer.append(this.nullText);
    }

    protected void appendFieldSeparator(StringBuffer buffer) {
        buffer.append(this.fieldSeparator);
    }

    protected void appendFieldStart(StringBuffer buffer, String fieldName) {
        if (this.useFieldNames && fieldName != null) {
            buffer.append(fieldName);
            buffer.append(this.fieldNameValueSeparator);
        }
    }

    protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
        appendFieldSeparator(buffer);
    }

    protected void appendSummarySize(StringBuffer buffer, String fieldName, int size) {
        buffer.append(this.sizeStartText);
        buffer.append(size);
        buffer.append(this.sizeEndText);
    }

    protected boolean isFullDetail(Boolean fullDetailRequest) {
        return fullDetailRequest == null ? this.defaultFullDetail : fullDetailRequest.booleanValue();
    }

    protected String getShortClassName(Class<?> cls) {
        return ClassUtils.getShortClassName(cls);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUseClassName() {
        return this.useClassName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUseClassName(boolean useClassName) {
        this.useClassName = useClassName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUseShortClassName() {
        return this.useShortClassName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUseShortClassName(boolean useShortClassName) {
        this.useShortClassName = useShortClassName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUseIdentityHashCode() {
        return this.useIdentityHashCode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUseIdentityHashCode(boolean useIdentityHashCode) {
        this.useIdentityHashCode = useIdentityHashCode;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUseFieldNames() {
        return this.useFieldNames;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setUseFieldNames(boolean useFieldNames) {
        this.useFieldNames = useFieldNames;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isDefaultFullDetail() {
        return this.defaultFullDetail;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDefaultFullDetail(boolean defaultFullDetail) {
        this.defaultFullDetail = defaultFullDetail;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isArrayContentDetail() {
        return this.arrayContentDetail;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setArrayContentDetail(boolean arrayContentDetail) {
        this.arrayContentDetail = arrayContentDetail;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getArrayStart() {
        return this.arrayStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setArrayStart(String arrayStart) {
        if (arrayStart == null) {
            arrayStart = "";
        }
        this.arrayStart = arrayStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getArrayEnd() {
        return this.arrayEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setArrayEnd(String arrayEnd) {
        if (arrayEnd == null) {
            arrayEnd = "";
        }
        this.arrayEnd = arrayEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getArraySeparator() {
        return this.arraySeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setArraySeparator(String arraySeparator) {
        if (arraySeparator == null) {
            arraySeparator = "";
        }
        this.arraySeparator = arraySeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getContentStart() {
        return this.contentStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setContentStart(String contentStart) {
        if (contentStart == null) {
            contentStart = "";
        }
        this.contentStart = contentStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getContentEnd() {
        return this.contentEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setContentEnd(String contentEnd) {
        if (contentEnd == null) {
            contentEnd = "";
        }
        this.contentEnd = contentEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getFieldNameValueSeparator() {
        return this.fieldNameValueSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFieldNameValueSeparator(String fieldNameValueSeparator) {
        if (fieldNameValueSeparator == null) {
            fieldNameValueSeparator = "";
        }
        this.fieldNameValueSeparator = fieldNameValueSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getFieldSeparator() {
        return this.fieldSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFieldSeparator(String fieldSeparator) {
        if (fieldSeparator == null) {
            fieldSeparator = "";
        }
        this.fieldSeparator = fieldSeparator;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFieldSeparatorAtStart() {
        return this.fieldSeparatorAtStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFieldSeparatorAtStart(boolean fieldSeparatorAtStart) {
        this.fieldSeparatorAtStart = fieldSeparatorAtStart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFieldSeparatorAtEnd() {
        return this.fieldSeparatorAtEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFieldSeparatorAtEnd(boolean fieldSeparatorAtEnd) {
        this.fieldSeparatorAtEnd = fieldSeparatorAtEnd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getNullText() {
        return this.nullText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setNullText(String nullText) {
        if (nullText == null) {
            nullText = "";
        }
        this.nullText = nullText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSizeStartText() {
        return this.sizeStartText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSizeStartText(String sizeStartText) {
        if (sizeStartText == null) {
            sizeStartText = "";
        }
        this.sizeStartText = sizeStartText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSizeEndText() {
        return this.sizeEndText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSizeEndText(String sizeEndText) {
        if (sizeEndText == null) {
            sizeEndText = "";
        }
        this.sizeEndText = sizeEndText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSummaryObjectStartText() {
        return this.summaryObjectStartText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSummaryObjectStartText(String summaryObjectStartText) {
        if (summaryObjectStartText == null) {
            summaryObjectStartText = "";
        }
        this.summaryObjectStartText = summaryObjectStartText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSummaryObjectEndText() {
        return this.summaryObjectEndText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSummaryObjectEndText(String summaryObjectEndText) {
        if (summaryObjectEndText == null) {
            summaryObjectEndText = "";
        }
        this.summaryObjectEndText = summaryObjectEndText;
    }

    /* loaded from: classes.dex */
    private static final class DefaultToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1;

        DefaultToStringStyle() {
        }

        private Object readResolve() {
            return ToStringStyle.DEFAULT_STYLE;
        }
    }

    /* loaded from: classes.dex */
    private static final class NoFieldNameToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1;

        NoFieldNameToStringStyle() {
            setUseFieldNames(false);
        }

        private Object readResolve() {
            return ToStringStyle.NO_FIELD_NAMES_STYLE;
        }
    }

    /* loaded from: classes.dex */
    private static final class ShortPrefixToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1;

        ShortPrefixToStringStyle() {
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
        }

        private Object readResolve() {
            return ToStringStyle.SHORT_PREFIX_STYLE;
        }
    }

    /* loaded from: classes.dex */
    private static final class SimpleToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1;

        SimpleToStringStyle() {
            setUseClassName(false);
            setUseIdentityHashCode(false);
            setUseFieldNames(false);
            setContentStart("");
            setContentEnd("");
        }

        private Object readResolve() {
            return ToStringStyle.SIMPLE_STYLE;
        }
    }

    /* loaded from: classes.dex */
    private static final class MultiLineToStringStyle extends ToStringStyle {
        private static final long serialVersionUID = 1;

        MultiLineToStringStyle() {
            setContentStart("[");
            setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
            setFieldSeparatorAtStart(true);
            setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
        }

        private Object readResolve() {
            return ToStringStyle.MULTI_LINE_STYLE;
        }
    }
}

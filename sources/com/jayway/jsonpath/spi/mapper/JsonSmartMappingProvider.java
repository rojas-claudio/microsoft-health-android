package com.jayway.jsonpath.spi.mapper;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.Callable;
import net.minidev.json.JSONValue;
import net.minidev.json.writer.JsonReader;
import net.minidev.json.writer.JsonReaderI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class JsonSmartMappingProvider implements MappingProvider {
    private final Callable<JsonReader> factory;
    private static final Logger logger = LoggerFactory.getLogger(JsonSmartMappingProvider.class);
    private static JsonReader DEFAULT = new JsonReader();

    static {
        DEFAULT.registerReader(Long.class, new LongReader());
        DEFAULT.registerReader(Long.TYPE, new LongReader());
        DEFAULT.registerReader(Integer.class, new IntegerReader());
        DEFAULT.registerReader(Integer.TYPE, new IntegerReader());
        DEFAULT.registerReader(Double.class, new DoubleReader());
        DEFAULT.registerReader(Double.TYPE, new DoubleReader());
        DEFAULT.registerReader(Float.class, new FloatReader());
        DEFAULT.registerReader(Float.TYPE, new FloatReader());
        DEFAULT.registerReader(BigDecimal.class, new BigDecimalReader());
        DEFAULT.registerReader(String.class, new StringReader());
        DEFAULT.registerReader(Date.class, new DateReader());
    }

    public JsonSmartMappingProvider(final JsonReader jsonReader) {
        this(new Callable<JsonReader>() { // from class: com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider.1
            @Override // java.util.concurrent.Callable
            public JsonReader call() {
                return JsonReader.this;
            }
        });
    }

    public JsonSmartMappingProvider(Callable<JsonReader> factory) {
        this.factory = factory;
    }

    public JsonSmartMappingProvider() {
        this(DEFAULT);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jayway.jsonpath.spi.mapper.MappingProvider
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        Object source2;
        if (source == 0) {
            return null;
        }
        if (!targetType.isAssignableFrom(source.getClass())) {
            try {
                if (!configuration.jsonProvider().isMap(source) && !configuration.jsonProvider().isArray(source)) {
                    source2 = (T) this.factory.call().getMapper((Class) targetType).convert(source);
                } else {
                    String s = configuration.jsonProvider().toJson(source);
                    source2 = (T) JSONValue.parse(s, (Class<Object>) targetType);
                }
                return (T) source2;
            } catch (Exception e) {
                throw new MappingException(e);
            }
        }
        return source;
    }

    @Override // com.jayway.jsonpath.spi.mapper.MappingProvider
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        throw new UnsupportedOperationException("Json-smart provider does not support TypeRef! Use a Jackson or Gson based provider");
    }

    /* loaded from: classes.dex */
    private static class StringReader extends JsonReaderI<String> {
        public StringReader() {
            super(null);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public String convert(Object src) {
            if (src == null) {
                return null;
            }
            return src.toString();
        }
    }

    /* loaded from: classes.dex */
    private static class IntegerReader extends JsonReaderI<Integer> {
        public IntegerReader() {
            super(null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.minidev.json.writer.JsonReaderI
        public Integer convert(Object src) {
            if (src == null) {
                return null;
            }
            if (Integer.class.isAssignableFrom(src.getClass())) {
                return (Integer) src;
            }
            if (Long.class.isAssignableFrom(src.getClass())) {
                return Integer.valueOf(((Integer) src).intValue());
            }
            if (Double.class.isAssignableFrom(src.getClass())) {
                return Integer.valueOf(((Double) src).intValue());
            }
            if (BigDecimal.class.isAssignableFrom(src.getClass())) {
                return Integer.valueOf(((BigDecimal) src).intValue());
            }
            if (Float.class.isAssignableFrom(src.getClass())) {
                return Integer.valueOf(((Float) src).intValue());
            }
            if (String.class.isAssignableFrom(src.getClass())) {
                return Integer.valueOf(src.toString());
            }
            throw new MappingException("can not map a " + src.getClass() + " to " + Integer.class.getName());
        }
    }

    /* loaded from: classes.dex */
    private static class LongReader extends JsonReaderI<Long> {
        public LongReader() {
            super(null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.minidev.json.writer.JsonReaderI
        public Long convert(Object src) {
            if (src == null) {
                return null;
            }
            if (Long.class.isAssignableFrom(src.getClass())) {
                return (Long) src;
            }
            if (Integer.class.isAssignableFrom(src.getClass())) {
                return Long.valueOf(((Integer) src).longValue());
            }
            if (Double.class.isAssignableFrom(src.getClass())) {
                return Long.valueOf(((Double) src).longValue());
            }
            if (BigDecimal.class.isAssignableFrom(src.getClass())) {
                return Long.valueOf(((BigDecimal) src).longValue());
            }
            if (Float.class.isAssignableFrom(src.getClass())) {
                return Long.valueOf(((Float) src).longValue());
            }
            if (String.class.isAssignableFrom(src.getClass())) {
                return Long.valueOf(src.toString());
            }
            throw new MappingException("can not map a " + src.getClass() + " to " + Long.class.getName());
        }
    }

    /* loaded from: classes.dex */
    private static class DoubleReader extends JsonReaderI<Double> {
        public DoubleReader() {
            super(null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.minidev.json.writer.JsonReaderI
        public Double convert(Object src) {
            if (src == null) {
                return null;
            }
            if (Double.class.isAssignableFrom(src.getClass())) {
                return (Double) src;
            }
            if (Integer.class.isAssignableFrom(src.getClass())) {
                return Double.valueOf(((Integer) src).doubleValue());
            }
            if (Long.class.isAssignableFrom(src.getClass())) {
                return Double.valueOf(((Long) src).doubleValue());
            }
            if (BigDecimal.class.isAssignableFrom(src.getClass())) {
                return Double.valueOf(((BigDecimal) src).doubleValue());
            }
            if (Float.class.isAssignableFrom(src.getClass())) {
                return Double.valueOf(((Float) src).doubleValue());
            }
            if (String.class.isAssignableFrom(src.getClass())) {
                return Double.valueOf(src.toString());
            }
            throw new MappingException("can not map a " + src.getClass() + " to " + Double.class.getName());
        }
    }

    /* loaded from: classes.dex */
    private static class FloatReader extends JsonReaderI<Float> {
        public FloatReader() {
            super(null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.minidev.json.writer.JsonReaderI
        public Float convert(Object src) {
            if (src == null) {
                return null;
            }
            if (Float.class.isAssignableFrom(src.getClass())) {
                return (Float) src;
            }
            if (Integer.class.isAssignableFrom(src.getClass())) {
                return Float.valueOf(((Integer) src).floatValue());
            }
            if (Long.class.isAssignableFrom(src.getClass())) {
                return Float.valueOf(((Long) src).floatValue());
            }
            if (BigDecimal.class.isAssignableFrom(src.getClass())) {
                return Float.valueOf(((BigDecimal) src).floatValue());
            }
            if (Double.class.isAssignableFrom(src.getClass())) {
                return Float.valueOf(((Double) src).floatValue());
            }
            if (String.class.isAssignableFrom(src.getClass())) {
                return Float.valueOf(src.toString());
            }
            throw new MappingException("can not map a " + src.getClass() + " to " + Float.class.getName());
        }
    }

    /* loaded from: classes.dex */
    private static class BigDecimalReader extends JsonReaderI<BigDecimal> {
        public BigDecimalReader() {
            super(null);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public BigDecimal convert(Object src) {
            if (src == null) {
                return null;
            }
            return new BigDecimal(src.toString());
        }
    }

    /* loaded from: classes.dex */
    private static class DateReader extends JsonReaderI<Date> {
        public DateReader() {
            super(null);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Date convert(Object src) {
            if (src == null) {
                return null;
            }
            if (Date.class.isAssignableFrom(src.getClass())) {
                return (Date) src;
            }
            if (Long.class.isAssignableFrom(src.getClass())) {
                return new Date(((Long) src).longValue());
            }
            if (String.class.isAssignableFrom(src.getClass())) {
                try {
                    return DateFormat.getInstance().parse(src.toString());
                } catch (ParseException e) {
                    throw new MappingException(e);
                }
            }
            throw new MappingException("can not map a " + src.getClass() + " to " + Date.class.getName());
        }
    }
}

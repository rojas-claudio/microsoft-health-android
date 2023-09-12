package net.minidev.json.writer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ArraysMapper<T> extends JsonReaderI<T> {
    public static JsonReaderI<int[]> MAPPER_PRIM_INT = new ArraysMapper<int[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.1
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public int[] convert(Object current) {
            int p = 0;
            int[] r = new int[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).intValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Integer[]> MAPPER_INT = new ArraysMapper<Integer[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.2
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Integer[] convert(Object current) {
            int p = 0;
            Integer[] r = new Integer[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Integer) {
                        r[p] = (Integer) e;
                    } else {
                        r[p] = Integer.valueOf(((Number) e).intValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<short[]> MAPPER_PRIM_SHORT = new ArraysMapper<short[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.3
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public short[] convert(Object current) {
            int p = 0;
            short[] r = new short[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).shortValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Short[]> MAPPER_SHORT = new ArraysMapper<Short[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.4
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Short[] convert(Object current) {
            int p = 0;
            Short[] r = new Short[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Short) {
                        r[p] = (Short) e;
                    } else {
                        r[p] = Short.valueOf(((Number) e).shortValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<byte[]> MAPPER_PRIM_BYTE = new ArraysMapper<byte[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.5
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public byte[] convert(Object current) {
            int p = 0;
            byte[] r = new byte[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).byteValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Byte[]> MAPPER_BYTE = new ArraysMapper<Byte[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.6
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Byte[] convert(Object current) {
            int p = 0;
            Byte[] r = new Byte[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Byte) {
                        r[p] = (Byte) e;
                    } else {
                        r[p] = Byte.valueOf(((Number) e).byteValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<char[]> MAPPER_PRIM_CHAR = new ArraysMapper<char[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.7
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public char[] convert(Object current) {
            int p = 0;
            char[] r = new char[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = e.toString().charAt(0);
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Character[]> MAPPER_CHAR = new ArraysMapper<Character[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.8
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Character[] convert(Object current) {
            int p = 0;
            Character[] r = new Character[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    r[p] = Character.valueOf(e.toString().charAt(0));
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<long[]> MAPPER_PRIM_LONG = new ArraysMapper<long[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.9
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public long[] convert(Object current) {
            int p = 0;
            long[] r = new long[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).intValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Long[]> MAPPER_LONG = new ArraysMapper<Long[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.10
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Long[] convert(Object current) {
            int p = 0;
            Long[] r = new Long[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Float) {
                        r[p] = (Long) e;
                    } else {
                        r[p] = Long.valueOf(((Number) e).longValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<float[]> MAPPER_PRIM_FLOAT = new ArraysMapper<float[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.11
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public float[] convert(Object current) {
            int p = 0;
            float[] r = new float[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).floatValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Float[]> MAPPER_FLOAT = new ArraysMapper<Float[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.12
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Float[] convert(Object current) {
            int p = 0;
            Float[] r = new Float[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Float) {
                        r[p] = (Float) e;
                    } else {
                        r[p] = Float.valueOf(((Number) e).floatValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<double[]> MAPPER_PRIM_DOUBLE = new ArraysMapper<double[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.13
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public double[] convert(Object current) {
            int p = 0;
            double[] r = new double[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Number) e).doubleValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Double[]> MAPPER_DOUBLE = new ArraysMapper<Double[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.14
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Double[] convert(Object current) {
            int p = 0;
            Double[] r = new Double[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Double) {
                        r[p] = (Double) e;
                    } else {
                        r[p] = Double.valueOf(((Number) e).doubleValue());
                    }
                    p++;
                }
            }
            return r;
        }
    };
    public static JsonReaderI<boolean[]> MAPPER_PRIM_BOOL = new ArraysMapper<boolean[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.15
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public boolean[] convert(Object current) {
            int p = 0;
            boolean[] r = new boolean[((List) current).size()];
            for (Object e : (List) current) {
                r[p] = ((Boolean) e).booleanValue();
                p++;
            }
            return r;
        }
    };
    public static JsonReaderI<Boolean[]> MAPPER_BOOL = new ArraysMapper<Boolean[]>(null) { // from class: net.minidev.json.writer.ArraysMapper.16
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Boolean[] convert(Object current) {
            int p = 0;
            Boolean[] r = new Boolean[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Boolean) {
                        r[p] = Boolean.valueOf(((Boolean) e).booleanValue());
                    } else if (!(e instanceof Number)) {
                        throw new RuntimeException("can not convert " + e + " toBoolean");
                    } else {
                        r[p] = Boolean.valueOf(((Number) e).intValue() != 0);
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public ArraysMapper(JsonReader base) {
        super(base);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        return new ArrayList();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) {
        ((List) current).add(value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.minidev.json.writer.JsonReaderI
    public T convert(Object current) {
        return current;
    }

    /* loaded from: classes.dex */
    public static class GenericMapper<T> extends ArraysMapper<T> {
        final Class<?> componentType;
        JsonReaderI<?> subMapper;

        public GenericMapper(JsonReader base, Class<T> type) {
            super(base);
            this.componentType = type.getComponentType();
        }

        /* JADX WARN: Type inference failed for: r4v1, types: [T, java.lang.Object[]] */
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public T convert(Object current) {
            int p = 0;
            ?? r4 = (T) ((Object[]) Array.newInstance(this.componentType, ((List) current).size()));
            for (Object e : (List) current) {
                r4[p] = e;
                p++;
            }
            return r4;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper((Class) this.componentType);
            }
            return this.subMapper;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper((Class) this.componentType);
            }
            return this.subMapper;
        }
    }
}

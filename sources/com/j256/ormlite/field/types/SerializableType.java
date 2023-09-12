package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseResults;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
/* loaded from: classes.dex */
public class SerializableType extends BaseDataType {
    private static final SerializableType singleTon = new SerializableType();

    public static SerializableType getSingleton() {
        return singleTon;
    }

    private SerializableType() {
        super(SqlType.SERIALIZABLE, new Class[0]);
    }

    protected SerializableType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        throw new SQLException("Default values for serializable types are not supported");
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getBytes(columnPos);
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        ObjectInputStream objInStream;
        byte[] bytes = (byte[]) sqlArg;
        ObjectInputStream objInStream2 = null;
        try {
            try {
                objInStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            Object readObject = objInStream.readObject();
            if (objInStream != null) {
                try {
                    objInStream.close();
                } catch (IOException e2) {
                }
            }
            return readObject;
        } catch (Exception e3) {
            e = e3;
            objInStream2 = objInStream;
            throw SqlExceptionUtil.create("Could not read serialized object from byte array: " + Arrays.toString(bytes) + "(len " + bytes.length + ")", e);
        } catch (Throwable th2) {
            th = th2;
            objInStream2 = objInStream;
            if (objInStream2 != null) {
                try {
                    objInStream2.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object javaToSqlArg(FieldType fieldType, Object obj) throws SQLException {
        ByteArrayOutputStream outStream;
        ObjectOutputStream objOutStream;
        ObjectOutputStream objOutStream2 = null;
        try {
            try {
                outStream = new ByteArrayOutputStream();
                objOutStream = new ObjectOutputStream(outStream);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            objOutStream.writeObject(obj);
            objOutStream.close();
            objOutStream2 = null;
            byte[] byteArray = outStream.toByteArray();
            if (0 != 0) {
                try {
                    objOutStream2.close();
                } catch (IOException e2) {
                }
            }
            return byteArray;
        } catch (Exception e3) {
            e = e3;
            objOutStream2 = objOutStream;
            throw SqlExceptionUtil.create("Could not write serialized object to byte array: " + obj, e);
        } catch (Throwable th2) {
            th = th2;
            objOutStream2 = objOutStream;
            if (objOutStream2 != null) {
                try {
                    objOutStream2.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isValidForField(Field field) {
        return Serializable.class.isAssignableFrom(field.getType());
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public boolean isStreamType() {
        return true;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isComparable() {
        return false;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isAppropriateId() {
        return false;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isArgumentHolderRequired() {
        return true;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) throws SQLException {
        throw new SQLException("Serializable type cannot be converted from string to Java");
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public Class<?> getPrimaryClass() {
        return Serializable.class;
    }
}

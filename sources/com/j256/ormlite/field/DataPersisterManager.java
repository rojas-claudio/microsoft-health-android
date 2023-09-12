package com.j256.ormlite.field;

import com.j256.ormlite.field.types.EnumStringType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class DataPersisterManager {
    private static final DataPersister DEFAULT_ENUM_PERSISTER = EnumStringType.getSingleton();
    private static List<DataPersister> registeredPersisters = null;
    private static final Map<String, DataPersister> builtInMap = new HashMap();

    static {
        DataType[] arr$ = DataType.values();
        for (DataType dataType : arr$) {
            DataPersister persister = dataType.getDataPersister();
            if (persister != null) {
                Class<?>[] arr$2 = persister.getAssociatedClasses();
                for (Class<?> clazz : arr$2) {
                    builtInMap.put(clazz.getName(), persister);
                }
                String[] associatedClassNames = persister.getAssociatedClassNames();
                if (associatedClassNames != null) {
                    String[] arr$3 = persister.getAssociatedClassNames();
                    for (String className : arr$3) {
                        builtInMap.put(className, persister);
                    }
                }
            }
        }
    }

    private DataPersisterManager() {
    }

    public static void registerDataPersisters(DataPersister... dataPersisters) {
        List<DataPersister> newList = new ArrayList<>();
        if (registeredPersisters != null) {
            newList.addAll(registeredPersisters);
        }
        for (DataPersister persister : dataPersisters) {
            newList.add(persister);
        }
        registeredPersisters = newList;
    }

    public static void clear() {
        registeredPersisters = null;
    }

    public static DataPersister lookupForField(Field field) {
        if (registeredPersisters != null) {
            for (DataPersister persister : registeredPersisters) {
                if (!persister.isValidForField(field)) {
                    Class<?>[] arr$ = persister.getAssociatedClasses();
                    for (Class<?> clazz : arr$) {
                        if (field.getType() == clazz) {
                            return persister;
                        }
                    }
                } else {
                    return persister;
                }
            }
        }
        DataPersister dataPersister = builtInMap.get(field.getType().getName());
        if (dataPersister != null) {
            return dataPersister;
        }
        if (field.getType().isEnum()) {
            return DEFAULT_ENUM_PERSISTER;
        }
        return null;
    }
}

package com.j256.ormlite.android.apptools;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.DatabaseTableConfigLoader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/* loaded from: classes.dex */
public class OrmLiteConfigUtil {
    protected static final String RAW_DIR_NAME = "raw";
    protected static final String RESOURCE_DIR_NAME = "res";
    protected static int maxFindSourceLevel = 20;
    private static final DatabaseType databaseType = new SqliteAndroidDatabaseType();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Main can take a single file-name argument.");
        }
        writeConfigFile(args[0]);
    }

    public static void writeConfigFile(String fileName) throws SQLException, IOException {
        List<Class<?>> classList = new ArrayList<>();
        findAnnotatedClasses(classList, new File("."), 0);
        writeConfigFile(fileName, (Class[]) classList.toArray(new Class[classList.size()]));
    }

    public static void writeConfigFile(String fileName, Class<?>[] classes) throws SQLException, IOException {
        File rawDir = findRawDir(new File("."));
        if (rawDir == null) {
            System.err.println("Could not find raw directory which is typically in the res directory");
            return;
        }
        File configFile = new File(rawDir, fileName);
        writeConfigFile(configFile, classes);
    }

    public static void writeConfigFile(File configFile) throws SQLException, IOException {
        writeConfigFile(configFile, new File("."));
    }

    public static void writeConfigFile(File configFile, File searchDir) throws SQLException, IOException {
        List<Class<?>> classList = new ArrayList<>();
        findAnnotatedClasses(classList, searchDir, 0);
        writeConfigFile(configFile, (Class[]) classList.toArray(new Class[classList.size()]));
    }

    public static void writeConfigFile(File configFile, Class<?>[] classes) throws SQLException, IOException {
        System.out.println("Writing configurations to " + configFile.getAbsolutePath());
        writeConfigFile(new FileOutputStream(configFile), classes);
    }

    public static void writeConfigFile(OutputStream outputStream, File searchDir) throws SQLException, IOException {
        List<Class<?>> classList = new ArrayList<>();
        findAnnotatedClasses(classList, searchDir, 0);
        writeConfigFile(outputStream, (Class[]) classList.toArray(new Class[classList.size()]));
    }

    public static void writeConfigFile(OutputStream outputStream, Class<?>[] classes) throws SQLException, IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream), 4096);
        try {
            writeHeader(writer);
            for (Class<?> clazz : classes) {
                writeConfigForTable(writer, clazz);
            }
            System.out.println("Done.");
        } finally {
            writer.close();
        }
    }

    protected static File findRawDir(File dir) {
        for (int i = 0; dir != null && i < 20; i++) {
            File rawDir = findResRawDir(dir);
            if (rawDir == null) {
                dir = dir.getParentFile();
            } else {
                return rawDir;
            }
        }
        return null;
    }

    private static void writeHeader(BufferedWriter writer) throws IOException {
        writer.append('#');
        writer.newLine();
        writer.append("# generated on ").append((CharSequence) new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
        writer.newLine();
        writer.append('#');
        writer.newLine();
    }

    private static void findAnnotatedClasses(List<Class<?>> classList, File dir, int level) throws SQLException, IOException {
        File[] arr$ = dir.listFiles();
        for (File file : arr$) {
            if (file.isDirectory()) {
                if (level < maxFindSourceLevel) {
                    findAnnotatedClasses(classList, file, level + 1);
                }
            } else if (file.getName().endsWith(".java")) {
                String packageName = getPackageOfClass(file);
                if (packageName == null) {
                    System.err.println("Could not find package name for: " + file);
                } else {
                    String name = file.getName();
                    String className = packageName + "." + name.substring(0, name.length() - ".java".length());
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (classHasAnnotations(clazz)) {
                            classList.add(clazz);
                        }
                        try {
                            Class<?>[] arr$2 = clazz.getDeclaredClasses();
                            for (Class<?> innerClazz : arr$2) {
                                if (classHasAnnotations(innerClazz)) {
                                    classList.add(innerClazz);
                                }
                            }
                        } catch (Throwable t) {
                            System.err.println("Could not load inner classes for: " + clazz);
                            System.err.println("     " + t);
                        }
                    } catch (Throwable t2) {
                        System.err.println("Could not load class file for: " + file);
                        System.err.println("     " + t2);
                    }
                }
            }
        }
    }

    private static void writeConfigForTable(BufferedWriter writer, Class<?> clazz) throws SQLException, IOException {
        String tableName = DatabaseTableConfig.extractTableName(clazz);
        List<DatabaseFieldConfig> fieldConfigs = new ArrayList<>();
        for (Class<?> working = clazz; working != null; working = working.getSuperclass()) {
            try {
                Field[] arr$ = working.getDeclaredFields();
                for (Field field : arr$) {
                    DatabaseFieldConfig fieldConfig = DatabaseFieldConfig.fromField(databaseType, tableName, field);
                    if (fieldConfig != null) {
                        fieldConfigs.add(fieldConfig);
                    }
                }
            } catch (Error e) {
                System.err.println("Skipping " + clazz + " because we got an error finding its definition: " + e.getMessage());
                return;
            }
        }
        if (fieldConfigs.isEmpty()) {
            System.out.println("Skipping " + clazz + " because no annotated fields found");
            return;
        }
        DatabaseTableConfig<?> tableConfig = new DatabaseTableConfig<>(clazz, tableName, fieldConfigs);
        DatabaseTableConfigLoader.write(writer, tableConfig);
        writer.append("#################################");
        writer.newLine();
        System.out.println("Wrote config for " + clazz);
    }

    private static boolean classHasAnnotations(Class<?> clazz) {
        while (clazz != null) {
            if (clazz.getAnnotation(DatabaseTable.class) != null) {
                return true;
            }
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getAnnotation(DatabaseField.class) != null || field.getAnnotation(ForeignCollectionField.class) != null) {
                        return true;
                    }
                }
                try {
                    clazz = clazz.getSuperclass();
                } catch (Throwable t) {
                    System.err.println("Could not get super class for: " + clazz);
                    System.err.println("     " + t);
                    return false;
                }
            } catch (Throwable t2) {
                System.err.println("Could not load get delcared fields from: " + clazz);
                System.err.println("     " + t2);
                return false;
            }
        }
        return false;
    }

    private static String getPackageOfClass(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (true) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    if (line.contains("package")) {
                        String[] parts = line.split("[ \t;]");
                        if (parts.length > 1 && parts[0].equals("package")) {
                            return parts[1];
                        }
                    }
                } else {
                    return null;
                }
            } finally {
                reader.close();
            }
        }
    }

    private static File findResRawDir(File dir) {
        File[] arr$ = dir.listFiles();
        for (File file : arr$) {
            if (file.getName().equals(RESOURCE_DIR_NAME) && file.isDirectory()) {
                File[] rawFiles = file.listFiles(new FileFilter() { // from class: com.j256.ormlite.android.apptools.OrmLiteConfigUtil.1
                    @Override // java.io.FileFilter
                    public boolean accept(File file2) {
                        return file2.getName().equals(OrmLiteConfigUtil.RAW_DIR_NAME) && file2.isDirectory();
                    }
                });
                if (rawFiles.length == 1) {
                    return rawFiles[0];
                }
            }
        }
        return null;
    }
}

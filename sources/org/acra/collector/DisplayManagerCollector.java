package org.acra.collector;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.acra.ACRA;
import org.apache.commons.lang3.ClassUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DisplayManagerCollector {
    static final SparseArray<String> mFlagsNames = new SparseArray<>();
    static final SparseArray<String> mDensities = new SparseArray<>();

    DisplayManagerCollector() {
    }

    public static String collectDisplays(Context ctx) {
        Display[] displays = null;
        StringBuilder result = new StringBuilder();
        if (Compatibility.getAPILevel() < 17) {
            WindowManager windowManager = (WindowManager) ctx.getSystemService("window");
            displays = new Display[]{windowManager.getDefaultDisplay()};
        } else {
            try {
                Object displayManager = ctx.getSystemService((String) ctx.getClass().getField("DISPLAY_SERVICE").get(null));
                Method getDisplays = displayManager.getClass().getMethod("getDisplays", new Class[0]);
                displays = (Display[]) getDisplays.invoke(displayManager, new Object[0]);
            } catch (IllegalAccessException e) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e);
            } catch (IllegalArgumentException e2) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e2);
            } catch (NoSuchFieldException e3) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e3);
            } catch (NoSuchMethodException e4) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e4);
            } catch (SecurityException e5) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e5);
            } catch (InvocationTargetException e6) {
                ACRA.log.w(ACRA.LOG_TAG, "Error while collecting DisplayManager data: ", e6);
            }
        }
        Display[] arr$ = displays;
        for (Display display : arr$) {
            result.append(collectDisplayData(display));
        }
        return result.toString();
    }

    private static Object collectDisplayData(Display display) {
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        StringBuilder result = new StringBuilder();
        result.append(collectCurrentSizeRange(display));
        result.append(collectFlags(display));
        result.append(display.getDisplayId()).append(".height=").append(display.getHeight()).append('\n');
        result.append(collectMetrics(display, "getMetrics"));
        result.append(collectName(display));
        result.append(display.getDisplayId()).append(".orientation=").append(display.getOrientation()).append('\n');
        result.append(display.getDisplayId()).append(".pixelFormat=").append(display.getPixelFormat()).append('\n');
        result.append(collectMetrics(display, "getRealMetrics"));
        result.append(collectSize(display, "getRealSize"));
        result.append(collectRectSize(display));
        result.append(display.getDisplayId()).append(".refreshRate=").append(display.getRefreshRate()).append('\n');
        result.append(collectRotation(display));
        result.append(collectSize(display, "getSize"));
        result.append(display.getDisplayId()).append(".width=").append(display.getWidth()).append('\n');
        result.append(collectIsValid(display));
        return result.toString();
    }

    private static Object collectIsValid(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method isValid = display.getClass().getMethod("isValid", new Class[0]);
            Boolean value = (Boolean) isValid.invoke(display, new Object[0]);
            result.append(display.getDisplayId()).append(".isValid=").append(value).append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static Object collectRotation(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method getRotation = display.getClass().getMethod("getRotation", new Class[0]);
            int rotation = ((Integer) getRotation.invoke(display, new Object[0])).intValue();
            result.append(display.getDisplayId()).append(".rotation=");
            switch (rotation) {
                case 0:
                    result.append("ROTATION_0");
                    break;
                case 1:
                    result.append("ROTATION_90");
                    break;
                case 2:
                    result.append("ROTATION_180");
                    break;
                case 3:
                    result.append("ROTATION_270");
                    break;
                default:
                    result.append(rotation);
                    break;
            }
            result.append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static Object collectRectSize(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method getRectSize = display.getClass().getMethod("getRectSize", Rect.class);
            Rect size = new Rect();
            getRectSize.invoke(display, size);
            result.append(display.getDisplayId()).append(".rectSize=[").append(size.top).append(',').append(size.left).append(',').append(size.width()).append(',').append(size.height()).append(']').append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static Object collectSize(Display display, String methodName) {
        StringBuilder result = new StringBuilder();
        try {
            Method getRealSize = display.getClass().getMethod(methodName, Point.class);
            Point size = new Point();
            getRealSize.invoke(display, size);
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append("=[").append(size.x).append(',').append(size.y).append(']').append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static String collectCurrentSizeRange(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method getCurrentSizeRange = display.getClass().getMethod("getCurrentSizeRange", Point.class, Point.class);
            Point smallest = new Point();
            Point largest = new Point();
            getCurrentSizeRange.invoke(display, smallest, largest);
            result.append(display.getDisplayId()).append(".currentSizeRange.smallest=[").append(smallest.x).append(',').append(smallest.y).append(']').append('\n');
            result.append(display.getDisplayId()).append(".currentSizeRange.largest=[").append(largest.x).append(',').append(largest.y).append(']').append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static String collectFlags(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method getFlags = display.getClass().getMethod("getFlags", new Class[0]);
            int flags = ((Integer) getFlags.invoke(display, new Object[0])).intValue();
            Field[] arr$ = display.getClass().getFields();
            for (Field field : arr$) {
                if (field.getName().startsWith("FLAG_")) {
                    mFlagsNames.put(field.getInt(null), field.getName());
                }
            }
            result.append(display.getDisplayId()).append(".flags=").append(activeFlags(mFlagsNames, flags)).append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static String collectName(Display display) {
        StringBuilder result = new StringBuilder();
        try {
            Method getName = display.getClass().getMethod("getName", new Class[0]);
            String name = (String) getName.invoke(display, new Object[0]);
            result.append(display.getDisplayId()).append(".name=").append(name).append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchMethodException e3) {
        } catch (SecurityException e4) {
        } catch (InvocationTargetException e5) {
        }
        return result.toString();
    }

    private static Object collectMetrics(Display display, String methodName) {
        StringBuilder result = new StringBuilder();
        try {
            Method getMetrics = display.getClass().getMethod(methodName, new Class[0]);
            DisplayMetrics metrics = (DisplayMetrics) getMetrics.invoke(display, new Object[0]);
            Field[] arr$ = DisplayMetrics.class.getFields();
            for (Field field : arr$) {
                if (field.getType().equals(Integer.class) && field.getName().startsWith("DENSITY_") && !field.getName().equals("DENSITY_DEFAULT")) {
                    mDensities.put(field.getInt(null), field.getName());
                }
            }
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".density=").append(metrics.density).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".densityDpi=").append(metrics.getClass().getField("densityDpi")).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append("scaledDensity=x").append(metrics.scaledDensity).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".widthPixels=").append(metrics.widthPixels).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".heightPixels=").append(metrics.heightPixels).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".xdpi=").append(metrics.xdpi).append('\n');
            result.append(display.getDisplayId()).append(ClassUtils.PACKAGE_SEPARATOR_CHAR).append(methodName).append(".ydpi=").append(metrics.ydpi).append('\n');
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchFieldException e3) {
        } catch (NoSuchMethodException e4) {
        } catch (SecurityException e5) {
        } catch (InvocationTargetException e6) {
        }
        return result.toString();
    }

    private static String activeFlags(SparseArray<String> valueNames, int bitfield) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < valueNames.size(); i++) {
            int maskValue = valueNames.keyAt(i);
            int value = bitfield & maskValue;
            if (value > 0) {
                if (result.length() > 0) {
                    result.append('+');
                }
                result.append(valueNames.get(value));
            }
        }
        return result.toString();
    }
}

package com.microsoft.kapp.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
/* loaded from: classes.dex */
public class ViewUtils {
    public static <T> T getValidView(View rootView, int targetViewId, Class<T> viewClass) {
        Validate.notNull(rootView, "rootView");
        Validate.notNull(viewClass, "viewClass");
        T t = (T) rootView.findViewById(targetViewId);
        if (t == null) {
            String message = String.format(Locale.US, "The activity does not contain a child view with the id %d", Integer.valueOf(targetViewId));
            throw new IllegalStateException(message);
        } else if (!viewClass.isInstance(t)) {
            String message2 = String.format(Locale.US, "The child view is not an instance of specified type '%s'", viewClass.getSimpleName());
            throw new IllegalStateException(message2);
        } else {
            return t;
        }
    }

    public static void closeSoftKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openSoftKeyboard(EditText field) {
        if (field != null) {
            field.requestFocus();
            int length = field.getText().toString().length();
            field.setSelection(length);
            InputMethodManager imm = (InputMethodManager) field.getContext().getSystemService("input_method");
            imm.showSoftInput(field, 1);
        }
    }

    public static View findViewByType(View view, Class classType) {
        if (view == null || !(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child.getClass() != classType) {
                View viewFromChild = findViewByType(child, classType);
                if (viewFromChild != null) {
                    return viewFromChild;
                }
            } else {
                return child;
            }
        }
        return null;
    }

    public static Pair<Integer, Integer> getScreenDimensionInPixes(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new Pair<>(Integer.valueOf(width), Integer.valueOf(height));
    }

    public static HashMap<Integer, Integer> getStyleAttributesWithIndex(int[] styleAttributes) {
        Arrays.sort(styleAttributes);
        HashMap<Integer, Integer> customStyleMap = new LinkedHashMap<>();
        for (int i : styleAttributes) {
            customStyleMap.put(Integer.valueOf(i), Integer.valueOf(Arrays.binarySearch(styleAttributes, i)));
        }
        return customStyleMap;
    }
}

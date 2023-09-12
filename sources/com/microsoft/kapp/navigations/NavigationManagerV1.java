package com.microsoft.kapp.navigations;

import android.app.Activity;
import com.microsoft.kapp.diagnostics.Validate;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class NavigationManagerV1 {
    private final List<NavigationCommandV1> mCommands;
    private WeakReference<Activity> mWeakActivity;

    public NavigationManagerV1(Activity activity) {
        Validate.notNull(activity, "activity");
        this.mWeakActivity = new WeakReference<>(activity);
        this.mCommands = new ArrayList();
    }

    public void addNavigationCommand(NavigationCommandV1 command) {
        Validate.notNull(command, "command");
        this.mCommands.add(command);
    }

    public boolean removeNavigationCommand(NavigationCommandV1 command) {
        Validate.notNull(command, "command");
        return this.mCommands.remove(command);
    }

    public List<NavigationCommandV1> getCommands() {
        return Collections.unmodifiableList(this.mCommands);
    }

    public void navigate(int index) {
        Validate.inRange(index, 0, this.mCommands.size(), "index");
        NavigationCommandV1 command = this.mCommands.get(index);
        navigate(command);
    }

    private void navigate(NavigationCommandV1 command) {
        Activity activity;
        Validate.notNull(command, "command");
        if (this.mWeakActivity != null && (activity = this.mWeakActivity.get()) != null) {
            command.navigate(activity);
        }
    }
}

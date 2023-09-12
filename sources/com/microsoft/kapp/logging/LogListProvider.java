package com.microsoft.kapp.logging;

import com.microsoft.kapp.logging.models.LogEntryType;
import java.util.List;
/* loaded from: classes.dex */
public interface LogListProvider {
    List<Logger> provideLoggers(LogEntryType logEntryType);
}

package org.slf4j.helpers;

import org.slf4j.Logger;
import org.slf4j.Marker;
/* loaded from: classes.dex */
public class SubstituteLogger implements Logger {
    private volatile Logger _delegate;
    private final String name;

    public SubstituteLogger(String name) {
        this.name = name;
    }

    @Override // org.slf4j.Logger
    public String getName() {
        return this.name;
    }

    @Override // org.slf4j.Logger
    public boolean isTraceEnabled() {
        return delegate().isTraceEnabled();
    }

    @Override // org.slf4j.Logger
    public void trace(String msg) {
        delegate().trace(msg);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object arg) {
        delegate().trace(format, arg);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object arg1, Object arg2) {
        delegate().trace(format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object... arguments) {
        delegate().trace(format, arguments);
    }

    @Override // org.slf4j.Logger
    public void trace(String msg, Throwable t) {
        delegate().trace(msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isTraceEnabled(Marker marker) {
        return delegate().isTraceEnabled(marker);
    }

    @Override // org.slf4j.Logger
    public void trace(Marker marker, String msg) {
        delegate().trace(marker, msg);
    }

    @Override // org.slf4j.Logger
    public void trace(Marker marker, String format, Object arg) {
        delegate().trace(marker, format, arg);
    }

    @Override // org.slf4j.Logger
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        delegate().trace(marker, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void trace(Marker marker, String format, Object... arguments) {
        delegate().trace(marker, format, arguments);
    }

    @Override // org.slf4j.Logger
    public void trace(Marker marker, String msg, Throwable t) {
        delegate().trace(marker, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isDebugEnabled() {
        return delegate().isDebugEnabled();
    }

    @Override // org.slf4j.Logger
    public void debug(String msg) {
        delegate().debug(msg);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object arg) {
        delegate().debug(format, arg);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object arg1, Object arg2) {
        delegate().debug(format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object... arguments) {
        delegate().debug(format, arguments);
    }

    @Override // org.slf4j.Logger
    public void debug(String msg, Throwable t) {
        delegate().debug(msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isDebugEnabled(Marker marker) {
        return delegate().isDebugEnabled(marker);
    }

    @Override // org.slf4j.Logger
    public void debug(Marker marker, String msg) {
        delegate().debug(marker, msg);
    }

    @Override // org.slf4j.Logger
    public void debug(Marker marker, String format, Object arg) {
        delegate().debug(marker, format, arg);
    }

    @Override // org.slf4j.Logger
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        delegate().debug(marker, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void debug(Marker marker, String format, Object... arguments) {
        delegate().debug(marker, format, arguments);
    }

    @Override // org.slf4j.Logger
    public void debug(Marker marker, String msg, Throwable t) {
        delegate().debug(marker, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isInfoEnabled() {
        return delegate().isInfoEnabled();
    }

    @Override // org.slf4j.Logger
    public void info(String msg) {
        delegate().info(msg);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object arg) {
        delegate().info(format, arg);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object arg1, Object arg2) {
        delegate().info(format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object... arguments) {
        delegate().info(format, arguments);
    }

    @Override // org.slf4j.Logger
    public void info(String msg, Throwable t) {
        delegate().info(msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isInfoEnabled(Marker marker) {
        return delegate().isInfoEnabled(marker);
    }

    @Override // org.slf4j.Logger
    public void info(Marker marker, String msg) {
        delegate().info(marker, msg);
    }

    @Override // org.slf4j.Logger
    public void info(Marker marker, String format, Object arg) {
        delegate().info(marker, format, arg);
    }

    @Override // org.slf4j.Logger
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        delegate().info(marker, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void info(Marker marker, String format, Object... arguments) {
        delegate().info(marker, format, arguments);
    }

    @Override // org.slf4j.Logger
    public void info(Marker marker, String msg, Throwable t) {
        delegate().info(marker, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isWarnEnabled() {
        return delegate().isWarnEnabled();
    }

    @Override // org.slf4j.Logger
    public void warn(String msg) {
        delegate().warn(msg);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object arg) {
        delegate().warn(format, arg);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object arg1, Object arg2) {
        delegate().warn(format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object... arguments) {
        delegate().warn(format, arguments);
    }

    @Override // org.slf4j.Logger
    public void warn(String msg, Throwable t) {
        delegate().warn(msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isWarnEnabled(Marker marker) {
        return delegate().isWarnEnabled(marker);
    }

    @Override // org.slf4j.Logger
    public void warn(Marker marker, String msg) {
        delegate().warn(marker, msg);
    }

    @Override // org.slf4j.Logger
    public void warn(Marker marker, String format, Object arg) {
        delegate().warn(marker, format, arg);
    }

    @Override // org.slf4j.Logger
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        delegate().warn(marker, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void warn(Marker marker, String format, Object... arguments) {
        delegate().warn(marker, format, arguments);
    }

    @Override // org.slf4j.Logger
    public void warn(Marker marker, String msg, Throwable t) {
        delegate().warn(marker, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isErrorEnabled() {
        return delegate().isErrorEnabled();
    }

    @Override // org.slf4j.Logger
    public void error(String msg) {
        delegate().error(msg);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object arg) {
        delegate().error(format, arg);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object arg1, Object arg2) {
        delegate().error(format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object... arguments) {
        delegate().error(format, arguments);
    }

    @Override // org.slf4j.Logger
    public void error(String msg, Throwable t) {
        delegate().error(msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isErrorEnabled(Marker marker) {
        return delegate().isErrorEnabled(marker);
    }

    @Override // org.slf4j.Logger
    public void error(Marker marker, String msg) {
        delegate().error(marker, msg);
    }

    @Override // org.slf4j.Logger
    public void error(Marker marker, String format, Object arg) {
        delegate().error(marker, format, arg);
    }

    @Override // org.slf4j.Logger
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        delegate().error(marker, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void error(Marker marker, String format, Object... arguments) {
        delegate().error(marker, format, arguments);
    }

    @Override // org.slf4j.Logger
    public void error(Marker marker, String msg, Throwable t) {
        delegate().error(marker, msg, t);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubstituteLogger that = (SubstituteLogger) o;
        return this.name.equals(that.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    Logger delegate() {
        return this._delegate != null ? this._delegate : NOPLogger.NOP_LOGGER;
    }

    public void setDelegate(Logger delegate) {
        this._delegate = delegate;
    }
}

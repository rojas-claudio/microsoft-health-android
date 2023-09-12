package com.microsoft.kapp.calendar;

import android.annotation.TargetApi;
import android.util.SparseArray;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
@TargetApi(15)
/* loaded from: classes.dex */
public class CalendarEvent {
    private final Availability mAvailability;
    private final int mColor;
    private final Date mEndDate;
    private final boolean mHasReminder;
    private final boolean mIsAllDay;
    private final boolean mIsCanceled;
    private final String mLocation;
    private final int mReminderMinutes;
    private final Date mStartDate;
    private final String mTitle;

    /* loaded from: classes.dex */
    public enum Availability {
        BUSY(0),
        FREE(1),
        TENTATIVE(2),
        UNKNOWN(3);
        
        private static final SparseArray<Availability> mMapping = new SparseArray<>();
        private final int mValue;

        static {
            Availability[] arr$ = values();
            for (Availability availability : arr$) {
                mMapping.put(availability.value(), availability);
            }
        }

        Availability(int value) {
            this.mValue = value;
        }

        public static Availability valueOf(int value) {
            return (value < 0 || value > 2) ? UNKNOWN : mMapping.get(value);
        }

        public int value() {
            return this.mValue;
        }

        @Override // java.lang.Enum
        public String toString() {
            switch (this) {
                case FREE:
                    return "Free";
                case BUSY:
                    return "Busy";
                case TENTATIVE:
                    return "Tentative";
                default:
                    return "Unknown";
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Availability mAvailability;
        private int mColor;
        private Date mEndDate;
        private boolean mHasReminder;
        private boolean mIsAllDay;
        private boolean mIsCanceled;
        private String mLocation;
        private int mReminderMinutes;
        private Date mStartDate;
        private String mTitle;

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setLocation(String location) {
            this.mLocation = location;
            return this;
        }

        public Builder setStartDate(Date date) {
            this.mStartDate = date;
            return this;
        }

        public Builder setEndDate(Date date) {
            this.mEndDate = date;
            return this;
        }

        public Builder setHasReminder(boolean hasReminder) {
            this.mHasReminder = hasReminder;
            return this;
        }

        public Builder setIsCanceled(boolean isCanceled) {
            this.mIsCanceled = isCanceled;
            return this;
        }

        public Builder setIsAllDay(boolean isAllDay) {
            this.mIsAllDay = isAllDay;
            return this;
        }

        public Builder setReminderMinutes(int reminderMinutes) {
            this.mReminderMinutes = reminderMinutes;
            return this;
        }

        public Builder setColor(int color) {
            this.mColor = color;
            return this;
        }

        public Builder setAvailability(Availability availability) {
            this.mAvailability = availability;
            return this;
        }

        public CalendarEvent Build() {
            return new CalendarEvent(this);
        }
    }

    public CalendarEvent(Builder builder) {
        String str;
        String str2;
        if (builder.mTitle != null) {
            str = builder.mTitle;
        } else {
            str = "";
        }
        this.mTitle = str;
        if (builder.mLocation != null) {
            str2 = builder.mLocation;
        } else {
            str2 = "";
        }
        this.mLocation = str2;
        this.mStartDate = builder.mStartDate;
        this.mEndDate = builder.mEndDate;
        this.mHasReminder = builder.mHasReminder;
        this.mIsAllDay = builder.mIsAllDay;
        this.mReminderMinutes = builder.mReminderMinutes;
        this.mColor = builder.mColor;
        this.mAvailability = builder.mAvailability;
        this.mIsCanceled = builder.mIsCanceled;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public Date getStartDate() {
        return this.mStartDate;
    }

    public Date getEndDate() {
        return this.mEndDate;
    }

    public long getDurationInMinutes() {
        return (this.mEndDate.getTime() - this.mStartDate.getTime()) / DateUtils.MILLIS_PER_MINUTE;
    }

    public boolean getHasReminder() {
        return this.mHasReminder;
    }

    public boolean getIsCanceled() {
        return this.mIsCanceled;
    }

    public boolean getIsAllDay() {
        return this.mIsAllDay;
    }

    public int getReminderMinutes() {
        return this.mReminderMinutes;
    }

    public int getColor() {
        return this.mColor;
    }

    public Availability getAvailability() {
        return this.mAvailability;
    }

    public int hashCode() {
        int result = (this.mAvailability == null ? 0 : this.mAvailability.hashCode()) + 31;
        return (((((((((((((((((result * 31) + this.mColor) * 31) + (this.mEndDate == null ? 0 : this.mEndDate.hashCode())) * 31) + (this.mHasReminder ? 1231 : 1237)) * 31) + (this.mIsAllDay ? 1231 : 1237)) * 31) + (this.mIsCanceled ? 1231 : 1237)) * 31) + (this.mLocation == null ? 0 : this.mLocation.hashCode())) * 31) + this.mReminderMinutes) * 31) + (this.mStartDate == null ? 0 : this.mStartDate.hashCode())) * 31) + (this.mTitle != null ? this.mTitle.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            CalendarEvent other = (CalendarEvent) obj;
            if (this.mAvailability == other.mAvailability && this.mColor == other.mColor) {
                if (this.mEndDate == null) {
                    if (other.mEndDate != null) {
                        return false;
                    }
                } else if (!this.mEndDate.equals(other.mEndDate)) {
                    return false;
                }
                if (this.mHasReminder == other.mHasReminder && this.mIsAllDay == other.mIsAllDay && this.mIsCanceled == other.mIsCanceled) {
                    if (this.mLocation == null) {
                        if (other.mLocation != null) {
                            return false;
                        }
                    } else if (!this.mLocation.equals(other.mLocation)) {
                        return false;
                    }
                    if (this.mReminderMinutes != other.mReminderMinutes) {
                        return false;
                    }
                    if (this.mStartDate == null) {
                        if (other.mStartDate != null) {
                            return false;
                        }
                    } else if (!this.mStartDate.equals(other.mStartDate)) {
                        return false;
                    }
                    return this.mTitle == null ? other.mTitle == null : this.mTitle.equals(other.mTitle);
                }
                return false;
            }
            return false;
        }
        return false;
    }
}

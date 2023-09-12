package com.microsoft.kapp.tasks;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.krestsdk.models.UserDailySummary;
import java.util.List;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class DailySummaryGetTask extends RestQueryTaskBase<List<UserDailySummary>, OnDailySummaryRetrievedListener> {
    private final LocalDate mEndDate;
    private final LocalDate mStartDate;

    /* loaded from: classes.dex */
    public interface OnDailySummaryRetrievedListener extends OnTaskStateChangedListener {
        void onDailySummaryRetrieved(List<UserDailySummary> list);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnDailySummaryRetrievedListener> {
        private LocalDate mEndDate;
        private LocalDate mStartDate;

        public Builder withStartDate(LocalDate startDate) {
            Validate.notNull(startDate, "startDate");
            this.mStartDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            Validate.notNull(endDate, "endDate");
            this.mEndDate = endDate;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public DailySummaryGetTask build() {
            validate();
            return new DailySummaryGetTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (this.mStartDate == null) {
                throw new IllegalStateException("StartDate is not set.");
            }
            if (this.mEndDate == null) {
                throw new IllegalStateException("EndDate is not set.");
            }
            if (this.mStartDate.isAfter(this.mEndDate)) {
                throw new IllegalStateException("StartDate is after EndDate.");
            }
        }
    }

    private DailySummaryGetTask(Builder builder) {
        super(builder);
        this.mStartDate = builder.mStartDate;
        this.mEndDate = builder.mEndDate;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<List<UserDailySummary>> callback) {
        getRestService().getDailySummaries(this.mStartDate, this.mEndDate, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(List<UserDailySummary> userDailySummaries) {
        getListener().onDailySummaryRetrieved(userDailySummaries);
    }
}

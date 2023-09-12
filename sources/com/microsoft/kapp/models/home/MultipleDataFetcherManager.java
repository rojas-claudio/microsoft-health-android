package com.microsoft.kapp.models.home;

import android.os.CountDownTimer;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.utils.MultipleRequestManager;
import java.util.List;
/* loaded from: classes.dex */
public class MultipleDataFetcherManager extends MultipleRequestManager {
    private CountDownTimer mCountDownTimer;
    private List<DataFetcher> mDataFetchers;

    public MultipleDataFetcherManager(int numOfRequestsIssued, MultipleRequestManager.OnRequestCompleteListener onRequestCompleteListener) {
        super(numOfRequestsIssued, onRequestCompleteListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTimeOut(long timeout) {
        if (timeout > 0) {
            this.mCountDownTimer = new CountDownTimer(timeout, timeout) { // from class: com.microsoft.kapp.models.home.MultipleDataFetcherManager.1
                @Override // android.os.CountDownTimer
                public void onTick(long millisUntilFinished) {
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    MultipleDataFetcherManager.this.fetchingFinished(LoadStatus.ERROR);
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDataFetchers(List<DataFetcher> dataFetchers) {
        this.mDataFetchers = dataFetchers;
    }

    public void start() {
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.start();
        }
        for (DataFetcher fetcher : this.mDataFetchers) {
            fetcher.fetch(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.utils.MultipleRequestManager
    public void fetchingFinished(LoadStatus loadStatus) {
        super.fetchingFinished(loadStatus);
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private List<DataFetcher> mFetchers;
        private MultipleRequestManager.OnRequestCompleteListener mOnRequestCompleteListener;
        private int mNumOfFetchers = 0;
        private long mTimeOut = 0;

        public Builder(MultipleRequestManager.OnRequestCompleteListener onRequestCompleteListener) {
            this.mOnRequestCompleteListener = onRequestCompleteListener;
        }

        public Builder withTimeOut(long timeout) {
            this.mTimeOut = timeout;
            return this;
        }

        public Builder withFetchers(List<DataFetcher> dataFetchers) {
            Validate.notNullOrEmpty(dataFetchers, "dataFetchers");
            this.mFetchers = dataFetchers;
            this.mNumOfFetchers = dataFetchers.size();
            return this;
        }

        public MultipleDataFetcherManager build() {
            MultipleDataFetcherManager manager = new MultipleDataFetcherManager(this.mNumOfFetchers, this.mOnRequestCompleteListener);
            manager.setDataFetchers(this.mFetchers);
            manager.setTimeOut(this.mTimeOut);
            return manager;
        }
    }
}

package com.microsoft.band.webtiles;

import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.tiles.pages.PageData;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class WebTileRefreshResult {
    private boolean mClearPage;
    private NotificationGenericDialog mDialog;
    private List<PageData> mPageDatas = new ArrayList();
    private boolean mSendAsMessage;

    public List<PageData> getPageDatas() {
        return this.mPageDatas;
    }

    public void setPageDatas(List<PageData> pageDatas) {
        this.mPageDatas = pageDatas;
    }

    public void addPageData(PageData pageData) {
        this.mPageDatas.add(pageData);
    }

    public NotificationGenericDialog getDialog() {
        return this.mDialog;
    }

    public void setDialog(NotificationGenericDialog dialog) {
        this.mDialog = dialog;
    }

    public boolean isClearPage() {
        return this.mClearPage;
    }

    public void setClearPage(boolean clearPage) {
        this.mClearPage = clearPage;
    }

    public boolean isSendAsMessage() {
        return this.mSendAsMessage;
    }

    public void setSendAsMessage(boolean sendAsMessage) {
        this.mSendAsMessage = sendAsMessage;
    }
}

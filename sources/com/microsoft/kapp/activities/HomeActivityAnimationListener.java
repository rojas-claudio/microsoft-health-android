package com.microsoft.kapp.activities;
/* loaded from: classes.dex */
public interface HomeActivityAnimationListener {
    void animateMainViewContainer(float f, int i, int i2, Runnable runnable);

    void animateTopMenu(float f, int i, int i2, Runnable runnable);

    void expandMainContentToFullHeight(int i);

    int getMainContentHeight();

    int getTopMenuHeight();

    void invalidateAllViews();

    void moveMainViewContainerToY(int i);

    void moveTopMenuToY(int i);

    void revertMainContentHeight(int i);

    void setMainContentContainerY(float f);

    void setTopMenuY(float f);
}

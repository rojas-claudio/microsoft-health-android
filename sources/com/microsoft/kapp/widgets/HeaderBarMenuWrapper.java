package com.microsoft.kapp.widgets;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class HeaderBarMenuWrapper extends HeaderBarWrapper {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    public HeaderBarMenuWrapper(Activity activity) {
        super(activity);
    }

    public ListView getDrawerListView() {
        return this.mDrawerListView;
    }

    public void openDrawer() {
        this.mDrawerLayout.openDrawer(this.mDrawerListView);
    }

    public void closeDrawer() {
        this.mDrawerLayout.closeDrawer(this.mDrawerListView);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.widgets.HeaderBarWrapper
    public void initializeDecorView() {
        super.initializeDecorView();
        View decorWindow = getDecorView();
        this.mDrawerLayout = (DrawerLayout) ViewUtils.getValidView(decorWindow, R.id.window_drawer_layout, DrawerLayout.class);
        this.mDrawerListView = (ListView) ViewUtils.getValidView(decorWindow, R.id.window_drawer_list_view, ListView.class);
        getHeaderBar().setLeftButtonOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.widgets.HeaderBarMenuWrapper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                HeaderBarMenuWrapper.this.openDrawer();
            }
        });
    }

    @Override // com.microsoft.kapp.widgets.HeaderBarWrapper
    protected int getLayoutResourceId() {
        return R.layout.screen_header_bar_menu;
    }
}

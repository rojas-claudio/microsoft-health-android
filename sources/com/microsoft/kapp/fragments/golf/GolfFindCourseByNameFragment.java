package com.microsoft.kapp.fragments.golf;

import android.widget.EditText;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.SearchBaseFragment;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class GolfFindCourseByNameFragment extends SearchBaseFragment {
    private final String TAG = getClass().getSimpleName();
    private GolfFindCourseByNameListener mListener;

    /* loaded from: classes.dex */
    public interface GolfFindCourseByNameListener {
        void setSearchTerms(String str);
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment
    protected void setSearchBoxHint(EditText searchBox) {
        searchBox.setHint(R.string.golf_search_box_hint);
    }

    @Override // android.support.v4.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment
    protected void executeSearchClick() {
        ViewUtils.closeSoftKeyboard(getActivity(), this.mFindIcon);
        if (this.mListener != null) {
            this.mListener.setSearchTerms(this.mSearchBox.getText().toString());
        }
    }

    public void setListener(GolfFindCourseByNameListener listener) {
        this.mListener = listener;
    }

    @Override // com.microsoft.kapp.fragments.SearchBaseFragment
    protected int getLayoutid() {
        return R.layout.golf_find_by_name_fragment;
    }
}

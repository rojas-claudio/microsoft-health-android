package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class SearchBaseFragment extends BaseFragmentWithOfflineSupport {
    protected static final String KEY_SEARCH_ICON_STATE = "IconEnabled";
    protected static final String KEY_SEARCH_STRING = "NameSearch";
    protected TextView mClearSearch;
    protected TextView mFindIcon;
    protected String mSavedSearchString;
    protected EditText mSearchBox;
    @Inject
    SettingsProvider mSettings;
    protected boolean mIsSearchOn = true;
    protected AtomicBoolean mIsSearchInProgress = new AtomicBoolean(false);

    protected abstract void executeSearchClick();

    protected abstract int getLayoutid();

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutid(), container, false);
        this.mSearchBox = (EditText) ViewUtils.getValidView(view, R.id.search_box, EditText.class);
        this.mFindIcon = (TextView) ViewUtils.getValidView(view, R.id.search_icon, TextView.class);
        this.mClearSearch = (TextView) ViewUtils.getValidView(view, R.id.clear_search, TextView.class);
        this.mFindIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SearchBaseFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SearchBaseFragment.this.executeSearchClick();
            }
        });
        setSearchBoxHint(this.mSearchBox);
        this.mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.microsoft.kapp.fragments.SearchBaseFragment.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6 || actionId == 2 || actionId == 5 || actionId == 3 || actionId == 4) {
                    SearchBaseFragment.this.mSavedSearchString = SearchBaseFragment.this.mSearchBox.getText().toString();
                    if (SearchBaseFragment.this.mSavedSearchString.length() > 0) {
                        SearchBaseFragment.this.executeSearchClick();
                    }
                    return true;
                }
                return false;
            }
        });
        this.mClearSearch.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SearchBaseFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SearchBaseFragment.this.mIsSearchInProgress.compareAndSet(false, true)) {
                    SearchBaseFragment.this.mSearchBox.setText("");
                    SearchBaseFragment.this.mClearSearch.setVisibility(4);
                    SearchBaseFragment.this.mIsSearchInProgress.set(false);
                    SearchBaseFragment.this.getActivity().onBackPressed();
                }
            }
        });
        this.mSearchBox.addTextChangedListener(new TextWatcher() { // from class: com.microsoft.kapp.fragments.SearchBaseFragment.4
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    SearchBaseFragment.this.mFindIcon.setEnabled(false);
                } else {
                    SearchBaseFragment.this.mFindIcon.setEnabled(true);
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.mSearchBox.requestFocus();
        this.mFindIcon.setEnabled(false);
        return view;
    }

    protected void setSearchBoxHint(EditText searchBox) {
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setState(1234);
        if (savedInstanceState != null) {
            this.mFindIcon.setEnabled(savedInstanceState.getBoolean(KEY_SEARCH_ICON_STATE));
            this.mSavedSearchString = savedInstanceState.getString(KEY_SEARCH_STRING);
            if (this.mSavedSearchString != null && this.mSavedSearchString.length() > 0) {
                this.mSearchBox.setText(this.mSavedSearchString);
                this.mFindIcon.setEnabled(true);
                executeSearchClick();
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_STRING, this.mSavedSearchString);
        outState.putBoolean(KEY_SEARCH_ICON_STATE, this.mFindIcon.isEnabled());
    }
}

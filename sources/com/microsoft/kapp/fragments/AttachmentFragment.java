package com.microsoft.kapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.adapters.AttachmentAdapter;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class AttachmentFragment extends BaseFragment {
    private static final String IMAGE_INTENT_TYPE = "image/*";
    private static final String KEY_SELECTED_URIS = "attachment_uris";
    private static final int REQUEST_ATTACH_IMAGE = 0;
    private AttachmentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Uri> mUris;

    public static AttachmentFragment newInstance() {
        return new AttachmentFragment();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    Uri imageUri = data.getData();
                    this.mAdapter.addItem(imageUri, this.mAdapter.getItemCount());
                    this.mRecyclerView.smoothScrollToPosition(this.mAdapter.getItemCount());
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Uri> uris = new ArrayList<>();
        if (savedInstanceState != null) {
            uris = savedInstanceState.getParcelableArrayList(KEY_SELECTED_URIS);
        }
        View rootView = inflater.inflate(R.layout.attachment_fragment, container, false);
        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.viewRecycler);
        this.mAdapter = new AttachmentAdapter(getActivity(), uris);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), 0, false);
        layoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.registerRecyclerViewForResizeEvents(this.mRecyclerView);
        CustomGlyphView btnAddAttachment = (CustomGlyphView) rootView.findViewById(R.id.btnAdd);
        btnAddAttachment.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.AttachmentFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (AttachmentFragment.this.getAttachmentUris().size() < 5) {
                    Intent imagePickerIntent = new Intent("android.intent.action.PICK");
                    imagePickerIntent.setType(AttachmentFragment.IMAGE_INTENT_TYPE);
                    AttachmentFragment.this.getParentFragment().startActivityForResult(imagePickerIntent, 0);
                    return;
                }
                AttachmentFragment.this.getDialogManager().showDialog(AttachmentFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.feedback_error_5_images), DialogPriority.LOW);
            }
        });
        if (this.mUris != null) {
            Iterator i$ = this.mUris.iterator();
            while (i$.hasNext()) {
                Uri uri = i$.next();
                this.mAdapter.addItem(uri);
            }
        }
        return rootView;
    }

    public void setAttachmentUris(ArrayList<Uri> uris) {
        this.mUris = uris;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(KEY_SELECTED_URIS, this.mAdapter.getItems());
        super.onSaveInstanceState(savedInstanceState);
    }

    public ArrayList<Uri> getAttachmentUris() {
        this.mUris = this.mAdapter.getItems();
        return this.mUris;
    }
}

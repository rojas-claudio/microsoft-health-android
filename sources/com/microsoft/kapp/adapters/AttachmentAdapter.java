package com.microsoft.kapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentViewHolder> {
    private static final int BITMAP_BASE_HEIGHT = 160;
    private static final int BITMAP_BASE_WIDTH = 90;
    private final int mAttachmentImageHeight;
    private final ArrayList<Uri> mAttachmentUris;
    private final Context mContext;
    private final OnAttachmentModifiedListener mOnAttachmentModifiedListener = new OnAttachmentModifiedListener() { // from class: com.microsoft.kapp.adapters.AttachmentAdapter.1
        @Override // com.microsoft.kapp.adapters.AttachmentAdapter.OnAttachmentModifiedListener
        public void onAttachmentRemoved(int position) {
            AttachmentAdapter.this.removeItem(position);
        }
    };
    private final int mTotalAttachmentViewHeight;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public interface OnAttachmentModifiedListener {
        void onAttachmentRemoved(int i);
    }

    public AttachmentAdapter(Context context, ArrayList<Uri> uris) {
        this.mContext = context;
        this.mAttachmentUris = new ArrayList<>(uris);
        this.mAttachmentImageHeight = ((int) context.getResources().getDisplayMetrics().density) * 160;
        int glyphHeight = (int) context.getResources().getDimension(R.dimen.font_size_attachment_glyph);
        int padding = (int) context.getResources().getDimension(R.dimen.padding_attachment_glyph);
        this.mTotalAttachmentViewHeight = this.mAttachmentImageHeight + glyphHeight + padding;
    }

    public int getAttachmentHeight() {
        return this.mTotalAttachmentViewHeight;
    }

    public void addItem(Uri uri, int position) {
        if (position > getItemCount()) {
            throw new IndexOutOfBoundsException(String.format("Position %d exceed the length of the target list. (List length = %d)", Integer.valueOf(position), Integer.valueOf(getItemCount())));
        }
        this.mAttachmentUris.add(position, uri);
        notifyItemInserted(position);
    }

    public void addItem(Uri uri) {
        this.mAttachmentUris.add(uri);
        notifyItemInserted(this.mAttachmentUris.size() - 1);
    }

    public void removeItem(int position) {
        if (this.mAttachmentUris.remove(position) != null) {
            notifyItemRemoved(position);
        }
    }

    public ArrayList<Uri> getItems() {
        return this.mAttachmentUris;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mAttachmentUris.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public AttachmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View attachmentView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attachment_list_item, viewGroup, false);
        return new AttachmentViewHolder(attachmentView, this.mOnAttachmentModifiedListener);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(AttachmentViewHolder attachmentViewHolder, int i) {
        Uri imageUri = this.mAttachmentUris.get(i);
        PicassoWrapper.with(this.mContext).load(imageUri).resize(0, this.mAttachmentImageHeight).into(attachmentViewHolder.mAttachmentImage);
    }

    public void registerRecyclerViewForResizeEvents(final RecyclerView recyclerView) {
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.microsoft.kapp.adapters.AttachmentAdapter.2
            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                AttachmentAdapter.this.updateRecyclerViewHeight(recyclerView);
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                AttachmentAdapter.this.updateRecyclerViewHeight(recyclerView);
            }
        });
        updateRecyclerViewHeight(recyclerView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRecyclerViewHeight(RecyclerView recyclerView) {
        if (recyclerView != null) {
            int newHeight = 0;
            if (getItemCount() > 0) {
                newHeight = getAttachmentHeight();
            }
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            if (newHeight != params.height) {
                params.height = newHeight;
                recyclerView.setLayoutParams(params);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class AttachmentViewHolder extends RecyclerView.ViewHolder {
        protected final ImageView mAttachmentImage;
        protected final ImageView mRemoveButton;

        public AttachmentViewHolder(View v, final OnAttachmentModifiedListener listener) {
            super(v);
            this.mAttachmentImage = (ImageView) v.findViewById(R.id.imgAttachment);
            this.mRemoveButton = (ImageView) v.findViewById(R.id.btnRemove);
            this.mRemoveButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.adapters.AttachmentAdapter.AttachmentViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onAttachmentRemoved(AttachmentViewHolder.this.getPosition());
                    }
                }
            });
        }
    }
}

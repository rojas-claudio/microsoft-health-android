package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.microsoft.kapp.R;
import com.squareup.picasso.Picasso;
/* loaded from: classes.dex */
public class PicassoImageView extends ImageView {
    public PicassoImageView(Context context) {
        super(context);
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    public PicassoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttributes(attrs);
    }

    private void setAttributes(AttributeSet attributes) {
        TypedArray typedAttributes = getContext().obtainStyledAttributes(attributes, R.styleable.PicassoImageView);
        try {
            int imageResourceId = typedAttributes.getResourceId(0, 0);
            if (imageResourceId > 0) {
                setImageResource(imageResourceId);
            }
        } finally {
            typedAttributes.recycle();
        }
    }

    @Override // android.widget.ImageView
    public void setImageResource(int resourceId) {
        Picasso.with(getContext()).load(resourceId).noFade().fit().into(this);
    }
}

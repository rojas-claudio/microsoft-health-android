package net.hockeyapp.android.views;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class AttachmentListView extends ViewGroup {
    static final /* synthetic */ boolean $assertionsDisabled;
    private int line_height;

    static {
        $assertionsDisabled = !AttachmentListView.class.desiredAssertionStatus();
    }

    public AttachmentListView(Context context) {
        super(context);
    }

    public ArrayList<Uri> getAttachments() {
        ArrayList<Uri> attachments = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            AttachmentView attachmentView = (AttachmentView) getChildAt(i);
            attachments.add(attachmentView.getAttachmentUri());
        }
        return attachments;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!$assertionsDisabled && View.MeasureSpec.getMode(widthMeasureSpec) == 0) {
            throw new AssertionError();
        }
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();
        int height = 0;
        int line_height = 0;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            AttachmentView attachmentView = (AttachmentView) child;
            height = attachmentView.getEffectiveMaxHeight() + attachmentView.getPaddingTop();
            if (child.getVisibility() != 8) {
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                child.measure(View.MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
                int childWidth = child.getMeasuredWidth();
                line_height = Math.max(line_height, child.getMeasuredHeight() + lp.height);
                if (xPos + childWidth > width) {
                    xPos = getPaddingLeft();
                    yPos += line_height;
                }
                xPos += lp.width + childWidth;
            }
        }
        this.line_height = line_height;
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 0) {
            height = yPos + line_height + getPaddingBottom();
        } else if (View.MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && yPos + line_height + getPaddingBottom() < height) {
            height = yPos + line_height + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(1, 1);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewGroup.LayoutParams;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = r - l;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                child.invalidate();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                if (xPos + childWidth > width) {
                    xPos = getPaddingLeft();
                    yPos += this.line_height;
                }
                child.layout(xPos, yPos, xPos + childWidth, yPos + childHeight);
                xPos += lp.width + childWidth + ((AttachmentView) child).getGap();
            }
        }
    }
}

package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.graphics.RectF;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public abstract class FlagPlacementStrategy {
    public static FlagPlacementStrategy NULL = new FlagPlacementStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy.1
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
        }
    };
    public static FlagPlacementStrategy TOP_LEFT_QUADRANT_FLAG_PLACEMENT_STRATEGY = new FlagPlacementStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy.2
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            int pointerHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            float left = placementRect.left + flagTextWidth;
            float top = placementRect.top;
            float right = placementRect.centerX();
            float bottom = placementRect.centerY() - pointerHeight;
            placementRect.set(left, top, right, bottom);
        }
    };
    public static FlagPlacementStrategy TOP_RIGHT_QUADRANT_FLAG_PLACEMENT_STRATEGY = new FlagPlacementStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy.3
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            int pointerHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            float left = placementRect.centerX();
            float top = placementRect.top;
            float right = placementRect.right;
            float bottom = placementRect.centerY() - pointerHeight;
            placementRect.set(left, top, right, bottom);
        }
    };
    public static FlagPlacementStrategy BOTTOM_LEFT_QUADRANT_FLAG_PLACEMENT_STRATEGY = new FlagPlacementStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy.4
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            int pointerHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            float left = placementRect.left + flagTextWidth;
            float top = (placementRect.centerY() - pointerHeight) - flagTextHeight;
            float right = placementRect.centerX();
            float bottom = placementRect.bottom;
            placementRect.set(left, top, right, bottom);
        }
    };
    public static FlagPlacementStrategy BOTTOM_RIGHT_QUADRANT_FLAG_PLACEMENT_STRATEGY = new FlagPlacementStrategy() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy.5
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            int pointerHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            float left = placementRect.centerX();
            float top = (placementRect.centerY() - pointerHeight) - flagTextHeight;
            float right = placementRect.right;
            float bottom = placementRect.bottom;
            placementRect.set(left, top, right, bottom);
        }
    };

    public abstract void updatePlacementRect(RectF rectF, int i, int i2, Resources resources);

    /* loaded from: classes.dex */
    public static class XPositionWithinRangeFlagPlacementStrategy extends FlagPlacementStrategy {
        private final boolean isInRight;
        private boolean isOtherFlagAdjustedToEdge = false;
        private float otherFlagPosition;
        private final float xMax;
        private final float xMin;
        private final float xPosition;

        public XPositionWithinRangeFlagPlacementStrategy(float xPosition, float xMin, float xMax, boolean isInRight, float otherFlagPosition) {
            this.xPosition = xPosition;
            this.xMin = xMin;
            this.xMax = xMax;
            this.isInRight = isInRight;
            this.otherFlagPosition = otherFlagPosition;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            Validate.notNull(resources, "resources");
            placementRect.left = (int) (this.xPosition - (flagTextWidth / 2.0d));
            placementRect.right = placementRect.left + flagTextWidth;
            int padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_sleep_flag_padding);
            float requiredGap = flagTextWidth + padding;
            float halfOfRequiredGap = requiredGap / 2.0f;
            if (this.otherFlagPosition - halfOfRequiredGap < this.xMin) {
                this.otherFlagPosition = this.xMin + halfOfRequiredGap;
                this.isOtherFlagAdjustedToEdge = true;
            } else if (this.otherFlagPosition + halfOfRequiredGap > this.xMax) {
                this.otherFlagPosition = this.xMax - halfOfRequiredGap;
                this.isOtherFlagAdjustedToEdge = true;
            }
            float existingGap = Math.abs(this.otherFlagPosition - this.xPosition);
            float xOffset = requiredGap - existingGap;
            if (!this.isOtherFlagAdjustedToEdge) {
                xOffset /= 2.0f;
            }
            if (existingGap < requiredGap) {
                if (placementRect.left - halfOfRequiredGap >= this.xMin && placementRect.right + halfOfRequiredGap <= this.xMax) {
                    if (this.isInRight) {
                        placementRect.offset(xOffset, 0.0f);
                    } else {
                        placementRect.offset(-xOffset, 0.0f);
                    }
                } else if (placementRect.left < this.xMin) {
                    if (this.isOtherFlagAdjustedToEdge && this.isInRight) {
                        placementRect.offset((this.xMin - placementRect.left) + requiredGap, 0.0f);
                    } else {
                        placementRect.offset(this.xMin - placementRect.left, 0.0f);
                    }
                } else if (placementRect.right > this.xMax) {
                    if (this.isOtherFlagAdjustedToEdge && !this.isInRight) {
                        placementRect.offset((this.xMax - placementRect.right) - requiredGap, 0.0f);
                    } else {
                        placementRect.offset(this.xMax - placementRect.right, 0.0f);
                    }
                } else if (this.otherFlagPosition - halfOfRequiredGap == this.xMin) {
                    placementRect.offset(requiredGap - existingGap, 0.0f);
                } else if (this.otherFlagPosition + halfOfRequiredGap == this.xMax) {
                    placementRect.offset(-(requiredGap - existingGap), 0.0f);
                }
            } else if (placementRect.left < this.xMin) {
                placementRect.offset(this.xMin - placementRect.left, 0.0f);
            } else if (placementRect.right > this.xMax) {
                placementRect.offset(this.xMax - placementRect.right, 0.0f);
            }
            placementRect.top = resources.getDimensionPixelSize(R.dimen.shinobicharts_icon_group_top_margin);
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            placementRect.bottom = placementRect.top + flagTextHeight + triangleHeight;
        }
    }

    /* loaded from: classes.dex */
    public static class XYPositionsWithinRangeFlagPlacementStrategy extends FlagPlacementStrategy {
        private final float x;
        private final float xMax;
        private final float xMin;
        private final float y;
        private final float yMax;
        private final float yMin;

        public XYPositionsWithinRangeFlagPlacementStrategy(float xPixelValue, float yPixelValue, float xMin, float xMax, float yMin, float yMax) {
            this.x = xPixelValue;
            this.y = yPixelValue;
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy
        public void updatePlacementRect(RectF placementRect, int flagTextWidth, int flagTextHeight, Resources resources) {
            int triangleHeight = resources.getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
            if (this.x < this.xMin) {
                placementRect.offset(this.xMin - this.x, 0.0f);
            } else if (this.x + flagTextWidth > this.xMax) {
                float left = placementRect.left;
                float top = (placementRect.centerY() - triangleHeight) - flagTextHeight;
                float right = left + flagTextWidth;
                float bottom = placementRect.bottom;
                placementRect.set(left, top, right, bottom);
            } else {
                placementRect.offset(flagTextWidth, 0.0f);
            }
            if ((this.y - flagTextHeight) - triangleHeight < this.yMax) {
                placementRect.offset(0.0f, this.yMax - ((this.y - flagTextHeight) - triangleHeight));
            } else if (this.y > this.yMin) {
                placementRect.offset(0.0f, this.yMin - this.y);
            }
        }
    }
}

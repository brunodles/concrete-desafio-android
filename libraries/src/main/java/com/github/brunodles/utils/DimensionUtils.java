package com.github.brunodles.utils;

import android.content.res.Resources;
import android.support.annotation.Dimension;
import android.util.TypedValue;

import static android.support.annotation.Dimension.DP;
import static android.support.annotation.Dimension.PX;

/**
 * Created by bruno on 30/10/16.
 */

public final class DimensionUtils {
    private DimensionUtils() {
    }

    /**
     * @return The pixel amount for the dp unit
     */
    @Dimension(unit = PX)
    public static float fromDp(Resources resources, @Dimension(unit = DP) int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.getDisplayMetrics());
    }
}

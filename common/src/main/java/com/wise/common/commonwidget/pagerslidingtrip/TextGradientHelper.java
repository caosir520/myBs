package com.wise.common.commonwidget.pagerslidingtrip;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.wise.common.R;


/**
 * @author jiangyz
 */
public class TextGradientHelper {
    private float mSelectSize = -1;
    private float mUnSelectSize = -1;
    private int mSelectColor = -1;
    private int mUnSelectColor = -1;
    private ColorGradient mGradient;
    private float mDFontFize = -1;

    private boolean isPxSize = false;

    public TextGradientHelper() {
        super();
    }

    public TextGradientHelper(float selectSize, float unSelectSize,
                              int selectColor, int unSelectColor) {
        super();
        updateValues(selectSize, unSelectSize, selectColor, unSelectColor);
    }

    public void updateValues(float selectSize, float unSelectSize,
                             int selectColor, int unSelectColor) {
        setColor(selectColor, unSelectColor);
        setSize(selectSize, unSelectSize);
    }

    public final TextGradientHelper setSize(float selectSize,
                                                  float unSelectSize) {
        isPxSize = false;
        mSelectSize = selectSize;
        mUnSelectSize = unSelectSize;
        mDFontFize = selectSize - unSelectSize;
        return this;
    }

    public final TextGradientHelper setValueFromRes(Context context,
                                                          int selectColorId, int unSelectColorId, int selectSizeId,
                                                          int unSelectSizeId) {
        setColorId(context, selectColorId, unSelectColorId);
        setSizeId(context, selectSizeId, unSelectSizeId);
        return this;
    }

    public final TextGradientHelper setColorId(Context context,
                                                     int selectColorId, int unSelectColorId) {
        Resources res = context.getResources();
        setColor(res.getColor(selectColorId), res.getColor(unSelectColorId));
        return this;
    }

    public final TextGradientHelper setSizeId(Context context,
                                                    int selectSizeId, int unSelectSizeId) {
        Resources res = context.getResources();
        setSize(res.getDimensionPixelOffset(selectSizeId),
                res.getDimensionPixelOffset(unSelectSizeId));
        isPxSize = true;
        return this;
    }

    public final TextGradientHelper setColor(int selectColor,
                                                   int unSelectColor) {
        if(selectColor == mSelectColor && unSelectColor == mUnSelectColor) {
            return this;
        }
        mSelectColor = selectColor;
        mUnSelectColor = unSelectColor;
        mGradient = new ColorGradient(unSelectColor, selectColor, 100);
        return this;
    }

    /**
     * 如果tabItemView 不是目标的TextView，那么你可以重写该方法返回实际要变化的TextView
     * @param tabItemView Indicator的每一项的view
     * @return
     */
    public TextView getTextView(View tabItemView) {
        if (tabItemView instanceof TextView) {
            return (TextView) tabItemView;
        } else {
            return (TextView) tabItemView.findViewById(R.id.tab_text_view);
            // 如果找不到，即返回空
        }
    }

    float fontTemp;
    private boolean blockGradientEffect;

    public void perform(View view, float selectPercent) {
        if(blockGradientEffect || view == null) {
            return;
        }
        TextView selectTextView = getTextView(view);
        if (mSelectColor != -1 && mUnSelectColor != -1) {
            selectTextView.setTextColor(mGradient
                    .getColor((int) (selectPercent * 100)));
        }
        if (mUnSelectSize > 0 && mSelectSize > 0) {
            // 字体变化插值
            fontTemp = getLineInterpoliation(mUnSelectSize, mDFontFize, selectPercent);
            if (isPxSize) {
                selectTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontTemp);
            } else {
                selectTextView.setTextSize(fontTemp);
            }
        }
    }

    private static float getLineInterpoliation(float unSelectSize, float dFontFize, float selectPercent) {
        return unSelectSize + dFontFize * selectPercent;
    }

    /************************* Getters and Setters **************************/

    public boolean isBlockGradientEffect() {
        return blockGradientEffect;
    }

    public void setBlockGradientEffect(boolean blockGradientEffect) {
        this.blockGradientEffect = blockGradientEffect;
    }

}

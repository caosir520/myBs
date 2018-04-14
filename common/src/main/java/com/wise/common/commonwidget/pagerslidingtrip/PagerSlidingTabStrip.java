package com.wise.common.commonwidget.pagerslidingtrip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wise.common.R;
import com.wise.common.commonutils.DisplayUtil;
import com.wise.common.commonutils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @describe 配合ViewPager 使用的indicator，支持ViewPager SCroll时联动
 * @date: 2014-11-14 上午10:38:46 <br/>
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface IconTabProvider {

        public int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};

    // @formatter:on

    @Deprecated
    private LinearLayout.LayoutParams defaultTabLayoutParams;

    @Deprecated
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    /**顶部PageSlidingTabStrip是否允许显示多行文本**/
    private int mEnableMaxLines;

    /**
     * 等宽两种布局
     */
    private LinearLayout.LayoutParams defaultEqualWidthTabLayoutParams;

    private LinearLayout.LayoutParams expandedEqualWidthTabLayoutParams;

    /**
     * 包裹两种布局，但每个子控件的宽度在第一次固定，不再跟随字的变化动态变化。
     * 由于是根据字内容wrap_content,所以并没有一个统一的宽度值，逻辑上无此两个变量。
     */
//	private LinearLayout.LayoutParams defaultFixedWrapWidthTabLayoutParams;
//
//	private LinearLayout.LayoutParams expandedFixedWrapWidthTabLayoutParams;

    private final PageListener pageListener = new PageListener();

    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;

    private ViewPager pager;

    public int getTabCount() {
        return tabCount;
    }

    private int tabCount;

    /** 动态添加tab个数*/
    private int addOthersTabCount = 0;

    private List<String> tabNames ;

    private boolean hasAnotherTab = false;

    private int currentPosition = 0;

    private float currentPositionOffset = 0f;

    private Paint rectPaint;

    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;

    private int underlineColor = 0x1A000000;

    private int dividerColor = 0x1A000000;

    //	private boolean shouldExpand = false;
    public enum Mode {
        EQUAL_WIDTH  // 等宽不平铺
        , EQUAL_WIDTH_EXPAND // 平铺（最少等宽）
        , FIXED_WIDTH // 定宽（wrap_content）
        , FIXED_WIDTH_EXPAND // 平铺（最少定宽）
    }

    // 默认使用等宽、展开
    private Mode mMode = Mode.EQUAL_WIDTH_EXPAND;

    private boolean textAllCaps = true;

    private int scrollOffset = 52;

    private int indicatorHeight = 8;

    private int underlineHeight = 2;

    private int dividerPadding = 12;

    private int tabPadding = 24;

    private float dividerWidth = 0.5f;

    private int tabTextSize = 12;

    private int tabTextColor = 0xFF666666;

    private Typeface tabTypeface = null;

    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private static final int INVALID_RESOURCE_ID = -1;

    private int tabSelectedBackgroundId = INVALID_RESOURCE_ID;
    private int tabSelectedFirstBackgroundId = INVALID_RESOURCE_ID;
    private int tabSelectedLastBackgroundId = INVALID_RESOURCE_ID;
    private int tabDefaultBackgroundId = INVALID_RESOURCE_ID;

    private Locale locale;

    /**
     * 底线是否和问题宽度保持一致
     */
    private boolean shouldScale = false;

    /**
     * 选中tab颜色
     */
    private int selectedtabTextColor = 0xFF666666;

    /**
     * 选中tab字号（in SP）
     */
    private int selectedtabTextSize = 15;


    /**
     * 选中tab位置
     */
    private int selectedtab = 0;

    private TabClickListener mTabClickListener;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
//		tabTextSize 一直就是按SP单位使用的，谁这么封装的？
//		tabTextSize = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = (int) a.getDimension(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        selectedtabTextColor = a.getColor(1, selectedtabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs,
                R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(
                R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
                indicatorColor);
        underlineColor = a.getColor(
                R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
                underlineColor);
        dividerColor = a
                .getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor,
                        dividerColor);
        indicatorHeight = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
                indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
                underlineHeight);
        dividerPadding = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsDividerPadding,
                dividerPadding);
        tabPadding = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
                tabPadding);
        tabBackgroundResId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabBackground,
                tabBackgroundResId);
        boolean shouldExpand = a
                .getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
                        true);
//		if(shouldExpand) {
//			mMode = Mode.EQUAL_WIDTH_EXPAND;
//		} else {
//			mMode = Mode.EQUAL_WIDTH;
//		}
        scrollOffset = a
                .getDimensionPixelSize(
                        R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
                        scrollOffset);
        textAllCaps = a.getBoolean(
                R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        tabSelectedBackgroundId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabSelectedBackground,
                INVALID_RESOURCE_ID);
        tabSelectedBackgroundId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabSelectedBackground,
                INVALID_RESOURCE_ID);
        tabSelectedFirstBackgroundId = a
                .getResourceId(
                        R.styleable.PagerSlidingTabStrip_pstsTabSelectedFirstBackground,
                        INVALID_RESOURCE_ID);
        tabSelectedLastBackgroundId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabSelectedLastBackground,
                INVALID_RESOURCE_ID);
        tabDefaultBackgroundId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabDefaultBackground,
                INVALID_RESOURCE_ID);
        dividerWidth = a.getFloat(R.styleable.PagerSlidingTabStrip_pstsDividerWidth,
                dividerWidth);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        //原来代码:new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 1.0f);  2.6.12 modify by cjx ,修改为WRAP_CONTENT,解决显示“今日特卖”不全的问题
        expandedTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        // 最大字宽和相应生成的布局清零
        clearMaxTextWidthValues();

        boolean isIconTabProvider = pager.getAdapter() instanceof IconTabProvider;

        if (isIconTabProvider) {
            for (int i = 0; i < tabCount; i++) {
                addIconTab(i,
                        ((IconTabProvider) pager.getAdapter())
                                .getPageIconResId(i));
            }
        } else {
            for (int i = 0; i < tabCount; i++) {
                updateTextMaxSize(pager.getAdapter().getPageTitle(i).toString().length());
                updateMaxTextWidth(getTextView4Measure(), pager.getAdapter().getPageTitle(i).toString());
            }
            for (int i = 0; i < tabCount; i++) {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
            LogUtil.d("yang","tabCount--" + tabCount + "addOthersTabCount--" + addOthersTabCount + "--tabNames--" + tabNames);
            if (isHasAnotherTab()&& getTabNames().size() != 0){
                tabCount += addOthersTabCount;
                int otherTabStartPosition = tabCount - addOthersTabCount;
                for (int i = 0; i < addOthersTabCount; i++) {
                    updateTextMaxSize(getTabNames().get(i).length());
                    updateMaxTextWidth(getTextView4Measure(), getTabNames().get(i));
                }

                for (int i = 0 ; i < addOthersTabCount; i++) {
                    addTextTab(otherTabStartPosition + i, getTabNames().get(i));
                }
            }
        }


        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        }

                        currentPosition = pager.getCurrentItem();
                        scrollToChild(currentPosition, 0);
                    }
                });

    }

    private void clearMaxTextWidthValues() {
        mMaxTextWidth = 0;
        defaultEqualWidthTabLayoutParams = null;
        expandedEqualWidthTabLayoutParams = null;
    }

    private int mTextMaxSize = 0;

    private void updateTextMaxSize(int textMaxSize) {
        if (textMaxSize > mTextMaxSize) {
            mTextMaxSize = textMaxSize;
        }
    }

    private TextView mTextView4Measure;

    public TextView getTextView4Measure() {
        if (mTextView4Measure == null) {
            mTextView4Measure = new TextView(getContext());
            mTextView4Measure.setTextSize(Math.max(tabTextSize, selectedtabTextSize));
        }
        return mTextView4Measure;
    }

    private float mMaxTextWidth = 0;

    private void updateMaxTextWidth(TextView textView, CharSequence text) {
        if (text == null) {
            return;
        }
        textView.setText(text);
        float textSize = getTextWidth(mTextView4Measure);
        if (textSize > mMaxTextWidth) {
            mMaxTextWidth = textSize;
        }

    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setId(R.id.tab_text_view);
        addTab(position, tab);
        if (position != tabCount - 1) {
            setTextMaxLine(tab);
        }
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //DebugLog.w("Frong[onTabClick]", "position-->" + position + "; currentPosition-->" + currentPosition);
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextSize(selectedtabTextSize);
                }
                if(position == tabCount-1 && isHasAnotherTab())
                    pager.setCurrentItem(currentPosition);
                else
                    pager.setCurrentItem(position);

                if (mTabClickListener != null) {
                    mTabClickListener.onTabClick(position, currentPosition);
                }
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        switch (mMode) {
            case EQUAL_WIDTH:
                tabsContainer.addView(tab, position, getDefaultEqualWidthTabLayoutParams(null));
                break;
            case EQUAL_WIDTH_EXPAND:
                tabsContainer.addView(tab, position, getExpandedEqualWidthTabLayoutParams(null));
                break;
            case FIXED_WIDTH:
                tabsContainer.addView(tab, position, getDefaultFixedWidthTabLayoutParams(null, fetchTextView(tab)));
                break;
            case FIXED_WIDTH_EXPAND:
                tabsContainer.addView(tab, position, getExpandedFixedWidthTabLayoutParams(null, fetchTextView(tab)));
                break;
        }
    }

    /**
     * 刷新选中和未选中的tab的颜色样式
     */
    private void updateTabStyles() {

        if (isDragging) {
            return;
        }

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                // tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
                if (i != selectedtab)
                    tab.setTextSize(tabTextSize);
                if (selectedtab == i) {
                    tab.setTextColor(selectedtabTextColor);
                    tab.setTextSize(selectedtabTextSize);
                    if (i == 0) {
                        if (tabSelectedFirstBackgroundId != INVALID_RESOURCE_ID) {
                            v.setBackgroundResource(tabSelectedFirstBackgroundId);
                        }
                    } else if (i == tabCount - 1) {
                        if (tabSelectedLastBackgroundId != INVALID_RESOURCE_ID) {
                            v.setBackgroundResource(tabSelectedLastBackgroundId);
                        }
                    } else {
                        if (tabSelectedBackgroundId != INVALID_RESOURCE_ID) {
                            v.setBackgroundResource(tabSelectedBackgroundId);
                        }
                    }
                } else {
                    tab.setTextColor(tabTextColor);
                    if (tabDefaultBackgroundId != INVALID_RESOURCE_ID) {
                        v.setBackgroundResource(tabDefaultBackgroundId);
                    }
                }

                // setAllCaps() is only available from API 14, so the upper case
                // is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString()
                                .toUpperCase(locale));
                    }
                }
            }
        }
    }

    /**
     * 更新由于更改字号或padding而需要更改的变量（渐变效果、宽度）
     */
    private void onUpdateTextSizeOrPadding() {
        mTextGradientHelper.updateValues(selectedtabTextSize, tabTextSize, selectedtabTextColor, tabTextColor);
        updateWidthAndPadding();
    }

    /**
     * 更新由于更改字色而需要更改的变量（渐变效果)
     */
    private void onUpdateTextColor() {
        mTextGradientHelper.updateValues(selectedtabTextSize, tabTextSize, selectedtabTextColor, tabTextColor);
    }

    private void updateWidthAndPadding() {
        // 拿到最大字宽
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            TextView tv = fetchTextView(v);
            if (tv != null) {
                updateMaxTextWidth(tv, tv.getText());
            }
        }

        // 更新padding和宽度
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View v = tabsContainer.getChildAt(i);
            // 重设宽度和padding
            TextView tv = fetchTextView(v);
            if (tv != null) {
                updateMaxTextWidth(tv, tv.getText());
            }
            v.setPadding(tabPadding, tv.getPaddingTop(), tabPadding, tv.getPaddingBottom());
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
            switch (mMode) {
                case EQUAL_WIDTH:
                    v.setLayoutParams(getDefaultEqualWidthTabLayoutParams(lp));
                    break;
                case EQUAL_WIDTH_EXPAND:
                    v.setLayoutParams(getExpandedEqualWidthTabLayoutParams(lp));
                    break;
                case FIXED_WIDTH:
                    v.setLayoutParams(getDefaultFixedWidthTabLayoutParams(lp, tv));
                    break;
                case FIXED_WIDTH_EXPAND:
                    v.setLayoutParams(getExpandedFixedWidthTabLayoutParams(lp, tv));
                    break;
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

//		// draw indicator line
//
		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}
		if (shouldScale)
			canvas.drawRect(lineLeft + tabPadding, height - indicatorHeight,
					lineRight - tabPadding, height, rectPaint);
		else
			canvas.drawRect(lineLeft, height - indicatorHeight, lineRight,
					height, rectPaint);

        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
                height, rectPaint);

        // draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
                    height - dividerPadding, dividerPaint);
        }
    }

    public interface TabClickListener {
        /**
         * @param position
         * @param currentPosition 点击之前的选中item
         */
        void onTabClick(int position, int currentPosition);
    }

    private boolean isDragging;

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            View childView = tabsContainer.getChildAt(position);
            if (childView != null) {
                scrollToChild(position,
                        (int) (positionOffset * childView.getWidth()));

                invalidate();

                if (delegatePageListener != null) {
                    delegatePageListener.onPageScrolled(position,
                            positionOffset, positionOffsetPixels);
                }

                if (mNeedTextGradient && isDragging) {
                    if (mTextGradientHelper != null) {
                        mTextGradientHelper.perform(childView, 1 - positionOffset);
                        int nextPosition = position + 1;
                        if (nextPosition < tabsContainer.getChildCount()) {
                            View nextChildView = tabsContainer.getChildAt(nextPosition);
                            mTextGradientHelper.perform(nextChildView, positionOffset);
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i("sz", "onPageScrollStateChanged state = " + state);
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
                if (isDragging) {
                    isDragging = false;
                }
                updateTabStyles();
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            Log.i("sz", "onPageSelected position = " + position);
            selectedtab = position;
            updateTabStyles();
        }
    }

    public void setTabClickListener(TabClickListener listener) {
        this.mTabClickListener = listener;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
//		this.shouldExpand = shouldExpand;
        if (shouldExpand) {
            mMode = Mode.EQUAL_WIDTH_EXPAND;
        } else {
            mMode = Mode.EQUAL_WIDTH;
        }
        requestLayout();
    }

    public boolean getShouldExpand() {
//		return shouldExpand;
        if (mMode == Mode.EQUAL_WIDTH_EXPAND) {
            return true;
        }
        return false;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public boolean isShouldScale() {
        return shouldScale;
    }

    /**
     * setShouldScale:设置底线是否和文字宽度保持一致 <br/>
     *
     * @param shouldScale
     * @author adison
     */
    public void setShouldScale(boolean shouldScale) {
        this.shouldScale = shouldScale;
        requestLayout();
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
        onUpdateTextSizeOrPadding();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
        onUpdateTextColor();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
        onUpdateTextColor();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    /****************
     * 设置选中颜色
     ******************/
    public void setSelectTextColor(int textColor) {
        this.selectedtabTextColor = textColor;
        updateTabStyles();
        onUpdateTextColor();
    }

    public void setSelectTextColorResource(int resId) {
        this.selectedtabTextColor = getResources().getColor(resId);
        updateTabStyles();
        onUpdateTextColor();
    }

    public int getSelectTextColor() {
        return selectedtabTextColor;
    }

    /**************
     * end
     ******************/

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
        onUpdateTextSizeOrPadding();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    /**
     * 给指定位置的tab设置个背景
     *
     * @param resId
     * @param index
     */
    public void setTabBackgroundByIndex(int resId, int index) {
        View tab = tabsContainer.getChildAt(index);

        if (tab instanceof TextView) {
            ((TextView) tab).setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    getResources().getDrawable(resId),
                    null);
        }
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
        onUpdateTextSizeOrPadding();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        selectedtab = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**************
     * 修复setDrawable带来整体宽度变大的问题区域
     ************/
    public static final float PADDING_FACTOR = .22f;

    /**
     * 带优化显示的setViewPager, 使用默认Padding因数
     **/
    public void setViewPagerWithOptimization(ViewPager pager) {
        this.setViewPagerWithOptimization(pager, PADDING_FACTOR);
    }

    /**
     * 带优化显示的setViewPager
     **/
    public void setViewPagerWithOptimization(ViewPager pager, float paddingFactor) {
        optimizeTabsBeforeSetViewPager(pager.getAdapter(), paddingFactor);
        setViewPager(pager);
    }

    /**
     * 优化PagerSlidingTabStrip的显示
     */
    public void optimizeTabsBeforeSetViewPager(PagerAdapter adapter, float paddingFactor) {
        Activity activity;
        // context校验
        if (getContext() instanceof Activity) {
            activity = ((Activity) getContext());
        } else {
            return;
        }
        // adapter校验
        if (adapter == null || adapter.getCount() <= 0) {
            return;
        }
        // 以控件宽度的paddingFactor倍数为padding，避免自动padding下添加drawableSomeDirection
        // 会把item宽度挤大，导致本来是2-4个满屏，但仍然可以滑动的Bug
        int count = adapter.getCount();
        int itemWidth = (int) (1f * DisplayUtil.getScreenWidth(activity) / count);

        this.setTabPaddingLeftRight((int) (itemWidth * paddingFactor));
    }
    /************** 修复setDrawable带来整体宽度变大的问题区域	 ************/

    /**
     * 设置纵向分割线的宽度
     **/
    public void setDividerWidth(float dividerWidth) {
        this.dividerWidth = dividerWidth;
    }

    /***********************
     * 增加滑动过程渐变动画
     ************************/
    public TextGradientHelper mTextGradientHelper = new TextGradientHelper(selectedtabTextSize, tabTextSize, selectedtabTextColor, tabTextColor);

    boolean mNeedTextGradient = true;

    public boolean isNeedTextGradient() {
        return mNeedTextGradient;
    }

    /**
     * 设置是否需要文字渐变效果
     *
     * @param needTextGradient 默认开启
     */
    public void setNeedTextGradient(boolean needTextGradient) {
        mNeedTextGradient = needTextGradient;
    }

    /**************************
     * 增加等宽布局
     ****************************/
    public LinearLayout.LayoutParams getDefaultEqualWidthTabLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (layoutParams == null) {
            if (defaultEqualWidthTabLayoutParams == null) {
                defaultEqualWidthTabLayoutParams = new LinearLayout.LayoutParams((int) (mMaxTextWidth + tabPadding * 2), ViewGroup.LayoutParams.MATCH_PARENT);
            }
            return defaultEqualWidthTabLayoutParams;
        } else {
            layoutParams.width = (int) mMaxTextWidth + tabPadding * 2;
            return layoutParams;
        }
    }

    public LinearLayout.LayoutParams getExpandedEqualWidthTabLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (layoutParams == null) {
            if (expandedEqualWidthTabLayoutParams == null) {
                expandedEqualWidthTabLayoutParams = new LinearLayout.LayoutParams((int) (mMaxTextWidth + tabPadding * 2), ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            }
            return expandedEqualWidthTabLayoutParams;
        } else {
            layoutParams.width = (int) mMaxTextWidth + tabPadding * 2;
            layoutParams.weight = 1f;
            return layoutParams;
        }
    }

    ;

    /****************************
     * 增加定宽布局
     **************************/
    private LinearLayout.LayoutParams getExpandedFixedWidthTabLayoutParams(LinearLayout.LayoutParams layoutParams, TextView textView) {
        if (layoutParams == null) {
            return new LinearLayout.LayoutParams((int) (getTextWidth(getTextView4Measure(), textView.getText()) + tabPadding * 2), ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        } else {
            layoutParams.width = (int) getTextWidth(textView) + tabPadding * 2;
            return layoutParams;
        }
    }

    private LinearLayout.LayoutParams getDefaultFixedWidthTabLayoutParams(LinearLayout.LayoutParams layoutParams, TextView textView) {
        if (layoutParams == null) {
            return new LinearLayout.LayoutParams((int) (getTextWidth(getTextView4Measure(), textView.getText()) + tabPadding * 2), ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams.width = (int) getTextWidth(textView) + tabPadding * 2;
            return layoutParams;
        }
    }

    /**
     * 获得字宽的工具方法
     *
     * @param view
     * @return
     */
    private static float getTextWidth(TextView view) {
        return view.getPaint().measureText(view.getText().toString());
    }

    /**
     * 获得字宽的工具方法
     *
     * @param view
     * @return
     */
    private static float getTextWidth(TextView view, CharSequence text) {
        return view.getPaint().measureText(text.toString());
    }

    /**
     * 找到tabs控件中的某个tab上TextView
     *
     * @param v
     * @return
     */
    private static TextView fetchTextView(View v) {
        if (v instanceof TextView) {
            return (TextView) v;
        } else {
            TextView tv = (TextView) v.findViewById(R.id.tab_text_view);
            return tv;
        }
    }


    public Mode getMode() {
        return mMode;
    }

    /**
     * 设置对文字的适配模式
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        mMode = mode;
    }

    /**
     * 修改顶部pageslidingtabstrip,支持多行显示
     **/
    public void setTextMaxLine(TextView tv) {
        if (mEnableMaxLines > 0) {
            tv.setMaxLines(mEnableMaxLines);
        }
    }

    public void setEnableMaxLines(int enableMaxLines) {
        this.mEnableMaxLines = enableMaxLines;
    }

    public void setTabSelectedBackgroundId(int tabSelectedBackgroundId) {
        this.tabSelectedBackgroundId = tabSelectedBackgroundId;
    }

    public void setTabSelectedFirstBackgroundId(int tabSelectedFirstBackgroundId) {
        this.tabSelectedFirstBackgroundId = tabSelectedFirstBackgroundId;
    }

    public void setTabSelectedLastBackgroundId(int tabSelectedLastBackgroundId) {
        this.tabSelectedLastBackgroundId = tabSelectedLastBackgroundId;
    }

    public int getAddOthersTabCount() {
        return addOthersTabCount;
    }

    public void setAddOthersTabCount(int addAnotherTabCount) {
        this.addOthersTabCount = addAnotherTabCount;
    }
    public boolean isHasAnotherTab() {
        return addOthersTabCount != 0;
    }

    public List<String> getTabNames() {
        if (tabNames == null)
            tabNames = new ArrayList<String>();
        return tabNames;
    }

    public void setAnotherTabNames(List<String> tabNames) {
        this.tabNames = tabNames;
    }

    public int getSelectedtabTextSize() {
        return selectedtabTextSize;
    }

    public void setSelectedtabTextSize(int selectedtabTextSize) {
        this.selectedtabTextSize = selectedtabTextSize;
    }
}

package com.wise.common.baseadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;

    protected List<T> mDatas;

    protected int[] mLayoutIds;

    protected int mColCount;

    public CommonAdapter(@NonNull Context context, @NonNull List<T> datas, int[] layoutIds) {
        this(context, datas, layoutIds, 1);
    }

    public CommonAdapter(@NonNull Context context, @NonNull List<T> datas, int[] layoutIds, int colCount) {
        mContext = context;
        mDatas = datas;
        mLayoutIds = layoutIds;

        if (colCount <= 0)
            throw new RuntimeException("列数不能小于1！");

        mColCount = colCount;
    }

    public int getColCount() {
        return mColCount;
    }

    /**
     * @return 这里应理解为UI的条目数量
     */
    @Override
    public int getCount() {
        return (int) Math.ceil(getDataCount() * 1.0 / mColCount);
    }

    protected int getDataCount() {
        return mDatas.size();
    }

    /**
     * 如果{@link #mColCount} > 1 的话，返回第0个
     *
     * @param rowIndex
     * @return
     */
    @Override
    public Object getItem(int rowIndex) {
        return getInnerItem(rowIndex * mColCount);
    }

    protected T getInnerItem(int dataIndex) {
        return mDatas.get(dataIndex);
    }

    @Override
    public long getItemId(int rowIndex) {
        return 0;
    }

    /**
     * 如果是最终被{@link AdapterWrapper}或者{@link HeaderFooterAdapter<T>}包装，
     * 那么此处的adapter只要保证返回一个独有的int值即可，不需要限制在[0, getViewTypeCount)之间
     *
     * @param rowIndex
     * @return
     */
    @Override
    public int getItemViewType(int rowIndex) {
        return 0;
    }

    public int getViewTypeCount() {
        return mLayoutIds.length;
    }

    /**
     * 返回创建条目view所需的res
     *
     * @param dataIndex
     * @return
     */
    protected int getInnerItemLayoutId(int dataIndex) {
        if (mLayoutIds.length > 1)
            throw new RuntimeException("please override getInnerItemLayoutId!");

        return mLayoutIds[0];
    }

    @Override
    public View getView(int rowIndex, View convertView, ViewGroup parent) {
        return mColCount == 1 ? getInnerListView(rowIndex, convertView, parent) : getInnerGridView(rowIndex, convertView, parent);
    }

    private View getInnerListView(int rowIndex, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, getInnerItemLayoutId(rowIndex));
        onBindHolder(holder, rowIndex, parent);
        return holder.getItemView();
    }

    private View getInnerGridView(int rowIndex, View convertView, ViewGroup parent) {
        //多列的Listview
        LinearLayout layout = (LinearLayout) convertView;
        if (layout == null) {
            layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setWeightSum(mColCount);
        }

        for (int i = 0; i < mColCount; i++) {
            //如果没有则会返回null
            View child = layout.getChildAt(i);

            boolean dataOutRange = rowIndex * mColCount + i >= getDataCount();
            //超出去的时候，传入一个合法值，多少无所谓，反正最后都不会显示这个View
            ViewHolder holder = ViewHolder.get(mContext, child, parent, getInnerItemLayoutId(dataOutRange ? getDataCount() - 1 : rowIndex * mColCount + i));
            if (!dataOutRange) {
                onBindHolder(holder, rowIndex * mColCount + i, parent);
                holder.getItemView().setVisibility(View.VISIBLE);
            } else {
                holder.getItemView().setVisibility(View.GONE);
            }

            if (child == null) {
                layout.addView(holder.getItemView());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.getItemView().getLayoutParams();
                params.weight = 1f;
                params.width = 0;
//                holder.getItemView().setLayoutParams(params);
            }
        }

        return layout;
    }

    /**
     * 这里可以实现数据的加载，事件的添加，界面的显隐等操作
     *
     * @param holder
     * @param dataIndex 对应的数据的位置 {@link #getInnerItem(int)}
     * @param parent    同{@link #getView(int, View, ViewGroup)}中的parent参数相同，应为ListView本身
     */
    public abstract void onBindHolder(ViewHolder holder, int dataIndex, ViewGroup parent);
}

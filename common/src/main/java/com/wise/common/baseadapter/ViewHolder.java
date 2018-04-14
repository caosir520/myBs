package com.wise.common.baseadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wise.common.R;
import com.wise.common.commonutils.ViewUtils;


public class ViewHolder {
    private SparseArray<View> mViews;
    private View mItemView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mItemView = inflater.inflate(layoutId, parent, false);
        mViews = new SparseArray<View>();

        mItemView.setTag(R.id.item_holder, this);
    }

    public View getItemView() {
        return mItemView;
    }

    public static ViewHolder get(Context context, View itemView, ViewGroup parent, int layoutId) {
        if (itemView == null)
            return new ViewHolder(context, parent, layoutId);
        else
            return (ViewHolder) itemView.getTag(R.id.item_holder);
    }

    public <T extends View> T getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public ViewHolder setText(int id, CharSequence sequence) {
        TextView view = getView(id);
        if (view != null)
            view.setText(sequence);
        return this;
    }

    public ViewHolder setText(int id, int resId) {
        TextView view = getView(id);
        if (view != null)
            view.setText(resId);
        return this;
    }

    public ViewHolder setImageResource(int id, int resId) {
        ImageView iv = getView(id);
        if (iv != null)
            iv.setImageResource(resId);

        return this;
    }

//    public ViewHolder setImageResource(int id, Banner banner) {
//        ImageView iv = getView(id);
//        if (iv != null)
//            YitengImageLoader.getInstance().display(iv, banner.getImgUrl(), R.drawable.loading_banner, 16, 1,
//                    (float) banner.getImgHeight() / banner.getImgWidth());
//        return this;
//    }

    public ViewHolder show(int id) {
        View view = getView(id);
        if (view != null)
            ViewUtils.setGone(view, false);
        return this;
    }

    public ViewHolder hide(int id) {
        View view = getView(id);
        if (view != null)
            ViewUtils.setGone(view, true);
        return this;
    }

}

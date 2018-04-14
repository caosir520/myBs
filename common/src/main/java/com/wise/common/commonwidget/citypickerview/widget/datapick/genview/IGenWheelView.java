package com.wise.common.commonwidget.citypickerview.widget.datapick.genview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface IGenWheelView {
    public View setup(Context context, int position, View convertView, ViewGroup parent, Object data);
}

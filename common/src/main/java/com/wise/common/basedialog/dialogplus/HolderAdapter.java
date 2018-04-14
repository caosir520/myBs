package com.wise.common.basedialog.dialogplus;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

/**
 * @author Orhan Obut
 */
public interface HolderAdapter extends Holder {

  void setAdapter(BaseAdapter adapter);

  void setRecyclerViewAdapter(RecyclerView.Adapter adapter);

  void setOnItemClickListener(OnHolderListener listener);

}

package com.exmaple.myapplication;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchStatus {
    boolean onItemRemove(int position);

    void onSaveItemStatus(RecyclerView.ViewHolder viewHolder);
}
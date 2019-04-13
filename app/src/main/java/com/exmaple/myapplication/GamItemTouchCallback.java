package com.exmaple.myapplication;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class GamItemTouchCallback extends ItemTouchHelper.Callback {
    private final ItemTouchStatus mItemTouchStatus;
    private final int mDefaultScrollX;
    private int mCurrentScrollX;
    private int mCurrentScrollXWhenInactive;
    private float mInitXWhenInactive;
    private boolean mFirstInactive;

    public GamItemTouchCallback(ItemTouchStatus itemTouchStatus, int defaultScrollX) {
        mItemTouchStatus = itemTouchStatus;
        mDefaultScrollX = defaultScrollX;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 向左滑动
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return Integer.MAX_VALUE;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // 首次滑动时，记录下ItemView当前滑动的距离
        if (dX == 0) {
            mCurrentScrollX = viewHolder.itemView.getScrollX();
            mFirstInactive = true;
        }
        if (isCurrentlyActive) { // 手指滑动
            // 基于当前的距离滑动
            viewHolder.itemView.scrollTo(mCurrentScrollX + (int) -dX, 0);
        } else { // 动画滑动
            if (mFirstInactive) {
                mFirstInactive = false;
                mCurrentScrollXWhenInactive = viewHolder.itemView.getScrollX();
                mInitXWhenInactive = dX;
            }
            if (viewHolder.itemView.getScrollX() >= mDefaultScrollX) {
                // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
                viewHolder.itemView.scrollTo(Math.max(mCurrentScrollX + (int) -dX, mDefaultScrollX), 0);
            } else {
                // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                viewHolder.itemView.scrollTo((int) (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive), 0);
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder.itemView.getScrollX() > mDefaultScrollX) {
            viewHolder.itemView.scrollTo(mDefaultScrollX, 0);
        } else if (viewHolder.itemView.getScrollX() < 0) {
            viewHolder.itemView.scrollTo(0, 0);
        }
        mItemTouchStatus.onSaveItemStatus(viewHolder);
    }
}
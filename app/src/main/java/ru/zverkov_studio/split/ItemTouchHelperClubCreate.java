package ru.zverkov_studio.split;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperClubCreate extends ItemTouchHelper.SimpleCallback {

    Drawable background;
    Drawable xMark;
    int xMarkMargin;
    boolean initiated;
    Context mContext;
    RecyclerView mRecyclerView;

    public ItemTouchHelperClubCreate(Context context, RecyclerView recyclerView, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        mContext = context;
        mRecyclerView = recyclerView;
    }

    private void init() {
        xMark = mContext.getResources().getDrawable(R.drawable.ic_right_arrow);
        xMarkMargin = 32;
        background = mContext.getResources().getDrawable(R.drawable.background_delete_club_create);
        initiated = true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int swipedPosition = viewHolder.getAdapterPosition();
        AdapterClubCreate adapter = (AdapterClubCreate) mRecyclerView.getAdapter();
        adapter.remove(swipedPosition);
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // not sure why, but this method get's called for viewholder that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            // not interested in those
            return;
        }

        if (!initiated) {
            init();
        }

        // draw red background
        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getLeft() + xMarkMargin;
        int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}

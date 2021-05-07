package ru.zverkov_studio.split;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ClubRemoveItem extends ItemTouchHelper.SimpleCallback {

    Drawable background;
    Drawable xMark;
    int xMarkMargin;
    boolean initiated;
    Context mContext;
    RecyclerView mRecyclerView;

    public ClubRemoveItem(Context context, RecyclerView recyclerView, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        mContext = context;
        mRecyclerView = recyclerView;
    }

    private void init() {
        xMark = mContext.getResources().getDrawable(R.drawable.ic_trash);
        xMarkMargin = 32;
        background = mContext.getResources().getDrawable(R.drawable.delete_club_item_background);
        initiated = true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        ClubAdapter clubAdapter = (ClubAdapter) recyclerView.getAdapter();
        if (clubAdapter.isUndoOn() && clubAdapter.isPendingRemoval(position)) {
            return 0;
        }
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int swipedPosition = viewHolder.getAdapterPosition();
        ClubAdapter adapter = (ClubAdapter) mRecyclerView.getAdapter();
        boolean undoOn = adapter.isUndoOn();
        if (undoOn) {
            adapter.pendingRemoval(swipedPosition);
            Log.d("myLog", "swipedPosition - " + String.valueOf(swipedPosition));
        } else {
            Log.d("myLog", "swipedPosition - " + String.valueOf(swipedPosition));
            adapter.remove(swipedPosition);
        }
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
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}

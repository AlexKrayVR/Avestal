package yelm.io.avestal.common;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int offsetStart;
    private int offsetEnd;
    private int offset;

    public ItemOffsetDecoration(int offsetStart, int offsetEnd, int offset) {
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = offsetStart;
        }
        if (parent.getChildAdapterPosition(view) == (state.getItemCount() - 1)) {
            outRect.right = offsetEnd;
        }else {
            outRect.right = offset;
        }
    }
}
package yelm.io.avestal.common;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ItemOffsetTopBottomDecoration extends RecyclerView.ItemDecoration {

    private final int offsetTop;
    private final int offsetBottom;

    public ItemOffsetTopBottomDecoration(int offsetTop, int offsetBottom) {
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
                               RecyclerView parent, @NotNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = offsetTop;
        }
        if (parent.getChildAdapterPosition(view) == (state.getItemCount() - 1)) {
            outRect.bottom = offsetBottom;
        }
    }
}
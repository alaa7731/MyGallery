package tigerspike.andorid.mygallery.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alaa.salah on 1/21/2017.
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {



            outRect.top = space / 2;
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space / 2;

    }
}
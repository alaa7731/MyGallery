package tigerspike.andorid.mygallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tigerspike.andorid.mygallery.R;
import tigerspike.andorid.mygallery.activities.ImagesGalleryActivity;
import tigerspike.andorid.mygallery.adapters.viewholders.ImageGalleryViewHolder;
import tigerspike.andorid.mygallery.models.FlickerItem;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class ImageGalleryRecyclerViewAdapter extends RecyclerView.Adapter<ImageGalleryViewHolder> {
    Context context;
    ArrayList<FlickerItem> flickerItems, filteredItems;

    public ImageGalleryRecyclerViewAdapter(Context context, ArrayList<FlickerItem> flickerItems) {
        this.flickerItems = flickerItems;//original data
        filteredItems = flickerItems;//data for search
        this.context = context;
    }

    @Override
    public ImageGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_flicker_image, parent, false);
        return new ImageGalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageGalleryViewHolder holder, int position) {
        FlickerItem flickerItem = getItem(position);


    }

    FlickerItem getItem(int position) {
        return flickerItems.get(position);
    }

    @Override
    public int getItemCount() {
        return filteredItems == null ? 0 : filteredItems.size();
    }


    public void filter(String textToSearch) {
        filteredItems.clear();
        if (TextUtils.isEmpty(textToSearch)) {
            filteredItems = flickerItems;
        } else {
            for (FlickerItem item : flickerItems) {
                if (item.getTags().contains(textToSearch.trim())) {
                    filteredItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}

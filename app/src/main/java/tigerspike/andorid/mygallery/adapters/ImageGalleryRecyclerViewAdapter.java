package tigerspike.andorid.mygallery.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tigerspike.andorid.mygallery.R;
import tigerspike.andorid.mygallery.activities.ImagesGalleryActivity;
import tigerspike.andorid.mygallery.adapters.viewholders.ImageGalleryViewHolder;
import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.sync.DownloadImage;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class ImageGalleryRecyclerViewAdapter extends RecyclerView.Adapter<ImageGalleryViewHolder> {
    Context context;
    ArrayList<FlickerItem> flickerItems;
    ArrayList<FlickerItem> filteredItems;
    OnClick onClick;

    public ImageGalleryRecyclerViewAdapter(Context context, ArrayList<FlickerItem> flickerItems, OnClick onClick) {
        this.flickerItems = flickerItems;//original data
        filteredItems = new ArrayList<>();//data for search
        this.onClick = onClick;
        for (int i = 0; i < flickerItems.size(); i++) {
            filteredItems.add(flickerItems.get(i));
        }
        this.context = context;
    }

    @Override
    public ImageGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_flicker_image, parent, false);
        return new ImageGalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageGalleryViewHolder holder, final int position) {
        FlickerItem flickerItem = getItem(position);

        holder.tvTitle.setText(flickerItem.getTitle().trim());
        holder.tvAuthor.setText(String.format(Locale.US, context.getString(R.string.taken_by),
                getAuthorName(flickerItem.getAuthor().trim())));
        holder.tvDateTake.setText(String.format(Locale.US, context.getString(R.string.taken_on),
                getDateTaken(flickerItem.getDateTaken())));

        downloadImage(flickerItem.getMedia(), holder.ivImage, flickerItem.getDateTaken());

        holder.ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onShareEmail(position);
            }
        });

        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onDownloadClicked(position);
            }
        });
    }

    private String getAuthorName(String author) {
        try {
            int indexStart = author.indexOf("(");
            int indexEnd = author.indexOf(")");
            author = author.substring(indexStart + 1, indexEnd).replaceAll("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    private void downloadImage(String media, ImageView ivDownload, String dateTaken) {
        DownloadImage downloadImage = new DownloadImage(context, ivDownload, dateTaken);
        downloadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, media);
    }

    private String getDateTaken(String dateTaken) {//get formatted Date
//        2017-01-20T01:47:10-08:00"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'-08:00'", Locale.US);
        SimpleDateFormat sdfFormatted = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateTaken));

            return sdfFormatted.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTaken;
    }

    public FlickerItem getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public int getItemCount() {
        return filteredItems == null ? 0 : filteredItems.size();
    }


    public void filter(String textToSearch) {
        if (TextUtils.isEmpty(textToSearch)) {
            filteredItems = new ArrayList<>();//data for search
            for (int i = 0; i < flickerItems.size(); i++) {
                filteredItems.add(flickerItems.get(i));
            }
        } else {
            filteredItems.clear();
            for (FlickerItem item : flickerItems) {
                if (item.getTags().trim().toLowerCase().contains(textToSearch.trim().toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    public interface OnClick {
        void onDownloadClicked(int position);

        void onShareEmail(int position);

        void onOpenInSystem(int position);

    }
}

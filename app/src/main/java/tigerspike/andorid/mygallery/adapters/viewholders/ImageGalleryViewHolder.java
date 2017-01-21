package tigerspike.andorid.mygallery.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tigerspike.andorid.mygallery.R;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class ImageGalleryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle,tvDateTake,tvAuthor;
    public ImageView ivImage,ivShare,ivDownload,ivEmail;

    public ImageGalleryViewHolder(View itemView) {
        super(itemView);

        tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
        tvDateTake= (TextView) itemView.findViewById(R.id.tvDateTake);
        tvAuthor= (TextView) itemView.findViewById(R.id.tvAuthor);

        ivImage= (ImageView) itemView.findViewById(R.id.ivImage);
        ivShare= (ImageView) itemView.findViewById(R.id.ivShare);
        ivEmail= (ImageView) itemView.findViewById(R.id.ivEmail);
        ivDownload= (ImageView) itemView.findViewById(R.id.ivDownload);
    }
}

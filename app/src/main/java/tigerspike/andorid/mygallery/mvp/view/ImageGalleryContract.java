package tigerspike.andorid.mygallery.mvp.view;

import java.util.ArrayList;

import tigerspike.andorid.mygallery.models.FlickerItem;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public interface ImageGalleryContract {

    interface UserAction {
        void getGalleryImages(String url);
    }

    interface View {
        void onImagesLoaded(ArrayList<FlickerItem> flickerItems);

        void onImagesLoadFailed(String errorMessage);
    }
}

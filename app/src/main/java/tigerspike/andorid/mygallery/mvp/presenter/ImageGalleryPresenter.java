package tigerspike.andorid.mygallery.mvp.presenter;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.OnFinishedListener;
import tigerspike.andorid.mygallery.mvp.fetcher.ImageGalleryFetcher;
import tigerspike.andorid.mygallery.mvp.view.ImageGalleryContract;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class ImageGalleryPresenter implements ImageGalleryContract.UserAction {

    ImageGalleryContract.View view;
    ImageGalleryFetcher fetcher;

    public ImageGalleryPresenter(ImageGalleryContract.View view, ImageGalleryFetcher fetcher) {
        this.view = view;
        this.fetcher = fetcher;
    }

    @Override
    public void getGalleryImages(String url) {
        fetcher.getGalleryImages(url, new OnFinishedListener<ArrayList<FlickerItem>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<FlickerItem> data) {
                view.onImagesLoaded(data);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onImagesLoadFailed(errorMessage);
            }
        });
    }
}
